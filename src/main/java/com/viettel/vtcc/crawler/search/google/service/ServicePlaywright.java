package com.viettel.vtcc.crawler.search.google.service;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.viettel.vtcc.crawler.search.google.model.ResponseModel;
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
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
        );
    }


    public ResponseModel executeRequest(String url) {
        log.info("process request {}", url);
        Page page;
        try {
            page = browser.newPage();
            page.navigate(url);
            String raw_html = page.content();
            ResponseModel responseModel = parseRequest(raw_html);
            String data = responseModel.getData();
            if (data.trim().isEmpty()) {
                return ResponseModel.getNotAnswerMessage();
            } else {
                responseModel.setData(responseModel.getData().replace(BLOCK_TEXT, ""));
            }
            log.info("process done request {}", url);
            return responseModel;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ResponseModel.getNotAnswerMessage();
    }

    private ResponseModel parseRequest(String html) {
        ResponseModel responseModel = new ResponseModel();
        StringBuilder builder = new StringBuilder();
        Document document = Jsoup.parse(html);
        document.select("h3.Uo8X3b.OhScic.zsYMMe").remove();
        document.select("a.w13wLe").remove();
        // get price stock
//        Elements elements_stock = document.select("div.Crs1tb");
//        if (!elements_stock.isEmpty()) {
//            Elements elements_table = elements_stock.select("div.webanswers-webanswers_table__webanswers-table").select("tr");
//            Element first_line = elements_table.get(1);
//            Elements attributes = first_line.select("td");
//            String stock_id = attributes.get(0).text();
//            String close_price = attributes.get(1).text();
//            String high_price = attributes.get(2).text();
//            builder.append(stock_id).append("\n").append(close_price).append("\n").append(high_price);
//            responseModel.setStock_id(stock_id);
//            responseModel.setClose_price(close_price);
//            responseModel.setHigh_price(high_price);
//            responseModel.setData(builder.toString());
//            responseModel.setStatus_code(0);
//            responseModel.setStatus_message("have answer");
//            return responseModel;
//        }
        // get population
        Elements elements_population = document.select("div.ayqGOc.kno-fb-ctx.KBXm4e");
        if (!elements_population.isEmpty()) {
            String population = elements_population.text();
            builder.append(population);
            responseModel.setData(builder.toString());
            responseModel.setPopulation(population);
            responseModel.setStatus_code(0);
            responseModel.setStatus_message("have answer");
            return responseModel;
        }
        // get calculator
        Elements elements_calculator = document.select("div.TIGsTb");
        if (!elements_calculator.isEmpty() && elements_calculator.hasAttr("jsname")) {
            String expression = elements_calculator.select("span.vUGUtc").text();
            String result = elements_calculator.select("span#cwos.qv3Wpe").text();
            builder.append(expression).append("\n").append(result);
            responseModel.setMath_expression(expression);
            responseModel.setMath_result(result);
            responseModel.setData(builder.toString());
            responseModel.setStatus_code(0);
            responseModel.setStatus_message("have answer");
            return responseModel;
        }
        // get test weather
        Elements elements_weather = document.select("div#wob_wc.nawv0d");
        if (!elements_weather.isEmpty()) {
            // question about the weather
            String weather = elements_weather.select("span#wob_tm.wob_t.q8U8x").text();
            String celsius = elements_weather.select("div.vk_bk.wob-unit").select("span.wob_t").first().text();
            builder.append(weather).append(" ").append(celsius).append("\n");
            responseModel.setWeather_temp(weather + " " + celsius);
            Elements addition_info_elements = elements_weather.select("div.wtsRwe");
            Elements addition_info = Jsoup.parse(addition_info_elements.html()).select("div");
            if (addition_info.size() == 3) {
                String rain = addition_info.get(0).text();
                responseModel.setWeather_rain(rain);
                String humid = addition_info.get(1).text();
                responseModel.setWeather_humid(humid);
                String wind = addition_info.get(2).text();
                responseModel.setWeather_wind(wind);
                builder.append(rain).append("\n").append(humid).append("\n").append(wind).append("\n");
            }
            // parse location of answer
            String located = document.select("div#wob_loc.wob_loc.q8U8x").text();
            String time = document.select("div#wob_dts.wob_dts").text();
            String current_weather = document.select("div#wob_dcp.wob_dcp").text();
            builder.append(located).append("\n").append(time).append("\n").append(current_weather);
            responseModel.setWeather_location(located);
            responseModel.setWeather_date(time);
            responseModel.setWeather_current(current_weather);
            responseModel.setData(builder.toString());
            responseModel.setStatus_code(0);
            responseModel.setStatus_message("have answer");
            return responseModel;
        }
        // get test check current money price
        Elements elements_currency = document.select("div#knowledge-currency__updatable-data-column.nRbRnb");
        if (!elements_currency.isEmpty()) {
            // question about the weather
            String condition = elements_currency.select("div.vk_sh.c8Zgcf").text();
            String price = elements_currency.select("div.dDoNo.ikb4Bb.gsrt").text();
            builder.append(condition).append(" ").append(price);
            responseModel.setData(builder.toString());
            responseModel.setStatus_code(0);
            responseModel.setStatus_message("have answer");
            return responseModel;
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
            // return
            responseModel.setData(builder.toString());
            responseModel.setStatus_code(0);
            responseModel.setStatus_message("have answer");
            return responseModel;
        }
        // event
        Elements elements_event = document.select("div.ZxoDOe.yGdMVd");
        if (!elements_event.isEmpty()) {
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
            responseModel.setData(builder.toString());
            responseModel.setStatus_code(0);
            responseModel.setStatus_message("have answer");
            return responseModel;
        }
        //first default answer
        Elements elements_first_answer = document.select("div.wDYxhc");
        if (!elements_first_answer.isEmpty()) {
            // parse first result
            Elements first_result = elements_first_answer.select("span.hgKElc");
            if (first_result != null && !first_result.isEmpty()) {
                String answer = elements_first_answer.select("span.hgKElc").text();
                responseModel.setData(answer);
                responseModel.setStatus_code(0);
                responseModel.setStatus_message("have answer");
                return responseModel;
            }
            // parse today
            Elements today_elements = elements_first_answer.select("div.vk_gy.vk_sh.card-section.sL6Rbf");
            if (today_elements != null && !today_elements.isEmpty()) {
                String answer = today_elements.text();
                responseModel.setData(answer);
                responseModel.setStatus_code(0);
                responseModel.setStatus_message("have answer");
                return responseModel;
            }
            // parse case relate first
            Elements related_first = elements_first_answer.select("div.Crs1tb");
            if (related_first != null && !related_first.isEmpty()) {
                String answer = related_first.text();
                responseModel.setData(answer);
                responseModel.setStatus_code(0);
                responseModel.setStatus_message("have answer");
                return responseModel;
            }
        }
        return ResponseModel.getNotAnswerMessage();
    }
}
