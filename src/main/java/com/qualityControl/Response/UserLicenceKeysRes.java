package com.qualityControl.Response;

import com.qualityControl.Model.LicenseLine;
import com.qualityControl.Model.ModuleUserLicenceKey;
import lombok.Data;

import java.util.List;
@Data
public class UserLicenceKeysRes {
    private LicenseLine licenseLine;
    private List<ModuleUserLicenceKey> userLicenceKeys;
}
