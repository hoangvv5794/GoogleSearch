package com.viettel.vtcc.crawler.search.google.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.vtcc.crawler.search.google.model.MuasamcongEntity;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ExcelConvert {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        List<String> listLines = FileUtils.readLines(new File("list_bid.txt"), "UTF-8");
        List<MuasamcongEntity> list_entity = new LinkedList<>();
        listLines.forEach(line -> {
            try {
                MuasamcongEntity entity = mapper.readValue(line, MuasamcongEntity.class);
                list_entity.add(entity);
            } catch (Exception e) {

            }
        });
        ExcelUtils.writeExcelBidInfo("bid_collector_part4.xlsx", list_entity);
    }
}
