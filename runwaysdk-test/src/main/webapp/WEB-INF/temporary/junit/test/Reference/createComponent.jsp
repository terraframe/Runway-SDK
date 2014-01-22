<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Create a new JUnitRefType"/>
<dl>
  <mjl:form id="temporary.junit.test.Reference.form.id" name="temporary.junit.test.Reference.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="temporary.junit.test.Reference.form.create.button" value="Create" action="temporary.junit.test.ReferenceController.create.mojo" />
  </mjl:form>
</dl>
