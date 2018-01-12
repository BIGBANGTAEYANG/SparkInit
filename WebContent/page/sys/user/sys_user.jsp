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
<link href="<%=syscontextPath%>/css/plugins/iCheck/custom.css"
	rel="stylesheet">
<link href="<%=syscontextPath%>/css/my.css" rel="stylesheet">
</head>
<body>
	<div class="jqGrid_wrapper" height="100%">
		<table id="tb"></table>
		<div id="toolbar"></div>
	</div>
	<div class="modal inmodal" id="myUser" tabindex="-1" role="dialog"
		aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content animated bounceInRight">
				<div class="modal-header">
					<button type="button" class="close" onclick="closeUsermodal()">
						<span aria-hidden="true">&times;</span><span class="sr-only">关闭</span>
					</button>
					<h5 class="modal-title" id="userTitle"></h5>
				</div>
				<div class="modal-body">
					<form class="form-horizontal m-t" id="userForm">
						<INPUT TYPE="hidden" NAME="userid" id="userid">
						<div class="form-group">
							<label class="col-sm-3 control-label">用户账号：</label>
							<div class="col-sm-8">
								<input id="usercode" name="usercode" class="form-control"
									type="text" required="" aria-required="true">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">用户名称：</label>
							<div class="col-sm-8">
								<input id="username" name="username" class="form-control"
									type="text" required="" aria-required="true">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">员工编号：</label>
							<div class="col-sm-8">
								<input id="empid" name="empid" class="form-control" type="text">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">所属机构：</label>
							<div class="col-sm-8">
								<input id="orgname" name="orgname" class="form-control"
									type="text" readonly="readonly"> <input id="orgcode"
									name="orgcode" type="hidden">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">当前状态：</label>
							<div class="col-sm-8">
								<label><input type="radio" value="1" checked
									name="status"> <i></i> 有效</label> <label><input
									type="radio" value="0" name="status"> <i></i> 无效</label>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">备注：</label>
							<div class="col-sm-8">
								<input id="des" name="des" class="form-control" type="text">
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-white"
						onclick="closeUsermodal()">关闭</button>
					<button type="button" class="btn btn-primary"
						onclick="saveOrUpdateUser()">保存</button>
				</div>
			</div>
		</div>
	</div>
	<div class="modal inmodal fade" id="myUserorg" tabindex="-1"
		role="dialog" aria-hidden="true">
		<div class="modal-dialog modal-sm">
			<div class="modal-content animated bounceInRight">
				<div class="modal-header">
					<button type="button" class="close" onclick="closeUserorgmodal()">
						<span aria-hidden="true">&times;</span><span class="sr-only">关闭</span>
					</button>
					<h5 class="modal-title" id="iconTitle">选择机构</h5>
				</div>
				<div class="modal-body">
					<div id="treeview"></div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-white"
						onclick="closeUserorgmodal()">关闭</button>
				</div>
			</div>
		</div>
	</div>

	<div class="modal inmodal fade" id="myUserRole" tabindex="-1"
		role="dialog" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content animated bounceInRight">
				<div class="modal-header">
					<button type="button" class="close" onclick="closeUserRolemodal()">
						<span aria-hidden="true">&times;</span><span class="sr-only">关闭</span>
					</button>
					<h5 class="modal-title" id="iconTitle">选择角色</h5>
				</div>
				<div class="modal-body">
					<table id="userRoles" style="width: 100%"></table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-white"
						onclick="closeUserRolemodal()">关闭</button>
					<button type="button" class="btn btn-primary" id="__updateUserRole"
						onclick="saveUserRole()">保存</button>
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
	function saveOrUpdateUser() {
		$("#userForm").validate();
		var isSuccess = $("#userForm").valid();
		if (isSuccess) {
			// 验证通过，提交表单
			$.ajax({
				type : "post",
				url : QDevelopDomain + "/app/sys/user/modify",
				datatype : 'json',
				data : $('#userForm').serialize(),
				success : function(data) {
					var _data = JSON.parse(data);
					if (_data.code == 1) {
						$("#myUser").modal('hide');
						swal("操作成功", _data.message, "success");
						//刷新表单
						$("#tb").trigger("reloadGrid");
					} else {
						swal("操作失败", _data.message, "error");
					}
				},
				error : function() {
					swal("操作失败", "", "error");
				}
			});

		}
	}

	function deleteAuth(id) {
		swal({
			title : "您确定要删除此用户吗",
			text : "删除后将无法恢复，请谨慎操作！",
			type : "warning",
			showCancelButton : true,
			confirmButtonColor : "#DD6B55",
			confirmButtonText : "删除",
			closeOnConfirm : false
		}, function() {
			$.ajax({
				type : "post",
				url : QDevelopDomain + "/app/sys/user/delete",
				datatype : 'json',
				data : {
					usercode : id
				},
				success : function(data) {
					var _data = JSON.parse(data);
					if (_data.code == 1) {
						swal("操作成功", _data.message, "success");
						//刷新列表
						$("#tb").trigger("reloadGrid");
					} else {
						swal("操作失败", _data.message, "error");
					}
				},
				error : function() {
					swal("操作失败", "", "error");
				}
			});
		});
	}

	function saveUserRole() {
		var _roles = getChecked();
		var id = $("#tb").jqGrid('getGridParam', 'selrow');
		var row = $("#tb").jqGrid('getRowData', id);
		var tarUsercode = row.usercode;
		$.ajax({
			url : QDevelopDomain + '/app/sys/user/updateUserRole',
			data : {
				roles : _roles,
				tarUsercode : tarUsercode
			},
			type : "post",
			dataType : "json",
			success : function(data) {
				if (data.code == 1) {
					$("#myUserRole").modal('hide');
					swal("操作成功", data.message, "success");
					//刷新列表
					$("#tb").trigger("reloadGrid");
				} else {
					swal("操作失败", data.message, "error");
				}
			},
			error : function(data) {
				swal("操作失败", "", "error");
			}
		});
	}

	function getChecked() {
		var ids = $("#userRoles").jqGrid('getGridParam', 'selarrrow');
		var s = '';
		$.each(ids, function(i, v) {
			var row = $("#userRoles").jqGrid('getRowData', v);
			if (s != '')
				s += '|';
			s += row.id;
		})
		return s;
	}

	function closeUsermodal() {
		$("#myUser").modal('hide');
	}

	function closeUserorgmodal() {
		$("#myUserorg").modal('hide');
		$("#myUser").modal('show');
	}

	function closeUserRolemodal() {
		$("#myUserRole").modal('hide');
	}

	function showUserForm(data) {
		$('#userForm #userid').val(data.userid);
		$('#userForm #usercode').val(data.usercode);
		$('#userForm #username').val(data.username);
		$('#userForm #empid').val(data.empid);
		$('#userForm #orgname').val(data.orgname);
		$('#userForm #orgcode').val(data.orgcode);
		$('#userForm #des').val(data.des);
		$(":radio[name='status'][value='" + data.status + "']").prop("checked",
				"checked");
		$("#userTitle").html('编辑用户信息');
		$("#myUser").modal('show');
	}

	var initUserorgView;
	$(function() {
		$("#orgname").click(function() {
			$("#myUser").modal('hide');
			$("#myUserorg").modal('show');
		});
		initMenuTreeView = function() {
			$.ajax({
				type : "post",
				url : QDevelopDomain + "/app/sys/org/buildOrgCombotree",
				datatype : 'json',
				success : function(data, status) {
					if (status == "success") {
						data1 = JSON.parse(data);
						buildTreeView(buildDomTree(data1));
					}
				},
				error : function() {
					alert('error');
				},
			});
		}
		initMenuTreeView();

		/**
		 * 递归组装菜单树列表
		 */
		function buildDomTree(ajaxData) {
			var data = [];
			function walk(nodes, data) {
				if (!nodes) {
					return;
				}
				$.each(nodes, function(id, node) {
					var obj = {
						id : node.id,
						text : node.text
					};
					if (node.children && node.children.length > 0) {
						obj.nodes = [];
						walk(node.children, obj.nodes);
					}
					data.push(obj);
				});
			}
			walk(ajaxData, data);
			return data;
		}

		function buildTreeView(datas) {
			var options = {
				color : "yellow",
				backColor : "purple",
				onhoverColor : "orange",
				borderColor : "red",
				showBorder : false,
				highlightSelected : true,
				selectedColor : "yellow",
				selectedBackColor : "darkorange",
				expandIcon : 'glyphicon glyphicon-chevron-right',
				collapseIcon : 'glyphicon glyphicon-chevron-down',
				data : datas,
				onNodeSelected : function(event, data) {
					$("#orgcode").val(data.id);
					$("#orgname").val(data.text);
					$("#myUserorg").modal('hide');
					$("#myUser").modal('show');
				}
			};
			$('#treeview').treeview(options);
		}

		$.jgrid.defaults.styleUI = 'Bootstrap';
		$("#tb")
				.jqGrid(
						{
							rowNum : 15,
							rowList : [ 10, 15, 20, 30, 50 ],
							shrinkToFit : true,
							autowidth : true,
							url : QDevelopDomain + '/app/sys/user/view',
							datatype : 'json',
							colNames : [ '用户编号', '用户账号', '用户名称', '员工编号',
									'所属机构', '所属机构编码', '角色', '状态', '状态', '备注',
									'创建时间', '最后登录时间' ],
							colModel : [
									{
										name : 'userid',
										index : 'userid',
										hidden : true
									},
									{
										name : 'usercode',
										index : 'usercode',
										width : 100
									},
									{
										name : 'username',
										index : 'username',
										width : 100
									},
									{
										name : 'empid',
										index : 'empid',
										width : 100
									},
									{
										name : 'orgname',
										index : 'orgname',
										width : 200
									},
									{
										name : 'orgcode',
										index : 'orgcode',
										hidden : true
									},
									{
										name : 'roles',
										index : 'roles',
										width : 200
									},
									{
										name : 'status',
										index : 'status',
										hidden : true
									},
									{
										name : '_status',
										index : '_status',
										width : 50,
										formatter : function(cellvalue,
												options, rowObject) {
											if (cellvalue == "1") {
												return '有效'
											} else if (cellvalue == "0") {
												return '无效'
											} else {
												return ''
											}
										}
									}, {
										name : 'des',
										index : 'des',
										width : 150
									}, {
										name : 'createtime',
										index : 'createtime',
										width : 140
									}, {
										name : 'lastlogintime',
										index : 'lastlogintime',
										width : 140
									} ],
							pager : "#toolbar",
							viewrecords : true,
							rownumbers : true,
							mtype : "POST",
							height : 540,
							loadComplete : function() {
								if (firstVaild) {
									$("#__add").hide();
									$("#__modify").hide();
									$("#__delete").hide();
									$("#__selectrole").hide();
									$("#__modifyRoleAndUser").hide();
									$("#__resetpwd").hide();
									checkHideFunction("/app/sys/user");
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
					id : '__add',
					onClickButton : function() {
						$('form')[0].reset();
						$('#userForm #usercode').attr("readonly", false);
						$("#userTitle").html('添加用户信息');
						$('#myUser').modal('show')
					}
				}).navButtonAdd('#toolbar', {
					caption : "编辑",
					buttonicon : "glyphicon glyphicon-edit",
					id : '__modify',
					onClickButton : function() {
						var id = $("#tb").jqGrid('getGridParam', 'selrow');
						if (!id) {
							swal("操作提示", '请选择一行数据', "");
							return;
						}
						var row = $("#tb").jqGrid('getRowData', id);
						$('form')[0].reset();
						$('#userForm #usercode').attr("readonly", true);
						showUserForm(row)
					}
				}).navButtonAdd('#toolbar', {
					caption : "删除",
					buttonicon : "glyphicon glyphicon-del",
					id : '__delete',
					onClickButton : function() {
						var id = $("#tb").jqGrid('getGridParam', 'selrow');
						if (!id) {
							swal("操作提示", '请选择一行数据', "");
							return;
						}
						var row = $("#tb").jqGrid('getRowData', id);
						deleteAuth(row.usercode);

					}
				}).navButtonAdd('#toolbar', {
					caption : "选择角色",
					buttonicon : "glyphicon glyphicon-ok",
					id : '__selectrole',
					onClickButton : function() {
						var id = $("#tb").jqGrid('getGridParam', 'selrow');
						if (!id) {
							swal("操作提示", '请选择一行数据', "");
							return;
						}
						var row = $("#tb").jqGrid('getRowData', id);
						initUserRole(row.usercode, row.orgcode);
						$("#myUserRole").modal('show');

					}
				}).navButtonAdd('#toolbar', {
					caption : "重置密码",
					buttonicon : "glyphicon glyphicon-ok",
					id : '__resetpwd',
					onClickButton : function() {
						var id = $("#tb").jqGrid('getGridParam', 'selrow');
						if (!id) {
							swal("操作提示", '请选择一行数据', "");
							return;
						}
						var row = $("#tb").jqGrid('getRowData', id);
						resetPwd(row.usercode);

					}
				}).navButtonAdd('#toolbar', {
					caption : "刷新",
					buttonicon : "glyphicon glyphicon-refresh",
					id : '__refresh',
					onClickButton : function() {
						$("#tb").trigger("reloadGrid");
					}
				})

		function resetPwd(usercode) {
			swal({
				title : "您确定要重置此用户密码吗？",
				text : "重置后密码与登录账号相同！",
				type : "warning",
				showCancelButton : true,
				confirmButtonColor : "#DD6B55",
				confirmButtonText : "确定",
				closeOnConfirm : false
			}, function() {
				$.ajax({
					url : QDevelopDomain + '/app/sys/user/resetpwd',
					data : {
						usercode : usercode
					},
					type : "post",
					dataType : "json",
					success : function(data) {
						if (data.code == 1) {
							swal("操作成功", data.message, "success");
						} else {
							swal("操作失败", data.message, "error");
						}
					},
					error : function() {
						swal("操作失败", "", "error");
					}
				});
			});
		}

		$("#userRoles").jqGrid({
			multiselect : true,
			shrinkToFit : true,
			autowidth : true,
			url : QDevelopDomain + '/app/sys/user/selectrole',
			datatype : 'json',
			colNames : [ '角色编号', '角色名称', '是否拥有' ],
			colModel : [ {
				name : 'id',
				index : 'id',
				hidden : true
			}, {
				name : 'text',
				index : 'text',
				width : 545
			}, {
				name : 'checked',
				index : 'checked',
				hidden : true
			} ],
			postData : {},
			mtype : "POST",
			loadComplete : function() {
				var ids = $("#userRoles").jqGrid('getDataIDs');
				$.each(ids, function(i, v) {
					var rows = $("#userRoles").jqGrid('getRowData', v);
					if (rows.checked == 'true')
						$("#userRoles").jqGrid('setSelection', v, false);
				})
			}
		})

		function initUserRole(tarUsercode, tarOrgcode) {
			$("#userRoles").jqGrid('clearGridData', false);
			$("#userRoles").jqGrid('setGridParam', {
				postData : {
					tarUsercode : tarUsercode,
					tarOrgcode : tarOrgcode
				}
			});
			$("#userRoles").trigger("reloadGrid");
		}

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
<script
	src="<%=syscontextPath%>/js/plugins/treeview/bootstrap-treeview.js"></script>
</html>