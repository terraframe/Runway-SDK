package ${package};

import java.io.IOException;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.runwaysdk.constants.ClientConstants;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.web.WebClientSession;

public class LoginFilter implements Filter
{
  public void init(FilterConfig filterConfig) throws ServletException
  {
  }

  public void destroy()
  {
  }

  /**
   * Simple filter method that assigns each user an anonymous session.
   */
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
      ServletException
  {
    HttpServletRequest httpReq = (HttpServletRequest) req;
    HttpServletResponse httpRes = (HttpServletResponse) res;

    HttpSession session = httpReq.getSession();

    WebClientSession clientSession = (WebClientSession) session
        .getAttribute(ClientConstants.CLIENTSESSION);

    if (clientSession == null)
    {
      // give everyone a public session
      clientSession = WebClientSession.createAnonymousSession(new Locale[] { CommonProperties.getDefaultLocale() });
    }

    // Create a request object for this request
    ClientRequestIF clientRequest = clientSession.getRequest();
    req.setAttribute(ClientConstants.CLIENTREQUEST, clientRequest);

    chain.doFilter(req, res);
  }
}
