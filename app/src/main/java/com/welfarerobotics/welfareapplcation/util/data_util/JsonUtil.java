package com.welfarerobotics.welfareapplcation.util.data_util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author : Hyunwoong
 * @when : 5/25/2019 1:29 PM
 * @homepage : https://github.com/gusdnd852
 */
public class JsonUtil {

    public static boolean isJson(String str) {
        try {
            new JSONObject(str);
        } catch (JSONException ex) {
            return false;
        }
        return true;
    }
}
