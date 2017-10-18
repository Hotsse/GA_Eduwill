package ga.api.dao;

import java.util.List;
import ga.api.domain.MercVO;

//Data Access Object to call MyBatis mapper;
public interface MercDAO {

	public List<MercVO> listAll() throws Exception;	
}
