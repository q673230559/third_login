<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%
	pageContext.setAttribute("appctx", application.getContextPath());
%>
<!DOCTYPE html">
<html>
<head>
<meta charset="utf-8">
<title>授权成功</title>
<script type="text/javascript">
	var type = "${param.type}";
	var parentWindow = (type === "window") ? window.opener : window.parent;
	parentWindow.location.href = "${appctx}/user_page";
	window.close();
</script>
</head>
<body>
</body>
</html>