<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Eduwill Ngene</title>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
	<link rel="stylesheet" href="/resources/css/style.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>

	<!-- top nav -->
	<nav class="navbar navbar-inverse">
	<div class="container-fluid">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
				<span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="#">에듀윌</a>
		</div>
		<div class="collapse navbar-collapse" id="myNavbar">
			<ul class="nav navbar-nav">
				<li class="active"><a href="#">상품페이지</a></li>
			</ul>
			<ul class="nav navbar-nav navbar-right">
				<li><a href="#">로그인</li>
			</ul>
		</div>
	</div>
	</nav>
	
	<!-- main container -->
	<div class="container-fluid text-center">
		<div class="row content">
		
			<!-- lnb -->
			<div class="col-sm-2 sidenav" id="sidenav">
				<div class="list-group">
					<span class="list-group-item"><strong>상품</strong></span>
					<a href="/goods/" class="list-group-item active">상품페이지관리</a>
				</div>
				<div class="list-group">
					<span class="list-group-item"><strong>강사</strong></span>
					<a href="/teacher/" class="list-group-item active">교수페이지관리</a>
				</div>
			</div>
			
			<!-- main contents -->
			<div class="col-sm-9 text-left">
				
				<h3 class="page-header">
					상품 목록<small> 리스트</small>
				</h3>
				<div class="table-responsive">
					<table id="tableDefault" class="table table-bordered table-hover" cellspacing="0" width="100%">
						<thead>
							<tr>
								<th>상품명</th>
								<th>상품코드</th>
								<th>관리</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="goodsVO" items="${list}" varStatus="status">
								<tr>
									<td>${goodsVO.name }</td>
									<td>${goodsVO.code }</td>
									<td><button type="button" class="btn btn-default btn-sm" onclick="openAnalyticsPage('${goodsVO.code}', '${goodsVO.idx }')">관리</button></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
	
		</div>
	</div>
	
	<script>
	function openAnalyticsPage(seq, idx){
		window.open("/goods/analytics?seq=" + seq + "&idx=" + idx + "&delim=G","상품 관리","width=1200, height=800, toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizeable=yes, left=150, top=150");
	}
	
	</script>

</body>
</html>
