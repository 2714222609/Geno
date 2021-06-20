<%--
  Created by IntelliJ IDEA.
  User: 27142
  Date: 2021/4/28
  Time: 22:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Genotyping</title>
    <link rel="stylesheet" type="text/css" href="css/contact.css" />
</head>
<body>

<div class="navi_bar">
    <div class="logo">
        <a style="padding: 16px 8px;" href="/home">Icon</a>
    </div>
    <div class="navigator">
        <a class="navi" href="./primer_submit.html" target="_blank">PrimerDesign</a>
        <a class="navi" href="./genotyping_submit.html" target="_blank">Genotyping</a>
    </div>
</div>

<div class="content_area">
    <div class="content_box">

        <div class="description_line">
            <p>
                If you have some questions need to contact us, please send your questions with Email address here.
                We will respond as soon as possible.
            </p>
        </div>

        <form action="/sendFeedBack" method="post" enctype="multipart/form-data">
            <div class="txt_line">
                <label>
                    <input type="email" style="width: 300px;" name="userEmailAddr" placeholder="Your Email Address" />
                </label>
            </div>
            <div class="txt_line">
                <textarea name="userFeedBack" placeholder="something you wanna tell us"></textarea>
            </div>
            <button type="submit" style="display:block; margin:20px auto; width: 100px; height: 30px; border-radius: 4px;" value="Send">Submit</button>
        </form>


    </div>
</div>

<footer>
    &copy; 2021 Li Lab
</footer>

</body>
</html>
