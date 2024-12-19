package com.ottss.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.ottss.dao.MoleGameDAO;
import com.ottss.domain.PlayRecordDTO;
import com.ottss.domain.SessionInfo;
import com.ottss.mvc.annotation.Controller;
import com.ottss.mvc.annotation.RequestMapping;
import com.ottss.mvc.annotation.RequestMethod;
import com.ottss.mvc.annotation.ResponseBody;
import com.ottss.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class MoleController {

    private MoleGameDAO dao = new MoleGameDAO();

    // 게임 페이지로 이동
    @RequestMapping("/games/mole")
    public ModelAndView main(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	ModelAndView mav = new ModelAndView("games/mole");
    	return mav;
    }

    // 포인트 확인 및 게임 시작 요청
    @ResponseBody
    @RequestMapping(value = "/games/mole/start", method = RequestMethod.POST)
    public Map<String, Object> startGame(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        Map<String, Object> model = new HashMap<>();
        HttpSession session = req.getSession();
        String state = "false";  

        try {
           SessionInfo info = (SessionInfo)session.getAttribute("member");
            
           if (info == null) {
                model.put("state", "false");
                model.put("message", "로그인된 사용자가 없습니다.");
                return model;
            }

           String id = info.getId();
           int point = 10;
           int left_point = dao.leftPoint(id);
           
    			if (left_point < point) { // 만약 포인트가 부족하다면
    				model.put("state", state);
    				model.put("message", "참가 포인트가 부족합니다.");
    				return model;
    			} else {
    				PlayRecordDTO dto = new PlayRecordDTO();
    				dto.setId(id);
    				dto.setUsed_point(point);
    				dao.startGame(dto);
    				state = "true";
    			}
    			model.put("state", state);
    			return model;
    		} catch (Exception e) {
    			e.printStackTrace();
    			resp.sendError(406);
    			throw e;
    		}
    	}
    @ResponseBody
    @RequestMapping(value = "/games/mole/end", method = RequestMethod.POST)
    public Map<String, Object> endGame(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> model = new HashMap<String, Object>();
        String state = "false";  // 기본적으로 상태는 "false"로 설정      
        int userPoint = 0;
        int win_point = 0;
        HttpSession session = req.getSession();
        
    	try {
			SessionInfo info = (SessionInfo)session.getAttribute("member");
			PlayRecordDTO dto = new PlayRecordDTO();
				
			dto.setId(info.getId());
			
			dto.setWin_point(Integer.parseInt(req.getParameter("win_point")));
			dto.setUsed_point(Integer.parseInt(req.getParameter("entry")));
			dto.setResult(req.getParameter("result"));
			dto.setGame_num(Integer.parseInt(req.getParameter("game_num")));
			dto.setUserPoint(dao.leftPoint(dto.getId())+Integer.parseInt(req.getParameter("win_point")));
			
			
			dao.endGame(dto);
			userPoint = dto.getUserPoint();
			win_point = dto.getWin_point();
			state = "true";
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.put("state", state);
		model.put("userPoint", userPoint);
		model.put("win_point", win_point);
		
		return model;
	}

}