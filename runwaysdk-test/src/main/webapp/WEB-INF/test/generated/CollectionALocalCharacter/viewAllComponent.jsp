<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View all A Local Character"/>
<mjl:table var="item" query="${query}">
  <mjl:context action="test.generated.CollectionALocalCharacterController.viewPage.mojo" />
  <mjl:columns>
    <mjl:attributeColumn attributeName="defaultLocale">
    </mjl:attributeColumn>
    <mjl:freeColumn>
      <mjl:header>
        
      </mjl:header>
      <mjl:row>
        <mjl:commandLink name="view.link" action="test.generated.CollectionALocalCharacterController.view.mojo">
          View
          <mjl:property name="id" value="${item.id}" />
        </mjl:commandLink>
      </mjl:row>
      <mjl:footer>
        
      </mjl:footer>
    </mjl:freeColumn>
  </mjl:columns>
  <mjl:pagination>
    <mjl:page />
  </mjl:pagination>
</mjl:table>
<br />
<mjl:commandLink name="CollectionALocalCharacterController.newInstance" action="test.generated.CollectionALocalCharacterController.newInstance.mojo">
  Create a new A Local Character
</mjl:commandLink>
