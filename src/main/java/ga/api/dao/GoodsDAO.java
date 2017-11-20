package ga.api.dao;

import java.util.ArrayList;
import java.util.List;

import ga.common.DailyInformVO;
import ga.api.domain.GoodsVO;
import ga.common.InformVO;
import ga.common.DailyVO;

//Data Access Object to call MyBatis mapper;
public interface GoodsDAO {

	public List<GoodsVO> listAll() throws Exception;
	
	public void updateDailyData(ArrayList<DailyInformVO> list) throws Exception;
	
	public List<InformVO> getIntegratedData(String seq, String startDate, String endDate);
	
	public List<DailyVO> getDailyData(String seq, String startDate, String endDate);
	
	public GoodsVO getPageName(String code);
}
