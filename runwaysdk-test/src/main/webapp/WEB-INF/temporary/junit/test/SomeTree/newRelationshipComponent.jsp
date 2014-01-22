<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Select some tree Relationship Participants"/>
<mjl:form id="temporary.junit.test.SomeTree.form.id" name="temporary.junit.test.SomeTree.form.name" method="POST">
  <dl>
    <dt>
      <label>
        JUnit Test Type
      </label>
    </dt>
    <dd>
      <mjl:select param="parentId" items="${parentList}" var="current" valueAttribute="id">
        <mjl:option>
          ${current.keyName}
        </mjl:option>
      </mjl:select>
    </dd>
    <dt>
      <label>
        JUnitRefType
      </label>
    </dt>
    <dd>
      <mjl:select param="childId" items="${childList}" var="current" valueAttribute="id">
        <mjl:option>
          ${current.keyName}
        </mjl:option>
      </mjl:select>
    </dd>
    <mjl:command name="temporary.junit.test.SomeTree.form.newInstance.button" value="New Instance" action="temporary.junit.test.SomeTreeController.newInstance.mojo" />
  </dl>
</mjl:form>
