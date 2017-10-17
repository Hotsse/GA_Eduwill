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

@Service
public class MercServiceImpl implements MercService {

	@Autowired
	private MercDAO dao;
	
	@Override
	public void ListAll(Map<String, Object> param, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		model.addAttribute("list",dao.listAll());
		
		return ;
	}
}
