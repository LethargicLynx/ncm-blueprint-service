package com.databake.ncmblueprint.type;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MachineTypeRepository extends CrudRepository<MachineType, Long> {

    @Query("SELECT m FROM MachineType m")
    List<MachineType> findAllMachineType();

}
