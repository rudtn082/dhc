<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link rel="stylesheet" href="../../../resources/css/textScrolling.css">
<script	src="../../../resources/js/main.js"></script>

<div class="codeDiv" style="display: none;">
	<span id="codeNm"></span>
	<span id="codeId"></span>
</div>
<div class="contentInit">
	<div class="initInfo">
	    <div class='masthead-image' id='scroller-container'>
	        <div class='content center'>
	            <h1 id='scroller'>
	              <div>실시간 인기 검색 종목</div>
	              <div id="popularStock" class="popularStock"></div>
	            </h1>
	        </div>
	    </div>
	</div>
</div>
<div class="content" style="display: none;">
	<span class="contTit">종목 키워드</span>
	<div class="contBox" id="wordcloud" style="height: 200px;"> </div>
</div>
<div class="content" style="display: none;">
	<span class="contTit">조회수순 게시글</span>
	<div class="contBox" id="inquirePost"></div> 
</div>
<div class="content" style="display: none;">
	<span class="contTit">공감순 게시글</span>
	<div class="contBox" id="sympathyPost"></div> 
</div>
<div class="content" style="display: none;">
	<span class="contTit">비공감순 게시글</span>
	<div class="contBox" id="nonSympathyPost"></div> 
</div>
<div class="content" style="display: none;">
	<span class="contTit">게시글 작성 순위</span>
	<div class="contBox" id="writerChart"> </div> 
</div>