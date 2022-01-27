package com.databake.ncmblueprint.machine;

import com.databake.ncmblueprint.exception.NotFoundException;
import com.databake.ncmblueprint.machinepart.MachinePart;
import com.databake.ncmblueprint.machinepart.MachinePartId;
import com.databake.ncmblueprint.machinepart.MachinePartRepository;
import com.databake.ncmblueprint.material.Material;
import com.databake.ncmblueprint.material.MaterialRepository;
import com.databake.ncmblueprint.part.Part;
import com.databake.ncmblueprint.part.PartRepository;
import com.databake.ncmblueprint.partmaterial.PartMaterial;
import com.databake.ncmblueprint.partmaterial.PartMaterialId;
import com.databake.ncmblueprint.partmaterial.PartMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
class MachineService {

    @Autowired
    private MachineRepository machineRepository;

    @Autowired
    private MachinePartRepository machinePartRepository;

    @Autowired
    private PartMaterialRepository partMaterialRepository;

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Value("${general.image_uri}")
    private String imageUri;

    Page<Machine> getMachine(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Machine> machinePage = machineRepository.findAll(pageRequest);
        List<Machine> machines = machinePage.getContent();
        machines.forEach(machine -> {
            if (machine.getImagePath() != null) {
                machine.setImagePath(imageUri + machine.getImagePath());
            }

            if (machine.getLabelImagePath() != null) {
                machine.setLabelImagePath(imageUri + machine.getLabelImagePath());
            }

            if (machine.getDrawingImagePath() != null) {
                List<String> drawingImagePaths = new ArrayList<>();
                for(String imgPath : machine.getDrawingImagePath()) {
                    drawingImagePaths.add(imageUri + imgPath);
                }
                machine.setDrawingImagePath(drawingImagePaths);
            }

        });

        return machinePage;
    }

    Page<Machine> getMachineByType(Integer page, Integer size, Integer id,String search, String sort) {
        Sort sortBy = Sort.by(sort);
        PageRequest pageRequest = PageRequest.of(page, size,sortBy);
        Page<Machine> machinePage = machineRepository.selectMachineByMachineTypeIdAndSearch(id,search,pageRequest);
        List<Machine> machines = machinePage.getContent();
        machines.forEach(machine -> {
            if (machine.getImagePath() != null) {
                machine.setImagePath(imageUri + machine.getImagePath());
            }

            if (machine.getLabelImagePath() != null) {
                machine.setLabelImagePath(imageUri + machine.getLabelImagePath());
            }

            if (machine.getDrawingImagePath() != null) {
                List<String> drawingImagePaths = new ArrayList<>();
                for(String imgPath : machine.getDrawingImagePath()) {
                    drawingImagePaths.add(imageUri + imgPath);
                }
                machine.setDrawingImagePath(drawingImagePaths);
            }

        });

        return  machinePage;
    }

    Machine getMachineById(Integer id) {
        Optional<Machine> optionalMachinemachine = machineRepository.findById(id);
        if(optionalMachinemachine.isEmpty()) {
            throw new NotFoundException(
                    "machine with id " + id + " not found");
        }

        Machine machine = optionalMachinemachine.get();
        if (machine.getImagePath() != null) {
            machine.setImagePath(imageUri + machine.getImagePath());
        }

        if (machine.getLabelImagePath() != null) {
            machine.setLabelImagePath(imageUri + machine.getLabelImagePath());
        }

        if (machine.getDrawingImagePath() != null) {
            List<String> drawingImagePaths = new ArrayList<>();
            for(String imgPath : machine.getDrawingImagePath()) {
                drawingImagePaths.add(imageUri + imgPath);
            }
            machine.setDrawingImagePath(drawingImagePaths);
        }

        List<MachinePart> machineParts = machinePartRepository.findByMachineId(machine.getId());
        List<Part> parts = new ArrayList<>();
        Integer cost = 0;
        for(MachinePart machinePart : machineParts) {
            Part part = machinePart.getPart();
            Integer sum = machinePart.getQuantity() * part.getCost();
            cost += sum;
            parts.add(part);
        }

        List<PartMaterial> partMaterials = new ArrayList<>();
        for(Part part : parts) {
            partMaterials.addAll(part.getPartMaterials());
        }

        List<Material> materials = new ArrayList<>();
        for(PartMaterial partMaterial : partMaterials) {
            materials.add(partMaterial.getMaterial());
        }

        machine.setMaterialCount(materials.size());
        machine.setCost(cost);

        return  machine;
    }

    void saveMachine(Machine machine) {
        machineRepository.save(machine);
    }

    List<MachinePart> findPartByMachineId(Integer id) {
        Optional<Machine> optionalMachine = machineRepository.findById(id);
        if(optionalMachine.isEmpty()) {
            throw new NotFoundException(
                    "machine with id " + id + " not found");
        }

        return machineRepository.findById(id).get().getMachineParts();
    }

    Page<Part> getPartsByMachine(Integer id,Integer page, Integer size, String search, String sort) {
        Optional<Machine> optionalMachine = machineRepository.findById(id);
        if(optionalMachine.isEmpty()) {
            throw new NotFoundException(
                    "machine with id " + id + " not found");
        }

        Sort sortBy = Sort.by(sort);
        PageRequest pageRequest = PageRequest.of(page, size,sortBy);

        Page<Part> partPage = partRepository.selectPartByMachineIdAndSearch(optionalMachine.get(),search,pageRequest);
        List<Part> parts = partPage.getContent();
        parts.forEach(part -> {
            MachinePart machinePart = machinePartRepository.findByMachinePartId(new MachinePartId(id, part.getId()));
            part.setQuantity(machinePart.getQuantity());

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

    Page<Material> getMaterialsByMachine(Integer id,Integer page, Integer size, String search,String sort) {
        Sort sortBy = Sort.by(sort);
        PageRequest pageRequest = PageRequest.of(page, size,sortBy);
        List<MachinePart> machineParts = machinePartRepository.findByMachineId(id);
        List<Part> parts = new ArrayList<>();
        for(MachinePart machinePart : machineParts) {
            Part part = machinePart.getPart();
            parts.add(part);
        }

        Page<Material> materialPage = materialRepository.selectMaterialByMachine(parts, search, pageRequest);
        List<Material> materials = materialPage.getContent();

        materials.forEach(material -> {

            PartMaterial partMaterial = null;
            for(Part part : parts) {
                Optional<PartMaterial> optionalPartMaterial = partMaterialRepository.findByPartMaterialId(new PartMaterialId(part.getId(), material.getId()));
                if(optionalPartMaterial.isPresent()) {
                    partMaterial = optionalPartMaterial.get();
                }
            }

            material.setQuantity(partMaterial != null ? partMaterial.getQuantity() : null);
            if(material.getImagePath() != null)
                material.setImagePath(imageUri + material.getImagePath());
        });

        return materialPage;
    }

}
