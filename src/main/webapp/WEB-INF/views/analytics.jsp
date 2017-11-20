<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.text.DecimalFormat" %>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Eduwill Ngene>상품 정보</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="/resources/css/style.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script type='text/javascript' src='//code.jquery.com/jquery-1.8.3.js'></script>

<!-- jqfloat -->
<script type="text/javascript" src="/resources/js/jquery.flot.js"></script>
<script type="text/javascript" src="/resources/js/jquery.flot.time.js"></script>
<script type="text/javascript" src="/resources/js/jquery.flot.navigate.js"></script>

<!-- datepicker -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.5.0/css/bootstrap-datepicker3.min.css">
<script type='text/javascript' src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.5.0/js/bootstrap-datepicker.min.js"></script>

</head>
<body id="myPage" data-spy="scroll" data-target=".navbar" data-offset="60">

	<script>
	$(function() {
		$('#startDatepicker').datepicker({
            calendarWeeks: false,
            todayHighlight: true,
            autoclose: true,
            format: "yyyy-mm-dd",
            language: "kr"
        });
		$('#endDatepicker').datepicker({
            calendarWeeks: false,
            todayHighlight: true,
            autoclose: true,
            format: "yyyy-mm-dd",
            language: "kr"
        });
	});
	</script>
	
	<!-- main conatiner -->
	<div class="container-fluid text-center">
		<div class="row content">
		
			<!-- 기간 선택 -->
			<div class="col-sm-12 text-left" id="Calendar">
				<h3 class="page-header">
					기간 선택<small> 시작일 / 종료일</small>
				</h3>
				
				<c:choose>
					<c:when test="${param.delim == 'G'}">
						<form action="/goods/analytics" method="get">
					</c:when>
					<c:when test="${param.delim == 'T'}">
						<form action="/teacher/analytics" method="get">
					</c:when>
					<c:when test="${param.delim == 'BT'}">
						<form action="/teacher/analytics" method="get">
					</c:when>
				</c:choose>
								
					<input type="hidden" name="seq" value=${param.seq } />
					<input type="hidden" name="idx" value=${param.idx } />
					<input type="hidden" name="delim" value=${param.delim } />
					<div class="col-sm-6">
						<div class="input-group date">
							<span class="input-group-addon gap">시작일</span>
							<c:choose>
								<c:when test="${startDate eq null}">
									<input type="text" class="form-control" name="startDate" id="startDatepicker" placeholder="yyyy-mm-dd" />
								</c:when>
								<c:otherwise>
									<input type="text" class="form-control" name="startDate" id="startDatepicker" placeholder="yyyy-mm-dd" value=${startDate } />
								</c:otherwise>
							</c:choose>
							
							<span class="input-group-addon gap">종료일</span>
							<c:choose>
								<c:when test="${startDate eq null}">
									<input type="text" class="form-control" name="endDate" id="endDatepicker" placeholder="yyyy-mm-dd" />
								</c:when>
								<c:otherwise>
									<input type="text" class="form-control" name="endDate" id="endDatepicker" placeholder="yyyy-mm-dd" value=${endDate } />
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="col-sm-1 text-right">
						<input class="btn btn-primary" type="submit"
							data-toggle="collapse" data-target="#collapseExample"
							aria-expanded="false" aria-controls="collapseExample" value="검색" />
					</div>
				</form>
				<c:choose>
					<c:when test="${result eq null}"></c:when>
					<c:otherwise>
						<div class="col-sm-1 text-left">
							<button class="btn btn-primary" type="button"
								data-toggle="popover" data="isAuth"
								onclick="excel('excel_body')">엑셀 다운로드</button>
						</div>
					</c:otherwise>
				</c:choose>
			</div>
			
			<!-- 상품 정보 -->
			<div class="col-sm-12 text-left" id="Information">

				<h3 class="page-header">
					<c:choose>
						<c:when test="${param.delim == 'G'}">
							상품 정보<small> ${pageName}</small>
						</c:when>
						<c:when test="${param.delim == 'T'}">
							강사 정보<small> (웹)</small>
						</c:when>
						<c:when test="${param.delim == 'BT'}">
							강사 정보<small> (모바일)</small>
						</c:when>
					</c:choose>
					
				</h3>

				<div id="resultDisplay"></div>
				
				<c:choose>
				
					<c:when test="${result eq null}"> <!-- if(result == null) {...} -->
					<!-- 검색 결과가 존재하지 않음 -->
						검색 기간을 설정해 주십시오.<br/></div>
					</c:when>
					
					
					<c:otherwise> <!-- else {...} -->
					<!-- 검색 결과가 존재함 -->
						<table class="table table-bordered table-hover" cellspacing="0"
							width="100%">
							<thead>
								<tr>
									<th>날짜</th>
									<th>페이지뷰</th>
									<th>순 페이지뷰</th>
									<th>세션 수</th>
									<th>방문자 수</th>
									<th>이탈율</th>
									<!-- 
									<th>전환</th>
									<th>전환율</th>
									 -->
								</tr>
							</thead>
							<tbody>
								<c:forEach var="inform" items="${result}" varStatus="status">
									<tr>
										<td>${startDate}~${endDate}</td>
										<td>${inform.pageviews}</td>
										<td>${inform.uniquePageviews}</td>
										<td>${inform.sessions}</td>
										<td>${inform.entrances}</td>
										<td>${inform.bounceRate}</td>
									</tr>
								</c:forEach>
								<c:forEach var="inform" items="${result_lastmonth}" varStatus="status">
									<tr style="color:#aaa;">
										<td><small>${lastmonth } (한달 전)</small></td>
										<td><small>${inform.pageviews}</small></td>
										<td><small>${inform.uniquePageviews}</small></td>
										<td><small>${inform.sessions}</small></td>
										<td><small>${inform.entrances}</small></td>
										<td><small>${inform.bounceRate}</small></td>
									</tr>
								</c:forEach>
								<c:forEach var="inform" items="${result_lastyear}" varStatus="status">
									<tr style="color:#aaa;">
										<td><small>${lastyear } (일년 전)</small></td>
										<td><small>${inform.pageviews}</small></td>
										<td><small>${inform.uniquePageviews}</small></td>
										<td><small>${inform.sessions}</small></td>
										<td><small>${inform.entrances}</small></td>
										<td><small>${inform.bounceRate}</small></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					
					<!-- 엑셀 -->
						<form name="frm" method="post">
							<input type="hidden" name="excel_data" /> <input type="hidden"
								name="excel_title"
								value="GA_Data_${param.seq}_${startDate}_${endDate}" />
						</form>
						<table id="excel_body" style="display: none;">
							<caption>Date = ${startDate } - ${endDate }</caption>
							<thead>
								<tr>
									<th>&nbsp;날짜</th>
									<th>&nbsp;페이지뷰</th>
									<th>&nbsp;순페이지뷰</th>
									<th>&nbsp;세션</th>
									<th>&nbsp;방문자수</th>
									<th>&nbsp;이탈율</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="dailyexcel" items="${dailyDataList}"
									varStatus="status">
									<tr>
										<td>${dailyexcel.mDate}</td>
										<td>${dailyexcel.mPageView}</td>
										<td>${dailyexcel.mUniquePageviews}</td>
										<td>${dailyexcel.mSessions}</td>
										<td>${dailyexcel.mEntrances}</td>
										<td><script>
										var result = new Number(eval("(${dailyexcel.mBounces} * 1.0) / (${dailyexcel.mSessions} * 1.0) * 100"));
										document.write(result.toFixed(2));
										</script></td>
								</c:forEach>
							</tbody>
						</table>
			
					<!-- 통계(차트확인) -->
					<div class="col-sm-12 text-left" id="Chart">
						<h3 class="page-header">
							통계<small> 차트 확인</small>
							<button type="button" class="btn btn-default btn-sm" onclick="onChartButtonClicked('pageviews')">페이지뷰</button>
							<button type="button" class="btn btn-default btn-sm" onclick="onChartButtonClicked('uniquePageviews')">순 페이지뷰</button>
							<button type="button" class="btn btn-default btn-sm" onclick="onChartButtonClicked('sessions')">세션 수</button>
							<button type="button" class="btn btn-default btn-sm" onclick="onChartButtonClicked('entrances')">방문자 수</button>
							<button type="button" class="btn btn-default btn-sm" onclick="onChartButtonClicked('bounceRate')">이탈율</button>
						</h3>
						
						<div id="chartCanvas" class="demo-placeholder" style="display:inline-block; height: 400px; width: 85%;"></div>
						<div style="display:inline-block; height:400px; width:13%; margin-left:1%; vertical-align: text-bottom;">
							<h3 class="page-header"><small>차트 목록</small></h3><br/>
							<input type="checkbox" id="check_today" onClick="onCheckboxChecked()" checked /> 지금<br/>
							
							<c:choose>				
								<c:when test="${result_lastmonth eq null}"> 
									<input type="checkbox" id="check_monthAgo" onClick="onCheckboxChecked()" disabled/>
								</c:when>
								<c:otherwise>
									<input type="checkbox" id="check_monthAgo" onClick="onCheckboxChecked()" checked />
								</c:otherwise>
							</c:choose> 한달 전<br/>
							<c:choose>
								<c:when test="${result_lastyear eq null}"> 
									<input type="checkbox" id="check_yearAgo" onClick="onCheckboxChecked()" disabled/>
								</c:when>
								<c:otherwise>
									<input type="checkbox" id="check_yearAgo" onClick="onCheckboxChecked()" checked />
								</c:otherwise>
							</c:choose> 일년 전
						</div>
		
						<!-- declare and initialize variables -->
						<script>
							// 현재
							var mdate = ["1999-01-01"];
							
							var mpageView = [0];
							var mUniquePageviews = [0];
							var mSessions = [0];
							var mEntrances = [0];
							var mBounces = [0];
							var mTotalEvents = [0];
							var mBounceRate = [0.0];
							var mEventRate = [0.0];
							
							// 한달 전
							var mpageView_lastmonth = [0];
							var mUniquePageviews_lastmonth = [0];
							var mSessions_lastmonth = [0];
							var mEntrances_lastmonth = [0];
							var mBounces_lastmonth = [0];
							var mTotalEvents_lastmonth = [0];
							var mBounceRate_lastmonth = [0.0];
							var mEventRate_lastmonth = [0.0];
							
							// 일년 전
							var mpageView_lastyear = [0];
							var mUniquePageviews_lastyear = [0];
							var mSessions_lastyear = [0];
							var mEntrances_lastyear = [0];
							var mBounces_lastyear = [0];
							var mTotalEvents_lastyear = [0];
							var mBounceRate_lastyear = [0.0];
							var mEventRate_lastyear = [0.0];
						</script>
						
						<!-- input data in data array-->
						<c:forEach var="dailyDataList" items="${dailyDataList}" varStatus="status">
							<script>
								mdate.push("${dailyDataList.mDate}");
								mpageView.push(${dailyDataList.mPageView});
								mUniquePageviews.push(${dailyDataList.mUniquePageviews});
								mSessions.push(${dailyDataList.mSessions});
								mEntrances.push(${dailyDataList.mEntrances});
								mBounces.push(${dailyDataList.mBounces});
								mTotalEvents.push(${dailyDataList.mTotalEvents});
								mBounceRate.push((${dailyDataList.mBounces} * 1.0) / (${dailyDataList.mSessions} * 1.0) * 100);
								mEventRate.push(${dailyDataList.mPageView});
							</script>
						</c:forEach>
						
						<c:forEach var="dailyDataList" items="${dailyDataList_lastmonth}" varStatus="status">
							<script>
								mpageView_lastmonth.push(${dailyDataList.mPageView});
								mUniquePageviews_lastmonth.push(${dailyDataList.mUniquePageviews});
								mSessions_lastmonth.push(${dailyDataList.mSessions});
								mEntrances_lastmonth.push(${dailyDataList.mEntrances});
								mBounces_lastmonth.push(${dailyDataList.mBounces});
								mTotalEvents_lastmonth.push(${dailyDataList.mTotalEvents});
								mBounceRate_lastmonth.push((${dailyDataList.mBounces} * 1.0) / (${dailyDataList.mSessions} * 1.0) * 100);
								mEventRate_lastmonth.push(${dailyDataList.mPageView});
							</script>
						</c:forEach>
						
						<c:forEach var="dailyDataList" items="${dailyDataList_lastyear}" varStatus="status">
							<script>
								mpageView_lastyear.push(${dailyDataList.mPageView});
								mUniquePageviews_lastyear.push(${dailyDataList.mUniquePageviews});
								mSessions_lastyear.push(${dailyDataList.mSessions});
								mEntrances_lastyear.push(${dailyDataList.mEntrances});
								mBounces_lastyear.push(${dailyDataList.mBounces});
								mTotalEvents_lastyear.push(${dailyDataList.mTotalEvents});
								mBounceRate_lastyear.push((${dailyDataList.mBounces} * 1.0) / (${dailyDataList.mSessions} * 1.0) * 100);
								mEventRate_lastyear.push(${dailyDataList.mPageView});
							</script>
						</c:forEach>
						
						<!-- input data in chart array -->
						<script>
						
							var dataPageviews = [];
							var dataUniquePageviews = [];
							var dataSessions = [];
							var dataEntrances = [];
							var dataBounceRate = [];
							
							var dataPageviews_lastmonth = [];
							var dataUniquePageviews_lastmonth = [];
							var dataSessions_lastmonth = [];
							var dataEntrances_lastmonth = [];
							var dataBounceRate_lastmonth = [];
							
							var dataPageviews_lastyear = [];
							var dataUniquePageviews_lastyear = [];
							var dataSessions_lastyear = [];
							var dataEntrances_lastyear = [];
							var dataBounceRate_lastyear = [];
							
							for (var i = 1; i < mdate.length; i ++) {
								dataPageviews.push([new Date(mdate[i]), mpageView[i]]);
								dataUniquePageviews.push([new Date(mdate[i]), mUniquePageviews[i]]);
								dataSessions.push([new Date(mdate[i]), mSessions[i]]);
								dataEntrances.push([new Date(mdate[i]), mEntrances[i]]);
								dataBounceRate.push([new Date(mdate[i]), mBounceRate[i]]);
								
								dataPageviews_lastmonth.push([new Date(mdate[i]), mpageView_lastmonth[i]]);
								dataUniquePageviews_lastmonth.push([new Date(mdate[i]), mUniquePageviews_lastmonth[i]]);
								dataSessions_lastmonth.push([new Date(mdate[i]), mSessions_lastmonth[i]]);
								dataEntrances_lastmonth.push([new Date(mdate[i]), mEntrances_lastmonth[i]]);
								dataBounceRate_lastmonth.push([new Date(mdate[i]), mBounceRate_lastmonth[i]]);
								
								dataPageviews_lastyear.push([new Date(mdate[i]), mpageView_lastyear[i]]);
								dataUniquePageviews_lastyear.push([new Date(mdate[i]), mUniquePageviews_lastyear[i]]);
								dataSessions_lastyear.push([new Date(mdate[i]), mSessions_lastyear[i]]);
								dataEntrances_lastyear.push([new Date(mdate[i]), mEntrances_lastyear[i]]);
								dataBounceRate_lastyear.push([new Date(mdate[i]), mBounceRate_lastyear[i]]);
							}
						
						</script>						
						
						<!-- jQuery.Flot Script -->
						<script type="text/javascript">						
						var data = [];
						var btnMode = "";
						
						function onChartButtonClicked(btn){
							
							data = [];
							
							var maximumData=0.0;
							
							btnMode = btn;
							if(btn == "pageviews"){
								
								if($("#check_today").is(":checked")){
									data.push({label: "페이지뷰(현재)", data:dataPageviews});
								}
								
								if($("#check_monthAgo").is(":checked")){
									data.push({label: "페이지뷰(한달 전)", data:dataPageviews_lastmonth});
								}
								
								if($("#check_yearAgo").is(":checked")){
									data.push({label: "페이지뷰(일년 전)", data:dataPageviews_lastyear});
								}
								
								for (var i = 1; i < mdate.length; i ++) {
									if(mpageView[i] > maximumData)maximumData=mpageView[i];
								}
							}
							if(btn == "uniquePageviews"){
								
								if($("#check_today").is(":checked")){
									data.push({label: "순 페이지뷰(현재)", data:dataUniquePageviews});
								}
								
								if($("#check_monthAgo").is(":checked")){
									data.push({label: "순 페이지뷰(한달 전)", data:dataUniquePageviews_lastmonth});
								}
								
								if($("#check_yearAgo").is(":checked")){
									data.push({label: "순 페이지뷰(일년 전)", data:dataUniquePageviews_lastyear});
								}								
								
								for (var i = 1; i < mdate.length; i ++) {
									if(mUniquePageviews[i] > maximumData)maximumData=mUniquePageviews[i];
								}
							}
							if(btn == "sessions"){
								
								if($("#check_today").is(":checked")){
									data.push({label: "세션 수(현재)", data:dataSessions});
								}
								
								if($("#check_monthAgo").is(":checked")){
									data.push({label: "세션 수(한달 전)", data:dataSessions_lastmonth});
								}
								
								if($("#check_yearAgo").is(":checked")){
									data.push({label: "세션 수(일년 전)", data:dataSessions_lastyear});
								}								
								
								for (var i = 1; i < mdate.length; i ++) {
									if(mSessions[i] > maximumData)maximumData=mSessions[i];
								}
							}
							if(btn == "entrances"){
								
								if($("#check_today").is(":checked")){
									data.push({label: "방문자 수(현재)", data:dataEntrances});
								}
								
								if($("#check_monthAgo").is(":checked")){
									data.push({label: "방문자 수(한달 전)", data:dataEntrances_lastmonth});
								}
								
								if($("#check_yearAgo").is(":checked")){
									data.push({label: "방문자 수(일년 전)", data:dataEntrances_lastyear});
								}								
								
								for (var i = 1; i < mdate.length; i ++) {
									if(mEntrances[i] > maximumData)maximumData=mEntrances[i];
								}
							}
							if(btn == "bounceRate"){
								if($("#check_today").is(":checked")){
									data.push({label: "이탈율(현재)", data:dataBounceRate});
								}
								
								if($("#check_monthAgo").is(":checked")){
									data.push({label: "이탈율(한달 전)", data:dataBounceRate_lastmonth});
								}
								
								if($("#check_yearAgo").is(":checked")){
									data.push({label: "이탈율(일년 전)", data:dataBounceRate_lastyear});
								}								
								
								for (var i = 1; i < mdate.length; i ++) {
									if(mBounceRate[i] > maximumData)maximumData=mBounceRate[i];									
								}
							}
							
							$.plot("#chartCanvas", data, {
								xaxis: { 
									mode: "time",
									panRange: [new Date(mdate[1]), new Date(mdate[mdate.length-1])]
								},
								yaxis:{
									panRange: [0, maximumData + maximumData/8]
								},
								series: {
									lines: { show: true },
									points: { show: true }
								},
								zoom:{
									interactive:true
								},
								pan:{
									interactive:true
								},
								grid: {
									hoverable: true,
									clickable: true
								}
							});
						}
						
						function onCheckboxChecked(){
							onChartButtonClicked(btnMode);
						}
						
						function floatCheck(obj){
							 var num_check=/^([0-9]*)[\.]?([0-9])?$/;
								if(!num_check.test(obj)){
								return false;
							}
							return true;
						}
						
						$("<div id='tooltip'></div>").css({
							position: "absolute",
							display: "none",
							border: "1px solid #fdd",
							padding: "2px",
							"background-color": "#fee",
							opacity: 0.80
						}).appendTo("body");
						
						$("#chartCanvas").bind("plothover", function (event, pos, item) {
				
							if (item) {
								var y = item.datapoint[1];
								if(!floatCheck(y))y=y.toFixed(2);
			
								$("#tooltip").html(item.series.label + ":" + y)
									.css({top: item.pageY+5, left: item.pageX+5})
									.fadeIn(200);
							} else {
								$("#tooltip").hide();
							}
							
						});
						
						$(document).ready(function() {							
							onCheckboxChecked();
						});
				
						</script>
						 
					</div>
					</c:otherwise>				
				</c:choose>

		</div>
	</div>
		
	<script>
	
	function excel(){
		 document.frm.action = "/excel";
		 document.frm.excel_data.value = document.getElementById("excel_body").outerHTML;
		 document.frm.submit();
	}
	
	$(document).ready(function(){
		
	  $(".navbar a, footer a[href='#myPage']").on('click', function(event) {
	    // Make sure this.hash has a value before overriding default behavior
	    if (this.hash !== "") {
	      // Prevent default anchor click behavior
	      event.preventDefault();
	
	      // Store hash
	      var hash = this.hash;
	
	      // Using jQuery's animate() method to add smooth page scroll
	      // The optional number (900) specifies the number of milliseconds it takes to scroll to the specified area
	      $('html, body').animate({
	        scrollTop: $(hash).offset().top
	      }, 900, function(){
	   
	        // Add hash (#) to URL when done scrolling (default click behavior)
	        window.location.hash = hash;
	      });
	    }
	  });
	});
	</script>
</body>
</html>