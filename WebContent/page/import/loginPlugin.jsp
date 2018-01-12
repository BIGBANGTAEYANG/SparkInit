<%@include file="include.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<SCRIPT LANGUAGE="JavaScript">
if(window.top !== window.self){ window.top.location = window.location;}
$(function(){
	$("body").bind('keyup',function(event) {  
		if(event.keyCode==13){  
			subform();
		}     
	}); 
});

function subform() {
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

</SCRIPT>