package ga.api.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;

import ga.api.domain.MercVO;

//Service to process MyBatis business logics for tbl_merchandise query;
public interface MercService {

	public void ListAll(Map<String, Object> param, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception;	
}
