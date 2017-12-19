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

	/**
	 * 상품 자동화 페이지의 전체 목록을 가져오는 메소드
	 * @return {@code List<GoodsVO>} 상품 자동화 페이지 정보 리스트
	 * @throws Exception
	 * */
	@Override
	public List<GoodsVO> listAll() throws Exception{
		
		return session.selectList(namespace+".listAll");
	}
	
	/**
	 * 수집 기간 내의 정보를 합산한 결과를 반환하는 메소드
	 * @param seq 상품 자동화 페이지 마스터 시퀀스
	 * @param startDate 검색 시작일
	 * @param endDate 검색 종료일
	 * @return {@code List<InformVO>} GA 데이터의 리스트
	 * */
	@Override
	public List<InformVO> getIntegratedData(String seq, String startDate, String endDate){
		Map<String, Object> sqlParam = new HashMap<>();
		
		sqlParam.put("seq",  seq);
		sqlParam.put("startDate", startDate);
		sqlParam.put("endDate", endDate);
		
		return session.selectList(namespace+".getIntegratedData", sqlParam);
	}
	
	/**
	 * 수집 기간 내의 정보의 일일 단위 결과를 반환하는 메소드
	 * @param seq 상품 자동화 페이지 마스터 시퀀스
	 * @param startDate 검색 시작일
	 * @param endDate 검색 종료일
	 * @return {@code List<DailyVO>} GA 데이터의 리스트
	 * */
	@Override
	public List<DailyVO> getDailyData(String seq, String startDate, String endDate){
		Map<String, Object> sqlParam = new HashMap<>();
		
		sqlParam.put("seq",  seq);
		sqlParam.put("startDate", startDate);
		sqlParam.put("endDate", endDate);
		
		return session.selectList(namespace+".getDailyData", sqlParam);
	}
	
	/**
	 * 코드를 통해 페이지 타이틀을 얻는 메소드
	 * @param seq 상품 자동화 페이지 마스터 시퀀스
	 * @return GoodsVO 상품 자동화 페이지 정보 객체
	 * */
	@Override
	public GoodsVO getPageName(String code) {
		Map<String, Object> sqlParam = new HashMap<>();
		sqlParam.put("code", code);
		
		return session.selectOne(namespace+".getPageName", sqlParam);
	}

}
