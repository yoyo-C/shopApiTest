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
 * Created by Bytes on 2017/9/21.
 */
public class AddApi_HttpTest extends TestBase {
    public static final Logger log = Logger.getLogger(AddApi_HttpTest.class);

    Map<String, Object> param =  new HashMap<String, Object>();
    List<String> ids = new ArrayList<String>();

    @BeforeTest
    public void setUp(){
        login("15158116767","crm","qwe123");
    }

    @DataProvider(name = "csvDataProvider_addApi")
    public static Object[][] data() throws IOException{
        return getData("/Users/Bytes/Documents/workspace/testCodes/shop-test/src/main/resources/testdata/Api_HttpTest/addApi.csv");
    }

    @Test(dataProvider = "csvDataProvider_addApi")
    public void AddApi_testcase1(final String name, final String system, final String type,final String url,final String status){
//      准备参数
        preParam(name, system, type, url);
        boolean status_csv = Boolean.valueOf(status);

//      增加api
        String result = HttpUtil.sendPostJson(HttpPostUrlEnum.ADDAPI_URL.getUrl(),param);
        HttpResult.checkStatus(result, status_csv);

        String id = ElephDBUtils.selectStrDB("api","api_id","name = '" + name + "'", "member");
        ids.add(id);
    }

    private void preParam(String name,String system,String type,String url){
        param.put("name",name);
        param.put("system",system);
        param.put("type",type);
        param.put("url",url);

    }
    @AfterTest
    public void cleanData(){
        for (String id : ids){
            ElephDBUtils.deleteDB("api","api_id = "+ id,"member");
        }
    }
}
