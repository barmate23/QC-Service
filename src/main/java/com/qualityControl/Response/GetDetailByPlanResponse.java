package com.qualityControl.Response;

import com.qualityControl.Model.Users;
import lombok.Data;

import java.util.Date;

@Data
public class GetDetailByPlanResponse {
    Integer id;
    String planId;
    String product;
    String brand;
    String model;
    String variant;
    Date startDate;
    Date endDate;
    String itemCode;
    String itemName;
    Integer itemQuantity;
    String qcStatus;
    Users qcEngineer;
}
