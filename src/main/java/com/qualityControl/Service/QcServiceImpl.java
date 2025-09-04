package com.qualityControl.Service;

import com.qualityControl.Model.*;
import com.qualityControl.Repository.*;
import com.qualityControl.Request.QcRequest;
import com.qualityControl.Response.*;
import com.qualityControl.Utils.GlobalMessages;
import com.qualityControl.Utils.ServiceConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Service
public class QcServiceImpl implements QcService {

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

    @Override
    public BaseResponse<Users> getAllQcEngineers() {
        long startTime = System.currentTimeMillis();
        log.info(ServiceConstants.GET_ALL_QC_ENGINEER, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.GET_ALL_QC_ENGINEER_METHOD_START);
        BaseResponse baseResponse = new BaseResponse<>();
        try {
            log.info(ServiceConstants.GET_ALL_QC_ENGINEER , loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.SUB_ORGANISATION_ID + loginUser.getSubOrgId());
            List<Users> users = this.userRepository.findByIsDeletedAndSubOrganizationIdAndIsActiveAndModuleUserLicenceKeyLicenceLineSubModuleSubModuleCode(false, loginUser.getSubOrgId(), true, "QCEG");
            ResponseMessage errorMessage = GlobalMessages.getResponseMessages(ServiceConstants.QUALITY_CONTROL_QUCN100001);
            baseResponse.setCode(errorMessage.getCode());
            baseResponse.setStatus(errorMessage.getStatus());
            baseResponse.setLogId(loginUser.getLogId());
            baseResponse.setMessage(errorMessage.getMessage());
            baseResponse.setData(users);

            log.info(ServiceConstants.GET_ALL_QC_ENGINEER, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.SUCCESSFULLY_FETCHED_QCENGINEER_LIST);

        } catch (Exception e) {
            ResponseMessage errorMessage = GlobalMessages.getResponseMessages(ServiceConstants.QUALITY_CONTROL_QUCN100002);
            baseResponse.setCode(errorMessage.getCode());
            baseResponse.setData(null);
            baseResponse.setLogId(loginUser.getLogId());
            baseResponse.setStatus(errorMessage.getStatus());
            baseResponse.setMessage(errorMessage.getMessage());
            long endTime = System.currentTimeMillis();
            log.error(ServiceConstants.GET_ALL_QC_ENGINEER, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.ERROR_GET_ALL_QC_ENGINEER_METHOD + (endTime - startTime), e);
        }
        long endTime = System.currentTimeMillis();
        log.info(ServiceConstants.GET_ALL_QC_ENGINEER, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.GET_ALL_QC_ENGINEER_METHOD_EXECUTED + (endTime - startTime));
        return baseResponse;
    }

    @Override
    public BaseResponse<QcResponse> getCrrListData(Integer qcEngineer, Date startDate, Date endDate) {

        long startTime = System.currentTimeMillis();
        log.info(ServiceConstants.GET_CRR_LIST_DATA, loginUser.getLogId(), loginUser.getUserId(), "  GET CRR LIST DATA METHOD START ");

        List<QcResponse> qcResponse = new ArrayList<>();
        BaseResponse<QcResponse> baseResponse = new BaseResponse<>();

        try {

            log.info(ServiceConstants.GET_CRR_LIST_DATA , loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.SUB_ORGANISATION_ID + loginUser.getSubOrgId());
            List<CodeMapper> codeMapperList = codeMapperRepository.findByIsDeletedAndSubOrganizationId(false, loginUser.getSubOrgId());

            QcResponse qresponse = new QcResponse();

            if (qcEngineer != null && startDate != null && endDate != null) {

                for (CodeMapper codeMapper : codeMapperList) {
                    if (isQCRequired(codeMapper)) {

                        List<AcceptedRejectedContainer> acceptedRejectedContainers;
                        if (codeMapper.getIsAsn()) {
                            acceptedRejectedContainers = acceptedRejectedContainerRepository.findByIsDeletedAndSubOrganizationIdAndASNOrderLineAsnHeadIdAsnNumberAndQcEngineerId(false, loginUser.getSubOrgId(), codeMapper.getAsnNumber(), qcEngineer);
                        } else {
                            acceptedRejectedContainers = acceptedRejectedContainerRepository.findByIsDeletedAndSubOrganizationIdAndPurchaseOrderLinePurchaseOrderHeadPurchaseOrderNumberAndQcEngineerId(false, loginUser.getSubOrgId(), codeMapper.getPoNumber(), qcEngineer);
                        }
                        if (acceptedRejectedContainers != null && acceptedRejectedContainers.size()!=0) {
                            String codeMapperStatus = determineQcStatus(acceptedRejectedContainers);
                            if (codeMapperStatus.equals(ServiceConstants.NOT_STARTED) || codeMapperStatus.equals(ServiceConstants.IN_PROGRESS)) {
                                if (codeMapper.getCrrBarcodeNumber() != null && isQcEngineer(codeMapper) && codeMapper.getCrrCreateDate().compareTo(startDate) >= 0 && codeMapper.getCrrCreateDate().compareTo(endDate) <= 0) {
                                    qresponse.setCodeMapperId(codeMapper.getId());
                                    qresponse.setCrrBarcodeNumber(codeMapper.getCrrBarcodeNumber());
                                    qresponse.setCrrCreateDate(codeMapper.getCrrCreateDate());
                                    qresponse.setAssignStatus(assignStatus(codeMapper));
                                    qresponse.setQcStatus(determineQcStatus(acceptedRejectedContainers));
                                    qcResponse.add(qresponse);

                                }

                            }

                        }
                    }

                }

            } else if (qcEngineer != null) {

                for (CodeMapper codeMapper : codeMapperList) {
                    if (isQCRequired(codeMapper)) {

                        List<AcceptedRejectedContainer> acceptedRejectedContainers;
                        if (codeMapper.getIsAsn()) {
                            acceptedRejectedContainers = acceptedRejectedContainerRepository.findByIsDeletedAndSubOrganizationIdAndASNOrderLineAsnHeadIdAsnNumberAndQcEngineerId(false, loginUser.getSubOrgId(), codeMapper.getAsnNumber(), qcEngineer);
                        } else {
                            acceptedRejectedContainers = acceptedRejectedContainerRepository.findByIsDeletedAndSubOrganizationIdAndPurchaseOrderLinePurchaseOrderHeadPurchaseOrderNumberAndQcEngineerId(false, loginUser.getSubOrgId(), codeMapper.getPoNumber(), qcEngineer);
                        }

                        if(acceptedRejectedContainers!=null && acceptedRejectedContainers.size()!=0) {
                            String codeMapperStatus = determineQcStatus(acceptedRejectedContainers);
                            if (codeMapperStatus.equals(ServiceConstants.NOT_STARTED) || codeMapperStatus.equals(ServiceConstants.IN_PROGRESS)) {
                                if (codeMapper.getCrrBarcodeNumber() != null && isQcEngineer(codeMapper)) {
                                    qresponse.setCodeMapperId(codeMapper.getId());
                                    qresponse.setCrrBarcodeNumber(codeMapper.getCrrBarcodeNumber());
                                    qresponse.setCrrCreateDate(codeMapper.getCrrCreateDate());
                                    qresponse.setAssignStatus(assignStatus(codeMapper));
                                    qresponse.setQcStatus(determineQcStatus(acceptedRejectedContainers));
                                    qcResponse.add(qresponse);

                                }

                            }
                        }

                    }

                }

            } else if (startDate != null && endDate != null) {

                for (CodeMapper codeMapper : codeMapperList) {
                    if (isQCRequired(codeMapper)) {

                        List<AcceptedRejectedContainer> acceptedRejectedContainers;
                        if (codeMapper.getIsAsn()) {
                            acceptedRejectedContainers = acceptedRejectedContainerRepository.findByIsDeletedAndSubOrganizationIdAndASNOrderLineAsnHeadIdAsnNumber(false, loginUser.getSubOrgId(), codeMapper.getAsnNumber());
                        } else {
                            acceptedRejectedContainers = acceptedRejectedContainerRepository.findByIsDeletedAndSubOrganizationIdAndPurchaseOrderLinePurchaseOrderHeadPurchaseOrderNumber(false, loginUser.getSubOrgId(), codeMapper.getPoNumber());
                        }

                        if(acceptedRejectedContainers!=null && acceptedRejectedContainers.size()!=0) {
                            String codeMapperStatus = determineQcStatus(acceptedRejectedContainers);
                            if (codeMapperStatus.equals(ServiceConstants.NOT_STARTED) || codeMapperStatus.equals(ServiceConstants.IN_PROGRESS)) {
                                if (codeMapper.getCrrBarcodeNumber() != null && codeMapper.getCrrCreateDate().compareTo(startDate) >= 0 && codeMapper.getCrrCreateDate().compareTo(endDate) <= 0) {
                                    qresponse.setCodeMapperId(codeMapper.getId());
                                    qresponse.setCrrBarcodeNumber(codeMapper.getCrrBarcodeNumber());
                                    qresponse.setCrrCreateDate(codeMapper.getCrrCreateDate());
                                    qresponse.setAssignStatus(assignStatus(codeMapper));
                                    qresponse.setQcStatus(determineQcStatus(acceptedRejectedContainers));
                                    qcResponse.add(qresponse);

                                }

                            }
                        }

                    }

                }

            } else {

                for (CodeMapper codeMapper : codeMapperList) {
                    if (isQCRequired(codeMapper)) {

                        List<AcceptedRejectedContainer> acceptedRejectedContainers;
                        if (codeMapper.getIsAsn()) {
                            acceptedRejectedContainers = acceptedRejectedContainerRepository.findByIsDeletedAndSubOrganizationIdAndASNOrderLineAsnHeadIdAsnNumber(false, loginUser.getSubOrgId(), codeMapper.getAsnNumber());
                        } else {
                            acceptedRejectedContainers = acceptedRejectedContainerRepository.findByIsDeletedAndSubOrganizationIdAndPurchaseOrderLinePurchaseOrderHeadPurchaseOrderNumber(false, loginUser.getSubOrgId(), codeMapper.getPoNumber());
                        }

                        if(acceptedRejectedContainers!=null && acceptedRejectedContainers.size()!=0){
                            String codeMapperStatus = determineQcStatus(acceptedRejectedContainers);
                            if (codeMapperStatus.equals(ServiceConstants.NOT_STARTED) || codeMapperStatus.equals(ServiceConstants.IN_PROGRESS)) {
                                if (codeMapper.getCrrBarcodeNumber() != null) {
                                    qresponse.setCodeMapperId(codeMapper.getId());
                                    qresponse.setCrrBarcodeNumber(codeMapper.getCrrBarcodeNumber());
                                    qresponse.setCrrCreateDate(codeMapper.getCrrCreateDate());
                                    qresponse.setAssignStatus(assignStatus(codeMapper));
                                    qresponse.setQcStatus(determineQcStatus(acceptedRejectedContainers));
                                    qcResponse.add(qresponse);

                                }
                            }

                        }

                    }

                }


            }


            ResponseMessage errorMessage = GlobalMessages.getResponseMessages(ServiceConstants.QUALITY_CONTROL_QUCN100003);
            baseResponse.setCode(errorMessage.getCode());
            baseResponse.setMessage(errorMessage.getMessage());
            baseResponse.setLogId(loginUser.getLogId());
            baseResponse.setData(qcResponse);
            baseResponse.setStatus(errorMessage.getStatus());
            log.info(ServiceConstants.GET_CRR_LIST_DATA, loginUser.getLogId(), loginUser.getUserId(), " CRR LIST FETCH SUCCESSFULLY ");

        } catch (Exception e) {
            e.printStackTrace();
            ResponseMessage errorMessage = GlobalMessages.getResponseMessages(ServiceConstants.QUALITY_CONTROL_QUCN100004);
            baseResponse.setCode(errorMessage.getCode());
            baseResponse.setMessage(errorMessage.getMessage());
            baseResponse.setLogId(loginUser.getLogId());
            baseResponse.setStatus(errorMessage.getStatus());
            long endTime = System.currentTimeMillis();
            log.error(ServiceConstants.GET_CRR_LIST_DATA, loginUser.getLogId(), loginUser.getUserId(), " ERROR OCCURS AT FETCHING CRR NUMBER LIST OF USER EXECUTED TIME " + (endTime - startTime), e);
        }
        long endTime = System.currentTimeMillis();
        log.info(ServiceConstants.GET_CRR_LIST_DATA, loginUser.getLogId(), loginUser.getUserId(), " GET CRR LIST DATA METHOD EXECUTED    EXECUTED TIME " + (endTime - startTime));
        return baseResponse;
    }

    @Override
    public BaseResponse<QcDetailResponse> getDetailByCrr(Long codeMapperId) {

        long startTime = System.currentTimeMillis();
        log.info(ServiceConstants.GET_DETAIL_BY_CRR, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.GET_DETAIL_BY_CRR_METHOD_START);

        List<QcDetailResponse> qcDetailResponse = new ArrayList<>();
        BaseResponse<QcDetailResponse> baseResponse = new BaseResponse<>();

        try {

            log.info(ServiceConstants.GET_DETAIL_BY_CRR , loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.SUB_ORGANISATION_ID + loginUser.getSubOrgId());
            CodeMapper codeMapper = codeMapperRepository.findByIsDeletedAndSubOrganizationIdAndId(false, loginUser.getSubOrgId(), codeMapperId);

            if (codeMapper.getIsAsn()) {

                List<AcceptedRejectedContainer> acceptedRejectedContainer = acceptedRejectedContainerRepository.findByIsDeletedAndSubOrganizationIdAndASNOrderLineAsnHeadIdAsnNumberAndIsAccepted(false, loginUser.getSubOrgId(), codeMapper.getAsnNumber(), true);
                QCCertificate qcCertificate;
                for (AcceptedRejectedContainer container : acceptedRejectedContainer) {
                    qcCertificate = qcCertificateRepository.findByIsDeletedAndSubOrganizationIdAndAsnLineId(false, loginUser.getSubOrgId(), container.getASNOrderLine().getId());

                    QcDetailResponse qcdetailResponse = new QcDetailResponse();
                    qcdetailResponse.setItemCode(container.getASNOrderLine().getItem().getItemCode());
                    qcdetailResponse.setItemName(container.getASNOrderLine().getItem().getName());
                    qcdetailResponse.setQcCertificateNumber(qcCertificate.getQcNumber());
                    qcdetailResponse.setQcDate(qcCertificate.getQcDate());
                    qcdetailResponse.setQcDocumentId(qcCertificate.getQcDocumentId());
                    qcdetailResponse.setItemQuantity(container.getASNOrderLine().getInvoiceQuantity());

                    //QCSTATUS
                    CommonMaster qcStatus = commonMasterRepository.findByIsDeletedAndSubOrganizationIdAndMasterValue(false, loginUser.getSubOrgId(), "STAGINGPUT");
                    if(qcStatus != null) {
                        container.setStatus(qcStatus);
                        qcdetailResponse.setQcStatus(ServiceConstants.NOT_STARTED);
                        acceptedRejectedContainerRepository.save(container);
                    }
                    if (container.getQcEngineer() != null) {
                        qcdetailResponse.setQcEngineer(container.getQcEngineer());
                    }
                    qcdetailResponse.setAcceptedRejectedId(container.getId());

                    qcDetailResponse.add(qcdetailResponse);
                }


            } else {

                List<AcceptedRejectedContainer> acceptedRejectedContainer = acceptedRejectedContainerRepository.findByIsDeletedAndSubOrganizationIdAndPurchaseOrderLinePurchaseOrderHeadPurchaseOrderNumberAndIsAccepted(false, loginUser.getSubOrgId(), codeMapper.getPoNumber(), true);
                QCCertificate qcCertificate;
                for (AcceptedRejectedContainer container : acceptedRejectedContainer) {
                    qcCertificate = qcCertificateRepository.findByIsDeletedAndSubOrganizationIdAndPurchaseOrderLineId(false, loginUser.getSubOrgId(), container.getPurchaseOrderLine().getId());

                    QcDetailResponse qcdetailResponse = new QcDetailResponse();
                    qcdetailResponse.setItemCode(container.getPurchaseOrderLine().getItem().getItemCode());
                    qcdetailResponse.setItemName(container.getPurchaseOrderLine().getItem().getName());
                    qcdetailResponse.setQcCertificateNumber(qcCertificate.getQcNumber());
                    qcdetailResponse.setQcDate(qcCertificate.getQcDate());
                    qcdetailResponse.setQcDocumentId(qcCertificate.getQcDocumentId());
                    qcdetailResponse.setItemQuantity(container.getPurchaseOrderLine().getInvoiceQuantity());
                    qcdetailResponse.setRemainingQuantity(container.getPurchaseOrderLine().getRemainingQuantity());

                    //QCSTATUS
                    CommonMaster qcStatus = commonMasterRepository.findByIsDeletedAndSubOrganizationIdAndMasterValue(false, loginUser.getSubOrgId(), "QCNOTSTARTED");
                    if(qcStatus != null){
                        container.setStatus(qcStatus);
                        qcdetailResponse.setQcStatus(ServiceConstants.NOT_STARTED);
                        acceptedRejectedContainerRepository.save(container);
                    }
                    if (container.getQcEngineer() != null) {
                        qcdetailResponse.setQcEngineer(container.getQcEngineer());
                    }
                    qcdetailResponse.setAcceptedRejectedId(container.getId());

                    qcDetailResponse.add(qcdetailResponse);
                }

            }

            ResponseMessage errorMessage = GlobalMessages.getResponseMessages(ServiceConstants.QUALITY_CONTROL_QUCN100005);
            baseResponse.setCode(errorMessage.getCode());
            baseResponse.setMessage(errorMessage.getMessage());
            baseResponse.setLogId(loginUser.getLogId());
            baseResponse.setData(qcDetailResponse);
            baseResponse.setStatus(errorMessage.getStatus());
            log.info(ServiceConstants.GET_DETAIL_BY_CRR, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.CRR_DETAIL_FETCH_SUCCESSFULLY);

        } catch (Exception e) {
            e.printStackTrace();
            ResponseMessage errorMessage = GlobalMessages.getResponseMessages(ServiceConstants.QUALITY_CONTROL_QUCN100006);
            baseResponse.setCode(errorMessage.getCode());
            baseResponse.setMessage(errorMessage.getMessage());
            baseResponse.setLogId(loginUser.getLogId());
            baseResponse.setStatus(errorMessage.getStatus());
            long endTime = System.currentTimeMillis();
            log.error(ServiceConstants.GET_DETAIL_BY_CRR, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.ERROR_GET_DETAIL_BY_CRR_METHOD + (endTime - startTime), e);
        }
        long endTime = System.currentTimeMillis();
        log.info(ServiceConstants.GET_DETAIL_BY_CRR, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.GET_DETAIL_BY_CRR_METHOD_EXECUTED + (endTime - startTime));
        return baseResponse;
    }

    @Override
    public BaseResponse<AcceptedRejectedContainer> assignQcEngineer(QcRequest qcRequest) {
        long startTime = System.currentTimeMillis();
        log.info(ServiceConstants.ASSIGN_QC_ENGINEER , loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.ASSIGN_QC_ENGINEER_METHOD_START);
        BaseResponse<AcceptedRejectedContainer> baseResponse = new BaseResponse<>();
        List<AcceptedRejectedContainer> accptRejtContainers = new ArrayList<>();
        try {
            log.info(ServiceConstants.ASSIGN_QC_ENGINEER , loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.SUB_ORGANISATION_ID + loginUser.getSubOrgId());
            AcceptedRejectedContainer acceptedRejectedContainer = acceptedRejectedContainerRepository.findByIsDeletedAndSubOrganizationIdAndId(false, loginUser.getSubOrgId(), qcRequest.getAcceptedRejectedContainerId());
            Optional<Users> users = userRepository.findByIdAndIsDeleted(qcRequest.getQcEngineer(), false);
            UserShiftMapper userShiftMappers = userShiftRepository.findByIsDeletedAndSubOrganizationIdAndUserUserId(false, loginUser.getSubOrgId(), users.get().getUserId());

            if (users.isPresent() && userShiftMappers != null) {
                Shift userShift = userShiftMappers.getShift();
                Timestamp shiftStart = userShift.getShiftStart();
                Timestamp shiftEnd = userShift.getShiftEnd();
                Date qcAssignDate = qcRequest.getQcAssignDate();

                Timestamp qcStartTime = new Timestamp(qcAssignDate.getTime() + qcRequest.getStartTime().getTime());
                Timestamp qcEndTime = new Timestamp(qcAssignDate.getTime() + qcRequest.getEndTime().getTime());

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
                        acceptedRejectedContainer.setQcEngineer(users.get());
                        acceptedRejectedContainer.setQcDate(qcRequest.getQcAssignDate());
                        acceptedRejectedContainer.setStartTime(qcRequest.getStartTime());
                        acceptedRejectedContainer.setEndTime(qcRequest.getEndTime());
                        accptRejtContainers.add(acceptedRejectedContainer);
                        acceptedRejectedContainerRepository.save(acceptedRejectedContainer);

                        ResponseMessage errorMessage = GlobalMessages.getResponseMessages(ServiceConstants.QUALITY_CONTROL_QUCN100007);
                        baseResponse.setCode(errorMessage.getCode());
                        baseResponse.setMessage(errorMessage.getMessage());
                        baseResponse.setLogId(loginUser.getLogId());
                        baseResponse.setData(accptRejtContainers);
                        baseResponse.setStatus(errorMessage.getStatus());
                        log.info(ServiceConstants.ASSIGN_QC_ENGINEER, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.QC_OFFICER_ASSIGN_SUCCESSFULLY);
                    } else {
                        baseResponse.setCode(0);
                        baseResponse.setMessage("The selected QC Officer is not available for the selected time slot ");
                        baseResponse.setLogId(loginUser.getLogId());
                        baseResponse.setStatus(500);
                        log.info(ServiceConstants.ASSIGN_QC_ENGINEER, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.QC_OFFICER_NOT_AVAILABLE);
                    }
                } else {
                    baseResponse.setCode(0);
                    baseResponse.setMessage("The selected QC Engineer is not available for the assigned QC date ");
                    baseResponse.setLogId(loginUser.getLogId());
                    baseResponse.setStatus(500);
                    log.info(ServiceConstants.ASSIGN_QC_ENGINEER, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.QC_OFFICER_NOT_AVAILABLE);
                }
            }
        } catch (Exception e) {
            ResponseMessage errorMessage = GlobalMessages.getResponseMessages(ServiceConstants.QUALITY_CONTROL_QUCN100008);
            baseResponse.setCode(errorMessage.getCode());
            baseResponse.setMessage(errorMessage.getMessage());
            baseResponse.setLogId(loginUser.getLogId());
            baseResponse.setStatus(errorMessage.getStatus());
            e.printStackTrace();
            long endTime = System.currentTimeMillis();
            log.error(ServiceConstants.ASSIGN_QC_ENGINEER, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.ERROR_ASSIGN_QC_ENGINEER_METHOD + (endTime - startTime), e);
        }
        long endTime = System.currentTimeMillis();
        log.info(ServiceConstants.ASSIGN_QC_ENGINEER, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.ASSIGN_QC_ENGINEER_METHOD_EXECUTED + (endTime - startTime));
        return baseResponse;
    }


    @Override
    public BaseResponse<QcItemResponse> getItemDetail(Integer acceptedRejectedContainerId) {

        long startTime = System.currentTimeMillis();
        log.info(ServiceConstants.GET_ITEM_DETAIL, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.GET_ITEM_DETAIL_METHOD_QC_MANAGER_START);
        QcItemResponse qcItemResponse = new QcItemResponse();
        BaseResponse<QcItemResponse> baseResponse = new BaseResponse<>();
        List<QcItemResponse> qcItemResponses = new ArrayList<>();

        try {
            log.info(ServiceConstants.GET_ITEM_DETAIL , loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.SUB_ORGANISATION_ID + loginUser.getSubOrgId());
            AcceptedRejectedContainer accptRejtContainer = acceptedRejectedContainerRepository.findByIsDeletedAndSubOrganizationIdAndId(false, loginUser.getSubOrgId(), acceptedRejectedContainerId);

            if (accptRejtContainer.getASNOrderLine() != null) {

                ASNOrderLine asnOrderLine = accptRejtContainer.getASNOrderLine();
//                        asnLineRepository.findByIsDeletedAndSubOrganizationIdAndId(false,loginUser.getSubOrgId(),accptRejtContainer.getASNOrderLine().getId());
                QCCertificate qcCertificate = qcCertificateRepository.findByIsDeletedAndSubOrganizationIdAndAsnLineId(false, loginUser.getSubOrgId(), asnOrderLine.getId());

                List<SerialBatchNumbers> serialBatchNumbers = serialBatchNumberRepository.findByIsDeletedAndSubOrganizationIdAndAsnLineId(false, loginUser.getSubOrgId(), asnOrderLine.getId());

                qcItemResponse.setItemCode(asnOrderLine.getItem().getItemCode());
                qcItemResponse.setItemName(asnOrderLine.getItem().getName());
                qcItemResponse.setItemQuantity(asnOrderLine.getInvoiceQuantity());
                qcItemResponse.setSupplierQcNumber(qcCertificate.getQcNumber());
                qcItemResponse.setSupplierQcDate(qcCertificate.getQcDate());
                qcItemResponse.setSupplierQcCertificate(qcCertificate.getQcDocumentId());
                qcItemResponse.setSerialBatchNumber(serialBatchNumbers);

                qcItemResponses.add(qcItemResponse);

            } else {
                PurchaseOrderLine poLine = purchaseOrderLineRepository.findByIsDeletedAndSubOrganizationIdAndId(false, loginUser.getSubOrgId(), accptRejtContainer.getPurchaseOrderLine().getId());
                QCCertificate qcCertificate = qcCertificateRepository.findByIsDeletedAndSubOrganizationIdAndPurchaseOrderLineId(false, loginUser.getSubOrgId(), poLine.getId());
                List<SerialBatchNumbers> serialBatchNumbers = serialBatchNumberRepository.findByIsDeletedAndSubOrganizationIdAndPurchaseOrderLineId(false, loginUser.getSubOrgId(), poLine.getId());


                qcItemResponse.setItemCode(poLine.getItem().getItemCode());
                qcItemResponse.setItemName(poLine.getItem().getName());
                qcItemResponse.setItemQuantity(poLine.getInvoiceQuantity());
                qcItemResponse.setSupplierQcNumber(qcCertificate.getQcNumber());
                qcItemResponse.setSupplierQcDate(qcCertificate.getQcDate());
                qcItemResponse.setSupplierQcCertificate(qcCertificate.getQcDocumentId());
                qcItemResponse.setSerialBatchNumber(serialBatchNumbers);

                qcItemResponses.add(qcItemResponse);

            }

            ResponseMessage errorMessage = GlobalMessages.getResponseMessages(ServiceConstants.QUALITY_CONTROL_QUCN100009);
            baseResponse.setCode(errorMessage.getCode());
            baseResponse.setStatus(errorMessage.getStatus());
            baseResponse.setData(qcItemResponses);
            baseResponse.setLogId(loginUser.getLogId());
            baseResponse.setMessage(errorMessage.getMessage());
            log.info(ServiceConstants.GET_ITEM_DETAIL, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.ITEM_DETAIL_FETCH_SUCCESSFULLY);

        } catch (Exception e) {

            ResponseMessage errorMessage = GlobalMessages.getResponseMessages(ServiceConstants.QUALITY_CONTROL_QUCN100010);
            baseResponse.setCode(errorMessage.getCode());
            baseResponse.setStatus(errorMessage.getStatus());
            baseResponse.setLogId(loginUser.getLogId());
            baseResponse.setMessage(errorMessage.getMessage());
            e.printStackTrace();
            long endTime = System.currentTimeMillis();
            log.error(ServiceConstants.GET_ITEM_DETAIL, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.ERROR_GET_ITEM_DETAIL_METHOD_QC_MANAGER + (endTime - startTime), e);
        }
        long endTime = System.currentTimeMillis();
        log.info(ServiceConstants.GET_ITEM_DETAIL, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.GET_ITEM_DETAIL_METHOD_QC_MANAGER_EXECUTED + (endTime - startTime));
        return baseResponse;
    }

    @Override
    public BaseResponse<Shift> getShift() {

        long startTime = System.currentTimeMillis();
        log.info(ServiceConstants.GET_SHIFT, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.GET_SHIFT_METHOD_START);
        BaseResponse<Shift> baseResponse = new BaseResponse<>();

        try {
            log.info(ServiceConstants.GET_SHIFT , loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.SUB_ORGANISATION_ID + loginUser.getSubOrgId());
            List<Shift> shiftList = shiftRepository.findByIsDeletedAndSubOrganizationId(false, loginUser.getSubOrgId());
            if (!shiftList.isEmpty()) {

                ResponseMessage errorMessage = GlobalMessages.getResponseMessages(ServiceConstants.QUALITY_CONTROL_QUCN100011);
                baseResponse.setCode(errorMessage.getCode());
                baseResponse.setLogId(loginUser.getLogId());
                baseResponse.setStatus(errorMessage.getStatus());
                baseResponse.setData(shiftList);
                baseResponse.setMessage(errorMessage.getMessage());
                log.info(ServiceConstants.GET_SHIFT, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.ALL_SHIFT_FETCH_SUCCESSFULLY);
            } else {

                ResponseMessage errorMessage = GlobalMessages.getResponseMessages(ServiceConstants.QUALITY_CONTROL_QUCN100012);
                baseResponse.setCode(errorMessage.getCode());
                baseResponse.setLogId(loginUser.getLogId());
                baseResponse.setStatus(errorMessage.getStatus());
                baseResponse.setMessage(errorMessage.getMessage());
            }

        } catch (Exception e) {

            ResponseMessage errorMessage = GlobalMessages.getResponseMessages(ServiceConstants.QUALITY_CONTROL_QUCN100013);
            baseResponse.setCode(errorMessage.getCode());
            baseResponse.setLogId(loginUser.getLogId());
            baseResponse.setStatus(errorMessage.getStatus());
            baseResponse.setMessage(errorMessage.getMessage());
            e.printStackTrace();
            long endTime = System.currentTimeMillis();
            log.error(ServiceConstants.GET_SHIFT, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.ERROR_GET_SHIFT_METHOD + (endTime - startTime), e);
        }
        long endTime = System.currentTimeMillis();
        log.info(ServiceConstants.GET_SHIFT, loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.GET_SHIFT_METHOD_EXECUTED + (endTime - startTime));
        return baseResponse;
    }

    @Override
    public BaseResponse<QcViewResponse> getByFilter(String qcDate, String shiftFilterName) {
        long startTime = System.currentTimeMillis();
        log.info(ServiceConstants.GET_BY_FILTER, loginUser.getLogId(), loginUser.getUserId(), " GET BY FILTER METHOD START ");

        BaseResponse<QcViewResponse> baseResponse = new BaseResponse<>();
        Map<Users, QcViewResponse> qcViewResponseMap = new HashMap<>();

        try {
            log.info(ServiceConstants.GET_BY_FILTER , loginUser.getLogId(), loginUser.getUserId(), ServiceConstants.SUB_ORGANISATION_ID + loginUser.getSubOrgId());

            List<UserShiftMapper> userShiftMappers = null;

            if (qcDate != null && shiftFilterName != null) {
                userShiftMappers = userShiftRepository.findByIsDeletedAndSubOrganizationIdAndShiftShiftNameAndUserModuleUserLicenceKeyLicenceLineSubModuleSubModuleCode(false, loginUser.getSubOrgId(), shiftFilterName, "QCEG");
            } else if (shiftFilterName != null) {
                userShiftMappers = userShiftRepository.findByIsDeletedAndSubOrganizationIdAndShiftShiftNameAndUserModuleUserLicenceKeyLicenceLineSubModuleSubModuleCode(false, loginUser.getSubOrgId(), shiftFilterName, "QCEG");
            }

            if (userShiftMappers != null && !userShiftMappers.isEmpty()) {
                for (UserShiftMapper userShiftMapper : userShiftMappers) {
                    processContainers(qcDate, userShiftMapper.getUser(), qcViewResponseMap);
                }
            } else if (qcDate != null) {
                List<AcceptedRejectedContainer> acceptedRejectedContainers = acceptedRejectedContainerRepository.findByIsDeletedAndSubOrganizationIdAndQcDateAndQcEngineerIsNotNull(false, loginUser.getSubOrgId(), qcDate);
                if (acceptedRejectedContainers != null && !acceptedRejectedContainers.isEmpty()) {
                    for (AcceptedRejectedContainer container : acceptedRejectedContainers) {
                        processContainer(container, qcViewResponseMap);
                    }
                }
            }

            List<QcViewResponse> qcViewResponses = new ArrayList<>(qcViewResponseMap.values());

            ResponseMessage successMessage = GlobalMessages.getResponseMessages(ServiceConstants.QUALITY_CONTROL_QUCN100014);
            baseResponse.setCode(successMessage.getCode());
            baseResponse.setStatus(successMessage.getStatus());
            baseResponse.setData(qcViewResponses);
            baseResponse.setLogId(loginUser.getLogId());
            baseResponse.setMessage(successMessage.getMessage());
            log.info(ServiceConstants.GET_BY_FILTER, loginUser.getLogId(), loginUser.getUserId(), " QC VIEW DETAIL FETCH SUCCESSFULLY ");
        } catch (Exception e) {
            ResponseMessage errorMessage = GlobalMessages.getResponseMessages(ServiceConstants.QUALITY_CONTROL_QUCN100015);
            baseResponse.setCode(errorMessage.getCode());
            baseResponse.setStatus(errorMessage.getStatus());
            baseResponse.setLogId(loginUser.getLogId());
            baseResponse.setMessage(errorMessage.getMessage());
            e.printStackTrace();
            long endTime = System.currentTimeMillis();
            log.error(ServiceConstants.GET_BY_FILTER, loginUser.getLogId(), loginUser.getUserId(), " ERROR OCCURS AT QC VIEW DETAIL METHOD    EXECUTED TIME " + (endTime - startTime), e);
        }
        long endTime = System.currentTimeMillis();
        log.info(ServiceConstants.GET_BY_FILTER, loginUser.getLogId(), loginUser.getUserId(), " AT GET BY FILTER METHOD EXECUTED    EXECUTED TIME " + (endTime - startTime));
        return baseResponse;
    }

    private void processContainers(String qcDate, Users qcEngineer, Map<Users, QcViewResponse> qcViewResponseMap) {
        List<AcceptedRejectedContainer> accptRejContList;
        if (qcDate != null) {
            accptRejContList = acceptedRejectedContainerRepository.findByIsDeletedAndSubOrganizationIdAndQcEngineerAndQcDate(false, loginUser.getSubOrgId(), qcEngineer, qcDate);
        } else {
            accptRejContList = acceptedRejectedContainerRepository.findByIsDeletedAndSubOrganizationIdAndQcEngineer(false, loginUser.getSubOrgId(), qcEngineer);
        }

        if (accptRejContList != null && !accptRejContList.isEmpty()) {
            for (AcceptedRejectedContainer container : accptRejContList) {
                processContainer(container, qcViewResponseMap);
            }
        }
    }

    private void processContainer(AcceptedRejectedContainer container, Map<Users, QcViewResponse> qcViewResponseMap) {
        Users qcEngineer = container.getQcEngineer();

        QcViewResponse qcViewResponse = qcViewResponseMap.computeIfAbsent(qcEngineer, k -> {
            QcViewResponse newQcViewResponse = new QcViewResponse();
            newQcViewResponse.setQcEngineer(qcEngineer);
            newQcViewResponse.setSchedulestatus(new ArrayList<>());
            newQcViewResponse.setActualstatus(new ArrayList<>());
            return newQcViewResponse;
        });

        qcViewResponse.getSchedulestatus().add(container);

        List<QcTimeActivity> qcTimeActivity;
        if (container.getASNOrderLine() != null) {
            qcTimeActivity = qcTimeActivityRepository.findByIsDeletedAndSubOrganizationIdAndItemIdAndQcEngineerIdOrderByIdAsc(false, loginUser.getSubOrgId(), container.getASNOrderLine().getItem().getId(), container.getQcEngineer().getId());
        } else {
            qcTimeActivity = qcTimeActivityRepository.findByIsDeletedAndSubOrganizationIdAndItemIdAndQcEngineerIdOrderByIdAsc(false, loginUser.getSubOrgId(), container.getPurchaseOrderLine().getItem().getId(), container.getQcEngineer().getId());
        }
        qcViewResponse.getActualstatus().addAll(qcTimeActivity);
    }



    public String assignStatus(CodeMapper codeMapper) {

        String assignStatus;

        List<AcceptedRejectedContainer> acceptedRejectedContainers;
        if (codeMapper.getIsAsn()) {
            acceptedRejectedContainers = acceptedRejectedContainerRepository.findByIsDeletedAndSubOrganizationIdAndASNOrderLineAsnHeadIdAsnNumber(false, loginUser.getSubOrgId(), codeMapper.getAsnNumber());
        } else {
            acceptedRejectedContainers = acceptedRejectedContainerRepository.findByIsDeletedAndSubOrganizationIdAndPurchaseOrderLinePurchaseOrderHeadPurchaseOrderNumber(false, loginUser.getSubOrgId(), codeMapper.getPoNumber());
        }

        Integer totalCount = acceptedRejectedContainers.size();
        Integer assignCount = 0;

        for (AcceptedRejectedContainer acceptedRejectedContainer : acceptedRejectedContainers) {
            if (acceptedRejectedContainer.getQcEngineer() != null) {
                assignCount++;
            }
        }

        if (assignCount == 0) {
            assignStatus = ServiceConstants.UN_ASSIGN;
        } else if (assignCount == totalCount) {
            assignStatus = ServiceConstants.ASSIGN;
        } else {
            assignStatus = ServiceConstants.PARTIALLY_ASSIGNED;
        }
        return assignStatus;
    }

    private String determineQcStatus(List<AcceptedRejectedContainer> acceptedRejectedContainers) {

        boolean anyNullStatus = acceptedRejectedContainers.stream()
                .anyMatch(container -> container.getStatus() == null || container.getStatus().getMasterValue() == null);

        if (anyNullStatus) {
            return ServiceConstants.NOT_STARTED;
        }

        boolean allNotStarted = acceptedRejectedContainers.stream().allMatch(container -> ServiceConstants.STAGINGPUT.equals(container.getStatus().getMasterValue()));
        boolean allQcComplete = acceptedRejectedContainers.stream().allMatch(container -> ServiceConstants.QCCOMPLETED.equals(container.getStatus().getMasterValue()));
        boolean allInProgress = acceptedRejectedContainers.stream().allMatch(container -> ServiceConstants.QCINPROGRESS.equals(container.getStatus().getMasterValue()));

        boolean anyNotStarted = acceptedRejectedContainers.stream().anyMatch(container -> ServiceConstants.STAGINGPUT.equals(container.getStatus().getMasterValue()));
        boolean anyInProgress = acceptedRejectedContainers.stream().anyMatch(container -> ServiceConstants.QCINPROGRESS.equals(container.getStatus().getMasterValue()));


        if (allNotStarted) {
            return ServiceConstants.NOT_STARTED;
        } else if (allQcComplete) {
            return ServiceConstants.COMPLETED;
        } else if(allInProgress){
            return ServiceConstants.IN_PROGRESS;
        } else if(anyNotStarted){
            return ServiceConstants.NOT_STARTED;
        } else if(anyInProgress){
            return ServiceConstants.IN_PROGRESS;
        }
        return ServiceConstants.NOT_STARTED;
    }

    public Boolean isQcEngineer(CodeMapper codeMapper) {

        Boolean isQcEngineer = false;
        List<AcceptedRejectedContainer> acceptedRejectedContainers;
        if (codeMapper.getIsAsn()) {
            acceptedRejectedContainers = acceptedRejectedContainerRepository.findByIsDeletedAndSubOrganizationIdAndASNOrderLineAsnHeadIdAsnNumber(false, loginUser.getSubOrgId(), codeMapper.getAsnNumber());
        } else {
            acceptedRejectedContainers = acceptedRejectedContainerRepository.findByIsDeletedAndSubOrganizationIdAndPurchaseOrderLinePurchaseOrderHeadPurchaseOrderNumber(false, loginUser.getSubOrgId(), codeMapper.getPoNumber());
        }

        for (AcceptedRejectedContainer acceptedRejectedContainer : acceptedRejectedContainers) {
            if (acceptedRejectedContainer.getQcEngineer() != null) {
                isQcEngineer = true;
            }

        }
        return isQcEngineer;
    }

    private boolean isQCRequired(CodeMapper codeMapper) {
        boolean isQcRequired = false;
        if (codeMapper.getIsAsn()) {
            List<ASNOrderLine> asnLineList = asnLineRepository.findByAsnHeadIdAsnNumberAndSubOrganizationIdAndIsDeletedAndItemQcRequired(codeMapper.getAsnNumber(), loginUser.getSubOrgId(), false,true);

            if(!asnLineList.isEmpty()){
                isQcRequired=true;
            }

        } else {
            List<PurchaseOrderLine> purchaseOrderLineList = purchaseOrderLineRepository.findByPurchaseOrderHeadPurchaseOrderNumberAndSubOrganizationIdAndIsDeletedAndItemQcRequired(codeMapper.getPoNumber(), loginUser.getSubOrgId(), false,true);

            if(!purchaseOrderLineList.isEmpty()){
                isQcRequired=true;
            }
        }
        return isQcRequired;
    }



}
