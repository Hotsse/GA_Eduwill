package ga.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import ga.api.dao.GoodsDAO;
import ga.api.domain.GoodsVO;

//Implementing MercService;
@Service
public class GoodsServiceImpl implements GoodsService {

	//Inject "Merchandise Data Access Object" to access MyBatis mapper(=query) to process SQL Query for tbl_merchandise;
	@Autowired
	private GoodsDAO dao;
	
	//get all of information in tbl_merchandise
	@Override
	public void ListAll(Map<String, Object> param, ModelMap model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		List<GoodsVO> list = replacePathToCode(dao.listAll());
		
		model.addAttribute("list", list);

		return;
	}

	private List<GoodsVO> replacePathToCode(List<GoodsVO> oldList) {

		List<GoodsVO> newList = new ArrayList<GoodsVO>();
		
		for(GoodsVO vo : oldList) {
			String tmpCode = parseCodeInURL(vo.getCode());
			vo.setCode(tmpCode);
			newList.add(vo);
		}
		
		return newList;
	}
	
	private String parseCodeInURL(String url) {
		  String code = null;
		  
		  String []arr = url.split("\\?");
		  if(arr.length > 1) {
			  String tmp = arr[1];
			  arr = tmp.split("&");
			  
			  int idx=0;
			  if(arr.length > 1) {
				  for(idx=0; idx < arr.length; idx++) {
					  if(arr[idx].contains("masterSeq"))break;
				  }
			  }
			  tmp = arr[idx];
			  arr = tmp.split("=");
			  code = arr[1];
			  while(code.length()%4 != 0)code = code.concat("=");
		  }
		  
		  return code;
	}
}


