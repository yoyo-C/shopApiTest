package com.member.controller.httptest;

import com.elephtribe.tools.ElephDBUtils;
import com.elephtribe.tools.httputils.HttpResult;
import com.elephtribe.tools.httputils.HttpUtil;
import com.member.controller.common.HttpGetUrlEnum;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by Bytes on 2017/10/11.
 */
public class GetAllAuthorityRole_HttpTest {
    public static final Logger logger = Logger.getLogger(GetAllAuthorityRole_HttpTest.class);

    @Test
    public void getAllAuthorityRole_case1(){
        String result = HttpUtil.sendGet(HttpGetUrlEnum.GETALLAUTHORITYROLE_URL.getUrl(1,10));

        HttpResult.checkHttpSucess(result);

        String count = HttpResult.getCount(result);

        String dbcount = ElephDBUtils.selectStrDB("authority_role","count(*)","is_delete=0","member");

        Assert.assertEquals(count,dbcount);
    }

    @Test
    public void getAllAuthorityRole_case2(){
        String result = HttpUtil.sendGet(HttpGetUrlEnum.GETALLAUTHORITYROLE_URL.getUrl(0,10));

        HttpResult.checkStatus(result, false);

    }

}
