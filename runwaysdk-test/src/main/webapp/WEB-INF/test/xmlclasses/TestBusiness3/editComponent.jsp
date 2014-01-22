<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing My*long/\Great?Sheet[]NameYEAHHHHHHHHHHHHHHHHHHHHHH"/>
<dl>
  <mjl:form id="test.xmlclasses.TestBusiness3.form.id" name="test.xmlclasses.TestBusiness3.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="test.xmlclasses.TestBusiness3.form.update.button" value="Update" action="test.xmlclasses.TestBusiness3Controller.update.mojo" />
    <mjl:command name="test.xmlclasses.TestBusiness3.form.delete.button" value="Delete" action="test.xmlclasses.TestBusiness3Controller.delete.mojo" />
    <mjl:command name="test.xmlclasses.TestBusiness3.form.cancel.button" value="Cancel" action="test.xmlclasses.TestBusiness3Controller.cancel.mojo" />
  </mjl:form>
</dl>
