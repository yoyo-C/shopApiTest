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
 * Created by Bytes on 2017/10/11.
 */
public class AddAuthorityRole_HttpTest extends TestBase{
    public static final Logger logger = Logger.getLogger(AddAuthorityRole_HttpTest.class);

    Map<String, Object> param = new HashMap<String, Object>();

    List<String> names = new ArrayList<String>();

    @BeforeTest
    public void setUp(){
        login("15158116767","crm","qwe123");
    }

    @DataProvider(name = "csvDataProvider_addRole")
    public Object[][] Data() throws IOException{
        return getData("/Users/Bytes/Documents/workspace/testCodes/shop-test/src/main/resources/testdata/Role_HttpTest/addRole.csv");
    }


    @Test(dataProvider = "csvDataProvider_addRole")
    public void addRole(final String name,final String platform,final String authorityIds,final String status){
        preparam(name,platform,authorityIds);
        Boolean http_status = Boolean.valueOf(status);

        String result = HttpUtil.sendPostJson(HttpPostUrlEnum.ADDAUTHORITYROLE_URL.getUrl(),param);
        HttpResult.checkStatus(result,http_status);
        names.add(name);
    }
    private void preparam(String name,String platform,String authorityIds){
        param.clear();
        param.put("name",name);
        param.put("platform",platform);
        if(authorityIds.equals("1")){
            String authorityId = ElephDBUtils.selectStrDB("authority","authority_id","type = 0 and parent_id != 0 limit 1","member");
            param.put("authorityIds",authorityId);
        }
        else{
            int[] list = new int[0];
            param.put("authorityIds",list);
        }
    }

    @AfterTest
    public void cleanData(){
        for (String nm : names){
            ElephDBUtils.deleteDB("authority_role","name ='"+nm + "'","member");
        }
    }
}
