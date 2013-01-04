package igpp.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Forces a referenced resource to be sent as an attachment in the
 * HTTP response. The attachment is tagged with a set MIME type.
 * If no MIME type is specified in the servlet "InitParam" the default
 * is "application/octet-stream". This servlet can be used to override
 * any embedded handling of specified extension. 
 *  
 * @author Todd King
 *
 */
public class Download extends HttpServlet {

	static private String mVersion = "1.0.0";
	private String mMime = "application/octet-stream";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Version: " + mVersion);
	}

    public void init() 
    		throws ServletException 
    {
        String value = getInitParameter("MimeType");
        if(value != null) mMime = value;	// If set, otherwise use default
    }

	public void destroy() {
	}

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
	                
	public void doAction(HttpServletRequest request, HttpServletResponse response)
		throws Exception
	{
	   // Stream a file as an attachement
	   String path = request.getRequestURI();
	   String name = igpp.util.File.getName(path);

	   System.out.println("stream: " + name + ";(" + mMime + ")");
	   igpp.web.DownloadStream.toHttpServletResponse(response, name, getServletContext().getRealPath(path), mMime);
	}
}
