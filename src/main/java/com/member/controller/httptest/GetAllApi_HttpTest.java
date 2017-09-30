package com.member.controller.httptest;

import com.elephtribe.tools.ElephDBUtils;
import com.elephtribe.tools.TestBase;
import com.elephtribe.tools.httputils.HttpResult;
import com.elephtribe.tools.httputils.HttpUtil;
import com.member.controller.common.HttpPostUrlEnum;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bytes on 2017/9/22.
 */
public class GetAllApi_HttpTest extends TestBase{
    public static final Logger log = Logger.getLogger(GetAllApi_HttpTest.class);

    Map<String, Object> param = new HashMap<String,Object>();

    @BeforeTest
    public void setUp(){
        login("15158116767","seller","qwe123");
    }

    @DataProvider(name = "csvDataProvider_getAllApi")
    public Object[][] getData() throws IOException{
        return getData("/Users/Bytes/Documents/workspace/testCodes/shop-test/src/main/resources/testdata/Api_HttpTest/getAllApi.csv");
    }

    @Test(dataProvider = "csvDataProvider_getAllApi")
    public void getAllApi_testcase1(final String url, final String system, final String author,final String pageNo, final String pageSize){
//      准备参数
        preParam(url,system,author,pageNo,pageSize);

//      查询api
        String result = HttpUtil.sendPostJson(HttpPostUrlEnum.GETALLAPI_URL.getUrl(),param);
        HttpResult.checkStatus(result, true);

//      数据库校验
        String count = HttpResult.getCount(result);
        String dbcount = system.equals("")? ElephDBUtils.selectStrDB("api","count(*)","url like '%" + url + "%' and author like '%" + author + "%' and is_delete = 0","member") : ElephDBUtils.selectStrDB("api","count(*)","url like '%" + url + "%' and system = "+system + " and author like '%" + author + "%' and is_delete = 0","member");
        Assert.assertEquals(count,dbcount);
        log.info(result);
    }


    private void preParam(String url,String system,String author,String pageNo,String pageSize){
        param.put("url",url);
        param.put("system",system);
        param.put("author",author);
        param.put("pageNow",pageNo);
        param.put("pageSize",pageSize);

    }
}
