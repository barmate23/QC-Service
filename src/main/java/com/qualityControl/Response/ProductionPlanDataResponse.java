package com.qualityControl.Response;

import lombok.Data;

import java.util.Date;

@Data
public class ProductionPlanDataResponse {
    String planId;
    String product;
    String brand;
    String model;
    String variant;
    String productionShop;
    String lineId;
    String planStatus;
    Date startDate;
    Date endDate;
}
