/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.runwaysdk.generation.loader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Stack;
import java.util.TreeSet;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.objectweb.asm.ClassReader;

import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.util.FileIO;

public class RunwayClassLoader extends URLClassLoader
{
  private static final boolean        DEBUG        = Boolean.getBoolean("runway.loader.debug");

  /**
   * Once loaded, classes are cached for quick retrieval.
   */
  protected HashMap<String, Class<?>> classes;

  /**
   * Directories for this loader to check for .class files in.
   */
  static TreeSet<File>                binDirs;

  /**
   * Lock is used for loading and reloading to prevent race conditions
   */
  // static final ReentrantLock reentrant = new ReentrantLock();

  /**
   * Regex patterns to match fully qualified Array class names
   */
  private static final Pattern        arrayPattern = Pattern.compile("(\\[)*L(.)*;");

  private static final Pattern        arrayPrefix  = Pattern.compile("\\[L(.)*;");

  /**
   * Private constructor prevents instances from being used for multiple loads.
   * Delegates to the specified parent for classes that are not Reloadable.
   * 
   * @param array
   *          The locations of the generatedClassPaths
   * @param parent
   *          The parent loader to delegate to for non Reloadable types
   */
  RunwayClassLoader(URL[] array, ClassLoader parent)
  {
    this(array, parent, LocalProperties.isRunwayEnvironment(), new File(LocalProperties.getClientGenBin()), new File(LocalProperties.getCommonGenBin()), new File(LocalProperties.getServerGenBin()));
  }

  RunwayClassLoader(URL[] array, ClassLoader parent, boolean isRunway, File... bins)
  {
    super(array, parent);

    classes = new HashMap<String, Class<?>>();
    binDirs = new TreeSet<File>();

//    for (URL url : array)
//    {
//      // url.
//    }

    for (File bin : bins)
    {
      if (!bin.exists() && !isRunway)
      {
        if (bin.getParentFile() != null && bin.getParentFile().exists()) {
          bin.mkdir();
        }
        else {
          String errMsg = "The specified client bin directory [" + bin + "] does not exist.  This is usually indicative of a problem with a property file.";
  
          // throw new ConfigurationException(errMsg);
          throw new RuntimeException(errMsg);
        }
      }

      binDirs.add(bin);
    }
  }

  /**
   * actualLoad breaks the standard delegation model for ClassLoaders. It reads
   * the bytes of the requested class, and if the class implements
   * {@link Reloadable}, then it loads the class <b>without</b> delegating up
   * the loader hierarchy. If the target class does not implement
   * {@link Reloadable}, then this well delegate to the parent classloader.
   * 
   * loadClass is synchronized with a {@link ReentrantLock}.
   * 
   * @param name
   *          The fully qualified name of the class
   * @param resolve
   *          If <tt>true</tt> then resolve the class
   * @return The resulting <tt>Class</tt> object
   */
  public Class<?> actualLoad(String name, boolean resolve) throws ClassNotFoundException
  {
    LockHolder.lock(this);
    debug(RunwayClassLoader.class, "Loading " + name);

    // Encompass everything in a try block so we can use finally to ensure that
    // the lock is released
    try
    {
      // First grab a cached copy, if it exists
      Class<?> c = classes.get(name);
      debug(c, "Found " + name + " in the cache");

      // Next, check if the class has already been loaded by the system
      if (c == null)
        c = findLoadedClass(name);
      debug(c, "Found " + name + " through ClassLoader.findLoadedClass()");

      // special case for arrays
      if (arrayPattern.matcher(name).matches())
      {
        c = loadArray(name);
        debug(c, "Found " + name + " as an array");
      }

      if (c == null)
      {
        // Invoke findClass in order to find the class.
        c = findClass(name);
        debug(c, "Found " + name + " with custom findClass");
      }

      // If still not found, delegate to the default loader
      if (c == null && ! ( getParent() instanceof LoaderManager ))
      {
        c = getParent().loadClass(name);
        debug(c, "Found " + name + " by delegating to parent " + c.getClassLoader());
      }

      // At this point, if c is null, we have failed to find the class
      if (c == null)
      {
        throw new ClassNotFoundException(name);
      }

      if (resolve)
        resolveClass(c);

      return c;
    }
    finally
    {
      LockHolder.unlock();
    }
  }

  @Override
  public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException
  {
    LockHolder.lock(this);
    try
    {
      if (getParent() instanceof LoaderManager)
      {
        return getParent().loadClass(name);
      }
      else
      {
        return actualLoad(name, resolve);
      }
    }
    finally
    {
      LockHolder.unlock();
    }
  }

  @Override
  public Class<?> loadClass(String name) throws ClassNotFoundException
  {
    return this.loadClass(name, false);
  }

  /**
   *
   */
  protected Class<?> findClass(final String name) throws ClassNotFoundException
  {
    String mod = name.replace('.', '/') + ".class";
    URL res = this.findResource(mod);
    if (res != null)
    {
      try
      {
        Class<?> clazz = null;

        Byte[] objectArray = FileIO.getBytesFromStream(res.openStream());
        byte[] classBytes = ArrayUtils.toPrimitive(objectArray);
        if (implementsReloadable(classBytes))
        {
          clazz = defineClass(name, classBytes, 0, classBytes.length);
          classes.put(name, clazz);
          return clazz;
        }
      }
      catch (IOException e1)
      {
        throw new ClassNotFoundException("Error reading class " + name);
      }
      // throw new ClassNotFoundException("Unable to find class " + name);
    }
    return null;
  }

  /**
   * @param classBytes
   * @return
   */
  private boolean implementsReloadable(byte[] classBytes)
  {
    String reloadable = Reloadable.class.getName().replace('.', '/');
    ClassReader reader = new ClassReader(classBytes);
    for (String s : reader.getInterfaces())
    {
      if (s.equals(reloadable))
        return true;
    }
    return false;
  }

  /**
   * Loads an array from the base component up. If an array is loaded without
   * its componentType already loaded then an error occurs. Thus it loads from
   * inside out.
   * 
   * @param arrayType
   */
  private Class<?> loadArray(String arrayType)
  {
    // Keep track of what types of array we have (an array of Integers and an
    // array of
    // Business objects, for example)
    Stack<String> baseTypes = new Stack<String>();
    String baseType = arrayType;

    Class<?> arrayClass = null;

    // This loop strips the base type out of any n-dimensional array
    while (arrayPattern.matcher(baseType).matches())
    {
      if (arrayPrefix.matcher(baseType).matches())
      {
        baseType = baseType.replaceFirst("\\[L", "").replace(";", "").trim();
      }
      else
      {
        baseType = baseType.replaceFirst("\\[", "");
      }
      // Add the base type to the stack
      baseTypes.push(baseType);
    }

    // We must load all base types before we can try to load arrays of those
    // types
    while (!baseTypes.isEmpty())
    {
      String type = baseTypes.pop();
      Class<?> componentType;
      componentType = LoaderDecorator.load(type);
      arrayClass = Array.newInstance(componentType, 0).getClass();
    }
    return arrayClass;
  }

  private void debug(Class<?> c, String message)
  {
    if (DEBUG && c != null)
      System.out.println("***" + message);
  }

  /**
   * 
   * @see java.lang.ClassLoader#clearAssertionStatus()
   */
  public void clearAssertionStatus()
  {
    getParent().clearAssertionStatus();
  }

  /**
   * @param name
   * @return
   * @see java.lang.ClassLoader#getResource(java.lang.String)
   */
  public URL getResource(String name)
  {
    return getParent().getResource(name);
  }

  /**
   * @param name
   * @return
   * @see java.lang.ClassLoader#getResourceAsStream(java.lang.String)
   */
  public InputStream getResourceAsStream(String name)
  {
    return getParent().getResourceAsStream(name);
  }

  /**
   * @param name
   * @return
   * @throws IOException
   * @see java.lang.ClassLoader#getResources(java.lang.String)
   */
  public Enumeration<URL> getResources(String name) throws IOException
  {
    return getParent().getResources(name);
  }

  /**
   * @return
   * @see java.lang.Object#hashCode()
   */
  public int hashCode()
  {
    return getParent().hashCode();
  }

  /**
   * @param className
   * @param enabled
   * @see java.lang.ClassLoader#setClassAssertionStatus(java.lang.String,
   *      boolean)
   */
  public void setClassAssertionStatus(String className, boolean enabled)
  {
    getParent().setClassAssertionStatus(className, enabled);
  }

  /**
   * @param enabled
   * @see java.lang.ClassLoader#setDefaultAssertionStatus(boolean)
   */
  public void setDefaultAssertionStatus(boolean enabled)
  {
    getParent().setDefaultAssertionStatus(enabled);
  }

  /**
   * @param packageName
   * @param enabled
   * @see java.lang.ClassLoader#setPackageAssertionStatus(java.lang.String,
   *      boolean)
   */
  public void setPackageAssertionStatus(String packageName, boolean enabled)
  {
    getParent().setPackageAssertionStatus(packageName, enabled);
  }
}