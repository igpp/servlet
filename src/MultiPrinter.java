package igpp.servlet;

//import java.io.*;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.IOException;

import javax.xml.transform.stream.StreamResult;

import javax.servlet.jsp.JspWriter;

/**
 * Implements the JspWriter interface for a PrintStream. 
 * Allows directing the output of a servlet to the display (System.out)
 * Useful in the main() method of an executable class.
 *
 * @author Todd King
 * @version 1.00 2006
 */
public class MultiPrinter extends JspWriter
{
	PrintStream outStream = null;
	PrintWriter outWriter = null;
	
	/**
	 * Command line interface.
	 *
	 * @param args   command-line arguments.
	 **/
	static public void main(String[] args)
	{
		MultiPrinter me = new MultiPrinter();
		System.out.println( me.getClass().getName() );
	}

	/**
	 * Create an instance of a MultiPrinter.
	 **/
	public MultiPrinter()
	{
		super(1024, true);
		setOut(System.out);
	}

	/**
	 * Create an instance of a MultiPrinter configured to output to a {@link PrintStream}.
	 *
	 * @param out  a pre-opened {@link PrintStream} like System.out.
	 **/
	public MultiPrinter(PrintStream out)
	{
		super(1024, true);
		setOut(out);
	}

	/**
	 * Create an instance of a MultiPrinter configured to output to a {@link PrintWriter}.
	 *
	 * @param out  a pre-opened {@link PrintWriter} like "out" in a JSP servlet.
	 **/
	public MultiPrinter(PrintWriter out)
	{
		super(1024, true);
		setOut(out);
	}

	/**
	 * Set the output to match another {@link MultiPrinter}.
	 *
	 * @param out  a pre-configured {@link MultiPrinter}.
	 **/
	public void setOut(MultiPrinter out)
	{
		this.outStream = out.outStream;
		this.outWriter = out.outWriter;
	}

	/**
	 * Set the output to a pre-opened {@link PrintWriter}.
	 *
	 * @param out  a pre-opened {@link PrintWriter} like "out" in a JSP servlet.
	 **/
	public void setOut(PrintWriter out)
	{
		this.outStream = null;
		this.outWriter = out;
	}
	
	/**
	 * Set the output to a pre-opened {@link PrintStream}.
	 *
	 * @param out  a pre-opened {@link PrintStream} like System.out.
	 **/
	public void setOut(PrintStream out)
	{
		this.outStream = out;
		this.outWriter = null;
	}
	
	/**
	 * Get a {@link StreamResult} for the current output stream.
	 *
	 * @return  a {@link StreamResult} prepared with the current output stream. Returns null if no output stream is set.
	 **/
	public StreamResult getStreamResult()
	{
		if(this.outStream != null) return new StreamResult(this.outStream);
		if(this.outWriter != null) return new StreamResult(this.outWriter);
		
		return null;
	}
	
	public int getRemaining()
	{
		return 0;
	}
	
	/**
	 * Close the currently set output method.
	 **/
	public void close()
	{
		if(outStream != null) outStream.close();	
		if(outWriter != null) outWriter.close();	
	}
	
	/**
	 * Flush the currently set output method.
	 **/
	public void flush()
	{	
		if(outStream != null) outStream.flush();
		if(outWriter != null) outWriter.flush();
	}
	
	/**
	 * Clear intermeadiate buffers in the currently set output method.
	 **/
	public void clearBuffer()
	{
	}
	
	/**
	 * Clear the currently set output method.
	 **/
	public void clear()
	{
	}
	
	/** 
	 * Output a new line sequence.
	 **/
	public void newLine()
	{
		if(outStream != null) outStream.println();
		if(outWriter != null) outWriter.println();
	}
	
	/** 
	 * Output a new line sequence.
	 **/
	public void println()
	{
		if(outStream != null) outStream.println();
		if(outWriter != null) outWriter.println();
	}
	
	/** 
	 * Print an Object and then terminate the line with a new line sequence.
	 *
	 * @param o the object to be printed.
	 **/
	public void println(Object o)
	{
		if(outStream != null) outStream.println(o);
		if(outWriter != null) outWriter.println(o);
	}
	
	/** 
	 * Print a String and then terminate the line with a new line secquence.
	 *
	 * @param s the {@link String} to be printed.
	 **/
	public void println(String s)
	{
		if(outStream != null) outStream.println(s);
		if(outWriter != null) outWriter.println(s);
	}
	
	/** 
	 * Print a long and then terminate the line with a new line secquence.
	 *
	 * @param l the long to be printed.
	 **/
	public void println(long l)
	{
		if(outStream != null) outStream.println(l);
		if(outWriter != null) outWriter.println(l);
	}
	
	/** 
	 * Print a float and then terminate the line with a new line secquence.
	 *
	 * @param f the float to be printed.
	 **/
	public void println(float f)
	{
		if(outStream != null) outStream.println(f);
		if(outWriter != null) outWriter.println(f);
	}
	
	/** 
	 * Print a double and then terminate the line with a new line secquence.
	 *
	 * @param d the double to be printed.
	 **/
	public void println(double d)
	{
		if(outStream != null) outStream.println(d);
		if(outWriter != null) outWriter.println(d);
	}
	
	/** 
	 * Print an int and then terminate the line with a new line secquence.
	 *
	 * @param i the int to be printed.
	 **/
	public void println(int i)
	{
		if(outStream != null) outStream.println(i);
		if(outWriter != null) outWriter.println(i);
	}
	
	/** 
	 * Print a character sequence and then terminate the line with a new line secquence.
	 *
	 * @param c the sequence of char to be printed.
	 **/
	public void println(char[] c)
	{
		if(outStream != null) outStream.println(c);
		if(outWriter != null) outWriter.println(c);
	}
	
	/** 
	 * Print a char and then terminate the line with a new line secquence.
	 *
	 * @param c the char to be printed.
	 **/
	public void println(char c)
	{
		if(outStream != null) outStream.println(c);
		if(outWriter != null) outWriter.println(c);
	}
	
	/** 
	 * Print a boolean and then terminate the line with a new line secquence.
	 *
	 * @param b the boolean to be printed.
	 **/
	public void println(boolean b)
	{
		if(outStream != null) outStream.println(b);
		if(outWriter != null) outWriter.println(b);
	}

	/** 
	 * Print an object.
	 *
	 * @param o the {@link Object} to be printed.
	 **/
	public void print(Object o)
	{
		if(outStream != null) outStream.print(o);
		if(outWriter != null) outWriter.print(o);
	}
	
	/** 
	 * Print a String.
	 *
	 * @param s the {@link String} to be printed.
	 **/
	public void print(String s)
	{
		if(outStream != null) outStream.print(s);
		if(outWriter != null) outWriter.print(s);
	}
	
	/** 
	 * Print an long integer.
	 *
	 * @param l the long to be printed.
	 **/
	public void print(long l)
	{
		if(outStream != null) outStream.print(l);
		if(outWriter != null) outWriter.print(l);
	}
	
	/** 
	 * Print a float.
	 *
	 * @param f the float to be printed.
	 **/
	public void print(float f)
	{
		if(outStream != null) outStream.print(f);
		if(outWriter != null) outWriter.print(f);
	}
	
	/** 
	 * Print a double.
	 *
	 * @param d the double to be printed.
	 **/
	public void print(double d)
	{
		if(outStream != null) outStream.print(d);
		if(outWriter != null) outWriter.print(d);
	}
	
	/** 
	 * Print an int.
	 *
	 * @param i the int to be printed.
	 **/
	public void print(int i)
	{
		if(outStream != null) outStream.print(i);
		if(outWriter != null) outWriter.print(i);
	}
	
	/** 
	 * Print an array of char.
	 *
	 * @param c the sequence of char to be printed.
	 **/
	public void print(char[] c)
	{
		if(outStream != null) outStream.print(c);
		if(outWriter != null) outWriter.print(c);
	}
	
	/** 
	 * Print an char.
	 *
	 * @param c the char to be printed.
	 **/
	public void print(char c)
	{
		if(outStream != null) outStream.print(c);
		if(outWriter != null) outWriter.print(c);
	}
	
	/** 
	 * Print a boolean.
	 *
	 * @param b the boolean to be printed.
	 **/
	public void print(boolean b)
	{
		if(outStream != null) outStream.print(b);
		if(outWriter != null) outWriter.print(b);
	}
	
	/** 
	 * Write a {@link String} to the output stream.
	 *
	 * @param s the {@link String} to write.
	 **/
	public void write(String s)
	{
		if(outStream != null) outStream.print(s);
		if(outWriter != null) outWriter.write(s);
	}
	
	/** 
	 * Write len char from the specified char array starting at offset off to this stream.
	 *
	 * @param c the char array.
	 * @param off the offset into the c to start writing.
	 * @param len the number of char to write starting at off.
	 **/
	public void write(char[] c, int off, int len)
	{
		String	buffer = new String(c, off, len);
		if(outStream != null) outStream.print(buffer);
		if(outWriter != null) outWriter.print(buffer);
	}
}