package com.qualityControl.Repository;

import com.qualityControl.Model.QcTimeActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Time;
import java.util.List;

public interface QcTimeActivityRepository extends JpaRepository<QcTimeActivity,Integer> {

    List<QcTimeActivity> findByIsDeletedAndSubOrganizationIdAndItemItemIdAndQcEngineerIdAndQcActivityTypeActivityName(boolean b, Integer subOrgId, String itemCode, Integer officerId, String activityType);

    List<QcTimeActivity> findByIsDeletedAndSubOrganizationIdAndItemItemIdAndQcEngineerIdAndQcActivityTypeActivityNameOrderByIdDesc(boolean b, Integer subOrgId, String itemCode, Integer officerId, String start);

    List<QcTimeActivity> findByIsDeletedAndSubOrganizationIdAndQcEngineerIdAndQcActivityTypeActivityNameOrderByIdDesc(boolean b, Integer subOrgId, Integer officerId, String stop);

    QcTimeActivity findByIsDeletedAndSubOrganizationIdAndItemId(boolean b, Integer userId, Integer id);

    List<QcTimeActivity> findByIsDeletedAndSubOrganizationIdAndItemIdAndQcEngineerIdOrderByIdAsc(boolean b, Integer subOrgId, Integer id, Integer id1);

    List<QcTimeActivity> findByIsDeletedAndSubOrganizationIdAndItemItemCodeAndQcEngineerIdAndQcActivityTypeActivityNameOrderByStartTimeDesc(boolean b, Integer subOrgId, String itemCode, Integer officerId, String pause);

    List<QcTimeActivity> findByIsDeletedAndSubOrganizationIdAndQcEngineerIdAndQcActivityTypeActivityNameOrderByStartTimeDesc(boolean b, Integer subOrgId, Integer officerId, String stop);
}
