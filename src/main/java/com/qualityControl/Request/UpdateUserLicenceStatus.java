package com.qualityControl.Request;

import lombok.Data;

@Data
public class UpdateUserLicenceStatus {
    Integer userLicId;
    Boolean status;
}
