package com.diy.sigmund.designpatterns.exceimport;

import java.util.Map;

/**
 * @author ylm-sigmund
 * @since 2020/11/30 17:58
 */
public class ExcelImportTest {

    public static void main(String[] args) {
        ExcelImportManager excelImportManager = new ExcelImportManager();
        final Map<String, ExcelImport> managerMap = excelImportManager.getManagerMap();
        if (managerMap.containsKey("oneExcelImport")) {
            managerMap.get("oneExcelImport").execute();
        }
        if (managerMap.containsKey("twoExcelImport")) {
            managerMap.get("twoExcelImport").execute();
        }
    }
}
