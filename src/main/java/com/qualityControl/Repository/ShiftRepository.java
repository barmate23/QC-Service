package com.qualityControl.Repository;

import com.qualityControl.Model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShiftRepository extends JpaRepository<Shift,Integer> {

    List<Shift> findByIsDeletedAndSubOrganizationId(boolean b, Integer subOrgId);
}
