package com.databake.ncmblueprint.machinepart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MachinePartRepository extends JpaRepository<MachinePart, MachinePartId> {

    List<MachinePart> findByMachineId(Integer id);

    MachinePart findByMachinePartId(MachinePartId machinePartId);

}
