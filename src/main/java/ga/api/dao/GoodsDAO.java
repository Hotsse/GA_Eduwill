package ga.api.dao;

import java.util.ArrayList;
import java.util.List;

import ga.api.domain.DailyInformVO;
import ga.api.domain.DailyVO;
import ga.api.domain.GoodsVO;
import ga.api.domain.InformVO;

//Data Access Object to call MyBatis mapper;
public interface GoodsDAO {

	public List<GoodsVO> listAll() throws Exception;
	
	public void updateDailyData(ArrayList<DailyInformVO> list) throws Exception;
	
	public List<InformVO> getIntegratedData(String seq, String startDate, String endDate);
	
	public List<DailyVO> getDailyData(String seq, String startDate, String endDate);
	
	public GoodsVO getPageName(String code);
}
