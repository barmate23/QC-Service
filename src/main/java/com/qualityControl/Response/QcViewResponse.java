package com.qualityControl.Response;

import com.qualityControl.Model.AcceptedRejectedContainer;
import com.qualityControl.Model.QcTimeActivity;
import com.qualityControl.Model.Users;
import lombok.Data;

import java.util.List;

@Data
public class QcViewResponse {

    private Users qcEngineer;
    private List<AcceptedRejectedContainer> schedulestatus;
    private List<QcTimeActivity> actualstatus;
}
