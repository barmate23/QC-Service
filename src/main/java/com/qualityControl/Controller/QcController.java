package com.qualityControl.Controller;

import com.qualityControl.Model.AcceptedRejectedContainer;
import com.qualityControl.Model.Shift;
import com.qualityControl.Model.Users;
import com.qualityControl.Request.QcRequest;
import com.qualityControl.Response.*;
import com.qualityControl.Service.QcService;
import com.qualityControl.Utils.ConstantsForAPIs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(ConstantsForAPIs.PREFIX)
public class QcController {

    @Autowired
    private QcService qcService;

    @GetMapping(ConstantsForAPIs.QC_ENGINEERS)
    public BaseResponse<Users> getOfficers(){
        return qcService.getAllQcEngineers();
    }

    @GetMapping(ConstantsForAPIs.CRR_LIST)
   public BaseResponse<QcResponse> getAllCRR(@RequestParam(required = false) Integer qcEngineer, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){
        return qcService.getCrrListData(qcEngineer,startDate,endDate);
   }

   @GetMapping(ConstantsForAPIs.GET_BY_CRR_NUMBER)
    public BaseResponse<QcDetailResponse> getDetailByCrrNumber(@PathVariable Long codeMapperId){
       return qcService.getDetailByCrr(codeMapperId);
   }

    @PutMapping(ConstantsForAPIs.ASSIGN_OFFICER)
    public BaseResponse<AcceptedRejectedContainer> assignOfficer(@RequestBody QcRequest qcRequest){
        return qcService.assignQcEngineer(qcRequest);
    }

    @GetMapping(ConstantsForAPIs.GET_QC_ITEM_DETAIL)
    public BaseResponse<QcItemResponse> getQcItemDetail(@PathVariable Integer acceptedRejectedContainerId){
        return qcService.getItemDetail(acceptedRejectedContainerId);
    }


    @GetMapping(ConstantsForAPIs.GET_SHIFT_FILTER)
    public BaseResponse<Shift> getShiftFilter(){
        return qcService.getShift();
    }


    @GetMapping(ConstantsForAPIs.GET_VIEW_DETAIL)
    public BaseResponse<QcViewResponse> getViewDetail(@RequestParam(required = false) String qcDate, @RequestParam(required = false) String shiftFilterName) {
        return qcService.getByFilter(qcDate, shiftFilterName);
    }


}
