package ga.api.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;

public interface TeacherGaApiService {
	
	public void readDataFromDB(Map<String, Object> param, ModelMap model, HttpServletRequest request, HttpServletResponse response);
	
	public void updatePeriodData(Map<String, Object> param, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	public void updateYesterdayData(Map<String, Object> param, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception;
	
}
