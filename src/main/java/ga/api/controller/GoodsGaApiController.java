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

import ga.api.service.GoodsGaApiService;
import ga.api.service.GoodsService;

/**
 * Handles requests for the application home page.
 */

@Controller
@RequestMapping("/goods")
public class GoodsGaApiController {
	
	//Inject GA API Service to execute GA API logics;
	@Autowired
	private GoodsGaApiService goodsGaApiService;
	
	//Inject MyBatis(for merchandise) Service to execute MyBatis logics;
	@Autowired
	private GoodsService goodsService;

	//welcome & list merchandises
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String home(@RequestParam Map<String, Object> param, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		goodsService.ListAll(param, model, request, response); //call service;
		
		return "goodsList";
	}
	
	//update daily analytics data
	@RequestMapping(value = "/updatePeriodData", method = RequestMethod.POST)
	public String updatePeriodData(@RequestParam Map<String, Object> param, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		goodsGaApiService.updatePeriodData(param, model, request, response); // call service;
		
		return "redirect:/goods";
	}
	
	//update yesterday analytics data
	@RequestMapping(value = "/updateYesterdayData", method = RequestMethod.GET)
	public String updateYesterdayData(@RequestParam Map<String, Object> param, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		goodsGaApiService.updateYesterdayData(param, model, request, response); // call service;
		
		return "redirect:/goods";
	}
	
	//showing api information
	@RequestMapping("/analytics")
	public String main(@RequestParam Map<String, Object> param, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		goodsGaApiService.readDataFromDB(param, model, request, response); //call service;
		
		return "analytics";
	}
}
