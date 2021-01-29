package com.uc.backend.dto;

public class FileUploadDto {
    private String url;

    public FileUploadDto(){}

    public FileUploadDto(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
