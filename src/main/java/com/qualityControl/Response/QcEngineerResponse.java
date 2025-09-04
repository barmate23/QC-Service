package com.qualityControl.Response;

import com.qualityControl.Model.Item;
import com.qualityControl.Model.Reason;
import lombok.Data;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@Data
public class QcEngineerResponse {

    private Integer crrNumber;
    private Date crrCreateDate;
    private Date currentDate;
    private String itemName;
    private String itemCode;
    private Date qcAssignDate;
    private Time startTime;
    private Time endTime;
    private Integer supplierQcNumber;
    private Date supplierQcDate;
    private Integer acceptedRejectedContainerId;

    private Item items;
    private List<String> serialBatchNumbers;
    private List<Reason> reasons;

}
