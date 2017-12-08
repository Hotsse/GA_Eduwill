package ga.api.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;

public interface TeacherService {
	
	public void ListAll(Map<String, Object> param, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception;	
	
	public void readDataFromDB(Map<String, Object> param, ModelMap model, HttpServletRequest request, HttpServletResponse response);
}
