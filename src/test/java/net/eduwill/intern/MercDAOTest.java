package net.eduwill.intern;

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
    	  
    	  
    	  List<MercVO> list = dao.listAll();
    	  
    	  for(MercVO vo : list) {
    		  System.out.println("readMerc = ");
    		  System.out.println("idx : " + vo.getIdx());
              System.out.println("name : " + vo.getName());
              System.out.println("code : " + vo.getCode());
    	  }
    	  
      }
}
