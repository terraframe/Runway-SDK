package com.runwaysdk.jstest;

public class Befriends extends BefriendsBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -1106016064;
  
  public Befriends(String parentId, String childId)
  {
    super(parentId, childId);
  }
  
  public Befriends(com.runwaysdk.jstest.TestClass parent, com.runwaysdk.jstest.RefClass child)
  {
    this(parent.getId(), child.getId());
  }
  
}
