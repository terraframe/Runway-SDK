<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing JS Test Class"/>
<dl>
  <mjl:form id="com.runwaysdk.jstest.business.ontology.Alphabet.form.id" name="com.runwaysdk.jstest.business.ontology.Alphabet.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.jstest.business.ontology.Alphabet.form.update.button" value="Update" action="com.runwaysdk.jstest.business.ontology.AlphabetController.update.mojo" />
    <mjl:command name="com.runwaysdk.jstest.business.ontology.Alphabet.form.delete.button" value="Delete" action="com.runwaysdk.jstest.business.ontology.AlphabetController.delete.mojo" />
    <mjl:command name="com.runwaysdk.jstest.business.ontology.Alphabet.form.cancel.button" value="Cancel" action="com.runwaysdk.jstest.business.ontology.AlphabetController.cancel.mojo" />
  </mjl:form>
</dl>
