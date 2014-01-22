<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Create a new Header Text"/>
<dl>
  <mjl:form id="com.runwaysdk.system.metadata.MdMobileHeaderHeaderText.form.id" name="com.runwaysdk.system.metadata.MdMobileHeaderHeaderText.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.system.metadata.MdMobileHeaderHeaderText.form.create.button" value="Create" action="com.runwaysdk.system.metadata.MdMobileHeaderHeaderTextController.create.mojo" />
  </mjl:form>
</dl>