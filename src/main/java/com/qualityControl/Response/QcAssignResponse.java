package com.qualityControl.Response;

import lombok.Data;

import java.util.Date;

@Data
public class QcAssignResponse {

    private Integer crrNumber;
    private Date crrDate;
    private String itemCode;
    private String itemName;
    private Integer itemQuantity;
    private Integer itemRemaining;
    private Date qcAssignDate;
    private Integer qcEngineer;
    private Date startTime;
    private Date endTime;
}
