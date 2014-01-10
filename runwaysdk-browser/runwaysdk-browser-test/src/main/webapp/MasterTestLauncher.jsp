<%--

    Copyright (c) 2013 TerraFrame, Inc. All rights reserved.

    This file is part of Runway SDK(tm).

    Runway SDK(tm) is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    Runway SDK(tm) is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.

--%>
<!DOCTYPE html>

<%@page import="com.runwaysdk.jstest.TestClassDTO" %>
<%@page import="com.runwaysdk.jstest.SubClassDTO" %>
<%@page import="com.runwaysdk.jstest.TestExceptionDTO" %>
<%@page import="com.runwaysdk.jstest.TestProblemDTO" %>
<%@page import="com.runwaysdk.jstest.TestStructDTO" %>
<%@page import="com.runwaysdk.jstest.RefClassDTO" %>
<%@page import="com.runwaysdk.jstest.StatesDTO" %>
<%@page import="com.runwaysdk.jstest.StateEnumDTO" %>
<%@page import="com.runwaysdk.jstest.BefriendsDTO" %>
<%@page import="com.runwaysdk.jstest.SummationClientRequestIF" %>
<%@page import="com.runwaysdk.jstest.TestViewDTO" %>
<%@page import="com.runwaysdk.jstest.TestUtilDTO" %>

<%@page import="com.runwaysdk.business.BusinessDTO"%>
<%@page import="com.runwaysdk.business.RelationshipDTO"%>
<%@page import="com.runwaysdk.web.json.JSONController"%>
<%@page import="com.runwaysdk.constants.ClientConstants"%>
<%@page import="com.runwaysdk.system.AllOperationsDTO"%>

<%@page import="com.runwaysdk.system.PhoneNumberDTO"%>

<%@page import="com.runwaysdk.system.UsersDTO"%>
<%@page import="com.runwaysdk.system.Users"%>
<%@page import="com.runwaysdk.business.BusinessQueryDTO"%>
<%@page import="com.runwaysdk.constants.UserInfo"%>
<%@page import="com.runwaysdk.facade.QueryTranslation"%>
<%@page import="com.runwaysdk.constants.QueryConditions"%>
<%@page import="com.runwaysdk.jstest.JSTestConstants"%>

<%@page import="java.util.List"%>

<%@page import="com.runwaysdk.system.OperationsDTO"%>
<%@page import="com.runwaysdk.constants.EntityTypes"%>
<%@page import="com.runwaysdk.constants.ClientRequestIF"%>
<%@page import="com.runwaysdk.jstest.SessionFilter"%>
<%@page import="com.runwaysdk.jstest.TestWarningDTO"%>
<%@page import="com.runwaysdk.jstest.TestInformationDTO"%>

<%@page import="com.runwaysdk.jstest.business.ontology.AlphabetDTO" %>
<%@page import="com.runwaysdk.jstest.business.ontology.SequentialDTO" %>
<%@page import="com.runwaysdk.jstest.business.ontology.AlphabetDisplayLabelDTO" %>

<%
  // capture the session id and clientRequest
  ClientRequestIF clientRequest = (ClientRequestIF) request.getAttribute(ClientConstants.CLIENTREQUEST);
  String sessionId = clientRequest.getSessionId();
%>


<html>

<head>
<!-- JQuery -->
<link rel="stylesheet" href="webjars/jquery-ui/1.10.3/themes/base/jquery-ui.css" />
<link rel="stylesheet" href="jquerytree/jqtree.css" />
<link rel="stylesheet" href="webjars/datatables/1.9.4/media/css/jquery.dataTables.css" />
<link rel="stylesheet" href="webjars/datatables/1.9.4/media/css/jquery.dataTables_themeroller.css" />

<!-- YUI2 -->
<link rel="stylesheet" type="text/css" href="yui2/build/fonts/fonts-min.css" /> 
<link rel="stylesheet" type="text/css" href="yui2/build/button/assets/skins/sam/button.css" /> 
<link rel="stylesheet" type="text/css" href="yui2/build/container/assets/skins/sam/container.css"/>
<script type="text/javascript" src="yui2/build/yahoo-dom-event/yahoo-dom-event.js"></script> 
<script type="text/javascript" src="yui2/build/connection/connection.js"></script> 
<script type="text/javascript" src="yui2/build/element/element.js"></script> 
<script type="text/javascript" src="yui2/build/button/button.js"></script> 
<script type="text/javascript" src="yui2/build/dragdrop/dragdrop.js"></script> 
<script type="text/javascript" src="yui2/build/container/container.js"></script> 

<!-- YUI3 version: 3.3.0 -->
<!-- YUI3 Modules we are currently using!:
attribute
console
console-filters
cssgrids
dataschema
datasource
datatable
dd
event-gestures
node
recordset
tabview
test
-->
<!-- CSS -->
<!--<link rel="stylesheet" type="text/css" href="yui3/build/cssreset/reset-min.css">-->
<link rel="stylesheet" type="text/css" href="yui3/build/cssfonts/fonts-min.css">
<link rel="stylesheet" type="text/css" href="yui3/build/cssgrids/grids-min.css">
<link rel="stylesheet" type="text/css" href="yui3/build/widget/assets/skins/sam/widget.css">
<link rel="stylesheet" type="text/css" href="yui3/build/console/assets/skins/sam/console.css">
<link rel="stylesheet" type="text/css" href="yui3/build/console/assets/skins/sam/console-filters.css">
<link rel="stylesheet" type="text/css" href="yui3/build/datatable/assets/skins/sam/datatable.css">
<link rel="stylesheet" type="text/css" href="yui3/build/tabview/assets/skins/sam/tabview.css">
<link rel="stylesheet" type="text/css" href="yui3/build/test/assets/skins/sam/test.css">
<!-- JS -->
<script type="text/javascript" src="yui3/build/yui/yui.js"></script>
<script type="text/javascript" src="yui3/build/oop/oop.js"></script>
<script type="text/javascript" src="yui3/build/event-custom/event-custom.js"></script>
<script type="text/javascript" src="yui3/build/attribute/attribute.js"></script>
<script type="text/javascript" src="yui3/build/pluginhost/pluginhost.js"></script>
<script type="text/javascript" src="yui3/build/dom/dom.js"></script>
<script type="text/javascript" src="yui3/build/dom/dom-style-ie.js"></script>
<script type="text/javascript" src="yui3/build/event/event.js"></script>
<script type="text/javascript" src="yui3/build/node/node.js"></script>
<script type="text/javascript" src="yui3/build/event/event-base-ie.js"></script>
<script type="text/javascript" src="yui3/build/intl/intl.js"></script>
<script type="text/javascript" src="yui3/build/console/lang/console.js"></script>
<script type="text/javascript" src="yui3/build/classnamemanager/classnamemanager.js"></script>
<script type="text/javascript" src="yui3/build/base/base.js"></script>
<script type="text/javascript" src="yui3/build/widget/widget.js"></script>
<script type="text/javascript" src="yui3/build/widget/widget-base-ie.js"></script>
<script type="text/javascript" src="yui3/build/dump/dump.js"></script>
<script type="text/javascript" src="yui3/build/substitute/substitute.js"></script>
<script type="text/javascript" src="yui3/build/console/console.js"></script>
<script type="text/javascript" src="yui3/build/plugin/plugin.js"></script>
<script type="text/javascript" src="yui3/build/console/console-filters.js"></script>
<script type="text/javascript" src="yui3/build/json/json.js"></script>
<script type="text/javascript" src="yui3/build/dataschema/dataschema.js"></script>
<script type="text/javascript" src="yui3/build/cache/cache-base.js"></script>
<script type="text/javascript" src="yui3/build/querystring/querystring-stringify-simple.js"></script>
<script type="text/javascript" src="yui3/build/io/io-base.js"></script>
<script type="text/javascript" src="yui3/build/datasource/datasource.js"></script>
<script type="text/javascript" src="yui3/build/collection/arraylist.js"></script>
<script type="text/javascript" src="yui3/build/arraysort/arraysort.js"></script>
<script type="text/javascript" src="yui3/build/collection/array-extras.js"></script>
<script type="text/javascript" src="yui3/build/recordset/recordset.js"></script>
<script type="text/javascript" src="yui3/build/datatable/lang/datatable.js"></script>
<script type="text/javascript" src="yui3/build/stylesheet/stylesheet.js"></script>
<script type="text/javascript" src="yui3/build/datatable/datatable.js"></script>
<script type="text/javascript" src="yui3/build/dd/dd.js"></script>
<script type="text/javascript" src="yui3/build/dd/dd-gestures.js"></script>
<script type="text/javascript" src="yui3/build/event/event-touch.js"></script>
<script type="text/javascript" src="yui3/build/event-gestures/event-gestures.js"></script>
<script type="text/javascript" src="yui3/build/widget/widget-parent.js"></script>
<script type="text/javascript" src="yui3/build/widget/widget-child.js"></script>
<script type="text/javascript" src="yui3/build/tabview/tabview-base.js"></script>
<script type="text/javascript" src="yui3/build/event-simulate/event-simulate.js"></script>
<script type="text/javascript" src="yui3/build/node/node-event-simulate.js"></script>
<script type="text/javascript" src="yui3/build/node-focusmanager/node-focusmanager.js"></script>
<script type="text/javascript" src="yui3/build/tabview/tabview.js"></script>
<script type="text/javascript" src="yui3/build/test/test.js"></script>
<script type="text/javascript" src="yui3/build/dd/dd-drop-plugin.js"></script>



<!-- RequireJS and Runway -->
<script data-main="TestLoader" src="webjars/requirejs/2.1.8/require.js"></script>
<link rel="stylesheet" type="text/css" href="com/runwaysdk/ui/factory/runway/default.css" />



<script type="text/javascript">
  require(["com/runwaysdk/RunwaySDK_DTO"], function(){

      <%
  // use a try catch before printing out the definitions, otherwise, if an
  // error occurs here, javascript spills onto the actual page (ugly!)
    try
    {
      String js = JSONController.importTypes(clientRequest.getSessionId(), new String[]{OperationsDTO.CLASS, AllOperationsDTO.CLASS, BefriendsDTO.CLASS, TestStructDTO.CLASS, TestClassDTO.CLASS, SubClassDTO.CLASS, RefClassDTO.CLASS, StateEnumDTO.CLASS, StatesDTO.CLASS, PhoneNumberDTO.CLASS, TestViewDTO.CLASS, TestUtilDTO.CLASS, TestExceptionDTO.CLASS, TestProblemDTO.CLASS, SummationClientRequestIF.CLASS, TestWarningDTO.CLASS, TestInformationDTO.CLASS, AlphabetDTO.CLASS, AlphabetDisplayLabelDTO.CLASS, SequentialDTO.CLASS}, true);
      out.print(js);
      /*
    out.println("var types = ['"+OperationsDTO.CLASS+"', '"+AllOperationsDTO.CLASS+"', '"+BefriendsDTO.CLASS+"', '"+TestStructDTO.CLASS+"', '"+TestClassDTO.CLASS+"', '"+SubClassDTO.CLASS+"', '"+RefClassDTO.CLASS+"', '"+StateEnumDTO.CLASS+"', '"+StatesDTO.CLASS+"', '"+PhoneNumberDTO.CLASS+"', '"+TestViewDTO.CLASS+"', '"+TestUtilDTO.CLASS+"', '"+TestExceptionDTO.CLASS+"', '"+TestProblemDTO.CLASS+"', '"+SummationClientRequestIF.CLASS+"', '"+TestWarningDTO.CLASS+"', '"+TestInformationDTO.CLASS+"'];");
        out.println("var handler = new TestHandler({onSuccess:initTest,onFailure:function(e){alert(e.getLocalizedMessage());}});");
        out.println("Mojo.Facade.importTypes(handler, types, {appendTo:document.getElementsByTagName('head')[0]});"); 
      */
    
    // set up a relationship between some parents and children.
    // we want to do this here because 1) we're already testing the AJAX portion of this in a test
    // and 2) creating several parents/children takes a while, so might as well do it here.
    TestClassDTO parent1 = new TestClassDTO(clientRequest);
    parent1.apply();
    
    TestClassDTO parent2 = new TestClassDTO(clientRequest);
    parent2.apply();
    
    TestClassDTO parent3 = new TestClassDTO(clientRequest);
    parent3.apply();
    
    TestClassDTO parent4 = new TestClassDTO(clientRequest);
    parent4.apply();
    
    RefClassDTO child1 = new RefClassDTO(clientRequest);
    child1.apply();
    
    RefClassDTO child2 = new RefClassDTO(clientRequest);
    child2.apply();
    
    RefClassDTO child3 = new RefClassDTO(clientRequest);
    child3.apply();
    
    RefClassDTO child4 = new RefClassDTO(clientRequest);
    child4.apply();
    
    // parent1 has the following children: child1, child2, child3
    BefriendsDTO relToDelete1 = parent1.addRefClass(child1);
    relToDelete1.apply(); 
    
    parent1.addRefClass(child2).apply();
    parent1.addRefClass(child3).apply();
    out.println("g_parentIdWithChildren = '"+parent1.getId()+"';");
    out.println("g_deletedChildRelId = '"+relToDelete1.getId()+"';");
    out.println("g_parentWithChildren = null;");
    
    // child4 has the following parents: parent2, parent3, parent4
    BefriendsDTO relToDelete2 = child4.addTestClass(parent2);
    relToDelete2.apply(); 
    
    child4.addTestClass(parent3).apply();
    child4.addTestClass(parent4).apply(); 
    out.println("g_childIdWithParents = '"+child4.getId()+"';");
    out.println("g_deletedParentRelId = '"+relToDelete2.getId()+"';");
    out.println("g_childWithParents = null;");
    
    // The inaccessible object is used for the user Sammy
    // to test accessing objects/attributes without permission.
    TestClassDTO inaccessible = new TestClassDTO(clientRequest);
    inaccessible.apply();
    out.println("g_inaccessibleId = '"+inaccessible.getId()+"';");
    
    // make a var to hold the id of the metadata that defines TestClass
    out.println("g_testClassMdId = '"+inaccessible.getMd().getId()+"';");
    
    // print out the user id with no permissions so that we can add permissions to him later
    BusinessQueryDTO query = (BusinessQueryDTO) clientRequest.getQuery(UsersDTO.CLASS);
    query.addCondition(UserInfo.USERNAME, QueryConditions.EQUALS, JSTestConstants.USERNAME_WITH_NO_PERMISSIONS);

    query = clientRequest.queryBusinesses(query);
    List<? extends BusinessDTO> users = query.getResultSet();

    out.println("g_userIdNoPermissions = '"+users.get(0).getId()+"';");
    
    // print out the metadata id of for attribute defined by TestClass (used for permissions)
    TestClassDTO testClass = new TestClassDTO(clientRequest);
    out.println("g_testBooleanMdId = '"+testClass.getTestBooleanMd().getId()+"';");
    out.println("g_testIntegerMdId = '"+testClass.getTestIntegerMd().getId()+"';");
    
    // create an instance of SubClass to reference
    SubClassDTO subClass = new SubClassDTO(clientRequest);
    subClass.apply();
    out.println("g_subclassId = '"+subClass.getId()+"';");
    out.println("g_subClassMdId = '"+subClass.getMd().getId()+"';");
    
    // create TestClass, RefClass, Befriends, and StructClass instances to query.
    for(int i = 1; i<11; i++)
    {
      TestClassDTO queryTestClass = new TestClassDTO(clientRequest);
      queryTestClass.setTestCharacter("queryMe!!!");
      queryTestClass.setTestLong(1234567890L);
      queryTestClass.setTestInteger(i); // value cannot be 0
      queryTestClass.apply();
      
      RefClassDTO queryRefClass = new RefClassDTO(clientRequest);
      queryRefClass.setRefChar("queryMe!!!");
      queryRefClass.apply();
      
      BefriendsDTO queryBefriends = queryRefClass.addTestClass(queryTestClass);
      queryBefriends.setRelChar("queryMe!!!");
      queryBefriends.apply();
      
      TestStructDTO queryStructClass = new TestStructDTO(clientRequest);
      queryStructClass.setStructChar("queryMe!!!");
      queryStructClass.apply();
    }
    
    out.println("g_noPermUser = '"+JSTestConstants.USERNAME_WITH_NO_PERMISSIONS+"'");
    out.println("g_noPermPass = '"+JSTestConstants.USER_PASSWORD_WITH_NO_PERMISSIONS+"'");
    out.println("g_allPermUser = '"+JSTestConstants.USERNAME_WITH_ALL_PERMISSIONS+"'");
    out.println("g_allPermPass = '"+JSTestConstants.USER_PASSWORD_WITH_ALL_PERMISSIONS+"'");
    
    AlphabetDTO term1NoChildren = new AlphabetDTO(clientRequest);
    term1NoChildren.getDisplayLabel().setValue("defaultLocale", "Term 1");
    term1NoChildren.apply();
    out.println("g_idTerm1NoChildren = '" + term1NoChildren.getId() + "'");
    
    AlphabetDTO term2NoChildren = new AlphabetDTO(clientRequest);
    term2NoChildren.getDisplayLabel().setValue("defaultLocale", "Term 2");
    term2NoChildren.apply();
    out.println("g_idTerm2NoChildren = '" + term2NoChildren.getId() + "'");
    
    AlphabetDTO term3NoChildren = new AlphabetDTO(clientRequest);
    term3NoChildren.getDisplayLabel().setValue("defaultLocale", "Term 3");
    term3NoChildren.apply();
    out.println("g_idTerm3NoChildren = '" + term3NoChildren.getId() + "'");
    
    AlphabetDTO termRoot = new AlphabetDTO(clientRequest);
    termRoot.getDisplayLabel().setValue("defaultLocale", "Root Term");
    termRoot.apply();
    out.println("g_idTermRoot = '" + termRoot.getId() + "'");
    
    AlphabetDTO termA = new AlphabetDTO(clientRequest);
    termA.getDisplayLabel().setValue("defaultLocale", "Term A");
    termA.apply();
    termRoot.addChildTerm(termA).apply();
    out.println("g_idTermA = '" + termA.getId() + "'");
    
    AlphabetDTO termB = new AlphabetDTO(clientRequest);
    termB.getDisplayLabel().setValue("defaultLocale", "Term B");
    termB.apply();
    termA.addChildTerm(termB).apply();
    out.println("g_idTermB = '" + termB.getId() + "'");
    
    AlphabetDTO termBB = new AlphabetDTO(clientRequest);
    termBB.getDisplayLabel().setValue("defaultLocale", "Term BB");
    termBB.apply();
    termA.addChildTerm(termBB).apply();
    out.println("g_idTermBB = '" + termBB.getId() + "'");
    
    AlphabetDTO termC = new AlphabetDTO(clientRequest);
    termC.getDisplayLabel().setValue("defaultLocale", "Term C");
    termC.apply();
    SequentialDTO bRelatC = termB.addChildTerm(termC);
    
    bRelatC.apply();
    out.println("g_idTermC = '" + termC.getId() + "'");
    out.println("g_idBRelatC = '" + bRelatC.getId() + "'");
  }
  catch(Exception e)
  {
    // perform cleanup
    throw e;
  }
      %>
  });
</script>

<style type="text/css">

span.com_runwaysdk_test_framework_span
{
  color : blue;
  text-decoration : underline;
  cursor : pointer;
}

ul.com_runwaysdk_test_framework_ul
{
    margin-top : 0px;
    list-style-type : none;
}

.indent
{
text-indent:50px;
}

.doubleIndent
{
text-indent:100px;
}

#testLogger {
    margin-bottom: 1em;
}
 
#testLogger .yui3-console .yui3-console-title {
    border: 0 none;
    color: #000;
    font-size: 13px;
    font-weight: bold;
    margin: 0;
    text-transform: none;
}
#testLogger .yui3-console .yui3-console-entry-meta {
    margin: 0;
}
 
.yui3-skin-sam .yui3-console-entry-pass .yui3-console-entry-cat {
    background: #070;
    color: #fff;
}

</style>

</head>

<body class="yui3-skin-sam  yui-skin-sam">

<div id="includeDiv"></div>

<div id="testLogger"></div>

<br/><br/>

GUI Framework:
<div id="guiFrameworkSelect"></div>

<br/><br/>

<div id="buttonsDiv"></div>

</body>

</html>
