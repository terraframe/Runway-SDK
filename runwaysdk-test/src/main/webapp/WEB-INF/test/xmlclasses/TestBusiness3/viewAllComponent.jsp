<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View all My*long/\Great?Sheet[]NameYEAHHHHHHHHHHHHHHHHHHHHHH"/>
<mjl:table var="item" query="${query}">
  <mjl:context action="test.xmlclasses.TestBusiness3Controller.viewPage.mojo" />
  <mjl:columns>
    <mjl:freeColumn>
      <mjl:header>
        
      </mjl:header>
      <mjl:row>
        <mjl:commandLink name="view.link" action="test.xmlclasses.TestBusiness3Controller.view.mojo">
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
<mjl:commandLink name="TestBusiness3Controller.newInstance" action="test.xmlclasses.TestBusiness3Controller.newInstance.mojo">
  Create a new My*long/\Great?Sheet[]NameYEAHHHHHHHHHHHHHHHHHHHHHH
</mjl:commandLink>
