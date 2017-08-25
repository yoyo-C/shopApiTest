package com.procurement.controller.httptest;

import com.elephtribe.tools.ElephDBUtils;
import com.elephtribe.tools.TestBase;
import com.elephtribe.tools.httputils.HttpResult;
import com.elephtribe.tools.httputils.HttpUtil;
import com.procurement.controller.common.HttpPostUrlEnum;
import org.apache.log4j.Logger;
import org.json.HTTP;
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
public class IsPlay_HttpTest extends TestBase{
    private static final Logger log = Logger.getLogger(IsPlay_HttpTest.class);
    String fodderId = null;
    String condition = null;

//    @BeforeTest
//    public void setUp() {
//        login("15158116767","seller","qwe123");
//    }


    @Test
    public void IsPlay_testCase1(){
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("spu","22266449611");

        String result = HttpUtil.sendPostJson(HttpPostUrlEnum.ISPLAY_URL.getUrl(),param);
        HttpResult.checkHttpSucess(result);
        Assert.assertEquals("null",HttpResult.getEntry(result),"接口请求返回的entry非null,和预期不符合......");
    }

    @DataProvider(name = "csvDataProvider")
    public static Object[][] data() throws IOException {
        return getData("/Users/Bytes/Documents/workspace/testCodes/shop-test/src/main/resources/testdata/IsPlay_HttpTest/isPlay.csv");
    }

    @Test(dataProvider = "csvDataProvider")
    public void IsPlay_testCase2(final String activityName, final String materialUrl, final String spuList){
        Map<String, Object> createMaterialParam = new HashMap<String, Object>();
//      素材参数准备
        createMaterialParam.put("activityName",activityName);
        createMaterialParam.put("matterUrl",materialUrl);
        createMaterialParam.put("list",spuList);

//      创建素材
        String createMaterialResult = HttpUtil.sendPostJson(HttpPostUrlEnum.CREATEMATERIAL_URL.getUrl(),createMaterialParam);

//      校验素材
        HttpResult.checkHttpSucess(createMaterialResult);
        condition = "fodder_name = '"+activityName  + "' and matter_url = '" + materialUrl + "'";

        fodderId = ElephDBUtils.selectStrDB("fodder","id",condition, "procurement");


        Map<String, Object> isPlayParam = new HashMap<String, Object>();
        isPlayParam.put("spu",spuList);
        String isPlayResult = HttpUtil.sendPostJson(HttpPostUrlEnum.ISPLAY_URL.getUrl(),isPlayParam);
        HttpResult.checkHttpSucess(isPlayResult);
        Assert.assertEquals(spuList,HttpResult.getEntryItem(isPlayResult,"spu"), "查询spu和接口返回不一致......");
    }

    @AfterTest
    public void deleteDB(){
        ElephDBUtils.deleteDB("fodder","id = " + fodderId,"procurement");
        ElephDBUtils.deleteDB("sub_display","fodder_id = " + fodderId,"procurement");
        log.info("删除数据：delete from sub_display where fodder_id =" + fodderId + ";\n");

    }
}
