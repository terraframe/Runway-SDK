<%--

    Copyright (c) 2015 TerraFrame, Inc. All rights reserved.

    This file is part of Runway SDK(tm).

    Runway SDK(tm) is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    Runway SDK(tm) is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.

--%>
<jsp:include page="WEB-INF/templates/header.jsp"></jsp:include>

<a href="/${artifactId}/${package}.HelloWorldController.viewAll.mojo">Click Here to view greetings.</a>

<br/>
<br/>

<a href="javascript:com.runwaysdk.inspector.Inspector.launch()">Click here to open the Runway SDK Javascript Inspector.</a>

<br/>

<script type="text/javascript">
  var rwjs = Mojo.Meta.newInstance("com.example.MyRunwayJS");
  document.write("<p>" + rwjs.fReturnString() + "</p>");
</script>

<jsp:include page="WEB-INF/templates/footer.jsp"></jsp:include>