
package com.ottss.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.ottss.dao.MoleGameDAO;
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

/*
- 게임기록을 테이블에 업데이트 
player(플레이어) 테이블 
- 아이디 

- 포인트에 인서트

point_record (포인트 내역) 테이블
- id (아이디)

- pt_num(번호) pt_seq 로 
- categories(카테고리) 
- point (포인트) 인서트
- left_pt(잔여포인트) 업데이트 // 면 쿼리 3번써야할듯?
- pt_date(포인트 발생날짜) 


- 게임기록에 인서트  

- play_record 테이블 
- play_num 플레이 번호 NUMBER NN(NOTNULL) PK  / play_seq 로 
- play_date 플레이한 날짜 DATE NN 
- used_point 사용포인트 NUMBER NN 
- win_point 얻은포인트 NUMBER NN
- result 기록 VARCHAR2 NN 
- id 아이디 VARCHAR2 NN FK 참조 TB : player  참조 컬럼 : id
- game_num 게임번호 NUMBER NN FK 참조 TB : game 참조컬럼 : game_num 


- 게임 시작전에 입장 포인트 보유 여부 확인
- 입장 포인트는 게임시작 전에 확인 
- 확인할 테이블 point_record 의 id 검색  left_pt 확인해서 
- 입장 포인트 없으면 게임 x 
- ajax- json으로 가져오기 alert창으로 << 돈이있으면 플레이 없으면 안됨 
- 사용포인트와 얻은포인트 한번에 인서트 
 - 입장포인트는 게임시작버튼 클릭시 확인 하게 
 - 게임 종료시 사용포인트(입장료)와 얻은 포인트를 한번에 인서트 
 
 */

/*
  1. 게임 화면으로 넘어가기 
  2. 게임시작 버튼을 누르면 포인트 여부를 파악하여 게임 시작 or 못하게 ajax-json 사용
  3. 게임 시키기 
  4. 게임을 종료하면 
 */
@Controller
public class MoleController {
	@RequestMapping("/games/mole")
	public ModelAndView main(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("games/mole");
		
		return mav;
	}

	@RequestMapping(value = "/games/mole/end", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> endGame(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    HttpSession session = req.getSession();
	    SessionInfo info = (SessionInfo) session.getAttribute("member");
	    Map<String, Object> model = new HashMap<>();

	    // 세션에 사용자 정보가 없을 경우 처리
	    if (info == null) {
	        model.put("status", "error");
	        model.put("message", "세션 정보가 없습니다. 로그인해주세요.");
	        return model;
	    }

	    String userId = info.getId();  // 세션에서 사용자 ID 가져오기
	    int usedPoint = 10; // 게임 참여에 사용된 포인트
	    int winPoint = 15; // 게임에서 얻은 포인트 (예시로 15포인트)
	    MoleGameDAO dao = new MoleGameDAO();

	    try {
	        // 포인트 차감 (게임 입장 시 사용된 포인트)
	        int remainingPoint = dao.updateUserPoint(userId, usedPoint, winPoint);  // DB에서 포인트 업데이트

	        // 게임 기록 저장
	        dao.insertPlayRecord(userId, usedPoint, winPoint, remainingPoint, userId); // 게임 기록 삽입

	        model.put("status", "success");
	        model.put("message", "게임이 종료되었습니다. 결과를 저장했습니다.");
	        model.put("remainingPoint", remainingPoint);
	    } catch (Exception e) {
	        e.printStackTrace();
	        model.put("status", "error");
	        model.put("message", "게임 종료 처리 중 오류가 발생했습니다.");
	    }

	    return model;
	}


}