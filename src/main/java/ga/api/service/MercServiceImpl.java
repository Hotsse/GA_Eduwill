package ga.api.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import ga.api.dao.MercDAO;
import ga.api.domain.MercVO;

//Implementing MercService;
@Service
public class MercServiceImpl implements MercService {

	//Inject "Merchandise Data Access Object" to access MyBatis mapper(=query) to process SQL Query for tbl_merchandise;
	@Autowired
	private MercDAO dao;
	
	//get all of information in tbl_merchandise
	@Override
	public void ListAll(Map<String, Object> param, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		model.addAttribute("list",dao.listAll());
		
		return ;
	}
}
