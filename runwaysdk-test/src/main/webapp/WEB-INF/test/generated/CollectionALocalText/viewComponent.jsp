<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a A Local Text"/>
<dl>
  <mjl:form id="test.generated.CollectionALocalText.form.id" name="test.generated.CollectionALocalText.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="defaultLocale">
        ${item.defaultLocale}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="test.generated.CollectionALocalText.form.edit.button" value="Edit" action="test.generated.CollectionALocalTextController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="test.generated.CollectionALocalText.viewAll.link" action="test.generated.CollectionALocalTextController.viewAll.mojo">
  View All
</mjl:commandLink>
