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
public class SelectMaterialList_HttpTest extends TestBase {
    private static final Logger log = Logger.getLogger(SelectMaterialList_HttpTest.class);

//    @BeforeTest
//    public void setUp() {
//
//        login("15158116767","seller","qwe123");
//    }

    @Test
    public void selectMaterialList_testCase1(){

        String selectMaterialListResult = HttpUtil.sendGet(HttpGetUrlEnum.SELECTMATERIALLIST_URL.getUrl(1,10));
        HttpResult.checkHttpSucess(selectMaterialListResult);
        Assert.assertEquals("查询成功",HttpResult.getMessage(selectMaterialListResult), "接口返回的message不为'查询成功'.......");

        String count = HttpResult.getCount(selectMaterialListResult);
        String dbCount = ElephDBUtils.selectStrDB("base_material","count(*)","type = 1 and is_delete = 0","message");
        Assert.assertEquals(count,dbCount,"接口返回的活动条数和数据库不一致......");
    }

    @Test
    public void selectMaterialList_testCase2(){

        String selectMaterialListResult = HttpUtil.sendGet(HttpGetUrlEnum.SELECTMATERIALLIST_URL.getUrl(0,10));
        HttpResult.checkStatus(selectMaterialListResult,false);
        Assert.assertEquals("查询出错",HttpResult.getMessage(selectMaterialListResult), "接口返回的message不为'查询出错'.......");

    }

    @Test
    public void selectMaterialList_testCase3(){

        String selectMaterialListResult = HttpUtil.sendGet(HttpGetUrlEnum.SELECTMATERIALLIST_URL.getUrl(1,0));
        HttpResult.checkHttpSucess(selectMaterialListResult);
        Assert.assertEquals("查询成功",HttpResult.getMessage(selectMaterialListResult), "接口返回的message不为'查询成功'.......");

    }
}
