<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%
	pageContext.setAttribute("appctx", application.getContextPath());
%>
<!DOCTYPE html">
<html>
<head>
<meta charset="utf-8">
<title>授权失败</title>
<link href="${appctx}/css/all.css" rel="stylesheet" type="text/css">
</head>
<body>
<div id="third_error">
${errMsg }
</div>
<script type="text/javascript">
	var type = "${type}";
	if ("self" === type) {
		setTimeout(function() {
			location.href = "${appctx}/login";
		}, 3000);
	} else if("window" === type) {
		setTimeout(function() {
			window.close();
		}, 3000);
	}
</script>
</body>
</html>