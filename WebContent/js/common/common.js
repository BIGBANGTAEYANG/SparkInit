/**
 * 权限检查
 * @param code
 */
function checkHideFunction(code){
	$.getJSON(QDevelopDomain+"/app/sys/auth/viewFunctionAuths?r="+Math.floor(Math.random()*100000),{code:code},function(data){
		$.each(data,function(i,item){
			$("#__"+item).show();
		});
	});
}


