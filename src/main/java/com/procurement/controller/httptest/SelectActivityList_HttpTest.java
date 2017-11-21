package com.procurement.controller.httptest;

import com.elephtribe.tools.ElephDBUtils;
import com.elephtribe.tools.TestBase;
import com.elephtribe.tools.httputils.HttpResult;
import com.elephtribe.tools.httputils.HttpUtil;
import com.procurement.controller.common.HttpGetUrlEnum;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Created by Bytes on 2017/7/24.
 */
public class SelectActivityList_HttpTest extends TestBase{
    private static final Logger log = Logger.getLogger(SelectActivityList_HttpTest.class);
    @BeforeTest
    public void setUp() {
        login("18390830656","qwe123",1);
    }

    @Test
    public void selectActivityList_testCase1(){

        String selectActivityResult = HttpUtil.sendGet(HttpGetUrlEnum.SELECTACTIVITYLIST_URL.getUrl(1,10));
        HttpResult.checkHttpSucess(selectActivityResult);
        Assert.assertEquals("查询成功",HttpResult.getMessage(selectActivityResult), "接口返回的message不为'查询成功'.......");

        String count = HttpResult.getCount(selectActivityResult);
        String dbCount = ElephDBUtils.selectStrDB("fodder","count(*)","is_delete = 0","procurement");
        Assert.assertEquals(count,dbCount,"接口返回的活动条数和数据库不一致......");
    }

    @Test
    public void selectActivityList_testCase2(){

        String selectActivityResult = HttpUtil.sendGet(HttpGetUrlEnum.SELECTACTIVITYLIST_URL.getUrl(0,10));
        HttpResult.checkHttpSucess(selectActivityResult);
        Assert.assertEquals("没有查询到数据",HttpResult.getMessage(selectActivityResult), "接口返回的message不为'没有查询到数据'.......");

    }

    @Test
    public void selectActivityList_testCase3(){

        String selectActivityResult = HttpUtil.sendGet(HttpGetUrlEnum.SELECTACTIVITYLIST_URL.getUrl(1,0));
        HttpResult.checkHttpSucess(selectActivityResult);
        Assert.assertEquals("没有查询到数据",HttpResult.getMessage(selectActivityResult), "接口返回的message不为'没有查询到数据'.......");

    }

}
