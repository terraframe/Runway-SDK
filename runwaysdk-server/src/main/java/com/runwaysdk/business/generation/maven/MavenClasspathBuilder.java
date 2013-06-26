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

package com.runwaysdk.business.generation.maven;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryPolicy;
import org.apache.maven.artifact.repository.MavenArtifactRepository;
import org.apache.maven.artifact.repository.layout.DefaultRepositoryLayout;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.MavenProject;
import org.apache.maven.repository.internal.DefaultServiceLocator;
import org.apache.maven.repository.internal.MavenRepositorySystemSession;
import org.apache.maven.wagon.Wagon;
import org.apache.maven.wagon.providers.http.HttpWagon;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.collection.CollectRequest;
import org.sonatype.aether.connector.file.FileRepositoryConnectorFactory;
import org.sonatype.aether.connector.wagon.WagonProvider;
import org.sonatype.aether.connector.wagon.WagonRepositoryConnectorFactory;
import org.sonatype.aether.graph.Dependency;
import org.sonatype.aether.graph.DependencyFilter;
import org.sonatype.aether.repository.LocalRepository;
import org.sonatype.aether.repository.RemoteRepository;
import org.sonatype.aether.resolution.ArtifactResult;
import org.sonatype.aether.resolution.DependencyRequest;
import org.sonatype.aether.resolution.DependencyResolutionException;
import org.sonatype.aether.spi.connector.RepositoryConnectorFactory;
import org.sonatype.aether.util.artifact.DefaultArtifact;
import org.sonatype.aether.util.artifact.JavaScopes;
import org.sonatype.aether.util.filter.DependencyFilterUtils;

public class MavenClasspathBuilder
{
  public static void main(String[] args) throws Exception
  {
    List<String> classpath = getClasspathFromMavenProject(new File("/users/terraframe/documents/workspace/MavenSandbox/pom.xml"), new File("/users/terraframe/.m2/repository"), true);
    System.out.println("classpath = " + classpath);
  }
  
  public static List<String> getClasspathFromMavenProject(File projectPom, File localRepoFolder, boolean isRunwayEnvironment) throws DependencyResolutionException, IOException, XmlPullParserException
  {
    MavenProject proj = loadProject(projectPom);
    
    proj.setRemoteArtifactRepositories(
        Arrays.asList(
            (ArtifactRepository) new MavenArtifactRepository(
                "maven-central", "http://repo1.maven.org/maven2/", new DefaultRepositoryLayout(),
                new ArtifactRepositoryPolicy(), new ArtifactRepositoryPolicy()
            )
        )
    );
    
    List<String> classpath = new ArrayList<String>();
    
    RepositorySystem system = Booter.newRepositorySystem();
    RepositorySystemSession session = Booter.newRepositorySystemSession(system, localRepoFolder);
    RemoteRepository repo = Booter.newCentralRepository();
    DependencyFilter classpathFlter = DependencyFilterUtils.classpathFilter( JavaScopes.COMPILE );
    
    List<org.apache.maven.model.Dependency> dependencies = proj.getDependencies();
    Iterator<org.apache.maven.model.Dependency> it = dependencies.iterator();
    
    while (it.hasNext()) {
      org.apache.maven.model.Dependency depend = it.next();
      
      Artifact artifact = new DefaultArtifact(depend.getGroupId(), depend.getArtifactId(), depend.getClassifier(), depend.getType(), depend.getVersion());

      CollectRequest collectRequest = new CollectRequest();
      collectRequest.setRoot( new Dependency( artifact, JavaScopes.COMPILE ) );
      collectRequest.addRepository( repo );

      DependencyRequest dependencyRequest = new DependencyRequest( collectRequest, classpathFlter );

      List<ArtifactResult> artifactResults =
          system.resolveDependencies( session, dependencyRequest ).getArtifactResults();

      for ( ArtifactResult artifactResult : artifactResults )
      {
        Artifact art = artifactResult.getArtifact();
        
        if (isRunwayEnvironment && art.getGroupId().equals("com.runwaysdk") && (
              art.getArtifactId().equals("runwaysdk-client") ||
              art.getArtifactId().equals("runwaysdk-common") ||
              art.getArtifactId().equals("runwaysdk-server")
            )) {
          continue;
        }
        
        classpath.add(art.getFile().getAbsolutePath());
      }
    }
    
    return classpath;
  }
  
  public static MavenProject loadProject(File pomFile) throws IOException, XmlPullParserException
  {
      MavenProject ret = null;
      MavenXpp3Reader mavenReader = new MavenXpp3Reader();

      if (pomFile != null && pomFile.exists())
      {
          FileReader reader = null;

          try
              {
              reader = new FileReader(pomFile);
              Model model = mavenReader.read(reader);
              model.setPomFile(pomFile);

              ret = new MavenProject(model);
          }
          finally
          {
            reader.close();
          }
      }

      return ret;
  }
  
  /**
   * A helper to boot the repository system and a repository system session.
   * 
   * http://stackoverflow.com/questions/11799923/programmatically-resolving-maven-dependencies-outside-of-a-plugin-get-reposito/16906463#16906463
   */
  public static class Booter
  {
    public static RepositorySystem newRepositorySystem()
    {
      /*
       * Aether's components implement org.sonatype.aether.spi.locator.Service to ease manual wiring and using the
       * prepopulated DefaultServiceLocator, we only need to register the repository connector factories.
       */
      DefaultServiceLocator locator = new DefaultServiceLocator();
      locator.addService( RepositoryConnectorFactory.class, FileRepositoryConnectorFactory.class );
      locator.addService( RepositoryConnectorFactory.class, WagonRepositoryConnectorFactory.class );
      locator.setServices( WagonProvider.class, new ManualWagonProvider() );

      return locator.getService( RepositorySystem.class );
    }

    public static RepositorySystemSession newRepositorySystemSession( RepositorySystem system, File localRepoDir )
    {
        MavenRepositorySystemSession session = new MavenRepositorySystemSession();

        LocalRepository localRepo = new LocalRepository(localRepoDir);
        session.setLocalRepositoryManager( system.newLocalRepositoryManager( localRepo ) );

        session.setTransferListener( new TransferListener() );
        session.setRepositoryListener( new RepositoryListener() );

        return session;
    }

    public static RemoteRepository newCentralRepository()
    {
        return new RemoteRepository( "central", "default", "http://repo1.maven.org/maven2/" );
    }
  }
  
  /**
   * A simplistic provider for wagon instances when no Plexus-compatible IoC container is used.
   */
  public static class ManualWagonProvider
      implements WagonProvider
  {
      public Wagon lookup( String roleHint )
          throws Exception
      {
          if ( "http".equals( roleHint ) )
          {
              return new HttpWagon();
          }
          return null;
      }

      public void release( Wagon wagon )
      {

      }
  }
}
