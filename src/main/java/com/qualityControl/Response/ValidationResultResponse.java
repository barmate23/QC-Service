package com.qualityControl.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ValidationResultResponse {

    private String type;
    private Integer rowIndex;
    private String columnName;
    private String errorMessage;


}