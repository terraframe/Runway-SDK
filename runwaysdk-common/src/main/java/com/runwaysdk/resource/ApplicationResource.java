package com.runwaysdk.resource;

import java.io.File;
import java.io.InputStream;

/**
 * Interface denoting an abstract "file", which may in practice exist on the classpath, in the vault, as a file on the filesystem, or potentially even as a remote resource (like on S3)
 * 
 * @author rrowlands
 */
public interface ApplicationResource extends AutoCloseable
{
  /**
   * Opens a new stream to access the underlying resource. The returned resource should be cleaned up after you are done
   * using it by invoking 'close' on it.
   */
  public InputStream openNewStream();
  
  /**
   * Opens a new connection to the resource as a file. Depending on the implementation, a new temporary file may be created
   * to fulfill the request, which may involve expensive copying of files. If this is not what you want, use 'getUnderlyingFile'
   * instead, which throws an exception if it cannot invoke performantly. For this reason you should always invoke "close" on the
   * ApplicationResource when you are done using the file returned by this method.
   */
  public CloseableFile openNewFile();
  
  /**
   * If the underlying implementation supports direct file access (vault, or file) a direct reference to the underlying
   * file will be returned. If this is not supported an UnsupportedOperationException will be thrown. In the case of vault
   * files a file will be returned, however the filename will be a randomized string with no extension. This method promises
   * to do no file copying or otherwise expensive operations, unlike 'openNewFile'.
   */
  public File getUnderlyingFile();
  
  public String getName();
  
  public String getBaseName();
  
  public String getNameExtension();
  
  public boolean isRemote();
  
  /**
   * Closes the underlying resource, freeing any OS resources. Depending on the underlying implementation of this resource,
   * some methods on this interface may no longer function properly after invoking this method.
   */
  public void close();
  
  public void delete();
}
