package com.maaya.demo.excel.validator.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
//import java.util.stream.IntStream;

@Component
public class ExcelService {
    private static final Logger logger = LoggerFactory.getLogger(ExcelService.class);

    //TODO プロパティかDBで管理するようにすべき(´・ω・`)・・・(すべて0スタート)
    private static final int TARGET_SHEET_NUMBER = 0;
    private static final int ERROR_MESSAGE_CELL_NUMBER = 16;
    //private static final int NAME_START_ROW_NUMBER = 3;
    private static final int ID_CELL_NUMBER = 0;
    private static final int ID_START_ROW_NUMBER = 1;
//    private static final int NAME_START_CELL_NUMBER = 1;
//    private static final int NAME_END_CELL_NUMBER = 3;
    private static final int VALIDATE_TARGET_CELL_NUMBER = 5;

    //TODO ほんとならプロパティで管(´・ω・`)(´・ω・`)
    final String nullErrorMessage = "値の入力は必須です。";
    final String typeErrorMessage = "数値を入力してください。";

    /**
     * Excel まわりのフロー処理実行メソッド
     * @param excelStream
     * @return true:バリデーションチェックOK false:ファイル不備あり
     * @throws IOException
     */
    public Sheet exec(InputStream excelStream, String fileName) throws IOException {
        Workbook workbook = new XSSFWorkbook(excelStream);
        Sheet sheetData = workbook.getSheetAt(TARGET_SHEET_NUMBER);

//        int rowNum = NAME_START_ROW_NUMBER;
        int rowNum = ID_START_ROW_NUMBER;
        final int maxRowNum = sheetData.getLastRowNum();
        CellStyle errorCellStyle = createErrorCellStyle(workbook);

        //バリデーションエラーがあった場合にfalseになる
        boolean completed = true;

        //バリデーション処理を対象カラムすべてに行う(コードが存在する行を対象とする)
        while (rowNum <= maxRowNum) {
            //Rowデータが取れなくなったらそこから先は情報なしと判断する
            //TODO 空行を用いたファイルフォーマットを作成する場合は別の条件式が必要
            Row row = sheetData.getRow(rowNum);
            if (row == null) {
                break;
            }

            logger.debug("rowNum: " + rowNum);
            Cell idCell = row.getCell(ID_CELL_NUMBER);

            //バリデーションチェックする
            if (StringUtils.isNotEmpty(idCell.getStringCellValue())) {
                //TODO 今回はとりあえずnullチェック・型チェックだけ・・・
                boolean checked = validate(errorCellStyle, sheetData, sheetData.getRow(rowNum).getCell(VALIDATE_TARGET_CELL_NUMBER));

                if (!checked) {
                    completed = false;
                }
            }

            //この行の処理を終了する
            rowNum++;
        }


//            //項目名の列から値をとってきていづれかのカラムに値があればバリデーションチェックを行う。
//            for (int cn = NAME_START_CELL_NUMBER; cn <= NAME_END_CELL_NUMBER; cn++) {
//                logger.error("rowNum: " + rowNum + "  ,cn: " + cn);
//                Cell nameCell = row.getCell(cn);
//
//                //バリデーションチェックする
//                if (StringUtils.isNotEmpty(nameCell.getStringCellValue())){
//                    //TODO 今回はとりあえずnullチェック・型チェックだけ・・・
//                    boolean checked = validate(errorCellStyle, sheetData, sheetData.getRow(rowNum).getCell(VALIDATE_TARGET_CELL_NUMBER));
//
//                    if (!checked) {
//                        completed = false;
//                    }
//
//                    //この行の処理を終了する
//                    rowNum++;
//                    break;
//                }
//            }
//        }

        //TODO 一回ファイル出力しちゃってるのですが、本当ならファイル出力せず直接InputStreamにしたい
        FileOutputStream out = new FileOutputStream(fileName);
        workbook.write(out);
        out.close();

        //完璧ファイルだったらSheet情報を返却、エラーファイルの場合はnull返却
        if (completed) {
            return sheetData;
        } else {
            return null;
        }
    }

    /**
     * シート情報の取得
     * @param excelStream
     * @return
     * @throws IOException
     */
    public Sheet extractSheet(InputStream excelStream) throws IOException {
        Workbook workbook = new XSSFWorkbook(excelStream);
        return workbook.getSheetAt(TARGET_SHEET_NUMBER);
    }

    /**
     * エラーメッセージのフォントスタイル・カラムスタイルの作成
     * @param targetWorkbook
     * @return
     */
    private CellStyle createErrorCellStyle(Workbook targetWorkbook){
        //エラーメッセージフォント設定
        Font font = targetWorkbook.createFont();
        font.setBold(true);
        font.setColor(Font.COLOR_RED);

        //セルスタイルへフォントデータの設定
        CellStyle style = targetWorkbook.createCellStyle();
        style.setFont(font);

        return style;
    }


    /**
     * Nullチェックを行う。
     * 対象のセルがnullだった場合はシートの指定列に
     * @param sheet
     * @param targetCell
     * @return true: 正常な値  false: エラーあり
     */
    private boolean validate(CellStyle cellStyle, Sheet sheet, Cell targetCell){
        String errorMessages = "";

        //数値型検知された=何かしら値が検知された -> 他の型だった場合はtypeErrorを出す
        //TODO 日付型などに対応した条件文を書いてないのであまりいじめないでほしい。
        if (targetCell.getCellType() != CellType.NUMERIC) {
            errorMessages = typeErrorMessage;

            //空だった場合はnullErrorを出す
            if (StringUtils.isEmpty(targetCell.getStringCellValue())) {
                errorMessages = errorMessages + nullErrorMessage;
            }

            //cellのフォント・背景スタイルとエラーメッセージをsheetに記入
            Cell errorCell = sheet.getRow(targetCell.getRow().getRowNum()).createCell(ERROR_MESSAGE_CELL_NUMBER);
            errorCell.setCellValue(errorMessages);
            errorCell.setCellStyle(cellStyle);
        }

        //エラーがあったかなかったかを返却する
        return StringUtils.isEmpty(errorMessages);
    }

}
