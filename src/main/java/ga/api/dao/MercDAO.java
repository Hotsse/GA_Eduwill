package ga.api.dao;

import java.util.List;
import ga.api.domain.MercVO;

public interface MercDAO {

	public List<MercVO> listAll() throws Exception;	
}
