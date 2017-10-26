package ga.api.domain;

import ga.common.InformVO;

public class DailyInformVO extends InformVO {
	private String uDate;

	public String getuDate() {
		return uDate;
	}

	public void setuDate(String uDate) {
		this.uDate = uDate;
	}
	
	public void setInformVO(InformVO vo) {
		setPageCode(vo.getPageCode());
		setPageviews(vo.getPageviews());
		setUniquePageviews(vo.getUniquePageviews());
		setSessions(vo.getSessions());
		setExitRate(vo.getExitRate());
		setBounceRate(vo.getBounceRate());
		setTotalEvents(vo.getTotalEvents());
	}
}
