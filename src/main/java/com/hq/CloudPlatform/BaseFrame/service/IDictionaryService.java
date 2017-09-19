package com.hq.CloudPlatform.BaseFrame.service;

import com.alibaba.fastjson.JSONObject;

public interface IDictionaryService {

    /**
     * 通过数据字典的Code以及该字典的父字典code从缓存服务器获取数据字典的相关信息
     *
     * @param parentCode
     * @param code
     * @return
     */
    JSONObject getDictionaryByCode(String parentCode, String code);
}
