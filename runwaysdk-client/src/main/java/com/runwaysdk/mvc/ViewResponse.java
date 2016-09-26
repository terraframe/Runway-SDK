package com.runwaysdk.mvc;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletException;

import com.runwaysdk.controller.JSPFetcher;
import com.runwaysdk.controller.RequestManager;
import com.runwaysdk.request.ServletRequestIF;
import com.runwaysdk.request.ServletResponseIF;

public class ViewResponse implements ResponseIF
{
  private Map<String, Object> attributes;

  private String              template;

  private String              path;

  public ViewResponse(String template, String directory, String view)
  {
    this(template, directory + File.separator + view);
  }

  public ViewResponse(String template, String path)
  {
    super();

    this.template = template;
    this.path = path;
    this.attributes = new HashMap<String, Object>();
  }

  public Map<String, Object> getAttributes()
  {
    return attributes;
  }

  public void set(String name, Object o)
  {
    this.attributes.put(name, o);
  }

  public Object getAttribute(String name)
  {
    return this.attributes.get(name);
  }

  @Override
  public void handle(RequestManager manager) throws ServletException, IOException
  {
    ServletRequestIF req = manager.getReq();
    ServletResponseIF resp = manager.getResp();

    Set<Entry<String, Object>> entries = this.attributes.entrySet();

    for (Entry<String, Object> entry : entries)
    {
      req.setAttribute(entry.getKey(), entry.getValue());
    }

    req.setAttribute(JSPFetcher.INNER_JSP, this.path);
    req.getRequestDispatcher(this.template).forward(req, resp);
  }
}
