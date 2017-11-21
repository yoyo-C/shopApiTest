package com.elephtribe.tools;
import com.elephtribe.tools.dataprovider.CsvData;
import com.elephtribe.tools.httputils.HttpResult;
import com.elephtribe.tools.httputils.HttpUtil;
import com.member.controller.common.HttpPostUrlEnum;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by Bytes on 2017/7/11.
 */
public class TestBase extends CsvData {

    public static final Logger log = Logger.getLogger("TestBase.class");

    public static final String cLogin = "http://daily.crm.elephtribe.com/api/member/clogin.do";

    public static final String rLogin = "http://daily.robot.elephtribe.com/api/member/rlogin.do";

    public static final String bLogin = "http://daily.business.elephtribe.com/api/member/blogin.do";


    public void login(String mobile,String password, int platform){
        if(mobile == null|| password == null || platform == 0){
            return;
        }
        Map<String, Object> loginParams = new HashMap<String, Object>();

        loginParams.put("mobile", mobile);
        loginParams.put("password", password);

        try {

            if(platform == 1){
                String loginResult = HttpUtil.sendPostJson(cLogin, loginParams);

                Boolean checkResult = HttpResult.checkStatus(loginResult, true);

                log.info(">>>>>帐户登录成功" + checkResult);
            }

            if(platform == 2){
                String loginResult = HttpUtil.sendPostJson(bLogin, loginParams);

                Boolean checkResult = HttpResult.checkStatus(loginResult, true);

                log.info(">>>>>帐户登录成功" + checkResult);
            }

            if(platform == 3){
                String loginResult = HttpUtil.sendPostJson(rLogin, loginParams);

                Boolean checkResult = HttpResult.checkStatus(loginResult, true);

                log.info(">>>>>帐户登录成功" + checkResult);
            }


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
