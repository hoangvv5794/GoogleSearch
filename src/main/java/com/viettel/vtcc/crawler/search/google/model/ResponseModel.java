package com.viettel.vtcc.crawler.search.google.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseModel {
    private int status_code;
    private String status_message;
    private String data;
    // weather fields
    private String weather_temp;
    private String weather_rain;
    private String weather_humid;
    private String weather_wind;
    private String weather_location;
    private String weather_date;
    private String weather_current;
    // math fields
    private String math_expression;
    private String math_result;

    public static ResponseModel getFailedMessage() {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setStatus_message("message error");
        responseModel.setStatus_code(1);
        return responseModel;
    }

    public static ResponseModel getSuccessMessage(String data) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setStatus_message("message success");
        responseModel.setStatus_code(0);
        responseModel.setData(data);
        return responseModel;
    }
}
