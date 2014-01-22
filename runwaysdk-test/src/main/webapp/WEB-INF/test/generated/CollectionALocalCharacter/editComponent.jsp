<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing A Local Character"/>
<dl>
  <mjl:form id="test.generated.CollectionALocalCharacter.form.id" name="test.generated.CollectionALocalCharacter.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="test.generated.CollectionALocalCharacter.form.update.button" value="Update" action="test.generated.CollectionALocalCharacterController.update.mojo" />
    <mjl:command name="test.generated.CollectionALocalCharacter.form.delete.button" value="Delete" action="test.generated.CollectionALocalCharacterController.delete.mojo" />
    <mjl:command name="test.generated.CollectionALocalCharacter.form.cancel.button" value="Cancel" action="test.generated.CollectionALocalCharacterController.cancel.mojo" />
  </mjl:form>
</dl>
