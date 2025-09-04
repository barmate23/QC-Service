package com.qualityControl.Repository;

import com.qualityControl.Model.AcceptedRejectedContainer;
import com.qualityControl.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcceptedRejectedContainerRepository extends JpaRepository<AcceptedRejectedContainer,Integer> {

    List<AcceptedRejectedContainer> findByIsDeletedAndSubOrganizationIdAndASNOrderLineAsnHeadIdAsnNumber(boolean b, Integer subOrgId, String asnNumber);

    List<AcceptedRejectedContainer> findByIsDeletedAndSubOrganizationIdAndPurchaseOrderLinePurchaseOrderHeadPurchaseOrderNumber(boolean b, Integer subOrgId, String poNumber);

    AcceptedRejectedContainer findByIsDeletedAndSubOrganizationIdAndASNOrderLineAsnHeadIdAsnNumberAndASNOrderLineItemId(boolean b, Integer subOrgId, String asnNumber, Integer id);

    AcceptedRejectedContainer findByIsDeletedAndSubOrganizationIdAndPurchaseOrderLinePurchaseOrderHeadPurchaseOrderNumberAndPurchaseOrderLineItemId(boolean b, Integer subOrgId, String poNumber, Integer id);

    AcceptedRejectedContainer findByIsDeletedAndSubOrganizationIdAndASNOrderLineItemItemId(boolean b, Integer subOrgId, String itemCode);

    AcceptedRejectedContainer findByIsDeletedAndSubOrganizationIdAndPurchaseOrderLineItemItemId(boolean b, Integer subOrgId, String itemCode);

    AcceptedRejectedContainer findByIsDeletedAndSubOrganizationIdAndId(boolean b, Integer subOrgId, Integer acceptedRejectedContainerId);

    @Query("SELECT arc FROM AcceptedRejectedContainer arc WHERE arc.isDeleted = :isDeleted AND arc.subOrganizationId = :subOrganizationId AND FUNCTION('DATE', arc.qcDate) = FUNCTION('DATE', :qcDate) AND arc.qcEngineer IS NOT NULL")
    List<AcceptedRejectedContainer> findByIsDeletedAndSubOrganizationIdAndQcDateAndQcEngineerIsNotNull(@Param("isDeleted") boolean isDeleted,
                                                                                 @Param("subOrganizationId") Integer subOrganizationId,
                                                                                 @Param("qcDate") String qcDate);

    @Query("SELECT arc FROM AcceptedRejectedContainer arc WHERE arc.isDeleted = :isDeleted AND arc.subOrganizationId = :subOrganizationId AND arc.qcEngineer = :qcEngineer AND FUNCTION('DATE', arc.qcDate) = FUNCTION('DATE', :qcDate)")
    List<AcceptedRejectedContainer> findByIsDeletedAndSubOrganizationIdAndQcEngineerAndQcDate(@Param("isDeleted") boolean isDeleted,
                                                                                              @Param("subOrganizationId") Integer subOrganizationId,
                                                                                              @Param("qcEngineer") Users qcEngineer,
                                                                                              @Param("qcDate") String qcDate);
    List<AcceptedRejectedContainer> findByIsDeletedAndSubOrganizationIdAndQcEngineer(boolean b, Integer subOrgId, Users user);

    List<AcceptedRejectedContainer> findByIsDeletedAndSubOrganizationIdAndASNOrderLineId(boolean b, Integer subOrgId, Integer id);

    List<AcceptedRejectedContainer> findByIsDeletedAndSubOrganizationIdAndPurchaseOrderLineId(boolean b, Integer subOrgId, Integer id);

    List<AcceptedRejectedContainer> findByIsDeletedAndSubOrganizationIdAndQcEngineerId(boolean b, Integer subOrgId, Integer userId);

    List<AcceptedRejectedContainer> findByIsDeletedAndSubOrganizationIdAndASNOrderLineIdAndIsAccepted(boolean b, Integer subOrgId, Integer id, boolean b1);

    List<AcceptedRejectedContainer> findByIsDeletedAndSubOrganizationIdAndPurchaseOrderLineIdAndIsAccepted(boolean b, Integer subOrgId, Integer id, boolean b1);

    List<AcceptedRejectedContainer> findByIsDeletedAndSubOrganizationIdAndASNOrderLineAsnHeadIdAsnNumberAndIsAccepted(boolean b, Integer subOrgId, String asnNumber, boolean b1);

    List<AcceptedRejectedContainer> findByIsDeletedAndSubOrganizationIdAndPurchaseOrderLinePurchaseOrderHeadPurchaseOrderNumberAndIsAccepted(boolean b, Integer subOrgId, String poNumber, boolean b1);

    List<AcceptedRejectedContainer> findByIsDeletedAndSubOrganizationIdAndQcEngineerIdAndASNOrderLineItemQcRequiredOrPurchaseOrderLineItemQcRequired(boolean b, Integer subOrgId, Integer userId, boolean b1, boolean b2);

    List<AcceptedRejectedContainer> findByIsDeletedAndSubOrganizationIdAndASNOrderLineAsnHeadIdAsnNumberAndQcEngineerId(boolean b, Integer subOrgId, String asnNumber, Integer qcEngineer);

    List<AcceptedRejectedContainer> findByIsDeletedAndSubOrganizationIdAndPurchaseOrderLinePurchaseOrderHeadPurchaseOrderNumberAndQcEngineerId(boolean b, Integer subOrgId, String poNumber, Integer qcEngineer);
}
