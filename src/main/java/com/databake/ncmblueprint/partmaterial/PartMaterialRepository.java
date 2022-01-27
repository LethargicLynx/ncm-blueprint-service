package com.databake.ncmblueprint.partmaterial;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartMaterialRepository extends JpaRepository<PartMaterial, PartMaterialId> {

    Optional<PartMaterial> findByPartMaterialId(PartMaterialId partMaterialId);
}
