/**
 * A servlet to apply an XML transform (XSL) on source retrieved from
 * a URL and send the results to HttpServletResponse. 
 * The XSL files are located in the directory given by XSLPath 
 * parameter in the servlet configuration.
 *
 * @author Todd King
 * @version 1.00 2006
 */
package igpp.servlet; 

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.IOException;

import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Transforms any stream referenced by a URL using an
 * XML Style Sheet.
 * <p>
 * Parameters:
 *   xsl: The name of the XSL file to apply. This is 
 *        the file name relative to the XSLPath.
 *   url: The URL to excecute and transform the results.
 * <p>
 * Initialization parameters <init-param>
 *    XSLPath: Path to the XSL files. The passed value of "xsl"
 */
public class Transform extends HttpServlet 
{ 
	private static final long serialVersionUID = 1L;
	String mXSLPath = ".";
	
   public void init()
   	throws ServletException
   {
   	String value = getServletConfig().getInitParameter("XSLPath");
   	if(value != null) setXSLPath(value);
   }
   
	
	public void destroy() {
	}

    public void doPost (HttpServletRequest request,
                       HttpServletResponse response)
   			throws ServletException, IOException
    {
    	doGet(request, response);
    }
    
	public void doGet (HttpServletRequest request, HttpServletResponse response)
	                throws ServletException, IOException 
	{

		String message = "";
		String fileURL = request.getParameter("url");
		String stylesheet = request.getParameter("xsl");
		
	   System.out.println("url: " + fileURL);
	   System.out.println("xsl: " + stylesheet);
		
		PrintWriter out = response.getWriter();
		
		if(fileURL == null) {	// No File parameter
		   message = "Unable able to locate resource.";
		} else {
			try {
		      URL url  = new URL(makeURL(request, fileURL));
		      
	         InputStream inStream = url.openStream();
	         
	         System.out.println("fileURL: " + fileURL);
	         System.out.println("  encoded: " + igpp.util.Encode.urlEncode(fileURL));
	         System.out.println("XSL File: " + mXSLPath + File.separator + stylesheet + ".xsl");
	         
	         igpp.xml.Transform.perform(inStream, mXSLPath + File.separator + stylesheet + ".xsl", out);
	         
	         inStream.close();
			} catch(Exception e) {
				message = "Unable access: " + e.getMessage();
			}
		}
			
		if(message.length() > 0) out.println(message);
	}
   
   /**
    * Create a fully qualified URL.
    * Add information from the servlet request information as needed
    * to create a fully qualified URL.
   **/
	public String makeURL(HttpServletRequest request, String path) 
	{
		URL url = null;
      URL requestURL = null;
		String	fileURL = path;
      
      try {
	      requestURL = new URL(request.getRequestURL().toString());
      } catch(Exception e) {
      	return null;
      }
					
		try {
			url  = new URL(fileURL);
		} catch(Exception e) {
			if( ! fileURL.startsWith("/")) fileURL = "/" + fileURL;
			fileURL = requestURL.getProtocol() + "://" + requestURL.getHost() + fileURL;
		}
		
		if(url == null) {
			try {
				url  = new URL(fileURL);
			} catch(Exception e) {
			}
		}
		
		return fileURL;
	}
	
	public String getServletInfo() {
	  return "The Transform servlet transforms a URL stream using a style sheet.";
	}
	
	private void setXSLPath(String value) { mXSLPath = value; }
}

