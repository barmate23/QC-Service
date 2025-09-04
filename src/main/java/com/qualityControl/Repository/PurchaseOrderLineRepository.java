package com.qualityControl.Repository;

import com.qualityControl.Model.PurchaseOrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderLineRepository extends JpaRepository<PurchaseOrderLine,Integer> {

    List<PurchaseOrderLine> findByIsDeletedAndSubOrganizationIdAndPurchaseOrderHeadPurchaseOrderNumberAndItemQcRequired(boolean b, Integer subOrgId, String poNumber, boolean b1);

    PurchaseOrderLine findByIsDeletedAndSubOrganizationIdAndId(boolean b, Integer subOrgId, Integer id);

    PurchaseOrderLine findByIsDeletedAndSubOrganizationIdAndItemItemId(boolean b, Integer subOrgId, String itemId);

    List<PurchaseOrderLine> findByPurchaseOrderHeadPurchaseOrderNumberAndSubOrganizationIdAndIsDeleted(String poNumber, Integer subOrgId, boolean b);

    List<PurchaseOrderLine> findByPurchaseOrderHeadPurchaseOrderNumberAndSubOrganizationIdAndIsDeletedAndItemQcRequired(String poNumber, Integer subOrgId, boolean b, boolean b1);
}
