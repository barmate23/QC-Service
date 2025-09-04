package com.qualityControl.Repository;

import com.qualityControl.Model.StockBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockBalanceRepository extends JpaRepository<StockBalance,Integer> {

    StockBalance findByIsDeletedAndSubOrganizationIdAndItemIdId(Boolean b, Integer subOrgId, Integer itemId);

}
