<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.concurrent.ConcurrentHashMap" %>
<!--
	Author: W3layouts
	Author URL: http://w3layouts.com
	License: Creative Commons Attribution 3.0 Unported
	License URL: http://creativecommons.org/licenses/by/3.0/
-->
<%--
Created by IntelliJ IDEA.
User: Adam
Date: 2019/7/22
Time: 14:05
To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="utf-8" %>
<c:set var="baseurl" value="${pageContext.request.contextPath}"></c:set>
<html lang="zxx">

<head>
    <title>Login-BankSystem</title>
    <!-- Meta-Tags -->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta charset="utf-8">
    <meta name="keywords" content="Quick Signin & Signup form a Responsive Web Template, Bootstrap Web Templates, Flat Web Templates, Android Compatible Web Template, Smartphone Compatible Web Template, Free Webdesigns for Nokia, Samsung, LG, Sony Ericsson, Motorola Web Design">
    <!-- //Meta-Tags -->
    <!-- Index-Page-CSS -->
    <link rel="stylesheet" href="${baseurl}/css/style.css" type="text/css" media="all">
    <!-- onlinefonts -->
    <link href="//fonts.googleapis.com/css?family=Dosis:200,300,400,500,600,700,800" rel="stylesheet">
    <!-- //Custom-Stylesheet-Links -->
</head>

<body>
    <header>
        <h1 class="title-agile text-center">Welcome to BankSystem！</h1>
    </header>
    <!-- //header -->
    <section class="login-wrap">
        <div class="main_w3agile">
            <input id="tab-1" type="radio" name="tab" class="sign-in" checked>
            <label for="tab-1" class="tab">Login</label>
            <input id="tab-2" type="radio" name="tab" class="sign-up">
            <label for="tab-2" class="tab">Register</label>
            <div class="login-form">
                <!-- signin form -->
                <div class="signin_wthree">
                    <form method="post" action="login.do">
                        <div class="group">
                            <label for="user" class="label">UserID</label>
                            <input id="user" type="text" class="input" name="userid" value="${cookie.userid.value}" required>
                        </div>
                        <div class="group">
                            <label for="pass" class="label">Password</label>
                            <input id="pass" type="password" class="input" name="password" value="${cookie.password.value}" data-type="password" required>
                        </div>

                        <div class="group">
                            <input id="check" value="1" name="isRemember" type="checkbox" class="check" checked>
                            <label for="check">
                                <span class="icon"></span>Remember me</label> 　　　
                        </div>

                        <div class="group">

                            <input type="submit" class="button" value="Sign In">
                        </div>

                        <div class="group" align="center">
                            <label class="switch" for="admin">
                                <input id="admin" type="checkbox" name="admin" value="1">
                                <span class="slider round"></span>Administrator</label>
                        </div>

                        <div class="hr"></div>
                        <div class="foot-lnk">
                            <h2><a href="#">Forgot Password?</a></h2>
                        </div>
                    </form>
                </div>
                <!-- //signin form -->
                <!-- signup form -->
                <div class="signup_wthree">
                    <form method="post" action="register.do">
                        <div class="group">
                            <label for="user1" class="label">Username</label>
                            <input id="user1" name="username" type="text" class="input" required>
                        </div>
                        <div class="group">
                            <label for="password1" class="label">Password</label>
                            <input id="password1" name="password1" type="password" class="input" data-type="password" required>
                        </div>
                        <div class="group">
                            <label for="password2" class="label">Re-Enter Password</label>
                            <input id="password2" name="password2" type="password" class="input" data-type="password" required>
                        </div>

                        <div class="group">
                            <label for="email" class="label">Email Address</label>
                            <input id="email" type="email" class="input" required>
                        </div>

                        <div class="group">

                            <input type="submit" class="button" value="Sign Up">
                        </div>
                        <div class="hr"></div>
                        <div class="foot-lnk">
                            <label for="tab-1">Already Member? </label>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </section>
    <footer>
        <div class="copy-wthree text-center">
            <p>2019 BankSystem.
            </p>
        </div>
    </footer>
</body>

</html>