<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a A Local Character"/>
<dl>
  <mjl:form id="test.generated.CollectionALocalCharacter.form.id" name="test.generated.CollectionALocalCharacter.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="defaultLocale">
        ${item.defaultLocale}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="test.generated.CollectionALocalCharacter.form.edit.button" value="Edit" action="test.generated.CollectionALocalCharacterController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="test.generated.CollectionALocalCharacter.viewAll.link" action="test.generated.CollectionALocalCharacterController.viewAll.mojo">
  View All
</mjl:commandLink>
