package com.member.controller.httptest;


import com.elephtribe.tools.ElephDBUtils;
import com.elephtribe.tools.TestBase;
import com.elephtribe.tools.httputils.HttpResult;
import com.elephtribe.tools.httputils.HttpUtil;
import com.member.controller.common.HttpPostUrlEnum;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bytes on 2017/9/21.
 */
public class AddApi_HttpTest extends TestBase {
    public static final Logger log = Logger.getLogger(AddApi_HttpTest.class);

    Map<String, Object> param =  new HashMap<String, Object>();

    @BeforeTest
    public void setUp(){
        login("15158116767","seller","qwe123");
    }

    @DataProvider(name = "csvDataProvider_addApi")
    public static Object[][] data() throws IOException{
        return getData("/Users/Bytes/Documents/workspace/testCodes/shop-test/src/main/resources/testdata/AddApi_HttpTest/addApi.csv");
    }

    @Test(dataProvider = "csvDataProvider_addApi")
    public void AddApi_testcase1(final String name, final String system, final String type,final String url){
        preParam(name, system, type, url);

        String result = HttpUtil.sendPostJson(HttpPostUrlEnum.ADDAPI_URL.getUrl(),param);
        HttpResult.checkStatus(result, true);

        ElephDBUtils.deleteDB("api","name = '" + name + "'","member");

    }

    private void preParam(String name,String system,String type,String url){
        param.put("name",name);
        param.put("system",system);
        param.put("type",type);
        param.put("url",url);

    }
}
