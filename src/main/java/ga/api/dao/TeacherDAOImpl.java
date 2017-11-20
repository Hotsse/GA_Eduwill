package ga.api.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import ga.common.DailyInformVO;
import ga.api.domain.TeacherVO;
import ga.common.InformVO;
import ga.common.DailyVO;

//Implementing MercDAO
@Repository
public class TeacherDAOImpl implements TeacherDAO {
	
	//Inject SqlSession to simplify management of SQL connection
	@Inject
    private SqlSession session;
    
    private static final String namespace = "net.eduwill.intern.mapper.TeacherMapper";

	
	@Override
	public List<TeacherVO> listAll() throws Exception{
		return session.selectList(namespace+".listAll");
	}
	
	@Override
	public void updateAnalyticsData(ArrayList<DailyInformVO> list) throws Exception{
		
		//중복 데이터 삭제
		for(DailyInformVO vo : list) {
			session.delete(namespace+".deleteDailyData", vo);
		}
		//새 데이터 생성
		for(DailyInformVO vo : list) {
			session.insert(namespace+".insertDailyData", vo);
		}
	}
	
	@Override
	public List<InformVO> getIntegratedData(String delim, String seq, String startDate, String endDate){
		Map<String, Object> sqlParam = new HashMap<>();
		
		sqlParam.put("seq",  seq);
		sqlParam.put("startDate", startDate);
		sqlParam.put("endDate", endDate);
		sqlParam.put("delim", delim);

		return session.selectList(namespace+".getIntegratedData", sqlParam);
	}
	
	@Override
	public List<DailyVO> getDailyData(String delim, String seq, String startDate, String endDate){
		Map<String, Object> sqlParam = new HashMap<>();
		
		sqlParam.put("seq",  seq);
		sqlParam.put("startDate", startDate);
		sqlParam.put("endDate", endDate);
		sqlParam.put("delim", delim);
		
		return session.selectList(namespace+".getDailyData", sqlParam);
	}

}
