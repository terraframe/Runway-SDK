package ${domain}$;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.runwaysdk.constants.ClientConstants;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.web.ServletUtility;
import com.runwaysdk.web.WebClientSession;

public class LoginFilter implements Filter, Reloadable
{
  private FilterConfig filterConfig;

  public void init(FilterConfig filterConfig) throws ServletException
  {
    this.filterConfig = filterConfig;
  }

  public void destroy()
  {
  }

  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException
  {
    HttpServletRequest httpReq = (HttpServletRequest) req;
    HttpSession session = httpReq.getSession();
    WebClientSession clientSession = (WebClientSession)session.getAttribute(ClientConstants.CLIENTSESSION);
    
    if (clientSession==null)
    {
      Locale[] locales = ServletUtility.getLocales(httpReq);
      clientSession = WebClientSession.createAnonymousSession(locales);
      session.setAttribute(ClientConstants.CLIENTSESSION, clientSession);
    }

    // Create a request object for this request
    ClientRequestIF clientRequest = clientSession.getRequest();
    req.setAttribute(ClientConstants.CLIENTREQUEST, clientRequest);
    chain.doFilter(req, res);
  }
}
