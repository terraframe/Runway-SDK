<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Comment Text"/>
<dl>
  <mjl:form id="com.runwaysdk.system.metadata.MdMobileCommentCommentText.form.id" name="com.runwaysdk.system.metadata.MdMobileCommentCommentText.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.system.metadata.MdMobileCommentCommentText.form.update.button" value="Update" action="com.runwaysdk.system.metadata.MdMobileCommentCommentTextController.update.mojo" />
    <mjl:command name="com.runwaysdk.system.metadata.MdMobileCommentCommentText.form.delete.button" value="Delete" action="com.runwaysdk.system.metadata.MdMobileCommentCommentTextController.delete.mojo" />
    <mjl:command name="com.runwaysdk.system.metadata.MdMobileCommentCommentText.form.cancel.button" value="Cancel" action="com.runwaysdk.system.metadata.MdMobileCommentCommentTextController.cancel.mojo" />
  </mjl:form>
</dl>
