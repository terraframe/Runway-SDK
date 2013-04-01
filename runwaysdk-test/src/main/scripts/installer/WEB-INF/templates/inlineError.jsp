<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="alert alertbox">
<p>

<c:if test="${errorMessage != null && errorMessage != ''}">
  <str:replace replace="\\\\n" with="<br />NL" newlineToken="NL"><str:escape>${errorMessage}</str:escape></str:replace> 
</c:if>

<c:if test="${errorMessageArray != null}">
  <str:escape><str:join separator="<br />" items="${errorMessageArray}"></str:join></str:escape>
</c:if>

<c:if test="${(errorMessage != null && errorMessage != '') || errorMessageArray != null}">
</c:if>

</p>
</div>
