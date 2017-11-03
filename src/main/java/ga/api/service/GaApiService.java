package ga.api.service;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;

public interface GaApiService {

	public void runApi(Map<String, Object> param, ModelMap model, HttpServletRequest request, HttpServletResponse response);
	
	public void updateDailyData(Map<String, Object> param, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	public void updateYesterdayData(Map<String, Object> param, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	public void updateOnedayData(Map<String, Object> param, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
