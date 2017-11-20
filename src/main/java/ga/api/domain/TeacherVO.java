package ga.api.domain;

public class TeacherVO {
	
	private String progress;
	private String tcode;	
	private String subj;
	private String extra;
	private String pageCode;
	
	public String getProgress() {
		return progress;
	}
	public void setProgress(String progress) {
		this.progress = progress;
		setPageCode();
	}
	public String getTcode() {
		return tcode;
	}
	public void setTcode(String tcode) {
		this.tcode = tcode;
		setPageCode();
	}
	public String getSubj() {
		return subj;
	}
	public void setSubj(String subj) {
		this.subj = subj;
		setPageCode();
	}
	public String getExtra() {
		return extra;
	}
	public void setExtra(String extra) {
		this.extra = extra;
		setPageCode();
	}
	private void setPageCode() {
		this.pageCode = progress + "-" + tcode + "-";
		if(subj != null && !subj.equals("NULL"))this.pageCode = this.pageCode + subj;
		this.pageCode = this.pageCode + "-";
		if(extra != null && !extra.equals("NULL"))this.pageCode = this.pageCode + extra;
	}
	public String getPageCode() {
		return pageCode;
	}

}
