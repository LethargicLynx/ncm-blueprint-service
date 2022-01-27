package com.databake.ncmblueprint.part;

import com.databake.ncmblueprint.machine.Machine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartRepository extends JpaRepository<Part, Integer> {

    @Query("SELECT p FROM Part p WHERE (lower(p.partNo) LIKE lower(concat('%', :search, '%')) " +
            "OR lower(p.name) LIKE lower(concat('%', :search, '%')) " +
            "OR lower(p.expressNo) LIKE lower(concat('%', :search, '%')) " +
            "OR lower(p.drawingNo) LIKE lower(concat('%', :search, '%')) " +
            "OR lower(p.drawn) LIKE lower(concat('%', :search, '%')) ) " +
            "AND p.partType IN :types")
    Page<Part> selectPartBySearchLikeAndTypes(@Param("search") String search, @Param("types") List<String> types, Pageable pageable);

    @Query("SELECT p FROM Part p JOIN p.machineParts mp " +
            "WHERE mp.machine = :machine AND ( " +
            "lower(p.name) LIKE lower(concat('%', :search, '%')) " +
            "OR lower(p.expressNo) LIKE lower(concat('%', :search, '%')) " +
            "OR lower(p.drawingNo) LIKE lower(concat('%', :search, '%')) " +
            "OR lower(p.drawn) LIKE lower(concat('%', :search, '%')) " +
            ") ")
    Page<Part> selectPartByMachineIdAndSearch(@Param("machine") Machine machine, @Param("search") String search, Pageable pageable);



}
