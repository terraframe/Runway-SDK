<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing A Local Text"/>
<dl>
  <mjl:form id="test.generated.CollectionALocalText.form.id" name="test.generated.CollectionALocalText.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="test.generated.CollectionALocalText.form.update.button" value="Update" action="test.generated.CollectionALocalTextController.update.mojo" />
    <mjl:command name="test.generated.CollectionALocalText.form.delete.button" value="Delete" action="test.generated.CollectionALocalTextController.delete.mojo" />
    <mjl:command name="test.generated.CollectionALocalText.form.cancel.button" value="Cancel" action="test.generated.CollectionALocalTextController.cancel.mojo" />
  </mjl:form>
</dl>
