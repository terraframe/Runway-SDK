<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Create a new some tree Relationship"/>
<dl>
  <mjl:form id="temporary.junit.test.SomeTree.form.id" name="temporary.junit.test.SomeTree.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="temporary.junit.test.SomeTree.form.create.button" value="Create" action="temporary.junit.test.SomeTreeController.create.mojo" />
  </mjl:form>
</dl>
