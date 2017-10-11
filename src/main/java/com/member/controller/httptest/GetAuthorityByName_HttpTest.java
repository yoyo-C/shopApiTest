package com.member.controller.httptest;

import com.elephtribe.tools.ElephDBUtils;
import com.elephtribe.tools.TestBase;
import com.elephtribe.tools.httputils.HttpResult;
import com.elephtribe.tools.httputils.HttpUtil;
import com.member.controller.common.HttpGetUrlEnum;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Created by Bytes on 2017/10/10.
 */
public class GetAuthorityByName_HttpTest extends TestBase{
    public static final Logger log = Logger.getLogger(GetAuthorityByName_HttpTest.class);

    @BeforeTest
    public void setUp(){
        login("15158116767","crm","qwe123");
    }


    @Test
    public void getAuthorityByName_case1(){
        String result = HttpUtil.sendGet(HttpGetUrlEnum.GETAUTHORITYBYNAME_URL.getUrl() + "name=999999999");
        HttpResult.checkHttpSucess(result);
    }

    @Test
    public void getAuthorityByName_case2(){
        String name = ElephDBUtils.selectStrDB("authority","name","parent_id != 0 limit 1","member");
        String result = HttpUtil.sendGet(HttpGetUrlEnum.GETAUTHORITYBYNAME_URL.getUrl() + "name=" + name);
        HttpResult.checkHttpSucess(result);
    }
}
