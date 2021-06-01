package com.example.spring.boot.demo.controller;


import com.alibaba.fastjson.JSONObject;
import com.example.spring.boot.demo.entity.Entity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: 胡文良
 * @ClassName DemoController.java
 * @description:
 * @create: 2021-05-28 14:07
 **/
@Controller
public class DemoController {

    @PostMapping(value = "demo/{id}")
    @ResponseBody
    public JSONObject demo(@PathVariable(value = "id") String id,
                           @RequestParam(value = "param") String param,
                           @RequestBody Entity entity) {
        JSONObject data = new JSONObject();
        data.put("code", 200);
        data.put("date", "success");
        return data;
    }

}
