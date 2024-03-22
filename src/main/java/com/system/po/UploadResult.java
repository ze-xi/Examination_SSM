package com.system.po;


import java.io.Serializable;

/**
 * 文件上传的结果
 *
 */
public class UploadResult implements Serializable {

    /**
     * 文件URL
     */
    private String url;

    /**
     * 文件大小
     */
    private long fileSize;

    /**
     * 文档MIME类型
     */
    private String contentType;

    /**
     * 文件名
     */
    private String fileName;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
