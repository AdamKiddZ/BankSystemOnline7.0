<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="baseurl" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>BankSystem</title>
<meta name="keywords" content="" />
<meta name="description" content="" />
<link href="${baseurl}/css/templatemo_style.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" href="${baseurl}/css/coda-slider.css" type="text/css" media="screen" charset="utf-8" />

<script src="${baseurl}/js/jquery-1.2.6.js" type="text/javascript"></script>
<script src="${baseurl}/js/jquery.scrollTo-1.3.3.js" type="text/javascript"></script>
<script src="${baseurl}/js/jquery.localscroll-1.2.5.js" type="text/javascript" charset="utf-8"></script>
<script src="${baseurl}/js/jquery.serialScroll-1.2.1.js" type="text/javascript" charset="utf-8"></script>
<script src="${baseurl}/js/coda-slider.js" type="text/javascript" charset="utf-8"></script>
<script src="${baseurl}/js/jquery.easing.1.3.js" type="text/javascript" charset="utf-8"></script>
<script src="${baseurl}/js/jquery-3.3.1.min.js" type="text/javascript" charset="utf-8"></script>

<script type="text/javascript">

    //检查浏览器是否支持ajax
    //var xhr=new XMLHttpRequest();
    function getajaxHttp() {
        var xmlHttp;
        try {
            // Firefox, Opera 8.0+, Safari
            xmlHttp = new XMLHttpRequest();
        } catch (e) {
            // Internet Explorer
            try {
                xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
            } catch (e) {
                try {
                    xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
                } catch (e) {
                    alert("您的浏览器不支持AJAX，请切换至更高版本的浏览器！");
                    return false;
                }
            }
        }
        xmlHttp.withCredentials=true;
        return xmlHttp;
    }

    //检查该用户ID是否存在
    function checkUser(T,Ttip){
        // var userID=document.getElementById("transferUserAccount").value;
        if(T.value!=""){
            var xmlhttp =getajaxHttp();
            // if(xmlhttp!=false) {
                xmlhttp.onreadystatechange =function(){
                    if (xmlhttp.readyState==4 && xmlhttp.status==200){
                        if(xmlhttp.responseText=="true"){
                            // 用户存在
                            Ttip.innerHTML="";
                            return true;
                        }else{
                            // 用户不存在
                            // var Ttip=document.getElementById("transferUserAccountTip");
                            Ttip.innerHTML="不存在";
                            return false;
                        }
                    }
                } //响应函数
                xmlhttp.open("GET","checkUser.do?username="+T.value, true);   //设置访问的页面
            // alert("666");
                xmlhttp.send(null);  //执行访问
            // }
        }

    }

    //验证用户输入的金额格式是否符合要求
    function checkAmount(T,Ttip){
        if(T.value==""){
            return false;
        }
        //匹配正数的正则表达式（正浮点数、正整数，但不包括正负号）
        // var moneyFormat=/\d+|[1-9]\d*\.\d*|0\.\d*[1-9]\d*/i;
        var moneyFormat=/^\d+(\.\d+)?$/i;
        if(!moneyFormat.test(T.value)){
            Ttip.innerHTML="输入错误";
            return false;
        }
        Ttip.innerHTML="";
        return true;
    }

    //提交取款交易
    function submitWithdraw(T,Ttip) {
        if(checkAmount(T,Ttip)){
            var xmlhttp=getajaxHttp();
            if(xmlhttp!=false) {
                xmlhttp.onreadystatechange =function(){
                    if(xmlhttp.readyState==4&&xmlhttp.status==200){
                        alert(decodeURI(xmlhttp.responseText));
                        T.value="";
                        Ttip.innerHTML="";
                    }
                } //响应函数
                xmlhttp.open("GET","withdraw.do?amount="+T.value, true);   //设置访问的页面
                xmlhttp.send(null);  //执行访问
            }
        }
    }

    //提交存款交易
    function submitDeposit(T,Ttip) {
        if(checkAmount(T,Ttip)){
            var xmlhttp=getajaxHttp();
            if(xmlhttp!=false) {
                xmlhttp.onreadystatechange=function(){
                    if(xmlhttp.readyState==4&&xmlhttp.status==200){
                        alert(decodeURI(xmlhttp.responseText));
                        T.value="";
                        Ttip.innerHTML="";
                        // if(xmlhttp.responseText=="true"){
                        //     alert("交易成功！页面即将刷新……");
                        //     // location.reload(true);
                        //     location.replace("home.jsp");
                        //     return true;
                        // }else{
                        //     alert("交易失败！"+xmlhttp.responseText);
                        //     return false;
                        // }
                    }
                } //响应函数
                xmlhttp.open("GET","deposit.do?amount="+T.value, true);   //设置访问的页面
                xmlhttp.send(null);  //执行访问
            }
        }
    }

    //提交转账交易
    function submitTransfer(T1,T1tip,T2,T2tip){
        // if(checkUser(T1,T1tip)&&checkAmount(T2,T2tip)){
            var xmlhttp=getajaxHttp();
            if(xmlhttp!=false) {
                xmlhttp.onreadystatechange =function(){
                    if(xmlhttp.readyState==4&&xmlhttp.status==200){
                        alert(decodeURI(xmlhttp.responseText));
                        T1.value="";
                        T2.value="";
                        T1tip.innerHTML="";
                        T2tip.innerHTML="";
                    }
                } //响应函数
                xmlhttp.open("GET","transfer.do?receiverID="+T1.value+"&amount="+T2.value, true);   //设置访问的页面
                xmlhttp.send(null);  //执行访问
            }
        // }
    }


    //冻结账户
    function lock(userid) {
        var xmlhttp=getajaxHttp();
        if(xmlhttp!=false) {
            xmlhttp.onreadystatechange =function(){
                if(xmlhttp.readyState==4&&xmlhttp.status==200){
                    //解码返回的字符串信息
                    alert(decodeURI(xmlhttp.responseText));
                    window.location.replace("getUserList.do");
                }
            } //响应函数
            xmlhttp.open("GET","lock.do?userid="+userid, true);   //设置访问的页面
            xmlhttp.send(null);  //执行访问
        }
    }
    //解冻账户
    function unlock(userid) {
        var xmlhttp=getajaxHttp();
        if(xmlhttp!=false) {
            xmlhttp.onreadystatechange =function(){
                if(xmlhttp.readyState==4&&xmlhttp.status==200){
                    //解码返回的字符串信息
                    alert(decodeURI(xmlhttp.responseText));
                    window.location.replace("getUserList.do");
                }
            } //响应函数
            xmlhttp.open("GET","unlock.do?userid="+userid, true);   //设置访问的页面
            xmlhttp.send(null);  //执行访问
        }
    }


    setInterval(function() {
        $("#home").load(location.href+" #home>*","");
    }, 1000);
</script>
</head>

<body>

<div id="slider">
	
    <div id="templatemo_sidebar">
    	<div id="templatemo_header">
        	<a href="#"><img src="${baseurl}/images/BankIcon2.png" alt="BankSystem" /></a>
        </div> <!-- end of header -->
        
        <ul class="navigation">
            <li><a href="inquiry.do" onclick="function refresh(){window.location.reload()}">Home<span class="ui_icon home"></span></a></li>
            <li><a href="#withdraw">Withdraw<span class="ui_icon withdraw"></span></a></li>
            <li><a href="#deposit">Deposit<span class="ui_icon deposit"></span></a></li>
            <li><a href="#transfer">Transfer<span class="ui_icon transfer"></span></a></li>
            <li><a href="getLogList.do">Log<span class="ui_icon log"></span></a></li>

            <!-- 管理员才可以看到 -->
            <c:if test="${sessionScope.user.adminGrant ==1}">
            <li><a href="getUserList.do">Administration<span class="ui_icon administration"></span></a></li>
            </c:if>
        </ul>
    </div> <!-- end of sidebar -->

	<div id="templatemo_main">
    	<ul id="social_box" style="vertical-align: center">
            <li><a href="#home" title="UserID：${sessionScope.user.id}"><img src="${baseurl}/images/userid.png" alt="UserID" /></a></li>
            <li><a href="#home" title="Username：${sessionScope.user.username}"><img src="${baseurl}/images/username.png" alt="Username" /></a></li>

            <!--是否被冻结-->
            <li><a href="#home" title="是否被冻结：${sessionScope.user.flag >0}"><img src="${baseurl}/images/locked.png" alt="是否账户冻结" /></a></li>
            <!--是否管理员-->
            <li><a href="#home" title="是否管理员：${sessionScope.user.adminGrant >0}"><img src="${baseurl}/images/admin.png" alt="technorati" /></a></li>
            <li><a href="logout.do" title="点击退出当前登录"><img src="${baseurl}/images/logout.png" alt="myspace" /></a></li>
<!--            <li>UserID:dssf</li>-->
<!--            <li>Username:fdsfdsfds</li>-->
<!--            <li>Locked:fsfdsfdff</li>-->
<!--            <li>UserID:fsssssss</li>-->
        </ul>
        
        <div id="content">
        
        <!-- scroll -->
        
        	
            <div class="scroll">
                <div class="scrollContainer">
                
                    <div class="panel" id="home">
                        <h1>欢迎登录银行系统, ${sessionScope.user.username}.</h1>
                        <c:if test="${sessionScope.user.flag > 0}">
                            <p style="color: #E53935">您当前账户处于冻结状态，无法转账、取款等！</p>
                        </c:if>
                        <p>现在开始您的理财生涯！</p>
                        <p>您账户当前余额为：${sessionScope.user.balance}</p>
                        <p>点击 <a href="#">这里</a> 进入 <a href="#">个人中心</a>.</p>
                    </div> <!-- end of home -->


                    <div class="panel" id="withdraw">
                        <h1>Withdraw</h1>
                        <p>请输入您要取款的金额</p>
                        <div class="contact_form">
                            <form class="aform" method="post" name="contact">

                                <label>金额：<span id="withdrawAmountTip" style="color:  #E53935"></span></label><input type="text" onblur="checkAmount(this,document.getElementById('withdrawAmountTip'))" id="withdrawAmount" class="required input_field" />
                                <div class="cleaner_h10"></div>

<!--                                <label for="email">Your Email:</label> <input type="text" name="email" class="validate-email required input_field" />-->
<!--                                <div class="cleaner_h10"></div>-->

                                <label for="text">Message:</label> <textarea name="message" rows="0" cols="0" class="required"></textarea>
                                <div class="cleaner_h10"></div>

                                <input type="button" class="submit_btn" onclick="submitWithdraw(document.getElementById('withdrawAmount'),document.getElementById('withdrawAmountTip'))" name="submit" value="Send" />
                                <input type="reset" class="submit_btn" name="reset" value="Reset" />

                            </form>
                        </div>
                    </div>

                    <div class="panel" id="deposit">
                        <h1>Deposit</h1>
                        <p>请输入您要存款的金额</p>
                        <div class="contact_form">
                            <form class="aform" method="post" name="contact">

                                <label for="depositAmount">金额:<span id="depositAmountTip" style="color:  #E53935"></span></label> <input type="text" onblur="checkAmount(this,document.getElementById('depositAmountTip'))" id="depositAmount" class="required input_field" />
                                <div class="cleaner_h10"></div>

<!--                                <label for="email">Your Email:</label> <input type="text" name="email" class="validate-email required input_field" />-->
<!--                                <div class="cleaner_h10"></div>-->

                                <label for="text">Message:</label> <textarea id="text" name="text" rows="0" cols="0" class="required"></textarea>
                                <div class="cleaner_h10"></div>

<!--                                <input type="submit" class="submit_btn" name="submit" id="submit" value="Send" />-->
<!--                                <input type="reset" class="submit_btn" name="reset" id="reset" value="Reset" />-->
                                <input type="button" class="submit_btn" onclick="submitDeposit(document.getElementById('depositAmount'),document.getElementById('depositAmountTip'))" name="submit" value="Send" />
                                <input type="reset" class="submit_btn"  name="reset" value="Reset" />

                            </form>
                        </div>
                    </div>

                    <div class="panel" id="transfer">
                        <h1>Transfer</h1>
                        <p>请输入您要转账的账户名及金额</p>
                        <div class="contact_form">
                            <form class="aform" method="post" name="contact">

                                <label for="transferUserAccount">转账给:<span id="transferUserAccountTip" style="color:  #E53935"></span></label> <input type="text" onblur="checkUser(this,document.getElementById('transferUserAccountTip'))" id="transferUserAccount" name="userid" class="required input_field" />
                                <div class="cleaner_h10"></div>

                                <label for="transferAmount">金额:<span id="transferAmountTip" style="color:  #E53935"></span></label> <input type="text" onblur="checkAmount(this,document.getElementById('transferAmountTip'))" id="transferAmount" name="email" class="validate-email required input_field" />
                                <div class="cleaner_h10"></div>

                                <label for="text">Message:</label> <textarea name="text" rows="0" cols="0" class="required"></textarea>
                                <div class="cleaner_h10"></div>

                                <input type="button" class="submit_btn" onclick="
                                submitTransfer(
                                    document.getElementById('transferUserAccount'),
                                    document.getElementById('transferUserAccountTip'),
                                    document.getElementById('transferAmount'),
                                    document.getElementById('transferAmountTip'))" name="submit" value="Send" />
                                <input type="reset" class="submit_btn" name="reset" value="Reset" />

                            </form>
                        </div>

                    </div>

                    <div class="panel" id="listLogs">
                        <h1>User logs</h1>
                        <div class="list_container">
                            <c:if test="${!empty sessionScope.logs}">
                                <c:forEach items="${sessionScope.logs}" var="log" varStatus="varStatus">
                                <div class="list_box">
                                    <img src="${baseurl}/images/transfer/image_01.jpg" alt="${varStatus.count}" />
                                    <h4>操作类型：${log.logtype}</h4>
                                    <p>操作金额：${log.amount}</p>
                                    <p>操作时间：${log.logtime}</p>
<%--                                    <div class="btn_more"><a href="#">Visit <span>&raquo;</span></a></div>--%>
                                    <div class="cleaner"></div>
                                </div>
                                </c:forEach>
                            </c:if>
                            <c:if test="${empty sessionScope.logs}">
                            <div class="list_box">
                                <p>当前还没有操作记录！</p>
                            </div>
                            </c:if>
                            <div class="cleaner"></div>
                        </div>
                    </div>

                    <div class="panel" id="listUsers">
                        <h1>Administrator to manage</h1>
                        <div class="list_container">
                            <c:if test="${!empty sessionScope.users}">
                                <c:forEach items="${sessionScope.users}" var="useritem" varStatus="varStatus">
                                    <div class="list_box">
                                        <img src="${baseurl}/images/transfer/Account.png" alt="${varStatus.count}" />
                                        <h4>用户ID：${useritem.id}</h4>
                                        <p>用户名：${useritem.username}</p>
                                        <p>账户状态：
                                            <c:if test="${useritem.flag > 0}" var="flag">
                                                已冻结　　　<button type="button" onclick="unlock(${useritem.id})">解冻</button>
                                            </c:if>
                                            <c:if test="${not flag}">
                                                未冻结　　　<button type="button" onclick="lock(${useritem.id})">冻结</button>
                                            </c:if>
                                        </p>
                                        <p>账户余额：${useritem.balance}</p>
                                        <p>注册时间：${useritem.regtime}</p>
                                        <div class="cleaner"></div>
                                    </div>
                                </c:forEach>
                            </c:if>
                            <c:if test="${empty sessionScope.users}">
                                <div class="list_box">
                                    <p>当前还没有用户记录！</p>
                                </div>
                            </c:if>
                            <div class="cleaner"></div>
                        </div>
                    </div>
                
                </div>
			</div>
            
        <!-- end of scroll -->
        
        </div> <!-- end of content -->
        
        <div id="templatemo_footer">

            2019 BankSystem
        
        </div> <!-- end of templatemo_footer -->
    
    </div> <!-- end of main -->
</div>
</body>
</html>