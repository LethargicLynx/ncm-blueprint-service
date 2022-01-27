package com.databake.ncmblueprint.partrelation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PartRelationRepository extends JpaRepository<PartRelation, Integer> {

    @Query("SELECT pr FROM PartRelation pr WHERE pr.subPartId = ?1 ")
    List<PartRelation> findBySubPartId(Integer subPartId);

    @Query("SELECT pr FROM PartRelation pr WHERE pr.assemblyPartId = ?1 ")
    List<PartRelation> findByAssemblyPartId(Integer assemblyPartId);

    @Query("SELECT pr FROM PartRelation pr WHERE pr.subPartId = ?1 AND pr.partId = ?2 ")
    Optional<PartRelation> findBySubPartIdAndPartId(Integer subPartId, Integer partId);

    @Query("SELECT pr FROM PartRelation pr WHERE pr.assemblyPartId = ?1 AND pr.partId = ?2 ")
    Optional<PartRelation> findByAssemblyPartIdtAndPartId(Integer assemblyPartId, Integer partId);
}
