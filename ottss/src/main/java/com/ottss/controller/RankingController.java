package com.ottss.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ottss.dao.RankingDAO;
import com.ottss.domain.PlayRecordDTO;
import com.ottss.mvc.annotation.Controller;
import com.ottss.mvc.annotation.RequestMapping;
import com.ottss.mvc.view.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class RankingController {
		
	@RequestMapping("/ranking/ranking")
	public ModelAndView rankingSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ModelAndView mav = new ModelAndView("ranking/ranking");
		List<PlayRecordDTO> list1 = new ArrayList<PlayRecordDTO>();
		List<PlayRecordDTO> list2 = new ArrayList<PlayRecordDTO>();
		List<PlayRecordDTO> list3 = new ArrayList<PlayRecordDTO>();
		List<PlayRecordDTO> list4 = new ArrayList<PlayRecordDTO>();
		RankingDAO dao = new RankingDAO();
		String [] title = new String [10]; 
		try {
			list1 = dao.molRanking();
			list2 = dao.rcpRanking();
			list3 = dao.rolleteRanking();
			list4 = dao.quizRanking();
			
			
			title[0] = list1.get(0).getGame_title(); //두더지
			title[1] = list2.get(0).getGame_title(); //가위바위보
			title[2] = list3.get(0).getGame_title(); //룰렛
			title[3] = list4.get(0).getGame_title(); //퀴즈
			
			// 선택 정렬(내림차순)
			for (int i = 0; i < list1.size() - 1; i++) {
			    int maxIndex = i;
			    for (int j = i + 1; j < list1.size(); j++) {
			        if (list1.get(j).getWin_point() > list1.get(maxIndex).getWin_point()) {
			            maxIndex = j;
			        }
			    }
			    // Swap
			    PlayRecordDTO temp = list1.get(i);
			    list1.set(i, list1.get(maxIndex));
			    list1.set(maxIndex, temp);
			}
			
			for (int i = 0; i < list2.size() - 1; i++) {
			    int maxIndex = i;
			    for (int j = i + 1; j < list2.size(); j++) {
			        if (Integer.parseInt(list2.get(j).getResult()) > Integer.parseInt(list2.get(maxIndex).getResult())) {
			            maxIndex = j;
			        }
			    }
			    // Swap
			    PlayRecordDTO temp = list2.get(i);
			    list2.set(i, list2.get(maxIndex));
			    list2.set(maxIndex, temp);
			}
			
			for (int i = 0; i < list3.size() - 1; i++) {
			    int maxIndex = i;
			    for (int j = i + 1; j < list3.size(); j++) {
			        if (list3.get(j).getSum_win_point() > list3.get(maxIndex).getSum_win_point()) {
			            maxIndex = j;
			        }
			    }
			    // Swap
			    PlayRecordDTO temp = list3.get(i);
			    list3.set(i, list3.get(maxIndex));
			    list3.set(maxIndex, temp);
			}
			
			for (int i = 0; i < list4.size() - 1; i++) {
			    int maxIndex = i;
			    for (int j = i + 1; j < list4.size(); j++) {
			        if (Integer.parseInt(list4.get(j).getResult()) > Integer.parseInt(list4.get(maxIndex).getResult())) {
			            maxIndex = j;
			        }
			    }
			    // Swap
			    PlayRecordDTO temp = list4.get(i);
			    list4.set(i, list4.get(maxIndex));
			    list4.set(maxIndex, temp);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mav.addObject("list1", list1);
		mav.addObject("list2", list2);
		mav.addObject("list3", list3);
		mav.addObject("list4", list4);
		mav.addObject("title", title);
		
		return mav;
	}

	
	
}
