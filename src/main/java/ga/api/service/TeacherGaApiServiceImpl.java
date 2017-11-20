package ga.api.service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import ga.api.dao.GoodsDAO;
import ga.api.dao.TeacherDAO;
import ga.api.domain.GoodsVO;
import ga.api.domain.TeacherVO;
import ga.common.InformVO;
import ga.common.runner.TeacherGaApiRunner;
import ga.common.DailyVO;

//Implementating GaApiService
@Service
public class TeacherGaApiServiceImpl implements TeacherGaApiService {
	
	@Autowired
	private TeacherGaApiRunner gaApiRunner;
	
	//Inject "Merchandise Data Access Object" to access MyBatis mapper(=query) to process SQL Query for tbl_merchandise;
	@Autowired
	private TeacherDAO dao;
	
	@Override
	public void readDataFromDB(Map<String, Object> param, ModelMap model, HttpServletRequest request, HttpServletResponse response){
		
		//변수 선언 및 초기화---------------------------------------
		//파라미터 정보(코드, 기간)
		String seq = (String) param.get("seq");
		String startDate = (String) param.get("startDate");
		String endDate = (String) param.get("endDate");
		String delim = (String) param.get("delim");
		
		//기본 날짜 범위
		if(startDate == null || endDate == null) {
			SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date todayDate = new Date();
			String today = transFormat.format(todayDate);
			String lastmonth = transFormat.format(getMonthAgo(todayDate));
			startDate = lastmonth;
			endDate = today;
		}
		
		//한달 전, 일년 전 기간
		String startDate_lastmonth = null, endDate_lastmonth = null, startDate_lastyear = null, endDate_lastyear = null;
		//통합 데이터(표)
		List<InformVO> resultList = null, resultList_lastmonth = null, resultList_lastyear = null;
		//일일 데이터(그래프)
		List<DailyVO> dailyDataList = null, dailyDataList_lastmonth = null, dailyDataList_lastyear = null;
		
		System.out.println("==========================================");
		System.out.println("seq			: " + seq);
		System.out.println("startDate	: " + startDate);
		System.out.println("endDate		: " + endDate);
		System.out.println("==========================================");
		
		//(한달 전, 일년 전) 기간정보 획득----------------------------
		if (startDate != null && endDate != null) {
			try {
				SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date start = transFormat.parse(startDate);
				Date end = transFormat.parse(endDate);
				// 한달 전
				startDate_lastmonth = transFormat.format(getMonthAgo(start));
				endDate_lastmonth = transFormat.format(getMonthAgo(end));
				System.out.println("한달 전 : " + startDate_lastmonth + " ~ " + endDate_lastmonth);

				// 일년 전
				startDate_lastyear = transFormat.format(getYearAgo(start));
				endDate_lastyear = transFormat.format(getYearAgo(end));
				System.out.println("일년 전 : " + startDate_lastyear + " ~ " + endDate_lastyear);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}		
		//데이터 읽어오기---------------------------------------------
		//현재
		resultList = dao.getIntegratedData(delim, seq, startDate, endDate);
		dailyDataList = dao.getDailyData(delim, seq, startDate, endDate);
		//한달 전
		if(startDate_lastmonth != null && endDate_lastmonth != null) {
			resultList_lastmonth = dao.getIntegratedData(delim, seq, startDate_lastmonth, endDate_lastmonth);
			dailyDataList_lastmonth = dao.getDailyData(delim, seq, startDate_lastmonth, endDate_lastmonth);
		}
		//일년 전
		if(startDate_lastyear != null && endDate_lastyear != null) {
			resultList_lastyear = dao.getIntegratedData(delim, seq, startDate_lastyear, endDate_lastyear);
			dailyDataList_lastyear = dao.getDailyData(delim, seq, startDate_lastyear, endDate_lastyear);
		}		
		
		//데이터 시각화(소숫점 제한)-----------------------------------
		//현재
		if(dailyDataList == null || dailyDataList.isEmpty())dailyDataList = null;
		if(resultList.isEmpty())resultList = null;
		else {
			//calculate bounceRate, eventRate
			DecimalFormat format = new DecimalFormat(".###"); // 소숫점 3자리 까지 제한
			
			for(InformVO vo : resultList) {
				if(vo.getSessions() > 0)vo.setBounceRate(Double.parseDouble(format.format(vo.getBounces() / (double)vo.getSessions() * 100)));
				if(vo.getPageviews() > 0)vo.setEventRate(Double.parseDouble(format.format(vo.getTotalEvents() / (double)vo.getPageviews() * 100)));
			}
		}		
		//한달 전
		if(dailyDataList_lastmonth == null || dailyDataList_lastmonth.isEmpty())dailyDataList_lastmonth = null;
		if(resultList_lastmonth == null || resultList_lastmonth.isEmpty())resultList_lastmonth = null;
		else {
			//calculate bounceRate, eventRate
			DecimalFormat format = new DecimalFormat(".###"); // 소숫점 3자리 까지 제한
			for(InformVO vo : resultList_lastmonth) {
				if(vo.getSessions() > 0)vo.setBounceRate(Double.parseDouble(format.format(vo.getBounces() / (double)vo.getSessions() * 100)));
				if(vo.getPageviews() > 0)vo.setEventRate(Double.parseDouble(format.format(vo.getTotalEvents() / (double)vo.getPageviews() * 100)));
			}
			
		}
		//일년 전
		if(dailyDataList_lastyear == null || dailyDataList_lastyear.isEmpty())dailyDataList_lastyear = null;
		if(resultList_lastyear == null || resultList_lastyear.isEmpty())resultList_lastyear = null;
		else {
			//calculate bounceRate, eventRate
			DecimalFormat format = new DecimalFormat(".###"); // 소숫점 3자리 까지 제한
			for(InformVO vo : resultList_lastyear) {
				if(vo.getSessions() > 0)vo.setBounceRate(Double.parseDouble(format.format(vo.getBounces() / (double)vo.getSessions() * 100)));
				if(vo.getPageviews() > 0)vo.setEventRate(Double.parseDouble(format.format(vo.getTotalEvents() / (double)vo.getPageviews() * 100)));
			}
			
		}
		
		//데이터 전송------------------------------------------------------------
		model.addAttribute("seq", seq);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("result", resultList);
		model.addAttribute("result_lastmonth", resultList_lastmonth);
		model.addAttribute("result_lastyear", resultList_lastyear);
		model.addAttribute("dailyDataList", dailyDataList);
		model.addAttribute("dailyDataList_lastmonth", dailyDataList_lastmonth);
		model.addAttribute("dailyDataList_lastyear", dailyDataList_lastyear);
		model.addAttribute("lastmonth", new String(startDate_lastmonth + "~" + endDate_lastmonth));
		model.addAttribute("lastyear", new String(startDate_lastyear + "~" + endDate_lastyear));
	}
	
	@Override
	public void updatePeriodData(Map<String, Object> param, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String startDate = (String) param.get("startDate");
		String endDate = (String) param.get("endDate");
		
		dao.updateAnalyticsData(gaApiRunner.getPeriodData(startDate, endDate));
	}	
	
	@Override
	public void updateYesterdayData(Map<String, Object> param, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		dao.updateAnalyticsData(gaApiRunner.getYesterdayData());
	}
	
	private Date getMonthAgo(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, -1);

		return cal.getTime();
	}

	private Date getYearAgo(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, -1);

		return cal.getTime();
	}
}
