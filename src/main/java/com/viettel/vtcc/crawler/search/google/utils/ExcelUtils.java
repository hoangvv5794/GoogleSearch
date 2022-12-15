package com.viettel.vtcc.crawler.search.google.utils;

import com.viettel.vtcc.crawler.search.google.model.ContractorEntity;
import com.viettel.vtcc.crawler.search.google.model.MuasamcongEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.List;

@Slf4j
public class ExcelUtils {

    public static void writeExcelBidInfo(String FILE_NAME, List<MuasamcongEntity> listModel) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Mua sam cong");
        Object[][] datatypes = {
                {"Mã định danh", "Tên đơn vị", "Tên đơn vị (Tiếng Anh)"
                        , "Mã số thuế", "Tên cơ quan chủ quản", "Tỉnh/Thành phố", "Địa chỉ", "Số điện thoại"
                        , "Người đại diện pháp luật", "Chức vụ", "Ngày phê duyệt đăng ký", "Trạng thái hoạt động"}
        };

        int rowNum = 0;
        log.info("Creating excel with {}", listModel.size());

        for (Object[] datatype : datatypes) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            for (Object field : datatype) {
                Cell cell = row.createCell(colNum++);
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                }
            }
        }
        for (MuasamcongEntity datatype : listModel) {
            try {
                if (datatype != null) {
                    Row row = sheet.createRow(rowNum++);
                    insert_model_bid_info(datatype, row);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(FILE_NAME);
            workbook.write(outputStream);
            workbook.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private static void insert_model_bid_info(MuasamcongEntity model, Row row) {
        try {
            Cell cell_id = row.createCell(0);
            cell_id.setCellValue(model.getCodeEntity());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            row.getCell(0).setCellValue("Error in write, need to write manual");
        }
        try {
            Cell cell_procedure_code = row.createCell(1);
            cell_procedure_code.setCellValue(model.getFullNameEntity());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            row.getCell(1).setCellValue("Error in write, need to write manual");
        }
        try {
            Cell cell_procedure_method = row.createCell(2);
            cell_procedure_method.setCellValue(model.getNameEntityEnglish());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            row.getCell(2).setCellValue("Error in write, need to write manual");
        }
        try {
            Cell cell_procedure_way = row.createCell(3);
            cell_procedure_way.setCellValue(model.getTaxCode());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            row.getCell(3).setCellValue("Error in write, need to write manual");
        }
        try {
            Cell cell_procedure_file = row.createCell(4);
            cell_procedure_file.setCellValue(model.getOwnerEntity());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            row.getCell(4).setCellValue("Error in write, need to write manual");
        }
        try {
            Cell cell_procedure_condition = row.createCell(5);
            cell_procedure_condition.setCellValue(model.getCity());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            row.getCell(5).setCellValue("Error in write, need to write manual");
        }
        try {
            Cell cell_procedure_condition = row.createCell(6);
            cell_procedure_condition.setCellValue(model.getAddress());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            row.getCell(6).setCellValue("Error in write, need to write manual");
        }
        try {
            Cell cell_procedure_condition = row.createCell(7);
            cell_procedure_condition.setCellValue(model.getPhone());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            row.getCell(7).setCellValue("Error in write, need to write manual");
        }
        try {
            Cell cell_procedure_condition = row.createCell(8);
            cell_procedure_condition.setCellValue(model.getPeopleOwnEntity());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            row.getCell(8).setCellValue("Error in write, need to write manual");
        }
        try {
            Cell cell_procedure_condition = row.createCell(9);
            cell_procedure_condition.setCellValue(model.getTypePeople());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            row.getCell(9).setCellValue("Error in write, need to write manual");
        }
        try {
            Cell cell_procedure_condition = row.createCell(10);
            cell_procedure_condition.setCellValue(model.getDateAcceptSub());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            row.getCell(10).setCellValue("Error in write, need to write manual");
        }
        try {
            Cell cell_procedure_condition = row.createCell(11);
            cell_procedure_condition.setCellValue(model.getStatus());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            row.getCell(11).setCellValue("Error in write, need to write manual");
        }
    }

    public static void writeExcelContractorInfo(String FILE_NAME, List<ContractorEntity> listModel) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Mua sam cong");
        Object[][] datatypes = {
                {"Mã TBMT", "Tên chủ đầu tư", "Tên gói thầu"
                        , "Tên dự án", "Nguồn vốn", "Lĩnh vực", "Dự toán", "Đơn vị"
                }
        };

        int rowNum = 0;
        log.info("Creating excel with {}", listModel.size());

        for (Object[] datatype : datatypes) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            for (Object field : datatype) {
                Cell cell = row.createCell(colNum++);
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                }
            }
        }
        for (ContractorEntity datatype : listModel) {
            try {
                if (datatype != null) {
                    Row row = sheet.createRow(rowNum++);
                    insert_model_contractor_info(datatype, row);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(FILE_NAME);
            workbook.write(outputStream);
            workbook.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private static void insert_model_contractor_info(ContractorEntity model, Row row) {
        try {
            Cell cell_id = row.createCell(0);
            cell_id.setCellValue(model.getCode_TBMT());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            row.getCell(0).setCellValue("Error in write, need to write manual");
        }
        try {
            Cell cell_procedure_code = row.createCell(1);
            cell_procedure_code.setCellValue(model.getName_CDT());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            row.getCell(1).setCellValue("Error in write, need to write manual");
        }
        try {
            Cell cell_procedure_method = row.createCell(2);
            cell_procedure_method.setCellValue(model.getName_bid());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            row.getCell(2).setCellValue("Error in write, need to write manual");
        }
        try {
            Cell cell_procedure_way = row.createCell(3);
            cell_procedure_way.setCellValue(model.getName_project());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            row.getCell(3).setCellValue("Error in write, need to write manual");
        }
        try {
            Cell cell_procedure_file = row.createCell(4);
            cell_procedure_file.setCellValue(model.getNguon_von());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            row.getCell(4).setCellValue("Error in write, need to write manual");
        }
        try {
            Cell cell_procedure_condition = row.createCell(5);
            cell_procedure_condition.setCellValue(model.getLinh_vuc());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            row.getCell(5).setCellValue("Error in write, need to write manual");
        }
        try {
            Cell cell_procedure_condition = row.createCell(6);
            cell_procedure_condition.setCellValue(model.getDu_toan());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            row.getCell(6).setCellValue("Error in write, need to write manual");
        }
        try {
            Cell cell_procedure_condition = row.createCell(7);
            cell_procedure_condition.setCellValue(model.getDon_vi());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            row.getCell(7).setCellValue("Error in write, need to write manual");
        }
    }
}

