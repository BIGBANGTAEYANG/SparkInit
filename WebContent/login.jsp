<%@ page language="java" contentType="text/html; charset=UTF-8"   pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="/page/import/loginPlugin.jsp" flush="true"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<title>SparkHUI</title>
<script>
if(window.top !== window.self){ window.top.location = window.location;}
<%-- $(function(){
	$("body").bind('keyup',function(event) {  
		if(event.keyCode==13){  
			subform();
		}     
	}); 
});

function subform() {
	var ss = $("#loginForm").valid();
	if(ss){
		var usercode = $('#usercode').val();
		var passWord =	$('#password').val();
		$.ajax({
			type:"post",
			url:"<%=syscontextPath%>/app/sys/auth/login",
			data:{userCode:usercode,passWord:passWord},
			dataType:"json",
			success : function(result) {
				if (result.code == 1) {
					window.location.href= '<%=syscontextPath%>/app/sys/auth/main';
				} else {
					//$.messager.alert("提示信息",result.message, "error");
					$('#usercode').val("");
					$('#password').val("");
				}
			},
			error : function() {
				//$.messager.alert("提示","登录失败！", "error");
			}
			
		});
	}
	
} --%>
</script>
</head>
<body class="gray-bg">
    <div class="middle-box text-center loginscreen  animated fadeInDown">
        <div>
            <div>
                <h2 class="logo-name">Spark H+</h2>
            </div>
            <h3>欢迎使用 Spark H+</h3>

            <form class="m-t" id="loginForm">
                <div class="form-group">
                    <input type="text" class="form-control" id="usercode" name="usercode" placeholder="用户名" required="">
                </div>
                <div class="form-group">
                    <input type="password" class="form-control" id="password" name="password" placeholder="密码" required="">
                </div>
                <input type="button" class="btn btn-primary block full-width m-b" onclick="subform()" value="登 录"></input>
            </form>
        </div>
    </div>
</body>
</html>