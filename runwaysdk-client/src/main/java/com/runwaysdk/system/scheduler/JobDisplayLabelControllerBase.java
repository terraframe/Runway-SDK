package com.runwaysdk.system.scheduler;

@com.runwaysdk.business.ClassSignature(hash = -409036369)
public class JobDisplayLabelControllerBase
{
  public static final String CLASS = "com.runwaysdk.system.scheduler.JobDisplayLabelController";
  protected javax.servlet.http.HttpServletRequest req;
  protected javax.servlet.http.HttpServletResponse resp;
  protected java.lang.Boolean isAsynchronous;
  protected java.lang.String dir;
  protected java.lang.String layout;
  
  public JobDisplayLabelControllerBase(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous)
  {
    this(req, resp, isAsynchronous, "","");
  }
  
  public JobDisplayLabelControllerBase(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean isAsynchronous, java.lang.String dir, java.lang.String layout)
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
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.system.scheduler.JobDisplayLabelDTO:dto", post=true)
  public void cancel(com.runwaysdk.system.scheduler.JobDisplayLabelDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.scheduler.JobDisplayLabelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.scheduler.JobDisplayLabelController.cancel");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.system.scheduler.JobDisplayLabelDTO:dto", post=true)
  public void failCancel(com.runwaysdk.system.scheduler.JobDisplayLabelDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.scheduler.JobDisplayLabelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.scheduler.JobDisplayLabelController.failCancel");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.system.scheduler.JobDisplayLabelDTO:dto", post=true)
  public void create(com.runwaysdk.system.scheduler.JobDisplayLabelDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.scheduler.JobDisplayLabelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.scheduler.JobDisplayLabelController.create");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.system.scheduler.JobDisplayLabelDTO:dto", post=true)
  public void failCreate(com.runwaysdk.system.scheduler.JobDisplayLabelDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.scheduler.JobDisplayLabelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.scheduler.JobDisplayLabelController.failCreate");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.system.scheduler.JobDisplayLabelDTO:dto", post=true)
  public void delete(com.runwaysdk.system.scheduler.JobDisplayLabelDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.scheduler.JobDisplayLabelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.scheduler.JobDisplayLabelController.delete");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.system.scheduler.JobDisplayLabelDTO:dto", post=true)
  public void failDelete(com.runwaysdk.system.scheduler.JobDisplayLabelDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.scheduler.JobDisplayLabelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.scheduler.JobDisplayLabelController.failDelete");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:id", post=false)
  public void edit(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.scheduler.JobDisplayLabelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.scheduler.JobDisplayLabelController.edit");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:id", post=false)
  public void failEdit(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.scheduler.JobDisplayLabelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.scheduler.JobDisplayLabelController.failEdit");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=false)
  public void newInstance() throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.scheduler.JobDisplayLabelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.scheduler.JobDisplayLabelController.newInstance");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=false)
  public void failNewInstance() throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.scheduler.JobDisplayLabelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.scheduler.JobDisplayLabelController.failNewInstance");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.system.scheduler.JobDisplayLabelDTO:dto", post=true)
  public void update(com.runwaysdk.system.scheduler.JobDisplayLabelDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.scheduler.JobDisplayLabelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.scheduler.JobDisplayLabelController.update");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="com.runwaysdk.system.scheduler.JobDisplayLabelDTO:dto", post=true)
  public void failUpdate(com.runwaysdk.system.scheduler.JobDisplayLabelDTO dto) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.scheduler.JobDisplayLabelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.scheduler.JobDisplayLabelController.failUpdate");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:id", post=false)
  public void view(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.scheduler.JobDisplayLabelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.scheduler.JobDisplayLabelController.view");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:id", post=false)
  public void failView(java.lang.String id) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.scheduler.JobDisplayLabelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.scheduler.JobDisplayLabelController.failView");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=false)
  public void viewAll() throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.scheduler.JobDisplayLabelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.scheduler.JobDisplayLabelController.viewAll");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="", post=false)
  public void failViewAll() throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.scheduler.JobDisplayLabelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.scheduler.JobDisplayLabelController.failViewAll");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:sortAttribute, java.lang.Boolean:isAscending, java.lang.Integer:pageSize, java.lang.Integer:pageNumber", post=false)
  public void viewPage(java.lang.String sortAttribute, java.lang.Boolean isAscending, java.lang.Integer pageSize, java.lang.Integer pageNumber) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.scheduler.JobDisplayLabelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.scheduler.JobDisplayLabelController.viewPage");
  }
  
  @com.runwaysdk.controller.ActionParameters(parameters="java.lang.String:sortAttribute, java.lang.String:isAscending, java.lang.String:pageSize, java.lang.String:pageNumber", post=false)
  public void failViewPage(java.lang.String sortAttribute, java.lang.String isAscending, java.lang.String pageSize, java.lang.String pageNumber) throws java.io.IOException, javax.servlet.ServletException
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.scheduler.JobDisplayLabelController.java";
    throw new com.runwaysdk.controller.UndefinedControllerActionException(msg, req.getLocale(), "com.runwaysdk.system.scheduler.JobDisplayLabelController.failViewPage");
  }
  
}
