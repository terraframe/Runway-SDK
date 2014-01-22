<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a JUnitRefType"/>
<dl>
  <mjl:form id="temporary.junit.test.Reference.form.id" name="temporary.junit.test.Reference.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
    </mjl:component>
    <mjl:command name="temporary.junit.test.Reference.form.edit.button" value="Edit" action="temporary.junit.test.ReferenceController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="temporary.junit.test.Reference.viewAll.link" action="temporary.junit.test.ReferenceController.viewAll.mojo">
  View All
</mjl:commandLink>
