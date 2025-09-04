package com.qualityControl.Request;

import lombok.Data;

import java.sql.Time;
import java.util.Date;

@Data
public class QcRequest {

    private Integer acceptedRejectedContainerId;

    private Integer qcEngineer;

    private Date qcAssignDate;

    private Time startTime;

    private Time endTime;
}
