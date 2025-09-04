package com.qualityControl.Repository;

import com.qualityControl.Model.UserShiftMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserShiftRepository extends JpaRepository<UserShiftMapper,Integer> {

    List<UserShiftMapper> findByIsDeletedAndSubOrganizationIdAndShiftShiftNameAndUserModuleUserLicenceKeyLicenceLineSubModuleSubModuleCode(boolean b, Integer subOrgId, String shiftFilterName, String qceg);

    List<UserShiftMapper> findByIsDeletedAndSubOrganizationIdAndUserModuleUserLicenceKeyLicenceLineSubModuleSubModuleCode(boolean b, Integer subOrgId, String qceg);

    UserShiftMapper findByIsDeletedAndSubOrganizationIdAndUserUserId(boolean b, Integer subOrgId, String userId);
}
