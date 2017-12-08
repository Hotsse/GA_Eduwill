package ga.api.dao;

import java.util.ArrayList;
import java.util.List;

import ga.api.domain.DailyInformVO;
import ga.api.domain.DailyVO;
import ga.api.domain.InformVO;
import ga.api.domain.TeacherVO;

public interface TeacherDAO {
	
	public List<TeacherVO> listAll() throws Exception;
	
	public void updateAnalyticsData(ArrayList<DailyInformVO> list) throws Exception;
	
	public List<InformVO> getIntegratedData(String delim, String seq, String startDate, String endDate);
	
	public List<DailyVO> getDailyData(String delim, String seq, String startDate, String endDate);
}
