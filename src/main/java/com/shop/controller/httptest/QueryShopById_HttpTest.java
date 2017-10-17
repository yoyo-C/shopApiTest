package com.shop.controller.httptest;

import com.elephtribe.tools.ElephDBUtils;
import com.elephtribe.tools.dataprovider.CsvData;
import com.elephtribe.tools.httputils.HttpResult;
import com.elephtribe.tools.httputils.HttpUtil;
import com.shop.controller.common.HttpPostUrlEnum;
import org.apache.log4j.Logger;
import org.testng.annotations.*;
import com.elephtribe.tools.TestBase;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bytes on 2017/7/19.
 */
public class QueryShopById_HttpTest extends TestBase{
    private static final Logger log = Logger.getLogger(QueryShopById_HttpTest.class);

    //创建店铺参数
    Map<String, Object> createShopParams = new HashMap<String, Object>();
    String shopId = null;


    @DataProvider(name = "csvDataProvider")
    public static Object[][] data() throws IOException {
        return getData("/Users/Bytes/Documents/workspace/testCodes/shop-test/src/main/resources/testdata/CreateShop_HttpTest/createShop.csv");
    }

    @Test(dataProvider = "csvDataProvider")
    public void QueryShopById_testCase1(final String address,final String areaCode, final String city, final String cityCode,
                                        final String mobile, final String provinceCode, final String shopName,final String type,
                                        final String ownerName){

        try{
//          delete shop
            ElephDBUtils.deleteDB("shop","shop_name= '" + shopName + "'","shop");
//        create shop
            preParam(address, areaCode, city, cityCode, mobile, provinceCode, shopName, type, ownerName);
            String createShopResult = HttpUtil.sendPostJson(HttpPostUrlEnum.CREATE_SHOP_URL.getUrl(), createShopParams);
            log.info(HttpPostUrlEnum.CREATE_SHOP_URL.getUrl()+createShopParams);
            HttpResult.checkHttpSucess(createShopResult);
            shopId = HttpResult.getEntry(createShopResult);


        }catch (Exception e) {
//            log.error("脚本出现未知异常！", e);

        }
    }

    @Test(dependsOnMethods = "QueryShopById_testCase1")
    public void QueryShopById_testCase2(){
        Map<String,Object> queryShopParams = new HashMap<String, Object>();
        queryShopParams.put("id",shopId);
        queryShopParams.put("operator","");

        String queryShopByIdResult = HttpUtil.sendPostJson(HttpPostUrlEnum.QUERY_SHOP_BY_ID_URL.getUrl(), queryShopParams);
        HttpResult.checkHttpSucess(queryShopByIdResult);
    }

    @Test
    public void QueryShopById_testCase3(){
        Map<String,Object> queryShopParams2 = new HashMap<String, Object>();
        queryShopParams2.put("id","999999999");
        queryShopParams2.put("operator","");
        String queryShopByIdResult2 = HttpUtil.sendPostJson(HttpPostUrlEnum.QUERY_SHOP_BY_ID_URL.getUrl(), queryShopParams2);
        HttpResult.checkStatus(queryShopByIdResult2,false);
    }

//    @AfterTest
//    public void deleteDB(){
//        ElephDBUtils.deleteDB("shop","id=" + shopId,"shop");
//        log.info("删除数据：delete from shop where id =" + shopId + ";\n");
//    }


    private void preParam(String address, String areaCode, String city, String cityCode, String mobile,
                          String provinceCode, String shopName, String type, String ownerName){
        createShopParams.put("address", address);
        createShopParams.put("areaCode", areaCode);
        createShopParams.put("city", city);
        createShopParams.put("cityCode", cityCode);
        createShopParams.put("provinceCode", provinceCode);
        createShopParams.put("shopName", shopName);
        createShopParams.put("type", type);
        createShopParams.put("ownerName", ownerName);
        createShopParams.put("mobile", mobile);
    }

}

