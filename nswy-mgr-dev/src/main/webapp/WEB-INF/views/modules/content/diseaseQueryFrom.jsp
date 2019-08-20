<%@ page import="com.ovit.manager.common.config.Global" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>病害管理</title>
	<meta name="decorator" content="default"/>

	<script type="text/javascript">

        function getPinyin(chinese){
            $.ajax({
                url:"<%= Global.getConfig("api.url")%>/wiki/api/species/getSpeciesPinYin/"+chinese,
                data:{

                },
                type:"GET",
                dataType: "JSON",
                success : function (data) {
                    $("#pinyin").val(data.data)
                }
            });
        }

        function fileChange(target) {
            var fileSize = 0;
            fileSize = target.files[0].size;
            var size = fileSize / 1024;
            if (size > 2000) {
                alert("图片大小超出限制");
                target.value = "";
                return false;   //阻止submit提交
            }
        }

        $(document).ready(function() {
            $("#name").focus();
            $("#inputForm").validate({
                submitHandler: function(form){
                    //loading('正在提交，请稍等...');
                    form.submit();
                },
                errorContainer: "#messageBox",
                errorPlacement: function(error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }
                }
            });
        });
	</script>
</head>
<body>
<ul class="nav nav-tabs">
	<li >
		<a href="${ctx}/nswy/diseaseQuery/findDiseaseList">病害列表</a>
	</li>
	<li class="active">
		<a >病害修改</a>
	</li>

</ul><br/>

<form:form id="inputForm"  action="${ctx}/nswy/diseaseQuery/updateDisease" method="post" class="form-horizontal" enctype="multipart/form-data">
	<input type="hidden" id="indexId" name="indexId" value="${disease.indexid}">

	<div class="control-group">
		<label class="control-label">病害名称:</label>
		<div class="controls">
			<input id="diseaseName" name="diseaseName" value="${disease.fname}" htmlEscape="false" maxlength="50"  class="required input-xlarge" oninput="getPinyin($('#diseaseName').val())"/>
			<span class="help-inline"><font color="red">*</font> </span>
		</div>
	</div>

	<div class="control-group">
		<label class="control-label">病害拼音:</label>
		<div class="controls">
			<input id="pinyin" name="pinyin" value="${disease.fpinyin}" htmlEscape="false" class="required input-xlarge" maxlength="50" readonly="true"/>
			<span class="help-inline"><font color="red">*</font> </span>
		</div>
	</div>

	<div class="control-group">
		<label class="control-label">危害物种:</label>
		<div class="controls">
			<select id="speciesId" name="speciesId" htmlEscape="false" class="required input-xlarge">
				<option value=''></option>
				<c:forEach var="report" items="${specieslist}" >
					<option value="${report.fid}" <c:if test="${disease.speciesid eq report.fid}">selected</c:if>>
							${report.fname}
					</option>
				</c:forEach>
			</select>
			<span class="help-inline"><font color="red">*</font> </span>
		</div>
	</div>

	<div class="control-group">
		<label class="control-label">上传图标:</label>
		<div class="controls">
			<input type="file" id="diseasePic" name="diseasePic" accept="image/png,image/jpeg" onchange="fileChange(this);" class="required" htmlEscape="false" maxlength="50" />
			<span class="help-inline"><font color="red">*</font> </span>
			<span class="help-inline">图片大小不超过2MB,最多上传1张</span>

		</div>
	</div>





	<div class="form-actions">
		<shiro:hasPermission name="sys:menu:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
		<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
	</div>
</form:form>

</body>
</html>