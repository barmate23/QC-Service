package com.qualityControl.Repository;

import com.qualityControl.Model.Shift;
import com.qualityControl.Model.ShiftMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShiftMapperRepository extends JpaRepository<ShiftMapper,Integer> {

    ShiftMapper findByIsDeletedAndSubOrganizationIdAndShiftIdAndDayDay(boolean b, Integer subOrgId, Integer shiftid,String day);
}
