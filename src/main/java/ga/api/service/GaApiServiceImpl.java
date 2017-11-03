package ga.api.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import ga.api.dao.MercDAO;
import ga.api.domain.DailyInformVO;
import ga.api.domain.MercVO;
import ga.common.GaApiRunner;
import ga.common.InformVO;
import ga.common.DailyVO;

//Implementating GaApiService
@Service
public class GaApiServiceImpl implements GaApiService {
	
	@Autowired
	private GaApiRunner gaApiRunner;
	
	//Inject "Merchandise Data Access Object" to access MyBatis mapper(=query) to process SQL Query for tbl_merchandise;
	@Autowired
	private MercDAO dao;
	
	@Override
	public void runApi(Map<String, Object> param, ModelMap model, HttpServletRequest request, HttpServletResponse response){
		
		String seq = (String) param.get("seq");
		String startDate = (String) param.get("startDate");
		String endDate = (String) param.get("endDate");
		
		String lastStartDate = parseLastYear(startDate);
		String lastEndDate = parseLastYear(endDate);
		
		System.out.println("==========================================");
		System.out.println("seq			: " + seq);
		System.out.println("startDate	: " + startDate);
		System.out.println("endDate		: " + endDate);
		System.out.println("==========================================");
		
		
		List<InformVO> resultList = dao.getSearchData(seq, startDate, endDate);
		List<InformVO> lastResultList = null;
		if(lastStartDate != null & lastEndDate != null)lastResultList = dao.getSearchData(seq, lastStartDate, lastEndDate);
		
		List<DailyVO> dailyDataList = dao.getDailyData(seq, startDate, endDate);
		
		
		if(resultList.isEmpty())resultList = null;
		else {
			//calculate bounceRate, eventRate
			DecimalFormat format = new DecimalFormat(".###"); // 소숫점 3자리 까지 제한
			
			for(InformVO vo : resultList) {
				vo.setBounceRate(Double.parseDouble(format.format(vo.getBounces() / (double)vo.getSessions() * 100)));
				vo.setEventRate(Double.parseDouble(format.format(vo.getTotalEvents() / (double)vo.getPageviews() * 100)));
			}
		}
		
		if(lastResultList == null || lastResultList.isEmpty())lastResultList = null;
		else {
			//calculate bounceRate, eventRate
			DecimalFormat format = new DecimalFormat(".###"); // 소숫점 3자리 까지 제한
			for(InformVO vo : lastResultList) {
				vo.setBounceRate(Double.parseDouble(format.format(vo.getBounces() / (double)vo.getSessions() * 100)));
				vo.setEventRate(Double.parseDouble(format.format(vo.getTotalEvents() / (double)vo.getPageviews() * 100)));
			}
			
		}
		
		if(dailyDataList.isEmpty())dailyDataList = null;
		
		model.addAttribute("seq", seq);
		model.addAttribute("result", resultList);
		model.addAttribute("lastYearResult", lastResultList);
		model.addAttribute("dailyDataList", dailyDataList);
	}
	
	@Override
	public void updateDailyData(Map<String, Object> param, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String startDate = (String) param.get("startDate");
		String endDate = (String) param.get("endDate");
		
		List<MercVO> codeList = replacePathToCode(dao.listAll());
		dao.updateDailyData(gaApiRunner.getDailyData(codeList, startDate, endDate));
	}
	
	@Override
	public void updateYesterdayData(Map<String, Object> param, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		dao.updateDailyData(gaApiRunner.getYesterdayData());
	}
	
	@Override
	public void updateOnedayData(Map<String, Object> param, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String date = (String) param.get("date");		
		dao.updateDailyData(gaApiRunner.getOnedayData(date));
	}
	
	private String parseLastYear(String date) {
		if(date == null)return null;
		
		String year = date.substring(0,4);
		String result = date.replace(year, String.valueOf(Integer.valueOf(year)-1));
		
		return result;
	}
	
	private List<MercVO> replacePathToCode(List<MercVO> oldList) {

		List<MercVO> newList = new ArrayList<MercVO>();
		
		for(MercVO vo : oldList) {
			String tmpCode = parseCodeInURL(vo.getCode());
			vo.setCode(tmpCode);
			newList.add(vo);
		}
		
		return newList;
	}
	
	private String parseCodeInURL(String url) {
		  String code = null;
		  
		  String []arr = url.split("\\?");
		  if(arr.length > 1) {
			  String tmp = arr[1];
			  arr = tmp.split("&");
			  
			  int idx=0;
			  if(arr.length > 1) {
				  for(idx=0; idx < arr.length; idx++) {
					  if(arr[idx].contains("masterSeq"))break;
				  }
			  }
			  tmp = arr[idx];
			  arr = tmp.split("=");
			  code = arr[1];
			  while(code.length()%4 != 0)code = code.concat("=");
		  }
		  
		  return code;
	}
}
