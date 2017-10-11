package com.member.controller.httptest;

import com.elephtribe.tools.ElephDBUtils;
import com.elephtribe.tools.TestBase;
import com.elephtribe.tools.httputils.HttpResult;
import com.elephtribe.tools.httputils.HttpUtil;
import com.member.controller.common.HttpPostUrlEnum;
import org.apache.http.client.methods.HttpPost;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bytes on 2017/9/28.
 */
public class UpdateApi_HttpTest extends TestBase{
    public static final Logger log = Logger.getLogger(UpdateApi_HttpTest.class);

    Map<String, Object> param = new HashMap<String, Object>();
    String id = null;

    @BeforeTest
    public void setUp(){
        login("15158116767","crm","qwe123");
    }


    @DataProvider(name = "csvDataProvider_addApi")
    public static Object[][] addApi() throws IOException{
        return getData("/Users/Bytes/Documents/workspace/testCodes/shop-test/src/main/resources/testdata/Api_HttpTest/updateApi_addApi.csv");
    }

    @DataProvider(name = "csvDataProvider_updateApi")
    public static Object[][] updateApi() throws IOException{
        return getData("/Users/Bytes/Documents/workspace/testCodes/shop-test/src/main/resources/testdata/Api_HttpTest/updateApi.csv");
    }

    @Test(dataProvider = "csvDataProvider_addApi")
    public void addApi(final String name, final String system, final String type,final String url){
        param.put("name",name);
        param.put("system",system);
        param.put("type",type);
        param.put("url",url);

        String result = HttpUtil.sendPostJson(HttpPostUrlEnum.ADDAPI_URL.getUrl(),param);

        HttpResult.checkStatus(result, true);

        id = ElephDBUtils.selectStrDB("api","api_id","name = '" + name + "'", "member");
    }

    @Test(dependsOnMethods = "addApi",dataProvider = "csvDataProvider_updateApi")
    public void updateApi(final String name, final String system, final String type,final String url){
        param.clear();
        param.put("name",name);
        param.put("system",system);
        param.put("type",type);
        param.put("url",url);
        param.put("apiId",id);

        String result = HttpUtil.sendPostJson(HttpPostUrlEnum.UPDATEAPI_URL.getUrl(),param);

        HttpResult.checkStatus(result, true);

    }

    @Test(dependsOnMethods = "updateApi")
    public void deleteApi_testcase1(){
        param.clear();
        param.put("apiId", id);
        String result = HttpUtil.sendPostJson(HttpPostUrlEnum.DELETEAPI_URL.getUrl(),param);
        HttpResult.checkHttpSucess(result);
    }

    @Test(dependsOnMethods = "updateApi")
    public void deleteApi_testcase2(){
        param.clear();
        param.put("apiId", "99999");
        String result = HttpUtil.sendPostJson(HttpPostUrlEnum.DELETEAPI_URL.getUrl(),param);
        HttpResult.checkStatus(result, false);
    }


    @AfterTest
    public void cleanData(){
        ElephDBUtils.deleteDB("api", "api_id = " + id, "member");
    }
}
