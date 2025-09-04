package com.qualityControl.Response;

import lombok.Data;

import java.util.Date;

@Data
public class QcResponse {
    private Long codeMapperId;
    private Integer crrBarcodeNumber;
    private Date crrCreateDate;
    private String assignStatus;
    private String qcStatus;
}
