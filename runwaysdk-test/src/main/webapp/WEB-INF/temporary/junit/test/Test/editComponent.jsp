<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing JUnit Test Type"/>
<dl>
  <mjl:form id="temporary.junit.test.Test.form.id" name="temporary.junit.test.Test.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="temporary.junit.test.Test.form.update.button" value="Update" action="temporary.junit.test.TestController.update.mojo" />
    <mjl:command name="temporary.junit.test.Test.form.delete.button" value="Delete" action="temporary.junit.test.TestController.delete.mojo" />
    <mjl:command name="temporary.junit.test.Test.form.cancel.button" value="Cancel" action="temporary.junit.test.TestController.cancel.mojo" />
  </mjl:form>
</dl>
