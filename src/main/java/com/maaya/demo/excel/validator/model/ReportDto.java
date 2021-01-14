package com.maaya.demo.excel.validator.model;

import org.springframework.stereotype.Component;

/**
 * demoでDBinsert用に
 */
public class ReportDto {
    private String 会計年度;
    private String 西暦;
    private String 企業コード;
    private String 項目コード;
    private String 金額;

    public String get会計年度() {
        return 会計年度;
    }

    public String get西暦() {
        return 西暦;
    }

    public String get企業コード() {
        return 企業コード;
    }

    public String get項目コード() {
        return 項目コード;
    }

    public String get金額() {
        return 金額;
    }

    public void set会計年度(String 会計年度) {
        this.会計年度 = 会計年度;
    }

    public void set西暦(String 西暦) {
        this.西暦 = 西暦;
    }

    public void set企業コード(String 企業コード) {
        this.企業コード = 企業コード;
    }

    public void set項目コード(String 項目コード) {
        this.項目コード = 項目コード;
    }

    public void set金額(String 金額) {
        this.金額 = 金額;
    }
}
