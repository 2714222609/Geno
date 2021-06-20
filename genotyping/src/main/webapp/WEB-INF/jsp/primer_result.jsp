<%--
  Created by IntelliJ IDEA.
  User: 27142
  Date: 2021/4/7
  Time: 20:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>PrimerResults</title>
    <link rel="stylesheet" type="text/css" href="css/primerpage.css" />
</head>

<body class="body_primer">

<div class="topLayer">
    <div class="logo">
        <a style="padding: 16px 8px;" href="/home">Icon</a>
    </div>
    <div class="navigationBar">
        <a class="navigator" href="./homePage.html" target="_blank">PrimerDesign</a>
        <a class="navigator" href="./indexUpload.html" target="_blank">Genotyping</a>
    </div>
</div>

<div class="interlayer_primer">
    <div>
        <p class="primer_title" align="center" style="font-weight: bolder;font-size: 24.72px;">Primer Results</p>
        <div style="margin: 20px 20px;">
            <div>
                <span style="display: inline-block;margin-left: 24px;">Demo for the results:</span>
                <a href="../${tempFileName}/primerOutPutPath/${xlsxFileName}" download="" style="float: right; margin-right: 10px; display: inline-block; color: #00a4ff" onmouseover="this.style.color='rgb(0,90,255)';" onmouseout="this.style.color='#00a4ff';">Download the primers file</a>
                <a href="../${tempFileName}/primerOutPutPath/${txtFileName}" download="" style="float: right; margin-right: 10px; display: inline-block; color: #00a4ff" onmouseover="this.style.color='rgb(0,90,255)';" onmouseout="this.style.color='#00a4ff';">Download the barcode file</a>
            </div>
            <div style="border: 1px solid white; border-radius: 9px; padding: 24px;">
                <p style="font-weight: bold; margin: 0;">
                    Product Szie<span style="font-size: 15px; font-weight: normal; color: gray;">&nbsp;</span>
                </p><br />
                <div class="primer_setting">
                    <div style="margin-right: 150px;">
                        <span>${ProductSize0} bp</span>
                    </div>
                </div>
                <br />
                <p style="font-weight: bold; margin: 0;">
                    Sequence<span style="font-size: 15px; font-weight: normal; color: gray;">&nbsp;</span>
                </p><br />
                <div class="primer_setting">
                    <div style="margin-right: 150px;">
                        <span>Primer_Left Sequence:&nbsp;&nbsp;</span>
                        <span>${PrimerLeftSequence0}</span>
                    </div>
                </div>
                <div class="primer_setting" style="margin-top: 5px;">
                    <div style="margin-right: 150px;">
                        <span>Primer_Right Sequence:&nbsp;&nbsp;</span>
                        <span>${PrimerRightSequence0}</span>
                    </div>
                </div>
                <br />
                <p style="font-weight: bold; margin: 0;">
                    Position and length<span style="font-size: 15px; font-weight: normal; color: gray;">&nbsp;(5' position of both primer)</span>
                </p><br />
                <div class="primer_setting">
                    <div style="margin-right: 150px;">
                        <span>Primer_Left Position:&nbsp;&nbsp;</span>
                        <span>${PrimerLeftPosition0}</span><span>&nbsp;&nbsp;(${PrimerLeftPositionbp0} bp)</span>
                    </div>
                    <div style="margin-right: 150px;">
                        <span>Primer_Right Position:&nbsp;&nbsp;</span>
                        <span>${PrimerRightPosition0}</span><span>&nbsp;&nbsp;(${PrimerRightPositionbp0} bp)</span>
                    </div>
                </div>
                <br />
                <p style="font-weight: bold; margin: 0;">
                    Tm<span style="font-size: 15px; font-weight: normal; color: gray;">&nbsp;</span>
                </p><br />
                <div class="primer_setting">
                    <div style="margin-right: 150px;">
                        <span>Primer_Left Tm:&nbsp;&nbsp;</span>
                        <span>${PrimerLeftTm0} &#8451</span>
                    </div>
                    <div style="margin-right: 150px;">
                        <span>Primer_Right Tm:&nbsp;&nbsp;</span>
                        <span>${PrimerRightTm0} &#8451</span>
                    </div>
                </div>
                <br />
                <p style="font-weight: bold; margin: 0;">
                    GC (%)<span style="font-size: 15px; font-weight: normal; color: gray;">&nbsp;</span>
                </p><br />
                <div class="primer_setting">
                    <div style="margin-right: 150px;">
                        <span>Primer_Left GC:&nbsp;&nbsp;</span>
                        <span>${PrimerLeftGC0} %</span>
                    </div>
                    <div style="margin-right: 150px;">
                        <span>Primer_Right GC:&nbsp;&nbsp;</span>
                        <span>${PrimerRightGC0} %</span>
                    </div>
                </div>
            </div>
            <br />
        </div>
    </div>
</div>

<footer>
    &copy; 2021 Li Lab
</footer>

</body>
</html>
