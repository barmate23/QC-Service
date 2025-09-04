package com.qualityControl.Response;

import com.qualityControl.Model.PPEHead;
import lombok.Data;

import java.util.List;

@Data
public class PPEHeadResponse {

    private List<PPEHead> ppeHeadData;

    private Integer totalAvailable;

    private Integer totalShortage;
}
