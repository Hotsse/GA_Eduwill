package net.eduwill.intern;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
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

import ga.api.domain.DailyInformVO;
import ga.api.domain.MercVO;
import ga.common.EventVO;

public class ApiRunnerTest {
	
	//Authorization Information for Google Analytics
	private final String APPLICATION_NAME = "Eduwill_Internship_GA_Project";
	private final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance(); // GsonFactory.getDefaultInstance()
	private final String KEY_FILE_LOCATION = "client_secrets_edw.json"; // /resources/secret_key 에 들어가는 인증 정보
	private final String VIEW_ID = "66471933"; // View ID 는 ga-dev-tools.appsport.com/account-explorer/

	@Test
	public void getDailyData() {
		ArrayList<DailyInformVO> result = new ArrayList<DailyInformVO>();
		
		String startDate = "2017-10-28";
		String endDate = "2017-10-31";
	
		try {
			//서비스를 초기화 시키고
			AnalyticsReporting service = initializeAnalyticsReporting();
				
			//쿼리를 실행 시켜서
			GetReportsResponse response = getDailyReport(service, startDate, endDate);
			
			//결과값을 파싱하여 뿌린다
			result = printDailyResponse(response);

		} catch(Exception e) {
			e.printStackTrace();
		}
		
		int sumpv = 0;
		for(DailyInformVO vo : result) {
			if(vo.getPagePath().contains("NDcw")) {
				System.out.println("pagePath :" + vo.getPagePath());
				sumpv+=vo.getPageviews();
			}
		}
		System.out.println("SUMPV : " + sumpv);
		
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
		FileInputStream fileInputStream = new FileInputStream(resource.getFile());
		
		//HTTP 통신을 초기화 시키고 인증 파일(.json)을 통해서 서비스 객체를 구성하여 반환한다.
		HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		GoogleCredential credential = GoogleCredential
				.fromStream(fileInputStream)
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
	private GetReportsResponse getDailyReport(AnalyticsReporting service, String startDate, String endDate) throws IOException {

		// 조사할 기간의 범위를 설정한다
		DateRange dateRange = new DateRange();
		if(startDate != null)dateRange.setStartDate(startDate);
		else dateRange.setStartDate("yesterday");
		if(endDate != null)dateRange.setEndDate(endDate);
		else dateRange.setEndDate("yesterday");
		

		/*
		 * 측정 항목과 측정기준의 객체를 만들고 내용물을 설정한다 내용물에 대한 쿼리 레퍼런스는 아래의 링크를 참고하자
		 * https://developers.google.com/analytics/devguides/reporting/core/dimsmets
		 */

		// 상품 페이지 보고서-----
		Metric pageviews = new Metric().setExpression("ga:pageviews").setAlias("pageviews");

		Metric uniqueviews = new Metric().setExpression("ga:uniquePageviews").setAlias("uniquePageviews");

		Metric sessions = new Metric().setExpression("ga:sessions").setAlias("sessions");

		Metric entrances = new Metric().setExpression("ga:entrances").setAlias("entrances");

		Metric bounces = new Metric().setExpression("ga:bounces").setAlias("bounces");

		// 디멘션 설정
		Dimension pageTitle = new Dimension().setName("ga:pagePath");
		Dimension pageLevel1 = new Dimension().setName("ga:pagePathLevel1");
		Dimension pageLevel2 = new Dimension().setName("ga:pagePathLevel2");
		Dimension pageLevel3 = new Dimension().setName("ga:pagePathLevel3");
		Dimension pageLevel4 = new Dimension().setName("ga:pagePathLevel4");
		Dimension date = new Dimension().setName("ga:date");

		System.out.println("URL 필터 적용 : ?masterSeq=");
		String resultSeq = "masterSeq="; // "masterSeq=" 가 포함되어 있어야함

		// 측정 기준 필터 적용~
		DimensionFilter nameFilter = new DimensionFilter().setDimensionName("ga:pagePath") // {{pagePath}} 에
				.setExpressions(Arrays.asList(resultSeq));

		DimensionFilterClause dFilterClause = new DimensionFilterClause().setFilters(Arrays.asList(nameFilter));

		// 위의 항목과 기준을 구글로 Request 하기 위한 객체를 만든다
		ReportRequest request = new ReportRequest().setViewId(VIEW_ID).setDateRanges(Arrays.asList(dateRange))
				.setMetrics(Arrays.asList(pageviews, uniqueviews, sessions, entrances, bounces))
				.setDimensions(Arrays.asList(pageTitle, date, pageLevel1, pageLevel2, pageLevel3, pageLevel4)).setDimensionFilterClauses(Arrays.asList(dFilterClause));

		// 리스트에 모두 싣습니다~
		ArrayList<ReportRequest> requests = new ArrayList<ReportRequest>();
		requests.add(request);

		// 아마 Request 한 것에 대한 결과를 Receive 하는 객체를 만드는 듯 하다
		GetReportsRequest getReport = new GetReportsRequest().setReportRequests(requests);

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
	private ArrayList<DailyInformVO> printDailyResponse(GetReportsResponse response) {
		  
		//하나의 response 에는 다수의 report 가 포함되어 있다.
		//List를 foreach를 통해 각 객체마다 접근한다.
		
		ArrayList<DailyInformVO> list = new ArrayList<DailyInformVO>();
		
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
				
				System.out.println("Success to find for " + VIEW_ID);
				
				int sumpv = 0;
				
				for (ReportRow row: rows) {
					
					//System.out.println("====================================");
					
					DailyInformVO entity = new DailyInformVO();
					String code = null;
					
					List<String> dimensions = row.getDimensions();
					List<DateRangeValues> metrics = row.getMetrics();
					
					for (int i = 0; i < dimensionHeaders.size() && i < dimensions.size(); i++) {
						if(dimensionHeaders.get(i).equals("ga:pagePath")) {
							entity.setPagePath(dimensions.get(i));
							code = parseCodeInURL(dimensions.get(i));
							if(code == null)break;
							else entity.setPageCode(code);
						}
						if(dimensionHeaders.get(i).equals("ga:date")) {
							entity.setuDate(dimensions.get(i));
						}
					}
					
					if(code == null)break;
					
					for (int j = 0; j < metrics.size(); j++) {
						DateRangeValues values = metrics.get(j);
						
						
						for (int k = 0; k < values.getValues().size() && k < metricHeaders.size(); k++) {
							
							if(metricHeaders.get(k).getName().equals("pageviews")) {
								entity.setPageviews(Integer.parseInt(values.getValues().get(k)));
								System.out.println("pageviews:" + values.getValues().get(k));
								sumpv+= Integer.parseInt(values.getValues().get(k));
							}
								
							else if(metricHeaders.get(k).getName().equals("uniquePageviews"))
								entity.setUniquePageviews(Integer.parseInt(values.getValues().get(k)));
							else if(metricHeaders.get(k).getName().equals("sessions"))
								entity.setSessions(Integer.parseInt(values.getValues().get(k)));
							else if(metricHeaders.get(k).getName().equals("entrances"))
								entity.setEntrances(Integer.parseInt(values.getValues().get(k)));
							else if(metricHeaders.get(k).getName().equals("bounces"))
								entity.setBounces(Integer.parseInt(values.getValues().get(k)));
						}
					}
					
					list.add(entity);
				}
				
				System.out.println("Sum PV : " + sumpv);
			}
		}
		
		
		return list;
	}

	private String parseCodeInURL(String url) {
		  String code = null;
		  
		  String []arr = url.split("\\?");
		  if(arr.length > 1) {
			  String tmp = arr[1];
			  arr = tmp.split("&");
			  
			  int idx=0;
			  if(arr.length > 1) {
				  for(idx=0; idx < arr.length; idx++) {
					  if(arr[idx].contains("masterSeq"))break;
				  }
			  }
			  tmp = arr[idx];
			  arr = tmp.split("=");
			  code = arr[1];
			  while(code.length()%4 != 0)code = code.concat("=");
		  }
		  
		  return code;
	}

}
