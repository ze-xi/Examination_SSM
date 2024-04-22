<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
	<title>资料管理</title>

	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<!-- 引入bootstrap -->
	<link rel="stylesheet" type="text/css" href="/css/bootstrap.min.css">
	<!-- 引入Bootstrap Fileinput的CSS文件 -->
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-fileinput/4.4.7/css/fileinput.min.css">

	<!-- 引入JQuery  bootstrap.js-->
	<script src="/js/jquery-3.2.1.min.js"></script>
	<script src="/js/bootstrap.min.js"></script>

	<%--<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">--%>

</head>
<body>
	<!-- 顶栏 -->

	<!-- 中间主体 --><jsp:include page="top.jsp"></jsp:include>
	<div class="container" id="content">
		<div class="row">
			<jsp:include page="menu.jsp"></jsp:include>
			<div class="col-md-10">
				<div class="panel panel-default">
				    <div class="panel-heading">
						<div class="row">
					    	<h1 class="col-md-5">资料管理</h1>
							<%--根据日期搜索--%>
							<form class="col-md-5" role="form" style="margin: 20px 0 10px 0;"
								  action="/admin/selectResource" id="DateSearch" method="post">
								<div class="input-group">
									<input type="hidden" name="CourseId" id="CourseId2" value="">
									<select class="form-control" name="findByDate" id="DateOption">
										<%-- <option>请输入日期</option>--%>
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
<%--							<form class="inline-block col-md-2" action="/admin/upload"--%>
<%--								  method="post" enctype="multipart/form-data" style="margin: 20px 0 10px 50px;">--%>
<%--								<input type="file" name="fileUpload" id="fileUpload" >--%>
<%--								<input type="submit" value="上传资料">--%>
<%--							</form>--%>


						</div>
				    </div>
				    <table class="table table-bordered table-hover">
					        <thead>
					            <tr>
									<th>资料编号</th>
									<th>职称</th>
									<th>用户姓名</th>
									<th>课程名字</th>
									<th>发布日期</th>
									<th>资料/图片</th>
					            </tr>
					        </thead>
					        <tbody>
							<c:forEach  items="${courseResourceList}" var="item">
								<tr>
									<td>${item.id}</td>
									<td>
										<c:if test="${item.isTeacher==0}">学生</c:if>
										<c:if test="${item.isTeacher==1}">教师</c:if>
									</td>
									<td>${item.username}</td>
									<td>${item.courseName}</td>
									<td>${item.gmtCreate}</td>
									<td>
										<img style="height: 40px;width: 40px;" src="${item.photos}">
									</td>
									<td>
										<button class="btn btn-default btn-xs btn-danger btn-primary"
												onClick="location.href='/admin/removeResource?id=${item.id}'">删除</button>
									</td>
								</tr>
							</c:forEach>
					        </tbody>
				    </table>
				    <div class="panel-footer">
						<c:if test="${pagingVO != null && date == null}">
							<nav style="text-align: center">
								<ul class="pagination">
									<li><a href="/admin/showResource?page=${pagingVO.upPageNo}">&laquo;上一页</a></li>
									<c:if test="${pagingVO.curentPageNo > 1}">
										<li><a href="/admin/showResource?page=1">1</a></li>
									</c:if>
									<c:if test="${pagingVO.curentPageNo > 2}">
										<li><a href="/admin/showResource?page=2">2</a></li>
									</c:if>
									<c:if test="${pagingVO.curentPageNo > 3}">
										<li><a href="#">...</a></li>
									</c:if>
									<li class="active"><a href="">${pagingVO.curentPageNo}</a></li>
									<c:if test="${pagingVO.curentPageNo+1 <= pagingVO.totalCount}">
										<li><a href="/admin/showResource?page=${pagingVO.curentPageNo+1}">${pagingVO.curentPageNo+1}</a></li>
									</c:if>
									<c:if test="${pagingVO.curentPageNo+2 <= pagingVO.totalCount}">
										<li><a href="/admin/showResource?page=${pagingVO.curentPageNo+2}">${pagingVO.curentPageNo+2}</a></li>
									</c:if>
									<c:if test="${pagingVO.curentPageNo+3 <= pagingVO.totalCount}">
										<li><a href="/admin/showResource?page=${pagingVO.curentPageNo+3}">${pagingVO.curentPageNo+3}</a></li>
									</c:if>
									<c:if test="${pagingVO.curentPageNo+4 <= pagingVO.totalCount}">
										<li><a href="/admin/showResource?page=${pagingVO.curentPageNo+4}">${pagingVO.curentPageNo+4}</a></li>
									</c:if>
									<li><a href="/admin/showResource?page=${pagingVO.totalCount}">最后一页&raquo;</a></li>
								</ul>
							</nav>
						</c:if>
						<c:if test="${pagingVO != null && date != null}">
							<nav style="text-align: center">
								<ul class="pagination">
									<li><a href="/admin/selectResource/${date}/${pagingVO.upPageNo}">&laquo;上一页</a></li>
									<c:if test="${pagingVO.curentPageNo > 1}">
										<li><a href="/admin/selectResource/${date}/1">1</a></li>
									</c:if>
									<c:if test="${pagingVO.curentPageNo > 2}">
										<li><a href="/admin/selectResource/${date}/2">2</a></li>
									</c:if>
									<c:if test="${pagingVO.curentPageNo > 3}">
										<li><a href="#">...</a></li>
									</c:if>
									<li class="active"><a href="">${pagingVO.curentPageNo}</a></li>
									<c:if test="${pagingVO.curentPageNo+1 <= pagingVO.totalCount}">
										<li><a href="/admin/selectResource/${date}/${pagingVO.curentPageNo+1}">${pagingVO.curentPageNo+1}</a></li>
									</c:if>
									<c:if test="${pagingVO.curentPageNo+2 <= pagingVO.totalCount}">
										<li><a href="/admin/selectResource/${date}/${pagingVO.curentPageNo+2}">${pagingVO.curentPageNo+2}</a></li>
									</c:if>
									<c:if test="${pagingVO.curentPageNo+3 <= pagingVO.totalCount}">
										<li><a href="/admin/selectResource/${date}/${pagingVO.curentPageNo+3}">${pagingVO.curentPageNo+3}</a></li>
									</c:if>
									<c:if test="${pagingVO.curentPageNo+4 <= pagingVO.totalCount}">
										<li><a href="/admin/selectResource/${date}/${pagingVO.curentPageNo+4}">${pagingVO.curentPageNo+4}</a></li>
									</c:if>
									<li><a href="/admin/selectResource/${date}/${pagingVO.totalCount}">最后一页&raquo;</a></li>
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
		$("#nav li:nth-child(4)").addClass("active")

        <c:if test="${pagingVO != null}">
			if (${pagingVO.curentPageNo} == ${pagingVO.totalCount}) {
				$(".pagination li:last-child").addClass("disabled")
			};

			if (${pagingVO.curentPageNo} == ${1}) {
				$(".pagination li:nth-child(1)").addClass("disabled")
			};
        </c:if>

        function confirmd() {
            var msg = "您真的确定要删除吗？！";
            if (confirm(msg)==true){
                return true;
            }else{
                return false;
            }
        }

        $("#sub").click(function () {
            $("#form1").submit();
        });

		//根据日期查数据
		function selectResourceByDate() {
			// 获取select标签元素
			var selectElement = document.getElementById('DateOption');

			// 获取当前选中的option的值
			var selectedOptionValue = selectElement.options[selectElement.selectedIndex].value;


			var url="/admin/selectResource/"+selectedOptionValue+"/1";
			// window.alert(courseId);
			window.location.href=url;
		}
	</script>
<!-- 引入Bootstrap Fileinput的JS文件 -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-fileinput/4.4.7/js/fileinput.min.js"></script>
</html>
