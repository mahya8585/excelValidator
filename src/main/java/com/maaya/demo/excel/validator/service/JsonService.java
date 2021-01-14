package com.maaya.demo.excel.validator.service;

import com.maaya.demo.excel.validator.model.ReportDto;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * DBにいれるためのJSONを作成するサービスクラス
 */
@Component
public class JsonService {
    //TODO 本来ならプロパティで値を持つべき
    private final int YEAR_ROW = 0;
    private final int YEAR_CELL = 5;

    public List<ReportDto> createJson(Sheet sheet) {
        //返却用
        List<ReportDto> reportJson = new ArrayList<>();

        //todo コードを読み取ってDTOに登録していく
        String year = sheet.getRow(YEAR_ROW).getCell(YEAR_CELL).toString();
        //todo 西暦の計算

        //todo DTOをJSON出力する(inputstream)

        return reportJson;
    }
}
