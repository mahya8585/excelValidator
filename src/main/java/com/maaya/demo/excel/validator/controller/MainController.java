package com.maaya.demo.excel.validator.controller;

import com.maaya.demo.excel.validator.helper.AzureStorageHelper;
import com.maaya.demo.excel.validator.model.FileUploadForm;
import com.maaya.demo.excel.validator.model.ReportDto;
import com.maaya.demo.excel.validator.service.ExcelService;
import com.maaya.demo.excel.validator.service.JsonService;
import com.microsoft.azure.storage.StorageException;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/")
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    AzureStorageHelper azureStorageHelper;
    @Autowired
    ExcelService excelService;
    @Autowired
    JsonService jsonService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    String uploadExcel(@RequestParam("uploadFile") MultipartFile uploadFile, Model model) throws InvalidKeyException, StorageException, URISyntaxException, IOException {
        logger.debug("####upload####");

        // ファイルが空の場合は異常終了
        if(uploadFile.isEmpty()){
            return "ファイルがありません";
        }

        //Excelもにょもにょ処理
        final String fileName = uploadFile.getOriginalFilename();
        Sheet sheet = excelService.exec(uploadFile.getInputStream(), fileName);

        try(FileInputStream fis = new FileInputStream(fileName)){
            //Azure Storageにアップロードする
            String today = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
            azureStorageHelper.upload(fis, "excel", today + fileName, uploadFile.getBytes().length);
        }

        //用済みになったexcelファイルを削除する
        //TODO 本島はファイル出力したくない出力されていないときのエラー処理してないでござる
        File deleteFile = new File(fileName);
        deleteFile.delete();

        //シート情報がある＝完璧ファイル
        if (sheet != null){
            //TODO Json化する
            List<ReportDto> reports = jsonService.createJson();
            //TODO Azure Storageにアップロードする
            //azureStorageHelper.upload();

            logger.error(uploadFile.getOriginalFilename() + " end.");
            return "received";
        } else {
            logger.error(uploadFile.getOriginalFilename() + " end.");
            return "failure";
        }
    }



    @GetMapping(value = "/")
    String index(Model model) {
        logger.debug("###index method###");
        model.addAttribute("fileUploadForm", new FileUploadForm());
        return "index";
    }

}
