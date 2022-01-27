package com.databake.ncmblueprint.type;

import com.databake.ncmblueprint.machine.MachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MachineTypeService {

    @Autowired
    private MachineTypeRepository machineTypeRepository;

    @Autowired
    private MachineRepository machineRepository;

    public List<MachineType> getAllMachineTypes() {
        List<MachineType> machineTypes = machineTypeRepository.findAllMachineType();
        machineTypes.stream().forEach(machineType -> {
            Integer count = machineRepository.countMachineByType(machineType.getId());
            machineType.setMachineCount(count);
        });

        return machineTypes;
    }
}
