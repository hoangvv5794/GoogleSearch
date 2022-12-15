package com.viettel.vtcc.crawler.search.google.msc;

import com.viettel.vtcc.crawler.search.google.model.MuasamcongEntity;
import com.viettel.vtcc.crawler.search.google.utils.ExcelUtils;
import com.viettel.vtcc.crawler.search.google.utils.HttpAction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class EntityDetails {
    public static void main(String[] args) {
        try {
            List<MuasamcongEntity> list_entity = new LinkedList<>();
            List<String> listUrls = FileUtils.readLines(new File("C:\\Users\\KGM\\Desktop\\list_bid_part1.txt"), "UTF-8");
            listUrls.forEach(url -> {
                log.info(url);
                MuasamcongEntity entity = getDetailInfoEntity(url);
                list_entity.add(entity);
            });
            ExcelUtils.writeExcelBidInfo("bid_info.xlsx", list_entity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    private static MuasamcongEntity getDetailInfoEntity(String url) {
        MuasamcongEntity muasamcongEntity = new MuasamcongEntity();
        try {
            String html_page = HttpAction.sendRequest(url, null, null);
            if (html_page!=null){
                Document document_page = Jsoup.parse(html_page);
                Elements elements = document_page.select("table.table").select("div.row.mr-0.ml-0");
                elements.forEach(element -> {
                    Elements firstElement = element.select("div.col-md-3");
                    String key_string = firstElement.text().trim();
                    Elements secondElement = element.select("div.col-md-9");
                    String value_string = secondElement.text().trim();
                    switch (key_string) {
                        case "Ngày phê duyệt yêu cầu đăng ký":
                            muasamcongEntity.setDateAcceptSub(value_string);
                        case "Trạng thái vai trò":
                            muasamcongEntity.setStatus(value_string);
                        case "Mã định danh":
                            muasamcongEntity.setCodeEntity(value_string);
                        case "Tên đơn vị (đầy đủ)":
                            muasamcongEntity.setFullNameEntity(value_string);
                        case "Tên đơn vị (tiếng Anh)":
                            muasamcongEntity.setNameEntityEnglish(value_string);
                        case "Mã số thuế":
                            muasamcongEntity.setTaxCode(value_string);
                        case "Loại hình pháp lý":
                            muasamcongEntity.setTypeEntity(value_string);
                        case "Tên cơ quan chủ quản":
                            muasamcongEntity.setOwnerEntity(value_string);
                        case "Mã quan hệ ngân sách":
                            muasamcongEntity.setCodeRelationship(value_string);
                        case "Tỉnh/Thành phố":
                            Elements cityElements = firstElement.next();
                            muasamcongEntity.setCity(cityElements.text());
                        case "Địa chỉ":
                            muasamcongEntity.setAddress(value_string);
                        case "Số điện thoại":
                            muasamcongEntity.setPhone(value_string);
                        case "Web":
                            muasamcongEntity.setWebsite(value_string);
                        case "Họ và tên":
                            muasamcongEntity.setPeopleOwnEntity(value_string);
                        case "Chức vụ":
                            muasamcongEntity.setTypePeople(value_string);

                    }
                });
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return muasamcongEntity;
    }
}
