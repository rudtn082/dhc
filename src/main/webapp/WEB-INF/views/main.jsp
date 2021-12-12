<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page session="false"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>종토방 뷰</title>
<meta name="viewport" content="width=device-width,initial-scale=1, user-scalable=no">
<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
<link rel="stylesheet" href="../../resources/css/materialize.css">
<link rel="stylesheet" href="../../resources/css/highcharts.css">
<link rel="stylesheet" href="../../resources/css/common.css">
<script	src="../../resources/js/materialize.min.js"></script>
<script	src="../../resources/js/d3.min.js"></script>
<script	src="../../resources/js/highcharts.js"></script>
<script src="https://rawgit.com/jasondavies/d3-cloud/master/build/d3.layout.cloud.js" type="text/JavaScript"></script>
<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>

<script type="text/javascript">

var width = 0;
var height = 200;

var svg = null;
var keyword1 = ["상한가", "상한", "상승", "오른", "오른다", "올라", "안티", "매수", "지지"];
var keyword2 = ["하한가", "하한", "하락", "내린", "내린다", "내려", "찬티", "매도", "탈출"];

$(window).resize( function() {
	width = $('.contBox').eq(0).innerWidth();

	$("svg").eq(0).attr("width", width);
	svg.attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");
});

$(function(){
	var wordcloudlist = null;
	var param = {
			'code' : '064260'
	};
	
	width = $('.contBox').eq(0).innerWidth();
	// 폰트크기
	
	$.ajax({
		type: "GET",
	    url: "/main/scrapAjax",
	    dataType: "json",
	    data: param,
	    contentType: "application/json; charset:UTF-8", 
	    async: false,
	    error : function(request,status,error) {
	        //alert("Error!");
	    },
	    success : function(data) {
	    	// 게시글 작성 순위
	    	writerChartGen(data.writerList);
	    	
	    	// 공감순 별 게시글
	    	sympathyPost(data.sympathyList);
	    	
	    	// 비공감순 별 게시글
	    	nonSympathyPost(data.nonSympathyList);

	    	// 조회수 별 게시글
	    	inquirePost(data.inquireList);
	    	
	    	// 종목 키워드
	    	svg = d3.select("#wordcloud").append("svg")
		            .attr("width", width)
		            .attr("height", height)
		            .append("g")
		            .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");
	    	
            showCloud(data.wordCloudList);
          	setInterval(function(){
	            showCloud(data.wordCloudList)
	       	},3500)
	    }
	});

	// 종목 키워드
	function showCloud(data) {
	    d3.layout.cloud().size([width, height])
				         .words(data)
				         .rotate(function (d) {
				             return d.key.length > 3 ? 0 : 90;
				         })
			             .fontSize(function(d) { return d.value+10; })
				         .on("end", draw)
				         .start();
	         
	    function draw(words) {
            var cloud = svg.selectAll("text").data(words);
            
		   cloud.enter()
		    	 .append("text")
                 .style("font-family", "NanumSquareB")
                 .style("fill", function (d) {
                     return (keyword1.indexOf(d.key) > -1 ? "#7c0e0e" : keyword2.indexOf(d.key) > -1 ? "#0e1c7c" : "#a7a7a7");
                 })
                 .attr("text-anchor", "middle") 
                 .style("font-size", function (d) {
                     return (d.value+10) + "px";
                 })
                 .attr("transform", function (d) {
                     return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
                 })
                 .text(function(d) { return d.key; });
                   
           cloud.transition()
                 .duration(600)
                 .style("font-size", function (d) {
                	 return (d.value+10) + "px";
                 })
                 .attr("transform", function (d) {
                     return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
                 });
	    }
	}
	              
});

// 게시글 작성 순위
function writerChartGen(data) {
	var seriesData = [];
	$.each(data, function(i, v){
		var test = [v.key, v.value];
		seriesData.push(test);
		if(i > 5) return false;
	});
	
	Highcharts.chart('writerChart', {
	    chart: {
	        type: 'column'
	    },
	    title: {
	        text: ''
	    },
	    xAxis: {
	        type: 'category',
	        labels: {
	            rotation: -45,
	            style: {
	                fontSize: '13px',
	                fontFamily: 'Verdana, sans-serif'
	            }
	        }
	    },
	    yAxis: {
	        min: 0,
	        title: {
	            text: ''
	        }
	    },
	    legend: {
	        enabled: false
	    },
	    plotOptions: {
	        series: {
	            label: {
	                connectorAllowed: false
	            },
	            pointStart: 2010
	        }
	    },
	    series: [{
	        data: seriesData,
	        dataLabels: {
	            enabled: true,
	            rotation: 0,
	            color: '#545454',
	            format: '{point.y:1f}', // one decimal
	            style: {
	                fontSize: '13px',
	                fontFamily: 'NanumSquareR'
	            }
	        }
	    }],
	    credits: {
	        enabled: false
	      }
	});
}

//공감순 별 게시글
function sympathyPost(data) {
	$("#sympathyPost").empty();
	
	var html = '';
	html += '<table>';
	html += '	<colgroup>';
	html += '		<col width="25%">';
	html += '		<col width="75%">';
	html += '	</colgroup>';
	html += '	<thead>';
	html += '		<tr>';
	html += '			<th style="text-align: center;">공감</th>';
	html += '			<th style="text-align: center;">제목</th>';
	html += '		</tr>';
	html += '	</thead>';
	html += '	<tbody>';
	$.each(data, function(i, v) {
		html += '		<tr>';
		html += '			<td style="text-align: center;"><span class="sympathy">'+v.value+'</span></td>';
		html += '			<td><a href="https://finance.naver.com'+v.key.split('||')[1]+'" target="_blank">'+v.key.split('||')[0]+'</a></td>';
		html += '		</tr>';

		if(i > 3) return false;
	});
	html += '	</tbody>';
	html += '</table>';
	
	$("#sympathyPost").append(html);
}

//비공감순 별 게시글
function nonSympathyPost(data) {
	$("#nonSympathyPost").empty();
	
	var html = '';
	html += '<table>';
	html += '	<colgroup>';
	html += '		<col width="25%">';
	html += '		<col width="75%">';
	html += '	</colgroup>';
	html += '	<thead>';
	html += '		<tr>';
	html += '			<th style="text-align: center;">비공감</th>';
	html += '			<th style="text-align: center;">제목</th>';
	html += '		</tr>';
	html += '	</thead>';
	html += '	<tbody>';
	$.each(data, function(i, v) {
		var value = v.value;
		if(value < 10) value = "0" + value;
		html += '		<tr>';
		html += '			<td style="text-align: center;"><span class="nonSympathy">'+value+'</span></td>';
		html += '			<td><a href="https://finance.naver.com'+v.key.split('||')[1]+'" target="_blank">'+v.key.split('||')[0]+'</a></td>';
		html += '		</tr>';

		if(i > 3) return false;
	});
	html += '	</tbody>';
	html += '</table>';
	
	$("#nonSympathyPost").append(html);
}

//조회수 별 게시글
function inquirePost(data) {
	$("#inquirePost").empty();
	
	var html = '';
	html += '<table>';
	html += '	<colgroup>';
	html += '		<col width="25%">';
	html += '		<col width="75%">';
	html += '	</colgroup>';
	html += '	<thead>';
	html += '		<tr>';
	html += '			<th style="text-align: center;">조회수</th>';
	html += '			<th style="text-align: center;">제목</th>';
	html += '		</tr>';
	html += '	</thead>';
	html += '	<tbody>';
	$.each(data, function(i, v) {
		var value = v.value;
		if(value < 10) value = "0" + value;
		html += '		<tr>';
		html += '			<td style="text-align: center;"><span class="inquire">'+value+'</span></td>';
		html += '			<td><a href="https://finance.naver.com'+v.key.split('||')[1]+'" target="_blank">'+v.key.split('||')[0]+'</a></td>';
		html += '		</tr>';

		if(i > 3) return false;
	});
	html += '	</tbody>';
	html += '</table>';
	
	$("#inquirePost").append(html);
}
</script>
</head>
<body>
	<div class="header">
		<span class="title">돔황챠</span>
		<input id="code" type="text" placeholder="도망쳐야할 종목명 입력"/>
		<span class="">최근 게시글 500개를 요약하였습니다.</span>
	</div>
	<div class="container">
		<div class="content">
			<span class="contTit">종목 키워드</span>
			<div class="contBox" id="wordcloud" style="height: 200px;"> </div>
		</div>
		<div class="content">
			<span class="contTit">조회수순 게시글</span>
			<div class="contBox" id="inquirePost"></div> 
		</div>
		<div class="content">
			<span class="contTit">공감순 게시글</span>
			<div class="contBox" id="sympathyPost"></div> 
		</div>
		<div class="content">
			<span class="contTit">비공감순 게시글</span>
			<div class="contBox" id="nonSympathyPost"></div> 
		</div>
		<div class="content">
			<span class="contTit">게시글 작성 순위</span>
			<div class="contBox" id="writerChart"> </div> 
		</div>
		
		
		<%-- <%@ include file="song.jsp"%> --%>
	</div>
</body>
</html>