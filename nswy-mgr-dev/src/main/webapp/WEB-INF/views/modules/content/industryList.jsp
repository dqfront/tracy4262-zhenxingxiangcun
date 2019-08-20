<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>区域管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
		    debugger
			var tpl = $("#treeTableTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
			var data = ${fns:toJson(list)}, rootId = "0";
			addRow("#treeTableList", tpl, data, rootId, true);
			$("#treeTable").treeTable({expandLevel : 5});
		});
		function addRow(list, tpl, data, pid, root){
			for (var i=0; i<data.length; i++){
				var row = data[i];
				if ((${fns:jsGetVal('row.pid')}) == pid){
					$(list).append(Mustache.render(tpl, {
						dict: {
							type: getDictLabel(${fns:toJson(fns:getDictList('sys_area_type'))}, row.type)
						}, pid: (root?0:pid), row: row
					}));
					addRow(list, tpl, data, row.id);
				}
			}
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/nswy/industry/control/">行业列表</a></li>

	</ul>
	<%--<form id="searchForm"   action="${ctx}/nswy/industry/control" method="post" class="breadcrumb form-search">
		<label>行业名称：</label><input id="dictName" name="dictName" htmlEscape="false" maxlength="50" class="input-medium"/>
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form>--%>
	<sys:message content="${message}"/>
	<table id="treeTable" class="table table-striped table-bordered table-condensed">
		<thead>
		<tr>
			<th>行业名称</th>
			<shiro:hasPermission name="sys:area:edit">
				<th>操作</th>
			</shiro:hasPermission>
		</tr>
		</thead>
		<tbody id="treeTableList"></tbody>
	</table>

	<script type="text/template" id="treeTableTpl">
		<tr id="{{row.id}}" pId="{{pid}}">
			<td>{{row.dictName}}</td>

			<shiro:hasPermission name="sys:area:edit"><td>
				<a href="${ctx}/nswy/industry/addPid?pid={{row.pid}}">添加同级行业</a>
				<a href="${ctx}/nswy/industry/add?id={{row.id}}">添加子级行业</a>
				<a href="${ctx}/nswy/industry/form?id={{row.id}}">修改</a>
				<a href="${ctx}/nswy/industry/delete?id={{row.id}}" onclick="return confirmx('是否删除该行业信息？', this.href)">删除</a>
			</td></shiro:hasPermission>
		</tr>
	</script>

</body>
</html>