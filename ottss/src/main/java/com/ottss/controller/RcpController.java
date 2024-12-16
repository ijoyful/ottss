package com.ottss.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.ottss.mvc.annotation.Controller;
import com.ottss.mvc.annotation.RequestMapping;
import com.ottss.mvc.annotation.RequestMethod;
import com.ottss.mvc.annotation.ResponseBody;
import com.ottss.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class RcpController {
    // 게임 페이지로 이동
    @RequestMapping("/games/rsp")
    public ModelAndView main(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	ModelAndView mav = new ModelAndView("games/rsp");
    	return mav;
    }


    //게임 시작
    @ResponseBody
    @RequestMapping(value = "/games/rsp/start", method = RequestMethod.POST)
    public Map<String, Object> startGame(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        Map<String, Object> model = new HashMap<>();
        return model;
    }

    //게임 끝
    @ResponseBody
    @RequestMapping(value = "/games/rsp/end", method = RequestMethod.POST)
    public Map<String, Object> endGame(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        Map<String, Object> model = new HashMap<>();
        return model;
    }

}
