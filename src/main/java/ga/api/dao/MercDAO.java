package ga.api.dao;

import java.util.ArrayList;
import java.util.List;
import ga.api.domain.MercVO;
import ga.common.InformVO;

//Data Access Object to call MyBatis mapper;
public interface MercDAO {

	public List<MercVO> listAll() throws Exception;
	
	public void updateDailyData(ArrayList<InformVO> list) throws Exception;
}
