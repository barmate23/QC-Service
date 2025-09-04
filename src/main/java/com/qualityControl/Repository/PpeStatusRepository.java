package com.qualityControl.Repository;
import com.qualityControl.Model.PpeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PpeStatusRepository extends JpaRepository<PpeStatus,Integer> {

  PpeStatus findByStatusName(String ppeStatus);

}
