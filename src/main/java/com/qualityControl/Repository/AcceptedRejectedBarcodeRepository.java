package com.qualityControl.Repository;

import com.qualityControl.Model.AcceptedRejectedContainerBarcode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcceptedRejectedBarcodeRepository extends JpaRepository<AcceptedRejectedContainerBarcode,Integer> {

}
