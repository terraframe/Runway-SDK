<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Create a new "/>
<dl>
  <mjl:form id="${package}.HelloWorld.form.id" name="${package}.HelloWorld.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="${package}.HelloWorld.form.create.button" value="Create" action="${package}.HelloWorldController.create.mojo" />
  </mjl:form>
</dl>
