package com.ottss.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ottss.dao.PointShopDAO;
import com.ottss.domain.PointRecordDTO;
import com.ottss.domain.PointShopDTO;
import com.ottss.domain.SessionInfo;
import com.ottss.mvc.annotation.Controller;
import com.ottss.mvc.annotation.RequestMapping;
import com.ottss.mvc.annotation.RequestMethod;
import com.ottss.mvc.annotation.ResponseBody;
import com.ottss.mvc.view.ModelAndView;
import com.ottss.util.MyUtil;
import com.ottss.util.MyUtilBootstrap;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class PointShopController {
	
	@RequestMapping(value = "/shop/shop", method = RequestMethod.GET)
	public ModelAndView itemList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 상점 아이템 리스트
		// 넘어온 파라미터 : [페이지 번호, size]
		ModelAndView mav = new ModelAndView("shop/shop");
		
		PointShopDAO dao = new PointShopDAO();
		MyUtil util = new MyUtilBootstrap();
		
		try {
			String page = req.getParameter("page");
			int current_page = 1;
			if(page != null) {
				current_page = Integer.parseInt(page);
			}
			
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			
			if(schType == null) {
				schType = "all";
				kwd = "";
			}
			
			if(req.getMethod().equalsIgnoreCase("GET")) {
				kwd = URLDecoder.decode(kwd,"utf-8");
			}
			
			// 한페이지 상품 수
			String pageSize = req.getParameter("size");
			int size = pageSize == null ? 30 : Integer.parseInt(pageSize);
			
			int dataCount, total_page;
			
			if(kwd.length() != 0) {
				dataCount = dao.dataCount(schType, kwd);
			} else {
				dataCount = dao.dataCount();
			}
			total_page = util.pageCount(dataCount, size);
			
			if(current_page > total_page) {
				current_page = total_page;
			}
			
			int offset = (current_page - 1) * size;
			if(offset < 0) offset = 0;
			
			List<PointShopDTO> list;
			if(kwd.length() != 0) {
				list = dao.itemList(offset, size, schType, kwd);
			} else {
				list = dao.itemList(offset, size);
			}
			
			String cp = req.getContextPath();
			String query = "size=" + size;
			String listUrl;
			
			if (kwd.length() != 0) {
				query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
			}
			listUrl = cp + "/shop/shop?" + query;

			
			String paging = util.paging(current_page, total_page, listUrl);
			
			// 포워딩 jsp에 전달할 데이터
			mav.addObject("itemList", list);
			mav.addObject("dataCount", dataCount);
			mav.addObject("size", size);
			mav.addObject("page", current_page);
			mav.addObject("total_page", total_page);
			mav.addObject("paging", paging);
			mav.addObject("schType", schType);
			mav.addObject("kwd", kwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
	
	
	@RequestMapping(value = "/shop/inventory", method = RequestMethod.GET)
	public ModelAndView haveList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 인벤토리 리스트
		// 넘어온 파라미터 : [페이지 번호, size]
		ModelAndView mav = new ModelAndView("shop/inventory");
		
		PointShopDAO dao = new PointShopDAO();
		MyUtil util = new MyUtilBootstrap();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		try {
			String page = req.getParameter("page");
			int current_page = 1;
			if(page != null) {
				current_page = Integer.parseInt(page);
			}
			
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			
			if(schType == null) {
				schType = "all";
				kwd = "";
			}
			
			if(req.getMethod().equalsIgnoreCase("GET")) {
				kwd = URLDecoder.decode(kwd,"utf-8");
			}
			
			// 한페이지 아이템 수
			String pageSize = req.getParameter("size");
			int size = pageSize == null ? 30 : Integer.parseInt(pageSize);
			
			int dataCount, total_page;
			
			if(kwd.length() != 0) {
				dataCount = dao.dataCount(schType, kwd);
			} else {
				dataCount = dao.dataCount();
			}
			total_page = util.pageCount(dataCount, size);
			
			if(current_page > total_page) {
				current_page = total_page;
			}
			
			int offset = (current_page - 1) * size;
			if(offset < 0) offset = 0;
			
			String id = info.getId();
			
			List<PointShopDTO> list;
			List<PointShopDTO> inventory = dao.getPlayerInventory(id);
			if(kwd.length() != 0) {
				list = dao.getPlayerInventory(id);
			} else {
				list = dao.getPlayerInventory(id);
			}
			
			String cp = req.getContextPath();
			String query = "size=" + size;
			String listUrl;
			
			if (kwd.length() != 0) {
				query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
			}
			listUrl = cp + "/shop/inventory?" + query;
			
			String paging = util.paging(current_page, total_page, listUrl);
			
			// 포워딩 jsp에 전달할 데이터
			mav.addObject("itemList", list);
			mav.addObject("dataCount", dataCount);
			mav.addObject("size", size);
			mav.addObject("page", current_page);
			mav.addObject("total_page", total_page);
			mav.addObject("paging", paging);
			mav.addObject("schType", schType);
			mav.addObject("kwd", kwd);
			mav.addObject("inventory", inventory);
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return mav;
	}
	

    // 1. 아이템 구매

	@ResponseBody
    @RequestMapping(value = "/shop/buy", method = RequestMethod.POST)
    public Map<String, Object> buyItem(HttpServletRequest req, HttpServletResponse resp) throws IOException { // 반환 타입을 Map으로 변경
        Map<String, Object> result = new HashMap<>(); // 결과를 담을 Map
        
        try {
            // 사용자 ID와 아이템 번호
            String id = req.getParameter("id");
            long itemNum = Long.parseLong(req.getParameter("itemNum"));
            
            PointShopDAO dao = new PointShopDAO();
            
            int itemPrice = dao.getItemPrice(itemNum);
            int left_point = dao.left_point(id);

            // 구매 처리
            boolean success = dao.purchaseItem(id, itemNum);

            // 결과 반환
            List<PointShopDTO> inventory = dao.getPlayerInventory(id);
            
            
            if (success) {
            	PointRecordDTO dto = new PointRecordDTO();
            	dto.setPoint(itemPrice);
            	dto.setLeft_point(left_point - itemPrice);
            	dto.setId(id);
            	
            	dao.insertPointRecord(dto);
            	
            	
	            result.put("success", true);
	            result.put("message", "구매가 완료되었습니다 : )");
	            result.put("itemNum", itemNum);
	            result.put("inventory", inventory);
            } else {
            	result.put("message", "포인트가 부족하거나 오류가 발생했습니다.");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "구매처리 중 오류가 발생했습니다.");
        }
        return result;
    }
	
	// 아이템 장착... 쿼리는 알겠는데 이거 어떻게 적용 시킴?
    
}


