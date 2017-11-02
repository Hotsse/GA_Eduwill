package ga.api.dao;

import java.util.ArrayList;
import java.util.List;

import ga.api.domain.DailyInformVO;
import ga.api.domain.MercVO;
import ga.common.InformVO;
import ga.common.DailyVO;

//Data Access Object to call MyBatis mapper;
public interface MercDAO {

	public List<MercVO> listAll() throws Exception;
	
	public void updateDailyData(ArrayList<DailyInformVO> list) throws Exception;
	
	public List<InformVO> getSearchData(String seq, String startDate, String endDate);
	
	public List<DailyVO> getDailyData(String seq, String startDate, String endDate);
}
