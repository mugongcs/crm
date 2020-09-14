<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <title>Title</title>

    <script>
        //AJAX模板
        $.ajax({
            url : "",
            data : {

            },
            type : "",
            datatype : "json",
            success : function(data){

            }
        });

        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
    </script>
</head>
<body>

</body>
</html>

