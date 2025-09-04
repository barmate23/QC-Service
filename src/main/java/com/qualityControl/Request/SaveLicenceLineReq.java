package com.qualityControl.Request;

import lombok.Data;

@Data
public class SaveLicenceLineReq {
    Integer licenseLineId;
    Integer status;
    Integer reason;
    String otherReason;
    Boolean isReason;
}
