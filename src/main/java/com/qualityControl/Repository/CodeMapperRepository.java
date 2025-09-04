package com.qualityControl.Repository;

import com.qualityControl.Model.CodeMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeMapperRepository extends JpaRepository<CodeMapper,Integer> {

    List<CodeMapper> findByIsDeletedAndSubOrganizationId(boolean b, Integer subOrgId);

    CodeMapper findByIsDeletedAndSubOrganizationIdAndId(boolean b, Integer subOrgId, Long codeMapperId);

    CodeMapper findByIsDeletedAndSubOrganizationIdAndAsnNumber(boolean b, Integer subOrgId, String asnNumber);

    CodeMapper findByIsDeletedAndSubOrganizationIdAndPoNumber(boolean b, Integer subOrgId, String purchaseOrderNumber);
}
