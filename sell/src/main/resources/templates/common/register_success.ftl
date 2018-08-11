<html>
<head>
    <meta charset="utf-8">
    <title>成功提示</title>
    <link href="https://cdn.bootcss.com/bootstrap/3.0.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<div class="container">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <div class="alert alert-dismissable alert-success">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                <h2>
                    成功!
                </h2>
                <h3>注册成功，管理员可能会联系您审核资料，您也可主动联系管理员加快审核，管理员联系方式：</h3>
                <h2>${admin.phone}</h2>

                <h2><a href="/sell/seller/onSellerLogin">点击登录</a></h2>
            </div>
        </div>
    </div>
</div>

</body>

<script>
    <#--setTimeout('location.href="${url}"', 3000);-->
</script>

</html>