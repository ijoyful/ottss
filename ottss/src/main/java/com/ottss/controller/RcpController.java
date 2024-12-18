package com.ottss.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.ottss.dao.RcpDAO;
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
    public Map<String, Object> startGame(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> model = new HashMap<>();
        HttpSession session = req.getSession();
        RcpDAO dao = new RcpDAO();
        String state = "false";

        try {
            // 세션에서 사용자 정보 가져오기
            SessionInfo info = (SessionInfo) session.getAttribute("member");
            if (info == null) {
                model.put("state", state);
                model.put("message", "로그인이 필요합니다.");
                return model;
            }

            String id = info.getId();
            int currentRound = Integer.parseInt(req.getParameter("currentRound"));
            // 사용자 포인트 확인
            boolean canStart = dao.checkPoint(id);
            
            if(currentRound == 1) {
            	 if (!canStart) {
                     model.put("state", state);
                     model.put("message", "게임을 시작하기 위한 포인트가 부족합니다.");
                     return model;
                 }

                 // 게임 시작 처리 (포인트 내역 insert, 사용자 보유포인트 update)
                 
                 dao.startGamePoint(id); // 사용자 포인트 차감
                 dao.insertStPoint(id); // 포인트 내역 기록

                 // 결과 설정
                 state = "true";
                 model.put("state", state);
                 model.put("message", "게임이 시작되었습니다!");
            } else { // 라운드가 1라운드가 아닌경우
            	state = "true";
            	model.put("state",state);	
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        return model;
    }


    //게임 종료
    @ResponseBody
    @RequestMapping(value = "/games/rsp/end", method = RequestMethod.POST)
    public Map<String, Object> endGame(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        //넘어오는 파라미터 : 최종 라운드, 획득 포인트
    	Map<String, Object> model = new HashMap<>();
    	HttpSession session = req.getSession();
    	RcpDAO dao = new RcpDAO();
    	String state = "false";
    	
    	try {
			SessionInfo info = (SessionInfo)session.getAttribute("member");
			PlayRecordDTO dto = new PlayRecordDTO();
			dto.setId(info.getId());
			dto.setRound(req.getParameter("currentRound"));
			dto.setWin_point(Integer.parseInt(req.getParameter("userPoint")));
			
			dao.insertEndPoint(dto); //포인트 내역 기록
			dao.insertPlayRecord(dto); //게임 플레이 기록
			dao.endGamePoint(dto); // 사용자 보유포인트 업데이트
			
			state = "true";
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	int userPoint = Integer.parseInt(req.getParameter("userPoint"));
    	
    	model.put("state", state);
    	model.put("userPoint", userPoint);
    	
        return model;
    }

}
