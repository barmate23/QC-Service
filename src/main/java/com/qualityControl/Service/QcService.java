package com.qualityControl.Service;

import com.qualityControl.Model.AcceptedRejectedContainer;
import com.qualityControl.Model.Shift;
import com.qualityControl.Model.Users;
import com.qualityControl.Request.QcRequest;
import com.qualityControl.Response.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface QcService {

    BaseResponse<Users> getAllQcEngineers();

    BaseResponse<QcResponse> getCrrListData(Integer qcEngineer, Date startDate, Date endDate);

    BaseResponse<QcDetailResponse> getDetailByCrr(Long codeMapperId);

    BaseResponse<AcceptedRejectedContainer> assignQcEngineer(QcRequest qcRequest);

    BaseResponse<QcItemResponse>    getItemDetail(Integer acceptedRejectedContainerId);

    BaseResponse<Shift> getShift();

    BaseResponse<QcViewResponse> getByFilter(String qcDate, String shiftFilterName);
}
