package com.runwaysdk.session;

import java.io.Serializable;
import java.util.concurrent.locks.ReentrantLock;

public class CloseableReentrantLock extends ReentrantLock implements AutoCloseable, Serializable
{

  private static final long serialVersionUID = 7581534074544018067L;

  public CloseableReentrantLock()
  {
    super();
  }

  public CloseableReentrantLock(boolean fair)
  {
    super(fair);
  }

  public CloseableReentrantLock open()
  {
    this.lock();
    return this;
  }

  @Override
  public void close()
  {
    if (this.isLocked())
    {
      this.unlock();
    }
  }

}
