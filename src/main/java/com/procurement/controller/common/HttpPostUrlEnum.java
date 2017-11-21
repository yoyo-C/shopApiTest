package com.procurement.controller.common;

/**
 * Created by Bytes on 2017/7/20.
 */
public enum HttpPostUrlEnum {

    ISPLAY_URL("/procurement/client/isPlay","是否有匹配的音频","JSON"),

    UPLOADDIFF_URL("/procurement/client/uploadDiff","是否有新素材下载","JSON"),

    SELECTSPUBYBARCODE_URL("/procurement/spu/selectSpubyBarcode","查询商品","JSON"),

    UPDATEMATERIAL_URL("/procurement/spu/updateMaterial","上传素材","JSON"),

    DELETEMATERIALBYID_URL("/procurement/material/deleteMaterialById","删除素材","JSON"),

    CREATEMATERIAL_URL("//procurement/spu/createMaterial","创建素材","JSON"),

    QUERYACTIVITYCREATIVE_URL("/procurement/activityCreative/query","查询创意列表","JSON"),

    CREATEACTIVITY_URL("/procurement/activity/createActivity","创建活动","JSON"),

    UPDATEACTIVITY_URL("/procurement/activity/updateActivity","更新活动","JSON"),

    UPDATEACTIVITYSTATUS_URL("/procurement/activity/updateActivityStatus","更新活动状态","JSON"),

    DELETEACTIVITY_URL("/procurement/activity/deleteActivity","删除活动","JSON");

    private String url;

    private String name;

    private String type;

    HttpPostUrlEnum(String url, String name, String type) {
        this.url = url;
        this.name = name;
        this.type = type;
    }


    public String getUrl() {
        String server_prefix = "http://daily.crm.elephtribe.com/api";
        return server_prefix + url;
    }

    public String getName() {
        return name;
    }

    public String getType(){
        return  type;
    }

//            public static String getTypeByUrl(String url){
//
//                for (com.shop.controller.common.HttpPostUrlEnum httpPostUrlEnum: com.shop.controller.common.HttpPostUrlEnum.values()){
//                    if (httpPostUrlEnum.getUrl().equals(url)){
//                        return  httpPostUrlEnum.getType();
//                    }
//                }
//                return  null;
//            }
}
