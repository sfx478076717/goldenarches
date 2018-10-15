<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

	<link rel="stylesheet" href="${APP_PATH}/bootstrap/css/bootstrap.min.css">
	<link rel="stylesheet" href="${APP_PATH}/css/font-awesome.min.css">
	<link rel="stylesheet" href="${APP_PATH}/css/main.css">
	<style>
        .tree li {
            list-style-type: none;
            cursor:pointer;
        }
	    table tbody tr:nth-child(odd){background:#F4F4F4;}
	</style>
  </head>

  <body>

    <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
        <%@include file="/WEB-INF/jsp/common/header.jsp"%>
    </nav>

    <div class="container-fluid">
      <div class="row">
        <div class="col-sm-3 col-md-2 sidebar">
			<div class="tree">
				<%@include file="/WEB-INF/jsp/common/menu.jsp"%>
			</div>
        </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<div class="panel panel-default">
			  <div class="panel-heading">
				<h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
			  </div>
			  <div class="panel-body">
          <div class="table-responsive">
            <form id="memberForm">
            <table class="table ">
              <thead>
                <tr >
                    <th width="50">#</th>
                    <th>会员名</th>
                    <th>手机号</th>
                    <th ><a class="btn" style="padding: 0px;font-weight: bold" onclick="balanceClick()">账户余额</a></th>
                    <th ><a class="btn" style="padding: 0px;font-weight: bold" onclick="createTimeClick()">创建时间</a></th>
                </tr>
              </thead>
              
              <tbody id="memberData">
                  
              </tbody>
              
			  <tfoot>
			     <tr >
				     <td colspan="9" align="center">
						<ul class="pagination"></ul>
					 </td>
				 </tr>

			  </tfoot>
            </table>
            </form>
          </div>
			  </div>
			</div>
        </div>
      </div>
    </div>
    <script src="${APP_PATH}/jquery/jquery-2.1.1.min.js" ></script>
    <script src="${APP_PATH}/bootstrap/js/bootstrap.min.js"></script>
	<script src="${APP_PATH}/script/docs.min.js"></script>
	<script src="${APP_PATH}/layer/layer.js"></script>
    <script type="text/javascript">
        var searchFlag = false;
        var balanceFlag=false;
        var cdescflag=true;
        var bdescflag=true;
        $(function () {
            $(".list-group-item").click(function () {
                if ($(this).find("ul")) {
                    $(this).toggleClass("tree-closed");
                    if ($(this).hasClass("tree-closed")) {
                        $("ul", this).hide("fast");
                    } else {
                        $("ul", this).show("fast");
                    }
                }
            });
            /**分页查询**/
            pageQuery(1);

        });
        /**根据创建时间排序**/
        function createTimeClick() {
            balanceFlag=false;
            cdescflag=!cdescflag;
            pageQuery(1);
        }
        /**根据账户余额排序**/
        function balanceClick() {
            balanceFlag =true;
            bdescflag=!bdescflag;
            pageQuery(1);
        }
        /***分页查询构建表格***/
        function pageQuery( pageno ) {
            var loadingIndex = null;
            var jsonData = {"pageno" : pageno};
            if(balanceFlag){
                if (bdescflag){
                    jsonData.orderText="vip_balance desc";
                }else {
                    jsonData.orderText="vip_balance"
                }
            }else {
                if(cdescflag){
                    jsonData.orderText="create_time desc";
                }else{
                    jsonData.orderText="create_time";
                }
            }
            $.ajax({
                type : "POST",
                url  : "${APP_PATH}/member/getAll",
                data : jsonData,
                beforeSend : function(){
                    loadingIndex = layer.msg('处理中', {icon: 16});
                },
                success : function(result) {
                    layer.close(loadingIndex);
                    console.log(result.extend.pageInfo); ///测试，t
                    if ( result.code==100 ) {
                        // 局部刷新页面数据
                        var tableContent = "";
                        var pageContent = "";
                        var pages =result.extend.pageInfo.pages;
                        var members=result.extend.pageInfo.list;
                        $.each(members, function(i, member){
                            tableContent += '<tr>';
                            tableContent += '  <td>'+(i+1)+'</td>';
                            tableContent += '  <td>'+member.name+'</td>';
                            tableContent += '  <td>'+member.phone+'</td>';
                            tableContent += '  <td>'+member.balance+'</td>';
                            tableContent += '  <td>'+member.createTime+'</td>';
                            tableContent += '</tr>';
                        });

                        if ( pageno > 1 ) {
                            pageContent += '<li><a href="#" onclick="pageQuery('+(pageno-1)+')">上一页</a></li>';
                        }

                        for ( var i = 1; i <= pages; i++ ) {
                            if ( i == pageno ) {
                                pageContent += '<li class="active"><a  href="#">'+i+'</a></li>';
                            } else {
                                pageContent += '<li ><a href="#" onclick="pageQuery('+i+')">'+i+'</a></li>';
                            }
                        }

                        if ( pageno < pages ) {
                            pageContent += '<li><a href="#" onclick="pageQuery('+(pageno+1)+')">下一页</a></li>';
                        }

                        $("#memberData").html(tableContent);
                        $(".pagination").html(pageContent);
                    } else {
                        layer.msg("商品信息查询失败", {time:2000, icon:5, shift:6}, function(){

                        });
                    }
                }
            });
        }
    </script>
  </body>
</html>
