package com.qualityControl.Response;

import com.qualityControl.Model.SerialBatchNumbers;
import lombok.Data;
import java.util.List;

@Data
public class PpeQcItemResponse {

    private String itemCode;
    private String itemName;
    private Integer itemQuantity;
    private String qcStatus;
    private String qcEngineer;
    private List<SerialBatchNumbers> serialBatchNumber;

}

