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
public class Cuser_HttpTest extends TestBase{
    public static final Logger logger = Logger.getLogger(Cuser_HttpTest.class);

    Map<String,Object> params = new HashMap<String,Object>();

    String CuserId = null;

    String roleId = null;

    String groupId = null;

    String cMobile = null;

    @BeforeTest
    private void setUp(){
        login("18390830652","qwe123",1);
    }

    @DataProvider(name = "csvDataProvider_addCuser")
    public Object[][] data_addCuser() throws IOException{
        return getData("/Users/Bytes/Documents/workspace/testCodes/shop-test/src/main/resources/testdata/Cuser_HttpTest/addCuser.csv");
    }

    @DataProvider(name = "csvDataProvider_queryCuser")
    public Object[][] data_queryCuser() throws IOException{
        return getData("/Users/Bytes/Documents/workspace/testCodes/shop-test/src/main/resources/testdata/Cuser_HttpTest/queryCuser.csv");
    }

    @Test(dataProvider = "csvDataProvider_addCuser")
    public void addCuser(final String realName,final String mobile,final String password,final String platform,
                         final String cuserGroupId,final String roleIds,final String status){
        preparam(realName,mobile,password,platform,cuserGroupId,roleIds);
        String result = HttpUtil.sendPostJson(HttpPostUrlEnum.ADD_URL.getUrl(),params);
        Boolean http_status = Boolean.valueOf(status);
        log.info(params);
        log.info(result);
        HttpResult.checkStatus(result,http_status);

        if (http_status) {
            CuserId = ElephDBUtils.selectStrDB("c_user", "id", "real_name='" + realName + "'", "member");
            cMobile = mobile;
        }
    }
    @Test(dataProvider = "csvDataProvider_queryCuser",dependsOnMethods = "addCuser")
    public void queryUser(final String mobile,final String id,final String cuserGroupId,
                          final String platform,final String realName,final String pageNo,final String pageSize,final String status){
        params.clear();
        params.put("mobile",mobile);
        params.put("id",id);
        params.put("cuserGroupId",cuserGroupId);
        params.put("platform",platform);
        params.put("realName",realName);
        params.put("pageNo",pageNo);
        params.put("pageSize",pageSize);
        Boolean http_result = Boolean.valueOf(status);

        String result = HttpUtil.sendPostJson(HttpPostUrlEnum.QUERYUSER_URL.getUrl(),params);
        HttpResult.checkStatus(result,http_result);
    }
    @Test(dependsOnMethods = "addCuser")
    public void updateUser(){
        params.clear();
        Map<String, Object> cUserDetailDTO = new HashMap<String, Object>();
        cUserDetailDTO.put("realName","apitest_addUser01_modified");
        cUserDetailDTO.put("cuserGroupId",groupId);
        cUserDetailDTO.put("password","qwe123");
        cUserDetailDTO.put("mobile",cMobile);
        cUserDetailDTO.put("id",CuserId);

        params.put("cUserDetailDTO",cUserDetailDTO);
        log.info(params);
        String result = HttpUtil.sendPostJson(HttpPostUrlEnum.UPDATEUSER_URL.getUrl(),params);
        HttpResult.checkHttpSucess(result);
    }

    @Test(dependsOnMethods = "addCuser")
    public void disableUser(){
        String result = HttpUtil.sendGet(HttpGetUrlEnum.DISABLEUSER_URL.getUrl()+CuserId);
        HttpResult.checkHttpSucess(result);
    }

    @Test(dependsOnMethods = "addCuser")
    public void enbleUser(){
        String result = HttpUtil.sendGet(HttpGetUrlEnum.ENABLEUSER_URL.getUrl()+CuserId);
        HttpResult.checkHttpSucess(result);
    }


    @AfterTest
    public void cleanData(){
        ElephDBUtils.deleteDB("c_user","id="+CuserId,"member");
    }

    private void preparam(String realName,String mobile,String password,String platform,
                           String cuserGroupId,String roleIds){
        params.clear();
        Map<String, Object> cUserDetailDTO = new HashMap<String, Object>();
        cUserDetailDTO.put("realName",realName);
        cUserDetailDTO.put("platform",platform);
        cUserDetailDTO.put("password",password);
        cUserDetailDTO.put("mobile",mobile);
        if(cuserGroupId.equals("1")){
            groupId = ElephDBUtils.selectStrDB("cuser_group", "id", "platform=" + platform + " limit 1", "member");
        }
        else{
            groupId = cuserGroupId;
        }
        cUserDetailDTO.put("cuserGroupId",groupId);
        params.put("cUserDetailDTO",cUserDetailDTO);


        if (roleIds.equals("1")) {
            roleId = ElephDBUtils.selectStrDB("authority_role", "role_id", "platform=" + platform + " limit 1", "member");
            int[] id = new int[1];
            int roleId_int = Integer.valueOf(roleId);
            id[0] = roleId_int;
            params.put("roleIds", id);
        } else {
            int[] id = new int[0];
            params.put("roleIds", id);
        }
    }
}
