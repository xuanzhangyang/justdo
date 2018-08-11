<!doctype html>
<html>
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width,initial-scale=1">
        <#--<meta http-equiv="refresh" content="5">-->
		<title>订单管理</title>
        <object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0>
            <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed>
        </object>
		<link href="/sell/ept/css/bootstrap.min.css" rel="stylesheet">
		<link href="/sell/ept/css/common.css" rel="stylesheet">
		<link href="/sell/ept/css/main.css" rel="stylesheet">
	</head>

	<body>
		<div id="container" class="container-fluid wrapOut">
		<#include "../common/nav1.ftl">
			<div class="mainWrap">
				<div class="main">
					<div class="row peopleM1 peopleM3 resourceM">
						<div class="col-md-12">
							<div class="panel peopPan1">
								<div class="panel-heading">
									<div class="title">已接订单</div>

								</div>
								<div class="panel-body">
									<div class="pMainWrap clearfix">
										<div class="tableWrap">
											<table class="table table-striped table-bordered table-hover">
												<thead>
													<tr>
														<th>桌号</th>
														<#--<th>订单编号</th>-->
														<th>订单金额</th>
														<th>下单时间</th>
														<th>操作</th>
													</tr>
												</thead>
												<tbody class="tds">

													<#list orderDTOPage.content as orderDTO>
                                                    <tr>
                                                        <td>${orderDTO.buyerAddress}</td>
                                                        <#--<td>${orderDTO.orderId}</td>-->
                                                        <td>${orderDTO.orderAmount}</td>
                                                        <td>${orderDTO.createTime}</td>
                                                        <#--<td>${orderDTO.getOrderStatusEnum().message}</td>-->
                                                        <#--<td>${orderDTO.getPayStatusEnum().message}</td>-->
                                                        <td class="btns">

                                                            <a class="edit_Btn" href="#" onclick="detail('${orderDTO.orderId}');return false;" data-toggle="modal" data-target="#orderDetail">详情</a>
                                                            <#--<a class="confirm_Btn"  href="#" onclick="test('${orderDTO.orderId}')" >点击JSON</a>-->

															<#if orderDTO.isprint  <  1>
                                                             <a class="confirm_Btn" href="/sell/seller/order/list?OrderId=${orderDTO.orderId}"  onclick="prn1_print('${orderDTO.orderId}',this);return false;" id="printa">打印</a>
                                                            <#else>
                                                             <a class="confirm_Btn" href="/sell/seller/order/list?OrderId=${orderDTO.orderId}"   onclick="prn1_print('${orderDTO.orderId}',this);return false;" id="printa">已打印</a>
                                                            </#if>
														</td>
                                                    </tr>
													</#list>
												</tbody>
											</table>

											<div class="page">
											<#if currentPage lte 1>
                                                <a class="prev" href="#">上一页</a>
											<#else>
                                               <a class="prev" href="/sell/seller/order/list?page=${currentPage - 1}&size=${size}">上一页</a>
											</#if>
												<#list widths as index>
													<#if currentPage == index>
														<a class="active" href="#">${index}</a>
													<#else>
													    <#if currentPage lte 0>
															<a href="/sell/seller/order/list?page=${index}&size=${size}">${index}</a>
														<#else>
															<a href="/sell/seller/order/list?page=${index}&size=${size}">${index}</a>
														</#if>


													</#if>
												</#list>

											<#if currentPage gte orderDTOPage.getTotalPages()>
                                                <a class="next" href="#">下一页</a>
											<#else>
                                                <a class="next" href="/sell/seller/order/list?page=${currentPage + 1}&size=${size}">下一页</a>
											</#if>

												<div class="count"><span>共&nbsp;${orderDTOPage.getTotalPages()}&nbsp;页</span> <span>当前第&nbsp;${currentPage}&nbsp;页</span> </div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="modal fade" id="orderDetail" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true" data-backdrop="static">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
							<h5 class="modal-title" id="myModalLabel">订单详情</h5>
						</div>
						<div class="modal-body">
							<div class="panel-body">
								<div class="pMainWrap clearfix">
									<div class="tableWrap">
										<table id="product" class="table table-striped table-bordered table-hover">
											<thead>
												<tr>
													<th>菜品</th>
													<th>数量/份</th>
													<th>金额/元</th>
                                                    <th>商品备注</th>
												</tr>
											</thead>
											<tbody class="tds">
											</tbody>
										</table>

                                        <h3 id="desc" style="color: #2c2c2c "></h3>
									</div>
								</div>
							</div>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-primary" data-dismiss="modal">确定</button>
						</div>
					</div>
				</div>
			</div>

            <div rows="15" cols="80" id="printDetial" style="display: none;">
                <table width="183px" id="table" style="font-size: 12px;">
                </table>
            </div>

        <script src="/sell/ept/js/jquery-2.0.3.min.js"></script>
        <script src="/sell/ept/js/bootstrap.min.js"></script>
        <script src="/sell/ept/js/WdatePicker/WdatePicker.js"></script>
        <script src="/sell/ept/js/common.js"></script>
		<script language="javascript" src="/sell/js/LodopFuncs.js"></script>


        <script>

            //定时10秒
            setInterval('refreshQuery()',20000);
            //只要有要打印的订单就刷新表格，并且设置订单的打印状态
            function refreshQuery(){
                $.getJSON("/sell/seller/order/GetData",function(data){
                    var flag = data.flag;
                    //刷新表格
                    if(!flag) window.location.reload(true);

                })
            };


            // function test(id){
            //     $.getJSON("/sell/seller/order/GetData",{orderId:id},function(data){
            //         var list = data.list;
            //         var flag = data.flag;
            //         alert(data.flag);
            //         alert(data.list[0].orderId);
            // })
            // };
            //

            function detail(id) {
                $.getJSON("/sell/seller/order/detail",{orderId:id},function(data){
                    var details=data.orderDetails;
                    var desc = data.buyerDesc;
                    var tb = document.getElementById('product');
                    var showdesc = document.getElementById('desc')
                    var rowNum=tb.rows.length;
                    for (i=0;i<rowNum;i++)
                    {
                        tb.deleteRow(i);
                        rowNum=rowNum-1;
                        i=i-1;
                    }

                    for(var i in details){
                        var currentRows = document.getElementById("product").rows.length;

                        var insertTr = document.getElementById("product").insertRow(currentRows);
                        var insertTd = insertTr.insertCell(0);
                        insertTd.style.textAlign="center";
                        insertTd.innerHTML = details[i].productName;

                        insertTd = insertTr.insertCell(1);
                        insertTd.style.textAlign="center";
                        insertTd.innerHTML = details[i].productQuantity ;
                        insertTd = insertTr.insertCell(2);
                        insertTd.style.textAlign="center";
                        insertTd.innerHTML = details[i].productPrice;

                    }

                    showdesc.innerText = "备注:"+desc;

                });
            }
            var LODOP; //声明为全局变量
            var myDetial;
            function prn1_print(id,obj) {
                $.ajaxSettings.async = false;
                // $.getJSON("/sell/seller/order/list",{orderId:id},function(data){};
                myDetial = new Array();
                $.getJSON("/sell/seller/order/detail",{orderId:id},function(data){
                    var details=data.orderDetails;
                    myDetial[0]=data.orderId;
                    myDetial[1]=data.buyerAddress;
                    myDetial[2]=data.createTime;
                    myDetial[3]=0;
                    myDetial[4]=data.buyerDesc;
                    var tb = document.getElementById('table');
                    var rowNum=tb.rows.length;
                    for (i=0;i<rowNum;i++)
                    {
                        tb.deleteRow(i);
                        rowNum=rowNum-1;
                        i=i-1;
                    }
                    for(var i in details){
                        var table=document.getElementById("table");
                        table.style.width="183px";
                        table.style.height="60px";
                        var currentRows = document.getElementById("table").rows.length;
                        var insertTr = document.getElementById("table").insertRow(currentRows);
                        var insertTd = insertTr.insertCell(0);
                        insertTd.style.textAlign="center";
                        insertTd.innerHTML = details[i].productName;

                        insertTd = insertTr.insertCell(1);
                        insertTd.style.textAlign="center";
                        insertTd.innerHTML = details[i].productQuantity ;
                        insertTd = insertTr.insertCell(2);
                        insertTd.style.textAlign="center";
                        insertTd.innerHTML = details[i].productPrice;
                        //计算订单金额
                        myDetial[3]+=details[i].productQuantity*details[i].productPrice;
                    }
                });
                $.ajaxSettings.async = false;
                PrintTable(myDetial);
                LODOP.PRINT();
                obj.innerHTML="已打印";
            };

            function prn1_preview(id) {
                PrintTable();
                LODOP.PREVIEW();
            };

            function PrintTable(myDetial) {
                LODOP = getLodop(document.getElementById('LODOP_OB'), document.getElementById('LODOP_EM'));
                var table=document.getElementById("table");
                var tableLength=table.rows.length;
                LODOP.PRINT_INIT("");
                LODOP.SET_PRINT_PAGESIZE(3, '58mm', (tableLength + 15) + "mm", 2);

                var height=20;
                LODOP.ADD_PRINT_TEXT(10, 40, 110, height, "欢迎光临本店就餐");
                LODOP.ADD_PRINT_LINE(30, 0, 30, 183, 2, 0);
                LODOP.ADD_PRINT_LINE(32, 0, 32, 183, 2, 0);
                LODOP.ADD_PRINT_TEXT(40, 0, 183, height, "销售单号："+myDetial[0]);
                //桌号
                LODOP.ADD_PRINT_TEXT(60,0,183,height,"桌号："+myDetial[1]);
                LODOP.ADD_PRINT_LINE(80, 0, 80, 183, 2, 0);
                LODOP.ADD_PRINT_TEXT(82,0,60,height,"菜品");
                LODOP.ADD_PRINT_TEXT(82,80,50,height,"份数");
                LODOP.ADD_PRINT_TEXT(82,130,60,height,"金额/元");
                LODOP.ADD_PRINT_HTM(102, 0, "100%", "100%", document.getElementById("printDetial").innerHTML);
                var tableEnd=103 + tableLength * 33;
                LODOP.ADD_PRINT_LINE(tableEnd, 0, tableEnd, 183, 2, 0);
                tableEnd+=10;
                //				订单总额
                LODOP.ADD_PRINT_TEXT(tableEnd, 0, 183, height, "总计金额："+myDetial[3]+"元");
                //				点单时间
                LODOP.ADD_PRINT_TEXT(tableEnd+20, 0, 183, height, "点单时间："+timestampToTime(myDetial[2]));
                //               备注
                LODOP.ADD_PRINT_TEXT(tableEnd+40, 0, 183, height, "备注："+myDetial[4]);
                //				谢谢惠顾
                LODOP.ADD_PRINT_TEXT(tableEnd+60, 60, 63, height, "谢谢惠顾");
                LODOP.ADD_PRINT_TEXT(tableEnd+80, 50, 83, height, "欢迎下次光临");
                LODOP.ADD_PRINT_TEXT(tableEnd+100, 0, 180, height, "请妥善保管收银条如需发票凭此单在服务台办理");
            };

            function timestampToTime(timestamp) {
                var date = new Date(timestamp * 1000);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
                Y = date.getFullYear() + '-';
                M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
                D = date.getDate() + ' ';
                h = date.getHours() + ':';
                m = date.getMinutes() + ':';
                s = date.getSeconds();
                return Y+M+D+h+m+s;
            }
        </script>

	</body>


</html>