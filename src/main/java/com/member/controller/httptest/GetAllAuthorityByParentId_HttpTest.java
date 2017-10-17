package com.member.controller.httptest;

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
public class GetAllAuthorityByParentId_HttpTest extends TestBase{
    public static final Logger log = Logger.getLogger(GetAllAuthorityByParentId_HttpTest.class);

//    @BeforeTest
//    public void setUp(){
//        login("15158116767","crm","qwe123");
//    }

    @Test
    public void getAllAuth(){
        String result = HttpUtil.sendGet(HttpGetUrlEnum.GETALLAUTHORITYBYPARENTID_URL.getUrl(1,100) + "&parentId=0");
        HttpResult.checkHttpSucess(result);
    }

}
