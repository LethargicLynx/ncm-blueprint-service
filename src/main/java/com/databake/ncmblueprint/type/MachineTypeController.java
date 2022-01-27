package com.databake.ncmblueprint.type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(originPatterns = "*")
@RequestMapping(value = "${general.root_uri}/machine")
public class MachineTypeController {

    @Autowired
    private MachineTypeService machineTypeService;

    @GetMapping(path = "/types")
    public List<MachineType> getAllMachineTypes() {
        return  machineTypeService.getAllMachineTypes();
    }

}
