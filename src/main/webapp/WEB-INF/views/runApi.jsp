<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.ArrayList, java.text.DecimalFormat, ga.common.InformDao" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Eduwill Ngene>상품 정보</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="/resources/css/style.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container-fluid text-center">    
  <div class="row content">
    <div class="col-sm-12 text-left"> 
      <h3 class="page-header">기간 선택<small> 시작일 / 종료일</small></h3>
      <form action="/analytics" method="get">
      	  <input type="hidden" name="seq" value=${seq} />
	      <div class="col-sm-5 text-left">
	      	시작일 : <input type="date" class="form-control" name="startDate">
	      </div>
	      <div class="col-sm-5 text-left">
	      	종료일 : <input type="date" class="form-control" name="endDate">
	      </div>
	      <div class="col-sm-2 text-center">
	      	<br/>
	      	<input type="submit" class="btn btn-primary btn-block" value="검색" />
	      </div>
      </form>
    </div>
    <div class="col-sm-12 text-left"> 
      
	  <h3 class="page-header">상품 정보<small> 수치 확인</small></h3>
	  
	  <div id="resultDisplay">
	  </div>
	 
	  <%
	  ArrayList<InformDao> list = (ArrayList<InformDao>)request.getAttribute("result");
	  
	  if(list == null){
		  %>검색 기간을 설정해 주십시오.<%
	  }
	  else {
	  %>
	 	<table class="table table-hover">
	    <thead>
	      <tr>
	        <th>URL</th>
	        <th>페이지뷰</th>
	        <th>순 페이지뷰</th>
	        <th>세션 수</th>
	        <th>이탈율</th>
	        <th>전환</th>
	        <th>전환율</th>
	      </tr>
	    </thead>
	    <tbody>
		    <c:forEach var="inform" items="${result}" varStatus="status">
		    	<tr>
		    		<td>${inform.pagePath}</td>
		    		<td>${inform.pageviews}</td>
		    		<td>${inform.uniquePageviews}</td>
		    		<td>${inform.sessions}</td>
		    		<td>
		    			<script>
		    				var num = ${inform.bounceRate};
		    				document.write(num.toFixed(3));
		    			</script>
		    		</td>
		    		<td>${inform.totalEvents}</td>
		    		<td>
			    		<script>
			    			var num2 = ${inform.totalEvents*1.0 / inform.pageviews*1.0 * 100.0};
			    			document.write(num2.toFixed(3));
			    		</script>		    		
		    		</td>
		    	</tr>
		    </c:forEach>
	    </tbody>
	  </table>

	<h3 class="page-header">통계<small> 차트 확인</small></h3>
	
	<script type="text/javascript">
	window.onload = function () {
		var chart = new CanvasJS.Chart("chartContainer",
		{
			theme: "theme2",
			title:{
				text: "통계"
			},
			data: [{
				type: "pie",
				showInLegend: true,
				toolTipContent: "{y} - #percent %",
				yValueFormatString: "#0.#,,. Million",
				legendText: "{indexLabel}",
				dataPoints: [
					{  y: <%=4%>, indexLabel: "테스트_아이템_1" },
					{  y: <%=3%>, indexLabel: "테스트_아이템_2" }
				]
			}]
		});
		chart.render();
	}
	
	</script>
	<script src="/resources/js/canvasjs.min.js"></script>
	<div id="chartContainer" style="height: 400px; width: 100%;"></div>
  
	  
	 <%
	  }
	  %>
    </div>

  </div>
</div>
 
</body>
</html>