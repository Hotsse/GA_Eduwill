package ga.api.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ga.api.service.TeacherService;

/**
 * Handles requests for the application home page.
 */

@Controller
@RequestMapping("/teacher")
public class TeacherGaApiController {
	
	//Inject MyBatis(for teachers) Service to execute MyBatis logics;
	@Autowired
	private TeacherService teacherService;
	
	//list teachers
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String home(@RequestParam Map<String, Object> param, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		teacherService.ListAll(param, model, request, response); //call service;
		
		return "teacherList";
	}
	
	//showing api information
	@RequestMapping("/analytics")
	public String Analytics(@RequestParam Map<String, Object> param, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		teacherService.readDataFromDB(param, model, request, response); //call service;
		
		return "analytics";
	}
	
}
