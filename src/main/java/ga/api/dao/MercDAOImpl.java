package ga.api.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import ga.api.domain.DailyInformVO;
import ga.api.domain.MercVO;
import ga.common.InformVO;
import ga.common.DailyVO;

//Implementing MercDAO
@Repository
public class MercDAOImpl implements MercDAO {
	
	//Inject SqlSession to simplify management of SQL connection
	@Inject
    private SqlSession session;
    
    private static final String namespace = "net.eduwill.intern.mapper.MercMapper";

	
	@Override
	public List<MercVO> listAll() throws Exception{
		return session.selectList(namespace+".listAll");
	}
	
	@Override
	public void updateDailyData(ArrayList<DailyInformVO> list) throws Exception{
		
		//중복 데이터 삭제
		for(DailyInformVO vo : list) {
			session.delete(namespace+".deleteDailyData", vo);
		}
		//새 데이터 생성
		for(DailyInformVO vo : list) {
			//if(vo.getPagePath().length() >= 100)vo.setPagePath(vo.getPagePath().substring(0,100));
			session.insert(namespace+".insertDailyData", vo);
		}
	}
	
	@Override
	public List<InformVO> getSearchData(String seq, String startDate, String endDate){
		Map<String, Object> sqlParam = new HashMap<>();
		
		sqlParam.put("seq",  seq);
		sqlParam.put("startDate", startDate);
		sqlParam.put("endDate", endDate);
		
		return session.selectList(namespace+".getSearchData", sqlParam);
	}
	
	@Override
	public List<DailyVO> getDailyData(String seq, String startDate, String endDate){
		Map<String, Object> sqlParam = new HashMap<>();
		
		sqlParam.put("seq",  seq);
		sqlParam.put("startDate", startDate);
		sqlParam.put("endDate", endDate);
		
		return session.selectList(namespace+".getDailyData", sqlParam);
	}

}
