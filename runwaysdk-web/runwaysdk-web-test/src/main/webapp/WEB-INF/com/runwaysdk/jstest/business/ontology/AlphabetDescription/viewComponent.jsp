<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Description"/>
<dl>
  <mjl:form id="com.runwaysdk.jstest.business.ontology.AlphabetDescription.form.id" name="com.runwaysdk.jstest.business.ontology.AlphabetDescription.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="defaultLocale">
        ${item.defaultLocale}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="com.runwaysdk.jstest.business.ontology.AlphabetDescription.form.edit.button" value="Edit" action="com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="com.runwaysdk.jstest.business.ontology.AlphabetDescription.viewAll.link" action="com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.viewAll.mojo">
  View All
</mjl:commandLink>
