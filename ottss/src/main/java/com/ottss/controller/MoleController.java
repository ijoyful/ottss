package com.ottss.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.ottss.dao.MoleGameDAO;
import com.ottss.domain.MoleGameDTO;
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
        int point = 0;

        try {
           SessionInfo info = (SessionInfo)session.getAttribute("member");
            if (info == null) {
                model.put("state", "false");
                model.put("message", "로그인된 사용자가 없습니다.");
                return model;
            }

            boolean canEnter = dao.checkPoint(info.getId());

            if (canEnter) {
                state = "true"; 
                MoleGameDTO dto = new MoleGameDTO();
                dto.setId(info.getId());
                dto.setUsedPoint(10);
                boolean gameStarted = dao.startGame(dto);
                if (gameStarted) {
                    point = dao.updateUserPoint(dto);
                }
            } else {

                point = 0;
            }
        } catch (SQLException e) {

            model.put("errorMessage", e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {

            model.put("errorMessage", e.getMessage());
            e.printStackTrace();
        }

        model.put("state", state);
        model.put("updatedPoint", point);
        return model;
    }

    @ResponseBody
    @RequestMapping(value = "/games/mole/end", method = RequestMethod.POST)
    public Map<String, Object> endGame(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> model = new HashMap<String, Object>();
        String state = "false";  // 기본적으로 상태는 "false"로 설정
        
        HttpSession session = req.getSession();
        
        try {
            // 세션에서 로그인한 사용자 정보 가져오기
            SessionInfo info = (SessionInfo) session.getAttribute("member");
            
            if (info == null) {
                model.put("state", state);
                model.put("message", "로그인된 사용자가 없습니다.");
                return model;
            }

            // MoleGameDAO와 MoleGameDTO 객체 생성
            MoleGameDAO dao = new MoleGameDAO();
            MoleGameDTO dto = new MoleGameDTO();

            // 요청 파라미터 받아오기
            String userId = info.getId();
            int usedPoint = Integer.parseInt(req.getParameter("usedPoint"));
            int winPoint = Integer.parseInt(req.getParameter("winPoint"));
            int gameNum = Integer.parseInt(req.getParameter("gameNum"));
            String result = req.getParameter("result");

            // DTO 설정
            dto.setId(userId);
            dto.setUsedPoint(usedPoint);
            dto.setWinPoint(winPoint);
            dto.setGameNum(gameNum);
            dto.setResult(result);
            
            // 포인트를 업데이트하고, 게임 기록을 저장
            int newPoint = dao.updateUserPoint(dto);  // 새로운 포인트 값을 받음
            dao.insertPlayRecord(dto);  // 게임 기록 저장

            // 상태를 "true"로 설정
            state = "true";
            
            model.put("message", "게임이 종료되었습니다. 사용 포인트: " + usedPoint + ", 얻은 포인트: " + winPoint);
            model.put("newPoint", newPoint);  // 새로 갱신된 포인트 추가
        } catch (Exception e) {
            e.printStackTrace();
            model.put("state", state);
            model.put("message", "게임 종료 중 오류가 발생했습니다.");
        }
        
        // 응답 상태 반환
        model.put("state", state);
        return model;
    }


}
