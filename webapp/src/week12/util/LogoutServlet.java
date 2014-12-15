package week12.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LogoutServlet()
	{
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		response.sendRedirect("http://localhost:8080/atm_webapp2/logout.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		response.sendRedirect("http://localhost:8080/atm_webapp2/logout.jsp");

	}

	private String getExceptionTrace(Exception ex)
	{
		StringBuffer msg = new StringBuffer();
		java.io.StringWriter writer = new java.io.StringWriter();
		java.io.PrintWriter pWriter = new java.io.PrintWriter(writer);
		ex.printStackTrace(pWriter);

		return msg.toString();
	}

	private void trace(String msg)
	{
		System.out.println(msg);
	}
}
