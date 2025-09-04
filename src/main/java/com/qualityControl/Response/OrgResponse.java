package com.qualityControl.Response;

import com.qualityControl.Model.Organization;
import com.qualityControl.Model.Users;
import lombok.Data;

@Data
public class OrgResponse {
    private Organization organization;
    private Users orgUser;
}
