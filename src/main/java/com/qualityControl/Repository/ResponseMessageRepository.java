package com.qualityControl.Repository;
import com.qualityControl.Model.ResponseMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ResponseMessageRepository extends JpaRepository<ResponseMessage, Integer> {
}

