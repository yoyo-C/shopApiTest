package com.procurement.controller.httptest;

import com.elephtribe.tools.ElephDBUtils;
import com.elephtribe.tools.TestBase;
import com.elephtribe.tools.httputils.HttpResult;
import com.elephtribe.tools.httputils.HttpUtil;
import com.procurement.controller.common.HttpPostUrlEnum;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bytes on 2017/8/9.
 */
public class QueryActivityCreative_HttpTest extends TestBase {
    private static final Logger log = Logger.getLogger("QueryActivityCreative_HttpTest.class");

    Map<String, Object> queryParam = new HashMap<String, Object>();
    String name = null;
    String author = null;
    String materialType = null;
    String dbId = null;

    @BeforeTest
    public void setUp() {
        login("18390830656","qwe123");
    }

    @Test
    public void queryActivityCreative_testCase1(){
//       查全部
        queryParam.put("activityCreativeAuthor","");
        queryParam.put("activityMaterialType","");
        queryParam.put("name","");
        queryParam.put("pageNo",1);
        queryParam.put("pageSize",1);

//      查询创意列表
        String result = HttpUtil.sendPostJson(HttpPostUrlEnum.QUERYACTIVITYCREATIVE_URL.getUrl(),queryParam);

//      校验结果
        HttpResult.checkHttpSucess(result);

//      校验返回的entry
        String entry = HttpResult.getEntry(result);
        if(entry.equals(null) || entry.equals("[]")){
            log.info("查询创意列表返回无数据.....");
        }
        else{
//          校验创意查询列表返回首条记录的id
            String activityCreativeDetails = HttpResult.getItemValueInEntryItemList(result,"activityCreativeDetailDTO");
            String id = HttpResult.getValueInJsonString(activityCreativeDetails,"id");

            dbId = ElephDBUtils.selectStrDB("activity_creative","id","is_delete = 0 order by gmt_modified desc\tlimit 1","procurement");
            Assert.assertEquals(id,dbId,"testcase1:查询的创意id和数据库不一致......");

            name = ElephDBUtils.selectStrDB("activity_creative","name","is_delete = 0 order by gmt_modified desc\tlimit 1","procurement");
            author = ElephDBUtils.selectStrDB("activity_creative","author","is_delete = 0 order by gmt_modified desc\tlimit 1","procurement");
            materialType = ElephDBUtils.selectStrDB("activity_creative","activity_material_type","is_delete = 0 order by gmt_modified desc\tlimit 1","procurement");
        }
    }


    @Test(dependsOnMethods = "queryActivityCreative_testCase1")
    public void queryActivityCreative_testCase2(){
//        带创意人，制作人，创意类型查询
        Map<String, Object> queryParam2 = new HashMap<String, Object>();
        queryParam2.put("name",name);
        queryParam2.put("activityCreativeAuthor",author);
        queryParam2.put("activityMaterialType",materialType);
        queryParam2.put("pageSize",10);
        queryParam2.put("pageNo",1);

        String result = HttpUtil.sendPostJson(HttpPostUrlEnum.QUERYACTIVITYCREATIVE_URL.getUrl(),queryParam);
        HttpResult.checkHttpSucess(result);
        log.info(result);

        String activityCreativeDetails = HttpResult.getItemValueInEntryItemList(result,"activityCreativeDetailDTO");
        String id = HttpResult.getValueInJsonString(activityCreativeDetails,"id");
        Assert.assertEquals(id,dbId,"testcase2:查询的创意id和查询时不一致......");

    }
}
