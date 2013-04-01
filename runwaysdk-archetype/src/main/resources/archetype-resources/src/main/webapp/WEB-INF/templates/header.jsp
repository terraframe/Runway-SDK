<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <title>${page_title}</title>
 
<%@page import="com.runwaysdk.constants.ClientConstants"%>
<%@page import="com.runwaysdk.constants.ClientRequestIF"%>
<%@page import="com.runwaysdk.web.json.JSONController"%>

<%
ClientRequestIF clientRequest = (ClientRequestIF) request.getAttribute(ClientConstants.CLIENTREQUEST);
%>
  
  <script type="text/javascript" src="js/runwaysdk/RunwaySDK_Core.js"></script>
  <script type="text/javascript" src="js/runwaysdk/RunwaySDK_UI.js"></script>
  <script type="text/javascript" src="js/runwaysdk/RunwaySDK_DTO.js"></script>
  <script type="text/javascript" src="js/runwaysdk/RunwaySDK_GIS.js"></script>
  <script type="text/javascript" src="js/runwaysdk/RunwaySDK_Inspector.js"></script>
</head>

<body>
<div id="main_menu"></div>
<div id="content">

<%@include file="inlineError.jsp" %>
