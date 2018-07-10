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
package com.runwaysdk.generation.loader;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.servlet.ServletException;

import org.apache.catalina.Context;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.Loader;
import org.apache.catalina.Wrapper;
import org.apache.catalina.core.StandardWrapper;
import org.apache.catalina.loader.WebappLoader;

public class TomcatLoader implements Loader, Lifecycle
{
  private WebappLoader webappLoader;
  
  /**
   * We use the generic ClassLoader type because LoaderManager is in the webapp and will
   * need to be pulled in through reflection
   */
  private ClassLoader loaderManager;
  private Class<?> decoratorClass;

  private static final String DECORATOR = "com.runwaysdk.generation.loader.LoaderDecorator";
  private static final String MANAGER = "com.runwaysdk.generation.loader.LoaderManager";

  /**
   * Construct a new WebappLoader with no defined parent class loader
   * (so that the actual parent will be the system class loader).
   */
  public TomcatLoader()
  {
      this(null);
  }

  /**
   * Construct a new WebappLoader with the specified class loader
   * to be defined as the parent of the ClassLoader we ultimately create.
   *
   * @param parent The parent class loader
   */
  public TomcatLoader(ClassLoader parent)
  {
      super();
      webappLoader = new WebappLoader(parent);
      webappLoader.setLoaderClass("com.runwaysdk.generation.loader.RunwayWebappClassLoader");
  }

  /**
   * Returns the current RunwayClassLoader, which has the normal WebAppClassLoader as
   * a parent.  Can be null if this is in the stopped state.
   * 
   * @return 
   */
  public ClassLoader getClassLoader()
  {
//    return RunwayClassLoader;
    return webappLoader.getClassLoader();
  }

  /**
   * @throws LifecycleException
   * @see org.apache.catalina.loader.WebappLoader#start()
   */
  public void start() throws LifecycleException
  {
    webappLoader.start();
    
    RunwayWebappClassLoader wacl = (RunwayWebappClassLoader)webappLoader.getClassLoader();
    wacl.setDelegate(true);
    try
    {
      // Create a new LoaderManager
      Class<?> managerClass = wacl.actualLoad(MANAGER);
      loaderManager = (ClassLoader) managerClass.getConstructor(ClassLoader.class).newInstance(wacl);
      managerClass.getMethod("setWebappClassLoader", ClassLoader.class).invoke(loaderManager, wacl);
      
      // Set the deploy path, since this requires knowledge of the application context which only we have (since only we know about the webapp class loader)
//      String deployPath = this.getContext().getCatalinaBase().getAbsolutePath() + File.separatorChar + "webapps" + File.separatorChar + this.getContext().getBaseName();
//      wacl.actualLoad("com.runwaysdk.configuration.CommonsConfigurationResolver").getMethod("setDeployPath", new Class[]{String.class}).invoke(null, deployPath);
      
      // Technically, at this point, I should be able to use loadClass instead of actualLoad.  But maybe not.
      decoratorClass = wacl.actualLoad(DECORATOR);

      // Invoke LoaderDecorator.setParentLoader(webappLoader.getClassLoader());
      decoratorClass.getMethod("setParentLoader", ClassLoader.class).invoke(null, loaderManager);
      // Invoke LoaderDecorator.addListener(this);
      decoratorClass.getMethod("addListener", Object.class).invoke(null, this);
      // Invoke LoaderDecorator.instance();
      decoratorClass.getMethod("instance").invoke(null);
      
      loaderManager.loadClass("com.runwaysdk.constants.CommonProperties").getMethod("getDefaultLocale").invoke(null);
      wacl.setManager(loaderManager);
      wacl.enableLocks();
    }
    catch (Throwable t)
    {
      throw new LifecycleException("start: ", t);
    }
  }

  /**
   * @throws LifecycleException
   * @see org.apache.catalina.loader.WebappLoader#stop()
   */
  public void stop() throws LifecycleException
  {
    try
    {
      // Invoke LoaderDecorator.deleteListener(this);
      if (decoratorClass!=null)
      {
        decoratorClass.getMethod("deleteListener", Object.class).invoke(null, this);
      }
    }
    catch (Throwable t)
    {
      throw new LifecycleException("stop: ", t);
    }
    webappLoader.stop();
    loaderManager = null;
    decoratorClass = null;
  }

  public void onReload() throws ServletException
  {
    Context context = this.getContext();
    Wrapper jspWrapper = (Wrapper)context.findChild("jsp");
    
    // There might not be any jsps if we're just getting started.
    if (jspWrapper==null) return;

    jspWrapper.unload();
    
    // tomcat-catalina 8.0.29
    // This custom hack is required to fix a bug in the StandardWrapper catalina sourcecode. This custom field in their source was not reset back to false
    // after calling unload, so the underlying JspServlet (the instance) did not get initialized properly upon calling load.
    // This bug happens in tomcat 7 and 8.
    // https://bz.apache.org/bugzilla/show_bug.cgi?id=58701
    AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
      public Boolean run() {
        try
        {
          Field field = ((StandardWrapper)jspWrapper).getClass().getDeclaredField("instanceInitialized");
          field.setAccessible(true);
          field.setBoolean(jspWrapper, false);
        }
        catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
        {
          throw new RuntimeException(e);
        }

        return Boolean.TRUE;
      }
    });
    
    
    jspWrapper.load();
  }
  
  /**
   * @param listener
   * @see org.apache.catalina.loader.WebappLoader#addLifecycleListener(org.apache.catalina.LifecycleListener)
   */
  public void addLifecycleListener(LifecycleListener listener)
  {
    webappLoader.addLifecycleListener(listener);
  }

  /**
   * @param listener
   * @see org.apache.catalina.loader.WebappLoader#addPropertyChangeListener(java.beans.PropertyChangeListener)
   */
  public void addPropertyChangeListener(PropertyChangeListener listener)
  {
    webappLoader.addPropertyChangeListener(listener);
  }

  /**
   * @see org.apache.catalina.Loader#backgroundProcess()
   */
  public void backgroundProcess()
  {
    webappLoader.backgroundProcess();
  }

  /**
   * @return
   * @see org.apache.catalina.loader.WebappLoader#findLifecycleListeners()
   */
  public LifecycleListener[] findLifecycleListeners()
  {
    return webappLoader.findLifecycleListeners();
  }

  /**
   * @return
   * @see org.apache.catalina.loader.WebappLoader#getDelegate()
   */
  public boolean getDelegate()
  {
    return webappLoader.getDelegate();
  }

  /**
   * @return
   * @see org.apache.catalina.loader.WebappLoader#get()
   */
  public boolean get()
  {
    return webappLoader.get();
  }

  /**
   * @return
   * @see org.apache.catalina.loader.WebappLoader#modified()
   */
  public boolean modified()
  {
    return webappLoader.modified();
  }

  /**
   * @param listener
   * @see org.apache.catalina.loader.WebappLoader#removeLifecycleListener(org.apache.catalina.LifecycleListener)
   */
  public void removeLifecycleListener(LifecycleListener listener)
  {
    webappLoader.removeLifecycleListener(listener);
  }

  /**
   * @param listener
   * @see org.apache.catalina.loader.WebappLoader#removePropertyChangeListener(java.beans.PropertyChangeListener)
   */
  public void removePropertyChangeListener(PropertyChangeListener listener)
  {
    webappLoader.removePropertyChangeListener(listener);
  }

  /**
   * @param delegate
   * @see org.apache.catalina.loader.WebappLoader#setDelegate(boolean)
   */
  public void setDelegate(boolean delegate)
  {
    webappLoader.setDelegate(delegate);
  }

  /**
   * @param 
   * @see org.apache.catalina.loader.WebappLoader#set(boolean)
   */
  public void set(boolean )
  {
    webappLoader.set();
  }

  @Override
  public void destroy() throws LifecycleException
  {
    webappLoader.destroy();
  }

  @Override
  public LifecycleState getState()
  {
    return webappLoader.getState();
  }

  @Override
  public String getStateName()
  {
    return webappLoader.getStateName();
  }

  @Override
  public void init() throws LifecycleException
  {
    webappLoader.init();
  }

  @Override
  public Context getContext()
  {
    return webappLoader.getContext();
  }

  @Override
  public void setContext(Context arg0)
  {
    webappLoader.setContext(arg0);
  }
}
