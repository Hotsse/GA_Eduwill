package net.eduwill.intern;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ga.api.dao.MercDAO;
import ga.api.domain.MercVO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= {"file:src/main/webapp/WEB-INF/spring/**/root-context.xml"})
public class MercDAOTest {
      
      @Inject
      private MercDAO dao;
      
      @Test
      public void test() throws Exception{
    	  
    	  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	  Date date = new Date();
    	  System.out.println(dateFormat.format(date));
    	  
    	  String url = "https://www.eduwill.net/Common/Product/goods/auto/index.asp?masterSeq=NDcw";
    	  String code = null;
    	  
    	  String []arr = url.split("\\?");
    	  if(arr.length > 1) {
    		  String tmp = arr[1];
    		  arr = tmp.split("=");
    		  if(arr.length > 1) {
    			  code = arr[1];
    		  }
    	  }
    	  
    	  if(code == null)System.out.println("null");
    	  else System.out.println(code);
    	  
    	  /*
    	  List<MercVO> list = dao.listAll();
    	  
    	  for(MercVO vo : list) {
    		  System.out.println("readMerc = ");
    		  System.out.println("idx : " + vo.getIdx());
              System.out.println("name : " + vo.getName());
              System.out.println("code : " + vo.getCode());
    	  }
    	  */
    	  
      }
      
      private String parseCodeInURL(String url) {
    	  String code = null;
    	  
    	  String []arr = url.split("\\?");
    	  if(arr.length > 1) {
    		  String tmp = arr[1];
    		  arr = tmp.split("=");
    		  if(arr.length > 1) {
    			  code = arr[1];
    		  }
    	  }
    	  
    	  return code;
      }
}
