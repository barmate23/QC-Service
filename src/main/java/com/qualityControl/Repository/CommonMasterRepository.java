package com.qualityControl.Repository;

import com.qualityControl.Model.CommonMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommonMasterRepository extends JpaRepository<CommonMaster,Integer> {

    CommonMaster findByIsDeletedAndSubOrganizationIdAndMasterValue(boolean b, Integer subOrgId, String qccompleted);
}
