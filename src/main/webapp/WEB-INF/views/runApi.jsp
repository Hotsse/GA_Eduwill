<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.text.DecimalFormat" %>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Eduwill Ngene>상품 정보</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="/resources/css/style.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<script type="text/javascript" src="/resources/js/jquery.flot.js"></script>
<script type="text/javascript" src="/resources/js/jquery.flot.time.js"></script>
<script type="text/javascript" src="/resources/js/jquery.flot.navigate.js"></script>
</head>
<body id="myPage" data-spy="scroll" data-target=".navbar" data-offset="60">

	<!-- top nav -->
	<nav class="navbar navbar-inverse">
	<div class="container-fluid">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
				<span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="#">상품 관리</a>
		</div>
		<div class="collapse navbar-collapse" id="myNavbar">
			<ul class="nav navbar-nav">
				<li><a href="#Calendar">기간 선택</a></li>
				<li><a href="#Information">상품 정보</a></li>
				<li><a href="#Chart">통계</a></li>
			</ul>
		</div>
	</div>
	</nav>
	
	<!-- main conatiner -->
	<div class="container-fluid text-center">
		<div class="row content">
		
			<!-- 기간 선택 -->
			<div class="col-sm-12 text-left" id="Calendar">
				<h3 class="page-header">
					기간 선택<small> 시작일 / 종료일</small>
				</h3>
				<form action="/analytics" method="get">
					<input type="hidden" name="seq" value=${seq } />
					<div class="col-sm-5 text-left">
						시작일 : <input type="date" class="form-control" name="startDate" value=${param.startDate }>
					</div>
					<div class="col-sm-5 text-left">
						종료일 : <input type="date" class="form-control" name="endDate" value=${param.endDate }>
					</div>
					<div class="col-sm-2 text-center">
						<br /> <input type="submit" class="btn btn-primary btn-block" value="검색" />
					</div>
				</form>
			</div>
			
			<!-- 상품 정보 -->
			<div class="col-sm-12 text-left" id="Information">

				<h3 class="page-header">
					상품 정보<small> 수치 확인</small>
				</h3>

				<div id="resultDisplay"></div>
				
				<c:choose>
				
					<c:when test="${result eq null}"> <!-- if(result == null) {...} -->
					<!-- 검색 결과가 존재하지 않음 -->
						검색 기간을 설정해 주십시오.<br/></div>
					</c:when>
					
					
					<c:otherwise> <!-- else {...} -->
					<!-- 검색 결과가 존재함 -->
						<table class="table table-hover">
							<thead>
								<tr>
									<th>상품코드</th>
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
										<td>${inform.pageCode}</td>
										<td>${inform.pageviews}</td>
										<td>${inform.uniquePageviews}</td>
										<td>${inform.sessions}</td>
										<td>${inform.entrances}</td>
										<td>${inform.bounceRate}</td>
										<!-- 
										<td>${inform.totalEvents}</td>
										<td>${inform.eventRate }</td>
										 -->
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					
					<!-- 통계(차트확인) -->
					<div class="col-sm-12 text-left" id="Chart">
						<h3 class="page-header">
							통계<small> 차트 확인</small>
						</h3>
						
						<div id="chartCanvas" class="demo-placeholder" style="display:inline-block; height: 400px; width: 85%;"></div>
						<div style="display:inline-block; height:400px; width:13%; margin-left:1%; vertical-align: text-bottom;">
							<h3 class="page-header"><small>차트 목록</small></h3><br/>
							<input type="checkbox" id="check_pageviews" onClick="onCheckboxChecked()" /> 페이지뷰<br/>
							<input type="checkbox" id="check_uniquePageviews" onClick="onCheckboxChecked()" /> 순 페이지뷰<br/>
							<input type="checkbox" id="check_sessions" onClick="onCheckboxChecked()" /> 세션 수<br/>
							<input type="checkbox" id="check_entrances" onClick="onCheckboxChecked()" /> 방문자 수<br/>
							<input type="checkbox" id="check_bounceRate" onClick="onCheckboxChecked()" /> 이탈율
						</div>
		
						<!-- declare and initialize variables -->
						<script>
							var mdate = ["1999-01-01"];
							var mpageView = [0];
							var mUniquePageviews = [0];
							var mSessions = [0];
							var mEntrances = [0];
							var mBounces = [0];
							var mTotalEvents = [0];
							var mBounceRate = [0.0];
							var mEventRate = [0.0];
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
						
						<!-- input data in chart array -->
						<script>
						
							var dataPageviews = [];
							var dataUniquePageviews = [];
							var dataSessions = [];
							var dataEntrances = [];
							var dataBounceRate = [];
							
							for (var i = 1; i < mdate.length; i ++) {
								dataPageviews.push([new Date(mdate[i]), mpageView[i]]);
								dataUniquePageviews.push([new Date(mdate[i]), mUniquePageviews[i]]);
								dataSessions.push([new Date(mdate[i]), mSessions[i]]);
								dataEntrances.push([new Date(mdate[i]), mEntrances[i]]);
								dataBounceRate.push([new Date(mdate[i]), mBounceRate[i]]);
							}
						
						</script>						
						
						<!-- jQuery.Flot Script -->
						<script type="text/javascript">						
						var data = [];
						
						function onCheckboxChecked(){
							data = [];
							
							var maximumData=0.0;
							
							if($("#check_pageviews").is(":checked")){
								data.push({label: "페이지뷰", data:dataPageviews});
								for (var i = 1; i < mdate.length; i ++) {
									if(mpageView[i] > maximumData)maximumData=mpageView[i];
								}
							}
							if($("#check_uniquePageviews").is(":checked")){
								data.push({label: "순 페이지뷰", data:dataUniquePageviews});
								for (var i = 1; i < mdate.length; i ++) {
									if(mUniquePageviews[i] > maximumData)maximumData=mUniquePageviews[i];
								}
							}
							if($("#check_sessions").is(":checked")){
								data.push({label: "세션 수", data:dataSessions});
								for (var i = 1; i < mdate.length; i ++) {
									if(mSessions[i] > maximumData)maximumData=mSessions[i];
								}
							}
							if($("#check_entrances").is(":checked")){
								data.push({label: "방문자 수", data:dataEntrances});
								for (var i = 1; i < mdate.length; i ++) {
									if(mEntrances[i] > maximumData)maximumData=mEntrances[i];
								}
							}
							if($("#check_bounceRate").is(":checked")){
								data.push({label: "이탈율", data:dataBounceRate});
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