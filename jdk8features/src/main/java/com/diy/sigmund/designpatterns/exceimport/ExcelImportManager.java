package com.diy.sigmund.designpatterns.exceimport;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.CollectionUtils;

/**
 * @author ylm-sigmund
 * @since 2020/11/30 17:45
 */
public class ExcelImportManager {
    private static Map<String, ExcelImport> excelImportManagerMap;
    private OneExcelImport oneExcelImport;
    private TwoExcelImport twoExcelImport;

    public void initExcelImportManager() {
        oneExcelImport = new OneExcelImport();
        twoExcelImport = new TwoExcelImport();
        excelImportManagerMap = new HashMap<>();
        excelImportManagerMap.put("oneExcelImport", oneExcelImport);
        excelImportManagerMap.put("twoExcelImport", twoExcelImport);
    }

    public Map<String, ExcelImport> getManagerMap() {
        if (CollectionUtils.isEmpty(excelImportManagerMap)) {
            initExcelImportManager();
        }
        return excelImportManagerMap;
    }
}
