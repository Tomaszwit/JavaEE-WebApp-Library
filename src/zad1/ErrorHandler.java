package zad1;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ErrorHandler extends HttpServlet {

	  public void serviceRequest(HttpServletRequest req,
	                             HttpServletResponse resp)
	                             throws ServletException, IOException
	  {
	    String charset = BundleInfo.getCharset();
	    resp.setContentType("text/html; charset=" + charset);
	    Throwable exc = (Throwable)
	                     req.getAttribute("javax.servlet.error.exception");

	    if (exc != null) {
	      PrintWriter out = resp.getWriter();
	      out.println("<h2>" + exc.getMessage() + "</h2><hr>");
	      Throwable cause = exc.getCause();
	      if (cause instanceof SQLException) {
	        SQLException sqlexc = (SQLException) cause;
	        out.println(sqlexc.getMessage() + "<br><br>");
	        out.println("Error code: " + sqlexc.getErrorCode() + "<br>");
	        out.println("SQL state : " + sqlexc.getSQLState() + "<br>");
	      }
	      out.close();
	    }

	  }

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		serviceRequest(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		serviceRequest(request, response);
	}  
}