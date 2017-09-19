package com.hq.CloudPlatform.BaseFrame.entity;

import com.hq.CloudPlatform.BaseFrame.utils.validation.annotation.ValidationBean;

/**
 * Created by Administrator on 7/24/2017.
 */
@ValidationBean
public class FileInfo extends BaseEntity {

    /**
     * 文件名
     */
    private String name;

    /**
     * 对应到上传文件的相关信息
     */
    private UploadFileInfo uploadFileInfo;

    public UploadFileInfo getUploadFileInfo() {
        return uploadFileInfo;
    }

    public void setUploadFileInfo(UploadFileInfo uploadFileInfo) {
        this.uploadFileInfo = uploadFileInfo;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
