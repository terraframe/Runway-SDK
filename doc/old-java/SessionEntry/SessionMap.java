package com.runwaysdk.geodashboard.gis.persist;

public class SessionMap extends SessionMapBase implements com.runwaysdk.generation.loader.
{
  private static final long serialVersionUID = -1714203169;
  
  public SessionMap(String parentOid, String childOid)
  {
    super(parentOid, childOid);
  }
  
  public SessionMap(com.runwaysdk.geodashboard.SessionEntry parent, com.runwaysdk.geodashboard.gis.persist.DashboardMap child)
  {
    this(parent.getOid(), child.getOid());
  }

  
}
