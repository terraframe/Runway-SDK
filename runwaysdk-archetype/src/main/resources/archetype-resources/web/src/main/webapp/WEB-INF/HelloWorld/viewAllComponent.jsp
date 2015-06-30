<%--

    Copyright (c) 2015 TerraFrame, Inc. All rights reserved.

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
<%@page import="${package}.HelloWorldDTO"%>
<%@page import="com.runwaysdk.web.json.JSONController"%>
<%@page import="com.runwaysdk.constants.ClientConstants"%>
<%@page import="com.runwaysdk.constants.ClientRequestIF"%>
<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View all HelloWorld"/>

<script type="text/javascript">
<% 
// statically import the javascript definition
  ClientRequestIF clientRequest = (ClientRequestIF) request.getAttribute(ClientConstants.CLIENTREQUEST);
  String js = JSONController.importTypes(clientRequest.getSessionId(), new String[]{HelloWorldDTO.CLASS},true);
  out.write(js);
%>

/*
 * Fetches a HelloWorld with a randomly generated greeting.
 */
window.onload = function(){

  var request = new Mojo.ClientRequest({
    onSuccess : function(helloWorld){
      var el = document.getElementById('async');
      el.innerHTML = helloWorld.getGreeting();
    },
    onFailure : function(e){
      alert(e.getLocalizedMessage());
    }
  });
  
  ${package}.HelloWorld.generateRandom(request);
};
</script>

<h2>Generated HelloWorld</h2>
  Synchronous: ${helloWorld.greeting}<br />
  Asynchronous: <span id="async"></span>
<hr />
<h2>HelloWorld CRUD</h2>
<mjl:table var="item" query="${query}">
  <mjl:context action="${package}.HelloWorldController.viewPage.mojo" />
  <mjl:columns>
    <mjl:attributeColumn attributeName="greeting">
    </mjl:attributeColumn>
    <mjl:freeColumn>
      <mjl:header>
        
      </mjl:header>
      <mjl:row>
        <mjl:commandLink name="view.link" action="${package}.HelloWorldController.view.mojo">
          View
          <mjl:property name="id" value="${item.id}" />
        </mjl:commandLink>
      </mjl:row>
      <mjl:footer>
        
      </mjl:footer>
    </mjl:freeColumn>
  </mjl:columns>
  <mjl:pagination>
    <mjl:page />
  </mjl:pagination>
</mjl:table>
<br />
<mjl:commandLink name="HelloWorldController.newInstance" action="${package}.HelloWorldController.newInstance.mojo">
  Create a new Hello World
</mjl:commandLink>
