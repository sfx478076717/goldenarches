<%--
  Created by IntelliJ IDEA.
  User: SAMSUNG-PC
  Date: 2018/9/30
  Time: 21:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>图片上传页面</title>

</head>
<body>
<div class="form-group">
    <label for="name"  class="col-sm-3 control-label">上传头像</label>
    <div class="col-sm-8">
        <img id="image" class="cover-radius"  src="${basePath}/upload_img.png"
             width="100%" style="cursor: pointer;" />
        <input id="picture_upload" name="file" type="file" onchange="upload_cover(this)"
               style="position: absolute; left: 0px; top: 0px; width: 100%; height: 100%; opacity: 0; cursor: pointer;"/>
        <small class="help-block cover-tips" style="color: #dd4b39;display: none;">请上传照片</small>
    </div>
</div>
<script src="${APP_PATH}/jquery/jquery-2.1.1.min.js"></script>
<script src="${APP_PATH}/script/ajaxfileupload.js" type="text/javascript"></script>
<script type="text/javascript">
    function upload_cover(obj) {
        ajax_upload(obj.id, function(data) { //function(data)是上传图片的成功后的回调方法
            var isrc = data.relatPath.replace(/\/\//g, '/'); //获取的图片的绝对路径
            $('#image').attr('src', basePath+isrc).data('img-src', isrc); //给<input>的src赋值去显示图片
        });
    }
    function ajax_upload(feid, callback) { //具体的上传图片方法
        if (image_check(feid)) { //自己添加的文件后缀名的验证
            $.ajaxFileUpload({
                fileElementId: feid,    //需要上传的文件域的ID，即<input type="file">的ID。
                url: '${APP_PATH}/upload', //后台方法的路径
                type: 'post',   //当要提交自定义参数时，这个参数要设置成post
                dataType: 'json',   //服务器返回的数据类型。可以为xml,script,json,html。如果不填写，jQuery会自动判断。
                secureuri: false,   //是否启用安全提交，默认为false。
                async : true,   //是否是异步
                success: function(data) {   //提交成功后自动执行的处理函数，参数data就是服务器返回的数据。
                    if (callback) callback.call(this, data);
                },
                error: function(data, status, e) {  //提交失败自动执行的处理函数。
                    console.error(e);
                }
            });
        }
    }
    function image_check(feid) { //自己添加的文件后缀名的验证
        var img = document.getElementById(feid);
        return /.(jpg|png|gif|bmp)$/.test(img.value)?true:(function() {
            modals.info('图片格式仅支持jpg、png、gif、bmp格式，且区分大小写。');
            return false;
        })();
    }
</script>


<div id="adddlg" class="easyui-dialog" style="width:400px;height:400px;padding:10px 20px" closed="true" buttons="#dlg-buttons">
    <form id="usercodeform" method="post" enctype="multipart/form-data">
        <div class="fitem">
            <label>用户ID:</label>
            <input id="userId"  name="id"  required="true" class="easyui-textbox" missingMessage="请填写内容！" style="width:248px;"/>
        </div>
        <div class="fitem">
            <label>Code: </label>
            <input id="userCode"  name="name"  required="true" class="easyui-textbox" missingMessage="请填写内容！" style="width:248px;"/>
        </div>
        <div class="fitem">
            <label>二维码:</label>
            <input id="codeUrl" name="codeUrl" accept="image/*" onchange="onupload(this)" type="file" style="width:200px" />
            <div id="xmTanDiv" style="text-align:center;">
                <img id="Img" width="180px" height="180px"src="${basePath}/img/upload.png"/>
            </div>
        </div>
    </form>    
    <a href="#" onclick="uploadImg()">新增</a>
</div>
<script src="${APP_PATH}/jquery/jquery-2.1.1.min.js"></script>
<script src="${APP_PATH}/script/ajaxfileupload.js" type="text/javascript"></script>
<%--<script type="text/javascript">--%>
<%--function imageUpload(){--%>
<%--$.ajaxFileUpload({--%>
<%--url : 'uploadSong.do', //用于文件上传的服务器端请求地址--%>
<%--fileElementId : 'file1', //文件上传空间的id属性  <input type="file" id="file" name="file" />--%>
<%--type : 'post',--%>
<%--dataType : 'text', //返回值类型 一般设置为json--%>
<%--success : function(data, status) //服务器成功响应处理函数--%>
<%--{--%>
<%--alert("歌曲上传成功");--%>

<%--},--%>
<%--error : function(data, status, e)//服务器响应失败处理函数--%>
<%--{--%>
<%--alert("歌曲上传失败");--%>

<%--}--%>
<%--});--%>
<%--}--%>
<%--</script>--%>



<script type="text/javascript">

    $.fn.serializeObject = function()
    {
        var o = {};
        var a = this.serializeArray();
        $.each(a, function() {
            if (o[this.name] !== undefined) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    };
    //选择图片，马上预览
    var image = '';
    function onupload(file) {
        if(!file.files || !file.files[0]){
            return;
        }
        var reader = new FileReader();
        //读取文件过程方法
        reader.onload = function (e) {
            document.getElementById('Img').src = e.target.result;
            image = e.target.result;
        };
        reader.readAsDataURL(file.files[0]);
    }


    //图片上传
    function uploadImg() {
        var dataobj=$('#usercodeform').serializeObject();
        alert(JSON.stringify(dataobj));
        var datajson=$.parseJSON(JSON.stringify(dataobj));
        alert(datajson);
        for (var i in datajson) {
            alert("i    "+i+"     data[i]   "+datajson[i]);
        }
        //dataobj={"id":"123","name":"tom2"};
        $.ajaxFileUpload({
            url : '${APP_PATH}/uploadIMG', //用于文件上传的服务器端请求地址
            fileElementId : 'codeUrl', //文件上传空间的id属性  <input type="file" id="file" name="file" />
            type : 'post',
            data:{"name":"tom"},
            dataType : 'json', //返回值类型 一般设置为json
            success : function(data, status) //服务器成功响应处理函数
            {
                alert("上传成功");

            },
            error : function(data, status, e)//服务器响应失败处理函数
            {
                alert("上传失败");
            }
        });
    }

</script>
</body>
</html>
