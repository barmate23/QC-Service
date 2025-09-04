package com.qualityControl.Controller;

import com.qualityControl.Model.PPELine;
import com.qualityControl.Request.PpeQcRequest;
import com.qualityControl.Response.*;
import com.qualityControl.Service.ProductionPlanQcService;
import com.qualityControl.Utils.ConstantsForAPIs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping(ConstantsForAPIs.PREFIX)
public class ProductionPlanQcController {

    @Autowired
    private ProductionPlanQcService productionPlanQcService;

    @GetMapping(ConstantsForAPIs.GET_ALL_PPE_DATA)
    public BaseResponse<ProductionPlanDataResponse> getAllProductionPlanData(@RequestParam(required = false) String planId, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){
        return productionPlanQcService.getProductionPlanData(planId,startDate,endDate);
    }

    @GetMapping(ConstantsForAPIs.GET_BY_PPE_PLAN_ID)
    public BaseResponse<GetDetailByPlanResponse> getDetailByPpePlanId(@PathVariable String ppePlanId){
        return productionPlanQcService.getDetailByPlanId(ppePlanId);
    }

    @PutMapping(ConstantsForAPIs.ASSIGN_QCENGINEER)
    public BaseResponse<PPELine> assignOfficer(@RequestBody PpeQcRequest ppeQcRequest){
        return productionPlanQcService.assignQcEngineer(ppeQcRequest);
    }

    @GetMapping(ConstantsForAPIs.GET_PPE_QC_ITEM_DETAIL)
    public BaseResponse<PpeQcItemResponse> getPpeQcItemDetail(@PathVariable Integer ppeLineId){
        return productionPlanQcService.getItemDetail(ppeLineId);
    }


}
