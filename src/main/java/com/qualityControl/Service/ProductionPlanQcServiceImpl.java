package com.qualityControl.Service;

import com.qualityControl.Model.*;
import com.qualityControl.Repository.*;
import com.qualityControl.Request.PpeQcRequest;
import com.qualityControl.Response.*;
import com.qualityControl.Utils.GlobalMessages;
import com.qualityControl.Utils.ServiceConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Service
public class ProductionPlanQcServiceImpl implements ProductionPlanQcService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginUser loginUser;

    @Autowired
    private CodeMapperRepository codeMapperRepository;

    @Autowired
    private AcceptedRejectedContainerRepository acceptedRejectedContainerRepository;

    @Autowired
    private ASNLineRepository asnLineRepository;

    @Autowired
    private PurchaseOrderLineRepository purchaseOrderLineRepository;

    @Autowired
    private QcCertificateRepository qcCertificateRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private SerialBatchNumberRepository serialBatchNumberRepository;

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private ShiftMapperRepository shiftMapperRepository;

    @Autowired
    private UserShiftRepository userShiftRepository;

    @Autowired
    private CommonMasterRepository commonMasterRepository;

    @Autowired
    private QcTimeActivityRepository qcTimeActivityRepository;

    @Autowired
    private PPELineRepository ppeLineRepository;

    @Autowired
    private PPEHeadRepository ppeHeadRepository;

    @Autowired
    private StockMovementRepository stockMovementRepository;

    @Override
    public BaseResponse<ProductionPlanDataResponse> getProductionPlanData(String planId, Date startDate, Date endDate) {

        long startTime = System.currentTimeMillis();
        log.info(ServiceConstants.GET_PPE_LIST_DATA, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.GET_PRODUCTION_PLAN_DATA_METHOD_START);
        List<ProductionPlanDataResponse> productionPlanDataResponseList = new ArrayList<>();
        BaseResponse<ProductionPlanDataResponse> baseResponse = new BaseResponse<>();

        try {
            log.info(ServiceConstants.GET_PPE_LIST_DATA , loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.SUB_ORGANISATION_ID + loginUser.getSubOrgId());
            List<PPEHead> ppeHeads=new ArrayList<>();
            List<String> masterValues = Arrays.asList("Return", "Reject");

            if (planId != null && startDate != null && endDate != null) {
                List<PPELine> ppelineList = ppeLineRepository.findByIsDeletedAndSubOrganizationIdAndStatusMasterValueInAndItemQcRequiredAndPpeHeadPpeId(false, loginUser.getSubOrgId(), masterValues, true, planId);
                if(ppelineList != null){
                    for(PPELine ppeLine:ppelineList){
                        ProductionPlanDataResponse productionPlanDataResponse=new ProductionPlanDataResponse();

                        PPEHead ppeHead=ppeHeadRepository.findByIsDeletedAndSubOrganizationIdAndIdAndPpeStatusStatusName(false,loginUser.getSubOrgId(),ppeLine.getPpeHead().getId(),"CANCEL");
                        if(ppeHead != null && ppeHead.getStartDate().compareTo(startDate) <= 0 && ppeHead.getEndDate().compareTo(endDate) >= 0){
                            ppeHeads.add(ppeHead);
                            productionPlanDataResponse.setPlanId(ppeHead.getPpeId());
                            productionPlanDataResponse.setProduct(ppeHead.getProduct());
                            productionPlanDataResponse.setPlanStatus(ppeHead.getPpeStatus().getStatusName());
                            productionPlanDataResponse.setProductionShop(ppeHead.getProductionShop());
                            productionPlanDataResponse.setLineId(ppeHead.getLineId());
                            productionPlanDataResponse.setVariant(ppeHead.getVariant());
                            productionPlanDataResponse.setBrand(ppeHead.getBrand());
                            productionPlanDataResponse.setModel(ppeHead.getModel());
                            productionPlanDataResponse.setStartDate(ppeHead.getStartDate());
                            productionPlanDataResponse.setEndDate(ppeHead.getEndDate());
                            productionPlanDataResponseList.add(productionPlanDataResponse);
                        }
                    }
                }

            } else if (planId != null) {
                List<PPELine> ppelineList = ppeLineRepository.findByIsDeletedAndSubOrganizationIdAndStatusMasterValueInAndItemQcRequiredAndPpeHeadPpeId(false, loginUser.getSubOrgId(), masterValues, true, planId);
                if(ppelineList != null){
                    for(PPELine ppeLine:ppelineList){
                        ProductionPlanDataResponse productionPlanDataResponse=new ProductionPlanDataResponse();
                        PPEHead ppeHead=ppeHeadRepository.findByIsDeletedAndSubOrganizationIdAndIdAndPpeStatusStatusName(false,loginUser.getSubOrgId(),ppeLine.getPpeHead().getId(),"CANCEL");
                        if(ppeHead != null){
                            ppeHeads.add(ppeHead);
                            productionPlanDataResponse.setPlanId(ppeHead.getPpeId());
                            productionPlanDataResponse.setProduct(ppeHead.getProduct());
                            productionPlanDataResponse.setPlanStatus(ppeHead.getPpeStatus().getStatusName());
                            productionPlanDataResponse.setProductionShop(ppeHead.getProductionShop());
                            productionPlanDataResponse.setLineId(ppeHead.getLineId());
                            productionPlanDataResponse.setVariant(ppeHead.getVariant());
                            productionPlanDataResponse.setBrand(ppeHead.getBrand());
                            productionPlanDataResponse.setModel(ppeHead.getModel());
                            productionPlanDataResponse.setStartDate(ppeHead.getStartDate());
                            productionPlanDataResponse.setEndDate(ppeHead.getEndDate());
                            productionPlanDataResponseList.add(productionPlanDataResponse);
                        }
                    }
                }

            } else if (startDate != null && endDate != null) {
                List<PPELine> ppeLineList=ppeLineRepository.findByIsDeletedAndSubOrganizationIdAndStatusMasterValueInAndItemQcRequired(false,loginUser.getSubOrgId(),masterValues,true);
                if(ppeLineList != null){
                    for(PPELine ppeLine:ppeLineList){
                        ProductionPlanDataResponse productionPlanDataResponse=new ProductionPlanDataResponse();
                        PPEHead ppeHead=ppeHeadRepository.findByIsDeletedAndSubOrganizationIdAndIdAndPpeStatusStatusName(false,loginUser.getSubOrgId(),ppeLine.getPpeHead().getId(),"CANCEL");
                        if(ppeHead != null && ppeHead.getStartDate().compareTo(startDate) <= 0 && ppeHead.getEndDate().compareTo(endDate) >= 0){
                            ppeHeads.add(ppeHead);
                            productionPlanDataResponse.setPlanId(ppeHead.getPpeId());
                            productionPlanDataResponse.setProduct(ppeHead.getProduct());
                            productionPlanDataResponse.setPlanStatus(ppeHead.getPpeStatus().getStatusName());
                            productionPlanDataResponse.setProductionShop(ppeHead.getProductionShop());
                            productionPlanDataResponse.setVariant(ppeHead.getVariant());
                            productionPlanDataResponse.setLineId(ppeHead.getLineId());
                            productionPlanDataResponse.setBrand(ppeHead.getBrand());
                            productionPlanDataResponse.setModel(ppeHead.getModel());
                            productionPlanDataResponse.setStartDate(ppeHead.getStartDate());
                            productionPlanDataResponse.setEndDate(ppeHead.getEndDate());
                            productionPlanDataResponseList.add(productionPlanDataResponse);
                        }
                    }

                }

            }
            else {
                List<PPELine> ppeLineList=ppeLineRepository.findByIsDeletedAndSubOrganizationIdAndStatusMasterValueInAndItemQcRequired(false,loginUser.getSubOrgId(),masterValues,true);
                if(ppeLineList != null){
                    for(PPELine ppeLine:ppeLineList){
                        ProductionPlanDataResponse productionPlanDataResponse=new ProductionPlanDataResponse();
                        PPEHead ppeHead=ppeHeadRepository.findByIsDeletedAndSubOrganizationIdAndIdAndPpeStatusStatusName(false,loginUser.getSubOrgId(),ppeLine.getPpeHead().getId(),"CANCEL");
                        if(ppeHead != null ){
                            ppeHeads.add(ppeHead);
                            productionPlanDataResponse.setPlanId(ppeHead.getPpeId());
                            productionPlanDataResponse.setProduct(ppeHead.getProduct());
                            productionPlanDataResponse.setPlanStatus(ppeHead.getPpeStatus().getStatusName());
                            productionPlanDataResponse.setProductionShop(ppeHead.getProductionShop());
                            productionPlanDataResponse.setLineId(ppeHead.getLineId());
                            productionPlanDataResponse.setVariant(ppeHead.getVariant());
                            productionPlanDataResponse.setBrand(ppeHead.getBrand());
                            productionPlanDataResponse.setModel(ppeHead.getModel());
                            productionPlanDataResponse.setStartDate(ppeHead.getStartDate());
                            productionPlanDataResponse.setEndDate(ppeHead.getEndDate());
                            productionPlanDataResponseList.add(productionPlanDataResponse);
                        }
                    }
                }
            }
            Set<ProductionPlanDataResponse> productionPlanDataSet = new HashSet<>(productionPlanDataResponseList);
            productionPlanDataResponseList = new ArrayList<>(productionPlanDataSet);
            baseResponse.setData(productionPlanDataResponseList);
            ResponseMessage successMessage = GlobalMessages.getResponseMessages(ServiceConstants.QUALITY_CONTROL_QCPPE100001);
            baseResponse.setCode(successMessage.getCode());
            baseResponse.setStatus(successMessage.getStatus());
            baseResponse.setMessage(successMessage.getMessage());
            log.info(ServiceConstants.GET_PPE_LIST_DATA, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.PPE_PLAN_FETCH_SUCCESSFULLY);
        }
        catch (Exception e) {
            e.printStackTrace();
            ResponseMessage errorMessage = GlobalMessages.getResponseMessages(ServiceConstants.QUALITY_CONTROL_QCPPE100002);
            baseResponse.setCode(errorMessage.getCode());
            baseResponse.setMessage(errorMessage.getMessage());
            baseResponse.setLogId(loginUser.getLogId());
            baseResponse.setStatus(errorMessage.getStatus());
            long endTime = System.currentTimeMillis();
            log.error(ServiceConstants.GET_PPE_LIST_DATA, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.FAILED_TO_FETCH_PPE_PLAN + (endTime - startTime), e);
        }
        long endTime = System.currentTimeMillis();
        log.info(ServiceConstants.GET_PPE_LIST_DATA, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.GET_PRODUCTION_PLAN_DATA_METHOD_EXECUTED + (endTime - startTime));
        return baseResponse;
    }
    @Override
    public BaseResponse<GetDetailByPlanResponse> getDetailByPlanId(String planId) {
        long startTime = System.currentTimeMillis();
        log.info(ServiceConstants.GET_DETAIL_BY_PLAN_ID, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.GET_DETAIL_BY_PLAN_ID_METHOD_START);
        List<GetDetailByPlanResponse> getDetailByPlanResponses = new ArrayList<>();
        BaseResponse<GetDetailByPlanResponse> baseResponse = new BaseResponse<>();
        try{
            log.info(ServiceConstants.GET_DETAIL_BY_PLAN_ID , loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.SUB_ORGANISATION_ID + loginUser.getSubOrgId());
            List<String> masterValues = Arrays.asList("Return", "Reject");
            List<PPELine> ppeLines = ppeLineRepository.findByIsDeletedAndSubOrganizationIdAndStatusMasterValueInAndItemQcRequiredAndPpeHeadPpeId(false,loginUser.getSubOrgId(),masterValues,true,planId);
            if(!ppeLines.isEmpty()) {
                for(PPELine ppeLine:ppeLines) {
                    GetDetailByPlanResponse getDetailByPlanResponse = new GetDetailByPlanResponse();
                    getDetailByPlanResponse.setPlanId(planId);
                    getDetailByPlanResponse.setProduct(ppeLine.getPpeHead().getProduct());
                    getDetailByPlanResponse.setBrand(ppeLine.getPpeHead().getBrand());
                    getDetailByPlanResponse.setModel(ppeLine.getPpeHead().getModel());
                    getDetailByPlanResponse.setVariant(ppeLine.getPpeHead().getVariant());
                    getDetailByPlanResponse.setStartDate(ppeLine.getPpeHead().getStartDate());
                    getDetailByPlanResponse.setEndDate(ppeLine.getPpeHead().getEndDate());
                    getDetailByPlanResponse.setItemCode(ppeLine.getItem().getItemCode());
                    getDetailByPlanResponse.setItemName(ppeLine.getItem().getName());
                    getDetailByPlanResponse.setItemQuantity(null);
                    //QCSTATUS
                    CommonMaster qcStatus = commonMasterRepository.findByIsDeletedAndSubOrganizationIdAndMasterValue(false, loginUser.getSubOrgId(), "QCNOTSTARTED");
                    if(qcStatus != null){
                        ppeLine.setStatus(qcStatus);
                        getDetailByPlanResponse.setQcStatus(ServiceConstants.NOT_STARTED);
                        ppeLineRepository.save(ppeLine);
                    }
                    //QC Engineer
                    if(ppeLine.getQcEngineer() != null){
                        getDetailByPlanResponse.setQcEngineer(ppeLine.getQcEngineer());
                    }
                    getDetailByPlanResponses.add(getDetailByPlanResponse);
                }
            }
            ResponseMessage successMessage = GlobalMessages.getResponseMessages(ServiceConstants.QUALITY_CONTROL_QCPPE100003);
            baseResponse.setMessage(successMessage.getMessage());
            baseResponse.setCode(successMessage.getCode());
            baseResponse.setStatus(successMessage.getStatus());
            baseResponse.setData(getDetailByPlanResponses);
            log.info(ServiceConstants.GET_DETAIL_BY_PLAN_ID, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.SUCCESSFULLY_FETCH_PPE_LINES_BY_PLAN_ID);
        }catch (Exception e){
            ResponseMessage errorMessage = GlobalMessages.getResponseMessages(ServiceConstants.QUALITY_CONTROL_QCPPE100004);
            baseResponse.setMessage(errorMessage.getMessage());
            baseResponse.setCode(errorMessage.getCode());
            baseResponse.setStatus(errorMessage.getStatus());
            e.printStackTrace();long endTime = System.currentTimeMillis();
            log.error(ServiceConstants.GET_DETAIL_BY_PLAN_ID, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.FAILED_TO_FETCH_PPE_LINES_BY_PLAN_ID + (endTime - startTime), e);
        }
        long endTime = System.currentTimeMillis();
        log.info(ServiceConstants.GET_DETAIL_BY_PLAN_ID, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.GET_DETAIL_BY_PLAN_ID_METHOD_EXECUTED + (endTime - startTime));
        return baseResponse;
    }

    @Override
    public BaseResponse<PPELine> assignQcEngineer(PpeQcRequest ppeQcRequest) {
        long startTime = System.currentTimeMillis();
        log.info(ServiceConstants.ASSIGN_QC_ENGINEER_FOR_PPE, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.ASSIGN_QC_ENGINEER_FOR_PPE_METHOD_START);
        BaseResponse<PPELine> baseResponse = new BaseResponse<>();
        List<PPELine> ppeLines = new ArrayList<>();
        try{
            log.info(ServiceConstants.ASSIGN_QC_ENGINEER_FOR_PPE , loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.SUB_ORGANISATION_ID + loginUser.getSubOrgId());
            PPELine ppeLine=ppeLineRepository.findByIsDeletedAndSubOrganizationIdAndId(false,loginUser.getSubOrgId(),ppeQcRequest.getPpeLineId());
            Optional<Users> users = userRepository.findByIdAndIsDeleted(ppeQcRequest.getQcEngineer(), false);
            UserShiftMapper userShiftMappers = userShiftRepository.findByIsDeletedAndSubOrganizationIdAndUserUserId(false, loginUser.getSubOrgId(), users.get().getUserId());

            if (users.isPresent() && userShiftMappers != null) {
                {
                    Shift userShift = userShiftMappers.getShift();
                    Timestamp shiftStart = userShift.getShiftStart();
                    Timestamp shiftEnd = userShift.getShiftEnd();
                    Date qcAssignDate = ppeQcRequest.getQcAssignDate();

                    Timestamp qcStartTime = new Timestamp(qcAssignDate.getTime() + ppeQcRequest.getStartTime().getTime());
                    Timestamp qcEndTime = new Timestamp(qcAssignDate.getTime() + ppeQcRequest.getEndTime().getTime());

                    // Convert Date to LocalDate
                    LocalDate qcAssignLocalDate = qcAssignDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    DayOfWeek qcAssignDayOfWeek = qcAssignLocalDate.getDayOfWeek();
                    String qcAssignDayOfWeekString = qcAssignDayOfWeek.name().toUpperCase();

                    LocalTime shiftStartTime = shiftStart.toLocalDateTime().toLocalTime();
                    LocalTime shiftEndTime = shiftEnd.toLocalDateTime().toLocalTime();
                    LocalTime qcStartLocalTime = qcStartTime.toLocalDateTime().toLocalTime();
                    LocalTime qcEndLocalTime = qcEndTime.toLocalDateTime().toLocalTime();

                    // Determine if the shift crosses midnight
                    boolean shiftCrossesMidnight = shiftEndTime.isBefore(shiftStartTime);

                    ShiftMapper daysofshift = shiftMapperRepository.findByIsDeletedAndSubOrganizationIdAndShiftIdAndDayDay(false, loginUser.getSubOrgId(), userShiftMappers.getShift().getId(), qcAssignDayOfWeekString);
                    if (daysofshift != null) {
                        boolean withinShift;
                        if (shiftCrossesMidnight) {
                            withinShift = !(qcStartLocalTime.isBefore(shiftStartTime) && qcStartLocalTime.isAfter(shiftEndTime)) &&
                                    !(qcEndLocalTime.isBefore(shiftStartTime) && qcEndLocalTime.isAfter(shiftEndTime));
                        } else {
                            withinShift = !qcStartLocalTime.isBefore(shiftStartTime) && !qcEndLocalTime.isAfter(shiftEndTime);
                        }

                        if (withinShift) {
                            ppeLine.setQcEngineer(users.get());
                            ppeLine.setQcDate(ppeQcRequest.getQcAssignDate());
                            ppeLine.setStartTime(ppeQcRequest.getStartTime());
                            ppeLine.setEndTime(ppeQcRequest.getEndTime());
                            ppeLines.add(ppeLine);
                            ppeLineRepository.save(ppeLine);

                            ResponseMessage errorMessage = GlobalMessages.getResponseMessages(ServiceConstants.QUALITY_CONTROL_QCPPE100005);
                            baseResponse.setCode(errorMessage.getCode());
                            baseResponse.setMessage(errorMessage.getMessage());
                            baseResponse.setLogId(loginUser.getLogId());
                            baseResponse.setData(ppeLines);
                            baseResponse.setStatus(errorMessage.getStatus());
                            log.info(ServiceConstants.ASSIGN_QC_ENGINEER, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.QC_OFFICER_ASSIGN_SUCCESSFULLY);
                        } else {
                            ResponseMessage errorMessage = GlobalMessages.getResponseMessages(ServiceConstants.QUALITY_CONTROL_QCPPE100006);
                            baseResponse.setCode(errorMessage.getCode());
                            baseResponse.setMessage(errorMessage.getMessage());
                            baseResponse.setLogId(loginUser.getLogId());
                            baseResponse.setStatus(errorMessage.getStatus());
                            log.info(ServiceConstants.ASSIGN_QC_ENGINEER, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.QC_OFFICER_NOT_AVAILABLE);
                        }
                    } else {
                        ResponseMessage errorMessage = GlobalMessages.getResponseMessages(ServiceConstants.QUALITY_CONTROL_QCPPE100006);
                        baseResponse.setCode(errorMessage.getCode());
                        baseResponse.setMessage(errorMessage.getMessage());
                        baseResponse.setLogId(loginUser.getLogId());
                        baseResponse.setStatus(errorMessage.getStatus());
                        baseResponse.setData(ppeLines);
                        log.info(ServiceConstants.ASSIGN_QC_ENGINEER, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.QC_OFFICER_NOT_AVAILABLE);
                    }
                }
            }
            ResponseMessage successMessage = GlobalMessages.getResponseMessages(ServiceConstants.QUALITY_CONTROL_QCPPE100007);
            baseResponse.setMessage(successMessage.getMessage());
            baseResponse.setCode(successMessage.getCode());
            baseResponse.setStatus(successMessage.getStatus());
            baseResponse.setData(ppeLines);
            log.info(ServiceConstants.GET_DETAIL_BY_PLAN_ID, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.SUCCESSFULLY_ASSIGN_QC_ENGINEER_FOR_PPE);

        }catch (Exception e){
            ResponseMessage errorMessage = GlobalMessages.getResponseMessages(ServiceConstants.QUALITY_CONTROL_QCPPE100008);
            baseResponse.setMessage(errorMessage.getMessage());
            baseResponse.setCode(errorMessage.getCode());
            baseResponse.setStatus(errorMessage.getStatus());
            e.printStackTrace();
            long endTime = System.currentTimeMillis();
            log.error(ServiceConstants.ASSIGN_QC_ENGINEER_FOR_PPE, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.FAILED_TO_ASSIGN_QC_ENGINEER_FOR_PPE + (endTime - startTime), e);

        }
        long endTime = System.currentTimeMillis();
        log.info(ServiceConstants.ASSIGN_QC_ENGINEER_FOR_PPE, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.ASSIGN_QC_ENGINEER_FOR_PPE_METHOD_EXECUTED + (endTime - startTime));
        return baseResponse;
    }

    @Override
    public BaseResponse<PpeQcItemResponse> getItemDetail(Integer ppeLineId) {
        long startTime = System.currentTimeMillis();
        log.info(ServiceConstants.GET_ITEM_DETAIL_FOR_PPE, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.GET_CRR_LIST_DATA_METHOD_START);
        PpeQcItemResponse ppeQcItemResponse=new PpeQcItemResponse();
        BaseResponse<PpeQcItemResponse> baseResponse=new BaseResponse<>();
        List<PpeQcItemResponse> ppeQcItemResponses=new ArrayList<>();
        try{
            log.info(ServiceConstants.GET_ITEM_DETAIL_FOR_PPE , loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.SUB_ORGANISATION_ID + loginUser.getSubOrgId());
            PPELine ppeLine=ppeLineRepository.findByIsDeletedAndSubOrganizationIdAndId(false,loginUser.getSubOrgId(),ppeLineId);
            if(ppeLine != null){

                List<StockMovement> stockMovementList=stockMovementRepository.findByIsDeletedAndSubOrganizationIdAndPpeLineId(false,loginUser.getSubOrgId(),ppeLineId);
                List<SerialBatchNumbers> serialBatchNumbers=new ArrayList<>();
                for(StockMovement stockMovement:stockMovementList){
                    serialBatchNumbers.add(stockMovement.getSerialBatchNumbers());
                }

                ppeQcItemResponse.setItemCode(ppeLine.getItem().getItemCode());
                ppeQcItemResponse.setItemQuantity(null);
                //QCSTATUS
                CommonMaster qcStatus = commonMasterRepository.findByIsDeletedAndSubOrganizationIdAndMasterValue(false, loginUser.getSubOrgId(), "STAGINGPUT");
                if(qcStatus != null) {
                    ppeLine.setStatus(qcStatus);
                    ppeQcItemResponse.setQcStatus(ServiceConstants.NOT_STARTED);
                    ppeLineRepository.save(ppeLine);
                }
                //QC Engineer
                if (ppeLine.getQcEngineer() != null) {
                    ppeQcItemResponse.setQcEngineer(ppeLine.getQcEngineer().getFirstName() + " " + ppeLine.getQcEngineer().getLastName());
                }
                ppeQcItemResponse.setSerialBatchNumber(serialBatchNumbers);
                ppeQcItemResponses.add(ppeQcItemResponse);
            }
            ResponseMessage successMessage = GlobalMessages.getResponseMessages(ServiceConstants.QUALITY_CONTROL_QCPPE100009);
            baseResponse.setMessage(successMessage.getMessage());
            baseResponse.setCode(successMessage.getCode());
            baseResponse.setStatus(successMessage.getStatus());
            baseResponse.setData(ppeQcItemResponses);

        }catch(Exception e){
            ResponseMessage errorMessage = GlobalMessages.getResponseMessages(ServiceConstants.QUALITY_CONTROL_QCPPE100010);
            baseResponse.setMessage(errorMessage.getMessage());
            baseResponse.setCode(errorMessage.getCode());
            baseResponse.setStatus(errorMessage.getStatus());
            e.printStackTrace();
            long endTime = System.currentTimeMillis();
            log.error(ServiceConstants.GET_ITEM_DETAIL_FOR_PPE, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.ERROR_GET_CRR_LIST_DATA_METHOD + (endTime - startTime), e);
        }
        long endTime = System.currentTimeMillis();
        log.info(ServiceConstants.GET_ITEM_DETAIL_FOR_PPE, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.GET_CRR_LIST_DATA_METHOD_EXECUTED + (endTime - startTime));
        return baseResponse;
    }

}
