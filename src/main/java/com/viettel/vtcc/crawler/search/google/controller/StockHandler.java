package com.viettel.vtcc.crawler.search.google.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.viettel.vtcc.crawler.search.google.model.ResponseModel;
import com.viettel.vtcc.crawler.search.google.service.ServiceStock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class StockHandler {
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    ServiceStock serviceStock;


    @PostMapping(path = "search/stock_price", consumes = "application/json")
    private String queryStock(@RequestBody String json_payload) throws JsonProcessingException {
        try {
            JsonObject jsonObject = JsonParser.parseString(json_payload).getAsJsonObject();
            String query = jsonObject.get("stock_code").getAsString();
            log.info("process query stock {}", query);
            String data = serviceStock.requestSearch(query);
            if (data != null) {
                return data;
            } else {
                mapper.writeValueAsString(ResponseModel.getNotAnswerMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return mapper.writeValueAsString(ResponseModel.getFailedMessage());
    }
}
