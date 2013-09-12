package igpp.servlet;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintWriter;

import igpp.util.FileComparator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author tking
 *
 */
public class ImageBrowse extends HttpServlet {
	private static final long serialVersionUID = 1L;

	static private String mVersion = "1.0.0";
	
	String mJQueryFile = "jquery.js";
	String mMainJSFile = "main.js";
	String mMainCSSFile = "main.css";
	String mOwner = null;
	String mGroup = null;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Version: " + mVersion);
	}

    public void init() 
    		throws ServletException 
    {
        String value = getInitParameter("JQuery");
        if(value != null) mJQueryFile = value;	// If set, otherwise use default
        
        value = getInitParameter("Main.js");
        if(value != null) mMainJSFile = value;	// If set, otherwise use default
        
        value = getInitParameter("Main.css");
        if(value != null) mMainCSSFile = value;	// If set, otherwise use default
        
        value = getInitParameter("Owner");
        if(value != null) mOwner = value;	// If set, otherwise use default
        
        value = getInitParameter("Group");
        if(value != null) mGroup = value;	// If set, otherwise use default
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
	                
	@SuppressWarnings("unchecked")
	public void doAction(HttpServletRequest request, HttpServletResponse response)
		throws Exception
	{
		PrintWriter out = response.getWriter();
		
		igpp.xml.XMLGrep config = new igpp.xml.XMLGrep();
		config.open(getServletContext().getRealPath("pageinfo.xml"));
		String pagetitle = config.getFirstValue("/page/pagetitle", "");
		String info = config.getFirstValue("/page/info", "");
		
		out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
		out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
		out.println("<head>");
		out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
		out.println("<title>" + pagetitle + "</title>");
		out.println("<script src=\"" + mJQueryFile + "\" type=\"text/javascript\"></script>");
		out.println("<script src=\"" + mMainJSFile + "\" type=\"text/javascript\"></script>");
		out.println("<link rel=\"stylesheet\" href=\"" + mMainCSSFile + "\" type=\"text/css\" />");
		out.println("</head>");

		out.println("<body>");
		out.println(info);
		
		String realPath = getServletContext().getRealPath(request.getRequestURI());
		
		File path = new File(realPath);
		
		// Find presentations and documents
	   File[] list = path.listFiles(new FileFilter()   
	            { 
	              public boolean accept(File pathname) { 
	                     if(pathname.getName().endsWith(".ppt")) return true; 
	                     if(pathname.getName().endsWith(".doc")) return true; 
	                     if(pathname.getName().endsWith(".pdf")) return true; 
	                     return false;
	                  }
	            } 
	   );
	   if(list.length > 0) {
	      java.util.Arrays.sort(list, new FileComparator());
	      out.println("<h3>Presentations and Documents</h3>");
		   for(File item : list) {
		      out.println("<dd><a class=\"normal\" href=\"" + item.getName() + "\">" + item.getName() + "</a></dd>");
		   }
	   }
	   
	   // Find pictures
	   list = path.listFiles(new FileFilter()   
	            { 
	              public boolean accept(File pathname) { 
	                     if(pathname.getName().endsWith("-s.jpg")) return false; 
	                     if(pathname.getName().endsWith("-m.jpg")) return false; 
	                     if(pathname.getName().endsWith(".jpg")) return true; 
	                     if(pathname.getName().endsWith(".JPG")) return true; 
	                     else return false;
	                  }
	            } 
	   );
	  
	   if(list.length > 0) {
	      java.util.Arrays.sort(list, new FileComparator());
	      out.println("<h3>Images</h3>");
	      out.println("Roll over an image to preview and click to view full resolution image.");
	      out.println("<ul>");
	      out.println("<ul>");

		   for(File item : list) {
		      String fullRes = item.getAbsolutePath();
		      if(fullRes.endsWith(".JPG")) {	// Convert to lower case
	           fullRes = fullRes.replace(".JPG", ".jpg");
	           item.renameTo(new File(fullRes));
	        }

		      File	full = new File(fullRes);
		      String thumbRes = fullRes.replace(".jpg", "-s.jpg");
		      File thumb = new File(thumbRes);
		      if( ! thumb.exists()) { // Generate a thumbnail and browse image
		         igpp.image.Prep.prepFile(fullRes);
			     thumbRes = fullRes.replace(".jpg", "-s.jpg");	// Small thumbnail
			     igpp.util.File.setOwner(thumbRes, mOwner, mGroup);
			     thumbRes = fullRes.replace(".jpg", "-m.jpg");	// Medium thumbnail
			     igpp.util.File.setOwner(thumbRes, mOwner, mGroup);			     
		      }
		      out.println("<li><a href=\"" + full.getName() + "\" class=\"preview\"><img src=\"" + 
		         thumb.getName() + "\" alt=\"" + full.getName() + "\" /></a></li>");
		   }
		   out.println("</ul>");
	   }
	   out.println("</body>");
	   out.println("</html>");
	}

}
