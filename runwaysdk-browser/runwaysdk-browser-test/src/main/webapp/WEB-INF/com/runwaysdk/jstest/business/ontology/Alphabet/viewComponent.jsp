<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a JS Test Class"/>
<dl>
  <mjl:form id="com.runwaysdk.jstest.business.ontology.Alphabet.form.id" name="com.runwaysdk.jstest.business.ontology.Alphabet.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
    </mjl:component>
    <mjl:command name="com.runwaysdk.jstest.business.ontology.Alphabet.form.edit.button" value="Edit" action="com.runwaysdk.jstest.business.ontology.AlphabetController.edit.mojo" />
  </mjl:form>
</dl>
<dl>
</dl>
<mjl:commandLink name="com.runwaysdk.jstest.business.ontology.Alphabet.viewAll.link" action="com.runwaysdk.jstest.business.ontology.AlphabetController.viewAll.mojo">
  View All
</mjl:commandLink>
