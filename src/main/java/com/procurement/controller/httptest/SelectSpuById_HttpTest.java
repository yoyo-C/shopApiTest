package com.procurement.controller.httptest;

import com.elephtribe.tools.ElephDBUtils;
import com.elephtribe.tools.TestBase;
import com.elephtribe.tools.httputils.HttpResult;
import com.elephtribe.tools.httputils.HttpUtil;
import com.procurement.controller.common.HttpGetUrlEnum;
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
 * Created by Bytes on 2017/7/24.
 */
public class SelectSpuById_HttpTest extends TestBase {
    private static final Logger log = Logger.getLogger(SelectSpuById_HttpTest.class);

    String fodderId = null;
    @BeforeTest
    public void setUp() {
        login("18390830656","qwe123");
    }

    @Test
    public void selectSpuById_testCase1(){
//      查询的活动id下无关联的spu
        String selectSpuResult = HttpUtil.sendGet(HttpGetUrlEnum.SELECTSPUBYID_URL.getUrl(1,10) + "&id=0");
        HttpResult.checkHttpSucess(selectSpuResult);
        Assert.assertEquals("查询成功",HttpResult.getMessage(selectSpuResult), "接口返回的message不为'查询成功'.......");
    }


    @DataProvider(name = "csvDataProvider")
    public static Object[][] data() throws IOException {
        return getData("/Users/Bytes/Documents/workspace/testCodes/shop-test/src/main/resources/testdata/SelectSpuById_HttpTest/selectSpuById.csv");
    }

    @Test(dataProvider = "csvDataProvider")
    public void selectSpuById_testcase2(final String activityName, final String materialUrl, final String spuList){
        Map<String, Object> createMaterialParam = new HashMap<String, Object>();

//        素材参数准备
        createMaterialParam.put("activityName",activityName);
        createMaterialParam.put("matterUrl",materialUrl);
        createMaterialParam.put("list",spuList);
        createMaterialParam.put("deviceIds","");

//      创建素材
        String createMaterialResult = HttpUtil.sendPostJson(HttpPostUrlEnum.CREATEMATERIAL_URL.getUrl(),createMaterialParam);

//      校验素材
        HttpResult.checkHttpSucess(createMaterialResult);
        String condition = "fodder_name = '"+activityName  + "' and matter_url = '" + materialUrl + "'";
        fodderId = ElephDBUtils.selectStrDB("fodder","id",condition, "procurement");

        String selectSpuResult = HttpUtil.sendGet(HttpGetUrlEnum.SELECTSPUBYID_URL.getUrl(1,10) + "&id=" +fodderId);
        HttpResult.checkHttpSucess(selectSpuResult);
        String spu = HttpResult.getItemValueInEntryItem(selectSpuResult,spuList,"barcode");

        Assert.assertEquals(spu,spuList,"接口返回的spu与创建时不一致........");



    }

    @AfterTest
    public void deleteDB(){
        ElephDBUtils.deleteDB("fodder","id = " + fodderId,"procurement");
        ElephDBUtils.deleteDB("sub_display","fodder_id = " + fodderId,"procurement");
        log.info("删除数据：delete from sub_display where fodder_id =" + fodderId + ";\n");

    }

}
