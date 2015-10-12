package com.runwaysdk.system.metadata;

import java.io.File;
import java.net.URL;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.dbdeploy.DbDeploy;

public class MetadataPatcher implements Runnable
{
  private static final String POSTGRESQL = "postgresql";

  private String              userid;

  private String              password;

  private String              url;

  private String              dbms;

  /**
   * @return the userid
   */
  public String getUserid()
  {
    return userid;
  }

  /**
   * @param userid
   *          the userid to set
   */
  public void setUserid(String userid)
  {
    this.userid = userid;
  }

  /**
   * @return the password
   */
  public String getPassword()
  {
    return password;
  }

  /**
   * @param password
   *          the password to set
   */
  public void setPassword(String password)
  {
    this.password = password;
  }

  /**
   * @return the url
   */
  public String getUrl()
  {
    return url;
  }

  /**
   * @param url
   *          the url to set
   */
  public void setUrl(String url)
  {
    this.url = url;
  }

  /**
   * @return the dbms
   */
  public String getDbms()
  {
    return dbms;
  }

  /**
   * @param dbms
   *          the dbms to set
   */
  public void setDbms(String dbms)
  {
    this.dbms = dbms;
  }

  private String getScriptLocation()
  {
    if (dbms.equals(POSTGRESQL))
    {
      return "/scripts/postgres";
    }

    throw new UnknownDBMSException("Unknown DBMS [" + this.dbms + "]");
  }

  private String getDriver()
  {
    if (dbms.equals(POSTGRESQL))
    {
      return "org.postgresql.Driver";
    }

    throw new UnknownDBMSException("Unknown DBMS [" + this.dbms + "]");
  }

  @Override
  public void run()
  {
    String location = this.getScriptLocation();
    String driver = this.getDriver();

    try
    {
      URL resource = this.getClass().getResource(location);
      File scripts = new File(resource.toURI());

      DbDeploy deploy = new DbDeploy();
      deploy.setDbms(this.dbms);
      deploy.setDriver(driver);
      deploy.setUserid(this.userid);
      deploy.setPassword(this.password);
      deploy.setUrl(this.url);
      deploy.setScriptdirectory(scripts);

      deploy.go();
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("static-access")
  public static void main(String[] args)
  {
    Options options = new Options();
    options.addOption(OptionBuilder.withArgName("vendor").hasArg().withDescription("Database vendor [" + POSTGRESQL + "]").isRequired().create("d"));
    options.addOption(OptionBuilder.withArgName("username").hasArg().withDescription("Database username").isRequired().create("u"));
    options.addOption(OptionBuilder.withArgName("password").hasArg().withDescription("Database password").isRequired().create("p"));
    options.addOption(OptionBuilder.withArgName("url").hasArg().withDescription("Database URL").isRequired().create("l"));

    CommandLineParser parser = new BasicParser();

    try
    {
      CommandLine cmd = parser.parse(options, args);

      MetadataPatcher patcher = new MetadataPatcher();
      patcher.setDbms(cmd.getOptionValue("d"));
      patcher.setUserid(cmd.getOptionValue("u"));
      patcher.setPassword(cmd.getOptionValue("p"));
      patcher.setUrl(cmd.getOptionValue("l"));
      patcher.run();
    }
    catch (ParseException exp)
    {
      System.err.println("Parsing failed.  Reason: " + exp.getMessage());
    }
    catch (RuntimeException exp)
    {
      System.err.println("Patching failed. Reason: " + exp.getMessage());
    }
  }
}
