package ga.api.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import ga.api.dao.TeacherDAO;

@Service
public class TeacherServiceImpl implements TeacherService {
	
	//Inject "Professor Data Access Object" to access MyBatis mapper(=query) to process SQL Query for tbl_prof_master;
	@Autowired
	private TeacherDAO dao;
	
	//get all of information in tbl_merchandise
	@Override
	public void ListAll(Map<String, Object> param, ModelMap model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		model.addAttribute("list", dao.listAll());

		return;
	}
}
