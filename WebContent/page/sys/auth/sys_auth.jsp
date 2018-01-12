<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/page/import/include.jsp"%>
<link href="<%=syscontextPath%>/css/plugins/jqgrid/ui.jqgrid.css?0820"
	rel="stylesheet">
<link
	href="<%=syscontextPath%>/css/plugins/treeview/bootstrap-treeview.css"
	rel="stylesheet">
	<link href="<%=syscontextPath%>/css/plugins/iCheck/custom.css" rel="stylesheet">
	<link href="<%=syscontextPath%>/css/my.css" rel="stylesheet">
</head>
<body>
	<div class="jqGrid_wrapper">
		<table id="tb"></table>
		<div id="toolbar"></div>
	</div>
	<div class="modal inmodal" id="myAuth" tabindex="-1" role="dialog"
		aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content animated bounceInRight">
				<div class="modal-header">
					<button type="button" class="close" onclick="closeAuthmodal()">
						<span aria-hidden="true">&times;</span><span class="sr-only">关闭</span>
					</button>
					<h5 class="modal-title" id="authTitle"></h5>
				</div>
				<div class="modal-body">
					<form class="form-horizontal m-t" id="authForm">
						<input type="hidden" id="updateFlag" name="updateFlag">
						<div class="form-group">
							<label class="col-sm-3 control-label">权限代码：</label>
							<div class="col-sm-8">
								<input id="authcode" name="authcode" class="form-control"
									type="text" required="" aria-required="true"> <span
									class="help-block m-b-none"><i class="fa fa-info-circle"></i>
									对应页面中的功能按钮</span>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">权限名称：</label>
							<div class="col-sm-8">
								<input id="authname" name="authname" class="form-control" type="text"
									required="" aria-required="true">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">权限描述：</label>
							<div class="col-sm-8">
								<input id="des" name="des" class="form-control"
									type="text" >
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">是否有效：</label>
							<div class="col-sm-8">
                                 <label><input type="radio" value="1" checked name="status"> <i></i> 有效</label>
                                  <label><input type="radio" value="0" name="status"> <i></i> 无效</label>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-white"
						onclick="closeAuthmodal()">关闭</button>
					<button type="button" class="btn btn-primary" onclick="saveOrUpdateAuth()">保存</button>
				</div>
			</div>
		</div>

	</div>
</body>
<script
	src="<%=syscontextPath%>/js/plugins/jqgrid/i18n/grid.locale-cn.js"
	type="text/javascript"></script>
<script src="<%=syscontextPath%>/js/plugins/jqgrid/jquery.jqGrid.min.js"
	type="text/javascript"></script>
<script src="<%=syscontextPath%>/js/plugins/peity/jquery.peity.min.js"></script>
<script src="<%=syscontextPath%>/js/plugins/iCheck/icheck.min.js"></script>


<SCRIPT LANGUAGE="JavaScript">
	var firstVaild = true;
	function saveOrUpdateAuth() {
		$("#authForm").validate();
		var isSuccess = $("#authForm").valid();
		if (isSuccess) {
			// 验证通过，提交表单
			$.ajax({
				type : "post",
				url : QDevelopDomain + "/app/sys/auth/modify",
				datatype : 'json',
				data : $('#authForm').serialize(),
				success : function(data) {
					var _data = JSON.parse(data);
					if (_data.code == 1) {
						$("#myAuth").modal('hide');
						swal("操作成功", _data.message,"success");
						//刷新表单
						$("#tb").trigger("reloadGrid");
					} else {
						swal("操作失败", _data.message,"error");
					}
				},
				error : function() {
					swal("操作失败","","error");
				},
			});

		}
	}

	function deleteAuth(id) {
		swal({
	        title: "您确定要删除此菜单功能吗",
	        text: "删除后将无法恢复，请谨慎操作！",
	        type: "warning",
	        showCancelButton: true,
	        confirmButtonColor: "#DD6B55",
	        confirmButtonText: "删除",
	        closeOnConfirm: false
	    }, function () {
			$.ajax({
				type : "post",
				url : QDevelopDomain + "/app/sys/auth/delete",
				datatype : 'json',
				data : {authcode:id},
				success : function(data) {
					var _data = JSON.parse(data);
					if (_data.code == 1) {
						swal("操作成功",_data.message,"success");
						//刷新列表
						$("#tb").trigger("reloadGrid");
					} else {
						swal("操作失败",_data.message,"error");
					}
				},
				error : function() {
					swal("操作失败","","error");
				},
			});
	    });
	}

	function closeAuthmodal() {
		$("#myAuth").modal('hide');
	}
	
	function showAuthForm(data) {
		$('#authForm #authcode').val(data.authcode);
		$('#authForm #authname').val(data.authname);
		$('#authForm #des').val(data.des);
		$(":radio[name='status'][value='" + data.status + "']").prop("checked", "checked");
		$("#authTitle").html('编辑菜单');
		$("#myAuth").modal('show');
	}

	$(function() {
		$.jgrid.defaults.styleUI = 'Bootstrap';
		$("#tb").jqGrid(
				{
					rowNum: 15,
	                rowList: [10, 15, 20, 30, 50],
					shrinkToFit : true,
					autowidth : true,
					url : QDevelopDomain + '/app/sys/auth/view',
					datatype : 'json',
					colNames : [ '权限代码','权限名称', '状态', '状态', '描述','更新时间' ],
					colModel : [ {
						name : 'authcode',
						index : 'authcode',
						width : 220
					}, {
						name : 'authname',
						index : 'authname',
						width : 220
					}, {
						name : 'des',
						index : 'des',
						width : 220
					}, {
						name : 'status',
						index : 'status',
						hidden:true
					}, {
						name : '_status',
						index : '_status',
						width : 80,
						formatter : function(cellvalue, options, rowObject) {
							if (cellvalue == "1") {
								return '有效'
							} else if (cellvalue == "0"){
								return '无效'
							} else {
								return ''
							}
						}
					}, {
						name : 'createtime',
						index : 'createtime',
						width : 150
					} ],
					pager: "#toolbar",
					viewrecords: true,
					rownumbers: true,
					mtype : "POST",
					height : 540,
					loadComplete:function() {
						if (firstVaild) {
							$("#__add").hide();
							$("#__modify").hide();
							$("#__delete").hide();
							checkHideFunction("/app/sys/auth");
							firstVaild = false;
						}
					}

				}).navGrid('#toolbar', {
			edit : false,
			add : false,
			del : false,
			search : false,
			refresh : false
		}).navButtonAdd('#toolbar', {
			caption : "新增",
			buttonicon : "glyphicon glyphicon-plus",
			id:'__add',
			onClickButton : function() {
				$('form')[0].reset();
				$('#authForm #updateFlag').val("2");
				$('#authForm #authcode').attr("readonly",false);
				$("#authTitle").html('添加功能');
				$('#myAuth').modal('show')
			}
		}).navButtonAdd('#toolbar', {
			caption : "编辑",
			buttonicon : "glyphicon glyphicon-edit",
			id:'__modify',
			onClickButton : function() {
				var id = $("#tb").jqGrid('getGridParam', 'selrow');
				if (!id) {
					swal("操作提示",'请选择一行数据',"");
					return;
				}
				var row = $("#tb").jqGrid('getRowData', id);
				$('form')[0].reset();
				$('#authForm #authcode').attr("readonly",true);
				$('#authForm #updateFlag').val("1");
				showAuthForm(row)
			}
		}).navButtonAdd('#toolbar', {
			caption : "删除",
			buttonicon : "glyphicon glyphicon-del",
			id:'__delete',
			onClickButton : function() {
				var id = $("#tb").jqGrid('getGridParam', 'selrow');
				if (!id) {
					swal("操作提示",'请选择一行数据',"");
					return;
				}
				var row = $("#tb").jqGrid('getRowData', id);
				deleteAuth(row.authcode);
				
			}
		}).navButtonAdd('#toolbar', {
			caption : "刷新",
			buttonicon : "glyphicon glyphicon-refresh",
			id:'__refresh',
			onClickButton : function() {
				$("#tb").trigger("reloadGrid");
			}
		})
		
		
		// 窗口大小改变，重新计算jqGrid宽度
		$(window).bind('resize', function() {
			var width = $('.jqGrid_wrapper').width();
			$('#tb').setGridWidth(width);
		});

		$.validator.setDefaults({
			highlight : function(element) {
				$(element).closest('.form-group').removeClass('has-success')
						.addClass('has-error');
			},
			success : function(element) {
				element.closest('.form-group').removeClass('has-error');
			},
			errorElement : "span",
			errorPlacement : function(error, element) {
				if (element.is(":radio") || element.is(":checkbox")) {
					error.appendTo(element.parent().parent().parent());
				} else {
					error.appendTo(element.parent());
				}
			},
			errorClass : "help-block m-b-none",
			validClass : "help-block m-b-none"
		});
	})
</SCRIPT>
<script src="<%=syscontextPath%>/js/plugins/treeview/bootstrap-treeview.js"></script>
</html>