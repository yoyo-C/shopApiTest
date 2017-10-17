package com.shop.controller.httptest;

import com.elephtribe.tools.ElephDBUtils;
import com.shop.controller.common.HttpPostUrlEnum;
import com.elephtribe.tools.httputils.HttpResult;
import com.elephtribe.tools.httputils.HttpUtil;
import com.elephtribe.tools.TestBase;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bytes on 2017/7/12.
 */
public class CreateShop_HttpTest extends TestBase {
    private static final Logger log = Logger.getLogger(CreateShop_HttpTest.class);

    //创建店铺参数
    Map<String, Object> createShopParams = new HashMap<String, Object>();
    String shopName = null;
    String shopId = null;

    @BeforeSuite
    public void setUp() {
        login("18390830656","qwe123");
    }

    @DataProvider(name = "csvDataProvider")
    public static Object[][] data() throws IOException {
        return getData("/Users/Bytes/Documents/workspace/testCodes/shop-test/src/main/resources/testdata/CreateShop_HttpTest/createShop.csv");
    }


    @Test(dataProvider = "csvDataProvider")
    public void createShop_TestCase1(final String address,final String areaCode, final String city, final String cityCode,
                           final String mobile, final String provinceCode, final String shopName,final String type,
                           final String ownerName){
//      成功创建店铺
        try {
            ElephDBUtils.deleteDB("shop","shop_name='" + shopName + "'","shop");

            this.shopName = shopName;
                preParam(address, areaCode, city, cityCode, mobile, provinceCode, shopName, type, ownerName);
                String createShopResult = HttpUtil.sendPostJson(HttpPostUrlEnum.CREATE_SHOP_URL.getUrl(), createShopParams);
                shopId = HttpResult.getEntry(createShopResult);
                checkCreateShopResult(createShopResult);

        }catch (Exception e) {
//            log.error("脚本出现未知异常！", e);
        }
    }
    @Test(dependsOnMethods = "createShop_TestCase1")
    public void createShop_TestCase2(){
//        无法重复创建相同名称的店铺
        String result = HttpUtil.sendPostJson(HttpPostUrlEnum.CREATE_SHOP_URL.getUrl(), createShopParams);
        HttpResult.checkStatus(result,false);
    }

    @AfterTest
    public void deleteDB(){
        ElephDBUtils.deleteDB("shop","id=" + shopId,"shop");
        log.info("删除数据：delete from shop where id =" + shopId + ";\n");
    }

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

    private void checkCreateShopResult(String result){
        HttpResult.checkHttpSucess(result);

        //check DB
        String dbShopName = ElephDBUtils.selectStrDB("shop", "shop_name","id=" + shopId,"shop");
        String dbStatus = ElephDBUtils.selectStrDB("shop", "status","id=" + shopId,"shop");
        Assert.assertEquals(dbStatus,"0","店铺表status字段值不为0>>>>>>>>>\n");
        Assert.assertEquals(dbShopName,shopName,"店铺表shop_name字段值和创建时不一致>>>>>>>>>\n");
        log.info("testcase1: db结果校验：shopId:" + shopId + ",shopName:" + shopName + ",status:" + dbStatus +";");

    }
}
