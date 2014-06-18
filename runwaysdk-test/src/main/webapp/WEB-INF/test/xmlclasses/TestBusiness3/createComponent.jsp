<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Create a new My*long/\Great?Sheet[]NameYEAHHHHHHHHHHHHHHHHHHHHHH"/>
<dl>
  <mjl:form id="test.xmlclasses.TestBusiness3.form.id" name="test.xmlclasses.TestBusiness3.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="test.xmlclasses.TestBusiness3.form.create.button" value="Create" action="test.xmlclasses.TestBusiness3Controller.create.mojo" />
  </mjl:form>
</dl>
