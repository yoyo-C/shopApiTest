package com.elephtribe.tools;
import com.elephtribe.tools.dataprovider.CsvData;
import com.elephtribe.tools.httputils.HttpResult;
import com.elephtribe.tools.httputils.HttpUtil;
import com.shop.controller.common.HttpPostUrlEnum;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by Bytes on 2017/7/11.
 */
public class TestBase extends CsvData {

    public static final Logger log = Logger.getLogger("TestBase.class");


    public void login(String mobile,String password){
        if(mobile == null|| password == null){
            return;
        }
        Map<String, Object> loginParams = new HashMap<String, Object>();

        loginParams.put("mobile", mobile);
        loginParams.put("password", password);

        try {
            String loginResult = HttpUtil.sendPostJson(HttpPostUrlEnum.LOGIN_URL.getUrl(), loginParams);

            Boolean checkResult = HttpResult.checkStatus(loginResult, true);

            System.out.println(">>>>>帐户登录成功" + checkResult);

        }catch(AssertionError e){
            e.getStackTrace();
            log.error("登陆失败......."+ "\n");
        }

    }
    public void logout(String role){
        if(role == null){
            return;
        }
        Map<String, Object> logoutParams = new HashMap<String, Object>();

        logoutParams.put("role", role);

        String logoutResult = HttpUtil.sendPostJson("http://daily.crm.elephtribe.com/api/member/logout.do", logoutParams);

        HttpResult.checkStatus(logoutResult, true);
        System.out.println(">>>>>成功退出");
    }

}
