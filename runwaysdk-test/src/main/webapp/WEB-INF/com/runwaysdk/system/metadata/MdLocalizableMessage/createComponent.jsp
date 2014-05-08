<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Create a new MdLocalizable Message"/>
<dl>
  <mjl:form id="com.runwaysdk.system.metadata.MdLocalizableMessage.form.id" name="com.runwaysdk.system.metadata.MdLocalizableMessage.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.system.metadata.MdLocalizableMessage.form.create.button" value="Create" action="com.runwaysdk.system.metadata.MdLocalizableMessageController.create.mojo" />
  </mjl:form>
</dl>
