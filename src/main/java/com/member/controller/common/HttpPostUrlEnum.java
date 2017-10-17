package com.member.controller.common;

/**
 * Created by Bytes on 2017/9/21.
 */
public enum HttpPostUrlEnum {
    GETALLAPI_URL("/member/api/getAllApi.do","查询api列表"),

    ADDAPI_URL("/member/api/addApi.do","添加api"),

    UPDATEAPI_URL("/member/api/updateApi.do","更新api"),

    DELETEAPI_URL("/member/api/deleteApiById.do","删除api"),

    ADDAUTHORITY_URL("/member/authority/addAuthority.do","添加权限"),

    DELETEAUTHORITYBYID_URL("/member/authority/deleteAuthorityById.do","删除权限"),

    UPDATEAUTHORITYBYID_URL("/member/authority/updateAuthorityById.do","更新"),

    ADDAUTHORITYROLE_URL("/member/authorityRole/addAuthorityRole.do","添加角色"),

    UPDATEAUTHORITYROLEBYID_URL("/member/authorityRole/updateAuthorityRoleById.do","更新角色"),

    DELETEAUTHORITYROLEBYID_URL("/member/authorityRole/deleteAuthorityRoleById.do","删除角色"),

    ADDUSERGROUP_URL("/member/cuserGroup/add.do","添加用户组"),

    QUERYUSERGROUP_URL("/member/cuserGroup/query.do","查询用户组"),

    UPDATEUSERGROUP_URL("/member/cuserGroup/update.do","更新用户组"),

    ADD_URL("/member/cuser/add.do","添加用户"),

    QUERYUSER_URL("/member/cuser/query.do","查询用户"),

    UPDATEUSER_URL("/member/cuser/update.do","跟新用户");

    private String url;

    private String name;

    HttpPostUrlEnum(String url, String name){
        this.url = url;
        this.name = name;
    }

    public String getUrl(){
        String server_prefix = "http://daily.crm.elephtribe.com/api";
        return server_prefix + url;
    }

    public String getUrl(int pageNow, int pageSize) {
        String server_prefix = "";
        return server_prefix + url + "pageNow=" + pageNow + "&pageSize=" + pageSize;
    }

    public String getName(){
        return name;
    }

}
