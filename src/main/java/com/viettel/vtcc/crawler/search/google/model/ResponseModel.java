package com.viettel.vtcc.crawler.search.google.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.viettel.vtcc.crawler.search.google.service.ServiceResponseAnswer;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Component
public class ResponseModel {

    private static ServiceResponseAnswer staticServiceResponseAnswer;

    @Autowired
    public ResponseModel(ServiceResponseAnswer foo) {
        ResponseModel.staticServiceResponseAnswer = foo;
    }

    public ResponseModel() {

    }

    private int status_code;
    private String status_message;
    private String data;
    private String not_answer_message;
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
    // stock price
    private String stock_id;
    private String close_price;
    private String high_price;
    // population
    private String population;

    public static ResponseModel getFailedMessage() {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setStatus_message("message error");
        responseModel.setStatus_code(1);
        return responseModel;
    }

    public static ResponseModel getNotAnswerMessage() {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setStatus_message("not answer");
        responseModel.setNot_answer_message(staticServiceResponseAnswer.getRandomAnswer());
        responseModel.setStatus_code(0);
        responseModel.setData("");
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
