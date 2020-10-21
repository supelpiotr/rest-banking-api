package com.supelpiotr.utils.controller;

import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.json.JSONObject;
import org.slf4j.Logger;


public class BaseController {

    protected Logger logger;
    protected static final String MEDIA_TYPE = MediaType.APPLICATION_JSON_VALUE;

    public BaseController() {
        logger = LoggerFactory.getLogger(this.getClass());
    }

    protected String objectResult(Object object) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", true);
        jsonObject.put("data", object);
        return jsonObject.toString();
    }

    protected String errorMessage(String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", false);
        jsonObject.put("message", message);
        return jsonObject.toString();
    }

}
