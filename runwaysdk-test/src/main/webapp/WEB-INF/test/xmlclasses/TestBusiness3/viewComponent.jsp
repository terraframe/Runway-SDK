<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a My*long/\Great?Sheet[]NameYEAHHHHHHHHHHHHHHHHHHHHHH"/>
<dl>
  <mjl:form id="test.xmlclasses.TestBusiness3.form.id" name="test.xmlclasses.TestBusiness3.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
    </mjl:component>
    <mjl:command name="test.xmlclasses.TestBusiness3.form.edit.button" value="Edit" action="test.xmlclasses.TestBusiness3Controller.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="test.xmlclasses.TestBusiness3.viewAll.link" action="test.xmlclasses.TestBusiness3Controller.viewAll.mojo">
  View All
</mjl:commandLink>
