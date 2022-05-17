package com.viettel.vtcc.crawler.search.google.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.viettel.vtcc.crawler.search.google.model.ResponseModel;
import com.viettel.vtcc.crawler.search.google.service.ServiceKnowledge;
import com.viettel.vtcc.crawler.search.google.service.ServicePlaywright;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class GoogleHandler {
    @Autowired
    ServicePlaywright servicePlaywright;

    @Autowired
    ServiceKnowledge serviceKnowledge;

    @PostMapping(path = "search/google", consumes = "application/json")
    private ResponseModel executeSearchGoogle(@RequestBody String json_payload) {
        try {
            JsonObject jsonObject = JsonParser.parseString(json_payload).getAsJsonObject();
            String query = jsonObject.get("system_message").getAsString();
            log.info("process query with playwright {}", query);
            String url = "https://www.google.com/search?q=" + query;
            String data = servicePlaywright.executeRequest(url);
            return ResponseModel.builder().status_code(0).status_message("message success").data(data).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ResponseModel.builder().status_code(1).status_message("message error").build();
    }

    @PostMapping(path = "search/knowledge", consumes = "application/json")
    private ResponseModel executeSearchAPI(@RequestBody String json_payload) {
        try {
            JsonObject jsonObject = JsonParser.parseString(json_payload).getAsJsonObject();
            String query = jsonObject.get("system_message").getAsString();
            log.info("process query with google knowledge {}", query);
            String data = serviceKnowledge.requestSearch(query);
            if (data != null) {
                return ResponseModel.builder().status_code(0).status_message("message success").data(data).build();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ResponseModel.builder().status_code(1).status_message("message error").build();
    }
}
