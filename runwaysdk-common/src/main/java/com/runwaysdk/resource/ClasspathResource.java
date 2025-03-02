/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.FilenameUtils;

import com.runwaysdk.query.ListOIterator;
import com.runwaysdk.query.OIterator;

/**
 * Represents a resource on the Classpath, which may or may not actually exist.
 * 
 * Big thanks to spring.
 * https://github.com/spring-projects/spring-framework/blob/master/spring-core/src/main/java/org/springframework/core/io/ClassPathResource.java
 * 
 * @author Richard Rowlands
 */
public class ClasspathResource implements ApplicationTreeResource
{
  private String path = null;
  
  private ClassLoader classLoader = null;
  
  /**
   * Constructs a ClasspathResource from an absolute path on the classpath.
   */
  public ClasspathResource(String absolutePath)
  {
    this.path = absolutePath;
  }
  
  /**
   * Constructs a ClasspathResource from an absolute path on the classpath. The classloader is optional and may be null.
   */
  public ClasspathResource(String absolutePath, ClassLoader classLoader)
  {
    this.path = absolutePath;
    this.classLoader = classLoader;
  }
  
  /**
   * Constructs a ClasspathResource from a url. The classloader is optional and may be null.
   */
  public ClasspathResource(URL url, ClassLoader classLoader)
  {
    this.path = url.getPath();
    this.classLoader = classLoader;
  }
  
  /**
   * Constructs a ClasspathResource from a url.
   */
  public ClasspathResource(URL url)
  {
    this.path = url.getPath();
  }
  
  /**
   * Returns true if the resource actually exists on the classpath with the given class loader and is resolvable as a URL.
   */
  @Override
  public boolean exists()
  {
    return (resolveURL() != null);
  }
  
  /**
   * Resolves the resource on the classpath and returns a URL. If the resource is not resolvable, null will be returned.
   */
  protected URL resolveURL()
  {
    if (this.classLoader != null)
    {
      return this.classLoader.getResource(this.path);
    }
    else {
//      return ClassLoader.getSystemResource(this.path);
      return Thread.currentThread().getContextClassLoader().getResource(this.path);
    }
  }
  
  /**
   * Resolves the ClasspathResource to a URL and returns it. If the resource does not exist or cannot be resolved a
   * {@link ResourceException} will be thrown.
   */
  public URL getURL()
  {
    URL url = resolveURL();
    
    if (url == null)
    {
      throw new ResourceException(getDescription() + " cannot be resolved to URL because it does not exist");
    }
    
    return url;
  }
  
  /**
   * Opens a stream to the classpath resource and returns it. If one cannot be opened a ResourceException will be thrown.
   * Autocloseable paradigm should be used with the returned stream to ensure resources are not leaked.
   */
  public InputStream openNewStream()
  {
    URL url = this.getURL();
    
    try
    {
      return url.openStream();
    }
    catch (IOException e)
    {
      throw new ResourceException(e);
    }
  }
  
  /**
   * Returns the absolute path of the resource on the classpath.
   */
  public String getAbsolutePath()
  {
    return this.path;
  }
  
  /**
   * Returns the absolute path to the classpath resource minus the name and the extension.
   */
  public String getPackage()
  {
    String path = this.getAbsolutePath();
    String name = path.substring(0, path.lastIndexOf('/'));
    
    return name;
  }
  
  /**
   * Returns the name of the resource on the classpath. If the resource has an extension, it will be included.
   */
  public String getName()
  {
    String path = this.getAbsolutePath();
    String name = path.substring(path.lastIndexOf('/') + 1);
    
    return name;
  }
  
  /**
   * Returns true if the resolved resource is inside a jar. Returns false if it is not resolvable or is on the filesystem.
   */
  public Boolean isInsideJar()
  {
    URL url = this.resolveURL();
    
    if (url == null) { return false; }
    
    return url.getProtocol().equals("jar");
  }
  
  /**
   * Returns true if the resource is a package (directory).
   */
  public Boolean isPackage()
  {
    return this.getNameExtension().equals("");
  }
  
  /**
   * If the resource is contained within a jar, this will return the jar. If the resource exists on the filesystem this will return the file on the filesystem.
   */
  public CloseableFile openNewFile()
  {
    URL url = this.getURL();
    
    if (url.getProtocol().equals("jar"))
    {
      try {
        String jarFileName = URLDecoder.decode(url.getFile(), "UTF-8");
        jarFileName = jarFileName.substring(5, jarFileName.indexOf("!"));
        return new CloseableFile(jarFileName, false);
      }
      catch (UnsupportedEncodingException e)
      {
        throw new ResourceException(e);
      }
    }
    else
    {
      return new CloseableFile(url.getFile(), false);
    }
  }
  
  /**
   * Returns the extension of the resource. If this ClasspathResource represents a resource without an extension
   * (like a package) this method will return an empty string.
   */
  public String getNameExtension()
  {
    return FilenameUtils.getExtension(this.getName());
  }
  
  /**
   * Returns the base name of the resource. For a file, this will return the name of the file minus the extension.
   * For a package, this will return the name of the package. The name will not include the pathing (package).
   */
  public String getBaseName()
  {
    return FilenameUtils.getBaseName(this.getName());
  }
  
  public static List<ClasspathResource> getResourcesInPackage(String packageName)
  {
    try
    {
      ClassLoader classLoader = ClasspathResource.class.getClassLoader();
      Enumeration<URL> packageURLs;
      ArrayList<ClasspathResource> resources = new ArrayList<ClasspathResource>();
  
      packageName = packageName.replace(".", "/");
      packageURLs = classLoader.getResources(packageName);
  
      while (packageURLs.hasMoreElements())
      {
        URL packageURL = packageURLs.nextElement();
  
        if (packageURL.getProtocol().equals("jar"))
        {
          String jarFileName;
          JarFile jf;
          Enumeration<JarEntry> jarEntries;
          String entryName;
          
          // build jar file name, then loop through zipped entries
          jarFileName = URLDecoder.decode(packageURL.getFile(), "UTF-8");
          jarFileName = jarFileName.substring(5, jarFileName.indexOf("!"));
          jf = new JarFile(jarFileName);
          try
          {
            jarEntries = jf.entries();
            while (jarEntries.hasMoreElements())
            {
              JarEntry jarEntry = jarEntries.nextElement();
              
              entryName = jarEntry.getName();
              if (entryName.startsWith(packageName) && entryName.length() > packageName.length() + 5)
              {
//                String name = entryName.substring(entryName.lastIndexOf('/') + 1);
                
                ClasspathResource resource = new ClasspathResource(entryName, classLoader);
                resources.add(resource);
              }
            }
          }
          finally
          {
            jf.close();
          }
        }
        else
        {
          File folder = new File(packageURL.getFile());
          File[] contenuti = folder.listFiles();
          String entryName;
          
          String packagePath = packageName;
          if (packagePath.endsWith("/"))
          {
            packagePath = packagePath.substring(0, packagePath.length()-1);
          }
          
          for (File actual : contenuti)
          {
            entryName = actual.getName();
            
            ClasspathResource resource = new ClasspathResource(packagePath + "/" + entryName, classLoader);
            resources.add(resource);
          }
        }
      }
      
      return resources;
    }
    catch(IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }
  
  /**
   * This is useful in rare scenarios where a single path on the classpath may resolve to more than one URL.
   */
  public static List<URL> getUrlsInPackage(String packageName)
  {
    try
    {
      ClassLoader classLoader = ClasspathResource.class.getClassLoader();
      Enumeration<URL> packageURLs;
      ArrayList<URL> resources = new ArrayList<URL>();
  
      packageName = packageName.replace(".", "/");
      packageURLs = classLoader.getResources(packageName);
  
      while (packageURLs.hasMoreElements())
      {
        URL packageURL = packageURLs.nextElement();
  
        if (packageURL.getProtocol().equals("jar"))
        {
          String jarFileName;
          JarFile jf;
          Enumeration<JarEntry> jarEntries;
          String entryName;
          
          // build jar file name, then loop through zipped entries
          jarFileName = URLDecoder.decode(packageURL.getFile(), "UTF-8");
          jarFileName = jarFileName.substring(5, jarFileName.indexOf("!"));
          jf = new JarFile(jarFileName);
          try
          {
            jarEntries = jf.entries();
            while (jarEntries.hasMoreElements())
            {
              JarEntry jarEntry = jarEntries.nextElement();
              
              entryName = jarEntry.getName();
              if (entryName.startsWith(packageName) && entryName.length() > packageName.length() + 5 && !entryName.endsWith("/"))
              {
                URL resource = new URL("jar:file:" + jarFileName + "!/" + entryName);
                resources.add(resource);
              }
            }
          }
          finally
          {
            jf.close();
          }
        }
        else
        {
          File folder = new File(packageURL.getFile());
          File[] contenuti = folder.listFiles();
          
          String packagePath = packageName;
          if (packagePath.endsWith("/"))
          {
            packagePath = packagePath.substring(0, packagePath.length()-1);
          }
          
          for (File actual : contenuti)
          {
            resources.add(actual.toURI().toURL());
          }
        }
      }
      
      return resources;
    }
    catch(IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }
  
  public static void main(String[] args)
  {
    List<ClasspathResource> list = ClasspathResource.getResourcesInPackage("com/runwaysdk/");
    
    ClasspathResource cpr = list.get(0);
    
    System.out.println(cpr);
  }
  
  public String getDescription()
  {
    return "ClasspathResource [" + this.getAbsolutePath() + "]";
  }
  
  @Override
  public String toString()
  {
    return "classpath:" + this.getAbsolutePath();
  }

  @Override
  public boolean isRemote()
  {
    return false;
  }

  @Override
  public void close()
  {
    
  }

  @Override
  public void delete()
  {
    if (!isInsideJar())
    {
      this.openNewFile().delete();
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public OIterator<ApplicationTreeResource> getChildren()
  {
    return new ListOIterator<ApplicationTreeResource>((List<ApplicationTreeResource>) (Object) getResourcesInPackage(this.path));
  }

  @Override
  public Optional<ApplicationTreeResource> getParent()
  {
    return Optional.of(new ClasspathResource(this.getPackage()));
  }

  @Override
  public Optional<ApplicationTreeResource> getChild(String path)
  {
    var res = new ClasspathResource(this.getAbsolutePath() + "/" + path);
    
    if (res.exists())
      return Optional.of(res);
    else
      return Optional.empty();
  }

  @Override
  public boolean hasChildren()
  {
    return getChildren().hasNext();
  }
  
  /**
   * Applies the given {@code Consumer} function to every child in the entire subtree
   * rooted at this {@code ArchiveFileResource}.
   *
   * @param action the function that will be executed for each child
   */
  @Override
  public void forAllChildren(Consumer<ApplicationTreeResource> action)
  {
    forAllChildrenHelper(this, action);
  }
  
  /**
   * A private helper that recurses through all children.
   */
  private static void forAllChildrenHelper(ApplicationTreeResource resource, Consumer<ApplicationTreeResource> action)
  {
    OIterator<ApplicationTreeResource> children = resource.getChildren();
    
    while (children.hasNext())
    {
      ApplicationTreeResource child = children.next();
      action.accept(child);
      forAllChildrenHelper(child, action);
    }
  }
}
