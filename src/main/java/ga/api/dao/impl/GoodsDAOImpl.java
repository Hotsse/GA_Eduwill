package ga.api.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import ga.api.dao.GoodsDAO;
import ga.api.domain.DailyInformVO;
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
	public void updateDailyData(ArrayList<DailyInformVO> list) throws Exception{
		
		//중복 제거 쿼리 단축 알고리즘
		ArrayList<DailyInformVO> tmp = new ArrayList<DailyInformVO>();
		tmp.addAll(list);
		int size = tmp.size();
		for (int i = 0; i < size; i++) {
			String firstVal = tmp.get(i).getuDate();
			for (int j = i + 1; j < size; j++) {
				String secondVal = tmp.get(j).getuDate();
				if (firstVal.equals(secondVal)) {
					tmp.remove(j);
					size--;
					i = 0;
					j--;
				}
			}
		}
		
		//중복 데이터 삭제
		for(DailyInformVO vo : tmp) {
			System.out.println("deleteuDate : " + vo.getuDate());
			session.delete(namespace+".deleteDailyData", vo);
		}
		//새 데이터 생성
		for(DailyInformVO vo : list) {
			System.out.println("insertuDate : " + vo.getuDate());
			session.insert(namespace+".insertDailyData", vo);
		}
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
