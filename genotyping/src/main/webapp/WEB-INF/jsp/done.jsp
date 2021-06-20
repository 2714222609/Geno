<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>预览</title>
</head>
<style>
    *{
        padding: 0;
        margin: 0;
    }
    img{
        width: 400px;
        height: 400px;
    }

    div{
        width: 15em;
        border-bottom: black 1px solid;
    }
</style>
<body>
<script type="text/javascript">
    let body=document.querySelector("body");
    var strs=new Array();
    let Filename=new Array();
    //获取png文件名字
    <c:forEach var="path" items="${paths}" varStatus="s">
    strs[${s.index}]="${path}"
    </c:forEach>

    <c:forEach var="name" items="${tempFilename}" varStatus="s">
    Filename[${s.index}]="${name}"
    </c:forEach>
    let tempFilename=""
    for (var i=0;i<Filename.length;i++){
        if (i==Filename.length-1){
            tempFilename=tempFilename+Filename[i]
        }
        else {tempFilename=tempFilename+Filename[i]+"-"}
    }
    console.log(tempFilename)

    for (var i=0;i<strs.length;i++){
        strs[i]="../img/"+tempFilename+"/"+strs[i]
    }


    for (var i=0;i<strs.length;i++){
        var img=document.createElement('img')
        img.src=strs[i]
        body.appendChild(img)
    }

    //打印表格
    var   print=new   Array();
    <c:forEach var="cell" items="${prs}" varStatus="s">
    print[${s.index}]="${cell}"
    </c:forEach>
    let counter=${counter};
    for (var i=0;i<counter;i++){
        var div =document.createElement('div');
        div.innerHTML=print[i];

        body.appendChild(div);
    }
</script>

</body>
</html>
