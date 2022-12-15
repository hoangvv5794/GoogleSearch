package com.viettel.vtcc.crawler.search.google.msc;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.viettel.vtcc.crawler.search.google.model.ContractorEntity;
import com.viettel.vtcc.crawler.search.google.utils.ExcelUtils;
import com.viettel.vtcc.crawler.search.google.utils.HttpAction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class ContractorCollector {
    private static final String FILE_NAME_INPUT = "";

    public static void main(String[] args) {
        try {
            List<ContractorEntity> listContractor = new LinkedList<>();
            List<String> listKeyword = FileUtils.readLines(new File(FILE_NAME_INPUT), "UTF-8");
            listKeyword.forEach(keyword -> listContractor.addAll(processKeyword(keyword)));
            ExcelUtils.writeExcelContractorInfo("contractor_collection.xlsx", listContractor);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private static List<ContractorEntity> processKeyword(String keyword) {
        List<ContractorEntity> listContractor = new LinkedList<>();
        try {
            List<String> listIdContractors = new LinkedList<>();
            String path_url_search = "https://muasamcong.mpi.gov.vn/o/egp-portal-contractor-selection/services/smart/search";
            String body_post_search_keyword = "{\"pageSize\":10,\"pageNumber\":__PAGE__,\"query\":{\"index\":\"es-notify-contractor\",\"keyWord\":\"\\\"" + keyword + "\\\"\",\"matchFields\":[\"bidName\",\"procuringEntityName\",\"notifyNo\"],\"filters\":[{\"fieldName\":\"isInternet\",\"searchType\":\"IN\",\"fieldValues\":[1,0]},{\"fieldName\":\"bidPrice\",\"searchType\":\"RANGE\",\"from\":0,\"to\":999999999999999},{\"fieldName\":\"publicDate\",\"searchType\":\"RANGE\",\"from\":\"2022-01-01T00:00:00.000\",\"to\":\"2022-11-10T23:59:59.000\"},{\"fieldName\":\"bidCloseDate\",\"searchType\":\"RANGE\",\"from\":null,\"to\":null}]}}";
            String response = HttpAction.sendPost(path_url_search, "192.168.104.52:8131", body_post_search_keyword.replace("__PAGE__", "-1"));
            if (response != null) {
                JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
                int totalPages = jsonResponse.get("page").getAsJsonObject()
                        .get("totalPages").getAsInt();
                int currentPages = 0;
                // parse current page
                JsonArray content = jsonResponse.getAsJsonObject("page")
                        .getAsJsonArray("content");
                for (JsonElement element : content) {
                    try {
                        JsonObject elementContent = element.getAsJsonObject();
                        String id_contractor = elementContent.get("id").getAsString();
                        listIdContractors.add(id_contractor);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
                // get all pages
                for (int page_process = currentPages + 1; page_process < totalPages; page_process++) {
                    try {
                        String responseNextPages = HttpAction.sendPost(path_url_search, "192.168.104.52:8131", body_post_search_keyword.replace("__PAGE__", String.valueOf(page_process)));
                        JsonObject jsonNextResponse = JsonParser.parseString(responseNextPages).getAsJsonObject();
                        JsonArray contentNextPage = jsonNextResponse.getAsJsonObject("page")
                                .getAsJsonArray("content");
                        for (JsonElement element : contentNextPage) {
                            try {
                                JsonObject elementContent = element.getAsJsonObject();
                                String id_contractor = elementContent.get("id").getAsString();
                                listIdContractors.add(id_contractor);
                            } catch (Exception e) {
                                log.error(e.getMessage(), e);
                            }
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
            listContractor.addAll(getDetailContractor(listIdContractors));
            listContractor.forEach(contractorEntity -> contractorEntity.setKeyword(keyword));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return listContractor;
    }

    private static List<ContractorEntity> getDetailContractor(List<String> listIds) {
        String path_url_get_detail = "https://muasamcong.mpi.gov.vn/o/egp-portal-contractor-selection/services/ebid/noti/detail";
        String body_get_detail_pattern = "{\"id\":\"__ID__\"}";
        List<ContractorEntity> listContractors = new LinkedList<>();
        try {
            listIds.forEach(id -> {
                try {
                    String response = HttpAction.sendPost(path_url_get_detail, "192.168.104.52:8131", body_get_detail_pattern.replace("__ID__", id));
                    if (response != null) {
                        JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
                        JsonObject bidoNotifyContractorM = jsonResponse.getAsJsonObject("bidoNotifyContractorM");
                        ContractorEntity contractorEntity = new ContractorEntity();
                        String code_tbmt = bidoNotifyContractorM.get("notifyNo").getAsString() + "-" + bidoNotifyContractorM.get("notifyVersion").getAsString();
                        contractorEntity.setCode_TBMT(code_tbmt);
                        String cdt = bidoNotifyContractorM.get("investorName").getAsString();
                        contractorEntity.setName_CDT(cdt);
                        String goi_thau = bidoNotifyContractorM.get("bidName").getAsString();
                        contractorEntity.setName_bid(goi_thau);
                        String du_an = bidoNotifyContractorM.get("planName").getAsString();
                        contractorEntity.setName_project(du_an);
                        String nguon_von = bidoNotifyContractorM.get("capitalDetail").getAsString();
                        contractorEntity.setNguon_von(nguon_von);
                        String linh_vuc = bidoNotifyContractorM.get("investField").getAsString();
                        contractorEntity.setLinh_vuc(linh_vuc);
                        String du_toan = String.valueOf(bidoNotifyContractorM.get("bidPrice").getAsLong());
                        contractorEntity.setDu_toan(du_toan);
                        String don_vi = bidoNotifyContractorM.get("bidPriceUnit").getAsString();
                        contractorEntity.setDon_vi(don_vi);
                        listContractors.add(contractorEntity);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return listContractors;
    }
}
