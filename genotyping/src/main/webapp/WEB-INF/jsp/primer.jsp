<%--
  Created by IntelliJ IDEA.
  User: 27142
  Date: 2021/4/7
  Time: 17:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>PrimerDesign</title>
    <link rel="stylesheet" type="text/css" href="css/primerpage.css" />
    <script type="text/javascript" src="js/upload.js"></script>
</head>

<body class="body_primer">

<div class="topLayer">
    <div class="logo">
        <a style="padding: 16px 8px;" href="/home">Icon</a>
    </div>
    <div class="navigationBar">
        <a class="navigator" href="/primerdesign" target="_blank">PrimerDesign</a>
        <a class="navigator" href="/home" target="_blank">Genotyping</a>
    </div>
</div>

<div class="interlayer_primer">
    <div class="primer-introBlock">
        <p class="primer_title">Introduction</p>
        <p class="primer_intro">
            In this page, provide at least  two value (template sequence and snp index in the sequence) to us.
            And the values in "Default Setting" block all are default which you can choose to identify them or not.
        </p>
    </div>
    <form class="primer_form" method="post" action="/doPrimer" enctype="multipart/form-data">
        <div>
            <p class="primer_title" style="font-weight: bolder;">User Input</p>
            <div style="margin: 20px 20px;">
                <input type="file" name="primerFile" required="required"/> <a href="../demoFiles/testSeq.txt" download="" style="color: #00a4ff;" onmouseover="this.style.color='rgb(0,90,255)';" onmouseout="this.style.color='#00a4ff';">download text_Seq.txt</a>
                <div style="margin-top: 20px;">
                    <select name="strategy">
                        <option value="1">strategy1</option>
                        <option value="2">strategy2</option>
                        <option value="3">strategy3</option>
                    </select>
                </div>
            </div>
        </div>
        <div>
            <p class="primer_title" style="font-weight: bolder;">Default Setting</p>
            <div style="margin: 20px 20px;">
                <span>Length of primers</span><br /><br />
                <div class="primer_setting">
                    <div style="margin-right: 150px;">
                        <span>Minimum:&nbsp;&nbsp;</span><input type="text" name="priMin" required="required" value="18" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                    </div>
                    <div style="margin-right: 150px;">
                        <span>Optimum:&nbsp;&nbsp;</span><input type="text" name="priOpt" required="required" value="20" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                    </div>
                    <div>
                        <span>Maximum:&nbsp;&nbsp;</span><input type="text" name="priMax" required="required" value="25" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                    </div>
                </div>
                <br />
                <span>Length of product</span><br /><br />
                <div class="primer_setting">
                    <div style="margin-right: 150px;">
                        <span>Minimum:&nbsp;&nbsp;</span><input type="text" name="proMin" required="required" value="180" />
                    </div>
                    <div style="margin-right: 150px;">
                        <span>Maximum:&nbsp;&nbsp;</span><input type="text" name="proMax" required="required" value="400" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
                    </div>
                </div>
                <br />
                <span>Tm of primers (&#8451)</span><br /><br />
                <div class="primer_setting">
                    <div style="margin-right: 150px;">
                        <span>Minimum:&nbsp;&nbsp;</span><input type="text" name="tmMin" required="required" value="50.0" onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')" />
                    </div>
                    <div style="margin-right: 150px;">
                        <span>Optimum:&nbsp;&nbsp;</span><input type="text" name="tmOpt" required="required" value="60.0" onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')" />
                    </div>
                    <div>
                        <span>Maximum:&nbsp;&nbsp;</span><input type="text" name="tmMax" required="required" value="65.0" onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')" />
                    </div>
                </div>
                <br />
            </div>
        </div>
            <button type="submit" style="display:block;margin:0 auto; width: 100px; height: 30px; border-radius: 4px;">Pick Primers</button><br /><br />
    </form>

</div>

<footer>
    &copy; 2021 Li Lab
</footer>

</body>
</html>

