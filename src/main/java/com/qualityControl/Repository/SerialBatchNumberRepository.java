package com.qualityControl.Repository;

import com.qualityControl.Model.SerialBatchNumbers;
import com.qualityControl.Request.RejectedSerialBatchNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SerialBatchNumberRepository extends JpaRepository<SerialBatchNumbers,Integer> {

    List<SerialBatchNumbers> findByIsDeletedAndSubOrganizationIdAndAsnLineId(boolean b, Integer subOrgId, Integer id);

    List<SerialBatchNumbers> findByIsDeletedAndSubOrganizationIdAndPurchaseOrderLineId(boolean b, Integer subOrgId, Integer id);

    SerialBatchNumbers findByIsDeletedAndSubOrganizationIdAndSerialBatchNumberAndAsnLineId(boolean b, Integer subOrgId, String acceptedSerialBatchNumber, Integer asnLineId);
    SerialBatchNumbers findByIsDeletedAndSubOrganizationIdAndSerialBatchNumberAndPurchaseOrderLineId(boolean b, Integer subOrgId, String acceptedSerialBatchNumber, Integer purchaseOrderLineId);

//    List<SerialBatchNumbers> findByIsDeletedAndSubOrganizationIdAnd

}
