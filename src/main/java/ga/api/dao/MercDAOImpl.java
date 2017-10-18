package ga.api.dao;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import ga.api.domain.MercVO;

//Implementing MercDAO
@Repository
public class MercDAOImpl implements MercDAO {
	
	//Inject SqlSession to simplify management of SQL connection
	@Inject
    private SqlSession session;
    
    private static final String namespace = "net.eduwill.intern.mapper.MercMapper";

	
	@Override
	public List<MercVO> listAll() throws Exception{
		return session.selectList(namespace+".listAll");
	}

}
