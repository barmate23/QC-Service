package com.qualityControl.Repository;

import com.qualityControl.Model.QcActivityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QcActivityTypeRepository extends JpaRepository<QcActivityType,Integer> {
    QcActivityType findByIsDeletedAndActivityName(boolean b, String activityType);
}
