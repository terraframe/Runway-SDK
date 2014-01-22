<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a JUnit Test Type"/>
<dl>
  <mjl:form id="temporary.junit.test.Test.form.id" name="temporary.junit.test.Test.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
    </mjl:component>
    <mjl:command name="temporary.junit.test.Test.form.edit.button" value="Edit" action="temporary.junit.test.TestController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="temporary.junit.test.Test.viewAll.link" action="temporary.junit.test.TestController.viewAll.mojo">
  View All
</mjl:commandLink>
