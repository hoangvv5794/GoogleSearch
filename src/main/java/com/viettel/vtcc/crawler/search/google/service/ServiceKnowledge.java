package com.viettel.vtcc.crawler.search.google.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ServiceKnowledge {

    @Autowired
    private Environment env;

    @Value("${api.key.google}")
    private String API_KEY;
    private String FORMAT_URL = "https://kgsearch.googleapis.com/v1/entities:search?query=_KEYWORD_&key=API_KEY&languages=vi&limit=1&indent=True";
    private final OkHttpClient client = new OkHttpClient();


    public String requestSearch(String query) {
        String url = FORMAT_URL.replace("API_KEY", API_KEY).replace("_KEYWORD_", query);
        Request getRequest = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(getRequest).execute();
            String body = response.body().string();
            if (!body.isEmpty()) {
                JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
                JsonArray itemListElement = jsonObject.getAsJsonArray("itemListElement");
                if (itemListElement.size() > 0) {
                    JsonObject itemElement = itemListElement.get(0).getAsJsonObject();
                    JsonObject result = itemElement.getAsJsonObject("result");
                    String name = result.get("name").getAsString();
                    String description = result.get("description").getAsString();
                    return name + "\t" + description;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
