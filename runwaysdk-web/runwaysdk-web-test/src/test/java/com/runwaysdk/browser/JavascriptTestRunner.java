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
package com.runwaysdk.browser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


import junit.framework.TestSuite;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;

/**
 * This code runs tests defined in Javascript using the TestFramework provided in TestFramework.js on multiple browsers.
 * This code should be run as a JUnit test and requires a selenium server running. At the moment, this code is loosely compatible
 * with JUnit4, although restrictions apply. If using JUnit4, test names must be unique across all suites or else result data
 * will be clobbered for the tests using the same names.
 * 
 * @author Richard Rowlands
 */

public class JavascriptTestRunner extends SeleneseTestCase
{
    private static int MAXIMUM_TOTAL_TEST_DURATION = 600; // in seconds
    private static String supportedBrowsers[] = { "*googlechrome" }; // todo: IE7, IE8, IE9, Opera 10, Opera 9, Safari 3, Safari 2, Chrome
    
    private static Selenium selenium;
    private static boolean isSeleniumStarted = false;
    
    public static Test suite() throws Exception
    {
      // Read project.version
      Properties prop1 = new Properties();
      ClassLoader loader1 = Thread.currentThread().getContextClassLoader();           
      InputStream stream1 = loader1.getResourceAsStream("avail-maven.properties");
      prop1.load(stream1);
      String projVer = prop1.getProperty("mvn.project.version");
      
      TestSuite suite = new TestSuite();
      
      int browserLoopIterationNumber = 0;
      
      System.out.println("Preparing to run cross-browser javascript unit tests.");
      long totalTime = System.currentTimeMillis();
      
      for (String browser : supportedBrowsers)
      {
        try
        {
          String browserDisplayName = String.valueOf(browser.charAt(1)).toUpperCase() + browser.substring(2);
          System.out.println("Opening " + browserDisplayName);
          
          TestSuite browserSuite = new TestSuite(browserDisplayName);
          
          selenium = new DefaultSelenium("localhost", 4444, browser, "http://localhost:8080/runwaysdk-browser-test-" + projVer + "/");
          selenium.start();
          isSeleniumStarted = true;
          selenium.open("MasterTestLauncher.jsp");
          
//          selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('all');", "6000");
          
          selenium.setTimeout("1000");
          
          System.out.println("Running tests...");
          long time = System.currentTimeMillis();
          
          selenium.click("all");
          selenium.waitForCondition("!selenium.browserbot.getCurrentWindow().com.runwaysdk.test.TestFramework.getY().Test.Runner.isRunning()", Integer.toString(MAXIMUM_TOTAL_TEST_DURATION*1000));
          
          time = System.currentTimeMillis()-time; // elapsed time in milis
          if (time < 1000)
          {
            System.out.println("Tests completed in " + time + " miliseconds.");
          }
          else if (time < 60000)
          {
            time = time/1000;
            System.out.println("Tests completed in " + time + " seconds.");
          }
          else if (time < 3600000)
          {
            time = time/(1000*60);
            System.out.println("Tests completed in " + time + " minutes.");
          }
          else
          {
            time = time/(1000*60*60);
            System.out.println("Tests completed in " + time + " hours.");
          }
          
          //System.out.println(selenium.getEval("\n\n" + "selenium.browserbot.getCurrentWindow().com.runwaysdk.test.TestFramework.getY().Test.Runner.getResults(selenium.browserbot.getCurrentWindow().com.runwaysdk.test.TestFramework.getY().Test.Format.XML);") + "\n\n");
          
          // tests are done running, get the results and display them through junit
          
          DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
          DocumentBuilder db = dbf.newDocumentBuilder();
          
          String resultsJunitXML = selenium.getEval("selenium.browserbot.getCurrentWindow().com.runwaysdk.test.TestFramework.getY().Test.Runner.getResults(selenium.browserbot.getCurrentWindow().com.runwaysdk.test.TestFramework.getY().Test.Format.JUnitXML);");
          String resultsYUITestXML = selenium.getEval("selenium.browserbot.getCurrentWindow().com.runwaysdk.test.TestFramework.getY().Test.Runner.getResults(selenium.browserbot.getCurrentWindow().com.runwaysdk.test.TestFramework.getY().Test.Format.XML);");
          
          
          // Write the test output to xml
          Properties prop = new Properties();
          ClassLoader loader = Thread.currentThread().getContextClassLoader();           
          InputStream stream = loader.getResourceAsStream("default/common/terraframe.properties");
          prop.load(stream);
          String basedir = prop.getProperty("local.root");
          
          
          System.out.println("Writing javascript test results to '" + basedir + "/target/surefire-reports/TEST-com.runwaysdk.browser.JavascriptTestRunner-" + browserDisplayName + ".xml.");
          File dir = new File(basedir + "/target/surefire-reports");
          dir.mkdirs();
          final OutputStream os = new FileOutputStream(dir.getAbsolutePath() + "/TEST-com.runwaysdk.browser.JavascriptTestRunner-" + browserDisplayName + ".xml", false);
          final PrintStream printStream = new PrintStream(os);
          printStream.print(resultsJunitXML);
          printStream.close();
          
          
          InputSource in = new InputSource();
          in.setCharacterStream(new StringReader(resultsYUITestXML));
          Element doc = db.parse(in).getDocumentElement();
          
          NodeList suiteList = doc.getElementsByTagName("testsuite");
          
          if (suiteList == null || suiteList.getLength() == 0)
          {
            //suiteList = (NodeList)doc;
            throw new Exception("Unable to find any suites!");
          }
          
          String uniqueWhitespace = "";
          for (int j = 0; j < browserLoopIterationNumber; j++)
          {
            uniqueWhitespace = uniqueWhitespace + " ";
          }
          
          for (int i = 0; i < suiteList.getLength(); i++) //looping through test suites
          {
            Node n = suiteList.item(i);
            TestSuite s = new TestSuite();
            NamedNodeMap nAttrMap = n.getAttributes();
            
            s.setName(nAttrMap.getNamedItem("name").getNodeValue() + uniqueWhitespace);
            
            NodeList testCaseList = ((Element)n).getElementsByTagName("testcase");
            for (int j = 0; j < testCaseList.getLength(); j++) // looping through test cases
            {
              Node x = testCaseList.item(j);
              NamedNodeMap xAttrMap = x.getAttributes();

              TestSuite testCaseSuite = new TestSuite();
              testCaseSuite.setName(xAttrMap.getNamedItem("name").getNodeValue() + uniqueWhitespace);
              
              NodeList testList = ((Element)x).getElementsByTagName("test");
              for (int k = 0; k < testList.getLength(); k++) // looping through tests
              {
                Node testNode = testList.item(k);
                NamedNodeMap testAttrMap = testNode.getAttributes();
                
                Test t = new GeneratedTest( testAttrMap.getNamedItem("name").getNodeValue() + uniqueWhitespace );
                
                if (testAttrMap.getNamedItem("result").getNodeValue().equals("fail"))
                {
                  ((GeneratedTest)t).testFailMessage = testAttrMap.getNamedItem("message").getNodeValue();
                }
                
                testCaseSuite.addTest(t);
              }
              
              s.addTest(testCaseSuite);
            }
            
            browserSuite.addTest(s);
          }
          
          //suite.addTest(browserSuite);
          browserLoopIterationNumber++;
        } // end try
        catch (Exception e)
        {
          throw(e);
        }
        finally
        {
          if (isSeleniumStarted)
          {
            selenium.stop();
            isSeleniumStarted = false;
          }
        }
      } // end for loop on browsers
      
      totalTime = System.currentTimeMillis()-totalTime; // elapsed time in milis
      if (totalTime < 1000)
      {
        System.out.println("Cross-browser javascript unit tests completed in " + totalTime + " miliseconds.");
      }
      else if (totalTime < 60000)
      {
        totalTime = totalTime/1000;
        System.out.println("Cross-browser javascript unit tests completed in " + totalTime + " seconds.");
      }
      else if (totalTime < 3600000)
      {
        totalTime = totalTime/(1000*60);
        System.out.println("Cross-browser javascript unit tests completed in " + totalTime + " minutes.");
      }
      else
      {
        totalTime = totalTime/(1000*60*60);
        System.out.println("Cross-browser javascript unit tests completed in " + totalTime + " hours.");
      }

      return suite;
    }
  
    public void setUp() throws Exception // this overwrite is required, otherwise it opens browsers for no reason after the tests are done
    {
    }
    /*
    private static String getUniqueTestName(String suite, String name, boolean isRecursiveCall)
    {
      if (!ALLOW_TEST_NAME_DUPLICATES)
        return name;
      
      if (testNames.contains(name))
        return getUniqueTestName(suite, name + " ", true);
      
      testNames.add(name);
      
      if (isRecursiveCall)
        knownDuplicateToUniqueMap.put(suite + ":" + name.trim(), name);
      
      return name;
    }
    private static String getUniqueTestName(String suite, String name)
    {
      return getUniqueTestName(suite, name, false);
    }
    
    private static String getUniqueTestNameFromMap(String suite, String name)
    {
      String val = knownDuplicateToUniqueMap.get(suite + ":" + name);
      if (val != null && val != "")
        return val;
      
      return name;
    }
    */
}
