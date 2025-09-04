package com.qualityControl.Repository;

import com.qualityControl.Model.ASNOrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface ASNLineRepository extends JpaRepository<ASNOrderLine,Integer> {

    ASNOrderLine findByIsDeletedAndSubOrganizationIdAndItemIdAndAsnHeadIdPurchaseStatusStatusNameAndAsnHeadIdRequiredOnDate(boolean b, Integer subOrgId, Integer itemId, String status, Date requiredOnDate);

    List<ASNOrderLine> findByIsDeletedAndSubOrganizationIdAndAsnHeadIdAsnNumberAndItemQcRequired(boolean b, Integer subOrgId, String asnNumber, boolean b1);

    ASNOrderLine findByIsDeletedAndSubOrganizationIdAndItemItemId(boolean b, Integer subOrgId, String itemCode);

    ASNOrderLine findByIsDeletedAndSubOrganizationIdAndId(boolean b, Integer subOrgId, Integer id);

    List<ASNOrderLine> findByAsnHeadIdAsnNumberAndSubOrganizationIdAndIsDeleted(String asnNumber, Integer subOrgId, boolean b);

    List<ASNOrderLine> findByAsnHeadIdAsnNumberAndSubOrganizationIdAndIsDeletedAndItemQcRequired(String asnNumber, Integer subOrgId, boolean b, boolean b1);
}
