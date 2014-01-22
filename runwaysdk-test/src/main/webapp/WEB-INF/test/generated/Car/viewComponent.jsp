<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a A car"/>
<dl>
  <mjl:form id="test.generated.Car.form.id" name="test.generated.Car.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
    </mjl:component>
    <mjl:command name="test.generated.Car.form.edit.button" value="Edit" action="test.generated.CarController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="test.generated.Car.viewAll.link" action="test.generated.CarController.viewAll.mojo">
  View All
</mjl:commandLink>
