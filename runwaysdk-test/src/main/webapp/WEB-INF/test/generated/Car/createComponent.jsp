<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Create a new A car"/>
<dl>
  <mjl:form id="test.generated.Car.form.id" name="test.generated.Car.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="test.generated.Car.form.create.button" value="Create" action="test.generated.CarController.create.mojo" />
  </mjl:form>
</dl>
