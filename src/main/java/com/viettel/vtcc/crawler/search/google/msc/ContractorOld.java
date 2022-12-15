package com.viettel.vtcc.crawler.search.google.msc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.vtcc.crawler.search.google.model.ContractorEntity;
import com.viettel.vtcc.crawler.search.google.utils.HttpAction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.*;

@Slf4j
public class ContractorOld {
    private static final String FILE_NAME_INPUT = "";
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        try {
//            getDetailContractor(Collections.singletonList("20220938463")).forEach(element -> {
//                try {
//                    System.out.println(mapper.writeValueAsString(element));
//                } catch (JsonProcessingException e) {
//                    e.printStackTrace();
//                }
//            });
            processDomain("1"); // 1: Hang hoa
//            List<ContractorEntity> listContractor = new LinkedList<>();
//            List<String> listKeyword = FileUtils.readLines(new File(FILE_NAME_INPUT), "UTF-8");
//            listKeyword.forEach(keyword -> listContractor.addAll(processDomain(keyword)));
//            ExcelUtils.writeExcelContractorInfo("contractor_collection.xlsx", listContractor);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private static List<ContractorEntity> processDomain(String keyword) {
        Map<String, Pair<String, String>> mapTimes = new HashMap<>();
        List<ContractorEntity> listContractor = new LinkedList<>();
        try {
            List<String> listIdContractors = new LinkedList<>();
            String url_search_contractor = "http://muasamcong.mpi.gov.vn:8081/GG/EP_SSJ_GGQ701.jsp?toOpenDate=31/12/2050&pqCls=Y&lang=&refNumber=&toDate=10/11/2022&fromDate=01/01/2022&bidMethod=&typeFind=1&isInstitu=0&viewType=0&fromOpenDate=01/01/2010&instituCode=&instituName_cln=&firstCall=N&pageSize=20&bidNM=&pageNo=&gubun=__DOMAIN__&page_no=";
            String response = HttpAction.sendRequestOldWebsite(url_search_contractor.replace("__DOMAIN__", keyword) + "1", null, null);
            if (response != null) {
                Document content = Jsoup.parse(response);
                // get all number contractor
                Elements elements_number = content.select("td.page");
                String number_text = elements_number.text();
                String total_contractor = number_text.replaceAll("\\D+", "");
                int totalByNumber = Integer.parseInt(total_contractor);
                // parse current page
                int totalPages = (totalByNumber / 200) + 1;
                int currentPages = 1;
                Elements elements = content.select("td");
                elements.forEach(element -> {
                    try {
                        if (element.select("a") != null) {
                            Elements contractor_element = element.select("a[href]");
                            if (contractor_element.hasAttr("onclick") && contractor_element.attr("onclick").contains("goAspTuch")) {
                                String contractor_id = contractor_element.text();
                                contractor_id = StringUtils.substringBefore(contractor_id, "-");
                                listIdContractors.add(contractor_id);
                                String time_post = contractor_element.next().next().text();
                                String close_bid = contractor_element.next().next().next().text();
                                ImmutablePair<String, String> pairTime = new ImmutablePair<>(time_post, close_bid);
                                mapTimes.put(contractor_id, pairTime);
                            }
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                });
                // get all pages
//                for (int page_process = currentPages + 1; page_process < totalPages; page_process++) {
//                    try {
//                        String responseNextPages = HttpAction.sendRequestOldWebsite(url_search_contractor.replace("__DOMAIN__", keyword) + page_process, null, null);
//                        Document contentNextPage = Jsoup.parse(responseNextPages);
//                        Elements elementsNextPage = contentNextPage.select("td");
//                        elementsNextPage.forEach(element -> {
//                            try {
//                                if (element.select("a") != null) {
//                                    Elements contractor_element = element.select("a[href]");
//                                    if (contractor_element.hasAttr("onclick") && contractor_element.attr("onclick").contains("goAspTuch")) {
//                                        String contractor_id = contractor_element.text();
//                                        contractor_id = StringUtils.substringBefore(contractor_id, "-");
//                                        listIdContractors.add(contractor_id);
//                                        String time_post = contractor_element.next().next().text();
//                                        String time_close_bid = contractor_element.next().next().next().text();
//                                        ImmutablePair<String, String> pairTime = new ImmutablePair<>(time_post, time_close_bid);
//                                        mapTimes.put(contractor_id, pairTime);
//                                    }
//                                }
//                            } catch (Exception e) {
//                                log.error(e.getMessage(), e);
//                            }
//                        });
//                    } catch (Exception e) {
//                        log.error(e.getMessage(), e);
//                    }
//                }
            }
            listContractor.addAll(getDetailContractor(listIdContractors));
            listContractor.forEach(contractorEntity -> {
                contractorEntity.setKeyword(keyword);
                Pair<String, String> pairTime = mapTimes.get(StringUtils.substringBefore(contractorEntity.getCode_TBMT(), "-"));
                contractorEntity.setNgay_dang_tai(pairTime.getLeft());
                contractorEntity.setThoi_gian_dong_thau(pairTime.getRight());
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return listContractor;
    }

    private static List<ContractorEntity> getDetailContractor(List<String> listIds) {
        String path_url_get_detail = "http://muasamcong.mpi.gov.vn:8081/biddauthau/trangchu/tbmt/viewChiTiet?bidNo=__CODE__&bidTurnNo=02&lang=";
        List<ContractorEntity> listContractors = new LinkedList<>();
        try {
            listIds.forEach(id -> {
                try {
                    String response = HttpAction.sendPostOldWebsite(path_url_get_detail.replace("__CODE__", id), null, null);
                    if (response != null) {
                        ContractorEntity contractorEntity = new ContractorEntity();
                        Document document = Jsoup.parse(response);
                        Elements elements = document.select("td.tdlabel");
                        elements.forEach(element -> {
                            try {
                                String label = element.text().trim();
                                String value = element.nextElementSibling().text().trim();
                                switch (label) {
                                    case "Số TBMT":
                                        contractorEntity.setCode_TBMT(value);
                                        break;
                                    case "Chủ đầu tư":
                                        contractorEntity.setName_CDT(value);
                                        break;
                                    case "Tên gói thầu":
                                        contractorEntity.setName_bid(value);
                                        break;
                                    case "Tên dự án":
                                        contractorEntity.setName_project(value);
                                        break;
                                    case "Chi tiết nguồn vốn":
                                        contractorEntity.setNguon_von(value);
                                        break;
                                    case "Lĩnh vực":
                                        contractorEntity.setLinh_vuc(value);
                                        break;
                                    case "Dự toán gói thầu":
                                        contractorEntity.setDu_toan(value.replaceAll("\\D+", ""));
                                        contractorEntity.setDon_vi("VND");
                                        break;
                                    case "Hình thức dự thầu":
                                        contractorEntity.setHinh_thuc_du_thau(value);
                                    case "":

                                    default:
                                        break;
                                }
                            } catch (Exception e) {
                                log.error(e.getMessage(), e);
                            }
                        });
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
