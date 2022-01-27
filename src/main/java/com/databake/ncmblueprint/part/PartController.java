package com.databake.ncmblueprint.part;

import com.databake.ncmblueprint.material.MaterialWithQuantity;
import com.databake.ncmblueprint.utils.FileModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipOutputStream;

import static com.databake.ncmblueprint.utils.FileService.zipFile;

@RestController
@RequestMapping(value = "${general.root_uri}/parts")
public class PartController {

    @Autowired
    private PartService partService;

    @Value("${general.file.part}")
    private String partRootPath;

    @GetMapping
    @PreAuthorize("hasAuthority('part:view')")
    Page<Part> getAllPart(@RequestParam(value = "page") Integer page,
                          @RequestParam(value = "size") Integer size,
                          @RequestParam(value = "search",required = false,defaultValue = "") String search,
                          @RequestParam(value = "types",required = false,defaultValue = "STANDARD_PART,SUB_PART,PART,ASSEMBLY_PART") String types,
                          @RequestParam(value = "sort",required = false,defaultValue = "id") String sort) {
        return  partService.getPart(page,size,search,types,sort);
    }

    @GetMapping(path = "/part/{id}")
    @PreAuthorize("hasAuthority('part:view')")
    Part getPartDetail(@PathVariable("id") Integer id) {
        return partService.getPartDetail(id);
    }

    @GetMapping(path = "/part/part-list/{id}")
    @PreAuthorize("hasAuthority('part:view')")
    Page<PartWithQuantity> getPartsInPart(@PathVariable("id") Integer id,
                              @RequestParam(value = "page") Integer page,
                              @RequestParam(value = "size") Integer size,
                              @RequestParam(value = "search",required = false,defaultValue = "") String search,
                              @RequestParam(value = "sort",required = false,defaultValue = "id") String sort) {
        return partService.getPartsByPart(id,page,size,search,sort);
    }

    @GetMapping(path = "/part/material/{id}")
    @PreAuthorize("hasAuthority('part:view')")
    Page<MaterialWithQuantity> getMaterialPageByPart(@PathVariable("id") Integer id,
                                                 @RequestParam(value = "page") Integer page,
                                                 @RequestParam(value = "size") Integer size,
                                                 @RequestParam(value = "search",required = false,defaultValue = "") String search,
                                                 @RequestParam(value = "sort",required = false,defaultValue = "id") String sort) {
        return partService.getMaterialQuantityByPart(id,page,size,search,sort);
    }

    @GetMapping(path = "/part/{id}/material-list")
    @PreAuthorize("hasAuthority('part:view')")
    List<MaterialWithQuantity> getMaterialListByPart(@PathVariable("id") Integer id) {
        return partService.getMaterialListByPart(id);
    }

    @GetMapping(path = "/part/{id}/files")
    @PreAuthorize("hasAuthority('part:view')")
    List<FileModel> getPartFiles(@PathVariable("id") Integer id) {
        return partService.getPartFiles(id);
    }

    @PostMapping(path = "/part",consumes = { MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE,"application/json" })
    @PreAuthorize("hasAuthority('part:create')")
    Part createNewPart(@ModelAttribute PartDto partDto) throws IOException {
        return partService.createNewPart(partDto);
    }

    @PutMapping(path = "/part/{id}")
    @PreAuthorize("hasAuthority('part:update')")
    Part updatePart(@PathVariable("id") Integer id,
                    @RequestBody UpdatedPartDTO updatedPartDTO) throws JsonProcessingException {
        return  partService.updatePart(id,updatedPartDTO);
    }

    @PutMapping(path = "/part/{id}/files")
    @PreAuthorize("hasAuthority('part:update')")
    List<FileModel> updateFilesPart(@PathVariable("id") Integer id,
                                    @RequestParam(value = "type") String type, 
                                    @RequestParam(value = "fileList") String fileList,
                                    @RequestParam("files") List<MultipartFile> files) throws JsonProcessingException {
       return partService.updatePartFile(id,type,files,fileList);
    }

    @DeleteMapping(path = "/part/{id}")
    @PreAuthorize("hasAuthority('part:delete')")
    void deletePart(@PathVariable("id") Integer id) {
        partService.deletePart(id);
    }

    @GetMapping(value = "part/{id}/zip", produces="application/zip")
    @PreAuthorize("hasAuthority('part:download')")
    void downloadPartFilesZip(@PathVariable("id") Integer id,
                              HttpServletResponse response) throws IOException {
        ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
        Part part = partService.getPartDetail(id);
        if(part.getFolderPath() == null) {
            Files.createDirectories(Paths.get(partRootPath + "/" + part.getId() + "/drawing"));
            part.setFolderPath(part.getId() + "/drawing");
            partService.savePart(part);
        }
        File fileToZip = new File(partRootPath + part.getFolderPath());

        zipFile(fileToZip, fileToZip.getName(), zipOut);

        zipOut.finish();
        zipOut.close();
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + part.getName() + "\"");
    }


}
