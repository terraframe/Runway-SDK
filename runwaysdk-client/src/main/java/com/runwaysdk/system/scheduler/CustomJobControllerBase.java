package com.runwaysdk.system.scheduler;

@com.runwaysdk.business.ClassSignature(hash = 592859776)
public class CustomJobControllerBase
{
  public static final String CLASS = "com.runwaysdk.system.scheduler.CustomJobController";
  protected javax.servlet.http.HttpServletRequest req;
  protected javax.servlet.http.HttpServletResponse resp;
  protected java.lang.Boolean isAsynchronous;
  protected java.lang.String dir;
  protected java.lang.String layout;
  
  public CustomJobControllerBase(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    this(req, resp, isAsynchronous, "","");
  }
  
  public CustomJobControllerBase(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous, java.lang.String dir, java.lang.String layout)
  {
    this.req = req;
    this.resp = resp;
    this.isAsynchronous = isAsynchronous;
    this.dir = dir;
    this.layout = layout;
  }
  
  protected void render(String jsp) throws java.io.IOException, javax.servlet.ServletException
  {
    if(!resp.isCommitted())
    {
      if(this.isAsynchronous())
      {
        req.getRequestDispatcher(dir+jsp).forward(req, resp);
      }
      else
      {
        req.setAttribute(com.runwaysdk.controller.JSPFetcher.INNER_JSP, dir+jsp);
        req.getRequestDispatcher(layout).forward(req, resp);
      }
    }
  }
  
  public javax.servlet.http.HttpServletRequest getRequest()
  {
    return this.req;
  }
  
  public javax.servlet.http.HttpServletResponse getResponse()
  {
    return this.resp;
  }
  
  public java.lang.Boolean isAsynchronous()
  {
    return this.isAsynchronous;
  }
  
  public com.runwaysdk.constants.ClientRequestIF getClientRequest()
  {
    return (com.runwaysdk.constants.ClientRequestIF) req.getAttribute(com.runwaysdk.constants.ClientConstants.CLIENTREQUEST);
  }
  
  public com.runwaysdk.ClientSession getClientSession()
  {
    return (com.runwaysdk.ClientSession) req.getSession().getAttribute(com.runwaysdk.constants.ClientConstants.CLIENTSESSION);
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.system.scheduler.CustomJobDTO:dto", post=true)
  public void cancel(com.runwaysdk.system.scheduler.CustomJobDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.scheduler.CustomJobController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.scheduler.CustomJobController.cancel");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.system.scheduler.CustomJobDTO:dto", post=true)
  public void failCancel(com.runwaysdk.system.scheduler.CustomJobDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.scheduler.CustomJobController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.scheduler.CustomJobController.failCancel");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.system.scheduler.CustomJobDTO:dto", post=true)
  public void create(com.runwaysdk.system.scheduler.CustomJobDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.scheduler.CustomJobController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.scheduler.CustomJobController.create");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.system.scheduler.CustomJobDTO:dto", post=true)
  public void failCreate(com.runwaysdk.system.scheduler.CustomJobDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.scheduler.CustomJobController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.scheduler.CustomJobController.failCreate");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.system.scheduler.CustomJobDTO:dto", post=true)
  public void delete(com.runwaysdk.system.scheduler.CustomJobDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.scheduler.CustomJobController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.scheduler.CustomJobController.delete");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.system.scheduler.CustomJobDTO:dto", post=true)
  public void failDelete(com.runwaysdk.system.scheduler.CustomJobDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.scheduler.CustomJobController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.scheduler.CustomJobController.failDelete");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:id", post=false)
  public void edit(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.scheduler.CustomJobController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.scheduler.CustomJobController.edit");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:id", post=false)
  public void failEdit(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.scheduler.CustomJobController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.scheduler.CustomJobController.failEdit");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=false)
  public void newInstance() throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.scheduler.CustomJobController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.scheduler.CustomJobController.newInstance");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=false)
  public void failNewInstance() throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.scheduler.CustomJobController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.scheduler.CustomJobController.failNewInstance");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.system.scheduler.CustomJobDTO:dto", post=true)
  public void update(com.runwaysdk.system.scheduler.CustomJobDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.scheduler.CustomJobController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.scheduler.CustomJobController.update");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.system.scheduler.CustomJobDTO:dto", post=true)
  public void failUpdate(com.runwaysdk.system.scheduler.CustomJobDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.scheduler.CustomJobController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.scheduler.CustomJobController.failUpdate");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:id", post=false)
  public void view(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.scheduler.CustomJobController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.scheduler.CustomJobController.view");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:id", post=false)
  public void failView(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.scheduler.CustomJobController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.scheduler.CustomJobController.failView");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=false)
  public void viewAll() throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.scheduler.CustomJobController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.scheduler.CustomJobController.viewAll");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=false)
  public void failViewAll() throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.scheduler.CustomJobController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.scheduler.CustomJobController.failViewAll");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:sortAttribute, java.lang.Boolean:isAscending, java.lang.Integer:pageSize, java.lang.Integer:pageNumber", post=false)
  public void viewPage(java.lang.String sortAttribute, java.lang.Boolean isAscending, java.lang.Integer pageSize, java.lang.Integer pageNumber) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.scheduler.CustomJobController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.scheduler.CustomJobController.viewPage");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:sortAttribute, java.lang.String:isAscending, java.lang.String:pageSize, java.lang.String:pageNumber", post=false)
  public void failViewPage(java.lang.String sortAttribute, java.lang.String isAscending, java.lang.String pageSize, java.lang.String pageNumber) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.scheduler.CustomJobController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.scheduler.CustomJobController.failViewPage");
  }
  
}
