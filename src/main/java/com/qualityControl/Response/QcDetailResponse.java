package com.qualityControl.Response;


import com.qualityControl.Model.Users;
import lombok.Data;

import java.util.Date;

@Data
public class QcDetailResponse {

    private String itemCode;
    private String itemName;
    private Integer qcCertificateNumber;
    private Date qcDate;
    private Integer qcDocumentId;
    private String qcStatus;
    private Users qcEngineer;
    private Integer itemQuantity;
    private Integer remainingQuantity;
    private Integer acceptedRejectedId;


}
