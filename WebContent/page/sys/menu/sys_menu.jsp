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
	<div class="jqGrid_wrapper" height="100%">
		<table id="tb"></table>
		<div id="toolbar"></div>
	</div>
	<div class="modal inmodal" id="myMenu" tabindex="-1" role="dialog"
		aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content animated bounceInRight">
				<div class="modal-header">
					<button type="button" class="close" onclick="closeMenumodal()">
						<span aria-hidden="true">&times;</span><span class="sr-only">关闭</span>
					</button>
					<h5 class="modal-title" id="menuTitle"></h5>
				</div>
				<div class="modal-body">
					<form class="form-horizontal m-t" id="menuForm">
						<input type="hidden" id="menuid" name="menuid">
						<input type="hidden" id="level" name="level">
						<div class="form-group">
							<label class="col-sm-3 control-label">菜单名称：</label>
							<div class="col-sm-8">
								<input id="menuname" name="menuname" class="form-control"
									type="text" required="" aria-required="true"> <span
									class="help-block m-b-none"><i class="fa fa-info-circle"></i>
									显示在左侧导航栏的中文名</span>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">菜单链接：</label>
							<div class="col-sm-8">
								<input id="url" name="url" class="form-control" type="text"
									required="" aria-required="true"> <span
									class="help-block m-b-none"><i class="fa fa-info-circle"></i>
									菜单对应的链接地址</span>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">上级菜单：</label>
							<div class="col-sm-8">
								<input id="parentname" name="parentname" class="form-control"
									type="text" readonly="readonly"> <input id="parentId"
									name="parentId" type="hidden">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">菜单图片：</label>
							<div class="col-sm-8">
								<div class="input-group">
									<input id="image" name="image" class="form-control" type="text"
										value="glyphicon glyphicon-asterisk" required="required"
										aria-required="true" readonly="readonly"><span
										class="input-group-btn">
										<button id="iconBtn" type="button" class="btn">
											<i class="glyphicon glyphicon-asterisk"></i>
										</button>
								</div>

							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">菜单排序：</label>
							<div class="col-sm-8">
								<!-- <input id="nodesort" name="nodesort" class="form-control"
									type="text" required="" aria-required="true"> -->
									<select class="form-control m-b" id="nodesort" name="nodesort" required="" aria-required="true">
                                       <option>1</option>
                                       <option>2</option>
                                       <option>3</option>
                                       <option>4</option>
                                       <option>5</option>
                                       <option>6</option>
                                       <option>7</option>
                                       <option>8</option>
                                       <option>9</option>
                                       <option>10</option>
                                   </select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">是否可见：</label>
							<div class="col-sm-8">
                                 <label><input type="radio" value="1" checked name="visible"> <i></i> 可见</label>
                                  <label><input type="radio" value="0" name="visible"> <i></i> 不可见</label>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-white"
						onclick="closeMenumodal()">关闭</button>
					<button type="button" class="btn btn-primary" onclick="saveOrUpdateMenu()">保存</button>
				</div>
			</div>
		</div>

	</div>
	<div class="modal inmodal fade" id="myIcon" tabindex="-1" role="dialog"
		aria-hidden="true">
		<div class="modal-dialog modal-lg">
			<div class="modal-content animated bounceInRight">
				<div class="modal-header">
					<button type="button" class="close" onclick="closeIconmodal()">
						<span aria-hidden="true">&times;</span><span class="sr-only">关闭</span>
					</button>
					<h5 class="modal-title" id="iconTitle">选择菜单图标</h5>
				</div>
				<div class="modal-body">
					<div class="bs-glyphicons">
						<ul class="bs-glyphicons-list">
							<li v-for="item in iconData" @click='selectIcon(item.icon)'><span
								:class="item.icon" aria-hidden="true"></span> <span
								class="glyphicon-class">{{item.icon}}</span></li>
						</ul>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-white"
						onclick="closeIconmodal()">关闭</button>
				</div>
			</div>

		</div>

	</div>

	<div class="modal inmodal fade" id="myMenuTree" tabindex="-1"
		role="dialog" aria-hidden="true">
		<div class="modal-dialog modal-sm">
			<div class="modal-content animated bounceInRight">
				<div class="modal-header">
					<button type="button" class="close" onclick="closeMenuTreemodal()">
						<span aria-hidden="true">&times;</span><span class="sr-only">关闭</span>
					</button>
					<h5 class="modal-title" id="iconTitle">选择上级菜单</h5>
				</div>
				<div class="modal-body">
					<div id="treeview"></div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-white"
						onclick="closeMenuTreemodal()">关闭</button>
				</div>
			</div>

		</div>

	</div>
	
	<div class="modal inmodal fade" id="myMenuAuth" tabindex="-1"
		role="dialog" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content animated bounceInRight">
				<div class="modal-header">
					<button type="button" class="close" onclick="closeMenuAuthmodal()">
						<span aria-hidden="true">&times;</span><span class="sr-only">关闭</span>
					</button>
					<h5 class="modal-title" id="iconTitle">选择菜单功能</h5>
				</div>
				<div class="modal-body">
					<table id="menuAuth" style="width:100%"></table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-white"
						onclick="closeMenuAuthmodal()">关闭</button>
					<button type="button" class="btn btn-primary" id="__updateMenuAuth" onclick="saveMenuAuth()">保存</button>
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
	function saveOrUpdateMenu() {
		$("#menuForm").validate();
		var isSuccess = $("#menuForm").valid();
		if (isSuccess) {
			// 验证通过，提交表单
			$.ajax({
				type : "post",
				url : QDevelopDomain + "/app/sys/menu/modify",
				datatype : 'json',
				data : $('#menuForm').serialize(),
				success : function(data) {
					var _data = JSON.parse(data);
					if (_data.code == 1) {
						$("#myMenu").modal('hide');
						swal("操作成功", _data.message,"success");
						//刷新表单
						$("#tb").trigger("reloadGrid");
						// 重新加载树形菜单
						initMenuTreeView();
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

	function deleteMenu(id) {
		swal({
	        title: "您确定要删除此菜单吗",
	        text: "删除后将无法恢复，请谨慎操作！",
	        type: "warning",
	        showCancelButton: true,
	        confirmButtonColor: "#DD6B55",
	        confirmButtonText: "删除",
	        closeOnConfirm: false
	    }, function () {
			$.ajax({
				type : "post",
				url : QDevelopDomain + "/app/sys/menu/delete",
				datatype : 'json',
				data : {menuid:id},
				success : function(data) {
					var _data = JSON.parse(data);
					if (_data.code == 1) {
						swal("操作成功",_data.message,"success");
						//刷新表单
						$("#tb").trigger("reloadGrid");
						// 重新加载树形菜单
						initMenuTreeView();
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
	
	function saveMenuAuth() {
		var ids = $("#menuAuth").jqGrid('getGridParam','selarrrow');
		var s = '';
		$.each(ids,function(i,v) {
			var row = $("#menuAuth").jqGrid('getRowData', v);
			if (s != '') s += '|';
			s += row.authcode;
		})
		var id = $("#tb").jqGrid('getGridParam', 'selrow');
		var rows = $("#tb").jqGrid('getRowData', id);
		$.ajax({
			type:"post",
			url:QDevelopDomain+"/app/sys/menu/updateMenuAuth",
			data:{auths:s,menuid:rows.id},
			dataType:"json",
			success : function(result) {
				if(result.code == 1) {
					$("#myMenuAuth").modal('hide');
					swal("操作成功", result.message,"success");
				} else {
					swal("操作失败", result.message,"error");
				}
			},
			error : function(data) {
				swal("操作失败", "","error");
			}
		});
	} 
	
	function closeIconmodal() {
		$("#myIcon").modal('hide');
		$("#myMenu").modal('show');
	}

	function closeMenuTreemodal() {
		$("#myMenuTree").modal('hide');
		$("#myMenu").modal('show');
	}

	function closeMenumodal() {
		$("#myMenu").modal('hide');
	}
	
	function closeMenuAuthmodal() {
		$("#myMenuAuth").modal('hide');
	}
	
	new Vue({
		el : "#myIcon",
		data : {
			iconData : []
		},
		mounted(){
			this.initIconData()
		},
		methods : {
			initIconData:function() {
				var _self = this;
				$.getJSON(QDevelopDomain+"/js/common/icon.json", function (data){
					_self.iconData = data;
				});
			},
			selectIcon : function(icon) {
				$("#image").val(icon);
				$("#iconBtn i").removeClass();
				$("#iconBtn i").addClass(icon);
				$("#myIcon").modal('hide');
				$("#myMenu").modal('show');
			}
		}
	})

	
	function showMenuForm(data) {
		if (data.id == 0) return;
		$('#menuForm #menuid').val(data.id);
		$('#menuForm #menuname').val(data.menuname);
		$('#menuForm #url').val(data.url);
		$('#menuForm #parentname').val(data.parentname);
		$('#menuForm #level').val(data.level);
		$('#menuForm #parentId').val(data.parent);
		$('#menuForm #image').val(data.image);
		$("#iconBtn i").removeClass();
		$("#iconBtn i").addClass(data.image);
		$('#menuForm #nodesort').val(data.nodesort);
		$(":radio[name='visible'][value='" + data.visible + "']").prop("checked", "checked");
		$("#menuTitle").html('编辑菜单');
		$("#myMenu").modal('show');
	}
	var initMenuTreeView;

	$(function() {
		$("#iconBtn").click(function() {
			$("#myMenu").modal('hide');
			$("#myIcon").modal('show')
		});

		$("#parentname").click(function() {
			$("#myMenu").modal('hide');
			$("#myMenuTree").modal('show');
		});

		initMenuTreeView = function() {
			$.ajax({
				type : "post",
				url : QDevelopDomain + "/app/sys/menu/buildMenuTree",
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
						text : node.name,
						icon : node.icon
					};
					if (node.subMenu && node.subMenu.length > 0) {
						obj.nodes = [];
						walk(node.subMenu, obj.nodes);
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
					$("#parentId").val(data.id);
					$("#parentname").val(data.text);
					$("#myMenuTree").modal('hide');
					$("#myMenu").modal('show');
				}
			};
			$('#treeview').treeview(options);
		}
		$.jgrid.defaults.styleUI = 'Bootstrap';
		$("#tb").jqGrid(
				{
					treeGrid : true,
					treeGridModel : 'adjacency',
					ExpandColumn : 'menuname',
					shrinkToFit : true,
					autowidth : true,
					url : QDevelopDomain + '/app/sys/menu/view',
					datatype : 'json',
					colNames : [ '菜单编号','菜单名称', '菜单url', '菜单图片','菜单图片', '排序', '是否可见','是否可见',
							'菜单提示', '更新时间', '操作人','父节点名称','菜单等级' ],
					colModel : [ {
						name : 'id',
						index : 'id',
						hidden : true
					},{
						name : 'menuname',
						index : 'menuname',
						width : 220
					}, {
						name : 'url',
						index : 'url',
						width : 400
					}, {
						name : 'image',
						index : 'image',
						hidden : true
					}, {
						name : '_image',
						index : '_image',
						width : 80,
						formatter : function(cellvalue, options, rowObject) {
							return "<i class='"+ cellvalue +"'></i>";
						}
					}, {
						name : 'nodesort',
						index : 'nodesort',
						width : 80
					}, {
						name : 'visible',
						index : 'visible',
						hidden : true
					}, {
						name : '_visible',
						index : '_visible',
						width : 80,
						formatter : function(cellvalue, options, rowObject) {
							if (cellvalue == "1") {
								return '可见'
							} else if (cellvalue == "0"){
								return '不可见'
							} else {
								return ''
							}
						}
					}, {
						name : 'tooltip',
						index : 'tooltip',
						hidden : true
					}, {
						name : 'createtime',
						index : 'createtime',
						width : 150
					}, {
						name : 'operator',
						index : 'operator',
						width : 80
					}, {
						name : 'parentname',
						index : 'parentname',
						hidden : true
					}, {
						name : 'level',
						index : 'level',
						hidden : true
					} ],
					jsonReader : {
						repeatitems : false
					},
					treeReader : {
						level_field : "level",
						parent_id_field : "parent",
						leaf_field : "isLeaf",
						expanded_field : "expanded",
						loaded_field : false
					},
					//caption: "菜单管理",     
					mtype : "POST",
					height : 540,
					pager:'#toolbar',
					ExpandColClick : true,
					/* ondblClickRow : function(rowid, iRow, iCol, e) {
						var rows = $("#tb").jqGrid('getRowData', rowid);
						showMenuForm(rows)
					}, */
					loadComplete:function() {
						if (firstVaild) {
							$("#__add").hide();
							$("#__modify").hide();
							$("#__delete").hide();
							$("#__updateMenuAuth").hide();
							checkHideFunction("/app/sys/menu");
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
				$("#menuTitle").html('添加菜单');
				$('#myMenu').modal('show')
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
				var rows = $("#tb").jqGrid('getRowData', id);
				if (rows.id == 0) {
					swal("操作警告","根菜单不允许编辑","warning");
					return;
				}
				var rows = $("#tb").jqGrid('getRowData', id);
				showMenuForm(rows)
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
				var rows = $("#tb").jqGrid('getRowData', id);
				if (rows.id == 0) {
					swal("操作警告","根菜单不允许删除","warning");
					return;
				}
				deleteMenu(rows.id);
				
			}
		}).navButtonAdd('#toolbar', {
			caption : "刷新",
			buttonicon : "glyphicon glyphicon-refresh",
			id:'__refresh',
			onClickButton : function() {
				$("#tb").trigger("reloadGrid");
			}
		}).navButtonAdd('#toolbar', {
			caption : "选择菜单功能",
			buttonicon : "glyphicon glyphicon-ok",
			id:'__updateMenuAuth',
			onClickButton : function() {
				var id = $("#tb").jqGrid('getGridParam', 'selrow');
				if (!id) {
					swal("操作提示",'请选择一行数据',"");
					return;
				}
				var rows = $("#tb").jqGrid('getRowData', id);
				if (rows.id == 0) {
					swal("操作警告","根菜单不可选择菜单功能","warning");
					return;
				}
				var rows = $("#tb").jqGrid('getRowData', id);
				initMenuAuth(rows.id);
				$("#myMenuAuth").modal('show');
			}
		})
		
		$("#menuAuth").jqGrid({
				multiselect:true,
				shrinkToFit : true,
				autowidth : true,
				url : QDevelopDomain + '/app/sys/auth/viewMenuAuths',
				datatype : 'json',
				colNames : [ '功能编号','功能名称','菜单编号' ],
				colModel : [ {
					name : 'authcode',
					index : 'authcode',
					hidden : true
				},{
					name : 'authname',
					index : 'authname',
					width : 545
				},{
					name : 'menuid',
					index : 'menuid',
					hidden : true
				}],
				postData:{},
				mtype : "POST",
				loadComplete:function() {
					var ids = $("#menuAuth").jqGrid('getDataIDs');
					$.each(ids,function(i,v) {
						var rows = $("#menuAuth").jqGrid('getRowData', v);
						if (rows.menuid)
							$("#menuAuth").jqGrid('setSelection', v, false);
					})
				}
			})
		
		function initMenuAuth(id) {
			$("#menuAuth").jqGrid('clearGridData',false);
			$("#menuAuth").jqGrid('setGridParam',{postData:{menuid:id}});
			$("#menuAuth").trigger("reloadGrid");
		}
		
		// 窗口大小改变，重新计算jqGrid宽度
		$(window).bind('resize', function() {
			var width = $('.jqGrid_wrapper').width();
			$('#tb').setGridWidth(width);
			
			var width1 = $('#myMenuAuth').width();
			$('#menuAuth').setGridWidth(width1);
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