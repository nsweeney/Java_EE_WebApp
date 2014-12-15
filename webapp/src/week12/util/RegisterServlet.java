package week12.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import week12.app.User;
import week12.data.DataAccess;

/**
 * Servlet implementation class EnrollServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegisterServlet()
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
		System.out.println("Get");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		System.out.println("Post");
		StringBuffer buffer = new StringBuffer();
		
		// Get the user and password from the Form POST
		String userIdString = request.getParameter("username");
		String pin = request.getParameter("password");
		String userFirstNameString = request.getParameter("firstname");
		String userLastNameString = request.getParameter("lastname");

		buffer.append("Version 13\n");

		AtmSecurity secure = new AtmSecurity();
		try
		{
			buffer.append("Getting user... \n");
			long userId = Long.parseLong(userIdString);
			int pinNumber = Integer.parseInt(pin);
		
			User curUser = null;
			// is user already logged in?
			Object obj = request.getSession().getAttribute("user");

			
			if(obj instanceof User)
			{
				buffer.append("Instance of user: \n");
				// if user object exists and accountid/pin match, use the
				curUser = (User)obj;
				if( curUser.getUserId() != userId ||
					curUser.getPin() != pinNumber)
				{
					
					curUser = secure.login(userId, pinNumber);
					response.sendRedirect("http://localhost:8080/atm_webapp2/user-accounts.jsp");
				}
			}
			else
			{	
				if (pin.length() < 4)
				{
					//System.out.println("invalid pin for user: \n");
					throw new NumberFormatException();
				}
				else
				{

					// create new user
					curUser = new User (userId, pinNumber, userFirstNameString, userLastNameString);
	
					// add user to db
					DataAccess da = new DataAccess();
					  da.connect();
					  da.saveUser(curUser);
					  da.disconnect();
				//}				
				// login the user
				curUser = secure.login(userId, pinNumber);
				
				// Assign the user to the session
				// This will make it available to other requests
				request.getSession().setAttribute("user", curUser);	
				
				response.sendRedirect("http://localhost:8080/atm_webapp2/user-accounts.jsp");
				}
			}			
		}
		catch(NumberFormatException ex)
		{
			// redirect to an error page
			// Invalid account id or pin
			response.sendRedirect("http://localhost:8080/atm_webapp2/invalid_id_pin.html");
			String msg = "Error - NumberFormatException: " + getExceptionTrace(ex);
			response.getWriter().println(msg);
			trace(msg);
		}
		catch(AtmInvalidUserException ex)
		{
			// redirect to invalid user error page
			response.sendRedirect("http://localhost:8080/atm_webapp2/invalid_user.html");
			
			String msg = "Error - AtmInvalidUserException: " + getExceptionTrace(ex);
			response.getWriter().println(msg);
			trace(msg);
		}
		catch(Exception ex)
		{
			// redirect to invalid user error page
			response.sendRedirect("http://localhost:8080/atm_webapp2/invalid_user.html");
			
			String msg = "Error - Exception: " + getExceptionTrace(ex);
			response.getWriter().println(msg);
			trace(msg);
		}

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
