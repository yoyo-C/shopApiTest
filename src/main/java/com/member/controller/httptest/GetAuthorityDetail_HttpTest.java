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
public class GetAuthorityDetail_HttpTest extends TestBase{
    public static final Logger log = Logger.getLogger(GetAuthorityDetail_HttpTest.class);

    @BeforeTest
    public void setUp(){
        login("15158116767","seller","qwe123");
    }

    @Test
    public void getAuthorityDetail_case1() {
        String result = HttpUtil.sendGet(HttpGetUrlEnum.GETAUTHORITYDETIAL_URL.getUrl() + "authorityId=999999999");
        HttpResult.checkStatus(result, false);
    }

    @Test
    public void getAuthorityDetail_case2() {
        String authorityId = ElephDBUtils.selectStrDB("authority","authority_id", "parent_id != 0 and type = 0 limit 1","member");
        String result = HttpUtil.sendGet(HttpGetUrlEnum.GETAUTHORITYDETIAL_URL.getUrl() + "authorityId=" + authorityId);
        HttpResult.checkStatus(result, true);
    }
}
