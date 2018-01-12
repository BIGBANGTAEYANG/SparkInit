
<%
String syscontextPath = "http://"  +  request.getServerName()  +  ":"  +  request.getServerPort()  +  request.getContextPath();
String contextPath = request.getServerName();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<script type="text/javascript">
var QDevelopDomain = '<%=syscontextPath%>';
var contextIp = '<%=contextPath%>';
</script>
<link href="<%=syscontextPath %>/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
<link href="<%=syscontextPath %>/css/font-awesome.css?v=4.4.0" rel="stylesheet">
<link href="<%=syscontextPath %>/css/animate.css" rel="stylesheet">
<link href="<%=syscontextPath %>/css/style.css?v=4.1.0" rel="stylesheet">
<link href="<%=syscontextPath %>/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">

<script src="<%=syscontextPath %>/js/jquery.min.js?v=2.1.4"></script>
<script src="<%=syscontextPath %>/js/vue.min.js?v=2.4.4"></script>
<script src="<%=syscontextPath %>/js/bootstrap.min.js?v=3.3.6"></script>

<script src="<%=syscontextPath %>/js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script src="<%=syscontextPath %>/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

<script src="<%=syscontextPath %>/js/plugins/layer/layer.min.js"></script>
<script src="<%=syscontextPath %>/js/plugins/pace/pace.min.js"></script>
<script src="<%=syscontextPath %>/js/plugins/validate/jquery.validate.min.js"></script>
<script src="<%=syscontextPath %>/js/plugins/validate/messages_zh.min.js"></script>
<script src="<%=syscontextPath %>/js/plugins/bootstrap-table/bootstrap-table.min.js"></script>
<script src="<%=syscontextPath %>/js/plugins/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>
<script src="<%=syscontextPath %>/js/content.js?v=1.0.0"></script>
<script src="<%=syscontextPath %>/js/plugins/sweetalert/sweetalert.min.js"></script>
<script src="<%=syscontextPath %>/js/common/common.js"></script>
<script type="text/javascript">
// Ajax全局函数，控制Ajax请求Session超时情况
$.ajaxSetup({
	contentType:"application/x-www-form-urlencoded;charset=utf-8",
	complete:function(xhr,textStatus) {
			var sessionstatus = xhr.getResponseHeader("sessionstatus");
			if (sessionstatus == "timeout") {
				window.location.href = QDevelopDomain+"/login.jsp";
			}
			// 权限检查未通过
			if (sessionstatus == "auth") {
				window.location.href = QDevelopDomain+"/authstip.html";
			}
	}
});
//禁用Backspace
$(document).keydown(function(e) {
	var keyEvent;
	if (e.keyCode == 8) {
		var d = e.srcElement||e.target;
		if (d.tagName.toUpperCase() == 'INPUT' || d.tagName.toUpperCase() == 'TEXTAREA') {
			keyEvent = d.readOnly||d.disabled;
		} else {
			keyEvent = true;
		}
	} else {
		keyEvent = false;
	}
	if (keyEvent) {
		e.preventDefault();
	}
});
//禁用鼠标右键
$(document).bind('contextmenu',function() {return false;});
//禁用复制
//$(document).bind('selectstart',function() {return false;});

// 解决IE8不支持数组indexOf方法（原理：如果数组不支持indexOf，则添加这个方法）
if (!Array.prototype.indexOf) {
	Array.prototype.indexOf = function(elt) {
		var len = this.length >>> 0;
		var from = Number(arguments[1]) || 0;
		from = (from < 0) ? Math.cell(from) : Math.floor(from);
		if (from < 0) {
			from += len;
		}
		for (; from < len;from ++) {
			if (from in this && this[from] === elt) {
				return from;
			}
		}
		return -1;
	};
}
</script>
