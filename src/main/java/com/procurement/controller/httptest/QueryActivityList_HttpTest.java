package com.procurement.controller.httptest;

import com.elephtribe.tools.ElephDBUtils;
import com.elephtribe.tools.httputils.HttpResult;
import com.elephtribe.tools.httputils.HttpUtil;
import com.procurement.controller.common.HttpGetUrlEnum;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by Bytes on 2017/8/8.
 */
public class QueryActivityList_HttpTest {
    private static final Logger log = Logger.getLogger(QueryActivityList_HttpTest.class);

//  验证查询接口是否正常，及首页第一条记录id
    @Test
    public void queryActivityList_testCase1() {
//      查询pageSize=10,pageNow=1
        String result = HttpUtil.sendGet(HttpGetUrlEnum.QUERYACTIVITYLIST_URL.getUrl(1, 10));

//      校验查询返回值 status
        HttpResult.checkHttpSucess(result);

//      校验查询返回值count和数据库一致
        String count = HttpResult.getCount(result);
        String dbcount = ElephDBUtils.selectStrDB("activity", "count(*)", "is_delete = 0", "procurement");
        Assert.assertEquals(count, dbcount, "接口返回的活动条数和数据库不一致......");

//      验证活动列表查询无结果
        String entry = HttpResult.getEntry(result);
        if(entry.isEmpty() || entry.equals("[]")){
            log.info("查询活动列表返回无数据.....");
        }
        else {
//          查询活动列表有结果时，验证首条记录id
            String id = HttpResult.getItemValueInEntryItemList(result, "id");
            String dbId = ElephDBUtils.selectStrDB("activity", "id", "is_delete = 0 order by id desc limit 1", "procurement");
            Assert.assertEquals(id, dbId, "第一页第一条数据和数据库不一致.......");
            log.info(id + dbId);
        }
    }


//  验证查询接口是否正常，及第二页第一条记录id

    @Test
    public void queryActivityList_testCase2() {
//      查询pageSize=10,pageNow=2
        String result = HttpUtil.sendGet(HttpGetUrlEnum.QUERYACTIVITYLIST_URL.getUrl(2, 10));

//      校验查询返回值 status
        HttpResult.checkHttpSucess(result);

//      验证活动列表查询无结果
        String entry = HttpResult.getEntry(result);
        if(entry.isEmpty() || entry.equals("[]")){
            log.info("查询活动列表第二页返回无数据.....");
        }
        else {
//          查询活动列表有结果时，验证首条记录id
            String id = HttpResult.getItemValueInEntryItemList(result, "id");
            String dbId = ElephDBUtils.selectStrDB("activity", "id", "is_delete = 0 order by id desc limit 10,1", "procurement");
            Assert.assertEquals(id, dbId, "第2页第一条数据和数据库不一致.......");
        }
    }
}
