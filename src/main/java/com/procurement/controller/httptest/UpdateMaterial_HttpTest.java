package com.procurement.controller.httptest;

import com.elephtribe.tools.ElephDBUtils;
import com.elephtribe.tools.TestBase;
import com.elephtribe.tools.httputils.HttpResult;
import com.elephtribe.tools.httputils.HttpUtil;
import com.procurement.controller.common.HttpPostUrlEnum;
import com.shop.controller.httptest.CreateShop_HttpTest;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bytes on 2017/7/21.
 */
public class UpdateMaterial_HttpTest extends TestBase{
    private static final Logger log = Logger.getLogger(CreateShop_HttpTest.class);

    Map<String, Object> createMaterialParam = new HashMap<String, Object>();
    String fodderId = null;
    String condition = null;

//    @BeforeTest
//    public void setUp() {
//        login("15158116767","seller","qwe123");
//    }

    @DataProvider(name = "csvDataProvider")
    public static Object[][] data() throws IOException {
        return getData("/Users/Bytes/Documents/workspace/testCodes/shop-test/src/main/resources/testdata/UpdateMaterial_HttpTest/updateMaterial.csv");
    }

    @Test(dataProvider = "csvDataProvider")
    public void createMaterial_testCase1(final String activityName, final String materialUrl, final String spuList, final String newSpuList) {
//      数据准备
        condition = "fodder_name = '" + activityName + "'and matter_url = '" + materialUrl + "'";

        createMaterialParam.put("activityName", activityName);
        createMaterialParam.put("matterUrl", materialUrl);
        createMaterialParam.put("list", spuList);

//      创建素材
        String createMaterialResult = HttpUtil.sendPostJson(HttpPostUrlEnum.CREATEMATERIAL_URL.getUrl(), createMaterialParam);
        HttpResult.checkHttpSucess(createMaterialResult);
        fodderId = ElephDBUtils.selectStrDB("fodder","id",condition, "procurement");

        Map<String, Object> updateMaterialParam = new HashMap<String, Object>();
        updateMaterialParam.put("list", newSpuList);
        updateMaterialParam.put("id",fodderId);
        String updateMaterialResult = HttpUtil.sendPostJson(HttpPostUrlEnum.UPDATEMATERIAL_URL.getUrl(), updateMaterialParam);

//      校验结果
        HttpResult.checkHttpSucess(updateMaterialResult);
        String spu = ElephDBUtils.selectStrDB("sub_display","barcode","fodder_id = " + fodderId, "procurement");
        Assert.assertEquals(spu,newSpuList);


    }

    @Test(dependsOnMethods = "createMaterial_testCase1")
    public void createMaterial_testCase2(){
        Map<String, Object> updateMaterialParam2 = new HashMap<String, Object>();
        updateMaterialParam2.put("id",fodderId);
        String updateMaterialResult = HttpUtil.sendPostJson(HttpPostUrlEnum.UPDATEMATERIAL_URL.getUrl(), updateMaterialParam2);
        HttpResult.checkStatus(updateMaterialResult,false);

    }
    @AfterTest
    public void deleteDB(){
        ElephDBUtils.deleteDB("fodder",condition,"procurement");
        ElephDBUtils.deleteDB("sub_display","fodder_id = " + fodderId,"procurement");
        log.info("删除数据：delete from sub_display where fodder_id =" + fodderId + ";\n");

    }
}
