package com.cointizen.streaming.deserializer;

/*
 * Created by jignesh on 08-01-2015.
 */


import com.cointizen.streaming.http.ResponseData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

public class BaseDeserializer<T> implements JsonDeserializer {

    final Class<T> typeParameterClass;

    public BaseDeserializer(Class<T> typeParameterClass) {
        this.typeParameterClass = typeParameterClass;
    }

    @Override
    public ResponseData deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
            throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();
        final int status = jsonObject.get("code").getAsInt();

        String message = "";
        if (jsonObject.has("msg")) {
            message = jsonObject.get("msg").getAsString();
        } else if (jsonObject.has("error")) {
            message = jsonObject.get("error").getAsString();
        }
        JsonObject data = null;
        if (jsonObject.has("data") && !jsonObject.get("data").isJsonNull() && jsonObject.get("data").isJsonObject()) {
            data = jsonObject.get("data").getAsJsonObject();
        }

        // Delegate the deserialization to the context
        //Author[] authors = context.deserialize(jsonObject.get("authors"), Author[].class);
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        ResponseData responseObject = new ResponseData();
        T dataObj = null;
        try {
            dataObj = typeParameterClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if (data != null) {
            dataObj = gson.fromJson(data, typeParameterClass);
        }
        responseObject.setData(dataObj);
        responseObject.setCode(status);
        responseObject.setMessage(message);

        return responseObject;
    }
}

