package com.runwaysdk.ontology;

import java.util.zip.GZIPOutputStream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.runwaysdk.business.ontology.TermDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.controller.ErrorUtility;
import com.runwaysdk.system.ontology.TermUtilDTO;
import com.runwaysdk.system.ontology.io.TermFileFormatDTO;

public class TermControllerUtil
{
  public static void writeExport(HttpServletRequest req, HttpServletResponse resp, String downloadToken, ClientRequestIF clientRequest, String parentTermId, TermFileFormatDTO format)  throws java.io.IOException {
    GZIPOutputStream out = new GZIPOutputStream(resp.getOutputStream());
    
    try {
      // The reason we're including a cookie here is because the browser does not give us any indication of when our response from the server is successful and its downloading the file.
      // This "hack" sends a downloadToken to the client, which the client then checks for the existence of every so often. When the cookie exists, it knows its downloading it.
      // http://stackoverflow.com/questions/1106377/detect-when-browser-receives-file-download
      Cookie cookie = new Cookie("downloadToken", downloadToken);
      cookie.setMaxAge(10*60); // 10 minute cookie expiration
      resp.addCookie(cookie);
      
      TermDTO parentTerm = (TermDTO) clientRequest.get(parentTermId);
      String parentTermDisplay = parentTerm.getDisplayLabel().getValue();
      String mdDisplay = parentTerm.getMd().getDisplayLabel();
      
      String version = TermUtilDTO.getTimestamp(clientRequest);
      
      String filename = mdDisplay + "-" + parentTermDisplay + "(" + version + ").xml.gz";
      
      resp.addHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");
      resp.setContentType("application/gzip");
      TermUtilDTO.exportTerm(clientRequest, out, parentTermId, true, format);
    }
    catch (RuntimeException e) {
      if (!resp.isCommitted()) {
        resp.reset();
      }
      ErrorUtility.prepareThrowable(e, req, out, resp, false, true);
    }
    finally {
      out.flush();
      out.close();
    }
  }
}
