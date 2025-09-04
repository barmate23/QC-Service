package com.qualityControl.Request;

import lombok.Data;

import java.sql.Time;

@Data
public class QcEngineerStartPauseEndRequest {

    Integer officerId;
    String itemCode;
    String activityType;
    Time activityTime;

}
