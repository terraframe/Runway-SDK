<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing MdLocalizable Message"/>
<dl>
  <mjl:form id="com.runwaysdk.system.metadata.MdLocalizableMessage.form.id" name="com.runwaysdk.system.metadata.MdLocalizableMessage.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.system.metadata.MdLocalizableMessage.form.update.button" value="Update" action="com.runwaysdk.system.metadata.MdLocalizableMessageController.update.mojo" />
    <mjl:command name="com.runwaysdk.system.metadata.MdLocalizableMessage.form.delete.button" value="Delete" action="com.runwaysdk.system.metadata.MdLocalizableMessageController.delete.mojo" />
    <mjl:command name="com.runwaysdk.system.metadata.MdLocalizableMessage.form.cancel.button" value="Cancel" action="com.runwaysdk.system.metadata.MdLocalizableMessageController.cancel.mojo" />
  </mjl:form>
</dl>
