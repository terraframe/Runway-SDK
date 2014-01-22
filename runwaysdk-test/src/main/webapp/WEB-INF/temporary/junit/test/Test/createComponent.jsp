<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Create a new JUnit Test Type"/>
<dl>
  <mjl:form id="temporary.junit.test.Test.form.id" name="temporary.junit.test.Test.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="temporary.junit.test.Test.form.create.button" value="Create" action="temporary.junit.test.TestController.create.mojo" />
  </mjl:form>
</dl>
