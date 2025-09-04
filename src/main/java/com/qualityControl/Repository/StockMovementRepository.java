package com.qualityControl.Repository;

import com.qualityControl.Model.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockMovementRepository extends JpaRepository<StockMovement,Integer> {
    StockMovement findByIsDeletedAndSubOrganizationIdAndSerialBatchNumbersSerialBatchNumber(boolean b, Integer userId, String serialBatchNumber);
    List<StockMovement> findByIsDeletedAndSubOrganizationIdAndPpeLineId(boolean isDeleted, Integer subOrgId, Integer ppePlanId);
}
