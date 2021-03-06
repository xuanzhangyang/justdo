<!doctype html>
<html>

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <title>营业额统计</title>
    <link href="/sell/ept/css/bootstrap.min.css" rel="stylesheet">
    <link href="/sell/ept/css/common.css" rel="stylesheet">
    <link href="/sell/ept/css/main.css" rel="stylesheet">
    <style type="text/css">
        .modal {
            font-family: "微软雅黑";
            color: #090909;
        }

        #inp_div {
            float: left;
        }

        #sub_div {
            float: right;
        }

        #startModal:after {
            content: "";
            display: block;
            height: 0;
            clear: both;
            zoom: 1;
        }
    </style>
</head>

<body>
<div id="container" class="container-fluid wrapOut">
			<#include "nav1.ftl">

    <div class="mainWrap">
        <div class="main">
            <div class="row dataM1">
                <div class="col-md-12">
                    <div class="panel dataMPan1">
                        <div class="panel-heading">
                            <div class="title">筛选条件</div>
                        </div>
                        <div class="panel-body">
                            <div class="dataCTop dataQTop clearfix">
                                <form id="turnover" method="get" action="/sell/admin/turnover">
                                    <div class="dataCTop2 clearfix"><span class="name">营业额</span>
                                        <div class="btn-group">
                                            <select class="form-control" name="date">
                                                <option value="1" <#if (((date)!'') == 1)>selected="selected"</#if> >今日营业额</option>
                                                <option value="7" <#if (((date)!'') == 7)>selected="selected"</#if> >本周营业额</option>
                                                <option value="30" <#if (((date)!'') == 30)>selected="selected"</#if> >本月营业额</option>
                                            </select>
                                        </div>
                                    </div>
                                    <a class="search_btn"  onclick="turnover()" href="javascript:void(0)">查询</a>
                                </form>
                            </div>

                            <h2>商家营业额</h2>
                            <div class="panel-body">
                                <div class="pMainWrap clearfix">
                                    <div class="tableWrap">
                                        <table class="table table-striped table-bordered table-hover">
                                            <thead>
                                            <tr>
                                                <#--<th>商户名称</th>-->
                                                <th>姓名</th>
                                                <th>电话号码</th>
                                                <th>银行卡号</th>
                                                <th>营业额</th>
                                            </tr>
                                            </thead>
                                            <tbody class="tds">

														<#list sellerDTOs as sellerInfo>
                                                        <tr>
                                                            <#--<td>${sellerInfo.merchantName}</td>-->
                                                            <td>${sellerInfo.name}</td>
                                                            <td>${sellerInfo.phone}</td>
                                                            <td>${sellerInfo.idCard}</td>
                                                            <td>${(sellerInfo.turnover)}</td>
                                                        </tr>
														</#list>
                                            </tbody>
                                        </table>
                                        <div class="page">
													<#if currentPage lte 1>
		                                                <a class="prev" href="#">上一页</a>
													<#else>
		                                               <a class="prev" href="/sell/admin/turnover?page=${currentPage - 1}&size=${size}">上一页</a>
													</#if>

														<#list 1..sellerInfoPage.getTotalPages() as index>
															<#if currentPage == index>.
																<a class="active" href="#">${index}</a>
															<#else>
																<#if currentPage lte 0>
																	<a href="/sell/admin/turnover?page=${index}&size=${size}">${index}</a>
																</#if>
															</#if>
														</#list>
													<#if currentPage gte sellerInfoPage.getTotalPages()>
		                                                <a class="next" href="#">下一页</a>
													<#else>
		                                                <a class="next" href="/sell/seller/order/turnover?page=${currentPage + 1}&size=${size}">下一页</a>
													</#if>
                                            <div class="count"><span>共&nbsp;${sellerInfoPage.getTotalPages()}&nbsp;页</span> <span>当前第&nbsp;${currentPage}&nbsp;页</span> </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="/sell/ept/js/jquery-1.11.3.min.js"></script>
<script src="/sell/ept/js/bootstrap.min.js"></script>
<script src="/sell/ept/js/WdatePicker/WdatePicker.js"></script>
<script src="/sell/ept/js/common.js"></script>
<script>
    $(function() {})
</script>
<script>
    function turnover() {
        $('#turnover').submit();
    }
</script>


</body>

</html>