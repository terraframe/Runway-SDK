<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing some tree Relationship"/>
<dl>
  <mjl:form id="temporary.junit.test.SomeTree.form.id" name="temporary.junit.test.SomeTree.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="temporary.junit.test.SomeTree.form.update.button" value="Update" action="temporary.junit.test.SomeTreeController.update.mojo" />
    <mjl:command name="temporary.junit.test.SomeTree.form.delete.button" value="Delete" action="temporary.junit.test.SomeTreeController.delete.mojo" />
    <mjl:command name="temporary.junit.test.SomeTree.form.cancel.button" value="Cancel" action="temporary.junit.test.SomeTreeController.cancel.mojo" />
  </mjl:form>
</dl>
