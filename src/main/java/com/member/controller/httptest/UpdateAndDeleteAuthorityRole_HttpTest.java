package com.member.controller.httptest;

import com.elephtribe.tools.ElephDBUtils;
import com.elephtribe.tools.httputils.HttpResult;
import com.elephtribe.tools.httputils.HttpUtil;
import com.member.controller.common.HttpPostUrlEnum;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bytes on 2017/10/11.
 */
public class UpdateAndDeleteAuthorityRole_HttpTest {
    public static final Logger logger = Logger.getLogger(UpdateAndDeleteAuthorityRole_HttpTest.class);

    Map<String, Object> params = new HashMap<String, Object>();

    String id = null;
    String authorityId = null;

    @Test
    public void addAuthorityRole(){
        String name = "apitest_updateAndDeleteRole01";
        params.put("name",name);
        params.put("platform",1);
        authorityId = ElephDBUtils.selectStrDB("authority","authority_id","type = 0 and parent_id != 0 limit 1","member");
        params.put("authorityIds",authorityId);

        String result = HttpUtil.sendPostJson(HttpPostUrlEnum.ADDAUTHORITYROLE_URL.getUrl(),params);
        HttpResult.checkHttpSucess(result);

        id = ElephDBUtils.selectStrDB("authority_role","role_id","name='"+name + "'","member");
    }

    @Test(dependsOnMethods = "addAuthorityRole")
    public void updateRole(){
        params.put("platform",1);
        params.put("name","apitest_updateAndDeleteRole01_modified");
        params.put("roleId",id);
        params.put("authorityIds",authorityId);
        String result = HttpUtil.sendPostJson(HttpPostUrlEnum.UPDATEAUTHORITYROLEBYID_URL.getUrl(),params);
        HttpResult.checkHttpSucess(result);
    }

    @Test
    public void deleteRole(){
        params.clear();
        params.put("roleId",id);
        String result = HttpUtil.sendPostJson(HttpPostUrlEnum.DELETEAUTHORITYROLEBYID_URL.getUrl(),params);
        HttpResult.checkHttpSucess(result);
    }

}
