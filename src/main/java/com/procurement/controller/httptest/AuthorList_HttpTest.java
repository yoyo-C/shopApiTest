package com.procurement.controller.httptest;

import com.elephtribe.tools.ElephDBUtils;
import com.elephtribe.tools.TestBase;
import com.elephtribe.tools.httputils.HttpResult;
import com.elephtribe.tools.httputils.HttpUtil;
import com.procurement.controller.common.HttpGetUrlEnum;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Created by Bytes on 2017/8/9.
 */
public class AuthorList_HttpTest extends TestBase {
    private static final Logger log = Logger.getLogger(AuthorList_HttpTest.class);

    @BeforeTest
    public void setUp() {
        login("15158116767","seller","qwe123");
    }

    @Test
    public void queryAuthorList_testCase1(){
        String result = HttpUtil.sendGet(HttpGetUrlEnum.AUTHORLIST_URL.getUrl());
        HttpResult.checkHttpSucess(result);

        String entry = HttpResult.getEntry(result);
        if(entry.equals(null) || entry.equals("[]")){
            log.info("查询创意作者返回无数据.....");
        }

        else{
            String user_id = HttpResult.getItemValueInEntryItemList(result, "userId");
            String db_user_id = ElephDBUtils.selectStrDB("activity_creative", " distinct user_id", "is_delete = 0 order by user_id asc limit 1", "procurement");
            Assert.assertEquals(user_id, db_user_id, "第一页第一条数据和数据库不一致.......");
        }
    }
}
