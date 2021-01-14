package com.maaya.demo.excel.validator.model;


import org.springframework.web.multipart.MultipartFile;

public class FileUploadForm {
    private MultipartFile uploadFile;

    public void setUploadFile(MultipartFile uploadFile) {
        this.uploadFile = uploadFile;
    }

    public MultipartFile getUploadFile() {
        return uploadFile;
    }
}
