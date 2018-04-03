<%--
  Created by IntelliJ IDEA.
  User: yiqia
  Date: 2018/3/26 0026
  Time: 9:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="container">
    <h1>个人信息</h1>
    <form name="studentInfo" id="studentInfo" enctype="multipart/form-data">
        <img id="photo" src=" value="${student.studentPortrait}"" style="width:200px;height:200px;">
        <%--<p hidden="hidden">ID:<input type="text" name="studentId" value="${student.studentId}"/></p>--%>
        <%--<p>姓名:<input type="text" name="studentName" value="${student.studentName}"/></p>--%>
        <%--<p>QQ:<input type="text" name="studentQq" value="${student.studentQq}"/></p>--%>
        <%--<p>修真类型:<input type="text" name="studentMajor" value="${student.studentMajor}"/></p>--%>
        <%--<p>入学时间:<input type="text" name="studentAdmissionTime" value="${student.studentAdmissionTime}"/></p>--%>
        <%--<p>毕业学校:<input type="text" name="studentGraduatedSchool" value="${student.studentGraduatedSchool}"/></p>--%>
        <%--<p>线上学号:<input type="text" name="studentOnlineId" value="${student.studentOnlineId}"/></p>--%>
        <%--<p>日报链接:<input type="text" name="studentDailyLinks" value="${student.studentDailyLinks}"/></p>--%>
        <%--<p>心愿:<input type="text" name="studentWishing" value="${student.studentWishing}"/></p>--%>
        <%--<p>师兄:<input type="text" name="studentCounselingBrother" value="${student.studentCounselingBrother}"/></p>--%>
        <%--<p>从何处了解:<input type="text" name="studentWhereKnow" value="${student.studentWhereKnow}"/></p>--%>
        <%--<p>手机号码:<input type="text" name="studentPhone" value="${student.studentPhone}"/></p>--%>
        <%--<p>邮箱:<input type="text" name="studentEmail" value="${student.studentEmail}"/></p>--%>
        <p>照片:<input type="file" name="studentPortrait" id="studentPortrait"/></p>
        <p><input type="button" name="b1" value="提交" onclick="fsubmit()"/></p>
    </form>
    <div id="result"></div>

</div>
<script type="text/javascript">

    function fsubmit() {
        var form = document.getElementById("studentInfo");
        var fd = new FormData(form);
        $.ajax({
            url: "authority/upload",
            type: "POST",
            data: fd,
            processData: false,  // 告诉jQuery不要去处理发送的数据
            contentType: false,   // 告诉jQuery不要去设置Content-Type请求头
            success: function (url) {
                // alert("提交成功");
                console.log(url);
                $('#photo').attr("src", url);
            },
            error: function () {
                alert("提交失败");
            }
            // success: function (response, status, xhr) {
            //     console.log(xhr);
            //     var json = $.parseJSON(response);
            //     var result = '';
            //     result += "个人信息：<br/>name:" + json['name'] + "<br/number:" + json['number'];
            //     result += '<br/>头像：<img src="' + json['photo'] + '" height="100" style="border-radius: 50%;" />';
            //     $('#result').html(result);
            // }
        });
        return false;
    }
</script>