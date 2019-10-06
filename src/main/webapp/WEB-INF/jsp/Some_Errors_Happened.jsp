<%--
  Created by IntelliJ IDEA.
  User: HP
  Date: 2019/9/24
  Time: 10:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=utf-8" pageEncoding="UTF-8" %>
<meta charset="utf-8">
<html>
<head>
    <title>错误！！！</title>
</head>
<body>
<h1 align="center">
    发生了错误，3s后跳转至首页！！！
</h1>
</body>
<%
    response.setHeader("refresh","3;URL=index.html");
    session.invalidate();
%>
</html>
