/**
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class NewProjectAssembler
{
  private File                projectRoot;

  private File                webappRoot;

  private String              projectName;
  
  private String              domain;

  private JarFile             jarFile;

  private static final String STATIC   = "Static/";

  /**
   * @param args
   * @throws URISyntaxException
   * @throws IOException
   */
  public static void main(String[] args) throws IOException, URISyntaxException
  {
    NewProjectAssembler npa;
    if (args.length == 4)
    {
      File workspace = new File(args[1]);
      File deploy = new File(args[3]);
      npa = new NewProjectAssembler(args[0], workspace, args[2], deploy);
    }
    else
    {
      System.out.println("Usage: NewProjectAssembler [projectName] [workspace] [domain] [deployPath]");
      System.out.println("  -projectName: The name of the project to create");
      System.out.println("  -workspace: The path to place the new project in.");
      System.out.println("  -domain: The spatially unique domain name for your project");
      System.out.println("  -deployPath: The path to your web container's root directory");
      return;
    }

    npa.install();
    System.out.println("New Project created at " + npa.projectRoot.getAbsolutePath());
  }

  public NewProjectAssembler(String projectName, File workspace, String domain, File deployRoot) throws IOException, URISyntaxException
  {
    this.projectName = projectName;
    this.domain = domain;
    this.projectRoot = new File(workspace.getAbsoluteFile(), projectName);
    this.projectRoot.mkdirs();
    this.webappRoot = deployRoot;

    URL jarURL = this.getClass().getProtectionDomain().getCodeSource().getLocation();
    this.jarFile = new JarFile(new File(jarURL.toURI()));
  }

  public void install() throws IOException
  {
    copyStaticResources();
    createClasspath();
    createProject();
    createProfiles();
    copyProfileDir();
    copyLaunches();
    copyFilter();
  }

  private void copyFilter() throws IOException
  {
    List<String> xmlLines = readAndReplace(jarFile.getJarEntry("web_application_settings.xml"));
    File xmlFile = new File(projectRoot, "scripts/web_application_settings.xml");
    writeTextFile(xmlLines, xmlFile);
    
    List<String> filterLines = readAndReplace(jarFile.getJarEntry("LoginFilter.java"));
    String filterPath = domain.replace('.', '/');
    File filterFile = new File(projectRoot, "src/client/" + filterPath + "/LoginFilter.java");
    filterFile.getParentFile().mkdirs();
    writeTextFile(filterLines, filterFile);
  }

  private void copyProfileDir() throws IOException
  {
    String prefix = "profiles/";
    File profileRoot = new File(projectRoot, prefix);
    Enumeration<JarEntry> entries = jarFile.entries();
    while (entries.hasMoreElements())
    {
      JarEntry entry = entries.nextElement();
      String name = entry.getName();
      
      // Only copy things from the profiles/ directory
      if (!name.startsWith(prefix))
      {
        continue;
      }
      
      // Trim off the prefix
      name = name.substring(prefix.length());
      // Ignore subdirectories (they get handled separately)
      if (name.contains("/") || name.length()==0)
      {
        continue;
      }
      
      File file = new File(profileRoot, name);
      // master.properties needs to do a special substitution
      if (name.equals("master.properties"))
      {
        List<String> list = new LinkedList<String>();
        for (String line : getLinesFromStream(jarFile.getInputStream(entry)))
        {
          if (line.startsWith("profile.name"))
          {
            list.add("profile.name=deploy");
          }
          else
          {
            list.add(line);
          }
        }
        writeTextFile(list, file);
      }
      // So does version.xsd
      else if (name.equals("version.xsd"))
      {
        List<String> list = new LinkedList<String>();
        for (String line : getLinesFromStream(jarFile.getInputStream(entry)))
        {
          if (line.startsWith("  <xs:include schemaLocation="))
          {
            list.add("<xs:include schemaLocation=\"../profiles/datatype.xsd\"/>");
          }
          else
          {
            list.add(line);
          }
        }
        writeTextFile(list, file);
      }
      else
      {
        writeTextFile(jarFile.getInputStream(entry), file);
      }
    }
  }

  private void copyLaunches() throws IOException
  {
    String prefix = "launches/";
    File launchDir = new File(projectRoot, prefix);
    launchDir.mkdir();
    Enumeration<JarEntry> entries = jarFile.entries();
    while (entries.hasMoreElements())
    {
      JarEntry entry = entries.nextElement();
      String name = entry.getName();
      
      // Only copy things from the launches/ directory
      if (!name.startsWith(prefix) || name.equals(prefix))
      {
        continue;
      }
      
      // Trim off the jar prefix, adding the project name
      name = projectName + " " + name.substring(prefix.length());
      File file = new File(launchDir, name);
      
      // Read the template and sub in the project name
      List<String> lines = readAndReplace(entry);
      writeTextFile(lines, file);
    }
  }

  /**
   * Copies the profiles and changes values as specified
   */
  private void createProfiles() throws IOException
  {
    Map<String, String> map = new TreeMap<String, String>();
    map.put("local.lib", "${local.root}/lib");
    map.put("local.root", projectRoot.getAbsolutePath().replace('\\', '/'));
    map.put("domain", domain);
    map.put("deploy.root", webappRoot.getAbsolutePath().replace('\\', '/'));
    map.put("deploy.appname", projectName);
    map.put("deploy.profile", "deploy");
    map.put("databaseBinDirectory", "C:/Program Files (x86)/PostgreSQL/8.4/bin/");
    map.put("databaseVendor", "com.runwaysdk.constants.PostgreSQL");
    String user = projectName.toLowerCase();
    map.put("user", user);
    map.put("password", user);
    map.put("databaseName", user);
    map.put("compileTimeWeaving", "false");
    map.put("logTransactions", "false");
    writeProfile("default", null, map);
    
    map.clear();
    user = projectName.toLowerCase() + "develop";
    map.put("user", user);
    map.put("password", user);
    map.put("databaseName", user);
    map.put("environment", "develop");
    writeProfile("develop", "default", map);
    
    map.clear();
    user = projectName.toLowerCase() + "deploy";
    map.put("user", user);
    map.put("password", user);
    map.put("databaseName", user);
    map.put("environment", "deploy");
    map.put("local.bin", "${deploy.bin}");
    map.put("local.src", "${deploy.webinf}/source");
    map.put("server.src", "${deploy.webinf}/source/server");
    map.put("server.bin", "${deploy.bin}");
    map.put("server.lib", "${deploy.lib}");
    map.put("client.src", "${deploy.webinf}/source/client");
    map.put("client.bin", "${deploy.bin}");
    map.put("client.lib", "${deploy.lib}");
    map.put("common.src", "${deploy.webinf}/source/common");
    map.put("common.bin", "${deploy.bin}");
    map.put("common.lib", "${deploy.lib}");
    map.put("log.dir", "${deploy.webinf}/logs/");
    map.put("jsp.dir", "${deploy.webinf}");
    map.put("session.cache", "${deploy.webinf}/sessionCache/");
    map.put("web.dir", "${deploy.path}/webDir/");
    writeProfile("deploy", "default", map);
  }
  
  private void writeProfile(String profileName, String parent, Map<String, String> attributes) throws IOException
  {
    File profileRoot = new File(projectRoot, "profiles/" + profileName + "/");
    profileRoot.mkdirs();
    
    Enumeration<JarEntry> entries = jarFile.entries();
    while (entries.hasMoreElements())
    {
      JarEntry entry = entries.nextElement();
      String name = entry.getName();
      String prefix = "profiles/default/";
      if (!name.startsWith(prefix))
        continue;
      
      File file = new File(profileRoot, name.substring(prefix.length()));
      if (entry.isDirectory())
      {
        file.mkdirs();
        continue;
      }
      
      // The substitution rules we're about to observe only apply to .properties files
      if (!name.endsWith(".properties"))
      {
        writeTextFile(jarFile.getInputStream(entry), file);
        continue;
      }
      
      List<String> lines = new LinkedList<String>();
      if (parent!=null)
      {
        lines.add("super=" + parent);
      }
      for (String line : getLinesFromStream(jarFile.getInputStream(entry)))
      {
        if (!line.contains("="))
        {
          lines.add(line);
          continue;
        }
        
        String[] split = line.split("=", 2);
        String key = split[0];
        if (attributes.containsKey(key))
        {
          lines.add(key + "=" + attributes.get(key));
        }
        else if (parent==null || key.equals("import"))
        {
          lines.add(line);
        }
        else
        {
          lines.add("#" + line);
        }
      }
      writeTextFile(lines, file);
    }
  }
  
//  private void writePropertyFile(File file, String parent, Map<String, String> attributes)
//  {
//  }

  /**
   * Creates the .classpath file for an Eclipse project. The list of libs is
   * populated dynamically based on what is in the jar.
   */
  private void createClasspath() throws IOException
  {
    // Get the header
    LinkedList<String> lines = new LinkedList<String>();
    lines.addAll(getLinesFromStream(jarFile.getInputStream(jarFile.getJarEntry("classpath.pre"))));

    // Loop through to populate the list of libs
    Enumeration<JarEntry> entries = jarFile.entries();
    while (entries.hasMoreElements())
    {
      JarEntry entry = entries.nextElement();
      String name = entry.getName();
      String prefix = STATIC + "lib/";
      if (!name.startsWith(prefix) || !name.endsWith(".jar"))
        continue;
      lines.add("    <classpathentry kind=\"lib\" path=\"" + name.substring(STATIC.length()) + "\"/>");
    }

    lines.addAll(getLinesFromStream(jarFile.getInputStream(jarFile.getJarEntry("classpath.post"))));
    writeTextFile(lines, new File(projectRoot, ".classpath"));
  }

  private void createProject() throws IOException
  {
    // Get the template
    JarEntry jarEntry = jarFile.getJarEntry("project.template");
    List<String> newLines = readAndReplace(jarEntry);
    writeTextFile(newLines, new File(projectRoot, ".project"));
  }

  /**
   * Reads the given entry into a list of strings (one line per string), and replaces any placeholders it encounters
   * 
   * @param jarEntry
   * @return
   * @throws IOException
   */
  private List<String> readAndReplace(JarEntry jarEntry) throws IOException
  {
    InputStream inputStream = jarFile.getInputStream(jarEntry);
    List<String> lines = getLinesFromStream(inputStream);
    LinkedList<String> newLines = new LinkedList<String>();
    
    // Loop through to find and change the project name
    for (String l : lines)
    {
      l = l.replace("${projectName}$", projectName);
      l = l.replace("${domain}$", domain);
      newLines.add(l);
    }
    return newLines;
  }
  
  /**
   * Most of the structure and resources don't require any changes. These can be
   * copied straight out of the jar as-is.
   * 
   * @throws IOException
   */
  private void copyStaticResources() throws IOException
  {
    Enumeration<JarEntry> entries = jarFile.entries();
    while (entries.hasMoreElements())
    {
      JarEntry entry = entries.nextElement();
      String name = entry.getName();
      if (!name.startsWith(STATIC))
        continue;

      File file = new File(projectRoot, name.substring(STATIC.length()));
      if (entry.isDirectory())
      {
        file.mkdirs();
        continue;
      }

      if (name.endsWith(".project") || name.endsWith(".classpath") || name.endsWith(".xml") || name.endsWith(".jsp")
          || name.endsWith(".tld") || name.endsWith(".properties"))
      {
        writeTextFile(jarFile.getInputStream(entry), file);
        continue;
      }

      writeBinaryFile(jarFile.getInputStream(entry), file);
    }
  }

  private List<String> getLinesFromStream(InputStream inputStream) throws IOException
  {
    LinkedList<String> list = new LinkedList<String>();
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    while (reader.ready())
    {
      String line = reader.readLine();
      list.add(line);
    }
    reader.close();
    return list;
  }

  private void writeTextFile(InputStream inputStream, File file) throws IOException
  {
    writeTextFile(getLinesFromStream(inputStream), file);
  }

  private void writeTextFile(List<String> lines, File file) throws IOException
  {
    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
    for (String line : lines)
    {
      writer.write(line);
      writer.newLine();
    }
    writer.flush();
    writer.close();
  }

  private void writeBinaryFile(InputStream inputStream, File destination) throws IOException
  {
    BufferedInputStream input = new BufferedInputStream(inputStream);
    byte[] data = new byte[input.available()];
    input.read(data);
    input.close();

    BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(destination));
    output.write(data);
    output.flush();
    output.close();
  }
}
