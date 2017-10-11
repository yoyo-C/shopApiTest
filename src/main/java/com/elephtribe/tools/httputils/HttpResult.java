package com.elephtribe.tools.httputils;

import org.apache.log4j.Logger;
import org.apache.maven.shared.utils.StringUtils;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.Assert;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Bytes on 2017/7/11.
 */
public class HttpResult {
    private static final Logger log = Logger.getLogger(HttpResult.class);

    //返回结果状态
    private  static  final  String RESULT_STATUS = "status";

    //返回结果码
   // private  static  final  String RESULT_CODE = "responseCode";

    //返回结果实体
   // private static  final  String[] RESULT_BODY = {"entry","result"};

    public static Boolean checkStatus(String result, Boolean status){

        if(StringUtils.isBlank(result)){
            log.error("checkStatus的入参result为空>>>>>>>\n");
            return false;
        }

        Object json = new JSONTokener(result).nextValue();
        if(json instanceof JSONObject){
            String message = null;
            String statusResult = null;
            try{
                Map<String, String> resultMap = parseJson2Map(result);

                statusResult = resultMap.get(RESULT_STATUS);

                message = resultMap.get("message");
                Assert.assertEquals(statusResult, String.valueOf(status),"http返回status状态不符合预期！");
            }catch (AssertionError e){
                e.getStackTrace();
                log.error(e.getMessage().toString() + "\n");
                throw e;

            }finally {
                if(StringUtils.isNotBlank(message)){
                    log.info("返回结果message：" + message);
                }
            }
            return true;
        }
        else{
//            log.error( "checkStatus入参非json格式>>>>>>\n");
            throw new Error("checkStatus入参非json格式>>>>>>\n");
//            return false;
        }


    }
    public static  String checkHttpSucess(String result) {
        String message = null;
        String statusResult = null;

        try{
            Map<String,String> resultMap = parseJson2Map(result);
            statusResult = resultMap.get(RESULT_STATUS);

            message = resultMap.get("message");
            Assert.assertTrue(statusResult.equals("true"),"http返回状态失败！");

        } catch (AssertionError e){
            e.getStackTrace();
            log.error(e.getMessage().toString() + "\n");
            throw e;

        }finally {
            if (StringUtils.isNotBlank(message)){
                System.out.println("返回结果信息：" + message);
            }
        }

        return statusResult;

    }

    private static Map<String, String> parseJson2Map(String jsonStr){

        Map<String, String> map = new HashMap<String, String>();

        JSONObject json = new JSONObject(jsonStr);
        Iterator iterator = json.keys();

        while (iterator.hasNext()){
            String key = String.valueOf(iterator.next());
            String value = json.get(key).toString();
            map.put(key, value);
        }
        return map;
    }

    public static String getEntry(String result){
        Map<String, String> map = parseJson2Map(result);
        return map.get("entry");
    }

    public static String getMessage(String result){
        Map<String, String> map = parseJson2Map(result);
        return map.get("message");
    }

    public static String getCount(String result){
        Map<String, String> map = parseJson2Map(result);
        return map.get("count");
    }

    public static String getEntryItem(String result, String item){
        String entryItems = getEntry(result);
        Map<String,String> map = parseJson2Map(entryItems);
        return map.get(item);
    }

    public static String getItemValueInEntryItem(String result, String item, String key){
        String entryItems = getEntry(result);
        Map<String,String> map = parseJson2Map(entryItems);
        String itemValue = map.get(item);

        Map<String,String> valueMap = parseJson2Map(itemValue);

        return valueMap.get(key);
    }

    public static String getItemValueInEntryItemList(String result, String key){
        String entryItems = getEntry(result);
        int openBrace = indexOfOpenBrace(entryItems);
        int closeBrace = indexOfCloseBrace(entryItems);
        if(openBrace < 0 || closeBrace < 0){
            log.info("entry value in request result is not an object....");
            return null;
        }
        Map<String,String> valueMap = parseJson2Map(entryItems.substring(openBrace,closeBrace+1));
        return valueMap.get(key);
    }

    private static int indexOfCloseBrace(String content){
        int openBrace = content.indexOf("{");
        int result = -1;
        if(openBrace < 0){
        }
        else {
            int count = 1;
            for (int i = openBrace + 1; i < content.length(); i++) {
                char temp = content.charAt(i);
                if (temp == '}') {
                    count--;
                }
                if (temp == '{') {
                    count++;
                }
                if (count == 0) {
                    return i;
                }
            }
        }
        return result;
    }

    private static int indexOfOpenBrace(String content){
        return content.indexOf("{");
    }

    public static String getValueInJsonString(String result, String key){
        Map<String, String> keyValue = parseJson2Map(result);
            return keyValue.get(key);
    }
}
