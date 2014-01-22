<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Comment Text"/>
<dl>
  <mjl:form id="com.runwaysdk.system.metadata.MdWebCommentCommentText.form.id" name="com.runwaysdk.system.metadata.MdWebCommentCommentText.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="d1_defaultLocale">
        ${item.d1_defaultLocale}
      </mjl:dt>
      <mjl:dt attribute="d2_defaultLocale">
        ${item.d2_defaultLocale}
      </mjl:dt>
      <mjl:dt attribute="defaultLocale">
        ${item.defaultLocale}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="com.runwaysdk.system.metadata.MdWebCommentCommentText.form.edit.button" value="Edit" action="com.runwaysdk.system.metadata.MdWebCommentCommentTextController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="com.runwaysdk.system.metadata.MdWebCommentCommentText.viewAll.link" action="com.runwaysdk.system.metadata.MdWebCommentCommentTextController.viewAll.mojo">
  View All
</mjl:commandLink>
