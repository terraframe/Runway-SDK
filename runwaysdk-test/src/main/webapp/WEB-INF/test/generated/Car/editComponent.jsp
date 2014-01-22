<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing A car"/>
<dl>
  <mjl:form id="test.generated.Car.form.id" name="test.generated.Car.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="test.generated.Car.form.update.button" value="Update" action="test.generated.CarController.update.mojo" />
    <mjl:command name="test.generated.Car.form.delete.button" value="Delete" action="test.generated.CarController.delete.mojo" />
    <mjl:command name="test.generated.Car.form.cancel.button" value="Cancel" action="test.generated.CarController.cancel.mojo" />
  </mjl:form>
</dl>
