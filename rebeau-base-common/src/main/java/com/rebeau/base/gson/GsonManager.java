package com.rebeau.base.gson;

import android.support.annotation.NonNull;

import com.rebeau.base.gson.adapter.DoubleDefaultAdapter;
import com.rebeau.base.gson.adapter.IntegerDefaultAdapter;
import com.rebeau.base.gson.adapter.LongDefaultAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * https://www.jianshu.com/p/3108f1e44155
 */
public class GsonManager {
/**   一个数组
 [
 {
 "amount": "1.00",
 "urlVer": "2"
 },
 {
 "amount": "1.00",
 "urlVer": "2"
 }
 ]
 */
/**
 * 格式1
 {
 "data": {
 "id": "0",
 "list": [
 {
 "adv_code": ""
 }
 ]
 }
 }
 */

    /**
     * 格式2
     * {
     * "data": {
     * "id": "0",
     * "list":{
     * "adv_code": "",
     * "adv_style": "3"
     * }
     * }
     * }
     */

    private static Gson gson = null;

    static {
        if (gson == null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .serializeNulls()
                    .enableComplexMapKeySerialization();

            /**
             * 设置默认值
             */
            gsonBuilder.registerTypeAdapter(Integer.class, new IntegerDefaultAdapter());
            gsonBuilder.registerTypeAdapter(int.class, new IntegerDefaultAdapter());
            gsonBuilder.registerTypeAdapter(Double.class, new DoubleDefaultAdapter());
            gsonBuilder.registerTypeAdapter(double.class, new DoubleDefaultAdapter());
            gsonBuilder.registerTypeAdapter(Long.class, new LongDefaultAdapter());
            gsonBuilder.registerTypeAdapter(long.class, new LongDefaultAdapter());

            gson = gsonBuilder.create();
        }
    }

    /**
     * 保存对象
     *
     * @param data 对象数据
     * @param <T>  对象的格式
     */
    public <T> String objectToString(@NonNull T data) {
        String gsonString = null;
        if (gson != null) {
            gsonString = gson.toJson(data);
        }
        return gsonString;
    }


    public <T> String objectToString(@NonNull List<T> data) {
        String gsonString = null;
        if (gson != null) {
            gsonString = gson.toJson(data);
        }
        return gsonString;
    }

    public <T> T stringToObject(String json, Class<T> clz) {
        return gson.fromJson(json, clz);
    }

    public <T> List<T> stringToObjectList(String json) {
        Type type = new TypeToken<List<T>>() {
        }.getType();
        return gson.fromJson(json, type);
    }


    /**
     * 这里的入参是json数组类型的字符串 jsonArrayString转成list
     */
    public static <T> List<T> stringToObjectList(String json, Class<T> cls) {
        Gson gson = new Gson();
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(gson.fromJson(elem, cls));
        }
        return list;
    }

    /**
     * 转成list中有map的
     *
     * @param gsonString
     * @return
     */
    public static <T> List<Map<String, T>> GsonToListMaps(String gsonString) {
        List<Map<String, T>> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString,
                    new TypeToken<List<Map<String, T>>>() {
                    }.getType());
        }
        return list;
    }

    /**
     * 转成map的
     *
     * @param gsonString
     * @return
     */
    public static <T> Map<String, T> GsonToMaps(String gsonString) {
        Map<String, T> map = null;
        if (gson != null) {
            map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
            }.getType());
        }
        return map;
    }

}
