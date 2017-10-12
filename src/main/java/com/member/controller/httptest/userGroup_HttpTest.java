package com.member.controller.httptest;


import com.elephtribe.tools.ElephDBUtils;
import com.elephtribe.tools.TestBase;
import com.elephtribe.tools.httputils.HttpResult;
import com.elephtribe.tools.httputils.HttpUtil;
import com.member.controller.common.HttpGetUrlEnum;
import com.member.controller.common.HttpPostUrlEnum;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bytes on 2017/10/12.
 */
public class userGroup_HttpTest extends TestBase{
    public static final Logger logger = Logger.getLogger(userGroup_HttpTest.class);

    Map<String, Object> params = new HashMap<String, Object>();

    String userGroupId = null;

    String userGroupName = null;

    @BeforeTest
    public void setUp(){
        login("15158116767","crm","qwe123");
    }

    @DataProvider(name = "csvDataProvider_addUserGroup")
    private Object[][] data_addUserGroup() throws IOException{
        return getData("/Users/Bytes/Documents/workspace/testCodes/shop-test/src/main/resources/testdata/Cuser_HttpTest/addCuserGroup.csv");
    }

    @DataProvider(name = "csvDataProvider_updateUserGroup")
    private Object[][] data_updateUserGroup() throws IOException{
        return getData("/Users/Bytes/Documents/workspace/testCodes/shop-test/src/main/resources/testdata/Cuser_HttpTest/updateCuserGroup.csv");
    }

    @Test(dataProvider = "csvDataProvider_addUserGroup")
    public void addUserGroup_case1(final String name,final String platform,final String roleIds,final String status) {
//       组装数据
        params.clear();
        params.put("name", name);
        params.put("platform", platform);
        if (roleIds.equals("1")) {
            String roleId = ElephDBUtils.selectStrDB("authority_role", "role_id", "platform=" + platform + " limit 1", "member");
            int[] id = new int[1];
            int roleId_int = Integer.valueOf(roleId);
            id[0] = roleId_int;
            params.put("roleIds", id);
            log.info(params);
        } else {
            int[] id = new int[0];
            params.put("roleIds", id);
        }
        Boolean http_status = Boolean.valueOf(status);

        String result = HttpUtil.sendPostJson(HttpPostUrlEnum.ADDUSERGROUP_URL.getUrl(), params);
        HttpResult.checkStatus(result, http_status);
        if (http_status) {
            userGroupId = ElephDBUtils.selectStrDB("cuser_group", "id", "name='" + name + "'", "member");
            userGroupName = name;
        }
    }

    @Test(dependsOnMethods = "addUserGroup_case1",dataProvider = "csvDataProvider_updateUserGroup")
    public void queryUserGroup(final String id,final String name,final String pageNo,final String pageSize,final String platform,final String status){
        params.clear();
        if(id.equals(1)){
            params.put("id",userGroupId);
        }
        else{
            params.put("id",id);
        }
        if(name.equals(1)){
            params.put("name",userGroupName);
        }
        else{
            params.put("name",name);
        }
        params.put("pageNo",pageNo);
        params.put("pageSize",pageSize);
        params.put("platform",platform);
        Boolean http_status = Boolean.valueOf(status);

        String result = HttpUtil.sendPostJson(HttpPostUrlEnum.QUERYUSERGROUP_URL.getUrl(),params);

        HttpResult.checkStatus(result, http_status);
    }

    @Test(dependsOnMethods = "addUserGroup_case1")
    public void updateUserGroup(){
        params.clear();
        params.put("name","apitest_addUserGroup01_modified");
        params.put("id",userGroupId);
        params.put("platform","2");
        int[] id = new int[0];
        params.put("roleIds", id);

        String result = HttpUtil.sendPostJson(HttpPostUrlEnum.UPDATEUSERGROUP_URL.getUrl(),params);
        HttpResult.checkHttpSucess(result);
    }

    @Test(dependsOnMethods = "addUserGroup_case1")
    public void deleteUserGroup(){
        String result = HttpUtil.sendGet(HttpGetUrlEnum.DELETEAUTHORITYGROUP_URL.getUrl()+"cuserGroupId="+userGroupId);
        HttpResult.checkHttpSucess(result);
    }

    @AfterTest
    public void cleanData(){
        ElephDBUtils.deleteDB("cuser_group","id="+userGroupId,"member");
    }
}
