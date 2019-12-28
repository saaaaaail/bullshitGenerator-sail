package com.sail.fun.bullshit.controller;

import com.alibaba.fastjson.JSONObject;
import com.sail.fun.bullshit.base.Result;
import com.sail.fun.bullshit.service.BullShitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Controller
@Slf4j
public class IndexController {

    @Autowired
    BullShitService bullShitService;

    @GetMapping("generate")
    @ResponseBody
    public Result<String> generator(String subject){
        String body = bullShitService.generator(subject);
        Result<String> result = new Result<>();
        result.setSuccessResult(body);
        return result;
    }

    @GetMapping("index")
    public ModelAndView index(HttpServletRequest request) throws UnknownHostException {
        ModelAndView modelAndView = new ModelAndView("generator");
        String httpUrl ="http://"+ InetAddress.getLocalHost().getHostAddress()+":"+request.getServerPort()+request.getContextPath();
        modelAndView.addObject("url",httpUrl);
        return modelAndView;
    }
}
