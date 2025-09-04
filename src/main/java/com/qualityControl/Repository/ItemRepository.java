package com.qualityControl.Repository;

import com.qualityControl.Model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item,Integer> {
    Item findByIsDeletedAndSubOrganizationIdAndItemId(boolean b, Integer subOrgId, String itemCode);
}
