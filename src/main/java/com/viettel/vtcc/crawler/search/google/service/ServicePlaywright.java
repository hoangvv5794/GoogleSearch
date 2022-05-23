package com.viettel.vtcc.crawler.search.google.service;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ServicePlaywright {
    private String BLOCK_TEXT = "Chia sẻ Chia sẻ Facebook Twitter Email Nhấp để sao chép đường liên kết Chia sẻ liên kết Đã sao chép đường liên kết Gửi phản hồi";

    private static Playwright playwright;
    private static Browser browser;

    public ServicePlaywright() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
    }

    public static void main(String[] args) {
        ServicePlaywright servicePlaywright = new ServicePlaywright();
        String url = "https://www.google.com/search?q=seagames 31";
        String text = servicePlaywright.executeRequest(url);
        log.info(text);
    }

    public String executeRequest(String url) {
        log.info("process request {}", url);
        Page page = browser.newPage();
        page.navigate(url);
        String raw_html = page.content();
        String text = parseRequest(raw_html);
        page.close();
        log.info("process done request {}", url);
        return text;
    }

    private String parseRequest(String html) {
        StringBuilder builder = new StringBuilder();
        Document document = Jsoup.parse(html);
        document.select("h3.Uo8X3b.OhScic.zsYMMe").remove();
        // get test weather
        Elements elements_weather = document.select("div#wob_wc.nawv0d");
        if (!elements_weather.isEmpty()) {
            // question about the weather
            String weather = elements_weather.select("span#wob_tm.wob_t.q8U8x").text();
            String celsius = elements_weather.select("div.vk_bk.wob-unit").select("span.wob_t").first().text();
            builder.append(weather).append(" ").append(celsius).append("\n");
            Elements addition_info_elements = elements_weather.select("div.wtsRwe");
            Jsoup.parse(addition_info_elements.html()).select("div").forEach(addition_info_element -> {
                String info = addition_info_element.text();
                builder.append(info).append("\n");
            });
            return builder.toString();
        }
        // get test check current money price
        Elements elements_currency = document.select("div#knowledge-currency__updatable-data-column.nRbRnb");
        if (!elements_currency.isEmpty()) {
            // question about the weather
            String condition = elements_currency.select("div.vk_sh.c8Zgcf").text();
            String price = elements_currency.select("div.dDoNo.ikb4Bb.gsrt").text();
            builder.append(condition).append(" ").append(price);
            return builder.toString();
        }

        Elements elements = document.select("div.osrp-blk").select("div.SPZz6b");

        if (!elements.isEmpty()) {
            // person
            // get title + subtitle
            elements = document.select("div.osrp-blk");
            String title = elements.select("div.SPZz6b").attr("data-attrid", "title").text();
            builder.append(title).append("\n");
            // get description
            String description = elements.select("div.PZPZlf.hb8SAc").attr("data-attrid", "description").text();
            builder.append(description).append("\n");
            Elements fields = elements.select("div.wDYxhc");
            fields.forEach(field -> {
                String attribute = field.select("span.w8qArf").text();
                String value = field.select("span.LrzXr.kno-fv.wHYlTd.z8gr9e").text();
                if (!attribute.isEmpty() && !value.isEmpty()) {
                    builder.append(attribute).append(value).append("\n");
                }
            });
        } else {
            // event
            String title = document.select("div.ZxoDOe.yGdMVd").attr("data-attrid", "title").text();
            builder.append(title).append("\n");
            // get description
            String description = document.select("div.PZPZlf.hb8SAc").attr("data-attrid", "description").text();
            builder.append(description).append("\n");
            Elements fields = document.select("div.wDYxhc");
            fields.forEach(field -> {
                String attribute = field.select("span.w8qArf").text();
                String value = field.select("span.LrzXr.kno-fv.wHYlTd.z8gr9e").text();
                if (!attribute.isEmpty() && !value.isEmpty()) {
                    builder.append(attribute).append(value).append("\n");
                }
            });
        }
        String result = builder.toString().replace(BLOCK_TEXT, "");
        return result;
    }
}
