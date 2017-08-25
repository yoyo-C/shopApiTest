package com.shop.controller.httptest;

import com.elephtribe.tools.TestBase;
import com.elephtribe.tools.httputils.HttpUtil;
import com.shop.controller.common.HttpPostUrlEnum;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bytes on 2017/7/27.
 */
public class QueryShopByKeys_HttpTest extends TestBase{
    private static Logger log = Logger.getLogger(QueryShopByKeys_HttpTest.class);


//    public void setUp() {
//
//        login("15158116767","seller","qwe123");
//
//    }


    @Test(dataProvider = "csvDataProvider")
    public void QueryShopByKeys_testCase1() {
//  case1:查全部
        Map<String, Object> queryShopByKeysParams = new HashMap<String, Object>();
        queryShopByKeysParams.put("city", "");
        queryShopByKeysParams.put("mobile", "");
        queryShopByKeysParams.put("pageNow", 1);
        queryShopByKeysParams.put("pageSize", 20);

        String queryShopResult = HttpUtil.sendPostJson(HttpPostUrlEnum.QUERY_SHOP_BY_KEYS_URL.getUrl(),queryShopByKeysParams);
    }
}

