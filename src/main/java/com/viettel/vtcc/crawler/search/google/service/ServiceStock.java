package com.viettel.vtcc.crawler.search.google.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
public class ServiceStock {

    private final OkHttpClient client = new OkHttpClient();
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private final String URL_REQUEST = "https://finfo-api.vndirect.com.vn/v4/stock_prices/?sort=date&size=1&q=code:__STOCK_CODE__~date:gte:__TIME__~date:lte:__TIME__&page=1";

    public String requestSearch(String stock_code) {
        String date_request = formatter.format(new Date());
        String url = URL_REQUEST
                .replace("__STOCK_CODE__", stock_code)
                .replace("__TIME__", date_request);
        Request getRequest = new Request.Builder()
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .url(url)
                .build();
        try {
            Response response = client.newCall(getRequest).execute();
            String body = response.body().string();
            if (!body.isEmpty()) {
                JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
                JsonArray itemListElement = jsonObject.getAsJsonArray("data");
                if (itemListElement.size() > 0) {
                    JsonObject itemElement = itemListElement.get(0).getAsJsonObject();
                    itemElement.addProperty("status_code", 0);
                    itemElement.addProperty("status_message", "have answer");
                    return itemElement.toString();
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
