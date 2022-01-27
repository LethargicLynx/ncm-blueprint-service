package com.databake.ncmblueprint.part;

import com.databake.ncmblueprint.exception.ApiRequestException;
import com.databake.ncmblueprint.material.Material;
import com.databake.ncmblueprint.material.MaterialRepository;
import com.databake.ncmblueprint.material.MaterialWithQuantity;
import com.databake.ncmblueprint.partmaterial.PartMaterial;
import com.databake.ncmblueprint.partmaterial.PartMaterialId;
import com.databake.ncmblueprint.partmaterial.PartMaterialRepository;
import com.databake.ncmblueprint.partrelation.PartRelation;
import com.databake.ncmblueprint.partrelation.PartRelationRepository;
import com.databake.ncmblueprint.utils.FileModel;
import com.databake.ncmblueprint.utils.FileService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.*;

@Service
public class PartService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private PartRelationRepository partRelationRepository;

    @Autowired
    private PartMaterialRepository partMaterialRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Value("${general.image_uri}")
    private String imageUri;

    @Value("${general.file.part}")
    private String partRootPath;

    @Autowired
    private FileService fileService;

    Page<Part> getPart(Integer page, Integer size, String search,String types,String sort) {
        Sort sortBy = Sort.by(sort);
        PageRequest pageRequest = PageRequest.of(page, size, sortBy);
        List<String> typeList = List.of(types.split(","));
        Page<Part> partPage = partRepository.selectPartBySearchLikeAndTypes(search,typeList,pageRequest);
        List<Part> parts = partPage.getContent();
        parts.forEach(part -> {
            if (part.getImagePath() != null) {
                part.setImagePath(imageUri + part.getImagePath());
            }

            if (part.getDrawingImagePath() != null) {
                List<String> drawingImagePaths = new ArrayList<>();
                for(String imgPath : part.getDrawingImagePath()) {
                    drawingImagePaths.add(imageUri + imgPath);
                }
                part.setDrawingImagePath(drawingImagePaths);
            }
        });

        return partPage;
    }

    Part getPartDetail(Integer id) {
        Optional<Part> optionalPart = partRepository.findById(id);
        if(optionalPart.isEmpty()) {
            throw new ApiRequestException("Part not found with id : " + id);
        }

        Part part = optionalPart.get();
        if (part.getImagePath() != null) {
            part.setImagePath(imageUri + part.getImagePath());
        }

        if (part.getDrawingImagePath() != null) {
            List<String> drawingImagePaths = new ArrayList<>();
            for(String imgPath : part.getDrawingImagePath()) {
                drawingImagePaths.add(imageUri + imgPath);
            }
            part.setDrawingImagePath(drawingImagePaths);
        }

        return part;
    }

    public void savePart(Part part) {
        partRepository.saveAndFlush(part);
    }

    Page<PartWithQuantity> getPartsByPart(Integer id,Integer page, Integer size, String search, String sort) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.*, p_relate.quantity AS quantity ");
        sql.append("FROM parts p LEFT JOIN part_relation p_relate ON p.id = p_relate.part ");
        sql.append("WHERE p_relate.sub_part = :id AND ( ");
        sql.append("lower(p.name) LIKE lower(concat('%', :search, '%')) ");
        sql.append("OR lower(p.drawing_no) LIKE lower(concat('%', :search, '%')) ");
        sql.append("OR  lower(p.drawn) LIKE lower(concat('%', :search, '%')) ");
        sql.append(") ");
        sql.append("ORDER BY :sortBy");

        Query queryMapping = em.createNativeQuery(sql.toString(), Tuple.class);
        queryMapping.setParameter("id", id);
        queryMapping.setParameter("search", search);
        queryMapping.setParameter("sortBy", sortPartWithQuantityProperties(sort));

        queryMapping.setFirstResult(page * size);
        queryMapping.setMaxResults(size);

        List<Tuple> tuples = queryMapping.getResultList();
        List<PartWithQuantity> partWithQuantities = new ArrayList<>();
        for(Tuple tuple : tuples) {
            PartWithQuantity partWithQuantity = new PartWithQuantity();
            partWithQuantity.setId(tuple.get("id", Integer.class));
            partWithQuantity.setPartNo(tuple.get("part_no", String.class));
            partWithQuantity.setExpressNo(tuple.get("express_no", String.class));
            partWithQuantity.setName(tuple.get("name", String.class));
            partWithQuantity.setDescription(tuple.get("description", String.class));
            partWithQuantity.setNote(tuple.get("note", String.class));
            partWithQuantity.setCost(tuple.get("cost", Integer.class));
            partWithQuantity.setDrawingNo(tuple.get("drawing_no", String.class));
            partWithQuantity.setDrawn(tuple.get("drawn", String.class));
            partWithQuantity.setCreated(tuple.get("created", Timestamp.class));
            partWithQuantity.setUpdated(tuple.get("updated", Timestamp.class));
            partWithQuantity.setImagePath(imageUri + tuple.get("image_path", String.class));
            partWithQuantity.setDrawingImagePath(prepend((String[]) tuple.get("drawing_image_path"), imageUri));
            partWithQuantity.setPartType(tuple.get("part_type", String.class));
            partWithQuantity.setQuantity(tuple.get("quantity", Integer.class));

            partWithQuantities.add(partWithQuantity);
        }

        StringBuilder countSql = new StringBuilder();
        countSql.append("SELECT COUNT(p.id) ");
        countSql.append("FROM parts p LEFT JOIN part_relation p_relate ON p.id = p_relate.part ");
        countSql.append("WHERE p_relate.sub_part = :id AND ( ");
        countSql.append("lower(p.name) LIKE lower(concat('%', :search, '%')) ");
        countSql.append("OR lower(p.drawing_no) LIKE lower(concat('%', :search, '%')) ");
        countSql.append("OR  lower(p.drawn) LIKE lower(concat('%', :search, '%')) ");
        countSql.append(") ");

        Query queryCount = em.createNativeQuery(countSql.toString());
        queryCount.setParameter("id", id);
        queryCount.setParameter("search", search);
        BigInteger count = (BigInteger) queryCount.getSingleResult();
        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(partWithQuantities, pageRequest, count.intValue());
    }

    Page<MaterialWithQuantity> getMaterialQuantityByPart(Integer id,Integer page, Integer size, String search, String sort) {
        Optional<Part> optionalPart = partRepository.findById(id);
        if(optionalPart.isEmpty()) {
            throw new ApiRequestException("Part not found with id : " + id);
        }

        String sortBy = sortMaterialWithQuantityProperties(sort);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT mat.* ," +
                " p_mat.quantity*p_relate.quantity AS quantity ");
        sql.append("FROM materials mat LEFT JOIN part_material p_mat ON mat.id = p_mat.material_id ");
        sql.append("LEFT JOIN part_relation p_relate ON p_mat.part_id = p_relate.part ");
        sql.append("WHERE p_relate.sub_part = :id AND ( ");
        sql.append("lower(mat.name) LIKE lower(concat('%', :search, '%')) ");
        sql.append("OR lower(mat.express_no) LIKE lower(concat('%', :search, '%')) ");
        sql.append(") ");
        sql.append("ORDER BY :sortBy");

        Query query = em.createNativeQuery(sql.toString(), Tuple.class);
        query.setParameter("id", id);
        query.setParameter("search", search);
        query.setParameter("sortBy", sortBy);

        query.setFirstResult(page * size);
        query.setMaxResults(size);

        List<Tuple> tuples = query.getResultList();
        List<MaterialWithQuantity> materialWithQuantityList = new ArrayList<>();
        for(Tuple tuple : tuples) {
            MaterialWithQuantity materialWithQuantity = new MaterialWithQuantity();
            materialWithQuantity.setId(tuple.get("id", Integer.class));
            materialWithQuantity.setExpressNo(tuple.get("express_no", String.class));
            materialWithQuantity.setName(tuple.get("name", String.class));
            materialWithQuantity.setDescription(tuple.get("description", String.class));
            materialWithQuantity.setPrice(tuple.get("price", Integer.class));
            materialWithQuantity.setCreated(tuple.get("created", Timestamp.class));
            materialWithQuantity.setUpdated(tuple.get("updated", Timestamp.class));
            materialWithQuantity.setImagePath(imageUri + tuple.get("image_path", String.class));
            materialWithQuantity.setUnit(tuple.get("unit", String.class));
            materialWithQuantity.setQuantity(tuple.get("quantity", Integer.class));

            materialWithQuantityList.add(materialWithQuantity);
        }

        StringBuilder countSql = new StringBuilder();
        countSql.append("SELECT COUNT(mat.id) ");
        countSql.append("FROM materials mat LEFT JOIN part_material p_mat ON mat.id = p_mat.material_id ");
        countSql.append("LEFT JOIN part_relation p_relate ON p_mat.part_id = p_relate.part ");
        countSql.append("WHERE p_relate.sub_part = :id AND ( ");
        countSql.append("lower(mat.name) LIKE lower(concat('%', :search, '%')) ");
        countSql.append("OR lower(mat.express_no) LIKE lower(concat('%', :search, '%')) ");
        countSql.append(") ");
        Query queryCount = em.createNativeQuery(countSql.toString());
        queryCount.setParameter("id", id);
        queryCount.setParameter("search", search);

        BigInteger count = (BigInteger) queryCount.getSingleResult();
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortBy));
        return new PageImpl<>(materialWithQuantityList, pageRequest, count.intValue());
    }

    List<MaterialWithQuantity> getMaterialListByPart(Integer id) {
        Optional<Part> optionalPart = partRepository.findById(id);
        if(optionalPart.isEmpty()) {
            throw new ApiRequestException("Part not found with id : " + id);
        }

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT mat.* ," +
                " p_mat.quantity*p_relate.quantity AS quantity ");
        sql.append("FROM materials mat LEFT JOIN part_material p_mat ON mat.id = p_mat.material_id ");
        sql.append("LEFT JOIN part_relation p_relate ON p_mat.part_id = p_relate.part ");
        sql.append("WHERE p_relate.sub_part = :id ");

        Query query = em.createNativeQuery(sql.toString(), Tuple.class);
        query.setParameter("id", id);

        List<Tuple> tuples = query.getResultList();
        List<MaterialWithQuantity> materialWithQuantityList = new ArrayList<>();
        for(Tuple tuple : tuples) {
            MaterialWithQuantity materialWithQuantity = new MaterialWithQuantity();
            materialWithQuantity.setId(tuple.get("id", Integer.class));
            materialWithQuantity.setExpressNo(tuple.get("express_no", String.class));
            materialWithQuantity.setName(tuple.get("name", String.class));
            materialWithQuantity.setDescription(tuple.get("description", String.class));
            materialWithQuantity.setPrice(tuple.get("price", Integer.class));
            materialWithQuantity.setCreated(tuple.get("created", Timestamp.class));
            materialWithQuantity.setUpdated(tuple.get("updated", Timestamp.class));
            materialWithQuantity.setImagePath(imageUri + tuple.get("image_path", String.class));
            materialWithQuantity.setUnit(tuple.get("unit", String.class));
            materialWithQuantity.setQuantity(tuple.get("quantity", Integer.class));

            materialWithQuantityList.add(materialWithQuantity);
        }

        return materialWithQuantityList;
    }

    List<FileModel> getPartFiles(Integer id) {
        Optional<Part> optionalPart = partRepository.findById(id);
        if(optionalPart.isEmpty()) {
            throw new ApiRequestException("Part not found with id : " + id);
        }
        List<FileModel> fileModelList = new ArrayList<>();

        Part part = optionalPart.get();
        String folderPath = part.getFolderPath();

        Collection<File> fileCollection = fileService.getListOfAllFiles(folderPath);
        if(fileCollection.isEmpty()) {
            return null;
        } else {
            for (File file : fileCollection) {
                Long fileSize = fileService.getFileSizeNIO(file.getAbsolutePath());
                FileModel fileModel = new FileModel();
                fileModel.setName(file.getName());
                fileModel.setSize(fileSize);

                fileModelList.add(fileModel);
            }
        }

        return fileModelList;
    }

    Part createNewPart(PartDto partDTO) throws IOException {
        String type = partDTO.getType();
        Part part = null;
        switch (type) {
            case "PART" :
                System.out.println("PART");
                part = saveNormalPart(partDTO);
                break;
            case "STANDARD_PART" :
                System.out.println("STANDARD_PART");
                part = saveStandardPart(partDTO);
                break;
            case "SUB_PART" :
            case "ASSEMBLY_PART" :
                part = saveSubPartOrAssemblyPart(partDTO);
                break;
        }

        if (part.getImagePath() != null) {
            part.setImagePath(imageUri + part.getImagePath());
        }

        if (part.getDrawingImagePath() != null) {
            List<String> drawingImagePaths = new ArrayList<>();
            for(String imgPath : part.getDrawingImagePath()) {
                drawingImagePaths.add(imageUri + imgPath);
            }
            part.setDrawingImagePath(drawingImagePaths);
        }

        return part;
    }

    Part updatePart(Integer id, UpdatedPartDTO updatedPartDTO) throws JsonProcessingException {
        Optional<Part> optionalPart = partRepository.findById(id);
        if(optionalPart.isEmpty()) {
            throw new ApiRequestException("Part not found with id : " + id);
        }

        Part part = optionalPart.get();
        part.setName(updatedPartDTO.getPartName());
        part.setDrawingNo(updatedPartDTO.getDwgNo());
        part.setCost(updatedPartDTO.getCost());
        part.setDescription(updatedPartDTO.getDescription());
        part.setNote(updatedPartDTO.getNote());
        part.setPartType(updatedPartDTO.getType());

        if (!StringUtils.isBlank(updatedPartDTO.getMaterialIdQuantityList())) {
            ObjectMapper mapper = new ObjectMapper();
            List<IdQuantity> idQuantityList = mapper.readValue(updatedPartDTO.getMaterialIdQuantityList(), new TypeReference<>() {
            });

            for(IdQuantity idQuantity : idQuantityList) {
                Optional<Material> optionalMaterial = materialRepository.findById(idQuantity.getId());
                if(optionalMaterial.isPresent()) {
                    Material material = optionalMaterial.get();
                    Optional<PartMaterial> optionalPartMaterial = partMaterialRepository.findByPartMaterialId(new PartMaterialId(part.getId(), material.getId()));
                    PartMaterial partMaterial;
                    if(optionalPartMaterial.isEmpty()) {
                        partMaterial = new PartMaterial(new PartMaterialId(part.getId(), material.getId())
                                ,part,material, idQuantity.getQuantity() );
                    } else {
                        partMaterial = optionalPartMaterial.get();
                        partMaterial.setQuantity(idQuantity.getQuantity());
                    }

                    part.addPartMaterial(partMaterial);
                }
            }
        }

        if (!StringUtils.isBlank(updatedPartDTO.getPartIdQuantityList())) {
            ObjectMapper mapper = new ObjectMapper();
            List<IdQuantity> idQuantityList = mapper.readValue(updatedPartDTO.getPartIdQuantityList(), new TypeReference<>() {
            });

            List<PartRelation> partRelationList = new ArrayList<>();
            for(IdQuantity idQuantity : idQuantityList) {
                PartRelation partRelation = null;
                if(updatedPartDTO.getType().equals("SUB_PART")) {
                    Optional<PartRelation> optionalPartRelation = partRelationRepository.findBySubPartIdAndPartId(part.getId(), idQuantity.getId());
                    if(optionalPartRelation.isEmpty()) {
                        partRelation = new PartRelation(null,part.getId(),
                                idQuantity.getId(),idQuantity.getQuantity());
                    } else {
                        partRelation = optionalPartRelation.get();
                        partRelation.setQuantity(idQuantity.getQuantity());
                    }

                } else {
                    Optional<PartRelation> optionalPartRelation = partRelationRepository.findByAssemblyPartIdtAndPartId(part.getId(), idQuantity.getId());
                    if(optionalPartRelation.isEmpty()) {
                        partRelation = new PartRelation(part.getId(),null,
                                idQuantity.getId(),idQuantity.getQuantity());
                    } else {
                        partRelation = optionalPartRelation.get();
                        partRelation.setQuantity(idQuantity.getQuantity());
                    }
                }

                partRelationList.add(partRelation);
            }

            partRelationRepository.saveAll(partRelationList);
        }

        return  partRepository.save(part);
    }

    List<FileModel> updatePartFile(Integer id, String type, List<MultipartFile> files, String fileModels) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<FileModel> fileModelList = new ArrayList<>();
        if (!StringUtils.isBlank(fileModels)) {
            fileModelList = mapper.readValue(fileModels, new TypeReference<>() {});
        }

        return fileModelList;
    }

    void deletePart(Integer id) {
        Optional<Part> optionalPart = partRepository.findById(id);
        if(optionalPart.isEmpty()) {
            throw new ApiRequestException("Part to delete not found with id : " + id);
        }


    }

    private Part saveNormalPart(PartDto partDTO) throws IOException {
        Part part = new Part();
        part.setName(partDTO.getPartName());
        part.setPartNo(partDTO.getDwgNo());
        part.setDrawingNo(partDTO.getDwgNo());
        part.setCost(partDTO.getCost());
        part.setPartType(partDTO.getType());
        part.setDescription(partDTO.getDescription());
        part.setNote(partDTO.getNote());
        part.setCreated(new Timestamp(System.currentTimeMillis()));

        Part savedPart = partRepository.saveAndFlush(part);

        if(partDTO.getImages() != null) {
            List<String> drawingImagePaths = new ArrayList<>();
            String dir = partRootPath + savedPart.getId() + "/";
            Files.createDirectories(Path.of(dir));
            for(MultipartFile image : partDTO.getImages()) {
                String extension = FilenameUtils.getExtension(image.getOriginalFilename());
                String name = String.format("%s.%s", RandomStringUtils.randomAlphanumeric(8), extension);
                fileService.write(image,dir + name);

                drawingImagePaths.add(dir + name);
            }
            savedPart.setImagePath(drawingImagePaths.get(0));
            savedPart.setDrawingImagePath(drawingImagePaths);
        }

        if(partDTO.getFiles() != null) {
            String folderPath = partRootPath + savedPart.getId() + "/drawing_image/";
            Files.createDirectories(Path.of(folderPath));
            for(MultipartFile file : partDTO.getFiles()) {
                String extension = FilenameUtils.getExtension(file.getOriginalFilename());
                String name = String.format("%s.%s", file.getOriginalFilename(), extension);
                fileService.write(file,folderPath + name);
            }

            savedPart.setFolderPath(folderPath);
        }

        if (!StringUtils.isBlank(partDTO.getMaterialIdQuantityList())) {
            ObjectMapper mapper = new ObjectMapper();
            List<IdQuantity> idQuantityList = mapper.readValue(partDTO.getMaterialIdQuantityList(), new TypeReference<>() {
            });

            for(IdQuantity idQuantity : idQuantityList) {
                Optional<Material> optionalMaterial = materialRepository.findById(idQuantity.getId());
                if(optionalMaterial.isPresent()) {
                    Material material = optionalMaterial.get();
                    PartMaterial partMaterial = new PartMaterial(new PartMaterialId(savedPart.getId(), material.getId())
                            ,savedPart,material, idQuantity.getQuantity() );

                    savedPart.addPartMaterial(partMaterial);
                }
            }
        }

        return partRepository.save(savedPart);
    }

    private Part saveStandardPart(PartDto partDTO) throws IOException {
        Part part = new Part();
        part.setName(partDTO.getPartName());
        part.setDrawingNo(partDTO.getDwgNo());
        part.setCost(partDTO.getCost());
        part.setPartType(partDTO.getType());
        part.setDescription(partDTO.getDescription());
        part.setNote(partDTO.getNote());
        part.setCreated(new Timestamp(System.currentTimeMillis()));

        Part savedPart = partRepository.saveAndFlush(part);

        if(partDTO.getImages() != null ) {
            List<String> drawingImagePaths = new ArrayList<>();
            String dir = partRootPath + "/";
            for(MultipartFile image : partDTO.getImages()) {
                String extension = FilenameUtils.getExtension(image.getOriginalFilename());
                String name = String.format("%s.%s", RandomStringUtils.randomAlphanumeric(8), extension);
                fileService.write(image,dir + name);

                drawingImagePaths.add(dir + name);
            }

            savedPart.setImagePath(drawingImagePaths.get(0));
            savedPart.setDrawingImagePath(drawingImagePaths);
        }

        if(partDTO.getFiles() != null) {
            String folderPath = partRootPath + savedPart.getId() + "/drawing_image/";
            Files.createDirectories(Path.of(folderPath));
            for(MultipartFile file : partDTO.getFiles()) {
                String extension = FilenameUtils.getExtension(file.getOriginalFilename());
                String name = String.format("%s.%s", file.getOriginalFilename(), extension);
                fileService.write(file,folderPath + name);
            }

            savedPart.setFolderPath(folderPath);
        }

        return partRepository.saveAndFlush(savedPart);
    }

    private Part saveSubPartOrAssemblyPart(PartDto partDTO) throws IOException {
        Part part = new Part();
        part.setName(partDTO.getPartName());
        part.setDrawingNo(partDTO.getDwgNo());
        part.setCost(partDTO.getCost());
        part.setPartType(partDTO.getType());
        part.setDescription(partDTO.getDescription());
        part.setNote(partDTO.getNote());
        part.setCreated(new Timestamp(System.currentTimeMillis()));

        Part savedPart = partRepository.saveAndFlush(part);

        if(partDTO.getLabelImage() != null) {
            MultipartFile labelImage = partDTO.getLabelImage();
            String dir = partRootPath + "/";
            String extension = FilenameUtils.getExtension(labelImage.getOriginalFilename());
            String name = String.format("%s.%s", RandomStringUtils.randomAlphanumeric(8), extension);
            fileService.write(labelImage,dir + name);

            savedPart.setLabelImagePath(dir + name);
        }

        if(partDTO.getImages() != null) {
            List<String> drawingImagePaths = new ArrayList<>();
            String dir = partRootPath + "/";
            for(MultipartFile image : partDTO.getImages()) {
                String extension = FilenameUtils.getExtension(image.getOriginalFilename());
                String name = String.format("%s.%s", RandomStringUtils.randomAlphanumeric(8), extension);
                fileService.write(image,dir + name);

                drawingImagePaths.add(dir + name);
            }

            savedPart.setImagePath(drawingImagePaths.get(0));
            savedPart.setDrawingImagePath(drawingImagePaths);
        }

        if(partDTO.getFiles() != null) {
            String folderPath = partRootPath + savedPart.getId() + "/drawing_image/";
            Files.createDirectories(Path.of(folderPath));
            for(MultipartFile file : partDTO.getFiles()) {
                String extension = FilenameUtils.getExtension(file.getOriginalFilename());
                String name = String.format("%s.%s", file.getOriginalFilename(), extension);
                fileService.write(file,folderPath + name);
            }

            savedPart.setFolderPath(folderPath);
        }

        if (!StringUtils.isBlank(partDTO.getPartIdQuantityList())) {
            ObjectMapper mapper = new ObjectMapper();
            List<IdQuantity> idQuantityList = mapper.readValue(partDTO.getPartIdQuantityList(), new TypeReference<>() {
            });

            List<PartRelation> partRelationList = new ArrayList<>();
            for(IdQuantity idQuantity : idQuantityList) {
                PartRelation partRelation = null;
                if(partDTO.getType().equals("SUB_PART")) {
                    partRelation = new PartRelation(null,savedPart.getId(),
                            idQuantity.getId(),idQuantity.getQuantity());
                } else {
                    partRelation = new PartRelation(savedPart.getId(),null,
                            idQuantity.getId(),idQuantity.getQuantity());
                }

                partRelationList.add(partRelation);
            }

            partRelationRepository.saveAll(partRelationList);
        }

        return partRepository.saveAndFlush(savedPart);
    }


    private String sortPartWithQuantityProperties(String sort) {
        String sortType = "p.id";
        if(sort.equals("partNo")) {
            sortType = "p.part_no";
        } else if(sort.equals("name")) {
            sortType = "p.name";
        } else if(sort.equals("cost")) {
            sortType = "p.cost";
        } else if(sort.equals("drawingNo")) {
            sortType = "p.drawing_no";
        } else if(sort.equals("drawn")) {
            sortType = "p.drawn";
        } else if(sort.equals("created")) {
            sortType = "p.created";
        } else if(sort.equals("updated")) {
            sortType = "p.updated";
        } else if(sort.equals("quantity")) {
            sortType = "quantity";
        }

        return sortType;
    }

    private String sortMaterialWithQuantityProperties(String sort) {
        String sortType = "mat.id";
        if(sort.equals("expressNo")) {
            sortType = "mat.express_no";
        } else if(sort.equals("name")) {
            sortType = "mat.name";
        } else if(sort.equals("description")) {
            sortType = "mat.description";
        } else if(sort.equals("price")) {
            sortType = "mat.price";
        } else if(sort.equals("created")) {
            sortType = "mat.created";
        } else if(sort.equals("updated")) {
            sortType = "mat.updated";
        } else if(sort.equals("quantity")) {
            sortType = "quantity";
        }

        return sortType;
    }

    private String[] prepend(String[] input, String prepend) {
        String[] output = new String[input.length];
        for (int index = 0; index < input.length; index++) {
            output[index] = "" + prepend + input[index];
        }
        return output;
    }

}
