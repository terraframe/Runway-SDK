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
