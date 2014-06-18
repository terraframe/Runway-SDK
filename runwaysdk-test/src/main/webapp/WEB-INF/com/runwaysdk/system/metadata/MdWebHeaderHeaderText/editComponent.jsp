<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Header Text"/>
<dl>
  <mjl:form id="com.runwaysdk.system.metadata.MdWebHeaderHeaderText.form.id" name="com.runwaysdk.system.metadata.MdWebHeaderHeaderText.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.system.metadata.MdWebHeaderHeaderText.form.update.button" value="Update" action="com.runwaysdk.system.metadata.MdWebHeaderHeaderTextController.update.mojo" />
    <mjl:command name="com.runwaysdk.system.metadata.MdWebHeaderHeaderText.form.delete.button" value="Delete" action="com.runwaysdk.system.metadata.MdWebHeaderHeaderTextController.delete.mojo" />
    <mjl:command name="com.runwaysdk.system.metadata.MdWebHeaderHeaderText.form.cancel.button" value="Cancel" action="com.runwaysdk.system.metadata.MdWebHeaderHeaderTextController.cancel.mojo" />
  </mjl:form>
</dl>
