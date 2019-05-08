/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.session;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.io.filefilter.DirectoryFileFilter;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.runwaysdk.business.Mutable;
import com.runwaysdk.business.rbac.SingleActorDAOIF;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.io.FileReadException;
import com.runwaysdk.dataaccess.io.FileWriteException;
import com.runwaysdk.generation.LoaderDecoratorExceptionIF;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.system.metadata.MdDimension;
import com.runwaysdk.util.FileIO;

/**
 * Concrete implementation of {@link FileSessionCache}. The {@link Session}s in
 * this {@link SessionCache} are stored on the file system. In addition, this
 * cache spawns a thread which automatically checks and removes expired
 * {@link Session}s from the cache.
 *
 * @author Justin Smethie
 */
public class FileSessionCache extends ManagedUserSessionCache
{
  private static final int     MAX_DPETH = 6;

  /**
   * The fully qualified path of the root directory for this
   * {@link SessionCache}.
   */
  private String               rootDirectory;

  /**
   * The session oid of the public session
   */
  private String               publicSessionId;

  /**
   * A cache of sessions, the mapping between the session oid and the Session
   */
  private Map<String, Session> sessions;

  /**
   * The number of request open for an individual session
   */
  private Map<String, Integer> requestCount;

  /**
   * Creates a new {@link FileSessionCache}. This cache assumes that any file in
   * the root directory or it's sub-directories is a serialized {@link Session}
   * which belongs to the cache. Thus it is important that the root directory
   * does not overlap another {@link FileSessionCache} or have exteranious files
   * in it.
   *
   * @param rootDirectory
   *          Fully qualified path of the root directory
   */
  @Inject
  public FileSessionCache(@Named("rootDirectory") String rootDirectory)
  {
    super();

    this.rootDirectory = rootDirectory;
    this.sessions = new HashMap<String, Session>();
    this.requestCount = new HashMap<String, Integer>();

    // Make the root directory
    File rootDirectoryFile = new File(rootDirectory);

    boolean directoryCreated = rootDirectoryFile.mkdirs();

    if (!directoryCreated)
    {
      String errMsg = "Error creating the directory for storing serialized sessions";
      new FileWriteException(errMsg, rootDirectoryFile);
    }

    // Make the public session
    Session publicSession = new Session(CommonProperties.getDefaultLocale());
    publicSession.setExpirationTime(-1);

    this.publicSessionId = publicSession.getOid();
    this.addSession(publicSession);
  }

  @Override
  protected void addSession(Session session)
  {
    if (!sessions.containsKey(session.getOid()))
    {
      putSessionOnFileSystem(session, true);
    }
  }

  /**
   * Serializes a {@link Session} to the file system. If the {@link Session}
   * already exists on the file system then the content of the file is
   * overwritten with the given {@link Session} object. Optionally, updates the
   * session count of the {@link UserDAO} of the {@link Session} if the
   * {@link Session} does not already exist on the file system.
   *
   * @param session
   *          The {@link Session} to serialize to the file system
   * @param updateUserCount
   *          Flag indicating if the {@link UserDAO}'s session count should be
   *          checked and updated.
   */
  private void putSessionOnFileSystem(Session session, boolean updateUserCount)
  {
    sessionCacheLock.lock();

    try
    {
      String sessionId = session.getOid();
      String directory = this.getDirectory(sessionId);

      // Create the directory structure and get the session file
      new File(directory).mkdirs();
      File file = new File(directory + sessionId);
      boolean exists = file.exists();

      try
      {
        // Serialize the session to it's file
        ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file));
        stream.writeObject(session);
        stream.close();

        // Do not update the session count until the Session has been
        // serialized in case of errors writing to the file system.
        if (updateUserCount && !exists)
        {
          super.addSession(session);
        }
      }
      catch (FileNotFoundException e)
      {
        String error = "Session [" + sessionId + "] does not exist or has expired.";
        throw new InvalidSessionException(error, e);
      }
      catch (IOException e)
      {
        throw new FileWriteException(file, e);
      }
    }
    finally
    {
      sessionCacheLock.unlock();
    }
  }

  @Override
  protected void cleanUp()
  {
    sessionCacheLock.lock();
    try
    {
      cleanUpDirectory(new File(rootDirectory), System.currentTimeMillis());
    }
    finally
    {
      sessionCacheLock.unlock();
    }
  }

  @Override
  protected void closeSession(String sessionId)
  {
    sessionCacheLock.lock();

    try
    {
      // Do nothing if the sessionId is equal to the public session oid
      if (!sessionId.equals(publicSessionId))
      {
        Session session = this.getSession(sessionId);
        super.decrementUserLoginCount(session);

        // If session is in memory remove from memory
        if (sessions.containsKey(sessionId))
        {
          sessions.remove(sessionId);
          requestCount.remove(sessionId);
        }

        // Delete the file from the cache
        String dir = this.getDirectory(sessionId);
        File file = new File(dir + sessionId);

        try
        {
          FileIO.deleteFile(file);
        }
        catch (IOException e)
        {
          throw new FileReadException(file, e);
        }
      }
    }
    finally
    {
      sessionCacheLock.unlock();
    }
  }

  @Override
  protected boolean containsSession(String sessionId)
  {
    String dir = this.getDirectory(sessionId);
    File file = new File(dir + sessionId);

    return file.exists();
  }

  @Override
  protected boolean full()
  {
    return false;
  }

  @Override
  protected Session makeRoom()
  {
    return null;
  }

  @Override
  protected Session getSession(String sessionId)
  {
    sessionCacheLock.lock();

    try
    {
      if (sessions.containsKey(sessionId))
      {
        return sessions.get(sessionId);
      }

      String dir = this.getDirectory(sessionId);
      File file = new File(dir + sessionId);

      try
      {
        ObjectInputStream stream = new ResolvedObjectInputStream(new FileInputStream(file));
        Session session = (Session) stream.readObject();
        stream.close();

        // Check that the session has not expired
        long currentTime = System.currentTimeMillis();

        if (session.isExpired(currentTime))
        {
          // Session has expired: delete it from the file
          // system and throw an exception
          FileIO.deleteFile(file);

          String error = "Session [" + sessionId + "] does not exist or has expired.";
          throw new InvalidSessionException(error);
        }

        return session;
      }
      catch (FileNotFoundException e)
      {
        String error = "Session [" + sessionId + "] does not exist or has expired.";
        throw new InvalidSessionException(error);
      }
      catch (IOException e)
      {
        throw new FileReadException(file, e);
      }
      catch (ClassNotFoundException e)
      {
        throw new ProgrammingErrorException(e);
      }
    }
    finally
    {
      sessionCacheLock.unlock();
    }
  }

  @Override
  protected String logIn(String username, String password, Locale[] locales)
  {
    Session session = new Session(locales);
    // Heads up: Smethie: why is this supering up to a different method that
    // does not add
    // the session to the session cache. Also, why are you adding it anyway
    // below?
    // See if you can remove this method entirely and just have polymorphism
    // call the super method.
    super.changeLogIn(username, password, session);

    // Update the session on the cache with the user logged in
    if (!sessions.containsKey(session.getOid()))
    {
      this.putSessionOnFileSystem(session, false);
    }

    return session.getOid();
  }

  @Override
  protected void changeLogin(String username, String password, String sessionId)
  {
    sessionCacheLock.lock();

    try
    {
      if (sessionId.equals(publicSessionId))
      {
        String msg = "Users are not permitted to log into the public session [" + sessionId + "]";
        throw new InvalidLoginException(msg);
      }

      Session session = this.getSession(sessionId);

      super.changeLogIn(username, password, session);

      if (!sessions.containsKey(sessionId))
      {
        this.putSessionOnFileSystem(session, false);
      }
    }
    finally
    {
      sessionCacheLock.unlock();
    }
  }

  @Override
  protected void changeLogin(SingleActorDAOIF user, String sessionId)
  {
    sessionCacheLock.lock();

    try
    {
      if (sessionId.equals(publicSessionId))
      {
        String msg = "Users are not permitted to log into the public session [" + sessionId + "]";
        throw new InvalidLoginException(msg);
      }

      Session session = this.getSession(sessionId);

      super.changeLogIn(user, session);

      if (!sessions.containsKey(sessionId))
      {
        this.putSessionOnFileSystem(session, false);
      }
    }
    finally
    {
      sessionCacheLock.unlock();
    }
  }

  @Override
  protected void changeLogIn(SingleActorDAOIF user, Session session)
  {
    sessionCacheLock.lock();

    try
    {
      super.changeLogIn(user, session);

      this.putSessionOnFileSystem(session, false);
    }
    finally
    {
      sessionCacheLock.unlock();
    }
  }

  /**
   * Sets the dimension of an existing {@link Session}.
   * 
   * @param sessionId
   *          The oid of the {@link Session}.
   * @param dimensionKey
   *          key of a {@link MdDimension}.
   */
  @Override
  protected void setDimension(String sessionId, String dimensionKey)
  {
    sessionCacheLock.lock();

    try
    {
      if (sessionId.equals(publicSessionId))
      {
        String msg = "Users are not permitted to change the dimension of the public session [" + sessionId + "]";
        throw new InvalidLoginException(msg);
      }

      Session session = this.getSession(sessionId);
      session.setDimension(dimensionKey);

      if (!sessions.containsKey(sessionId))
      {
        this.putSessionOnFileSystem(session, false);
      }
    }
    finally
    {
      sessionCacheLock.unlock();
    }
  }

  @Override
  protected void renewSession(String sessionId)
  {
    sessionCacheLock.lock();

    try
    {
      Session session = this.getSession(sessionId);
      session.renew();

      if (!sessions.containsKey(sessionId))
      {
        this.putSessionOnFileSystem(session, false);
      }
    }
    finally
    {
      sessionCacheLock.unlock();
    }
  }

  @Override
  protected void clearSessions()
  {
    sessionCacheLock.lock();

    try
    {
      super.clearSessions();

      // Get the public session
      Session publicSession = this.getPublicSession();

      // Delete all of the files in the file cache
      File directory = new File(rootDirectory);
      FileIO.deleteDirectory(directory);

      // Recreate the root directory
      directory.mkdir();

      // Add the public session back into the cache
      this.addSession(publicSession);
    }
    catch (IOException e)
    {
      throw new FileReadException(new File(rootDirectory), e);
    }
    finally
    {
      sessionCacheLock.unlock();
    }
  }

  /**
   * @param sessionId
   *          The session oid
   * @return Fully qualified directory location of a Session with the given
   *         session oid.
   */
  private String getDirectory(String sessionId)
  {

    StringBuilder path = new StringBuilder();
    path.append(rootDirectory);

    for (int i = 0; i < MAX_DPETH; i++)
    {
      path.append(sessionId.charAt(i) + "/");
    }

    return path.toString();
  }

  /**
   * Crawls through a directory and all sub directories removing any serialized
   * {@link Session} which has expired. This method assumes that any file it
   * finds on the file system is either a directory or a serialized
   * {@link Session}. This method performs it crawl through the use of
   * recursion. Do not use this method for a deep directory structure.
   *
   * @param directory
   *          The root directory.
   * @param time
   *          The time to use in expiration checks.
   */
  private void cleanUpDirectory(File directory, long time)
  {
    sessionCacheLock.lock();

    try
    {
      if (directory.isDirectory())
      {
        File[] files = directory.listFiles();

        for (File file : files)
        {
          cleanUpDirectory(file, time);
        }
      }
      else
      {
        // File is not directory: Get the session from the file and check if it
        // has expired. If it has expired removed it from the
        // file system.
        String sessionId = directory.getName();

        try
        {
          this.getSession(sessionId);
        }
        catch (InvalidSessionException e)
        {
          // If an expection is thrown then it means
          // the session file has expired and has been removed.
        }
      }
    }
    finally
    {
      sessionCacheLock.unlock();
    }
  }

  @Override
  protected Session getPublicSession()
  {
    return this.getSession(publicSessionId);
  }

  @Override
  protected Session getSessionForRequest(String sessionId)
  {
    sessionCacheLock.lock();

    try
    {
      if (sessions.containsKey(sessionId))
      {
        // Increment the request count by one
        int count = requestCount.get(sessionId) + 1;
        requestCount.put(sessionId, count);

        // return the session in memory
        return sessions.get(sessionId);
      }

      // Unserialize session from file system
      Session session = this.getSession(sessionId);

      // Put the session into memory
      sessions.put(sessionId, session);

      // Intialize the request count to 1
      requestCount.put(sessionId, new Integer(1));

      return session;
    }
    finally
    {
      sessionCacheLock.unlock();
    }
  }

  @Override
  protected void endOfRequest(String sessionId)
  {
    this.sessionCacheLock.lock();

    try
    {
      Session session = this.getSession(sessionId);

      if (session.closeOnEndOfRequest())
      {
        this.closeSession(sessionId);
      }
      else if (sessions.containsKey(sessionId))
      {
        // Decrement the request count
        int count = requestCount.get(sessionId) - 1;

        if (count < 1)
        {
          requestCount.remove(sessionId);
          sessions.remove(sessionId);

          // Serialize the session back onto the file system
          this.putSessionOnFileSystem(session, false);
        }
        else
        {
          requestCount.put(sessionId, count);
        }
      }
    }
    finally
    {
      this.sessionCacheLock.unlock();
    }
  }

  @Override
  protected void put(String sessionId, Mutable mutable)
  {
    this.sessionCacheLock.lock();

    try
    {
      Session session = this.getSession(sessionId);
      session.put(mutable);

      if (!sessions.containsKey(sessionId))
      {
        this.putSessionOnFileSystem(session, false);
      }
    }
    finally
    {

    }
  }

  /**
   * ObjectInputStream which delegates to LoaderDecorator when loading classes
   * from the JVM.
   *
   * @author Justin Smethie
   */
  class ResolvedObjectInputStream extends ObjectInputStream
  {
    public ResolvedObjectInputStream(InputStream in) throws IOException
    {
      super(in);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.io.ObjectInputStream#resolveClass(java.io.ObjectStreamClass)
     */
    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException
    {
      String name = desc.getName();
      try
      {
        return LoaderDecorator.loadClass(name);
      }
      catch (ClassNotFoundException ex)
      {
        return super.resolveClass(desc);
      }
      catch (RuntimeException ex)
      {
        if (ex instanceof LoaderDecoratorExceptionIF)
        {
          return super.resolveClass(desc);
        }
        else
        {
          throw ex;
        }
      }
    }
  }

  /**
   * @return The number of sessions in memory
   */
  int getMemoryCount()
  {
    return sessions.size();
  }

  @Override
  public Map<String, SessionIF> getAllSessions()
  {
    sessionCacheLock.lock();
    
    try
    {
      Collection<SessionIF> sessions = new FileSessionIterator().getAll();
      
      HashMap<String, SessionIF> map = new HashMap<String, SessionIF>();
      
      for (SessionIF ses : sessions)
      {
        map.put(ses.getOid(), ses);
      }
      
      return map;
    }
    finally
    {
      sessionCacheLock.unlock();
    }
  }

  private class FileSessionIterator
  {
    private Iterator<String> iterator;

    private boolean          initialized;

    private String           sessionId;

    private FileSessionIterator()
    {
      this.initialized = false;
    }

    /**
     * Loads into memory a list of all the possible session ids
     */
    private synchronized void initialize()
    {
      if (!this.initialized)
      {
        Set<String> sessionIds = new TreeSet<String>();
        sessionIds.addAll(sessions.keySet());

        File root = new File(rootDirectory);

        this.loadSetFromDirectory(root, sessionIds, 0);

        // Remove the public user from the list of sessionIds
        sessionIds.remove(FileSessionCache.this.publicSessionId);

        this.iterator = sessionIds.iterator();

        this.initialized = true;
      }
    }

    private void loadSetFromDirectory(File directory, Set<String> sessionIds, int depth)
    {
      if (depth < MAX_DPETH)
      {
        FileFilter filter = (FileFilter) DirectoryFileFilter.DIRECTORY;

        File[] subdirectories = directory.listFiles(filter);

        for (File subdirectory : subdirectories)
        {
          this.loadSetFromDirectory(subdirectory, sessionIds, ( depth + 1 ));
        }
      }
      else
      {
        File[] sessions = directory.listFiles();

        for (File session : sessions)
        {
          if (session.isFile())
          {
            String sessionId = session.getName();

            sessionIds.add(sessionId);
          }
          else
          {
            throw new ProgrammingErrorException("Expecting only session");
          }
        }
      }
    }

    /**
     * @see com.runwaysdk.session.SessionIterator#next()
     */
    public SessionIF next()
    {
      this.initialize();

      this.sessionId = this.iterator.next();

      return FileSessionCache.this.getSession(sessionId);
    }

    /**
     * @see com.runwaysdk.session.SessionIterator#remove()
     */
//    @Override
//    public void remove()
//    {
//      this.initialize();
//
//      this.iterator.remove();
//
//      FileSessionCache.this.closeSession(this.sessionId);
//    }

    /**
     * @see com.runwaysdk.session.SessionIterator#hasNext()
     */
    public boolean hasNext()
    {
      this.initialize();

      return this.iterator.hasNext();
    }

    /**
     * @see com.runwaysdk.session.SessionIterator#close()
     */
    public synchronized void close()
    {
      this.iterator = null;
      this.initialized = false;
    }

    /**
     * @see com.runwaysdk.session.SessionIterator#getAll()
     */
    public Collection<SessionIF> getAll()
    {
      Collection<SessionIF> sesses = new LinkedList<SessionIF>();

      while (this.hasNext())
      {
        SessionIF session = this.next();

        sesses.add(session);
      }
      
      this.close();

      return sesses;
    }
  }
}
