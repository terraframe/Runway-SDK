<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing "/>
<dl>
  <mjl:form id="com.runwaysdk.jstest.business.ontology.AlphabetDisplayLabel.form.id" name="com.runwaysdk.jstest.business.ontology.AlphabetDisplayLabel.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.jstest.business.ontology.AlphabetDisplayLabel.form.update.button" value="Update" action="com.runwaysdk.jstest.business.ontology.AlphabetDisplayLabelController.update.mojo" />
    <mjl:command name="com.runwaysdk.jstest.business.ontology.AlphabetDisplayLabel.form.delete.button" value="Delete" action="com.runwaysdk.jstest.business.ontology.AlphabetDisplayLabelController.delete.mojo" />
    <mjl:command name="com.runwaysdk.jstest.business.ontology.AlphabetDisplayLabel.form.cancel.button" value="Cancel" action="com.runwaysdk.jstest.business.ontology.AlphabetDisplayLabelController.cancel.mojo" />
  </mjl:form>
</dl>
