package ga.common.runner;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

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

import ga.common.DailyInformVO;

@Component
public class TeacherGaApiRunner {
	
	//Authorization Information for Google Analytics
	private final String APPLICATION_NAME = "Eduwill_Internship_GA_Project";
	private final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance(); // GsonFactory.getDefaultInstance()
	private final String KEY_FILE_LOCATION = "client_secrets_edw.json"; // /resources/secret_key 에 들어가는 인증 정보
	private final String VIEW_ID = "66471933"; // View ID 는 ga-dev-tools.appsport.com/account-explorer/
	
	public ArrayList<DailyInformVO> getPeriodData(String startDate, String endDate){
		
		ArrayList<DailyInformVO> result = new ArrayList<DailyInformVO>();
		
		//날짜 범위 설정
		String dt = startDate; // Start date
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(sdf.parse(dt));
			dt = sdf.format(c.getTime()); // dt is now the new date
			c.add(Calendar.DATE, -1); // number of days to add			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		//기간 내 GA 정보 수집 루프
		try {
			while (!dt.equals(endDate)) { // End date
				c.add(Calendar.DATE, 1); // number of days to add
				dt = sdf.format(c.getTime()); // dt is now the new date
				
				AnalyticsReporting service = initializeAnalyticsReporting(); // 1단계 : API 인증 정보 확인 및 서비스 초기화
				
				GetReportsResponse response = getDailyReport(service, dt, dt); // 2단계 : 요청 정보 객체를 생성, GA 서버로 전송하여 정보 획득
				
				ArrayList<DailyInformVO> tmp = printDailyResponse(response); // 3단계 : 정보를 파싱하여 자료를 얻음
				for(DailyInformVO vo : tmp) {
					result.add(vo);
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		if(result.isEmpty())return null;		
		return result;		
	}
	
	public ArrayList<DailyInformVO> getYesterdayData() {
		
		ArrayList<DailyInformVO> result = null;
		try {
			
			AnalyticsReporting service = initializeAnalyticsReporting(); // 1단계 : API 인증 정보 확인 및 서비스 초기화
			
			GetReportsResponse response = getDailyReport(service, null, null); // 2단계 : 요청 정보 객체를 생성, GA 서버로 전송하여 정보 획득
			
			result = printDailyResponse(response); // 3단계 : 정보를 파싱하여 자료를 얻음

		} catch(Exception e) {
			e.printStackTrace();
		}		
		
		return result;		
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
		

		/**
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
		Dimension pagePath = new Dimension().setName("ga:pagePath");
		Dimension pagePathLevel1 = new Dimension().setName("ga:pagePathLevel1");
		Dimension date = new Dimension().setName("ga:date");

		// 측정 기준 필터 적용~
		DimensionFilter tcodeFilter = new DimensionFilter().setDimensionName("ga:pagePath") // {{pagePath}} 에
				.setExpressions(Arrays.asList(".*[\\?|&]tcode=.*"));
		
		DimensionFilter teacherFilter = new DimensionFilter().setDimensionName("ga:pagePath") // {{pagePath}} 에
				.setExpressions(Arrays.asList("teacher"));
		
		DimensionFilter nBookFilter = new DimensionFilter().setDimensionName("ga:pagePath") // {{pagePath}} 에
				.setNot(true)
				.setExpressions(Arrays.asList("book"));
		
		DimensionFilter nClassPlanFilter = new DimensionFilter().setDimensionName("ga:pagePath") // {{pagePath}} 에
				.setNot(true)
				.setExpressions(Arrays.asList("classplan"));
		
		DimensionFilter nPremainFilter = new DimensionFilter().setDimensionName("ga:pagePath") // {{pagePath}} 에
				.setNot(true)
				.setExpressions(Arrays.asList("premain.asp"));

		DimensionFilterClause dFilterClause = new DimensionFilterClause()
				.setFilters(Arrays.asList(tcodeFilter, teacherFilter, nBookFilter, nClassPlanFilter,nPremainFilter))
				.setOperator("AND");

		// 위의 항목과 기준을 구글로 Request 하기 위한 객체를 만든다
		ReportRequest request = new ReportRequest().setViewId(VIEW_ID).setDateRanges(Arrays.asList(dateRange))
				.setMetrics(Arrays.asList(pageviews, uniqueviews, sessions, entrances, bounces))
				.setDimensions(Arrays.asList(pagePath, pagePathLevel1, date)).setDimensionFilterClauses(Arrays.asList(dFilterClause));

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
		

		for (Report report: response.getReports()) {			
						
			//하나의 report 에는 하나의 header 밖에 없다.
			ColumnHeader header = report.getColumnHeader();			
			
			//헤더와 측정 기준, 항목 사이에 하나의 분류가 더 있는 모양이다.
			List<String> dimensionHeaders = header.getDimensions();
			List<MetricHeaderEntry> metricHeaders = header.getMetricHeader().getMetricHeaderEntries();
			List<ReportRow> rows = report.getData().getRows();

			
			if (rows == null) {
				System.out.println("No data found for " + VIEW_ID);
				return null;
			}
			
			System.out.println("Success to find for " + VIEW_ID + "(" + rows.size() + ")");
			for (ReportRow row: rows) {
				
				DailyInformVO entity = new DailyInformVO();
				String code = null;
				
				List<String> dimensions = row.getDimensions();
				List<DateRangeValues> metrics = row.getMetrics();
				
				String path = "", pathLevel1 = "";
				
				for (int i = 0; i < dimensionHeaders.size() && i < dimensions.size(); i++) {
					if(dimensionHeaders.get(i).equals("ga:pagePath")) {
						entity.setPagePath(dimensions.get(i));
						path = dimensions.get(i);
					}
					if(dimensionHeaders.get(i).equals("ga:pagePathLevel1")) {
						pathLevel1 = dimensions.get(i);
					}
					if(dimensionHeaders.get(i).equals("ga:date")) {
						entity.setuDate(dimensions.get(i));
					}
				}
				
				code = parseCodeInURL(path, pathLevel1);
				if(code != null)entity.setPageCode(code);
				
				for (int j = 0; j < metrics.size(); j++) {
					DateRangeValues values = metrics.get(j);
					
					
					for (int k = 0; k < values.getValues().size() && k < metricHeaders.size(); k++) {
						
						if(metricHeaders.get(k).getName().equals("pageviews"))
							entity.setPageviews(Integer.parseInt(values.getValues().get(k)));
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
		
		}
		
		return list;
	}

	private String parseCodeInURL(String url, String level1){
		  
		String progress = null, tcode = null, subj = null, extra = null;
		if (level1.length() > 2) {
			if(level1.startsWith("_")) {
				progress = level1.toUpperCase();
			}
			else {
				progress = level1.substring(1, 2).toUpperCase();
			}
		}
		
		Map<String, String> param = null;
		
		try {
			URL paramUrl = new URL("http://www.eduwill.net"+url);
			param = splitQuery(paramUrl);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		if(param.get("tcode") != null)tcode = param.get("tcode").substring(0, 4);
		if(param.get("subj") != null)subj = param.get("subj");
		if(param.get("company") != null)extra = param.get("company");
		
		if (progress == null || tcode == null)
			return "---";

		String code = "";

		code = progress + "-" + tcode + "-";
		if(subj != null)code = code + subj;
		code = code + "-";
		if(extra != null)code = code + extra;
		
		
		if(code.length() >= 20) {
			System.out.println("pageCode : " + code);
			return code.substring(0, 19);
		}
		return code;	
	}
	
	public static Map<String, String> splitQuery(URL url) throws UnsupportedEncodingException {
	    Map<String, String> query_pairs = new LinkedHashMap<String, String>();
	    String query = url.getQuery();
	    String[] pairs = query.split("&");
	    for (String pair : pairs) {
	        int idx = pair.indexOf("=");
	        query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8").toLowerCase(), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
	    }
	    return query_pairs;
	}
}