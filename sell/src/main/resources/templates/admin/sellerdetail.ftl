<!doctype html>
<html>

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <title>商户管理</title>
    <link href="/sell/ept/css/bootstrap.min.css" rel="stylesheet">
    <link href="/sell/ept/css/common.css" rel="stylesheet">
    <link href="/sell/ept/css/main.css" rel="stylesheet">

</head>

<body>
<div id="container" class="container-fluid wrapOut">

            <#include "nav1.ftl">

    <div class="mainWrap">
        <div class="main">
            <div class="row dataM1 peopleM4 teachDetail">
                <div class="col-md-12">
                    <div class="panel peopPan1 peopPanM2">
                        <div class="panel-heading clearfix">
                            <div class="title">详情</div>
                            <div class="controlBtns pull-right">
                                <a href="javascript:void(0)" class="save_btn" onclick="submitForm()">保存</a>
                                <a class="back_btn" href="javascript:history.back(-1);">返回</a>
                            </div>
                        </div>
                        <div class="panel-body">
                            <div class="pMainWrap addTeachM clearfix">
                                <form class="form-horizontal" id="detail" action="/sell/seller/save" method="post" enctype="multipart/form-data">

                                    <div class="form-group static"><label for="exampleInputName" class="col-md-3 control-label">身份证：</label>
                                        <div class="col-md-9">
                                            <img src="${icon}" name="sellerIcon" style="height：300px;width:500px">
                                        </div>
                                    </div>
                                    <div class="form-group static"><label for="exampleInputName" class="col-md-3 control-label">用户名：</label>
                                        <div class="col-md-9">
                                            <input type="text" name="sellerName" class="form-control" id="exampleInputName" placeholder="" value="${name}">
                                        </div>
                                    </div>
                                    <div class="form-group static"><label for="exampleInputName2" class="col-md-3 control-label">商户名称：</label>
                                        <div class="col-md-9">
                                            <input type="text" name="sellerDname" class="form-control" id="exampleInputName2" placeholder="" value="${merchantName}">
                                        </div>
                                    </div>
                                    <div class="form-group static"><label for="exampleInputName4" class="col-md-3 control-label">银行卡号：</label>
                                        <div class="col-md-9"><input type="text" name="sellerIdcard" class="form-control" id="exampleInputName4" placeholder="" value="${idCard}"></div>
                                    </div>
                                    <div class="form-group static"><label for="exampleInputName5" class="col-md-3 control-label">私钥：</label>
                                        <div class="col-md-9"><input type="text" name="sellerSecret" class="form-control" id="exampleInputName5" placeholder="" value="${key}"></div>
                                    </div>

                                    <div class="form-group static"><label for="exampleInputName6" class="col-md-3 control-label">税点：</label>
                                        <div class="col-md-9"><input type="text" name="point" class="form-control" id="exampleInputName6" placeholder="" value="${point}"></div>
                                    </div>
                                    <input hidden type="text" name="sellerid" value="${id}"/>
                                </form>
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
    function submitForm() {
        var form = document.getElementById('detail');
        form.submit();
    }

</script>
<script type="text/javascript">
    function imgPreview(fileDom) {
        //判断是否支持FileReader
        if(window.FileReader) {
            var reader = new FileReader();
        } else {
            alert("您的浏览器不支持图片预览功能，请使用google浏览器或其他浏览器！");
        }

        //获取文件
        var file = fileDom.files[0];
        var imageType = /^image\//;
        //是否是图片
        if(!imageType.test(file.type)) {
            alert("请选择图片！");
            return;
        }
        //读取完成
        reader.onload = function(e) {
            //获取图片dom
            var img = document.getElementById("dishPic");
            //图片路径设置为读取的图片
            img.src = e.target.result;
        };
        reader.readAsDataURL(file);
    }
</script>
</body>

</html>