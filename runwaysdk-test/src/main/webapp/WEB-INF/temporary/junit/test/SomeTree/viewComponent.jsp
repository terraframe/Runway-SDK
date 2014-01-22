<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a some tree Relationship"/>
<dl>
  <mjl:form id="temporary.junit.test.SomeTree.form.id" name="temporary.junit.test.SomeTree.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <dt>
        <label>
          JUnit Test Type
        </label>
      </dt>
      <dd>
        <mjl:commandLink name="temporary.junit.test.Test.form.view.link" action="temporary.junit.test.TestController.view.mojo">
          ${item.parent.keyName}
          <mjl:property name="id" value="${item.parentId}" />
        </mjl:commandLink>
      </dd>
      <dt>
        <label>
          JUnitRefType
        </label>
      </dt>
      <dd>
        <mjl:commandLink name="temporary.junit.test.Reference.form.view.link" action="temporary.junit.test.ReferenceController.view.mojo">
          ${item.parent.keyName}
          <mjl:property name="id" value="${item.parentId}" />
        </mjl:commandLink>
      </dd>
    </mjl:component>
    <mjl:command name="temporary.junit.test.SomeTree.form.edit.button" value="Edit" action="temporary.junit.test.SomeTreeController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="temporary.junit.test.SomeTree.viewAll.link" action="temporary.junit.test.SomeTreeController.viewAll.mojo">
  View All
</mjl:commandLink>
