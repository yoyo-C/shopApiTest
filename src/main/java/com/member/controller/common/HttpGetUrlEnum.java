package com.member.controller.common;

/**
 * Created by Bytes on 2017/9/21.
 */
public enum HttpGetUrlEnum {
    GETALLAUTHORITYROLE_URL("/member/authorityRole/getAllAuthorityRole.do?","获取全部角色"),

    GETALLAUTHORITYBYPARENTID_URL("/member/authority/getAllAuthorityByParentId.do?","查询权限"),

    GETAUTHORITYDETIAL_URL("/member/authority/getAuthorityDetail.do?","根据权限id查询权限"),

    GETAUTHORITYBYNAME_URL("/member/authority/getAuthorityByName.do?","根据名称查询权限"),

    DELETEAUTHORITYGROUP_URL("/member/cuserGroup/delete.do?","删除用户组");

    private String url;

    private String name;

    HttpGetUrlEnum(String url,String name){
        this.url = url;
        this.name = name;
    }

    public String getUrl(){
        String server_prefix = "http://daily.elephtribe.com";
        return server_prefix + url;
    }

    public String getName(){
        return name;
    }

    public String getUrl(int pageNow, int pageSize) {
        String server_prefix = "http://daily.elephtribe.com";
        return server_prefix + url + "pageNow=" + pageNow + "&pageSize=" + pageSize;
    }
}
