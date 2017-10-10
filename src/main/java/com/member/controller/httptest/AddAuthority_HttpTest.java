package com.member.controller.httptest;

import com.elephtribe.tools.ElephDBUtils;
import com.elephtribe.tools.TestBase;
import com.elephtribe.tools.httputils.HttpResult;
import com.elephtribe.tools.httputils.HttpUtil;
import com.member.controller.common.HttpPostUrlEnum;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bytes on 2017/10/10.
 */
public class AddAuthority_HttpTest extends TestBase{
    public static final Logger log = Logger.getLogger(AddAuthority_HttpTest.class);

    List<String> names = new ArrayList<String>();

    @BeforeTest
    public void setUp(){
        login("15158116767","seller","qwe123");
    }

    @DataProvider(name = "csvDataProvider_createAuthorityGroup")
    public Object[][] authorityGroup_Data() throws IOException{
        return getData("/Users/Bytes/Documents/workspace/testCodes/shop-test/src/main/resources/testdata/Authority_HttpTest/addAuthorityGroups.csv");
    }

    @DataProvider(name = "csvDataProvider_createAuthority")
    public Object[][] authority_Data() throws IOException{
        return getData("/Users/Bytes/Documents/workspace/testCodes/shop-test/src/main/resources/testdata/Authority_HttpTest/addAuthority.csv");
    }

    @Test(dataProvider = "csvDataProvider_createAuthorityGroup")
    public void addAuthorityGroup(final String name,final String parentId,final String type,final String status){
//      准备参数
        Map<String, Object> params_addAuthGroup = new HashMap<String, Object>();
        params_addAuthGroup.put("name",name);
        params_addAuthGroup.put("parentId",parentId);
        params_addAuthGroup.put("type",type);

        boolean csv_status = Boolean.valueOf(status);

//      创建权限组
        String result = HttpUtil.sendPostJson(HttpPostUrlEnum.ADDAUTHORITY_URL.getUrl(),params_addAuthGroup);
        HttpResult.checkStatus(result,csv_status);
        names.add(name);
    }

    @Test(dataProvider = "csvDataProvider_createAuthority")
    public void addAuthority(final String name,final String url,final String type,final String apiIds,final String status){
        Map<String, Object> params_addAuths = new HashMap<String, Object>();
        params_addAuths.put("name",name);
        params_addAuths.put("url",url);
        params_addAuths.put("type",type);
        if(apiIds.equals("888888")){
            String api_id = ElephDBUtils.selectStrDB("api", "api_id", "is_delete = 0 limit 1", "member");
            params_addAuths.put("apiIds",api_id);
        }
        else {
            params_addAuths.put("apiIds",apiIds);
        }
        String parentId =ElephDBUtils.selectStrDB("authority","authority_id","type = 0 limit 1", "member");

        params_addAuths.put("parentId",parentId);
        Boolean csv_status = Boolean.valueOf(status);

        String result = HttpUtil.sendPostJson(HttpPostUrlEnum.ADDAUTHORITY_URL.getUrl(),params_addAuths);

        HttpResult.checkStatus(result,csv_status);

        if(!name.equals(null)){
            names.add(name);
        }

    }


    @AfterTest
    public void cleanData(){
        for (String name : names){
            ElephDBUtils.deleteDB("authority","name = '"+ name + "'","member");
        }
    }

}
