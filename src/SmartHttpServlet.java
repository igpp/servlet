package igpp.servlet;

import igpp.servlet.MultiPrinter;

import java.io.InputStream;
import java.io.PrintStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.Enumeration;
import java.util.Date;
import java.util.Random;
import java.util.ArrayList;

import java.lang.reflect.Method;
import java.lang.reflect.Field;

import javax.servlet.http.HttpServlet;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletContext;

import org.apache.commons.cli.Option;

/**
 * A utility class that provides methods for self-aware actions.
 * Methods support the calling am set*() methods using keyword/value
 * arguments. Also support "keyword=value" syntax.
 * Additional methods support dumping the value of all arguments and
 * generating XML.
 *
 * @author Todd King
 * @version 1.00 2006
 */
public class SmartHttpServlet extends HttpServlet
{
	static private String mVersion = "1.0.0";
	private String mInstanceVersion = mVersion;
	
	public MultiPrinter	mOut	= new MultiPrinter();
	
	public String	mRemoteHost = "";
	public String	mRemoteUser = "";
	
	private ServletContext	mContext = null;

   /**
    * Command-line interface
    *
    * @param args   command-line arguments.
    **/	
	static public void main(String[] args)
	{
		SmartHttpServlet me = new SmartHttpServlet();
		System.out.println("version: " + me.getVersion());
		System.out.println("class: " + me.getClass().getName());
		System.out.println("servlet: " + me.getServletInfo());
	}
	
	/**
	 * Called by the Servlet framework when servlet initialized.
	 **/
	 public void init()
   	throws ServletException
	 {
	 	super.init();
	 	mContext = getServletContext();
	 }

	/**
	 * Called by the Servlet framework to retrieve servlet information.
	 **/
	 public String getServletInfo()
	 {
	 	return getServletName() + " version " + getVersion();
	 }

	/**
	 * Set a member variable using the syntax "name=value".
	 *
	 * @param value   the value to set specified as "name=value".
	 *
	 * @return <code>true</code> if the syntax of value is correct.
	 **/
    public boolean setMember(String value)
    {
    	String[] part;
    	
    	part = value.split("=", 2);
    	if(part.length != 2) return false;	// No equal sign
    	setMember(part[0], part[1]);
    	
    	return true;
    }
    
   /** 
    * Call the set() method with a given name suffix and a {@link String} array
    * as an argument. Calls the appropriate set method for each value.
    *
    * @param name       the name of the item to set.
    * @param value      the {@link String} array of values to set.
    *
	 * @return <code>true</code>.
    **/
    public boolean setMember(String name, String[] value)
    {
		 for(int i = 0; i < value.length; i++) {
		 	setMember(name, value[i]);
		 }
       return true;
    }

	/** 
	* Call the set() method with a given name suffix and a {@link String}
	* as an argument.
	*
	* @param name       the name of the item to set.
	* @param value      the {@link String} value to set.
	*
   * @return <code>true</code> if the set method was found, otherwise <code>false</code>.
	**/
	public boolean setMember(String name, String value)
	{
		String      member;
		String      methodName = "";
		Object[] passParam = new Object[1];
		Method   method;
		boolean	setFound = true;
		
		try {
			// Signature and parameters for "set" methods
			Class[]  argSig = new Class[1];
			argSig[0] = Class.forName("java.lang.String");
			
			methodName = "set" + igpp.util.Text.toProperCase(name);
			method = getClass().getMethod(methodName, argSig);
			passParam[0] = value;
			method.invoke(this, passParam);
       } catch(Exception e) {
       	setFound = false;
       }
       
      return setFound;
    }

	/** 
	* Call the set() method in the passed object for each passed parameter
	* of the HttpServletRequest.
	*
	* @param obj      the object to modify.
	* @param request	the {@link HttpServletRequest} to search for parameters.
	*
	**/
	public void setMembers(Object obj, HttpServletRequest request)
		throws Exception
	{
		if(obj == null) return;
		if(request == null) return;
		System.out.println("setMembers()");
		for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
			String name = (String) e.nextElement();
			String[] values = request.getParameterValues(name);
			System.out.println(name + ": (" + values.length + ")");
			for(String value : values) {
				igpp.util.Reflection.setMember(obj, name, value);				
			}
     }
	}
	

	/** 
	* Print the value of all member variables.
	**/
	public void dump(Object obj)
		throws IllegalAccessException
	{
		igpp.util.Reflection.dump(obj);
	}
	
	/**
	 * Get a unique string token.
	 *
	 * @return a unique token for this instance.
	 **/
	static public String getCID() { Random random = new Random(new Date().getTime()); return Long.toString(random.nextLong()); }
	
	/** 
	 * Get a quoted URL parameter string if the value is defined (not empty).
	 *
	 * @param name   the name of the parameter.
	 * @param value  the value to assign the parameter.
	 *
	 * @return string containing the quoted name=value expression or blank quoted string if either name or value is blank.
	 **/
	static public String getParameterIfSet(String name, String value)
	{
		if(igpp.util.Text.isEmpty(name)) return "\"\"";
		if(igpp.util.Text.isEmpty(value)) return "\"\"";
		
		return "\"" + name + "=" + value + "\"";
	}	

	/** 
	 * Returns the phrase "checked" if the values match.
	 * Useful in setting states of web form elements.
	 *
	 * @param base the value to compare to.
	 * @param value the value to check.
	 *
	 * @return the string "checked" if base and value are equal, otherwise a blank string.
	 **/
	static public String checked(String base, String value)
	{
		if(igpp.util.Text.isMatch(base, value)) return "checked";
		
		return "";
	}	

	/** 
	 * Returns an empty string if value is null, otherwise the string
	 * value is returned.
	 *
	 * @param value  the {@link String} to check.
	 *
	 * @return a blank string if value is empty or null, otherwise the value.
	 **/
	static public String formNormal(String value)
	{
		if(igpp.util.Text.isEmpty(value)) return "";
		
		return value;
	}	

	/** 
	 * Get an input stream connected to the output of this servlet.
	 *
	 * @return an {@link InputStream} connected to this servlet.
	 **/
	public InputStream getInputStream()
		throws IOException
	{
		PipedOutputStream pipeOut = new PipedOutputStream();
      PipedInputStream pipeIn = new PipedInputStream(pipeOut);
      
      mOut.setOut(new PrintStream(pipeOut));
      
      return pipeIn;
	}	

	/** 
	 * Executes a urlQuery and returns an InputStream of the results.
	 * The names of the parameters to pass along with the request is
	 * defined in an String array. For each name a getXXXX() method
	 * is called (if it exists) and the value sent with the request.
	 *
	 * @param urlQuery   the URL to executed.
	 * @param obj  the {@link Object} to inspect for "get" methods.
	 * @param parameters  array of parameter names to use in the "get" calls and pass with the URL.
	 *
	 * @return a {@link InputStream} to read the response from executation of the URL.
	 **/
   static public InputStream getInputStream(String urlQuery, Object obj, String[] parameters)
   	throws Exception
   {
   	String	delim = "";
   	String	temp;
   	
		URL url = new URL(urlQuery);
		URLConnection connection = url.openConnection();
		connection.setDoOutput(true);
		
		OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
		// Pass parameters
		for(int i = 0; i < parameters.length; i++) {
			String	value = igpp.util.Reflection.getMemberValue(obj, parameters[i]);
			if(igpp.util.Text.isEmpty(value)) continue;
			out.write(delim + parameters[i] + "=" + URLEncoder.encode(value, "UTF-8")); 
			delim = "&";
		}
		out.close();
		
		return connection.getInputStream();
   }
   
	/** 
	 * Executes a urlQuery and returns an InputStream of the results.
	 * Each parameter is passed along with the request as 
	 * defined in an String array. 
	 *
	 * @param urlQuery   the URL to executed.
	 * @param parameters  array of parameters to pass with the URL formatted as "name=value".
	 *
	 * @return a {@link InputStream} to read the response from executation of the URL.
	 **/
   static public InputStream getInputStream(String urlQuery, String[] parameters)
   	throws Exception
   {
   	String	delim = "";
   	String	temp;
   	
		URL url = new URL(urlQuery);
		URLConnection connection = url.openConnection();
		connection.setDoOutput(true);
		
		OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
		// Pass parameters
		for(int i = 0; i < parameters.length; i++) {
			out.write(delim + parameters[i]); 
			delim = "&";
		}
		out.close();
		
		return connection.getInputStream();
   }
   
	/** 
	 * Executes a urlQuery and returns an InputStream of the results.
	 * The query is a reconstruction of the passed request.
	 *
	 * @param request  the {@link HttpServletRequest} of the original request.
	 *
	 * @return a {@link InputStream} to read the response from executation of the URL.
	 **/
   static public InputStream getInputStream(HttpServletRequest request)
   	throws Exception
   {
   	String	delim = "";
   	String	temp;
   	
		URL url = new URL(request.getRequestURL().toString());
		URLConnection connection = url.openConnection();
		connection.setDoOutput(true);
		
		OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
		// Pass parameters
		String query = request.getQueryString();
		
		if(query != null) {
			out.write(query); 
		}
		out.close();
		
		return connection.getInputStream();
   }

	/** 
	 * Executes a urlQuery with parameters. No response is expected.
	 *
	 * @param urlQuery   the URL to executed.
	 * @param parameters  array of parameters to pass with the URL formatted as "name=value".
	 **/
   static public void sendDatagram(String urlQuery, String[] parameters)
   	throws Exception
   {
   	String	delim = "";
   	String	temp;
   	
		URL url = new URL(urlQuery);
		URLConnection connection = url.openConnection();
		connection.setDoOutput(true);
		
		OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
		// Pass parameters
		for(int i = 0; i < parameters.length; i++) {
			out.write(delim + parameters[i]); 
			delim = "&";
		}
		out.close();
		
		String	buffer;
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		while((buffer = reader.readLine()) != null) {
			// System.out.println(buffer);
		}
   }

   /**
    * Set host and user information based on the current request.
    *
	 * @param request  the {@link HttpServletRequest} of the original request.
    **/
	public void getRequestInfo(HttpServletRequest request)
	{
		mRemoteHost = request.getRemoteHost();
		if(mRemoteHost == null) mRemoteHost = "";
		
		mRemoteUser = request.getRemoteUser();
		if(mRemoteUser == null) mRemoteUser = "";
	}
   
   /** 
    * Get a fully qualified URL. 
    *
    * If the path does not include a protocol then the
    * fully qualified URL is constructed from the  
    * scheme, host, port and context of the request.
    * A path that begins with a slash ("/") is considered
    * relative to the servlet context. 
    *
	 * @param request  the {@link HttpServletRequest} of the original request.
	 * @param path     the path portion of the new URL.
	 *
	 * @return  the constructed URL based on information in the request and path.
	 *				If request is null, then the passed path is returned.
    **/
	static public String getQualifiedURL(HttpServletRequest request, String path)
	{
		if(request == null) return path;
		
		String scheme = request.getScheme();
		String serverName = request.getServerName();
		String context = request.getContextPath();
		
		String port = "";
		port = port.format(":%d", request.getServerPort());
		
		try {
			URL temp = new URL(path);
			return path;
		} catch(Exception e) {	// Not a fully qualified URL
		}
		
		String	delim = "";
		if( ! path.startsWith("/")) delim = "/";
		
		return scheme + "://" + serverName + port + context + delim + path;
	}
	
	/** 
	 * Get the real system path to a file.
	 *
	 * Checks the servlet context and uses the passed information
	 * to construct an appropriate real path to the desired file.
	 * If executed as a servlet the path is relative to the
	 * passed "folder" value in the WEB-INF folder of the servlet context.
	 * Otherwise its the passed pathname. If the passed pathname is
	 * absolute the pathname is return unchanged regardless of execution
	 * method.
	 *
	 * @param folder the folder within the servlet context to look.
	 * @param pathname	the pathname of the file.
	 *
	 * @return the pathname to the file appropriate for the context.
	 **/
	public String getRealPath(String folder, String pathname)
	{
		if(mContext != null) {	// Its a servlet
			if(pathname.startsWith("./")) {	// Relative to context
	   		pathname = mContext.getRealPath(pathname.substring(1));
			} else {	// either obsolute or relative to "WEB-INF"
				if(pathname.startsWith("/") || pathname.startsWith(".") || pathname.indexOf(":") != -1) {	// If absolute, relative or URI
					// Do nothing
				} else {	// Look in "folder" directory under "WEB-INF"
		   		pathname = mContext.getRealPath("/WEB-INF/" + folder + "/" + pathname);
				}
			}
		} else {	// Local files system based.
			pathname = igpp.util.Text.concatPath(folder, pathname);
		}
		
		return pathname;
	}

	/** 
	 * Send the capabilities information to the current output stream.
	 *
	 * The generated capabilities has a title and four sections. The four sections
	 * are awareness, overview, parameters, acknowledement.
	 * Information is formatted as HTML with am embedded stylesheet.
	 *
	 * @param title   page title. Also used as the title in the contents.
	 * @param overview	text to use in the overview section. If null no overview section is generated.
	 * @param acknowledge	text to use in the acknowledgment section. If null no ackowledgement section is generated.
	 * @param param	an array of parameter names recognized by this servlet. If null "none" is placed in the parameter section.
	 * @param aware	an array of awareness information. The items in the aware array whichh start with a colon (:) are considered descriptive information. All other
	 *                items are considered individual facets and are indendeted relative to the descriptive text. If null no awareness section is generated.
	 *
	 **/
	public void sendCapabilities(String title, String overview, String acknowledge, String param[], ArrayList<String> aware)
   	throws Exception
	{
		mOut.println("<html>");
		mOut.println("<head>");
		mOut.println("<title>" + title + " capabilities</title>");
		mOut.println("<style>");
     	mOut.println("body {  background-color: #e9f0f5; color: black; font-family: Verdana, Arial, sans-serif; font-size:12px; line-height: 1.2; padding: 10px 3% 10px 3%; }");
      mOut.println("div.title { font-size: 16px; font-weight: bold; }");
      mOut.println("span.version { font-size: 12px; }");
      mOut.println("div.info, div.overview, div.param { background-color: white; border: thin solid #333; padding: 10px 15px 10px 15px; margin-top: 5px; }");
      mOut.println("h1 { font-size: 14px; font-weight: bold; }");
      mOut.println("dd { margin-left: 5ex; }");
      mOut.println("table { border: thin solid #666; margin-top: 5px; margin-bottom: 10px; }");
		mOut.println("thead,tbody { border: thin solid #666; }");
      mOut.println("td, th { margin: 0; padding: 2px 2px 2px 2px; border-top: 1px solid #DDD; border-left: 1px solid #DDD;}");            
		mOut.println("th { font-style: oblique; }");
		mOut.println("</style>");
		mOut.println("</head>");
		
		mOut.println("<body>");
		
		mOut.println("<div class=\"title\">");
		mOut.println(title + " <span class=\"version\">(version: " + getVersion() + ")</span>");
		mOut.println("</div>");
		
		if(aware != null) {
			mOut.println("<h1>Awareness</h1>");
			mOut.println("<div class=\"info\">");
			for(String item : aware) { 	// First line is description
				if(item.startsWith(":")) {
					mOut.println(item.substring(1));
				} else { 
					mOut.println("   <dd>" + item + "</dd>"); 
				}
			}
			
			mOut.println("</div>");
		}
			
		mOut.println("<h1>Overview</h1>");
		
		mOut.println("<div class=\"overview\">");
		mOut.println(getServletInfo());
		if(overview != null) {	// Supplement
			mOut.println("<p>");
			mOut.println(overview);
			mOut.println("</p>");
		}
		mOut.println("</div>");

		mOut.println("<h1>Parameters</h1>");
		mOut.println("<div class=\"param\">");
		mOut.println("The following parameters can be used:");
		
		if(param == null) {
			mOut.println("<dd>none.</dd>");
		} else {
			mOut.println("<table>");
			mOut.println("<thead>");
			mOut.println("<tr><th>Parameter</th><th>Description</th></tr>");
			mOut.println("</thead>");
			
			mOut.println("<tbody>");
			for(String item : param) { sendOption(item); }
			mOut.println("</tbody>");
			
			mOut.println("</table>");
		}
		mOut.println("</div>");
		
		if(acknowledge != null) {
			mOut.println("<div class=\"acknowledge\">");
			mOut.println("Acknowledgements:<br/>");
			mOut.println(acknowledge);
			mOut.println("</div>");
		}
			
		mOut.println("</body>");
		mOut.println("</html>");
	}
	
	/** 
	 * Send the capabilities information to the current output stream.
	 *
	 * Write an segment of HTML that describes the option with the passed name.
	 *
	 * @param opt  the name of the option to retrieve.
	 **/
	public void sendOption(String opt)
	{
		Option option = getAppOption(opt);
		if(option == null) return;
		
		mOut.println("<tr><td valign=\"top\">");
		mOut.println(option.getOpt());
		if(option.hasLongOpt()) {
			mOut.println("<br/>");
			mOut.println(option.getLongOpt());
		}
		mOut.println("</td><td valign=\"top\">");
		mOut.println(option.getDescription());
		mOut.println("</td></tr>");
	}


	/** 
	 * Retrieves an option defined for the application.
	 *
	 * Override this method in derived classes as appropriate.
	 * Expects options to be defined using the Apache command line options
	 * class org.apache.commons.cli.
	 *
	 * @param opt  the name of the option to retrieve.
	 *
	 * @return an {@link Option} associated with the passed option name, otherwise null if an option does not exist.
	 **/
	public Option getAppOption(String opt)
	{
		return null;
	}
	
	/** 
	 * Determines if the class has been initialized as a servlet.
	 *
	 * @return true if the initialized as a servlet, otherwise false.
	 **/
	public Boolean isServlet()
	{
		return (mContext != null);
	}
	
	/** 
	 * Set the version string for implementation instance.
	 *
	 * @param version the version string
	 **/
	public void setVersion(String version)
	{
		mInstanceVersion = version;
	}
	
	/**
	 * Retrieve the version string for the implementation instance.
	 *
	 * @return the current version string.
	 **/
	public String getVersion()
	{
		return mInstanceVersion;
	}
}