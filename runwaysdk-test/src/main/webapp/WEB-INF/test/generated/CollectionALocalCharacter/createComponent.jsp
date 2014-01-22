<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Create a new A Local Character"/>
<dl>
  <mjl:form id="test.generated.CollectionALocalCharacter.form.id" name="test.generated.CollectionALocalCharacter.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="test.generated.CollectionALocalCharacter.form.create.button" value="Create" action="test.generated.CollectionALocalCharacterController.create.mojo" />
  </mjl:form>
</dl>
