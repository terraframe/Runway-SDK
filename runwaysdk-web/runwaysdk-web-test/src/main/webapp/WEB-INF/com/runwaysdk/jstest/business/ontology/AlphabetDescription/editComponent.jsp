<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Description"/>
<dl>
  <mjl:form id="com.runwaysdk.jstest.business.ontology.AlphabetDescription.form.id" name="com.runwaysdk.jstest.business.ontology.AlphabetDescription.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.jstest.business.ontology.AlphabetDescription.form.update.button" value="Update" action="com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.update.mojo" />
    <mjl:command name="com.runwaysdk.jstest.business.ontology.AlphabetDescription.form.delete.button" value="Delete" action="com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.delete.mojo" />
    <mjl:command name="com.runwaysdk.jstest.business.ontology.AlphabetDescription.form.cancel.button" value="Cancel" action="com.runwaysdk.jstest.business.ontology.AlphabetDescriptionController.cancel.mojo" />
  </mjl:form>
</dl>
