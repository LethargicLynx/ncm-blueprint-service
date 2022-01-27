package com.databake.ncmblueprint.material;

import com.databake.ncmblueprint.part.Part;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Integer> {

    @Query("SELECT m FROM Material m WHERE lower(m.name) LIKE lower(concat('%', :search, '%')) " +
            "OR lower(m.expressNo) LIKE lower(concat('%', :search, '%'))")
    Page<Material> selectMaterialBysearch(@Param("search") String search, Pageable pageable);

    @Query("SELECT m FROM Material m JOIN m.partMaterials pm " +
            "WHERE pm.part IN :parts " +
            "AND ( " +
            "lower(m.name) LIKE lower(concat('%', :search, '%')) " +
            "OR lower(m.expressNo) LIKE lower(concat('%', :search, '%')) " +
            ") ")
    Page<Material> selectMaterialByMachine(@Param("parts") List<Part> parts, @Param("search") String search, Pageable pageable);

}
