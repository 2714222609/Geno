<%--
  Created by IntelliJ IDEA.
  User: 27142
  Date: 2021/4/28
  Time: 22:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Genotyping</title>
    <link rel="stylesheet" type="text/css" href="css/references.css" />
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

    <div class="table_box">
        <h3>Software</h3>
        <table>
            <tr>
                <td class="name"><a href="http://bio-bwa.sourceforge.net/" target="_blank">BWA</a></td>
                <td class="version">Version: 0.7.17</td>
                <td>BWA is a software package for mapping low-divergent sequences
                    against a large reference genome</td>
            </tr>
            <tr>
                <td class="name"><a href="http://samtools.sourceforge.net/" target="_blank">Samtools</a></td>
                <td class="version">Version: 1.11</td>
                <td>SAM Tools provide various utilities for manipulating alignments
                    in the SAM format</td>
            </tr>
            <tr>
                <td class="name"><a href="https://spring.io/" target="_blank">Spring Boot Framework</a></td>
                <td class="version">Version: 2.4.5</td>
                <td>The Spring Framework provides a comprehensive programming and
                    configuration model for modern Java-based enterprise applications
                    - on any kind of deployment platform.</td>
            </tr>
        </table>
    </div>

    <div class="table_box">
        <h3>Genome Reference</h3>
        <table>
            <tr>
                <td class="name"><a href="http://www.gramene.org/" target="_blank">Maize</a></td>
                <td>B73 Genome Reference</td>
            </tr>
        </table>
    </div>

</div>

<footer>
    &copy; 2021 Li Lab
</footer>

</body>
</html>