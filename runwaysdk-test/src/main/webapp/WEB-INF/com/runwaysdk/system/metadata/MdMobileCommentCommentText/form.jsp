<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<mjl:component param="dto" item="${item}">
  <mjl:dt attribute="d1_defaultLocale">
    <mjl:input param="d1_defaultLocale" type="text" />
  </mjl:dt>
  <mjl:dt attribute="d2_defaultLocale">
    <mjl:input param="d2_defaultLocale" type="text" />
  </mjl:dt>
  <mjl:dt attribute="defaultLocale">
    <mjl:input param="defaultLocale" type="text" />
  </mjl:dt>
</mjl:component>
