<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<!DOCTYPE html>
<html>
<head>
    <title>课程资料</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- 引入bootstrap -->
    <link rel="stylesheet" type="text/css" href="/css/bootstrap.min.css">
    <!-- 引入Bootstrap Fileinput的CSS文件 -->
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-fileinput/4.4.7/css/fileinput.min.css">
    <!-- 引入JQuery  bootstrap.js-->
    <script src="/js/jquery-3.2.1.min.js"></script>
    <script src="/js/bootstrap.min.js"></script>
    <%--<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">--%>

</head>
<body>
<!-- 顶栏 -->

<!-- 中间主体 -->
<jsp:include page="top.jsp"></jsp:include>
<div class="container" id="content">
    <div class="row">
        <jsp:include page="menu.jsp"></jsp:include>
        <div class="col-md-10">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <div class="row">
                        <h1 class="col-md-4">课程资料</h1>
                        <%--根据日期搜索--%>
                        <form class="col-md-4" role="form" style="margin: 20px 0 10px 0;"
                              action="/teacher/selectResource" id="DateSearch" method="post">
                            <div class="input-group">
                                <input type="hidden" name="CourseId" id="CourseId2" value="">
                                <select class="form-control" name="findByDate" id="DateOption">
<%--                                    <option>请输入日期</option>--%>
                                    <c:set var="previousValue" value=""/>
                                    <c:forEach items="${courseResourceList}" var="item" varStatus="status">
                                        <c:if test="${item.gmtCreate ne previousValue}">
                                            <!-- 只显示不同的值 -->
                                            <option>${item.gmtCreate}</option>
                                        </c:if>
                                        <c:set var="previousValue" value="${item.gmtCreate}"/>
                                    </c:forEach>
                                </select>
                                <span class="input-group-addon btn"
                                      onclick="selectResourceByDate()"
                                      id="sub">搜索</span>
                            </div>
                        </form>
                        <form class="col-md-2 inline-block" action="/teacher/upload" method="post"
                              style="margin: 20px 0 10px 50px;" id="form2"
                              enctype="multipart/form-data">
                            <input type="hidden" name="CourseId" id="CourseId" value="">

                            <input type="file" name="fileUpload" id="fileUpload" style="display: inline-block">
                            <input type="submit" value="上传资料" style="display: inline-block">
                        </form>

                    </div>
                </div>
                <table id="MyTable" class="table table-bordered table-condensed" style="overflow-y: hidden">
                    <thead>
                    <tr>
                        <th>编号</th>
                        <th>职称</th>
                        <th>用户姓名</th>
                        <th>课程名字</th>
                        <th>发布日期</th>
                        <th>资料/图片</th>
                        <th>
                            操作
                            <button class="btn btn-default btn-xs btn-primary"
                                    id="batchDownload">
                                批量下载
                            </button>
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${courseResourceList}" var="item" varStatus="status">
                        <tr id="index${status.index+1}">
                            <td>${status.index+1}</td>
                            <td>
                                <c:if test="${item.isTeacher==0}">学生</c:if>
                                <c:if test="${item.isTeacher==1}">教师</c:if>
                            </td>
                            <td>${item.username}</td>
                            <td>${item.courseName}</td>
                            <td>${item.gmtCreate}</td>
                            <td><img class="img-rounded" style="width: 85px;height: 85px;" src="${item.photos}"></td>
                            <td>
                                <label>
                                        <%--批量下载--%>
                                    <input type="checkbox" class="checkbox" value="${item.id}">
                                </label>
                                <button class="btn btn-default btn-xs btn-primary"
                                        onClick="onceDownload(this,${item.id})">
                                    下载
                                </button>
                                <button class="btn btn-default btn-xs btn-danger btn-primary"
                                        onClick="deleteFunction(this,${item.id},${item.courseId})">
                                    删除
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <div class="panel-footer">
                    <%--分页 这里没用到--%>
                    <c:if test="${pagingVO != null}">
                        <nav style="text-align: center">
                            <ul class="pagination">
                                <li><a href="/teacher/showResource?page=${pagingVO.upPageNo}">&laquo;上一页</a></li>
                                <li class="active"><a href="">${pagingVO.curentPageNo}</a></li>
                                <c:if test="${pagingVO.curentPageNo+1 <= pagingVO.totalCount}">
                                    <li>
                                        <a href="/teacher/showResource?page=${pagingVO.curentPageNo+1}">${pagingVO.curentPageNo+1}</a>
                                    </li>
                                </c:if>
                                <c:if test="${pagingVO.curentPageNo+2 <= pagingVO.totalCount}">
                                    <li>
                                        <a href="/teacher/showResource?page=${pagingVO.curentPageNo+2}">${pagingVO.curentPageNo+2}</a>
                                    </li>
                                </c:if>
                                <c:if test="${pagingVO.curentPageNo+3 <= pagingVO.totalCount}">
                                    <li>
                                        <a href="/teacher/showResource?page=${pagingVO.curentPageNo+3}">${pagingVO.curentPageNo+3}</a>
                                    </li>
                                </c:if>
                                <c:if test="${pagingVO.curentPageNo+4 <= pagingVO.totalCount}">
                                    <li>
                                        <a href="/teacher/showResource?page=${pagingVO.curentPageNo+4}">${pagingVO.curentPageNo+4}</a>
                                    </li>
                                </c:if>
                                <li><a href="/teacher/showResource?page=${pagingVO.totalCount}">最后一页&raquo;</a></li>
                            </ul>
                        </nav>
                    </c:if>
                </div>
            </div>

        </div>
    </div>
</div>
<div class="container" id="footer">
    <div class="row">
        <div class="col-md-12"></div>
    </div>
</div>
</body>
<script type="text/javascript">
    $("#nav li:nth-child(1)").addClass("active")

    <c:if test="${pagingVO != null}">
    if (${pagingVO.curentPageNo} == ${pagingVO.totalCount}) {
        $(".pagination li:last-child").addClass("disabled")
    }
    ;

    if (${pagingVO.curentPageNo} == ${1}) {
        $(".pagination li:nth-child(1)").addClass("disabled")
    }
    ;
    </c:if>

    function confirmd() {
        var msg = "您真的确定要删除吗？！";
        if (confirm(msg) == true) {
            return true;
        } else {
            return false;
        }
    }

    $("#sub").click(function () {
        $("#form1").submit();
    });
    //jQuery的初始化
    $(document).ready(function () {
        // 获取<shiro:principal/>标签的内容
        var userInfo = $("shiro\\:principal").text();
        // 将用户信息填充到HTML元素中
        $("#UserId").text(+userInfo);
    });

    // 定义初始化函数
    function init() {
        // 在这里执行需要进行的初始化操作
        // 获取地址栏中的CourseId参数
        var queryString = window.location.search;

// 将查询字符串解析为参数对象
        var params = new URLSearchParams(queryString);

// 获取特定参数的值
        var CourseId = params.get('CourseId');
        document.getElementById("CourseId").value = CourseId;
        document.getElementById("CourseId2").value = CourseId;
    }

    // 当页面加载完成时调用初始化函数
    window.onload = init;

    //批量下载的func
    document.getElementById('batchDownload').addEventListener('click', function () {

        var ids = [];
        //获取所有被点击的多选框
        $('.checkbox:checked').each(function () {
            ids.push($(this).val());
        });

        if (Object.keys(ids).length !== 0) {
            console.log(JSON.stringify({ids: ids}));
            $.ajax({
                url: "/teacher/batchDownload",
                method: "POST",
                data: JSON.stringify({ids: ids}),
                contentType: 'application/json',
                xhrFields: {
                    responseType: 'blob'
                },
                success: function (data, status, xhr) {
                    console.log(data);
                    var blob = new Blob([data], {type: xhr.getResponseHeader("Content-Type")});
                    var link = document.createElement('a');
                    link.href = window.URL.createObjectURL(blob);
                    link.download = "downloaded_file.zip";
                    document.body.appendChild(link);
                    link.click();
                    document.body.removeChild(link);
                }
            });
        } else {
            alert("你还没选择批量下载的文件");
        }
    });

    //单次下载的func
    function onceDownload(row, id) {
        if (id !== "") {
            $.ajax({
                url: "/teacher/onceDownload?id=" + id,
                method: "GET",
                xhrFields: {
                    responseType: 'blob'
                },
                success: function (data, status, xhr) {
                    console.log(data);
                    var blob = new Blob([data], {type: xhr.getResponseHeader("Content-Type")});
                    var link = document.createElement('a');
                    link.href = window.URL.createObjectURL(blob);
                    link.download = "downloaded_file.zip";
                    document.body.appendChild(link);
                    link.click();
                    document.body.removeChild(link);
                }
            });
        }
    }

    //删除函数
    function deleteFunction(row, id, courseId) {
        console.log("删除中");
        <%--location.href='/teacher/removeResource?id=${item.id}&CourseId=${item.courseId}'--%>
        var getMethod = "/teacher/removeResource?id=" + id + "&CourseId=" + courseId;

        var i = row.parentNode.parentNode.rowIndex;
        document.getElementById("MyTable").deleteRow(i);

        window.location.href = getMethod;
    }

    //根据日期查数据
    function selectResourceByDate() {
        // 获取select标签元素
        var selectElement = document.getElementById('DateOption');

        // 获取当前选中的option的值
        var selectedOptionValue = selectElement.options[selectElement.selectedIndex].value;
        var courseId=document.getElementById("CourseId2").value;
        //某个补丁，别深究
        if (courseId=="" || courseId === null || courseId === undefined) {
            // 获取路径部分
            var path = window.location.pathname;
            // 使用正则表达式匹配 /selectResource/1/ 这部分内容
            var regex = /\/selectResource\/(\d+)\//;
            var match = path.match(regex);
            // 如果匹配成功，则获取到了对应的参数值
            if (match) {
                var paramValue = match[1];
                // console.log(paramValue); // 输出 "1"
                courseId=paramValue;
            }
        }

        var url="/teacher/selectResource/"+courseId+"/"+selectedOptionValue;
        // window.alert(courseId);
        window.location.href=url;
    }
</script>
<!-- 引入Bootstrap Fileinput的JS文件 -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-fileinput/4.4.7/js/fileinput.min.js"></script>
</html>
