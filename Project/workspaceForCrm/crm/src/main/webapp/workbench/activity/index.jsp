<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

<script type="text/javascript">

	$(function(){
		$("#createBtn").click(function(){
            $(".time").datetimepicker({
                minView: "month",
                language:  'zh-CN',
                format: 'yyyy-mm-dd',
                autoclose: true,
                todayBtn: true,
                pickerPosition: "bottom-left"
            });
			//获取用户信息列表，为所有者下拉框口赋值
			$.ajax({
				url : "workbench/activity/getUserList.do",
				type : "get",
				datatype : "json",
				success : function(data){
					var html = "<option></option>";
					//遍历出来的每一个n是一个User对象
					$.each(data,function(i,n){
                        html += "<option value='"+n.id+"'>"+n.name+"</option>";
					})
					$("#create-owner").html(html);
                    var id = "${user.id}";
                    $("#create-owner").val(id);
				}
			})
			$("#createActivityModal").modal("show");
		})

		//保存添加按钮绑定事件，执行添加操作
        $("#saveBtn").click(function(){
            $.ajax({
                url : "workbench/activity/save.do",
                data : {
                    "owner" : $.trim($("#create-owner").val()),
                    "name" : $.trim($("#create-name").val()),
                    "startDate" : $.trim($("#create-startDate").val()),
                    "endDate" : $.trim($("#create-endDate").val()),
                    "cost" : $.trim($("#create-cost").val()),
                    "description" : $.trim($("#create-description").val())
                },
                type : "post",
                datatype : "json",
                success : function(data){

                    if(data.flag){
                        //添加成功，刷新市场活动信息列表(局部刷新)
                        pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
                        //清空模态窗口的数据
                        //$("#create-ActivityForm")[0].reset();
                        //关闭添加操作的模态窗口
                        $("#createActivityModal").modal("hide");
                    }else{
                        alert("添加市场活动失败");
                    }
                }
            })
        })

        pageList(1,2);

		//为查询绑定事件
        $("#searchBtn").click(function(){
        	//将查询信息保存到隐藏域中
        	$("#hidden-name").val($.trim($("#search-name").val()));
        	$("#hidden-owner").val($.trim($("#search-owner").val()));
        	$("#hidden-startDate").val($.trim($("#search-startDate").val()));
        	$("#hidden-endDate").val($.trim($("#search-endDate").val()));
            pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
        })

		//为全选复选框绑定时间
		$("#selectAll").click(function(){
			$("input[name=select]").prop("checked",this.checked);
		})
		$("#activityBody").on("click",$("input[name=select]"),function(){
			$("#selectAll").prop("checked",$("input[name=select]").length==$("input[name=select]:checked").length);
		})

        //为删除按钮绑定时间，执行市场活动删除操作
        $("#deleteBtn").click(function(){
            //找到复选框中所有选中的jquery对象
            var $select = $("input[name=select]:checked");
            if($select.length == 0){
                alert("请选择要删除的记录");
            }else{
                //询问是否要删除数据
                if(confirm("确定删除所选市场记录吗？")){
                    //拼接参数  url: workbench/activity/delete.do?id=xxxxx&id=xxxxx
                    var param = "";
                    //遍历$select中的每一个dom对象,获取值
                    for(var i = 0; i < $select.length ; i++){
                        param += "id="+$($select[i]).val();
                        if(i < $select.length-1 ){
                            param += "&";
                        }
                    }
                    $.ajax({
                        url : "workbench/activity/delete.do",
                        data : param,
                        type : "post",
                        datatype : "json",
                        success : function(data){
                            if(data.flag){
                                pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
                            }else{
                                alert("删除市场活动失败")
                            }
                        }
                    })
                }
            }
        })


        //为修改按钮绑定时间，打开修改操作的模态按钮
        $("#editBtn").click(function(){
            $(".time").datetimepicker({
                minView: "month",
                language:  'zh-CN',
                format: 'yyyy-mm-dd',
                autoclose: true,
                todayBtn: true,
                pickerPosition: "bottom-left"
            });
            var $select = $("input[name=select]:checked");

            if($select.length == 0){
                alert("请选择需要修改的记录");
            }else if($select.length > 1){
                alert("修改只能选择一条记录");
            }else{
                var id = $select.val();
                $.ajax({
                    url : "workbench/activity/getUserListAndActivity.do",
                    data : {
                        "id" : id
                    },
                    type : "get",
                    datatype : "json",
                    success : function(data){

                        //处理所有者的下拉条
                        var html = "<option></option>";
                        $.each(data.uList,function(i,n){
                            html += "<option value='"+n.id+"'>"+n.name+"</option>";
                        })
                        $("#edit-owner").html(html);

                        //处理单条Activity
                        $("#edit-id").val(data.a.id);
                        $("#edit-name").val(data.a.name);
                        $("#edit-owner").val(data.a.owner);
                        $("#edit-startDate").val(data.a.startDate);
                        $("#edit-endDate").val(data.a.endDate);
                        $("#edit-cost").val(data.a.cost);
                        $("#edit-description").val(data.a.description);

                        //打开修改操作的模态窗口
                        $("#editActivityModal").modal("show");

                    }
                })
            }
        })

        //为更新按钮绑定事件，执行市场活动的修改操作
        $("#updateBtn").click(function(){
            $.ajax({
                url : "workbench/activity/update.do",
                data : {
                    "id" : $.trim($("#edit-id").val()),
                    "owner" : $.trim($("#edit-owner").val()),
                    "name" : $.trim($("#edit-name").val()),
                    "startDate" : $.trim($("#edit-startDate").val()),
                    "endDate" : $.trim($("#edit-endDate").val()),
                    "cost" : $.trim($("#edit-cost").val()),
                    "description" : $.trim($("#edit-description").val())
                },
                type : "post",
                datatype : "json",
                success : function(data){
                    if(data.flag){
                        //修改成功，刷新市场活动信息列表(局部刷新)
                        pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
                            ,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
                        //关闭添加操作的模态窗口
                        $("#editActivityModal").modal("hide");
                    }else{
                        alert("修改市场活动失败");
                    }
                }
            })
        })

	});

    /**
     * @description 对前端页面的信息进行分页显示
     * @param pageNo 页码
     * @param pageSize 每页展示的记录数
     * @deprecated 函数入口：市场活动标签，添加、修改、删除操作，查询操作，分页组件
     */
	function pageList(pageNo,pageSize) {
	    //将全选复选框删除
        $("#selectAll").prop("checked",false);
		//获取隐藏域中的查询信息
		$("#search-name").val($.trim($("#hidden-name").val()));
		$("#search-owner").val($.trim($("#hidden-owner").val()));
		$("#search-startDate").val($.trim($("#hidden-startDate").val()));
		$("#search-endDate").val($.trim($("#hidden-endDate").val()));
        $.ajax({
            url : "workbench/activity/pageList.do",
            data : {
                "pageNo" : pageNo,
                "pageSize" : pageSize,
                "name" : $.trim($("#search-name").val()),
                "owner" : $.trim($("#search-owner").val()),
                "startDate" : $.trim($("#search-startDate").val()),
                "endDate" : $.trim($("#search-endDate").val())
            },
            type : "get",
            datatype : "json",
            success : function(data){
                var html = "";
                $.each(data.dataList,function(i,n){
                    html += '<tr class="active">';
                    html += '<td><input type="checkbox" name="select" value="'+n.id+'"/></td>';
                    html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>';
                    html += '<td>'+n.owner+'</td>';
                    html += '<td>'+n.startDate+'</td>';
                    html += '<td>'+n.endDate+'</td>';
                    html += '</tr>';
                })
                $("#activityBody").html(html);
                var totalPages = data.total%pageSize == 0 ? data.total/pageSize : parseInt(data.total/pageSize)+1;
                //数据处理完毕之后，结合分页查询插件，展示分页信息
                $("#activityPage").bs_pagination({
                    currentPage: pageNo,    // 页码
                    rowsPerPage: pageSize,  // 每页显示的记录条数
                    maxRowsPerPage: 20,     // 每页最多显示的记录条数
                    totalPages: totalPages, // 总页数
                    totalRows: data.total,  // 总记录条数

                    visiblePageLinks: 3, // 显示几个卡片

                    showGoToPage: true,
                    showRowsPerPage: true,
                    showRowsInfo: true,
                    showRowsDefaultInfo: true,

                    //该回调函数是在点击分页组件的时候触发
                    onChangePage : function(event, data){
                        pageList(data.currentPage , data.rowsPerPage);
                    }
                });
            }
        })
    }
</script>
</head>
<body>

	<%--隐藏域保存信息--%>
	<input type="hidden" id="hidden-name"/>
	<input type="hidden" id="hidden-owner"/>
	<input type="hidden" id="hidden-startDate"/>
	<input type="hidden" id="hidden-endDate"/>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">

		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="create-ActivityForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">

								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-marketActivitystartTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate" readonly/>
							</div>
							<label for="create-marketActivityendTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate" readonly/>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
                    <!--
                    data-dismiss--------关闭模态窗口
                    -->
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>



	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
                            <!--隐藏域，存储市场活动的id值-->
                            <input type="hidden" id="edit-id"/>

							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">
								  <%--<option>zhangsan</option>--%>
								  <%--<option>lisi</option>--%>
								  <%--<option>wangwu</option>--%>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-name">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startDate" readonly/>
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endDate" readonly/>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost"/>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>




	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>


	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-startDate"/>
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="search-endDate">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="searchBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
					<!--
					data-toggle="modal"-------表示触发该按钮将打开一个模态窗口
					data-target="#createActivityModal"--------表示打开模态窗口的对象名，通过  #+id 方式获得
					-->

				  <button type="button" class="btn btn-primary" id="createBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="selectAll"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
						<%--<tr class="active">--%>
							<%--<td><input type="checkbox" /></td>--%>
							<%--<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>--%>
                            <%--<td>zhangsan</td>--%>
							<%--<td>2020-10-10</td>--%>
							<%--<td>2020-10-20</td>--%>
						<%--</tr>--%>
                        <%--<tr class="active">--%>
                            <%--<td><input type="checkbox" /></td>--%>
                            <%--<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>--%>
                            <%--<td>zhangsan</td>--%>
                            <%--<td>2020-10-10</td>--%>
                            <%--<td>2020-10-20</td>--%>
                        <%--</tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">
				<div id="activityPage"></div>
			</div>
			
		</div>
		
	</div>
</body>
</html>