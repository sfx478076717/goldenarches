<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="header.jsp" %>
<html>
<head>
    <title>注册/充值</title>
</head>
<body>
<hr/>
<form action="${APP_PATH}/addForm" method="post">
    <table style="margin: 0 auto">
        <tr>
            <td><label>姓名: </label></td>
            <td><input type="text" id="name" name="name"></td>
        </tr>
        <tr>
            <td><label>手机号: </label></td>
            <td><input type="text" id="phone" name="phone"></td>
        </tr>
        <tr>
            <td><label>充值金额: </label></td>
            <td><input type="text" id="balance" name="balance"></td>
        </tr>

        <tr>
            <td></td>
            <td><input id="submit" type="submit" value="注册/充值"></td>
        </tr>
    </table>
</form>

</body>
</html>
