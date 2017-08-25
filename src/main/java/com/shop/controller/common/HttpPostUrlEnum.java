package com.shop.controller.common;

/**
 * Created by Bytes on 2017/7/11.
 */
public enum HttpPostUrlEnum {

    CREATE_SHOP_URL("/shop/posVirtual/creatShop","创建店铺","JSON"),

    QUERY_SHOP_BY_ID_URL("/shop/queryShopById","查询店铺","JSON"),

    QUERY_SHOP_BY_USERID_URL("/shop/queryShopByUserId","根据用户ID查询店铺信息","JSON"),

    LOGIN_URL("/member/login.do","登录","JSON"),

    QUERY_SHOP_BY_KEYS_URL("/queryShopByKeys","查询门店信息","JSON");

    private String url;

    private String name;

    private String type;

    HttpPostUrlEnum(String url, String name, String type) {
        this.url = url;
        this.name = name;
        this.type = type;
    }

    public String getUrl() {
        String server_prefix = "http://daily.elephtribe.com";
        return server_prefix + url;
    }

    public String getName() {
        return name;
    }

    public String getType(){
        return  type;
    }

    public static String getTypeByUrl(String url){

        for (HttpPostUrlEnum httpPostUrlEnum:HttpPostUrlEnum.values()){
            if (httpPostUrlEnum.getUrl().equals(url)){
                return  httpPostUrlEnum.getType();
            }
        }
        return  null;
    }




}
