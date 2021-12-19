var width = 0;
var height = 200;

var svg = null;
var keyword1 = ["상한가", "상한", "상승", "오른", "오른다", "올라", "안티", "매수", "지지"];
var keyword2 = ["하한가", "하한", "하락", "내린", "내린다", "내려", "찬티", "매도", "탈출"];


$(function(){
	$("#popularStock").empty();
	$.ajax({
		type: "GET",
	    url: "/main/popularStockAjax",
	    dataType: "json",
	    data: {},
	    contentType : "application/json; charset:UTF-8",
	    async: false,
	    error : function(request,status,error) {

	    },
	    success : function(data) {
	    	var html = '';
    		var first = "";
	    	$.each(data.result, function(i, v) {
	    		if(i == 0) {
	    			first = v.STOCKS_NM;
	    		}
	    		html += '<div class="scroller_item">';
	    		html += '	<a class="cta-link" href="#">'+v.STOCKS_NM+'</a>';
	    		html += '</div>';
	    	});
    		html += '<div class="scroller_item">';
    		html += '	<a class="cta-link">'+first+'</a>';
    		html += '</div>';
           
	    	$("#popularStock").append(html);

	    	function step(index) {
    	        $('#popularStock ol').delay(2000).animate({
    	            top: -height * index,
    	        }, 500, function() {
    	            step((index + 1) % count);
    	        });
    	    }
	    }
	});
});

$(window).resize( function() {
	width = $('.contBox').eq(0).innerWidth();

	$("svg").eq(0).attr("width", width);
	if(svg != null) {
		svg.attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");	
	}
});

// 종목 데이터 로드
function setStockDate(code) {
	$(".loading").show();
	var wordcloudlist = null;
	var param = {
			'code' : code
	};

	width = $('.contBox').eq(0).innerWidth();
	// 폰트크기
	$.ajax({
		type: "GET",
	    url: "/main/scrapAjax",
	    dataType: "json",
	    data: param,
	    contentType : "application/json; charset:UTF-8",
	    async: true,
	    error : function(request,status,error) {
	    	$(".loading").hide();
	    },
	    success : function(data) {
	    	$("#wordcloud").empty();
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

	       	$(".loading").hide();
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
}

// 종목명 검색
$(document).keyup("#code", function() {
	if($("#code").val().length > 1) {
		var searchText = $("#code").val().toUpperCase();
		var param = {
				'searchText' : searchText
		};
		
		$.ajax({
			type: "GET",
		    url: "/main/searchStockAjax",
		    dataType: "json",
		    data: param,
		    contentType : "application/json; charset:UTF-8",
		    async: true,
		    error : function(request,status,error) {
		    },
		    success : function(data) {
		    	$("#codeList").empty();
		    	var html = '';

		    	if(data.result.length == 0) {
		    		html += "<li code=''>검색된 종목이 없습니다.</li>";
		    		$('.codeDiv').hide();
		    	} else {
			    	$.each(data.result, function(i, v) {
			    		var first = v.STOCKS_NM.indexOf(searchText);
			    		var text1 = v.STOCKS_NM.substr(0, first);
			    		var text2 = v.STOCKS_NM.substr(first, searchText.length);
			    		var text3 = v.STOCKS_NM.substr(first+searchText.length);

			    		html += "<li code='"+v.STOCKS_ID+"'>"+v.STOCKS_NM.substr(0, first)+"<span style='color: #fd8000;'>"+text2+"</span>"+text3+"</li>";	
			    	});
		    	}
		    	
		    	$("#codeList").append(html);
		    	$("#codeList").show();
		    }
		});
	}
});

//종목명 클릭
$(document).on("click", "#codeList li", function() {
	if($(this).attr("code") != "") {
		$("#codeNm").text($(this).text());
		$("#codeId").text("(" + $(this).attr("code") + ")");
		$("#code").val('');
		$("#codeList").hide();
		$(".contentInit").hide();
		$('.codeDiv').show();
		$('.content').show();
		

		var param = {
				'STOCKS_ID' : $(this).attr("code")
		};
		
		$.ajax({
			type: "GET",
		    url: "/main/clickStockAjax",
		    dataType: "json",
		    data: param,
		    contentType : "application/json; charset:UTF-8",
		    async: true,
		    error : function(request,status,error) {
		    },
		    success : function(data) {
		    	
		    }
		});
		
		setStockDate($(this).attr("code"));
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
	                fontFamily: 'NanumSquareR'
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
	    tooltip: {
	        pointFormat: '{point.y:1f}건',
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