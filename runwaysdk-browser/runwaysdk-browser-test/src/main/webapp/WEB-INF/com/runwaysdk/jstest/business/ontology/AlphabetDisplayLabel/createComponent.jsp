<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Create a new "/>
<dl>
  <mjl:form id="com.runwaysdk.jstest.business.ontology.AlphabetDisplayLabel.form.id" name="com.runwaysdk.jstest.business.ontology.AlphabetDisplayLabel.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.jstest.business.ontology.AlphabetDisplayLabel.form.create.button" value="Create" action="com.runwaysdk.jstest.business.ontology.AlphabetDisplayLabelController.create.mojo" />
  </mjl:form>
</dl>
