package com.runwaysdk.business.generation.maven;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sonatype.aether.AbstractRepositoryListener;
import org.sonatype.aether.RepositoryEvent;

/**
 * Based on code from:
 * 
 * https://github.com/sonatype/sonatype-aether/blob/master/aether-demo/src/main/java/demo/util/ConsoleRepositoryListener.java
 */
public class RepositoryListener extends AbstractRepositoryListener
{
  private Log log = LogFactory.getLog(RepositoryListener.class);

  public RepositoryListener()
  {

  }

  public void artifactDeployed(RepositoryEvent event)
  {
    log.info("Deployed " + event.getArtifact() + " to " + event.getRepository());
  }

  public void artifactDeploying(RepositoryEvent event)
  {
    log.info("Deploying " + event.getArtifact() + " to " + event.getRepository());
  }

  public void artifactDescriptorInvalid(RepositoryEvent event)
  {
    log.warn("Invalid artifact descriptor for " + event.getArtifact() + ": "
        + event.getException().getMessage());
  }

  public void artifactDescriptorMissing(RepositoryEvent event)
  {
    log.warn("Missing artifact descriptor for " + event.getArtifact());
  }

  public void artifactInstalled(RepositoryEvent event)
  {
    log.info("Installed " + event.getArtifact() + " to " + event.getFile());
  }

  public void artifactInstalling(RepositoryEvent event)
  {
    log.info("Installing " + event.getArtifact() + " to " + event.getFile());
  }

  public void artifactDownloading(RepositoryEvent event)
  {
    log.info("Downloading artifact " + event.getArtifact() + " from " + event.getRepository());
  }

  public void artifactDownloaded(RepositoryEvent event)
  {
    log.info("Downloaded artifact " + event.getArtifact() + " from " + event.getRepository());
  }

  public void artifactResolving(RepositoryEvent event)
  {
    log.trace("Resolving artifact " + event.getArtifact());
  }

  public void artifactResolved(RepositoryEvent event)
  {
    log.trace("Resolved artifact " + event.getArtifact() + " from " + event.getRepository());
  }

  public void metadataDeployed(RepositoryEvent event)
  {
    log.warn("Deployed " + event.getMetadata() + " to " + event.getRepository());
  }

  public void metadataDeploying(RepositoryEvent event)
  {
    log.warn("Deploying " + event.getMetadata() + " to " + event.getRepository());
  }

  public void metadataInstalled(RepositoryEvent event)
  {
    log.trace("Installed " + event.getMetadata() + " to " + event.getFile());
  }

  public void metadataInstalling(RepositoryEvent event)
  {
    log.trace("Installing " + event.getMetadata() + " to " + event.getFile());
  }

  public void metadataInvalid(RepositoryEvent event)
  {
    log.warn("Invalid metadata " + event.getMetadata());
  }

  public void metadataResolved(RepositoryEvent event)
  {
    log.trace("Resolved metadata " + event.getMetadata() + " from " + event.getRepository());
  }

  public void metadataResolving(RepositoryEvent event)
  {
    log.trace("Resolving metadata " + event.getMetadata() + " from " + event.getRepository());
  }
}
