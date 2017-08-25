package com.procurement.controller.httptest;

import com.elephtribe.tools.TestBase;
import com.elephtribe.tools.httputils.HttpResult;
import com.elephtribe.tools.httputils.HttpUtil;
import com.procurement.controller.common.HttpPostUrlEnum;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Bytes on 2017/7/24.
 */
public class UploadDiff_HttpTest extends TestBase{
    private static final Logger log = Logger.getLogger(UploadDiff_HttpTest.class);

//    @BeforeTest
//    public void setUp() {
//        login("15158116767","seller","qwe123");
//    }

    @Test
    public void UploadDiff_testCase1(){
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("gmtModify","2017-01-01 01:00:00");
        param.put("shopId","20448");

        String uploadDiffResult = HttpUtil.sendPostJson(HttpPostUrlEnum.UPLOADDIFF_URL.getUrl(),param);
        HttpResult.checkHttpSucess(uploadDiffResult);

        String message = HttpResult.getMessage(uploadDiffResult);
        Assert.assertEquals(message,"查到新的更新","接口返回的message不为'查到新的更新'..........");
    }

    @Test
    public void UploadDiff_testCase2(){
        Map<String, Object> param = new HashMap<String, Object>();
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

        param.put("gmtModify",timeStamp);
        param.put("shopId","20448");

        String uploadDiffResult = HttpUtil.sendPostJson(HttpPostUrlEnum.UPLOADDIFF_URL.getUrl(),param);
        HttpResult.checkHttpSucess(uploadDiffResult);
        Assert.assertEquals("null",HttpResult.getEntry(uploadDiffResult),"接口返回的entry不为'null'..........");
    }
}
