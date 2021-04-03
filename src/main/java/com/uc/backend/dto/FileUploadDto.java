package com.uc.backend.dto;

public class FileUploadDto {
    private String url;

    private String name;

    public FileUploadDto(){}

    public FileUploadDto(String url) {
        this.url = url;
    }

    public FileUploadDto(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
