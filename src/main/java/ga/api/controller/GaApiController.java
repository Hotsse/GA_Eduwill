package ga.api.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ga.api.service.GaApiService;
import ga.api.service.MercService;
import ga.common.GaApiRunner;

/**
 * Handles requests for the application home page.
 */
@Controller
public class GaApiController {
	
	//Inject GA API Service to execute GA API logics;
	@Autowired
	private GaApiService gaApiService;
	
	//Inject MyBatis(for merchandise) Service to execute MyBatis logics;
	@Autowired
	private MercService mercService;

	//welcome & list merchandises
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(@RequestParam Map<String, Object> param, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		mercService.ListAll(param, model, request, response); //call service;
		
		return "home";
	}
	
	//showing api information
	@RequestMapping("/analytics")
	public String main(@RequestParam Map<String, Object> param, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		gaApiService.runApi(param, model, request, response); //call service;
		
		return "runApi";
	}
	
}
