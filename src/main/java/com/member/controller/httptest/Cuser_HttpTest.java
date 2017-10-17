package com.member.controller.httptest;

import com.elephtribe.tools.TestBase;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bytes on 2017/10/12.
 */
public class Cuser_HttpTest extends TestBase{
    public static final Logger logger = Logger.getLogger(Cuser_HttpTest.class);

    Map<String,Object> params = new HashMap<String,Object>();

    String CuserId = null;
//
//    @BeforeTest
//    private void setUp(){
//        login("15158116767","crm","qwe123");
//    }

    @DataProvider(name = "csvDataProvider_addCuser")
    public Object[][] data_addCuser() throws IOException{
        return getData("/Users/Bytes/Documents/workspace/testCodes/shop-test/src/main/resources/testdata/Cuser_HttpTest/addCuser.csv");
    }

    @DataProvider(name = "csvDataProvider_queryCuser")
    public Object[][] data_queryCuser() throws IOException{
        return getData("/Users/Bytes/Documents/workspace/testCodes/shop-test/src/main/resources/testdata/Cuser_HttpTest/queryCuser.csv");
    }

    @Test(dataProvider = "csvDataProvider_addCuser")
    public void addCuser(final String realName,final String mobile,final String password,final String platform,
                         final String cuserGroupId,final String roleId,final String status){
        params.clear();

    }

}
