<html>
<#include "../common/header.ftl">

<body>
<div id="wrapper" class="toggled">

<#--边栏sidebar-->
<#include "../common/nav.ftl">

<#--主要内容content-->
    <div id="page-content-wrapper">
        <div class="container-fluid">
            <div class="row clearfix">
                <div class="col-md-12 column">
                    <form role="form" method="post" action="/sell/seller/product/save" enctype="multipart/form-data">
                        <div class="form-group">
                            <label>名称</label>
                            <input name="productName" type="text" class="form-control" value="${(productInfo.productName)!''}"/>
                        </div>
                        <div class="form-group">
                            <label>价格</label>
                            <input name="productPrice" type="text" class="form-control" value="${(productInfo.productPrice)!''}"/>
                        </div>
                        <div class="form-group">
                            <label>库存</label>
                            <input name="productStock" type="number" class="form-control" value="${(productInfo.productStock)!''}"/>
                        </div>
                        <div class="form-group">
                            <label>描述</label>
                            <input name="productDescription" type="text" class="form-control" value="${(productInfo.productDescription)!''}"/>
                        </div>
                        <div class="form-group">
                            <label>图片</label>
                            <input id="file-0" class="file" type="file"  onchange="imgPreview(this)"  name="multipartFile" data-min-file-count="1">
                        </div>


                        <div class="form-group">
                            <label>类目</label>
                            <select name="categoryType" class="form-control">
                                <#list categoryList as category>
                                    <option value="${category.categoryType}"
                                            <#if (productInfo.categoryType)?? && productInfo.categoryType == category.categoryType>
                                                selected
                                            </#if>
                                        >${category.categoryName}
                                    </option>
                                </#list>
                            </select>
                        </div>
                        <input hidden type="text" name="productId" value="${(productInfo.productId)!''}">
                        <button type="submit" class="btn btn-default">提交</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

</div>
</body>
<script>
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
        var curSize = file.size;
        //是否是图片
        if(!imageType.test(file.type)) {
            alert("请选择图片！");
            return;
        }
        //1、判断图片大小
        if(curSize>1024*1024)
        {
            alert("图片大小不能超过"+1024+"KB！");
            return;
        }
        //读取完成
        reader.onload = function(e) {
            //获取图片dom
            var img = document.getElementById("file-0");
            //图片路径设置为读取的图片
            img.src = e.target.result;
        };
        reader.readAsDataURL(file);
    }
</script>

</html>