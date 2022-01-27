package com.databake.ncmblueprint.material;

import com.databake.ncmblueprint.exception.NotFoundException;
import com.databake.ncmblueprint.utils.FileService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private FileService fileService;

    @Value("${general.image_uri}")
    private String imageUri;

    @Value("${general.file.material}")
    private String materialRootPath;

    Page<Material> getMaterial(Integer page, Integer size,String search, String sort) {
        Sort sortBy = Sort.by(sort);
        PageRequest pageRequest = PageRequest.of(page, size,sortBy);
        Page<Material> materialPage = materialRepository.selectMaterialBysearch(search,pageRequest);
        List<Material> materials = materialPage.getContent();
        materials.forEach(material -> {
            if(material.getImagePath() != null)
            material.setImagePath(imageUri + material.getImagePath());
        });

        return  materialPage;
    }

    Material getMaterialById(Integer id) {
        Optional<Material> optionalMaterial = materialRepository.findById(id);
        if(optionalMaterial.isEmpty()) {
            throw new NotFoundException(
                    "material with id " + id + " not found");
        }
        Material material = optionalMaterial.get();
        if(material.getImagePath() != null)
        material.setImagePath(imageUri + material.getImagePath());

        return material;
    }

    Material createNewMaterial(MaterialDTO materialDTO) throws IOException {
        Material material = new Material();
        material.setName(materialDTO.getMaterialName());
        material.setPrice(materialDTO.getCost());
        material.setUnit(materialDTO.getUnit());
        material.setExpressNo(material.getExpressNo());
        material.setCreated(new Timestamp(System.currentTimeMillis()));
        material.setDescription(materialDTO.getNote());
        material.setExpressNo(materialDTO.getExpressNo());

        if(materialDTO.getImage() != null && !materialDTO.getImage().isEmpty()) {
            String dir = materialRootPath;
            MultipartFile image = materialDTO.getImage();
            String extension = FilenameUtils.getExtension(image.getOriginalFilename());
            String name = String.format("%s.%s", RandomStringUtils.randomAlphanumeric(8), extension);
            fileService.write(image,dir + name);

            material.setImagePath("material/" + name);
        }

        Material savedMaterial = materialRepository.saveAndFlush(material);
        savedMaterial.setImagePath(imageUri + material.getImagePath());
        return savedMaterial;
    }

    Material updateMaterial(Integer id, MaterialDTO materialDTO) throws IOException {
        Optional<Material> optionalMaterial = materialRepository.findById(id);
        if(optionalMaterial.isEmpty()) {
            throw new NotFoundException(
                    "material with id " + id + " not found");
        }

        Material material = optionalMaterial.get();
        material.setName(materialDTO.getMaterialName());
        material.setPrice(materialDTO.getCost());
        material.setUnit(materialDTO.getUnit());
        material.setExpressNo(material.getExpressNo());
        material.setDescription(materialDTO.getNote());
        material.setUpdated(new Timestamp(System.currentTimeMillis()));
        material.setExpressNo(materialDTO.getExpressNo());

        if(materialDTO.getImage() != null) {
            String oldFilePath = "/data/" + material.getImagePath();
            Files.deleteIfExists(Paths.get(oldFilePath));
            String dir = materialRootPath;
            MultipartFile image = materialDTO.getImage();
            String extension = FilenameUtils.getExtension(image.getOriginalFilename());
            String name = String.format("%s.%s", RandomStringUtils.randomAlphanumeric(8), extension);
            fileService.write(image,dir + name);

            material.setImagePath("material/" + name);
        }

        Material savedMaterial = materialRepository.saveAndFlush(material);
        savedMaterial.setImagePath(imageUri + material.getImagePath());

        return savedMaterial;
    }

    void deleteMaterial(Integer id) {
        Optional<Material> optionalMaterial = materialRepository.findById(id);
        if(optionalMaterial.isEmpty()) {
            throw new NotFoundException(
                    "material with id " + id + " not found");
        }

        materialRepository.delete(optionalMaterial.get());
    }

}
