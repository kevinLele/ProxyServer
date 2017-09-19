package com.hq.CloudPlatform.BaseFrame.entity;

import com.hq.CloudPlatform.BaseFrame.utils.validation.annotation.ValidationBean;

/**
 * Created by hellowin10 on 2017/7/23.
 */
@ValidationBean
public class UploadFileInfo extends BaseEntity {

    /**
     * 文件的唯一标识,通过MD5和SIZE生成
     */
    private String key;

    /**
     * 文件的存放目录
     */
    private String filePath;

    /**
     * 文件内容的MIMETYPE
     */
    private String contentType;

    /**
     * 文件的大小
     */
    private long size;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
