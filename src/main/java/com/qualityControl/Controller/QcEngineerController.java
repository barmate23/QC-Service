package com.qualityControl.Controller;

import com.qualityControl.Model.QcTimeActivity;
import com.qualityControl.Request.QcEngineerRequest;
import com.qualityControl.Request.QcEngineerStartPauseEndRequest;
import com.qualityControl.Response.BaseResponse;
import com.qualityControl.Response.QcEngineerResponse;
import com.qualityControl.Service.QcEngineerService;
import com.qualityControl.Utils.ConstantsForAPIs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ConstantsForAPIs.PREFIX)
public class QcEngineerController {

    @Autowired
    private QcEngineerService qcEngineerService;


    @GetMapping(ConstantsForAPIs.GET_ITEM_DETAIL)
    public BaseResponse<QcEngineerResponse> getItemDetail(){
       return qcEngineerService.getItemDetail();

    }

    @PostMapping(ConstantsForAPIs.GENERATE_GRR_CRR)
    public BaseResponse getItemList(@RequestBody QcEngineerRequest qcEngineerRequest){
         return qcEngineerService.generateGrrAndCrrNumber(qcEngineerRequest);
    }

    @PostMapping(ConstantsForAPIs.RECORD)
    public BaseResponse<QcTimeActivity> recordActivity(@RequestBody QcEngineerStartPauseEndRequest request) {
        return qcEngineerService.calculateQcTimeActivity(request);
    }
}
