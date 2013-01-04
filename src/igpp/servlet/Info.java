/**
 * 
 */
package igpp.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author tking
 *
 */
public class Info extends HttpServlet {
	   /**
	    * Process a HTTP post request.
	    *
	    * Called as part of the servlet framework when a HTTP post event occurs.
	    *
	    * @param request	the {@link HttpServletRequest} with request information.
	    * @param response	the {@link HttpServletResponse} to stream output.
	    **/
		public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
		{
			try {
		    	doAction(request, response);
			} catch(IOException i) {
				throw i;
			} catch(Exception e) {
				throw new ServletException(e);
			}
		}
	   
	   /**
	    * Process a HTTP get request.
	    *
	    * Called as part of the servlet framework when a HTTP get event occurs.
	    *
	    * @param request	the {@link HttpServletRequest} with request information.
	    * @param response	the {@link HttpServletResponse} to stream output.
	    **/
		public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
		{
			try {
				doAction(request, response);
			} catch(IOException i) {
				throw i;
			} catch(Exception e) {
				e.printStackTrace();
				throw new ServletException(e);
			}
		}

	   /**
	    * Process a HTTP request.
	    *
	    * Unifies the handling of HTTP get and post events.
	    * Processes passed parameters and performs the appropriate tasks.
	    *
	    * @param request	the {@link HttpServletRequest} with request information.
	    * @param response	the {@link HttpServletResponse} to stream output.
	    **/
		public void doAction(HttpServletRequest request, HttpServletResponse response)
			throws Exception
		{
			ServletOutputStream out = response.getOutputStream();
			
			out.println("getProtocol: " + request.getProtocol() + "<br>");
			out.println("getQueryString: " + request.getQueryString() + "<br>");
			out.println("getPathInfo: " + request.getPathInfo() + "<br>");
			out.println("<br>");
			out.println("getServerName: " + request.getServerName() + "<br>");
			out.println("getServerPort: " + request.getServerPort() + "<br>");
			out.println("getServletPath: " + request.getServletPath() + "<br>");
			out.println("<br>");
			out.println("getRequestURI: " + request.getRequestURI() + "<br>");
			out.println("getRequestURL: " + request.getRequestURL() + "<br>");
			out.println("<br>");
			out.println("getRemoteAddr: " + request.getRemoteAddr() + "<br>");
			out.println("getRemoteHost: " + request.getRemoteHost() + "<br>");
			out.println("getRemoteUser: " + request.getRemoteUser() + "<br>");
			
	    	// Remove extension
			String text = request.getRequestURL().toString();
	    	int n = text.lastIndexOf('.');
	    	if(n != -1) text = text.substring(0, n);
			out.println("Without extension: " + text + "<br>");
	    }
}
