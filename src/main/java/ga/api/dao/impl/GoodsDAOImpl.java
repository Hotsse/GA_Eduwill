package ga.api.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import ga.api.dao.GoodsDAO;
import ga.api.domain.DailyVO;
import ga.api.domain.GoodsVO;
import ga.api.domain.InformVO;

//Implementing MercDAO
@Repository
public class GoodsDAOImpl implements GoodsDAO {
	
	//Inject SqlSession to simplify management of SQL connection
	@Inject
    private SqlSession session;
    
    private static final String namespace = "net.eduwill.intern.mapper.GoodsMapper";

	
	@Override
	public List<GoodsVO> listAll() throws Exception{
		
		return session.selectList(namespace+".listAll");
	}
	
	@Override
	public List<InformVO> getIntegratedData(String seq, String startDate, String endDate){
		Map<String, Object> sqlParam = new HashMap<>();
		
		sqlParam.put("seq",  seq);
		sqlParam.put("startDate", startDate);
		sqlParam.put("endDate", endDate);
		
		return session.selectList(namespace+".getIntegratedData", sqlParam);
	}
	
	@Override
	public List<DailyVO> getDailyData(String seq, String startDate, String endDate){
		Map<String, Object> sqlParam = new HashMap<>();
		
		sqlParam.put("seq",  seq);
		sqlParam.put("startDate", startDate);
		sqlParam.put("endDate", endDate);
		
		return session.selectList(namespace+".getDailyData", sqlParam);
	}
	
	@Override
	public GoodsVO getPageName(String code) {
		Map<String, Object> sqlParam = new HashMap<>();
		sqlParam.put("code", code);
		
		return session.selectOne(namespace+".getPageName", sqlParam);
	}

}
