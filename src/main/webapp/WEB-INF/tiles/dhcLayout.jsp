<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page session="false"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<head>
<title>돔황챠</title>
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
<script async src="https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js?client=ca-pub-1265374192468982" crossorigin="anonymous"></script>
</head>
<body>
	<!-- 노래 리스트 -->
	<%@ include file="../template/loading.jsp"%>
	
	<div class="header">
		<tiles:insertAttribute name="header" />
	</div>
	<div class="container">
		<tiles:insertAttribute name="body"/>
	</div>
</body>
</html>