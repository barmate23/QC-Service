package com.qualityControl.Request;

import lombok.Data;

import java.util.List;

@Data
public class QcEngineerRequest {
    private Integer acceptedRejectedContainerId;
    private List<String> acceptedSerialBatchNumber;
    private List<RejectedSerialBatchNumber> rejectedSerialBatchNumberList;
    private String itemCode;

}
