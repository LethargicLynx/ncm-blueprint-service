package com.databake.ncmblueprint.machine;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Integer> {

    @Query("SELECT m FROM Machine m WHERE (" +
            "lower(m.machineNo) LIKE lower(concat('%', :search, '%')) " +
            "OR lower(m.expressNo) LIKE lower(concat('%', :search, '%')) " +
            "OR lower(m.name) LIKE lower(concat('%', :search, '%')) " +
            "OR lower(m.drawingNo) LIKE lower(concat('%', :search, '%')) " +
            "OR lower(m.drawn) LIKE lower(concat('%', :search, '%'))) " +
            "AND m.type = :id ")
    Page<Machine> selectMachineByMachineTypeIdAndSearch(@Param("id") Integer id, @Param("search") String search, Pageable pageable);

    @Query("SELECT COUNT(m) FROM Machine m WHERE m.type=?1")
    Integer countMachineByType(Integer type);

}