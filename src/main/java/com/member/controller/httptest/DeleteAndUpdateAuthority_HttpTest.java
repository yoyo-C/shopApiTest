package com.member.controller.httptest;

import com.elephtribe.tools.ElephDBUtils;
import com.elephtribe.tools.TestBase;
import com.elephtribe.tools.httputils.HttpResult;
import com.elephtribe.tools.httputils.HttpUtil;
import com.member.controller.common.HttpPostUrlEnum;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bytes on 2017/10/10.
 */
public class DeleteAndUpdateAuthority_HttpTest extends TestBase{
    public static final Logger logger = Logger.getLogger(DeleteAndUpdateAuthority_HttpTest.class);

    Map<String, Object> params = new HashMap<String, Object>();

    List<String> ids = new ArrayList<String>();

    @DataProvider(name = "csvDataProvider")
    public Object[][] data() throws IOException{
        return getData("/Users/Bytes/Documents/workspace/testCodes/shop-test/src/main/resources/testdata/Authority_HttpTest/updateAndDeleteAuthority.csv");
    }

    @BeforeTest
    public void setUp(){
        login("15158116767","seller","qwe123");
    }

    @Test(dataProvider = "csvDataProvider")
    public void updateAuthority_case1(final String name,final String parentId,final String type,final String url,
                                               final String modifiedName){
        params.clear();
        params.put("name",name);

        params.put("type",type);

        if(parentId.equals("1")){
            params.put("url",url);
            params.put("apiIds","");
            String parentId_DB = ElephDBUtils.selectStrDB("authority","authority_id","parent_id = 0 and type ="+type+" limit 1","member");
            params.put("parentId",parentId_DB);
        }
        else {
            params.put("parentId",parentId);
        }

        String result = HttpUtil.sendPostJson(HttpPostUrlEnum.ADDAUTHORITY_URL.getUrl(),params);
        HttpResult.checkHttpSucess(result);

        String authorityId = ElephDBUtils.selectStrDB("authority","authority_id","name='" + name + "'","member");

        ids.add(authorityId);

        params.put("authorityId",authorityId);

        params.put("name",modifiedName);

        String updateResult = HttpUtil.sendPostJson(HttpPostUrlEnum.UPDATEAUTHORITYBYID_URL.getUrl(),params);

        HttpResult.checkHttpSucess(updateResult);

    }
    @Test(dependsOnMethods = "updateAuthority_case1")
    public void deleteAuthority(){
        for(String id:ids){
            Map<String,Object> param = new HashMap<String, Object>();
            param.put("authorityId",id);
            String deleteResult = HttpUtil.sendPostJson(HttpPostUrlEnum.DELETEAUTHORITYBYID_URL.getUrl(),param);
            HttpResult.checkHttpSucess(deleteResult);
        }
    }

    @AfterTest
    public void clearData(){
        for(String id:ids){
            ElephDBUtils.deleteDB("authority","authority_id="+ id,"member");
        }
    }
}
