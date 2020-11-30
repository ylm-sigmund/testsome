package com.diy.sigmund.designpatterns.exceimport;

/**
 * @author ylm-sigmund
 * @since 2020/11/30 17:44
 */
public class TwoExcelImport implements ExcelImport {

    @Override
    public void execute() {
        System.out.println("TwoExcelImport::execute");
    }
}
