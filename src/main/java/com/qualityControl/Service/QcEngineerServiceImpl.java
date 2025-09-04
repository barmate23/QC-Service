package com.qualityControl.Service;


import com.qualityControl.Model.*;
import com.qualityControl.Repository.*;
import com.qualityControl.Request.QcEngineerRequest;
import com.qualityControl.Request.QcEngineerStartPauseEndRequest;
import com.qualityControl.Request.RejectedSerialBatchNumber;
import com.qualityControl.Response.BaseResponse;
import com.qualityControl.Response.QcEngineerResponse;
import com.qualityControl.Utils.Const;
import com.qualityControl.Utils.GlobalMessages;
import com.qualityControl.Utils.ServiceConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class QcEngineerServiceImpl implements QcEngineerService {

    @Autowired
    private LoginUser loginUser;

    @Autowired
    private CodeMapperRepository codeMapperRepository;

    @Autowired
    private AcceptedRejectedContainerRepository acceptedRejectedContainerRepository;

    @Autowired
    private QcCertificateRepository qcCertificateRepository;

    @Autowired
    private ASNLineRepository asnLineRepository;

    @Autowired
    private SerialBatchNumberRepository serialBatchNumberRepository;

    @Autowired
    private AcceptedRejectedBarcodeRepository acceptedRejectedBarcodeRepository;

    @Autowired
    private StockMovementRepository stockMovementRepository;

    @Autowired
    private CommonMasterRepository commonMasterRepository;

    @Autowired
    private QcTimeActivityRepository qcTimeActivityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QcActivityTypeRepository qcActivityTypeRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ReasonRepository reasonRepository;

    @Override
    public BaseResponse<QcEngineerResponse> getItemDetail() {
        long startTime = System.currentTimeMillis();
        log.info(ServiceConstants.GET_ITEM_DETAIL_LOG, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.GET_ITEM_DETAIL_METHOD_START);

        QcEngineerResponse qcEngineerResponse = new QcEngineerResponse();
        List<QcEngineerResponse> qcEngineerResponses = new ArrayList<>();

        BaseResponse<QcEngineerResponse> baseResponse = new BaseResponse<>();

        try {
            log.info(ServiceConstants.GET_ITEM_DETAIL_LOG + loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.SUB_ORGANISATION_ID, loginUser.getSubOrgId());
            List<AcceptedRejectedContainer> acceptedRejectedContainers = acceptedRejectedContainerRepository.findByIsDeletedAndSubOrganizationIdAndQcEngineerIdAndASNOrderLineItemQcRequiredOrPurchaseOrderLineItemQcRequired(false, loginUser.getSubOrgId(), loginUser.getUserId(), true, true);

            for (AcceptedRejectedContainer acceptedRejectedContainer : acceptedRejectedContainers) {
                if (acceptedRejectedContainer.getASNOrderLine() != null) {
                    CodeMapper codeMapper = codeMapperRepository.findByIsDeletedAndSubOrganizationIdAndAsnNumber(false, loginUser.getSubOrgId(), acceptedRejectedContainer.getASNOrderLine().getAsnHeadId().getAsnNumber());

                    QCCertificate qcCertificate = qcCertificateRepository.findByIsDeletedAndSubOrganizationIdAndAsnLineId(false, loginUser.getSubOrgId(), acceptedRejectedContainer.getASNOrderLine().getId());
                    List<Reason> reasons = reasonRepository.findByIsDeletedAndSubOrganizationIdAndReasonCategory(false, loginUser.getSubOrgId(), "QC");

                    qcEngineerResponse.setItemCode(acceptedRejectedContainer.getASNOrderLine().getItem().getItemCode());
                    qcEngineerResponse.setItemName(acceptedRejectedContainer.getASNOrderLine().getItem().getName());
                    qcEngineerResponse.setCrrNumber(codeMapper.getCrrBarcodeNumber());
                    qcEngineerResponse.setCrrCreateDate(codeMapper.getCrrCreateDate());
                    qcEngineerResponse.setCurrentDate(new Date());
                    qcEngineerResponse.setQcAssignDate(acceptedRejectedContainer.getQcDate());
                    qcEngineerResponse.setStartTime(acceptedRejectedContainer.getStartTime());
                    qcEngineerResponse.setEndTime(acceptedRejectedContainer.getEndTime());
                    qcEngineerResponse.setSupplierQcNumber(qcCertificate.getQcNumber());
                    qcEngineerResponse.setSupplierQcDate(qcCertificate.getQcDate());
                    qcEngineerResponse.setAcceptedRejectedContainerId(acceptedRejectedContainer.getId());
                    if(reasons!=null || !reasons.isEmpty()) {
                        qcEngineerResponse.setReasons(reasons);
                    }

                    qcEngineerResponse.setItems(acceptedRejectedContainer.getASNOrderLine().getItem());
                    List<SerialBatchNumbers> serialBatchNumbers = serialBatchNumberRepository.findByIsDeletedAndSubOrganizationIdAndAsnLineId(false, loginUser.getSubOrgId(), acceptedRejectedContainer.getASNOrderLine().getId());
                    List<String> serialBatchNumberList = new ArrayList<>();
                    for (SerialBatchNumbers serialBatchNumber : serialBatchNumbers) {
                        serialBatchNumberList.add(serialBatchNumber.getSerialBatchNumber());
                    }
                    qcEngineerResponse.setSerialBatchNumbers(serialBatchNumberList);

                } else {
                    CodeMapper codeMapper = codeMapperRepository.findByIsDeletedAndSubOrganizationIdAndPoNumber(false, loginUser.getSubOrgId(), acceptedRejectedContainer.getPurchaseOrderLine().getPurchaseOrderHead().getPurchaseOrderNumber());
                    QCCertificate qcCertificate = qcCertificateRepository.findByIsDeletedAndSubOrganizationIdAndPurchaseOrderLineId(false, loginUser.getSubOrgId(), acceptedRejectedContainer.getPurchaseOrderLine().getId());

                    qcEngineerResponse.setItemCode(acceptedRejectedContainer.getPurchaseOrderLine().getItem().getItemCode());
                    qcEngineerResponse.setItemName(acceptedRejectedContainer.getPurchaseOrderLine().getItem().getName());
                    qcEngineerResponse.setCrrNumber(codeMapper.getCrrBarcodeNumber());
                    qcEngineerResponse.setCrrCreateDate(codeMapper.getCrrCreateDate());
                    qcEngineerResponse.setCurrentDate(new Date());
                    qcEngineerResponse.setQcAssignDate(acceptedRejectedContainer.getQcDate());
                    qcEngineerResponse.setStartTime(acceptedRejectedContainer.getStartTime());
                    qcEngineerResponse.setEndTime(acceptedRejectedContainer.getEndTime());
                    qcEngineerResponse.setSupplierQcNumber(qcCertificate.getQcNumber());
                    qcEngineerResponse.setSupplierQcDate(qcCertificate.getQcDate());
                    qcEngineerResponse.setAcceptedRejectedContainerId(acceptedRejectedContainer.getId());

                    qcEngineerResponse.setItems(acceptedRejectedContainer.getPurchaseOrderLine().getItem());
                    List<SerialBatchNumbers> serialBatchNumbers = serialBatchNumberRepository.findByIsDeletedAndSubOrganizationIdAndPurchaseOrderLineId(false, loginUser.getSubOrgId(), acceptedRejectedContainer.getPurchaseOrderLine().getId());
                    List<String> serialBatchNumberList = new ArrayList<>();
                    for (SerialBatchNumbers serialBatchNumber : serialBatchNumbers) {
                        serialBatchNumberList.add(serialBatchNumber.getSerialBatchNumber());
                    }
                    qcEngineerResponse.setSerialBatchNumbers(serialBatchNumberList);
                }

                qcEngineerResponses.add(qcEngineerResponse);
            }

            ResponseMessage errorMessage = GlobalMessages.getResponseMessages(ServiceConstants.QUALITY_CONTROL_QUCN100016);
            baseResponse.setCode(errorMessage.getCode());
            baseResponse.setStatus(errorMessage.getStatus());
            baseResponse.setData(qcEngineerResponses);
            baseResponse.setLogId(loginUser.getLogId());
            baseResponse.setMessage(errorMessage.getMessage());
            log.info(ServiceConstants.GET_ITEM_DETAIL_LOG, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.ITEM_FETCH_SUCCESSFULLY);

        } catch (Exception e) {

            ResponseMessage errorMessage = GlobalMessages.getResponseMessages(ServiceConstants.QUALITY_CONTROL_QUCN100017);
            baseResponse.setCode(errorMessage.getCode());
            baseResponse.setStatus(errorMessage.getStatus());
            baseResponse.setData(qcEngineerResponses);
            baseResponse.setLogId(loginUser.getLogId());
            baseResponse.setMessage(errorMessage.getMessage());
            e.printStackTrace();
            long endTime = System.currentTimeMillis();
            log.error(ServiceConstants.GET_ITEM_DETAIL_LOG, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.ERROR_GET_ITEM_DETAIL_METHOD + (endTime - startTime), e);
        }
        long endTime = System.currentTimeMillis();
        log.info(ServiceConstants.GET_ITEM_DETAIL_LOG, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.GET_ITEM_DETAIL_METHOD_EXECUTED + (endTime - startTime));
        return baseResponse;
    }

    @Override
    public BaseResponse generateGrrAndCrrNumber(QcEngineerRequest qcEngineerRequest) {
        long startTime = System.currentTimeMillis();
        log.info(ServiceConstants.GENERATE_GRR_AND_CRR_NUMBER, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.GENERATE_GRR_AND_CRR_NUMBER_METHOD_START);
        BaseResponse baseResponse = new BaseResponse();
        try {
            log.info(ServiceConstants.GENERATE_GRR_AND_CRR_NUMBER + loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.SUB_ORGANISATION_ID, loginUser.getSubOrgId());
            AcceptedRejectedContainer acceptedRejectedContainer = acceptedRejectedContainerRepository.findByIsDeletedAndSubOrganizationIdAndId(false, loginUser.getSubOrgId(), qcEngineerRequest.getAcceptedRejectedContainerId());
            if (acceptedRejectedContainer.getASNOrderLine() != null) {
                //check GRR
                List<String> acceptedSerialBatchNumbers = qcEngineerRequest.getAcceptedSerialBatchNumber();
                int i = 1;
                for (String acceptedSerialBatchNumber : acceptedSerialBatchNumbers) {

                    SerialBatchNumbers serialBatchNumber = serialBatchNumberRepository.findByIsDeletedAndSubOrganizationIdAndSerialBatchNumberAndAsnLineId(false, loginUser.getSubOrgId(), acceptedSerialBatchNumber, acceptedRejectedContainer.getASNOrderLine().getId());

                    CodeMapper codeMapper = codeMapperRepository.findByIsDeletedAndSubOrganizationIdAndAsnNumber(false, loginUser.getSubOrgId(), serialBatchNumber.getAsnLine().getAsnHeadId().getAsnNumber());

                    StockMovement stockMovement = stockMovementRepository.findByIsDeletedAndSubOrganizationIdAndSerialBatchNumbersSerialBatchNumber(false, loginUser.getSubOrgId(), serialBatchNumber.getSerialBatchNumber());

                    String formattedIndex = String.format("%03d", i);
                    if (codeMapper.getGrrNumber() != null) {
                        stockMovement.setCrrGrrBarcode(
                                codeMapper.getGrrNumber() + "-" + serialBatchNumber.getAsnLine().getItem().getItemCode() + ServiceConstants.GRR_ACCEPTED_CODE + formattedIndex);
                        i++;
                    } else {
                        String grrId = grrNumber();
                        stockMovement.setCrrGrrBarcode(
                                grrId + "-" + serialBatchNumber.getAsnLine().getItem().getItemCode() + ServiceConstants.GRR_ACCEPTED_CODE + formattedIndex);
                        i++;
                    }
                    stockMovementRepository.save(stockMovement);
                }
                //create crr
                int j = 1;
                List<RejectedSerialBatchNumber> rejectedSerialBatchNumbers = qcEngineerRequest.getRejectedSerialBatchNumberList();
                for (RejectedSerialBatchNumber rejectedSerialBatchNumber : rejectedSerialBatchNumbers) {

                    SerialBatchNumbers serialBatchNumber = serialBatchNumberRepository.findByIsDeletedAndSubOrganizationIdAndSerialBatchNumberAndAsnLineId(false, loginUser.getSubOrgId(), rejectedSerialBatchNumber.getRejectedSerialBatchNumber(), acceptedRejectedContainer.getASNOrderLine().getId());

                    StockMovement stockMovement = stockMovementRepository.findByIsDeletedAndSubOrganizationIdAndSerialBatchNumbersSerialBatchNumber(false, loginUser.getSubOrgId(), serialBatchNumber.getSerialBatchNumber());

                    Reason reason = reasonRepository.findByIsDeletedAndSubOrganizationIdAndId(false, loginUser.getSubOrgId(), rejectedSerialBatchNumber.getReasonId());
                    stockMovement.setReason(reason);

                    CodeMapper codeMapper = codeMapperRepository.findByIsDeletedAndSubOrganizationIdAndAsnNumber(false, loginUser.getSubOrgId(), serialBatchNumber.getAsnLine().getAsnHeadId().getAsnNumber());
                    String formattedIndex = String.format("%03d", j);
                    if (codeMapper.getCrrBarcodeNumber() != null) {
                        stockMovement.setCrrGrrBarcode(
                                codeMapper.getCrrBarcodeNumber() + "-" + serialBatchNumber.getAsnLine().getItem().getItemCode() + ServiceConstants.GRR_REJECTED_CODE + formattedIndex);
                        j++;
                    } else {
                        String crrId = crrNumber();
                        stockMovement.setCrrGrrBarcode(
                                crrId + "-" + serialBatchNumber.getAsnLine().getItem().getItemCode() + ServiceConstants.GRR_REJECTED_CODE + formattedIndex);
                        j++;
                    }
                    stockMovementRepository.save(stockMovement);
                }
            } else {
                //check GRR
                List<String> acceptedSerialBatchNumbers = qcEngineerRequest.getAcceptedSerialBatchNumber();
                int i = 1;
                for (String acceptedSerialBatchNumber : acceptedSerialBatchNumbers) {

                    SerialBatchNumbers serialBatchNumber = serialBatchNumberRepository.findByIsDeletedAndSubOrganizationIdAndSerialBatchNumberAndPurchaseOrderLineId(false, loginUser.getSubOrgId(), acceptedSerialBatchNumber, acceptedRejectedContainer.getPurchaseOrderLine().getId());

                    StockMovement stockMovement = stockMovementRepository.findByIsDeletedAndSubOrganizationIdAndSerialBatchNumbersSerialBatchNumber(false, loginUser.getSubOrgId(), serialBatchNumber.getSerialBatchNumber());

                    CodeMapper codeMapper = codeMapperRepository.findByIsDeletedAndSubOrganizationIdAndPoNumber(false, loginUser.getSubOrgId(), serialBatchNumber.getPurchaseOrderLine().getPurchaseOrderHead().getPurchaseOrderNumber());
                    String formattedIndex = String.format("%03d", i);
                    if (codeMapper.getGrrNumber() != null) {
                        stockMovement.setCrrGrrBarcode(
                                codeMapper.getGrrNumber() + "-" + serialBatchNumber.getPurchaseOrderLine().getItem().getItemCode() + ServiceConstants.GRR_ACCEPTED_CODE + formattedIndex);
                        i++;
                    } else {
                        String grrId = grrNumber();
                        stockMovement.setCrrGrrBarcode(
                                grrId + "-" + serialBatchNumber.getPurchaseOrderLine().getItem().getItemCode() + ServiceConstants.GRR_ACCEPTED_CODE + formattedIndex);
                        i++;
                    }
                    stockMovementRepository.save(stockMovement);
                }
                //create crr
                int j = 1;
                List<RejectedSerialBatchNumber> rejectedSerialBatchNumbers = qcEngineerRequest.getRejectedSerialBatchNumberList();
                for (RejectedSerialBatchNumber rejectedSerialBatchNumber : rejectedSerialBatchNumbers) {
                    acceptedRejectedContainer.getPurchaseOrderLine().getItem();
                    SerialBatchNumbers serialBatchNumber = serialBatchNumberRepository.findByIsDeletedAndSubOrganizationIdAndSerialBatchNumberAndPurchaseOrderLineId(false, loginUser.getSubOrgId(), rejectedSerialBatchNumber.getRejectedSerialBatchNumber(), acceptedRejectedContainer.getPurchaseOrderLine().getId());

                    StockMovement stockMovement = stockMovementRepository.findByIsDeletedAndSubOrganizationIdAndSerialBatchNumbersSerialBatchNumber(false, loginUser.getSubOrgId(), serialBatchNumber.getSerialBatchNumber());

                    Reason reason = reasonRepository.findByIsDeletedAndSubOrganizationIdAndId(false, loginUser.getSubOrgId(), rejectedSerialBatchNumber.getReasonId());
                    stockMovement.setReason(reason);

                    CodeMapper codeMapper = codeMapperRepository.findByIsDeletedAndSubOrganizationIdAndPoNumber(false, loginUser.getSubOrgId(), serialBatchNumber.getPurchaseOrderLine().getPurchaseOrderHead().getPurchaseOrderNumber());
                    String formattedIndex = String.format("%03d",j);
                    if (codeMapper.getCrrBarcodeNumber() != null) {
                        stockMovement.setCrrGrrBarcode(
                                codeMapper.getCrrBarcodeNumber() + "-" + serialBatchNumber.getPurchaseOrderLine().getItem().getItemCode() + ServiceConstants.GRR_REJECTED_CODE + formattedIndex);
                        j++;
                    } else {
                        String crrId = crrNumber();
                        stockMovement.setCrrGrrBarcode(
                                crrId + "-" + serialBatchNumber.getPurchaseOrderLine().getItem().getItemCode() + ServiceConstants.GRR_REJECTED_CODE + formattedIndex);
                        j++;
                    }
                    stockMovementRepository.save(stockMovement);
                }
            }
            CommonMaster qcStatus = commonMasterRepository.findByIsDeletedAndSubOrganizationIdAndMasterValue(false, loginUser.getSubOrgId(), "QCCOMPLETED");
            acceptedRejectedContainer.setStatus(qcStatus);

            acceptedRejectedContainerRepository.save(acceptedRejectedContainer);

            ResponseMessage errorMessage = GlobalMessages.getResponseMessages(ServiceConstants.QUALITY_CONTROL_QUCN100018);
            baseResponse.setCode(errorMessage.getCode());
            baseResponse.setStatus(errorMessage.getStatus());
            baseResponse.setLogId(loginUser.getLogId());
            baseResponse.setMessage(errorMessage.getMessage());
            log.info(ServiceConstants.GENERATE_GRR_AND_CRR_NUMBER, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.CRR_AND_GRR_NUMBER_CREATED_SUCCESSFULLY);
        }
        catch (Exception e) {

            ResponseMessage errorMessage = GlobalMessages.getResponseMessages(ServiceConstants.QUALITY_CONTROL_QUCN100019);
            baseResponse.setCode(errorMessage.getCode());
            baseResponse.setStatus(errorMessage.getStatus());
            baseResponse.setLogId(loginUser.getLogId());
            baseResponse.setMessage(errorMessage.getMessage());
            e.printStackTrace();
            long endTime = System.currentTimeMillis();
            log.error(ServiceConstants.GENERATE_GRR_AND_CRR_NUMBER, loginUser.getLogId(), loginUser.getUserId(),ServiceConstants.ERROR_GENERATE_GRR_AND_CRR_NUMBER_METHOD + (endTime - startTime), e);
        }
        long endTime = System.currentTimeMillis();
        log.info(ServiceConstants.GENERATE_GRR_AND_CRR_NUMBER, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.GENERATE_GRR_AND_CRR_NUMBER_METHOD_EXECUTED + (endTime - startTime));
        return baseResponse;
    }

    @Override
    public BaseResponse<QcTimeActivity> calculateQcTimeActivity(QcEngineerStartPauseEndRequest request) {
        long startTime = System.currentTimeMillis();
        log.info(ServiceConstants.CALCULATE_QC_TIME_ACTIVITY, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.CALCULATE_QC_TIME_ACTIVITY_METHOD_START);
        BaseResponse<QcTimeActivity> baseResponse = new BaseResponse();
        try {
            log.info(ServiceConstants.CALCULATE_QC_TIME_ACTIVITY + loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.SUB_ORGANISATION_ID, loginUser.getSubOrgId());
            Users qcEngineer = userRepository.findByIsDeletedAndSubOrganizationIdAndIsActiveAndId(
                    false, loginUser.getSubOrgId(), true, request.getOfficerId()
            );
            Item item = itemRepository.findByIsDeletedAndSubOrganizationIdAndItemId(
                    false, loginUser.getSubOrgId(), request.getItemCode()
            );
            if (request.getActivityType().equals(Const.START)) {
                // Fetch the last PAUSE activity to update its endTime
                List<QcTimeActivity> lastPauseActivities = qcTimeActivityRepository.findByIsDeletedAndSubOrganizationIdAndItemItemIdAndQcEngineerIdAndQcActivityTypeActivityNameOrderByIdDesc(
                        false, loginUser.getSubOrgId(), request.getItemCode(), request.getOfficerId(), "PAUSE"
                );


                if (!lastPauseActivities.isEmpty() && lastPauseActivities.get(0).getEndTime() == null) {
                    QcTimeActivity lastPause = lastPauseActivities.get(0);
                    lastPause.setEndTime(request.getActivityTime());
                    qcTimeActivityRepository.save(lastPause);
                } else if (!lastPauseActivities.isEmpty() && lastPauseActivities.get(1).getEndTime() == null) {
                    QcTimeActivity lastPause = lastPauseActivities.get(0);
                    lastPause.setEndTime(request.getActivityTime());
                    qcTimeActivityRepository.save(lastPause);
                }

                // Update the end time of the last STOP activity
                List<QcTimeActivity> lastStopActivities = qcTimeActivityRepository.findByIsDeletedAndSubOrganizationIdAndQcEngineerIdAndQcActivityTypeActivityNameOrderByIdDesc(
                        false, loginUser.getSubOrgId(), request.getOfficerId(), "STOP"
                );

                if (!lastStopActivities.isEmpty() && lastStopActivities.get(0).getEndTime() == null) {
                    QcTimeActivity lastStop = lastStopActivities.get(0);
                    lastStop.setEndTime(request.getActivityTime());
                    qcTimeActivityRepository.save(lastStop);
                }
                // Create a new START activity
                QcTimeActivity qcTimeActivity = new QcTimeActivity();
                qcTimeActivity.setQcEngineer(qcEngineer);
                QcActivityType activityName = qcActivityTypeRepository.findByIsDeletedAndActivityName(false, request.getActivityType());
                qcTimeActivity.setQcActivityType(activityName);
                qcTimeActivity.setItem(item);
                qcTimeActivity.setStartTime(request.getActivityTime());
                qcTimeActivity.setSubOrganizationId(loginUser.getSubOrgId());
                qcTimeActivity.setIsDeleted(false);
                qcTimeActivityRepository.save(qcTimeActivity);
            } else if (request.getActivityType().equals(Const.PAUSE)) {
                // Fetch the last START activity to update its endTime
                List<QcTimeActivity> lastStartActivities = qcTimeActivityRepository.findByIsDeletedAndSubOrganizationIdAndItemItemIdAndQcEngineerIdAndQcActivityTypeActivityNameOrderByIdDesc(
                        false, loginUser.getSubOrgId(), request.getItemCode(), request.getOfficerId(), "START"
                );
                if (!lastStartActivities.isEmpty() && lastStartActivities.get(0).getEndTime() == null) {
                    QcTimeActivity lastStart = lastStartActivities.get(0);
                    lastStart.setEndTime(request.getActivityTime());
                    qcTimeActivityRepository.save(lastStart);
                }

                // Create a new PAUSE activity
                QcTimeActivity pauseActivity = new QcTimeActivity();
                pauseActivity.setQcEngineer(qcEngineer);
                QcActivityType activityName = qcActivityTypeRepository.findByIsDeletedAndActivityName(false, request.getActivityType());
                pauseActivity.setQcActivityType(activityName);
                pauseActivity.setItem(item);
                pauseActivity.setStartTime(request.getActivityTime());
                pauseActivity.setSubOrganizationId(loginUser.getSubOrgId());
                pauseActivity.setIsDeleted(false);

                qcTimeActivityRepository.save(pauseActivity);
            } else if (request.getActivityType().equals(Const.STOP)) {
                // Fetch the last START activity to update its endTime
                List<QcTimeActivity> lastStartActivities = qcTimeActivityRepository.findByIsDeletedAndSubOrganizationIdAndItemItemIdAndQcEngineerIdAndQcActivityTypeActivityNameOrderByIdDesc(
                        false, loginUser.getSubOrgId(), request.getItemCode(), request.getOfficerId(), "START"
                );

                if (!lastStartActivities.isEmpty() && lastStartActivities.get(0).getEndTime() == null) {
                    QcTimeActivity lastStart = lastStartActivities.get(0);
                    lastStart.setEndTime(request.getActivityTime());
                    qcTimeActivityRepository.save(lastStart);
                }

                // Create a new STOP activity
                QcTimeActivity stopActivity = new QcTimeActivity();
                stopActivity.setQcEngineer(qcEngineer);
                QcActivityType activityName = qcActivityTypeRepository.findByIsDeletedAndActivityName(false, request.getActivityType());
                stopActivity.setQcActivityType(activityName);
                stopActivity.setItem(item);
                stopActivity.setStartTime(request.getActivityTime());
                stopActivity.setEndTime(null); // Initially set endTime to NULL
                stopActivity.setSubOrganizationId(loginUser.getSubOrgId());
                stopActivity.setIsDeleted(false);

                qcTimeActivityRepository.save(stopActivity);
                ResponseMessage errorMessage = GlobalMessages.getResponseMessages(ServiceConstants.QUALITY_CONTROL_QUCN100020);
                baseResponse.setCode(errorMessage.getCode());
                baseResponse.setStatus(errorMessage.getStatus());
                baseResponse.setLogId(loginUser.getLogId());
                baseResponse.setMessage(errorMessage.getMessage());
                log.info(ServiceConstants.CALCULATE_QC_TIME_ACTIVITY, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.CALCULATE_QC_TIME_ACTIVITY_SUCCESSFULLY);
            }

        } catch (Exception e) {
            ResponseMessage errorMessage = GlobalMessages.getResponseMessages(ServiceConstants.QUALITY_CONTROL_QUCN100021);
            baseResponse.setCode(errorMessage.getCode());
            baseResponse.setStatus(errorMessage.getStatus());
            baseResponse.setLogId(loginUser.getLogId());
            baseResponse.setMessage(errorMessage.getMessage());
            e.printStackTrace();
            long endTime = System.currentTimeMillis();
            log.error(ServiceConstants.CALCULATE_QC_TIME_ACTIVITY, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.ERROR_CALCULATE_QC_TIME_ACTIVITY_METHOD + (endTime - startTime), e);
        }
        long endTime = System.currentTimeMillis();
        log.info(ServiceConstants.CALCULATE_QC_TIME_ACTIVITY, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.CALCULATE_QC_TIME_ACTIVITY_METHOD_EXECUTED + (endTime - startTime));
        return baseResponse;
    }

    public static final Random random= new Random();

    public String grrNumber() {

        int randomNumber = random.nextInt(9999 - 1000 + 1) + 1000;
        return ("" + LocalDate.now() + randomNumber).trim().replace("-", "");

    }

    public String crrNumber() {
        int randomNumber = random.nextInt(9999 - 1000 + 1) + 1000;
        return ("" + LocalDate.now() + randomNumber).trim().replace("-", "");

    }

}
