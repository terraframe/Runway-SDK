<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Display Label"/>
<dl>
  <mjl:form id="com.runwaysdk.system.metadata.MdFieldDisplayLabel.form.id" name="com.runwaysdk.system.metadata.MdFieldDisplayLabel.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.system.metadata.MdFieldDisplayLabel.form.update.button" value="Update" action="com.runwaysdk.system.metadata.MdFieldDisplayLabelController.update.mojo" />
    <mjl:command name="com.runwaysdk.system.metadata.MdFieldDisplayLabel.form.delete.button" value="Delete" action="com.runwaysdk.system.metadata.MdFieldDisplayLabelController.delete.mojo" />
    <mjl:command name="com.runwaysdk.system.metadata.MdFieldDisplayLabel.form.cancel.button" value="Cancel" action="com.runwaysdk.system.metadata.MdFieldDisplayLabelController.cancel.mojo" />
  </mjl:form>
</dl>
