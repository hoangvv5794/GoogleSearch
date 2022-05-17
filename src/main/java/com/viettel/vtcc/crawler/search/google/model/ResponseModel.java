package com.viettel.vtcc.crawler.search.google.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseModel {
    private int status_code;
    private String status_message;
    private String data;
}
