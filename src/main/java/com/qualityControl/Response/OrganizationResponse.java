package com.qualityControl.Response;

import com.qualityControl.Model.Organization;
import lombok.Data;

import java.util.List;
@Data
public class OrganizationResponse {
    List<Organization> organizations;
    private Integer pageCount;
    private Long recordCount;
}
