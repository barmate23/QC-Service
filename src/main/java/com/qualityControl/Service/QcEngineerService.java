package com.qualityControl.Service;

import com.qualityControl.Model.QcTimeActivity;
import com.qualityControl.Request.QcEngineerRequest;
import com.qualityControl.Request.QcEngineerStartPauseEndRequest;
import com.qualityControl.Response.BaseResponse;
import com.qualityControl.Response.QcEngineerResponse;
import org.springframework.stereotype.Service;

@Service
public interface QcEngineerService {

    BaseResponse<QcEngineerResponse> getItemDetail();

    BaseResponse generateGrrAndCrrNumber(QcEngineerRequest qcEngineerRequest);

    BaseResponse<QcTimeActivity> calculateQcTimeActivity(QcEngineerStartPauseEndRequest request);
}
