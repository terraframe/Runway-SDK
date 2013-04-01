<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing "/>
<dl>
  <mjl:form id="${package}.HelloWorld.form.id" name="${package}.HelloWorld.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="${package}.HelloWorld.form.update.button" value="Update" action="${package}.HelloWorldController.update.mojo" />
    <mjl:command name="${package}.HelloWorld.form.delete.button" value="Delete" action="${package}.HelloWorldController.delete.mojo" />
    <mjl:command name="${package}.HelloWorld.form.cancel.button" value="Cancel" action="${package}.HelloWorldController.cancel.mojo" />
  </mjl:form>
</dl>
