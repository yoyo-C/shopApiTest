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
 * Created by Bytes on 2017/8/9.
 */
public class CreateActivity_HttpTest extends TestBase{
    public static final Logger log = Logger.getLogger("CreateActivity_HttpTest.class");

    Map<String, Object> param = new HashMap<String, Object>();
    String id = null;

    @BeforeTest
    public void setUp() {
        login("18390830652","qwe123",1);
    }

    @DataProvider(name = "csvDataProvider_createActivity")
    public static Object[][] data1() throws IOException {
        return getData("/Users/Bytes/Documents/workspace/testCodes/shop-test/src/main/resources/testdata/CreateActivity_HttpTest/createActivity.csv");
    }

    @DataProvider(name = "csvDataProvider_updateActivity")
    public static Object[][] data2() throws IOException {
        return getData("/Users/Bytes/Documents/workspace/testCodes/shop-test/src/main/resources/testdata/CreateActivity_HttpTest/updateActivity.csv");
    }

    @Test(dataProvider = "csvDataProvider_createActivity")
    public void createActivity_testCase(final String activityCreativeIds, final String endDate, final String name,final String shopIds,final String startDate,final String status){
        preParam(activityCreativeIds,endDate,name,shopIds,startDate,"");
        Boolean statusBool = Boolean.parseBoolean(status);

        String result = HttpUtil.sendPostJson(HttpPostUrlEnum.CREATEACTIVITY_URL.getUrl(),param);
        HttpResult.checkStatus(result,statusBool);
        log.info(result);
        if(statusBool == true){
            id = ElephDBUtils.selectStrDB("activity","id","name = '" + name + "'","procurement");
            log.info(id);
//          验证数据status
            String dbStatus = ElephDBUtils.selectStrDB("activity","status","id = " + id,"procurement");
            Assert.assertEquals(dbStatus,"0","数据库状态不为0......");
//          验证数据库is_delete字段
            String dbIsDelete = ElephDBUtils.selectStrDB("activity","is_delete","id = " + id,"procurement");
            Assert.assertEquals(dbIsDelete,"0","数据库isdelete不为0......");

//          验证店铺-活动绑定关系
            String dbShopId = ElephDBUtils.selectStrDB("activity_shop_mapping","shop_id","activity_id = " + id + " order by gmt_create desc limit 1","procurement");
            Assert.assertEquals(dbShopId,shopIds,"数据库店铺-活动关系和创建时不一致........");

//          验证活动-创意绑定关系
            String dbActivityCreativeId = ElephDBUtils.selectStrDB("activity_creative_activity_mapping","activity_creative_id","activity_id = " + id,"procurement");
            Assert.assertEquals(dbActivityCreativeId,activityCreativeIds,"数据库创意-活动绑定关系和创建时不一致........");
        }

    }

    @Test(dependsOnMethods = "createActivity_testCase",dataProvider = "csvDataProvider_updateActivity")
    public void updateActivity_testCase(final String activityCreativeIds, final String endDate, final String name,final String shopIds,final String startDate,final String status){
        param.clear();
        preParam(activityCreativeIds,endDate,name,shopIds,startDate,id);
        Boolean statusBool = Boolean.parseBoolean(status);

        String result = HttpUtil.sendPostJson(HttpPostUrlEnum.UPDATEACTIVITY_URL.getUrl(),param);
        HttpResult.checkStatus(result,statusBool);

        if(statusBool == true){
//          验证数据status
            String dbStatus = ElephDBUtils.selectStrDB("activity","status","id = " + id,"procurement");
            Assert.assertEquals(dbStatus,"0","数据库状态不为0......");
//          验证数据库is_delete字段
            String dbIsDelete = ElephDBUtils.selectStrDB("activity","is_delete","id = " + id,"procurement");
            Assert.assertEquals(dbIsDelete,"0","数据库isdelete不为0......");

//          验证店铺-活动绑定关系
            String dbShopId = ElephDBUtils.selectStrDB("activity_shop_mapping","shop_id","activity_id = " + id,"procurement");
            Assert.assertEquals(dbShopId,shopIds,"数据库店铺-活动关系和创建时不一致........");

//          验证活动-创意绑定关系
            String dbActivityCreativeId = ElephDBUtils.selectStrDB("activity_creative_activity_mapping","activity_creative_id","activity_id = " + id,"procurement");
            Assert.assertEquals(dbActivityCreativeId,activityCreativeIds,"数据库创意-活动绑定关系和创建时不一致........");
        }
    }

    @Test(dependsOnMethods = "updateActivity_testCase")
    public void updateActivityStatus_testCase(){
        log.info("updateActivityStatus case1.........");
        param.clear();
        param.put("id",id);
        param.put("status","1");
        String result = HttpUtil.sendPostJson(HttpPostUrlEnum.UPDATEACTIVITYSTATUS_URL.getUrl(),param);
        HttpResult.checkHttpSucess(result);

        String dbStatus = ElephDBUtils.selectStrDB("activity","status","id = " + id,"procurement");
        Assert.assertEquals("1", dbStatus);

        log.info("updateActivityStatus case2.........");
        String status = "0";
        param.clear();
        param.put("id",id);
        param.put("status","0");
        String result2 = HttpUtil.sendPostJson(HttpPostUrlEnum.UPDATEACTIVITYSTATUS_URL.getUrl(),param);
        HttpResult.checkHttpSucess(result2);

        String dbStatus2 = ElephDBUtils.selectStrDB("activity","status","id = " + id,"procurement");
        Assert.assertEquals("0", dbStatus2);


    }


    @Test(dependsOnMethods = "updateActivityStatus_testCase")
    public void deleteActivity_testCase(){
        param.clear();
        param.put("id",id);
        param.put("isDelete","1");
        String result = HttpUtil.sendPostJson(HttpPostUrlEnum.DELETEACTIVITY_URL.getUrl(),param);
        HttpResult.checkHttpSucess(result);

        String dbIsDelete = ElephDBUtils.selectStrDB("activity","is_delete","id = " + id,"procurement");
        Assert.assertEquals("1", dbIsDelete);

    }

    @AfterTest
    public void deleteDB(){
        ElephDBUtils.deleteDB("activity","id = " + id,"procurement");
    }

    private void preParam(String activityCreativeIds,String endDate,String name,String shopIds,String startDate,String id){
        param.put("activityCreativeIds",activityCreativeIds);
        param.put("endDate",endDate);
        param.put("id",id);
        param.put("name",name);
        param.put("shopIds",shopIds);
        param.put("startDate",startDate);
    }
}
