package com.runwaysdk.dataaccess.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.FilenameUtils;

import com.runwaysdk.dataaccess.CoreException;
import com.runwaysdk.dataaccess.InstallerCP;

public class ClasspathResource
{
  /**
   * The name of the classpath resource. This includes the extension.
   */
  public String name;
  
  /**
   * The path of the classpath resource.
   */
  public String cpPackage;
  
  public ClasspathResource(String name, String cpPackage)
  {
    this.name = name;
    this.cpPackage = cpPackage;
  }
  
  public InputStream getStream()
  {
    String resource = this.cpPackage + "/" + this.name;
    
    if (resource.startsWith("/"))
    {
      resource = resource.substring(1);
    }
    
    InputStream is = ClasspathResource.class.getClassLoader().getResourceAsStream(resource);
    
    if (is == null)
    {
      String msg = "Unable to get the stream for resource [" + resource + "].";
      throw new CoreException(msg);
    }
    
    return is;
  }
  
  public String getAbsolutePath()
  {
    return this.cpPackage + "/" + this.name;
  }
  
  public String getPackage()
  {
    return this.cpPackage;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getNameExtension()
  {
    return FilenameUtils.getExtension(this.name);
  }
  
  public String getBaseName()
  {
    return FilenameUtils.getBaseName(this.name);
  }
  
  public static List<ClasspathResource> getResourcesInPackage(String packageName)
  {
    try
    {
      ClassLoader classLoader = InstallerCP.class.getClassLoader();
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
              entryName = jarEntries.nextElement().getName();
              if (entryName.startsWith(packageName) && entryName.length() > packageName.length() + 5)
              {
                String name = entryName.substring(entryName.lastIndexOf('/') + 1);
                
                ClasspathResource resource = new ClasspathResource(name, packageName);
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
          for (File actual : contenuti)
          {
            entryName = actual.getName();
            
            ClasspathResource resource = new ClasspathResource(entryName, packageName);
            resources.add(resource);
          }
        }
      }
      
      return resources;
    }
    catch(IOException ex)
    {
      throw new CoreException(ex);
    }
  }
  
  @Override
  public String toString()
  {
    return this.getAbsolutePath();
  }
}
