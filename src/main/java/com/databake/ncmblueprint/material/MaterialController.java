package com.databake.ncmblueprint.material;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping(value = "${general.root_uri}/materials")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @GetMapping
    @PreAuthorize("hasAuthority('material:view')")
    Page<Material> getAllMaterial(
                @RequestParam(value = "page") Integer page,
                @RequestParam(value = "size")  Integer size,
                @RequestParam(value = "search",required = false,defaultValue = "")  String search,
                @RequestParam(value = "sort",required = false,defaultValue = "id")  String sort) {
        return  materialService.getMaterial(page,size,search,sort);
    }

    @GetMapping(path = "/material/{id}")
    @PreAuthorize("hasAuthority('material:view')")
    Material getMaterialById(@PathVariable("id") Integer id) {
        return materialService.getMaterialById(id);
    }

    @PostMapping(path = "/material")
    @PreAuthorize("hasAuthority('material:create')")
    Material createNewMaterial(@ModelAttribute @Valid MaterialDTO materialDTO) throws IOException {
        return materialService.createNewMaterial(materialDTO);
    }

    @PutMapping(path = "/material/{id}")
    @PreAuthorize("hasAuthority('material:update')")
    Material updateMaterial(@ModelAttribute @Valid MaterialDTO materialDTO, @PathVariable("id") Integer id) throws IOException {
        return  materialService.updateMaterial(id, materialDTO);
    }

    @DeleteMapping(path = "/material/{id}")
    @PreAuthorize("hasAuthority('material:delete')")
    void deleteMaterial(@PathVariable("id") Integer id) {
        materialService.deleteMaterial(id);
    }
}
