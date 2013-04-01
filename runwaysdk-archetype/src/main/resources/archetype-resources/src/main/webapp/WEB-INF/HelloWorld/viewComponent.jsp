<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View"/>
<dl>
  <mjl:form id="${package}.HelloWorld.form.id" name="${package}.HelloWorld.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="greeting">
        ${item.greeting}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="${package}.HelloWorld.form.edit.button" value="Edit" action="${package}.HelloWorldController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="${package}.HelloWorld.viewAll.link" action="${package}.HelloWorldController.viewAll.mojo">
  View All
</mjl:commandLink>
