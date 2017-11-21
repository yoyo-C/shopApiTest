package com.procurement.controller.httptest;

import com.elephtribe.tools.ElephDBUtils;
import com.elephtribe.tools.TestBase;
import com.elephtribe.tools.httputils.HttpResult;
import com.elephtribe.tools.httputils.HttpUtil;
import com.procurement.controller.common.HttpPostUrlEnum;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bytes on 2017/7/21.
 */
public class SelectSpuByBarcode_HttpTest extends TestBase{
    private static final Logger log = Logger.getLogger(SelectSpuByBarcode_HttpTest.class);

//    @BeforeTest
//    public void setUp() {
//        login("15158116767","seller","qwe123");
//    }

    @Test
    public void SelectSpuByBarcode_testCase1(){
        Map<String, Object> spuParam1 = new HashMap<String, Object>();
        spuParam1.put("barcode","922266449611");
        String selectSpuResult = HttpUtil.sendPostJson(HttpPostUrlEnum.SELECTSPUBYBARCODE_URL.getUrl(), spuParam1);
        log.info(selectSpuResult);
        HttpResult.checkStatus(selectSpuResult,false);
    }

    @Test
    public void SelectSpuByBarcode_testCase2(){
        Map<String, Object> spuParam = new HashMap<String, Object>();
        spuParam.put("barcode","6922266449611");
        String selectSpuResult = HttpUtil.sendPostJson(HttpPostUrlEnum.SELECTSPUBYBARCODE_URL.getUrl(), spuParam);

        HttpResult.checkHttpSucess(selectSpuResult);
        String barcode = HttpResult.getEntryItem(selectSpuResult,"barcode");
        Assert.assertEquals(barcode,"6922266449611", "spu码与查询结果返回的spu不一致.........");
        String id = HttpResult.getEntryItem(selectSpuResult,"id");
        String dbId = ElephDBUtils.selectStrDB("spu","id","barcode =6922266449611","itemcenter");
        Assert.assertEquals(id,dbId, "spu查询结果返回的id与数据库不一致.........");
    }

}
