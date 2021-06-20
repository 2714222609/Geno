<%@ page import="java.io.OutputStream" %>
<%@ page import="java.io.FileOutputStream" %>
<%@ page import="java.security.Principal" %>
<%@ page import="java.io.PrintWriter" %>
<%@page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Flowchart3</title>
    <meta name="description" content="Interactive flowchart diagram implemented by GoJS in JavaScript for HTML."/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Copyright 1998-2020 by Northwoods Software Corporation. -->

    <script type="text/javascript" src="/js/go.js"></script>
    <link href='https://fonts.googleapis.com/css?family=Lato:300,400,700' rel='stylesheet' type='text/css'>
    <style>
        .bt {
            display: block;
            height: 38px;
            width: 200px;
            border: 2px solid #00a4ff;
            text-align: center;
            line-height: 38px;
            color: #00a4ff;
            background-color: white;
            font-size: 16px;
            font-weight: 700;
            margin-top: 5px;
            display: inline-block;
        }
        .bt:hover {
            background-color: #00a4ff;
            color: white;
        }
    </style>
    <script id="code">
        function init() {

            // make构建模板
            var $ = go.GraphObject.make;  // for conciseness in defining templates
            myDiagram =
                $(go.Diagram, document.getElementById("myDiagramDiv"),  // must name or refer to the DIV HTML element
                    {
                        // 每次画线后调用的事件：为条件连线加上标签
                        "LinkDrawn": showLinkLabel,  // this DiagramEvent listener is defined below
                        // 每次重画线后调用的事件
                        "LinkRelinked": showLinkLabel,
                        // 启用Ctrl-Z和Ctrl-Y撤销重做功能
                        "undoManager.isEnabled": true,  // enable undo & redo
                        // 居中显示内容
                        initialContentAlignment: go.Spot.Center,
                        // 是否允许从Palette面板拖入元素
                        allowDrop: true,
                    });

            // 当图有改动时，在页面标题后加*，且启动保存按钮
            myDiagram.addDiagramListener("Modified", function (e) {
                var button = document.getElementById("SaveButton");
                if (button) button.disabled = !myDiagram.isModified;
                var idx = document.title.indexOf("*");
                if (myDiagram.isModified) {
                    if (idx < 0) document.title += "*";
                } else {
                    if (idx >= 0) document.title = document.title.substr(0, idx);
                }
            });

            // 设置节点位置风格，并与模型"loc"属性绑定，该方法会在初始化各种节点模板时使用
            function nodeStyle() {
                return [
                    // 将节点位置信息 Node.location 同节点模型数据中 "loc" 属性绑定：
                    // 节点位置信息从 节点模型 "loc" 属性获取, 并由静态方法 Point.parse 解析.
                    // 如果节点位置改变了, 会自动更新节点模型中"loc"属性, 并由 Point.stringify 方法转化为字符串
                    new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),
                    {
                        // 节点位置 Node.location 定位在节点的中心
                        locationSpot: go.Spot.Center
                    }
                ];
            }
            // 创建"port"方法，"port"是一个透明的长方形细长图块，在每个节点的四个边界上，如果鼠标移到节点某个边界上，它会高亮
            // "name": "port" ID，即GraphObject.portId,
            // "align": 决定"port" 属于节点4条边的哪条
            // "spot": 控制连线连入/连出的位置，如go.Spot.Top指, go.Spot.TopSide
            // "output" / "input": 布尔型，指定是否允许连线从此"port"连入或连出
            function makePort(name, align, spot, output, input) {
                // 表示如果是上，下，边界则是水平的"port"
                var horizontal = align.equals(go.Spot.Top) || align.equals(go.Spot.Bottom);
                return $(go.Shape,
                    {
                        fill: "transparent",  // 默认透明不现实
                        strokeWidth: 0,  // 无边框
                        width: horizontal ? NaN : 8,  // 垂直"port"则8像素宽
                        height: !horizontal ? NaN : 8,  // 水平"port"则8像素
                        alignment: align,  // 同其节点对齐
                        stretch: (horizontal ? go.GraphObject.Horizontal : go.GraphObject.Vertical),//自动同其节点一同伸缩
                        portId: name,  // 声明ID
                        fromSpot: spot,  // 声明连线头连出此"port"的位置
                        fromLinkable: output,  // 布尔型，是否允许连线从此"port"连出
                        toSpot: spot,  // 声明连线尾连入此"port"的位置
                        toLinkable: input,  // 布尔型，是否允许连线从此"port"连出
                        cursor: "pointer",  // 鼠标由指针改为手指，表示此处可点击生成连线
                        mouseEnter: function (e, port) {  //鼠标移到"port"位置后，高亮
                            if (!e.diagram.isReadOnly) port.fill = "rgba(255,0,255,0.5)";
                        },
                        mouseLeave: function (e, port) {// 鼠标移出"port"位置后，透明
                            port.fill = "transparent";
                        }
                    });
            }
            // 定义图形上的文字风格
            function textStyle() {
                return {
                    font: "bold 11pt Lato, Helvetica, Arial, sans-serif",
                    stroke: "#F8F8F8"
                }
            }

            // 定义输入节点的模板
            myDiagram.nodeTemplateMap.add("Input",
                $(go.Node, "Table", nodeStyle(),
                    // 步骤节点是一个包含可编辑文字块的长方形图块
                    $(go.Panel, "Auto",
                        $(go.Shape, "Rectangle",
                            {fill: "#000079", stroke: "#00A9C9", strokeWidth: 3.5},
                            new go.Binding("figure", "figure")),
                        $(go.TextBlock, textStyle(),
                            {
                                margin: 8,
                                maxSize: new go.Size(160, NaN),
                                wrap: go.TextBlock.WrapFit,// 尺寸自适应
                                editable: true// 文字可编辑
                            },
                            new go.Binding("text").makeTwoWay())// 双向绑定模型中"text"属性
                    ),
                    // 上、左、右可以入，左、右、下可以出
                    // "Top"表示中心，"TopSide"表示上方任一位置，自动选择
                    makePort("T", go.Spot.Top, go.Spot.TopSide, false, true),
                    makePort("L", go.Spot.Left, go.Spot.LeftSide, true, true),
                    makePort("R", go.Spot.Right, go.Spot.RightSide, true, true),
                    makePort("B", go.Spot.Bottom, go.Spot.BottomSide, true, false)
                ));
            //定义输出节点模板
            myDiagram.nodeTemplateMap.add("Output",
                $(go.Node, "Table", nodeStyle(),
                    // 步骤节点是一个包含可编辑文字块的长方形图块
                    $(go.Panel, "Auto",
                        $(go.Shape, "Rectangle",
                            {fill: "#282c34", stroke: "#00A9C9", strokeWidth: 3.5},
                            new go.Binding("figure", "figure")),
                        $(go.TextBlock, textStyle(),
                            {
                                margin: 8,
                                maxSize: new go.Size(160, NaN),
                                wrap: go.TextBlock.WrapFit,// 尺寸自适应
                                editable: true// 文字可编辑
                            },
                            new go.Binding("text").makeTwoWay())// 双向绑定模型中"text"属性
                    ),
                    // 上、左、右可以入，左、右、下可以出
                    // "Top"表示中心，"TopSide"表示上方任一位置，自动选择
                    makePort("T", go.Spot.Top, go.Spot.TopSide, false, true),
                    makePort("L", go.Spot.Left, go.Spot.LeftSide, true, true),
                    makePort("R", go.Spot.Right, go.Spot.RightSide, true, true),
                    makePort("B", go.Spot.Bottom, go.Spot.BottomSide, true, false)
                ));
            // 定义默认节点的模板
            myDiagram.nodeTemplateMap.add("",
                $(go.Node, "Table", nodeStyle(),
                    // 步骤节点是一个包含可编辑文字块的长方形图块
                    $(go.Panel, "Auto",
                        $(go.Shape, "Rectangle",
                            {fill: "#282c34", stroke: "#00A9C9", strokeWidth: 3.5},
                            new go.Binding("figure", "figure")),
                        $(go.TextBlock, textStyle(),
                            {
                                margin: 8,
                                maxSize: new go.Size(160, NaN),
                                wrap: go.TextBlock.WrapFit,// 尺寸自适应
                                editable: true// 文字可编辑
                            },
                            new go.Binding("text").makeTwoWay())// 双向绑定模型中"text"属性
                    ),
                    // 上、左、右可以入，左、右、下可以出
                    // "Top"表示中心，"TopSide"表示上方任一位置，自动选择
                    makePort("T", go.Spot.Top, go.Spot.TopSide, false, true),
                    makePort("L", go.Spot.Left, go.Spot.LeftSide, true, true),
                    makePort("R", go.Spot.Right, go.Spot.RightSide, true, true),
                    makePort("B", go.Spot.Bottom, go.Spot.BottomSide, true, false)
                ));
            // 定义条件节点的模板
            myDiagram.nodeTemplateMap.add("Conditional",
                $(go.Node, "Table", nodeStyle(),
                    // 条件节点是一个包含可编辑文字块的菱形图块
                    $(go.Panel, "Auto",
                        $(go.Shape, "Diamond",
                            {fill: "#282c34", stroke: "#00A9C9", strokeWidth: 3.5},
                            new go.Binding("figure", "figure")),
                        $(go.TextBlock, textStyle(),
                            {
                                margin: 8,
                                maxSize: new go.Size(160, NaN),
                                wrap: go.TextBlock.WrapFit,
                                editable: true
                            },
                            new go.Binding("text").makeTwoWay())
                    ),
                    // 上、左、右可以入，左、右、下可以出
                    makePort("T", go.Spot.Top, go.Spot.Top, false, true),
                    makePort("L", go.Spot.Left, go.Spot.Left, true, true),
                    makePort("R", go.Spot.Right, go.Spot.Right, true, true),
                    makePort("B", go.Spot.Bottom, go.Spot.Bottom, true, false)
                ));
            //定义算法模板
            myDiagram.nodeTemplateMap.add("algorithm",
                $(go.Node, "Table", nodeStyle(),
                    $(go.Panel, "Auto",
                        $(go.Shape, "RoundedRectangle",
                            {fill: "green", stroke: "#00A9C9", strokeWidth: 3.5},
                            new go.Binding("figure", "figure")),
                        $(go.TextBlock, textStyle(),
                            {
                                margin: 8,
                                maxSize: new go.Size(160, NaN),
                                wrap: go.TextBlock.WrapFit,// 尺寸自适应
                                editable: false// 文字不可编辑
                            },
                            new go.Binding("text").makeTwoWay())// 双向绑定模型中"text"属性
                    ),
                    // 上、左、右可以入，左、右、下可以出
                    // "Top"表示中心，"TopSide"表示上方任一位置，自动选择
                    makePort("T", go.Spot.Top, go.Spot.TopSide, false, true),
                    makePort("L", go.Spot.Left, go.Spot.LeftSide, true, true),
                    makePort("R", go.Spot.Right, go.Spot.RightSide, true, true),
                    makePort("B", go.Spot.Bottom, go.Spot.BottomSide, true, false)
                ));
            // 定义开始节点的模板
            myDiagram.nodeTemplateMap.add("Start",
                $(go.Node, "Table", nodeStyle(),
                    $(go.Panel, "Spot",
                        $(go.Shape, "Circle",
                            {desiredSize: new go.Size(70, 70), fill: "#282c34", stroke: "#09d3ac", strokeWidth: 3.5}),
                        $(go.TextBlock, "Start", textStyle(),
                            new go.Binding("text"))
                    ),
                    // 左、右、下可以出，但都不可入
                    makePort("L", go.Spot.Left, go.Spot.Left, true, false),
                    makePort("R", go.Spot.Right, go.Spot.Right, true, false),
                    makePort("B", go.Spot.Bottom, go.Spot.Bottom, true, false)
                ));
            // 定义结束节点的模板
            myDiagram.nodeTemplateMap.add("End",
                $(go.Node, "Table", nodeStyle(),
                    // 结束节点是一个圆形图块，文字不可编辑
                    $(go.Panel, "Spot",
                        $(go.Shape, "Circle",
                            {desiredSize: new go.Size(60, 60), fill: "#282c34", stroke: "#DC3C00", strokeWidth: 3.5}),
                        $(go.TextBlock, "End", textStyle(),
                            new go.Binding("text"))
                    ),
                    // 上、左、右可以入，但都不可出
                    makePort("T", go.Spot.Top, go.Spot.Top, false, true),
                    makePort("L", go.Spot.Left, go.Spot.Left, false, true),
                    makePort("R", go.Spot.Right, go.Spot.Right, false, true)
                ));

            // taken from ../extensions/Figures.js:
            go.Shape.defineFigureGenerator("File", function (shape, w, h) {
                var geo = new go.Geometry();
                var fig = new go.PathFigure(0, 0, true); // starting point
                geo.add(fig);
                fig.add(new go.PathSegment(go.PathSegment.Line, .75 * w, 0));
                fig.add(new go.PathSegment(go.PathSegment.Line, w, .25 * h));
                fig.add(new go.PathSegment(go.PathSegment.Line, w, h));
                fig.add(new go.PathSegment(go.PathSegment.Line, 0, h).close());
                var fig2 = new go.PathFigure(.75 * w, 0, false);
                geo.add(fig2);
                // The Fold
                fig2.add(new go.PathSegment(go.PathSegment.Line, .75 * w, .25 * h));
                fig2.add(new go.PathSegment(go.PathSegment.Line, w, .25 * h));
                geo.spot1 = new go.Spot(0, .25);
                geo.spot2 = go.Spot.BottomRight;
                return geo;
            });
            // 定义注释节点的模板
            myDiagram.nodeTemplateMap.add("Comment",
                // 注释节点是一个包含可编辑文字块的文件图块
                $(go.Node, "Auto", nodeStyle(),
                    $(go.Shape, "File",
                        {fill: "#282c34", stroke: "#DEE0A3", strokeWidth: 3}),
                    $(go.TextBlock, textStyle(),
                        {
                            margin: 8,
                            maxSize: new go.Size(200, NaN),
                            wrap: go.TextBlock.WrapFit,// 尺寸自适应
                            textAlign: "center",
                            editable: true// 文字可编辑
                        },
                        new go.Binding("text").makeTwoWay())
                    // 不支持连线入和出
                ));


            // 初始化连接线的模板
            myDiagram.linkTemplate =
                $(go.Link,  // 所有连接线
                    {
                        routing: go.Link.AvoidsNodes,// 连接线避开节点
                        curve: go.Link.JumpOver,
                        corner: 5, toShortLength: 4,// 直角弧度，箭头弧度
                        relinkableFrom: true,// 允许连线头重设
                        relinkableTo: true,// 允许连线尾重设
                        reshapable: true,// 允许线形修改
                        resegmentable: true,// 允许连线分割（折线）修改
                        // 鼠标移到连线上后高亮
                        mouseEnter: function (e, link) {
                            link.findObject("HIGHLIGHT").stroke = "rgba(30,144,255,0.2)";
                        },
                        mouseLeave: function (e, link) {
                            link.findObject("HIGHLIGHT").stroke = "transparent";
                        },
                        selectionAdorned: false
                    },
                    new go.Binding("points").makeTwoWay(), // 双向绑定模型中"points"数组属性
                    $(go.Shape,  // 隐藏的连线形状，8个像素粗细，当鼠标移上后显示
                        {isPanelMain: true, strokeWidth: 8, stroke: "transparent", name: "HIGHLIGHT"}),
                    $(go.Shape,  // 连线规格（颜色，选中/非选中，粗细）
                        {isPanelMain: true, stroke: "gray", strokeWidth: 2},
                        new go.Binding("stroke", "isSelected", function (sel) {
                            return sel ? "dodgerblue" : "gray";
                        }).ofObject()),
                    $(go.Shape,   // 箭头规格
                        {toArrow: "standard", strokeWidth: 0, fill: "gray"}),
                    $(go.Panel, "Auto",  // 连线标签，默认不显示
                        {visible: false, name: "LABEL", segmentIndex: 2, segmentFraction: 0.5},
                        new go.Binding("visible", "visible").makeTwoWay(),// 双向绑定模型中"visible"属性
                        $(go.Shape, "RoundedRectangle",  // 连线中显示的标签形状
                            {fill: "#F8F8F8", strokeWidth: 0}),
                        $(go.TextBlock, "Yes",  // // 连线中显示的默认标签文字
                            {
                                textAlign: "center",
                                font: "10pt helvetica, arial, sans-serif",
                                stroke: "#333333",
                                editable: true
                            },
                            new go.Binding("text").makeTwoWay()) // 双向绑定模型中"text"属性
                    )
                );

            // 此事件方法由整个画板的LinkDrawn和LinkRelinked事件触发
            // 如果连线是从"conditional"条件节点出发，则将连线上的标签显示出来
            function showLinkLabel(e) {
                var label = e.subject.findObject("LABEL");
                if (label !== null) label.visible = (e.subject.fromNode.data.category === "Conditional");
            }

            // 临时的连线（还在画图中），包括重连的连线，都保持直角
            myDiagram.toolManager.linkingTool.temporaryLink.routing = go.Link.Orthogonal;
            myDiagram.toolManager.relinkingTool.temporaryLink.routing = go.Link.Orthogonal;

            load();  // load an initial diagram from some JSON text

            // 在图形页面的左边初始化图例Palette面板
            myPalette =
                $(go.Palette, "myPaletteDiv", // 必须同HTML中Div元素id一致
                    {
                        // Instead of the default animation, use a custom fade-down
                        "animationManager.initialAnimationStyle": go.AnimationManager.None,
                        "InitialAnimationStarting": animateFadeDown, // 使用此函数设置动画

                        nodeTemplateMap: myDiagram.nodeTemplateMap,  // 同myDiagram公用一种node节点模板
                        model: new go.GraphLinksModel([  // 初始化Palette面板里的内容
                            {category: "Start", text: "Start"},
                            {category: "Input", text: "Input"},
                            {category: "Output", text: "Output"},
                            {text: "Text"},
                            {category: "Conditional", text: "???"},
                            {category: "End", text: "End"},
                            {category: "Comment", text: "Comment"},
                        ])
                    });

            myPalette =
                $(go.Palette, "myAlgorithmDiv", // 必须同HTML中Div元素id一致
                    {
                        // Instead of the default animation, use a custom fade-down
                        "animationManager.initialAnimationStyle": go.AnimationManager.None,
                        "InitialAnimationStarting": animateFadeDown, // 使用此函数设置动画

                        nodeTemplateMap: myDiagram.nodeTemplateMap,  // 同myDiagram公用一种node节点模板
                        model: new go.GraphLinksModel([  // 初始化Palette面板里的内容
                            {category: "algorithm", text: "SampleSplitAlgo"},
                            {category: "algorithm", text: "SplitThird"},
                            {category: "algorithm", text: "BWA"},
                            {category: "algorithm", text: "SAMTools-view"},
                            {category: "algorithm", text: "SAMTools-sort"},
                            {category: "algorithm", text: "SAMTools-index"},
                            {category: "algorithm", text: "IGVTools"},
                            {category: "algorithm", text: "UmiFreq"},
                            {category: "algorithm", text: "FilterSNP"},
                            {category: "algorithm", text: "Minima"}
                        ])
                    });

            // 动画效果
            function animateFadeDown(e) {
                var diagram = e.diagram;
                var animation = new go.Animation();
                animation.isViewportUnconstrained = true; // So Diagram positioning rules let the animation start off-screen
                animation.easing = go.Animation.EaseOutExpo;
                animation.duration = 900;
                // Fade "down", in other words, fade in from above
                animation.add(diagram, 'position', diagram.position.copy().offset(0, 200), diagram.position);
                animation.add(diagram, 'opacity', 0, 1);
                animation.start();
            }

        } // end init
        // 将go模型以JSon格式保存在文本框内
        function save() {
            document.getElementById("mySavedModel").value = myDiagram.model.toJson();
            myDiagram.isModified = false;
        }
        // 初始化模型范例
        function load() {
            myDiagram.model = go.Model.fromJson(document.getElementById("mySavedModel").value);
        }

    </script>
</head>
<body onload="init()">
<div id="sample">
    <div style="width: 100%; display: flex; justify-content: space-between">
<%--        <div id="myPaletteDiv" style="width: 200px;  margin-right: 2px; border: 5px solid darkgreen; background-color: skyblue;"></div>--%>
        <div id="myDiagramDiv" style="flex-grow: 1; height: 1000px; background-color: #282c34;"></div>
<%--        <div id="myAlgorithmDiv" style="width: 200px;  margin-right: 2px; border: 5px solid darkgreen; background-color: skyblue;"></div>--%>
    </div>
    <h4>You can turn to process2</h4>

    <form action="/getJson3" method="post" enctype="multipart/form-data">
<%--        <button id="SaveButton" onclick="save()" class="bt">Save</button>--%>
        <%--            <button onclick="load()" class="bt">Load</button>--%>
        <button type="submit" onclick="save()" class="bt">Run</button>
        <textarea id="mySavedModel" name="myJson" style="width:100%;height:300px;display: none;">
{ "class": "GraphLinksModel",
  "linkFromPortIdProperty": "fromPort",
  "linkToPortIdProperty": "toPort",
  "nodeDataArray": [
{"category":"Start", "text":"Start", "key":-1, "loc":"-283.4066192336909 -1069.940860562665"},
{"category":"Input", "text":"clean_1.fq\nclean_2.fq", "key":-2, "loc":"-283.4066192336911 -983.2434886573028"},
{"text":"reference.fa", "key":-4, "loc":"-318.6167183986796 -822.677387212901"},
{"category":"algorithm", "text":"BWA", "key":-8, "loc":"-165 -734"},
{"category":"Output", "text":"samples.sam", "key":-3, "loc":"-165 -674"},
{"category":"Output", "text":"reference.fa.amb\nreference.fa.ann\nreference.fa.bwt\nreference.fa.pac\nreference.fa.sa", "key":-6, "loc":"69 -734"},
{"category":"Output", "text":"Samples.bam", "key":-10, "loc":"-165 -550"},
{"category":"algorithm", "text":"Minima", "key":-17, "loc":"-328.2825562503036 -177.15986799656025"},
{"category":"Output", "text":"genotype.xlsx", "key":-18, "loc":"-424.06156030481725 -109.71928570793193"},
{"category":"End", "text":"End", "key":-19, "loc":"-318.0205201016057 2.551087890766212"},
{"category":"Comment", "text":"Genotyping Visual Process Self-definition", "key":-7, "loc":"45.27696775881532 -1024.2595585118331"},
{"category":"algorithm", "text":"SAMTools-view", "key":-9, "loc":"-165 -606.4444427490234"},
{"category":"algorithm", "text":"SAMTools-sort", "key":-20, "loc":"-165 -486.11111068725586"},
{"category":"Output", "text":"Samples.sorted.bam", "key":-21, "loc":"-165 -421.11111068725586"},
{"category":"algorithm", "text":"SAMTools-index", "key":-11, "loc":"91 -420.77777779102325"},
{"category":"Output", "text":"Samples.sorted.bam.bai", "key":-23, "loc":"90.79877853393555 -351.38888466358173"},
{"text":"demo.csv", "key":-24, "loc":"-328.2784554007002 -418.77141014628535"},
{"category":"Output", "text":"sample_0.fq\nsample_1.fq   (X96)", "key":-25, "loc":"-155.64088158858408 -820.9628918958309"},
{"category":"Output", "text":"genotype.png", "key":-26, "loc":"-232.60615270381868 -109.46171891944448"},
{"category":"algorithm", "text":"SplitThird", "key":-27, "loc":"-155 -909"},
{"text":"sample(n)_0.fq\nsample(n)_1.fq ", "key":-22, "loc":"-472.0536628401027 -420.74348209661355"},
{"category":"algorithm", "text":"UmiFreq", "key":-28, "loc":"-328.3851567583324 -328.3851567583324"},
{"category":"Output", "text":"sample.csv", "key":-29, "loc":"-328.3851567583323 -244.57852821063307"}
 ],
  "linkDataArray": [
{"from":-1, "to":-2, "fromPort":"B", "toPort":"T", "points":[-283.4066192336909,-1033.190860562665,-283.4066192336909,-1023.190860562665,-283.4066192336909,-1021.0298989141831,-283.4066192336911,-1021.0298989141831,-283.4066192336911,-1018.8689372657012,-283.4066192336911,-1008.8689372657012]},
{"from":-8, "to":-3, "fromPort":"B", "toPort":"T", "points":[-165,-712.6266133602211,-165,-702.6266133602211,-165,-702.619288125423,-165,-702.619288125423,-165,-702.611962890625,-165,-692.611962890625]},
{"from":-8, "to":-6, "fromPort":"R", "toPort":"L", "points":[-135.2704213436195,-734,-125.27042134361949,-734,-65.90266672649724,-734,-65.90266672649724,-734,-6.534912109375,-734,3.465087890625,-734]},
{"from":-17, "to":-18, "fromPort":"B", "toPort":"T", "points":[-340.86987806235885,-156.71071994320707,-340.86987806235885,-146.71071994320707,-340.86987806235885,-142.0588649776691,-424.06156030481725,-142.0588649776691,-424.06156030481725,-137.40701001213114,-424.06156030481725,-127.40701001213115]},
{"from":-18, "to":-19, "fromPort":"B", "toPort":"T", "points":[-424.06156030481725,-92.03156140373272,-424.06156030481725,-82.03156140373272,-424.06156030481725,-60.615236756483256,-318.0205201016057,-60.615236756483256,-318.0205201016057,-39.19891210923379,-318.0205201016057,-29.198912109233788]},
{"from":-3, "to":-9, "fromPort":"B", "toPort":"T", "points":[-165,-655.388037109375,-165,-645.388037109375,-165,-641.6029332490887,-165,-641.6029332490887,-165,-637.8178293888025,-165,-627.8178293888025]},
{"from":-9, "to":-10, "fromPort":"B", "toPort":"T", "points":[-165,-585.0710561092445,-165,-575.0710561092445,-165,-575.0710561092445,-165,-578.611962890625,-165,-578.611962890625,-165,-568.611962890625]},
{"from":-10, "to":-20, "fromPort":"B", "toPort":"T", "points":[-165,-531.388037109375,-165,-521.388037109375,-165,-519.4362672182049,-165,-519.4362672182049,-165,-517.4844973270349,-165,-507.4844973270349]},
{"from":-20, "to":-21, "fromPort":"B", "toPort":"T", "points":[-165,-464.73772404747695,-165,-454.73772404747695,-165,-452.2303988126789,-165,-452.2303988126789,-165,-449.7230735778809,-165,-439.7230735778809]},
{"from":-21, "to":-11, "fromPort":"R", "toPort":"L", "points":[-89.2727279663086,-421.11111068725586,-79.2727279663086,-421.11111068725586,-32.24733479938167,-421.11111068725586,-32.24733479938167,-420.77777779102325,14.778058367545256,-420.77777779102325,24.778058367545256,-420.77777779102325]},
{"from":-11, "to":-23, "fromPort":"B", "toPort":"T", "points":[91,-399.4043911512443,91,-389.4043911512443,91,-384.7026193527255,90.79877853393555,-384.7026193527255,90.79877853393555,-380.00084755420676,90.79877853393555,-370.00084755420676]},
{"from":-25, "to":-8, "fromPort":"B", "toPort":"T", "points":[-155.64088158858408,-795.3374432874325,-155.64088158858408,-785.3374432874325,-155.64088158858408,-778.8932956703928,-155.12921566353072,-778.8932956703928,-155.12921566353072,-772.4491480533532,-155.12921566353072,-754.4491480533532]},
{"from":-4, "to":-8, "fromPort":"B", "toPort":"T", "points":[-318.6167183986796,-804.9896629087018,-318.6167183986796,-794.9896629087018,-318.6167183986796,-779.7194054810275,-174.8707843364693,-779.7194054810275,-174.8707843364693,-764.4491480533532,-174.8707843364693,-754.4491480533532]},
{"from":-17, "to":-26, "fromPort":"B", "toPort":"T", "points":[-315.69523443824835,-156.71071994320707,-315.69523443824835,-146.71071994320707,-315.69523443824835,-141.93008158342536,-232.60615270381868,-141.93008158342536,-232.60615270381868,-137.1494432236437,-232.60615270381868,-127.1494432236437]},
{"from":-26, "to":-19, "fromPort":"B", "toPort":"T", "points":[-232.60615270381868,-91.77399461524527,-232.60615270381868,-81.77399461524527,-232.60615270381868,-60.48645336223953,-318.0205201016057,-60.48645336223953,-318.0205201016057,-39.19891210923379,-318.0205201016057,-29.198912109233788]},
{"from":-2, "to":-27, "fromPort":"B", "toPort":"T", "points":[-283.4066192336911,-957.6180400489044,-283.4066192336911,-947.6180400489044,-283.4066192336911,-943.5335940511288,-155,-943.5335940511288,-155,-939.4491480533532,-155,-929.4491480533532]},
{"from":-27, "to":-25, "fromPort":"B", "toPort":"T", "points":[-155,-888.5508519466468,-155,-878.5508519466468,-155,-867.5695962254381,-155.64088158858408,-867.5695962254381,-155.64088158858408,-856.5883405042293,-155.64088158858408,-846.5883405042293]},
{"from":-22, "to":-28, "fromPort":"B", "toPort":"T", "points":[-472.0536628401027,-395.1180334882151,-472.0536628401027,-385.1180334882151,-472.0536628401027,-371.97616914995035,-348.88121363413006,-371.97616914995035,-348.88121363413006,-358.83430481168557,-348.88121363413006,-348.83430481168557]},
{"from":-24, "to":-28, "fromPort":"B", "toPort":"T", "points":[-328.2784554007002,-401.08368584208614,-328.2784554007002,-391.08368584208614,-328.2784554007002,-378.9589953268859,-328.3851567583324,-378.9589953268859,-328.3851567583324,-366.83430481168557,-328.3851567583324,-348.83430481168557]},
{"from":-21, "to":-28, "fromPort":"B", "toPort":"T", "points":[-165,-403.42338638305665,-165,-393.42338638305665,-165,-376.1288455973711,-307.8890998825347,-376.1288455973711,-307.8890998825347,-358.83430481168557,-307.8890998825347,-348.83430481168557]},
{"from":-28, "to":-29, "fromPort":"B", "toPort":"T", "points":[-328.3851567583324,-307.9360087049792,-328.3851567583324,-297.9360087049792,-328.3851567583324,-285.10113060990574,-328.3851567583323,-285.10113060990574,-328.3851567583323,-272.2662525148323,-328.3851567583323,-262.2662525148323]},
{"from":-29, "to":-17, "fromPort":"B", "toPort":"T", "points":[-328.3851567583323,-226.89080390643383,-328.3851567583323,-216.89080390643383,-328.3851567583323,-212.24990997817363,-328.2825562503036,-212.24990997817363,-328.2825562503036,-207.60901604991344,-328.2825562503036,-197.60901604991344]},
{"from":-28, "to":-8, "fromPort":"L", "toPort":"L", "points":[-369.37727050992777,-328.3851567583324,-379.37727050992777,-328.3851567583324,-379.37727050992777,-328.3851567583324,-584.9360604757794,-328.3851567583324,-584.9360604757794,-733.7355846318991,-204.61235300940788,-733.7355846318991,-204.61235300940788,-734,-194.61235300940788,-734]}
 ]}
                </textarea>

    </form>
    <a href="/flowchart"><button class="bt">Turn to the process 2</button></a>
</div>
</body>
</html>
