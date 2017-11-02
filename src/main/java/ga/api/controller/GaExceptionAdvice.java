package ga.api.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GaExceptionAdvice {

	@ExceptionHandler(Exception.class)
	public String common(Exception e) {
		return "error_common";
	}
	
}