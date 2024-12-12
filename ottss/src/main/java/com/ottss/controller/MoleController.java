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
        return new ModelAndView("games/mole");
    }

    // 게임 시작 요청
    @ResponseBody
    @RequestMapping(value = "/games/mole/start", method = RequestMethod.POST)
    public Map<String, Object> startGame(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> model = new HashMap<>();
        HttpSession session = req.getSession();

        try {
            // 세션에서 로그인된 사용자 정보 확인
            SessionInfo info = (SessionInfo) session.getAttribute("member");
            if (info == null) {
                model.put("state", "false");
                model.put("message", "로그인된 사용자가 없습니다.");
                return model;
            }

            String userId = info.getId();

            // 포인트 확인 및 차감 (입장 가능 여부 확인)
            boolean canEnter = dao.checkPoint(userId);
            if (!canEnter) {
                model.put("state", "false");
                model.put("message", "포인트가 부족합니다.");
                return model;
            }

            // 게임 시작 기록 삽입
            MoleGameDTO dto = new MoleGameDTO();
            dto.setId(userId);
            dto.setUsedPoint(10);  // 게임 시작 시 사용된 포인트 10
            dto.setGameNum(1);     // 임시 게임 번호 (추후 실제 게임 번호로 처리)
            dao.insertPlayRecordStart(dto);  // 게임 시작 기록 삽입

            model.put("state", "true");
            model.put("message", "게임이 시작되었습니다.");
        } catch (SQLException e) {
            e.printStackTrace();
            model.put("state", "false");
            model.put("message", "게임 시작 중 오류가 발생했습니다.");
        }

        return model;
    }

    // 게임 종료 요청
    @ResponseBody
    @RequestMapping(value = "/games/mole/end", method = RequestMethod.POST)
    public Map<String, Object> endGame(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> model = new HashMap<>();
        HttpSession session = req.getSession();

        try {
            // 세션에서 로그인된 사용자 정보 확인
            SessionInfo info = (SessionInfo) session.getAttribute("member");
            if (info == null) {
                model.put("state", "false");
                model.put("message", "로그인된 사용자가 없습니다.");
                return model;
            }

            String userId = info.getId();
            String result = req.getParameter("result");
            int gameNum = Integer.parseInt(req.getParameter("gameNum"));

            // 보상 포인트 계산 (예시: 승리 시 20 포인트, 패배 시 0 포인트)
            int rewardPoint = "win".equalsIgnoreCase(result) ? 20 : 0;

            // 게임 종료 기록 삽입
            MoleGameDTO dto = new MoleGameDTO();
            dto.setId(userId);
            dto.setUsedPoint(10);  // 게임 종료 시 사용된 포인트는 10 (입장료와 동일)
            dto.setWinPoint(rewardPoint); // 보상 포인트 설정
            dto.setResult(result);  // 게임 결과
            dto.setGameNum(gameNum);  // 게임 번호
            dao.insertPlayRecordEnd(dto);  // 게임 종료 기록 삽입

            // 최종 포인트 업데이트
            int updatedPoint = dao.updateUserPoint(dto);  // 사용자 포인트 업데이트

            model.put("state", "true");
            model.put("message", "게임이 종료되었습니다. 최종 포인트: " + updatedPoint);
        } catch (SQLException e) {
            e.printStackTrace();
            model.put("state", "false");
            model.put("message", "게임 종료 중 오류가 발생했습니다.");
        }

        return model;
    }
}
