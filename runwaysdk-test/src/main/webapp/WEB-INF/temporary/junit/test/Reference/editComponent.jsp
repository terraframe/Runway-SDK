<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing JUnitRefType"/>
<dl>
  <mjl:form id="temporary.junit.test.Reference.form.id" name="temporary.junit.test.Reference.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="temporary.junit.test.Reference.form.update.button" value="Update" action="temporary.junit.test.ReferenceController.update.mojo" />
    <mjl:command name="temporary.junit.test.Reference.form.delete.button" value="Delete" action="temporary.junit.test.ReferenceController.delete.mojo" />
    <mjl:command name="temporary.junit.test.Reference.form.cancel.button" value="Cancel" action="temporary.junit.test.ReferenceController.cancel.mojo" />
  </mjl:form>
</dl>
