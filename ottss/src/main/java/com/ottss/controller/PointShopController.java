package com.ottss.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import com.ottss.dao.PointShopDAO;
import com.ottss.domain.PointShopDTO;
import com.ottss.mvc.annotation.Controller;
import com.ottss.mvc.annotation.RequestMapping;
import com.ottss.mvc.annotation.RequestMethod;
import com.ottss.mvc.view.ModelAndView;
import com.ottss.util.MyUtil;
import com.ottss.util.MyUtilBootstrap;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
			String articleUrl;
			
			if (kwd.length() != 0) {
				query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
			}
			listUrl = cp + "/shop/shop?" + query;
			articleUrl = cp + "/shop/shop?page=" + current_page + "&" + query;

			String paging = util.paging(current_page, total_page, listUrl);
			
			// 포워딩 jsp에 전달할 데이터
			mav.addObject("itemList", list);
			mav.addObject("articleUrl", articleUrl);
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
			
			List<PointShopDTO> list;
			if(kwd.length() != 0) {
				list = dao.itemList(offset, size, schType, kwd);
			} else {
				list = dao.itemList(offset, size);
			}
			
			String cp = req.getContextPath();
			String query = "size=" + size;
			String listUrl;
			String articleUrl;
			
			if (kwd.length() != 0) {
				query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
			}
			listUrl = cp + "/shop/inventory?" + query;
			articleUrl = cp + "/shop/inventory?page=" + current_page + "&" + query;

			String paging = util.paging(current_page, total_page, listUrl);
			
			 // 사용자 ID와 아이템 번호
            String id = req.getParameter("id");
            long item_num = Long.parseLong(req.getParameter("item_num"));

            // 구매 처리
            boolean success = dao.purchaseItem(id, item_num);

            // 결과 반환
            List<PointShopDTO> inventory = dao.getPlayerInventory(id);
            if (success) {
	            mav.addObject("success", true);
	            mav.addObject("message", "구매가 완료되었습니다 : )");
	            mav.addObject("inventory", inventory);
            } else {
               mav.addObject("success", false);
               mav.addObject("message", "포인트가 부족하거나 오류가 발생했습니다.");
            }
            
            System.out.println("사용자 ID: " + id);
            System.out.println("아이템 번호: " + item_num);
            System.out.println("인벤토리 데이터: " + inventory);
			
			// 포워딩 jsp에 전달할 데이터
			mav.addObject("itemList", list);
			mav.addObject("articleUrl", articleUrl);
			mav.addObject("dataCount", dataCount);
			mav.addObject("size", size);
			mav.addObject("page", current_page);
			mav.addObject("total_page", total_page);
			mav.addObject("paging", paging);
			mav.addObject("schType", schType);
			mav.addObject("kwd", kwd);
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("success", false);
            mav.addObject("message", "구매처리 중 오류가 발생했습니다.");
		}
		return mav;
	}
	

	 /*
    // 1. 아이템 구매
    @RequestMapping(value = "/shop/buy", method = RequestMethod.POST)
    public ModelAndView buyItem(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	ModelAndView mav = new ModelAndView("shop/buy");
    	
        try {
            // 사용자 ID와 아이템 번호
            String id = req.getParameter("id");
            long itemNum = Long.parseLong(req.getParameter("itemNum"));

            // 구매 처리
            PointShopDAO dao = new PointShopDAO();
            boolean success = dao.purchaseItem(id, itemNum);

            // 결과 반환
            List<PointShopDTO> inventory = dao.getPlayerInventory(id);
            if (success) {
	            mav.addObject("success", true);
	            mav.addObject("message", "구매가 완료되었습니다 : )");
	            mav.addObject("inventory", inventory);
            } else {
               mav.addObject("success", false);
               mav.addObject("message", "포인트가 부족하거나 오류가 발생했습니다.");
            }
            
            System.out.println("사용자 ID: " + id);
            System.out.println("아이템 번호: " + itemNum);
            System.out.println("인벤토리 데이터: " + inventory);
        } catch (Exception e) {
            e.printStackTrace();
            mav.addObject("success", false);
            mav.addObject("message", "구매처리 중 오류가 발생했습니다.");
        }
        return mav;
    }
    */
    
    // 2. 구매한 아이템 인벤토리에서 표시
    
    // 3. 아이템 장착
    
    // 

   
}


