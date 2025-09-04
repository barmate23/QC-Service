package com.qualityControl.Repository;

import com.qualityControl.Model.PPELine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PPELineRepository extends JpaRepository<PPELine,Integer> {

    List<PPELine> findByIsDeletedAndSubOrganizationId(boolean b, Integer subOrgId);

    List<PPELine> findByIsDeletedAndSubOrganizationIdAndPpeHeadId(boolean b, Integer subOrgId, Integer id);

    List<PPELine> findByIsDeletedAndPpeHeadId(boolean b, Integer id);

    List<PPELine> findByIsDeletedAndSubOrganizationIdAndStatusMasterValueInAndItemQcRequired(boolean isDeleted, Integer subOrgId, List<String> masterValue, boolean qcRequired);

    List<PPELine> findByIsDeletedAndSubOrganizationIdAndStatusMasterValueInAndItemQcRequiredAndPpeHeadPpeId(boolean isDeleted, Integer subOrgId, List<String> masterValue, boolean qcRequired, String planId);

    PPELine findByIsDeletedAndSubOrganizationIdAndId(boolean isDeleted, Integer subOrgId, Integer ppeLineId);
}
