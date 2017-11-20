package ga.api.dao;

import java.util.ArrayList;
import java.util.List;

import ga.common.DailyInformVO;
import ga.common.DailyVO;
import ga.common.InformVO;
import ga.api.domain.TeacherVO;

public interface TeacherDAO {
	
	public List<TeacherVO> listAll() throws Exception;
	
	public void updateAnalyticsData(ArrayList<DailyInformVO> list) throws Exception;
	
	public List<InformVO> getIntegratedData(String delim, String seq, String startDate, String endDate);
	
	public List<DailyVO> getDailyData(String delim, String seq, String startDate, String endDate);
}
