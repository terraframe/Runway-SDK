<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Comment Text"/>
<dl>
  <mjl:form id="com.runwaysdk.system.metadata.MdWebCommentCommentText.form.id" name="com.runwaysdk.system.metadata.MdWebCommentCommentText.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.system.metadata.MdWebCommentCommentText.form.update.button" value="Update" action="com.runwaysdk.system.metadata.MdWebCommentCommentTextController.update.mojo" />
    <mjl:command name="com.runwaysdk.system.metadata.MdWebCommentCommentText.form.delete.button" value="Delete" action="com.runwaysdk.system.metadata.MdWebCommentCommentTextController.delete.mojo" />
    <mjl:command name="com.runwaysdk.system.metadata.MdWebCommentCommentText.form.cancel.button" value="Cancel" action="com.runwaysdk.system.metadata.MdWebCommentCommentTextController.cancel.mojo" />
  </mjl:form>
</dl>
