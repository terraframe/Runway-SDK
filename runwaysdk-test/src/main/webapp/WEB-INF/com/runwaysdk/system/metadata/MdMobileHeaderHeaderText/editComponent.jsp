<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Header Text"/>
<dl>
  <mjl:form id="com.runwaysdk.system.metadata.MdMobileHeaderHeaderText.form.id" name="com.runwaysdk.system.metadata.MdMobileHeaderHeaderText.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.system.metadata.MdMobileHeaderHeaderText.form.update.button" value="Update" action="com.runwaysdk.system.metadata.MdMobileHeaderHeaderTextController.update.mojo" />
    <mjl:command name="com.runwaysdk.system.metadata.MdMobileHeaderHeaderText.form.delete.button" value="Delete" action="com.runwaysdk.system.metadata.MdMobileHeaderHeaderTextController.delete.mojo" />
    <mjl:command name="com.runwaysdk.system.metadata.MdMobileHeaderHeaderText.form.cancel.button" value="Cancel" action="com.runwaysdk.system.metadata.MdMobileHeaderHeaderTextController.cancel.mojo" />
  </mjl:form>
</dl>
