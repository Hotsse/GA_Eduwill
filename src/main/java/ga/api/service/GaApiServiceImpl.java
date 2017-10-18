package ga.api.service;

import java.util.ArrayList;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import ga.common.GaApiRunner;
import ga.common.InformDao;
import ga.common.PageViewVO;

@Service
public class GaApiServiceImpl implements GaApiService {
	
	@Autowired
	private GaApiRunner gaApiRunner;
	
	@Override
	public void runApi(Map<String, Object> param, ModelMap model, HttpServletRequest request, HttpServletResponse response){
		
		String seq = (String) param.get("seq");
		String startDate = (String) param.get("startDate");
		String endDate = (String) param.get("endDate");
		
		System.out.println("==========================================");
		System.out.println("seq			: " + seq);
		System.out.println("startDate	: " + startDate);
		System.out.println("endDate		: " + endDate);
		System.out.println("==========================================");
		
		model.addAttribute("seq", seq);
		model.addAttribute("result", gaApiRunner.print(seq, startDate, endDate));
		
		ArrayList<PageViewVO> list = gaApiRunner.getPageViewsforDay(seq, startDate, endDate);
		
		if(list != null) {
			System.out.println("일별 페이지뷰 조회 통계 결과");
			for(PageViewVO vo : list) {
				System.out.println("=================================");
				System.out.println("Date : " + vo.getmDate());
				System.out.println("PageView : " + vo.getmPageView());
				System.out.println("=================================");
			}
		}
		
		model.addAttribute("pageViews", list);
		
		return ;
	}
}
