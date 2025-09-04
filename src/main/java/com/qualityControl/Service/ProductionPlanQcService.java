package com.qualityControl.Service;

import com.qualityControl.Model.PPELine;
import com.qualityControl.Model.Users;
import com.qualityControl.Request.PpeQcRequest;
import com.qualityControl.Response.*;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public interface ProductionPlanQcService {

    BaseResponse<ProductionPlanDataResponse> getProductionPlanData(String planId, Date startDate, Date endDate);
    BaseResponse<GetDetailByPlanResponse> getDetailByPlanId(String planId);
    BaseResponse<PPELine> assignQcEngineer(PpeQcRequest ppeQcRequest);
    BaseResponse<PpeQcItemResponse> getItemDetail(Integer ppeLineId);


}
