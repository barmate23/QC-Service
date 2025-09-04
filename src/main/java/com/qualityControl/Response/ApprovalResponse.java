package com.qualityControl.Response;

import com.qualityControl.Model.LicenseHead;
import lombok.Data;

import java.util.List;

@Data
public class ApprovalResponse {
    List<LicenseHead> licenseHeadList;
    private Integer pageCount;
    private Long recordCount;
}
