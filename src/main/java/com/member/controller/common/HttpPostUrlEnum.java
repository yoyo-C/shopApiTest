package com.member.controller.common;

/**
 * Created by Bytes on 2017/9/21.
 */
public enum HttpPostUrlEnum {
    GETALLAPI_URL("/member/api/getAllApi.do","查询api列表"),

    ADDAPI_URL("/member/api/addApi.do","添加api"),

    UPDATEAPI_URL("/member/api/updateApi.do","更新api"),

    DELETEAPI_URL("/member/api/deleteApiById.do","删除api");

    private String url;

    private String name;

    HttpPostUrlEnum(String url, String name){
        this.url = url;
        this.name = name;
    }

    public String getUrl(){
        String server_prefix = "http://daily.elephtribe.com";
        return server_prefix + url;
    }

    public String getUrl(int pageNow, int pageSize) {
        String server_prefix = "http://daily.elephtribe.com";
        return server_prefix + url + "pageNow=" + pageNow + "&pageSize=" + pageSize;
    }

    public String getName(){
        return name;
    }

}
