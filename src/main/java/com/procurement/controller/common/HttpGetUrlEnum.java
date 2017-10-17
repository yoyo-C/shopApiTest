package com.procurement.controller.common;

/**
 * Created by Bytes on 2017/7/24.
 */
public enum HttpGetUrlEnum {

    SELECTACTIVITYLIST_URL("/procurement/material/selectActivityList?","查询活动列表"),

    SELECTMATERIALLIST_URL("/procurement/material/selectMaterialList?","查询素材列表"),

    QUERYACTIVITYLIST_URL("/procurement/activity/queryActivityList?","查询活动列表"),

    AUTHORLIST_URL("/procurement/activityCreative/authorList","查询创意作者"),

    SELECTSPUBYID_URL("/procurement/spu/selectSpuById?","查询活动下关联的spu");

    private String url;

    private String name;

    HttpGetUrlEnum(String url, String name){
        this.url = url;
        this.name = name;
    }

    public String getUrl() {
        String server_prefix = "http://daily.crm.elephtribe.com/api";
        return server_prefix + url;
    }

    public String getUrl(int pageNow, int pageSize) {
        String server_prefix = "http://daily.crm.elephtribe.com/api";
        return server_prefix + url + "pageNow=" + pageNow + "&pageSize=" + pageSize;
    }

    public String getName() {
        return name;
    }
}
