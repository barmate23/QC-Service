package com.qualityControl.Response;

import com.qualityControl.Model.SerialBatchNumbers;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class QcItemResponse {

    private String itemCode;
    private String itemName;
    private Integer itemQuantity;
    private Integer supplierQcNumber;
    private Date supplierQcDate;
    private Integer internalQcNumber;
    private Date internalQcDate;
    private Integer supplierQcCertificate;
    private List<SerialBatchNumbers> serialBatchNumber;

}
