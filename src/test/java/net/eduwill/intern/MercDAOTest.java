package net.eduwill.intern;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/**/root-context.xml" })
public class MercDAOTest {

	@Test
	public void test() throws Exception {
		
		ArrayList<String> aList = new ArrayList<String>();
		aList.add("and1");
		aList.add("the");
		aList.add("the");
		aList.add("the");
		aList.add("and");
		aList.add("the");
		aList.add("and");
		aList.add("and");
		aList.add("and1");
		int size = aList.size();
		for (int i = 0; i < size; i++) {
			String firstVal = aList.get(i);
			for (int j = i + 1; j < size; j++) {
				String secondVal = aList.get(j);
				if (firstVal.equals(secondVal)) {
					aList.remove(j);
					size--;
					i = 0;
					j--;
				}
			}
		}
		System.out.println(aList);
		
		String url = "http://www.eduwill.net/Won2/Teacher/Teacher/gov2/Teacher_Detail_review.asp?TCode=FL00";
		String level1 = "/Won2/";
		
		String code = parseCodeInURL(url, level1);
		
		System.out.println("pageCode : " + code);
		
		
	}

	private String parseCodeInURL(String url, String level1) throws Exception{
		  
		String progress = null, tcode = null, subj = null, extra = null;
		if (level1.length() > 2) {
			if(level1.startsWith("_")) {
				progress = level1.toUpperCase();
			}
			else {
				progress = level1.substring(1, 2).toUpperCase();
			}
		}
			

		String[] arr = null;
		

		URL paramUrl = new URL(url);
		Map<String, String> param = splitQuery(paramUrl);
		
		System.out.println("tcode : " + param.get("tcode"));
		if(param.get("ddfdf") == null)System.out.println("널이지롱!!!!");

		arr = url.split("\\?");
		if (arr.length > 1) {
			String tmp = arr[arr.length - 1];
			arr = tmp.split("&");

			int idx = 0;
			boolean flag = false;
			if (arr.length > 0) {
				for (idx = 0; idx < arr.length; idx++) {
					if (arr[idx].toLowerCase().contains("tcode")) {
						flag=true;
						break;
					}
				}
			}
			if(!flag)return progress+"---";
			tmp = arr[idx];
			arr = tmp.split("=");
			if (arr.length > 1) {
				tcode = arr[1];
			}
		}
		
		arr = url.split("\\?");
		if (arr.length > 1) {
			String tmp = arr[arr.length - 1];
			arr = tmp.split("&");

			int idx = 0;
			boolean flag = false;
			if (arr.length > 1) {
				for (idx = 0; idx < arr.length; idx++) {
					if (arr[idx].toLowerCase().contains("subj")) {
						flag=true;
						break;
					}
						
				}
			}
			if(flag) {
				tmp = arr[idx];
				arr = tmp.split("=");
				if (arr.length > 1) {
					subj = arr[1];
				}
			}			
		}
		
		arr = url.split("\\?");
		if (arr.length > 1) {
			String tmp = arr[arr.length - 1];
			arr = tmp.split("&");

			int idx = 0;
			boolean flag = false;
			if (arr.length > 1) {
				for (idx = 0; idx < arr.length; idx++) {
					if (arr[idx].toLowerCase().contains("company")) {
						flag=true;
						break;
					}
						
				}
			}
			if(flag) {
				tmp = arr[idx];
				arr = tmp.split("=");
				if (arr.length > 1) {
					subj = arr[1];
				}
			}			
		}

		if (progress == null || tcode == null)
			return "---";

		String code = "";

		code = progress + "-" + tcode + "-";
		if(subj != null)code = code + subj;
		code = code + "-";
		if(extra != null)code = code + extra;

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
