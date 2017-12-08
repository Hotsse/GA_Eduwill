package ga.api.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import ga.api.dao.TeacherDAO;
import ga.api.domain.DailyVO;
import ga.api.domain.InformVO;
import ga.api.domain.TeacherVO;

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
