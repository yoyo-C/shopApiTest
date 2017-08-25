package com.procurement.controller.httptest;

import com.elephtribe.tools.ElephDBUtils;
import com.elephtribe.tools.TestBase;
import com.elephtribe.tools.httputils.HttpResult;
import com.elephtribe.tools.httputils.HttpUtil;
import com.procurement.controller.common.HttpPostUrlEnum;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.*;

/**
 * Created by Bytes on 2017/7/20.
 */
public class CreateMaterial_HttpTest extends TestBase {
    private static final Logger log = Logger.getLogger(CreateMaterial_HttpTest.class);

    Map<String, Object> createMaterialParam = new HashMap<String, Object>();
    String fodderId = null;
    String condition = null;

//    @BeforeTest
//    public void setUp() {
//        login("15158116767","seller","qwe123");
//    }

    @DataProvider(name = "csvDataProvider")
    public static Object[][] data() throws IOException {
        return getData("/Users/Bytes/Documents/workspace/testCodes/shop-test/src/main/resources/testdata/CreateMaterial_HttpTest/createMaterial.csv");
    }

    @Test(dataProvider = "csvDataProvider")
    public void createMaterial_testCase1(final String activityName, final String materialUrl, final String spuList, final String materialId){
//      数据清理
        String[] conditions = {"fodder_name = '" + activityName + "'","matter_url = '" + materialUrl + "'"};
        ElephDBUtils.deleteDB("fodder",conditions,"procurement");

//      素材参数准备
        createMaterialParam.put("activityName",activityName);
        createMaterialParam.put("matterUrl",materialUrl);
        createMaterialParam.put("list",spuList);

//      创建素材
        String createMaterialResult = HttpUtil.sendPostJson(HttpPostUrlEnum.CREATEMATERIAL_URL.getUrl(),createMaterialParam);

//      校验素材
        HttpResult.checkHttpSucess(createMaterialResult);

        condition = "fodder_name = '"+activityName  + "' and matter_url = '" + materialUrl + "'";
        String matterId = ElephDBUtils.selectStrDB("fodder","matter_id",condition, "procurement");
        Assert.assertEquals(matterId,materialId,"matter_id和预期不一致...........");

        String isDelete = ElephDBUtils.selectStrDB("fodder","is_delete",condition, "procurement");
        Assert.assertEquals(isDelete,"0","is_delete和预期不一致...........");

        fodderId = ElephDBUtils.selectStrDB("fodder","id",condition, "procurement");
        String spu = ElephDBUtils.selectStrDB("sub_display","barcode","fodder_id = " + fodderId, "procurement");
        Assert.assertEquals(spu,spuList,"barcode和预期不一致...........");


    }

    @Test
    public void CreateMaterial_testCase2(){
        Map<String, Object> createMaterialParam2 = new HashMap<String, Object>();
        createMaterialParam2.put("activityName","testfalse");
        createMaterialParam2.put("list","6922266449611");
        String createMaterialResult = HttpUtil.sendPostJson(HttpPostUrlEnum.CREATEMATERIAL_URL.getUrl(),createMaterialParam);
        HttpResult.checkStatus(createMaterialResult,false);
    }

    @AfterTest
    public void deleteDB(){
        ElephDBUtils.deleteDB("fodder","id = " + fodderId,"procurement");
        ElephDBUtils.deleteDB("sub_display","fodder_id = " + fodderId,"procurement");
        log.info("删除数据：delete from sub_display where fodder_id =" + fodderId + ";\n");

    }
}
