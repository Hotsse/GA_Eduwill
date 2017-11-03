<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

	<form name="frm" method="post">
		<input type="hidden" name="excel_data" />
	</form>

	<table id="excel_body">
		<caption>list</caption>
		<thead>
			<tr>
				<th>pageTitle</th>
				<th>pageCode</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="mercVO" items="${list}" varStatus="status">
				<tr>
					<td>${mercVO.name }</td>
					<td>${mercVO.code }</td>
				</tr>
			</c:forEach>

		</tbody>
	</table>
	<br/>
	<button onclick="excel()">Export to Excel</button>
	
	<script type="text/javascript">
		function excel(){
	
			document.frm.action = "/excel";
			document.frm.excel_data.value = document
						.getElementById("excel_body").outerHTML;
			document.frm.submit();
	
		}
	</script>

</body>
</html>