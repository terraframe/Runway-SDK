package com.runwaysdk.query;

import java.util.Iterator;
import java.util.List;

public class ListOIterator<T> implements OIterator<T>
{
    List<T>     list;

    Iterator<T> it;

    public ListOIterator(List<T> list)
    {
      this.list = list;
      it = this.list.iterator();
    }

    @Override
    public Iterator<T> iterator()
    {
      return it;
    }

    @Override
    public T next()
    {
      return it.next();
    }

    @Override
    public void remove()
    {
      it.remove();
    }

    @Override
    public boolean hasNext()
    {
      return it.hasNext();
    }

    @Override
    public void close()
    {

    }

    @Override
    public List<T> getAll()
    {
      return list;
    }

}
