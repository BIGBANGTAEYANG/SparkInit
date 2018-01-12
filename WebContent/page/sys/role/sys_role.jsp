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
<link
	href="<%=syscontextPath%>/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css"
	rel="stylesheet">
<link href="<%=syscontextPath%>/css/my.css" rel="stylesheet">
</head>
<body style="width: 100%; height: 100%">
	<div class="jqGrid_wrapper" height="100%">
		<div id="toolbar"></div>
		<table id="tb"></table>
	</div>
	<div class="modal inmodal" id="myRole" tabindex="-1" role="dialog"
		aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content animated bounceInRight">
				<div class="modal-header">
					<button type="button" class="close" onclick="closeRolemodal()">
						<span aria-hidden="true">&times;</span><span class="sr-only">关闭</span>
					</button>
					<h5 class="modal-title" id="roleTitle"></h5>
				</div>
				<div class="modal-body">
					<form class="form-horizontal m-t" id="roleForm">
						<INPUT TYPE="hidden" NAME="opflag" id="opflag">
						<div class="form-group">
							<label class="col-sm-3 control-label">角色代码：</label>
							<div class="col-sm-8">
								<input id="rolecode" name="rolecode" class="form-control"
									type="text" required="" aria-required="true">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">角色名称：</label>
							<div class="col-sm-8">
								<input id="rolename" name="rolename" class="form-control"
									type="text" required="" aria-required="true">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">备注：</label>
							<div class="col-sm-8">
								<textarea class="form-control" id="des" name="des" rows="3"
									placeholder="请输入角色说明..."></textarea>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-white"
						onclick="closeRolemodal()">关闭</button>
					<button type="button" class="btn btn-primary"
						onclick="saveOrUpdateRole()">保存</button>
				</div>
			</div>
		</div>
	</div>
	<div class="modal inmodal fade" id="myMenuAuth" tabindex="-1"
		role="dialog" aria-hidden="true">
		<div class="modal-dialog" style="width: 100%;">
			<div class="modal-content animated bounceInRight"
				style="width: 100%; height: 530px">
				<div class="modal-header">
					<button type="button" class="close" onclick="closeMenuAuthmodal()">
						<span aria-hidden="true">&times;</span><span class="sr-only">关闭</span>
					</button>
					<h5 class="modal-title" id="iconTitle">选择菜单功能</h5>
				</div>
				<div class="modal-body" style="width: 100%; height: 400px;">
					<table id="tbMenu"></table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-white"
						onclick="closeMenuAuthmodal()">关闭</button>
					<button type="button" class="btn btn-primary"
						id="__updateRoleMenuAuth" onclick="saveRoleMenuAuth()">保存</button>
				</div>
			</div>
		</div>
	</div>
	<script
		src="<%=syscontextPath%>/js/plugins/jqgrid/i18n/grid.locale-cn.js"
		type="text/javascript"></script>
	<script
		src="<%=syscontextPath%>/js/plugins/jqgrid/jquery.jqGrid.min.js"
		type="text/javascript"></script>
	<script src="<%=syscontextPath%>/js/plugins/peity/jquery.peity.min.js"></script>
	<script src="<%=syscontextPath%>/js/plugins/iCheck/icheck.min.js"></script>


	<SCRIPT LANGUAGE="JavaScript">
		var firstVaild = true;
		function saveOrUpdateRole() {
			$("#roleForm").validate();
			var isSuccess = $("#roleForm").valid();
			if (isSuccess) {
				// 验证通过，提交表单
				$.ajax({
					type : "post",
					url : QDevelopDomain + "/app/sys/role/modify",
					datatype : 'json',
					data : $('#roleForm').serialize(),
					success : function(data) {
						var _data = JSON.parse(data);
						if (_data.code == 1) {
							$("#myRole").modal('hide');
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

		function saveRoleMenuAuth() {
			var rolemenuauthids = getCheckedObj();
			var id = $("#tb").jqGrid('getGridParam', 'selrow');
			var row = $("#tb").jqGrid('getRowData', id);
			$.ajax({
				type : "post",
				dataType : "json",
				url : QDevelopDomain + "/app/sys/role/updateRoleMenuAuth",
				data : {
					parameter : rolemenuauthids.join(","),
					rolecode : row.rolecode
				},
				success : function(data) {
					if (data.code == 1) {
						swal("操作成功", data.message, "success");
					} else {
						swal("操作失败", data.message, "error");
					}
				},
				error : function(data) {
					swal("操作失败", "", "error");
				}
			});
		}

		//取已经选择的checkBox
		function getCheckedObj() {
			var arr = $(":input[type=checkbox]");
			var array = new Array();
			arr.each(function(i, item) {
				if (this.checked && item.id.indexOf("0_") != 0) {
					array.push(item.id);
				}
			});
			return array;
		}

		function deleteRole(id) {
			swal({
				title : "您确定要删除此角色吗",
				text : "删除后将无法恢复，请谨慎操作！",
				type : "warning",
				showCancelButton : true,
				confirmButtonColor : "#DD6B55",
				confirmButtonText : "删除",
				closeOnConfirm : false
			}, function() {
				$.ajax({
					type : "post",
					url : QDevelopDomain + "/app/sys/role/delete",
					datatype : 'json',
					data : {
						rolecode : id
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

		function closeRolemodal() {
			$("#myRole").modal('hide');
		}

		function closeMenuAuthmodal() {
			$("#myMenuAuth").modal('hide');
		}

		function showRoleForm(data) {
			$('#roleForm #rolecode').val(data.rolecode);
			$('#roleForm #rolename').val(data.rolename);
			$('#roleForm #des').val(data.des);
			$("#roleTitle").html('编辑角色信息');
			$("#myRole").modal('show');
		}

		//选择列
		function checkAllColumns(obj) {
			var arr = $(":input[type=checkbox]");
			var flag = $(obj).is(":checked");
			if (!flag) {
				arr.each(function(i, item) {
					if (item.id.split("_")[1] == obj.id.split("_")[1]) {
						this.checked = false;
					}
				});
			} else {
				arr.each(function(i, item) {
					if (item.disabled)
						this.checked = false;
					else if (item.id.split("_")[1] == obj.id.split("_")[1]) {
						this.checked = true;
					}
				});
			}
		}
		//选择行
		function checkRows(obj) {
			var arr = $(":input[type=checkbox]");
			var flag = $(obj).is(":checked");
			console.log(obj.id)
			if (!flag) {
				arr.each(function(i, item) {
					if (item.id.split("_")[0] == obj.id.split("_")[1]) {
						this.checked = false;
					}
				});
			} else {
				arr.each(function(i, item) {
					if (item.disabled)
						this.checked = false;
					else if (item.id.split("_")[0] == obj.id.split("_")[1]) {
						console.log(item.id)
						this.checked = true;
					}
				});
			}
		}

		//全选
		function checkAll() {
			var flag = $("#0_quanxuan").is(":checked");
			var arr = $(":input[type=checkbox]");
			if (flag) {
				arr.each(function(i, item) {
					if (item.disabled)
						this.checked = false;
					else
						this.checked = true;
				});
			} else {
				arr.each(function() {
					this.checked = false;
				});
			}
		}
		$(function() {
			$.jgrid.defaults.styleUI = 'Bootstrap';
			$("#tb").jqGrid({
				rowNum : 15,
				rowList : [ 10, 15, 20, 30, 50 ],
				shrinkToFit : true,
				autowidth : true,
				url : QDevelopDomain + '/app/sys/role/view',
				datatype : 'json',
				colNames : [ '角色代码', '角色名称', '角色描述' ],
				colModel : [ {
					name : 'rolecode',
					index : 'rolecode',
					width : 200
				}, {
					name : 'rolename',
					index : 'rolename',
					width : 200
				}, {
					name : 'des',
					index : 'des',
					width : 500
				} ],
				pager : "#toolbar",
				viewrecords : true,
				rownumbers : true,
				mtype : "POST",
				height : 540,
				recordtext : "当前{0} - {1}条，共{2}条记录",
				loadComplete : function() {
					if (firstVaild) {
						$("#__add").hide();
						$("#__modify").hide();
						$("#__delete").hide();
						$("#__viewRoleMenuAuth").hide();
						$("#__updateRoleMenuAuth").hide();
						checkHideFunction("/app/sys/role");
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
					$('#roleForm #rolecode').attr("readonly", false);
					$('#roleForm #opflag').val("0");
					$("#roleTitle").html('添加角色信息');
					$('#myRole').modal('show')
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
					$('#roleForm #rolecode').attr("readonly", true);
					$('#roleForm #opflag').val("1");
					showRoleForm(row)
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
					deleteRole(row.rolecode);

				}
			}).navButtonAdd('#toolbar', {
				caption : "角色功能授权",
				buttonicon : "glyphicon glyphicon-ok",
				id : '__viewRoleMenuAuth',
				onClickButton : function() {
					var id = $("#tb").jqGrid('getGridParam', 'selrow');
					if (!id) {
						swal("操作提示", '请选择一行数据', "");
						return;
					}
					var row = $("#tb").jqGrid('getRowData', id);
					initRoleMenuAuthDatas(row.rolecode, row.rolename);
					//$("#myMenuAuth").modal('show');
				}
			})

			function initRoleMenuAuthDatas(rolecode, rolename) {
				$
						.ajax({
							type : "post",
							url : QDevelopDomain
									+ "/app/sys/role/viewRoleMenuAuth",
							datatype : 'json',
							data : {
								rolecode : rolecode
							},
							success : function(data) {
								var _data = JSON.parse(data);
								var _header = _data.header;
								var _griddata = _data.data;
								var colNames = [];
								var colModel = [];
								$.each(_header, function(i, v) {
									var av = v.split(",");
									colNames.push(av[0]);
									if (i == 0) {
										colModel.push({
											"name" : av[1],
											"index" : av[1],
											"hidden" : true
										});
									} else if (i == 1) {
										colModel.push({
											"name" : av[1],
											"index" : av[1],
											"frozen" : true
										});
									} else {
										colModel.push({
											"name" : av[1],
											"index" : av[1],
											width : '90'
										});
									}
								});

								var gData = []
								$
										.each(
												_griddata,
												function(di, dv) {
													var checkall;
													if (di == 0) {
														checkall = "<input id='0_quanxuan' type='checkbox' onclick='checkAll()'>全选"
													} else {
														checkall = "<input id='0_"
																+ dv.id
																+ "' type='checkbox' onclick='checkRows(this)'>选择行"
													}
													dv["checkall"] = checkall;
													$
															.each(
																	_header,
																	function(i,
																			v) {
																		if (i > 2) {
																			var av = v
																					.split(",");
																			var k = av[1];
																			var _d = dv[k]
																					.split("|");
																			var checked = _d[1] === "1" ? "checked='checked'"
																					: "";
																			var have = _d[0] === "1" ? ""
																					: "disabled='disabled'";
																			var oc = dv.id === 0 ? "onclick='checkAllColumns(this)'"
																					: "";
																			var des = dv.id === 0 ? "选择列"
																					: ""
																			var html = "<input id='"+ dv.id +"_"+ av[1] +"' type='checkbox' "+ checked +" "+ have +" "+ oc +">"
																					+ des;
																			dv[k] = html;
																		}
																	});
													gData.push(dv);
												})

								$("#tbMenu").jqGrid({
									treeGrid : true,
									treeGridModel : 'adjacency',
									ExpandColumn : 'menuname',
									//shrinkToFit : false,
									//autowidth : true,
									datatype : 'local',
									colNames : colNames,
									colModel : colModel,
									ExpandColClick : true,
									treeReader : {
										level_field : "level",
										parent_id_field : "parent",
										leaf_field : "isLeaf",
										expanded_field : "expanded",
										loaded_field : false
									},
									mtype : "POST",
									width : $("#myMenuAuth").width() * 0.98,
									height : $("#myMenuAuth").height() * 0.55,
								}).navGrid('#toolbar', {
									edit : false,
									add : false,
									del : false,
									search : false,
									refresh : false
								})
								$("#tbMenu")[0].addJSONData(gData);
								$("#tbMenu").jqGrid('setFrozenColumns');
								$("#iconTitle")
										.html('角色功能授权（' + rolename + '）');
								$("#myMenuAuth").modal('show');
							},
							error : function() {
								swal("操作失败", "", "error");
							}
						});
			}

			// 窗口大小改变，重新计算jqGrid宽度
			$(window).bind('resize', function() {
				var width = $('.jqGrid_wrapper').width();
				$('#tb').setGridWidth(width);
			});

			$.validator.setDefaults({
				highlight : function(element) {
					$(element).closest('.form-group')
							.removeClass('has-success').addClass('has-error');
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
			$(document).ready(function() {
				$('.i-checks').iCheck({
					checkboxClass : 'icheckbox_square-green',
					radioClass : 'iradio_square-green',
				});
			});
		})
	</SCRIPT>
	<script
		src="<%=syscontextPath%>/js/plugins/treeview/bootstrap-treeview.js"></script>
</html>