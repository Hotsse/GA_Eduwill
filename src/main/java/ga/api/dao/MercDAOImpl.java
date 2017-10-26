package ga.api.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import ga.api.domain.DailyInformVO;
import ga.api.domain.MercVO;
import ga.common.InformVO;

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
	
	@Override
	public void updateDailyData(ArrayList<InformVO> list) throws Exception{
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String today = dateFormat.format(date);
		
		for(InformVO vo : list) {
			DailyInformVO dvo = new DailyInformVO();
			dvo.setInformVO(vo);
			dvo.setuDate(today);
			session.insert(namespace+".updateDailyData", dvo);
		}
	}

}
