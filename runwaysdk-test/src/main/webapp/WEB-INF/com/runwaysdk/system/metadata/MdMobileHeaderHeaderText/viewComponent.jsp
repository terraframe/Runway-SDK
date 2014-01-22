<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Header Text"/>
<dl>
  <mjl:form id="com.runwaysdk.system.metadata.MdMobileHeaderHeaderText.form.id" name="com.runwaysdk.system.metadata.MdMobileHeaderHeaderText.form.name" method="POST">
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
    <mjl:command name="com.runwaysdk.system.metadata.MdMobileHeaderHeaderText.form.edit.button" value="Edit" action="com.runwaysdk.system.metadata.MdMobileHeaderHeaderTextController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="com.runwaysdk.system.metadata.MdMobileHeaderHeaderText.viewAll.link" action="com.runwaysdk.system.metadata.MdMobileHeaderHeaderTextController.viewAll.mojo">
  View All
</mjl:commandLink>
