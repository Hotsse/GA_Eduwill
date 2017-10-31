package ga.common;

public class InformVO {
	private String pageCode;
	private String pagePath;
	private int pageviews;
	private int uniquePageviews;
	private int sessions;
	private double exitRate;
	private int bounces;
	private double bounceRate;
	private int totalEvents;
	private double eventRate;
	
	public String getPagePath() {
		return pagePath;
	}
	public void setPagePath(String pagePath) {
		this.pagePath = pagePath;
	}
	public double getEventRate() {
		return eventRate;
	}
	public void setEventRate(double eventRate) {
		this.eventRate = eventRate;
	}
	public int getBounces() {
		return bounces;
	}
	public void setBounces(int bounces) {
		this.bounces = bounces;
	}
	public double getBounceRate() {
		return bounceRate;
	}
	public void setBounceRate(double bounceRate) {
		this.bounceRate = bounceRate;
	}
	public String getPageCode() {
		return pageCode;
	}
	public void setPageCode(String pageCode) {
		this.pageCode = pageCode;
	}
	public int getPageviews() {
		return pageviews;
	}
	public void setPageviews(int pageviews) {
		this.pageviews = pageviews;
	}
	public int getUniquePageviews() {
		return uniquePageviews;
	}
	public void setUniquePageviews(int uniquePageviews) {
		this.uniquePageviews = uniquePageviews;
	}
	public int getSessions() {
		return sessions;
	}
	public void setSessions(int sessions) {
		this.sessions = sessions;
	}
	public double getExitRate() {
		return exitRate;
	}
	public void setExitRate(double exitRate) {
		this.exitRate = exitRate;
	}
	public int getTotalEvents() {
		return totalEvents;
	}
	public void setTotalEvents(int totalEvents) {
		this.totalEvents = totalEvents;
	}
	
}
