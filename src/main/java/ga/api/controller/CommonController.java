package ga.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Handles requests for the application home page.
 */
@Controller
public class CommonController {
	
	//welcome
	@RequestMapping("/")
	public String home() throws Exception {
		return "redirect:/goods/";
	}
	
	@RequestMapping(value = "/excel")
	public String excel() {
		
		return "excel";
	}
}
