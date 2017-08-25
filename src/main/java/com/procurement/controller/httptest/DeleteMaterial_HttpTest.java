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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bytes on 2017/7/21.
 */
public class DeleteMaterial_HttpTest extends TestBase{
    private static final Logger log = Logger.getLogger(DeleteMaterial_HttpTest.class);

    Map<String, Object> createMaterialParam = new HashMap<String, Object>();
    String fodderId = null;
    String condition = null;

//    @BeforeTest
//    public void setUp() {
//        login("15158116767","seller","qwe123");
//    }

    @DataProvider(name = "csvDataProvider")
    public static Object[][] data() throws IOException {
        return getData("/Users/Bytes/Documents/workspace/testCodes/shop-test/src/main/resources/testdata/DeleteMaterial_HttpTest/deleteMaterial.csv");
    }

    @Test(dataProvider = "csvDataProvider")
    public void DeleteMaterial_testCase1(final String activityName, final String materialUrl, final String spuList, final String materialId){

//      素材参数准备
        condition = "fodder_name = '" + activityName + "'and matter_url = '" + materialUrl + "'";

        createMaterialParam.put("activityName",activityName);
        createMaterialParam.put("matterUrl",materialUrl);
        createMaterialParam.put("list",spuList);

//      创建素材
        String createMaterialResult = HttpUtil.sendPostJson(HttpPostUrlEnum.CREATEMATERIAL_URL.getUrl(),createMaterialParam);

//      校验素材
        HttpResult.checkHttpSucess(createMaterialResult);

        fodderId = ElephDBUtils.selectStrDB("fodder","id",condition, "procurement");
        Map<String, Object> deleteMaterialParam = new HashMap<String, Object>();
        deleteMaterialParam.put("id",fodderId);
        String deleteMaterialResult = HttpUtil.sendPostJson(HttpPostUrlEnum.DELETEMATERIALBYID_URL.getUrl(),deleteMaterialParam);

        HttpResult.checkHttpSucess(deleteMaterialResult);

        String isDelete = ElephDBUtils.selectStrDB("fodder","is_delete","id = " + fodderId,"procurement");

        Assert.assertEquals(isDelete,"1","is_delete和预期不一致.........");

    }
    @Test
    public void DeleteMaterial_testCase2(){

        Map<String, Object> deleteMaterialParam2 = new HashMap<String, Object>();
        deleteMaterialParam2.put("id","999999");
        String deleteMaterialResult = HttpUtil.sendPostJson(HttpPostUrlEnum.DELETEMATERIALBYID_URL.getUrl(),deleteMaterialParam2);
        HttpResult.checkStatus(deleteMaterialResult,false);

    }

    @AfterTest
    public void deleteDB(){
        ElephDBUtils.deleteDB("fodder","id = " + fodderId,"procurement");
        ElephDBUtils.deleteDB("sub_display","fodder_id = " + fodderId,"procurement");
        log.info("删除数据：delete from sub_display where fodder_id =" + fodderId + ";\n");

    }
}
