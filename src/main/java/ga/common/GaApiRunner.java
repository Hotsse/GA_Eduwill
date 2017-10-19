package ga.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.AnalyticsReportingScopes;
import com.google.api.services.analyticsreporting.v4.model.ColumnHeader;
import com.google.api.services.analyticsreporting.v4.model.DateRange;
import com.google.api.services.analyticsreporting.v4.model.DateRangeValues;
import com.google.api.services.analyticsreporting.v4.model.Dimension;
import com.google.api.services.analyticsreporting.v4.model.DimensionFilter;
import com.google.api.services.analyticsreporting.v4.model.DimensionFilterClause;
import com.google.api.services.analyticsreporting.v4.model.GetReportsRequest;
import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import com.google.api.services.analyticsreporting.v4.model.Metric;
import com.google.api.services.analyticsreporting.v4.model.MetricHeaderEntry;
import com.google.api.services.analyticsreporting.v4.model.Report;
import com.google.api.services.analyticsreporting.v4.model.ReportRequest;
import com.google.api.services.analyticsreporting.v4.model.ReportRow;
import com.google.api.services.analyticsreporting.v4.model.SegmentDimensionFilter;
import com.google.api.services.analyticsreporting.v4.model.SegmentFilterClause;

public class GaApiRunner {
	
	
	//애널리틱스 인증 정보
	private final String APPLICATION_NAME = "Eduwill_Internship_GA_Project";
	private final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance(); // GsonFactory.getDefaultInstance()
	private final String KEY_FILE_LOCATION = "client_secrets_edw.json"; // /resources/secret_key 에 들어가는 인증 정보
	
	private final String VIEW_ID = "66471933"; // View ID 는 ga-dev-tools.appsport.com/account-explorer/

	public ArrayList<InformVO> print(String seq, String startDate, String endDate) {		
		
		ArrayList<InformVO> tmp = null;
		
		try {
			//서비스를 초기화 시키고
			AnalyticsReporting service = initializeAnalyticsReporting();

			//날짜 입력이 올바른 상태에서만 실행
			if(startDate != null && endDate != null) {
				
				//쿼리를 실행 시켜서
				GetReportsResponse response = getReport(service, seq, startDate, endDate);
			
				//결과값을 파싱하여 뿌린다
				tmp = printResponse(response);
			} 
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return tmp;		
	}
	
	public ArrayList<PageViewVO> getPageViewsforDay(String seq, String startDate, String endDate){
		
		ArrayList<PageViewVO> tmp = null;
		
		try {
			//서비스를 초기화 시키고
			AnalyticsReporting service = initializeAnalyticsReporting();

			//날짜 입력이 올바른 상태에서만 실행
			if(startDate != null && endDate != null) {
			
				//결과값을 파싱하여 뿌린다
				tmp = getPageViewinRange(service, seq, startDate, endDate);
			} 
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return tmp;
	}
	/**
	   * 애널리틱스 보고서 API V4 서비스 객체를 초기화 시키는 메소드.
	   *
	   * @return An authorized Analytics Reporting API V4 service object.
	   * @throws IOException
	   * @throws GeneralSecurityException
	   
	   */
	
	private AnalyticsReporting initializeAnalyticsReporting() throws GeneralSecurityException, IOException {
		
		Resource resource = new ClassPathResource("/secret_key/client_secrets_edw.json"); 
		FileInputStream fis = new FileInputStream(resource.getFile());
		
		//HTTP 통신을 초기화 시키고 인증 파일(.json)을 통해서 서비스 객체를 구성하여 반환한다.
		HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		GoogleCredential credential = GoogleCredential
				.fromStream(fis)
				.createScoped(AnalyticsReportingScopes.all());
		
		// Construct the Analytics Reporting service object.
		return new AnalyticsReporting.Builder(httpTransport, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build();
	}	
	
	
	/**
	   * 애널리틱스 보고서 API V4 를 위한 쿼리를 Send 하는 메소드.
	   *
	   * @param service An authorized Analytics Reporting API V4 service object.
	   * @return GetReportResponse The Analytics Reporting API V4 response.
	   * @throws IOException
	*/
	private GetReportsResponse getReport(AnalyticsReporting service, String seq, String startDate, String endDate) throws IOException {
		
	    // 조사할 기간의 범위를 설정한다
	    DateRange dateRange = new DateRange();
	    dateRange.setStartDate(startDate);
	    dateRange.setEndDate(endDate);

	    /* 측정 항목과 측정기준의 객체를 만들고 내용물을 설정한다
	     * 내용물에 대한 쿼리 레퍼런스는 아래의 링크를 참고하자
	     * https://developers.google.com/analytics/devguides/reporting/core/dimsmets
	     */
	    
	    //상품 페이지 보고서-----
	    Metric pageviews = new Metric()
	        .setExpression("ga:pageviews")
	        .setAlias("pageviews");
	    
	    Metric uniqueviews = new Metric()
		        .setExpression("ga:uniquePageviews")
		        .setAlias("uniquePageviews");
	    
	    Metric sessions = new Metric()
		        .setExpression("ga:sessions")
		        .setAlias("sessions");
	    
	    Metric exitRate = new Metric()
		        .setExpression("ga:exitRate")
		        .setAlias("exitRate");
	    
	    Metric bounceRate = new Metric()
		        .setExpression("ga:bounceRate")
		        .setAlias("bounceRate");
	    
	    //디멘션 설정
	    Dimension pageTitle = new Dimension().setName("ga:pagePath");
	    
	    System.out.println("필터 적용 : ?masterSeq=" + seq);
	    String resultSeq = "masterSeq="+seq; // "masterSeq=" 가 포함되어 있어야함
	    
	    //측정 기준 필터 적용~
	    DimensionFilter nameFilter = new DimensionFilter()
	    		.setDimensionName("ga:pagePath") // {{pagePath}} 에
	    		.setExpressions(Arrays.asList(resultSeq));
	    
	    DimensionFilterClause dFilterClause = new DimensionFilterClause()
	    		.setFilters(Arrays.asList(nameFilter));
	   

	    // 위의 항목과 기준을 구글로 Request 하기 위한 객체를 만든다
	    ReportRequest request = new ReportRequest()
	        .setViewId(VIEW_ID)
	        .setDateRanges(Arrays.asList(dateRange))
	        .setMetrics(Arrays.asList(pageviews,uniqueviews,sessions,exitRate,bounceRate))
	        .setDimensions(Arrays.asList(pageTitle))
	        .setDimensionFilterClauses(Arrays.asList(dFilterClause));
	    
	    //이벤트 보고서-----------------
	    Metric totalEvents = new Metric()
		        .setExpression("ga:totalEvents")
		        .setAlias("totalEvents");
	    
	    //디멘션 설정
	    Dimension eventCategory = new Dimension().setName("ga:eventCategory");
	    Dimension eventAction = new Dimension().setName("ga:eventAction");
	    
	    //측정 기준 필터 적용~
	    DimensionFilter actionFilter = new DimensionFilter()
	    		.setDimensionName("ga:eventAction") // {{eventAction}} 에
	    		.setExpressions(Arrays.asList(resultSeq));
	    
	    DimensionFilterClause actionFilterClause = new DimensionFilterClause()
	    		.setFilters(Arrays.asList(actionFilter));
	   

	    // 위의 항목과 기준을 구글로 Request 하기 위한 객체를 만든다
	    ReportRequest eventRequest = new ReportRequest()
	        .setViewId(VIEW_ID)
	        .setDateRanges(Arrays.asList(dateRange))
	        .setMetrics(Arrays.asList(totalEvents))
	        .setDimensions(Arrays.asList(eventCategory, eventAction))
	        .setDimensionFilterClauses(Arrays.asList(actionFilterClause));
	    
	    
	    //리스트에 모두 싣습니다~
	    ArrayList<ReportRequest> requests = new ArrayList<ReportRequest>();
	    requests.add(request);
	    requests.add(eventRequest);
	    
	    // 아마 Request 한 것에 대한 결과를 Receive 하는 객체를 만드는 듯 하다
	    GetReportsRequest getReport = new GetReportsRequest()
	        .setReportRequests(requests);

	    // 상기의 리스트를 GA 서버로 Request 하여 결과 값을 Response 하는 메소드(Execute)
	    GetReportsResponse response = service.reports().batchGet(getReport).execute();

	    // 결과를 반환
	    return response;
	  }
	
	/**
	   * 애널리틱스로부터 response 한 데이터를 파싱하고 출력하는 메소드.
	   *
	   * @param response An Analytics Reporting API V4 response.
	   */
	private ArrayList<InformVO> printResponse(GetReportsResponse response) {
		
		System.out.println("성공적으로 실행했습니다.");
		  
		//하나의 response 에는 다수의 report 가 포함되어 있다.
		//List를 foreach를 통해 각 객체마다 접근한다.
		
		ArrayList<InformVO> list = new ArrayList<InformVO>();
		
		int cnt = 0;
		for (Report report: response.getReports()) {			
						
			//하나의 report 에는 하나의 header 밖에 없다.
			ColumnHeader header = report.getColumnHeader();			
			
			//헤더와 측정 기준, 항목 사이에 하나의 분류가 더 있는 모양이다.
			List<String> dimensionHeaders = header.getDimensions();
			List<MetricHeaderEntry> metricHeaders = header.getMetricHeader().getMetricHeaderEntries();
			List<ReportRow> rows = report.getData().getRows();
			
			cnt++;
			if(cnt == 1) { // PagePath
				
				if (rows == null) {
					System.out.println("No data found for " + VIEW_ID);
					return null;
				}
				
				for (ReportRow row: rows) {
					
					InformVO entity = new InformVO();
					
					List<String> dimensions = row.getDimensions();
					List<DateRangeValues> metrics = row.getMetrics();
					
					for (int i = 0; i < dimensionHeaders.size() && i < dimensions.size(); i++) {
						System.out.println(dimensionHeaders.get(i) + ": " + dimensions.get(i));
						if(dimensionHeaders.get(i).equals("ga:pagePath"))entity.setPagePath(dimensions.get(i));
					}
					
					for (int j = 0; j < metrics.size(); j++) {
						System.out.print("Date Range (" + j + "): ");
						DateRangeValues values = metrics.get(j);
						
						
						for (int k = 0; k < values.getValues().size() && k < metricHeaders.size(); k++) {
							
							if(metricHeaders.get(k).getName().equals("pageviews"))
								entity.setPageviews(Integer.parseInt(values.getValues().get(k)));
							else if(metricHeaders.get(k).getName().equals("uniquePageviews"))
								entity.setUniquePageviews(Integer.parseInt(values.getValues().get(k)));
							else if(metricHeaders.get(k).getName().equals("sessions"))
								entity.setSessions(Integer.parseInt(values.getValues().get(k)));
							else if(metricHeaders.get(k).getName().equals("exitRate"))
								entity.setExitRate(Double.parseDouble(values.getValues().get(k)));
							else if(metricHeaders.get(k).getName().equals("bounceRate"))
								entity.setBounceRate(Double.parseDouble(values.getValues().get(k)));
							
							System.out.println(metricHeaders.get(k).getName() + ": " + values.getValues().get(k));
						}
					}
					
					list.add(entity);
				}
			}
			else if(cnt == 2) { // Event
				
				if (rows == null) {
					System.out.println("No data found for " + VIEW_ID);
					break;
				}
				
				for (ReportRow row: rows) {
					
					EventVO entity = new EventVO();
					
					List<String> dimensions = row.getDimensions();
					List<DateRangeValues> metrics = row.getMetrics();
					
					String cat = null, act = null;
					for (int i = 0; i < dimensionHeaders.size() && i < dimensions.size(); i++) {
						System.out.println(dimensionHeaders.get(i) + ": " + dimensions.get(i));
						if(dimensionHeaders.get(i).equals("ga:eventCategory"))cat=dimensions.get(i);
						if(dimensionHeaders.get(i).equals("ga:eventAction"))act=dimensions.get(i);
					}
					entity.setEventCategory(cat);
					entity.setEventAction(act);
					
					for (int j = 0; j < metrics.size(); j++) {
						DateRangeValues values = metrics.get(j);
						for (int k = 0; k < values.getValues().size() && k < metricHeaders.size(); k++) {
							
							if(metricHeaders.get(k).getName().equals("totalEvents"))
								entity.setTotalEvents(Integer.parseInt(values.getValues().get(k)));
							
							System.out.println(metricHeaders.get(k).getName() + ": " + values.getValues().get(k));
						}
					}
					
					if(entity.getEventCategory().contains("수강신청_클릭")) {
						for(InformVO en : list) {
							if(entity.getEventAction().contains(en.getPagePath())) {
								int entry = list.indexOf(en);							
								en.setTotalEvents(entity.getTotalEvents());
								list.set(entry, en);
							}
						}
					}
				}
			}
			
		}
		return list;
	}
	
private ArrayList<PageViewVO> getPageViewinRange(AnalyticsReporting service, String seq, String startDate, String endDate) throws IOException {

	    /* 측정 항목과 측정기준의 객체를 만들고 내용물을 설정한다
	     * 내용물에 대한 쿼리 레퍼런스는 아래의 링크를 참고하자
	     * https://developers.google.com/analytics/devguides/reporting/core/dimsmets
	     */
	
		 //기간 설정
		DateRange dateRange = new DateRange();
	    dateRange.setStartDate(startDate);
	    dateRange.setEndDate(endDate);
	    
	    //상품 페이지 보고서-----
	    Metric pageviews = new Metric()
	        .setExpression("ga:pageviews")
	        .setAlias("pageviews");
	    
	    //디멘션 설정
	    Dimension pageTitle = new Dimension().setName("ga:pagePath");
	    Dimension date = new Dimension().setName("ga:date");
	    
	    System.out.println("필터 적용 : ?masterSeq=" + seq);
	    String resultSeq = "masterSeq="+seq; // "masterSeq=" 가 포함되어 있어야함
	    
	    //측정 기준 필터 적용~
	    DimensionFilter nameFilter = new DimensionFilter()
	    		.setDimensionName("ga:pagePath") // {{pagePath}} 에
	    		.setExpressions(Arrays.asList(resultSeq));
	    
	    DimensionFilterClause dFilterClause = new DimensionFilterClause()
	    		.setFilters(Arrays.asList(nameFilter));
	    
	    
	    //일별 페이지뷰 정보를 저장하는 ArrayList
	    ArrayList<PageViewVO> allPageViews = new ArrayList<PageViewVO>();
		
		// 위의 항목과 기준을 구글로 Request 하기 위한 객체를 만든다
		ReportRequest request = new ReportRequest()
				.setViewId(VIEW_ID)
				.setDateRanges(Arrays.asList(dateRange))
				.setMetrics(Arrays.asList(pageviews))
				.setDimensions(Arrays.asList(pageTitle, date))
				.setDimensionFilterClauses(Arrays.asList(dFilterClause));

		// 리스트에 모두 싣습니다~
		ArrayList<ReportRequest> requests = new ArrayList<ReportRequest>();
		requests.add(request);

		// 아마 Request 한 것에 대한 결과를 Receive 하는 객체를 만드는 듯 하다
		GetReportsRequest getReport = new GetReportsRequest().setReportRequests(requests);

		// 상기의 리스트를 GA 서버로 Request 하여 결과 값을 Response 하는 메소드(Execute)
		GetReportsResponse response = service.reports().batchGet(getReport).execute();

		for (Report report : response.getReports()) {

			// 하나의 report 에는 하나의 header 밖에 없다.
			ColumnHeader header = report.getColumnHeader();

			// 헤더와 측정 기준, 항목 사이에 하나의 분류가 더 있는 모양이다.
			List<String> dimensionHeaders = header.getDimensions();
			List<MetricHeaderEntry> metricHeaders = header.getMetricHeader().getMetricHeaderEntries();
			List<ReportRow> rows = report.getData().getRows();

			if (rows == null) {
				System.out.println("No data found for " + VIEW_ID);
				return null;
			}

			for (ReportRow row : rows) {
				
				PageViewVO vo = new PageViewVO();
				
				
				List<String> dimensions = row.getDimensions();
				List<DateRangeValues> metrics = row.getMetrics();

				for (int i = 0; i < dimensionHeaders.size() && i < dimensions.size(); i++) {
					if(dimensionHeaders.get(i).equals("ga:date")) {
						String diHeader = dimensions.get(i);
						String str = String.format("%s-%s-%s", diHeader.substring(0, 4), diHeader.substring(4, 6), diHeader.substring(6, 8));
						vo.setmDate(str);
					}
				}

				for (int j = 0; j < metrics.size(); j++) {
					DateRangeValues values = metrics.get(j);

					int pv = 0;
					for (int k = 0; k < values.getValues().size() && k < metricHeaders.size(); k++) {
						if (metricHeaders.get(k).getName().equals("pageviews")) {
							pv += Integer.parseInt(values.getValues().get(k));
						}							
					}
					
					vo.setmPageView(pv);
				}
				
				Boolean flag = false;
				int idx = -1;
				for(PageViewVO tmp : allPageViews) {
					if(tmp.getmDate().equals(vo.getmDate())) {
						flag=true;
						idx = allPageViews.indexOf(tmp);
					}
				}
				if(idx != -1) {
					vo.setmPageView(vo.getmPageView() + allPageViews.get(idx).getmPageView());
					allPageViews.set(idx, vo);
				}
				else allPageViews.add(vo);
			}
		}
		
		Collections.sort(allPageViews, new Comparator<PageViewVO>(){
		      public int compare(PageViewVO obj1, PageViewVO obj2)
		      {
		            // TODO Auto-generated method stub
		            return obj1.getmDate().compareToIgnoreCase(obj2.getmDate());
		      }
		});
		
		// 결과를 반환
		return allPageViews;
	}
}