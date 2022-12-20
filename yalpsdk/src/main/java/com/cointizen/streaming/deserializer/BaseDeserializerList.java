package com.cointizen.streaming.deserializer;

/**
 * Created by jignesh on 08-01-2015.
 */


import com.cointizen.streaming.http.ResponseData;
import com.cointizen.streaming.utils.LogUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;


public class BaseDeserializerList<T extends ResponseData, U> implements JsonDeserializer {

    final Class<T> typeParameterClass;
    final Class<U> st;

    public BaseDeserializerList(Class<T> typeParameterClass, Class<U> s) {
        this.typeParameterClass = typeParameterClass;
        st = s;
    }

    @Override
    public T deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
            throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();

        final int status = jsonObject.get("code").getAsInt();


        String message = "";
        if (jsonObject.has("message")) {
            message = jsonObject.get("message").getAsString();
        } else if (jsonObject.has("error")) {
            message = jsonObject.get("error").getAsString();
        }
        LogUtil.error("json data --->>> " + jsonObject.toString());
        LogUtil.error("json data --->>> " + jsonObject.get("data"));
        // JsonObject data = null;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        U dataObj = null;
        try {
            dataObj = st.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        T tObj = null;
        try {
            tObj = typeParameterClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        SortedMap<Integer, U> mapValues = new TreeMap<Integer, U>();
        if (jsonObject.has("data") && !jsonObject.get("data").isJsonNull()) {

            if (jsonObject.get("data").isJsonArray()) {

                JsonArray arrayJson = jsonObject.get("data").getAsJsonArray();

                for (int j = 0; j < arrayJson.size(); j++) {

                    JsonElement itemData = arrayJson.get(j);
                    dataObj = gson.fromJson(itemData.getAsJsonObject(), st);
                    mapValues.put(j, dataObj);
                }
            } else {

                Set<Entry<String, JsonElement>> entries = jsonObject.getAsJsonObject("data").entrySet();

                for (Entry<String, JsonElement> iter : entries) {

                    String key = iter.getKey();
                    JsonElement itemData = iter.getValue();

                    dataObj = gson.fromJson(itemData.getAsJsonObject(), st);
                    mapValues.put(Integer.parseInt(key), dataObj);
                }
            }
            tObj.setCode(status);
            tObj.setMessage(message);
            tObj.setData(new LinkedList<U>(mapValues.values()));

        } else {
            mapValues.put(1, dataObj);
            tObj.setCode(status);
            tObj.setMessage(message);
            tObj.setData(new LinkedList<U>(mapValues.values()));
        }
        return tObj;
    }
}

