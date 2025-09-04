package com.qualityControl.Request;

import lombok.Data;

import java.sql.Time;
import java.util.Date;

@Data
public class PpeQcRequest {

    private Integer ppeLineId;

    private Integer qcEngineer;

    private Date qcAssignDate;

    private Time startTime;

    private Time endTime;
}
