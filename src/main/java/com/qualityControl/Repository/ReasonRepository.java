package com.qualityControl.Repository;

import com.qualityControl.Model.Reason;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReasonRepository extends JpaRepository<Reason,Integer> {

    Reason findByIsDeletedAndId(boolean b, Integer reasonId);

    List<Reason> findByIsDeletedAndSubOrganizationIdAndReasonCategory(boolean b, Integer subOrgId, String ppe);

    List<Reason> findByIsDeletedAndReasonCategory(boolean b, String qc);

    Reason findByIsDeletedAndSubOrganizationIdAndId(boolean b, Integer subOrgId, Integer reasonId);
}
