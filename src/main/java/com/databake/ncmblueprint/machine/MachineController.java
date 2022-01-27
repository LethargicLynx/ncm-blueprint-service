package com.databake.ncmblueprint.machine;

import com.databake.ncmblueprint.machinepart.MachinePart;
import com.databake.ncmblueprint.material.Material;
import com.databake.ncmblueprint.part.Part;
import com.databake.ncmblueprint.part.PartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipOutputStream;

import static com.databake.ncmblueprint.utils.FileService.*;

@RestController
@CrossOrigin(originPatterns = "*")
@RequestMapping(value = "${general.root_uri}/machines")
public class MachineController {

    @Autowired
    private MachineService machineService;

    @Autowired
    private PartService partService;

    @Value("${general.file.machine}")
    private String machineRootPath;

    @Value("${general.file.part}")
    private String partRootPath;

    @GetMapping(path = "/version")
    public String hello() {
        return "ncm blueprint service version 1";
    }

    @GetMapping
    @PreAuthorize("hasAuthority('machine:view')")
    public Page<Machine> getMachine(@RequestParam(value = "page") Integer page,
                                    @RequestParam(value = "size") Integer size) {
        return machineService.getMachine(page, size);
    }

    @GetMapping(path = "/type")
    @PreAuthorize("hasAuthority('machine:view')")
    public Page<Machine> getMachineByType(@RequestParam(value = "page") Integer page,
                                          @RequestParam(value = "size") Integer size,
                                          @RequestParam(value = "type") Integer typeId,
                                          @RequestParam(value = "search",required = false,defaultValue = "") String search,
                                          @RequestParam(value = "sort",required = false,defaultValue = "id") String sort) {
        return machineService.getMachineByType(page, size, typeId, search,sort);
    }

    @GetMapping(path = "/machine/{id}")
    @PreAuthorize("hasAuthority('machine:view')")
    public Machine getMachineById(@PathVariable("id") Integer id) {
        return machineService.getMachineById(id);
    }

    @GetMapping(path = "/machine/parts/{id}")
    @PreAuthorize("hasAuthority('machine:view')")
    public Page<Part> getMachineParts(@PathVariable("id") Integer id,
                                      @RequestParam(value = "page") Integer page,
                                      @RequestParam(value = "size") Integer size,
                                      @RequestParam(value = "search",required = false,defaultValue = "") String search,
                                      @RequestParam(value = "sort",required = false,defaultValue = "id") String sort) {
        return machineService.getPartsByMachine(id,page,size,search,sort);
    }

    @GetMapping(path = "/machine/materials/{id}")
    @PreAuthorize("hasAuthority('machine:view')")
    public Page<Material> getMachineMaterials(@PathVariable("id") Integer id,
                                              @RequestParam(value = "page") Integer page,
                                              @RequestParam(value = "size") Integer size,
                                              @RequestParam(value = "search",required = false,defaultValue = "") String search,
                                              @RequestParam(value = "sort",required = false,defaultValue = "id") String sort) {
        return machineService.getMaterialsByMachine(id, page, size, search,sort);
    }

    @GetMapping(value = "/machine/{id}/zip", produces="application/zip")
    @PreAuthorize("hasAuthority('machine:download')")
    void downloadPartFilesZip(@PathVariable("id") Integer id,
                              HttpServletResponse response) throws Exception {
        ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
        Machine machine = machineService.getMachineById(id);
        if(machine.getFolderPath() == null) {
            Files.createDirectories(Paths.get(machineRootPath + "/" + machine.getId() + "/drawing"));
            machine.setFolderPath(machine.getId() + "/drawing");
            machineService.saveMachine(machine);
        }
        File fileToZip = new File(machineRootPath + machine.getFolderPath());
        zipFile(fileToZip, fileToZip.getName(), zipOut);
        List<MachinePart> machinePartList = machineService.findPartByMachineId(id);
        for(MachinePart machinePart : machinePartList) {
            Part part = machinePart.getPart();
            if(part.getFolderPath() != null) {
                addFolderToZip(part.getDrawingNo(),partRootPath + part.getFolderPath(), zipOut);
            } else {
                Files.createDirectories(Paths.get(partRootPath + "/" + part.getId() + "/drawing"));
                part.setFolderPath(part.getId() + "/drawing");
                partService.savePart(part);
            }
        }

        zipOut.finish();
        zipOut.close();
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + machine.getName() + "\"");
    }

}
