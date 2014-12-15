package week12.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import week12.data.DataAccess;
import week12.app.Account;
import week12.app.SystemIdGenerator;
import week12.app.User;

/**
 * Servlet implementation class HelloServlet
 */
@WebServlet("/NewAccountServlet")
public class NewAccountServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NewAccountServlet()
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

		List<Long> accountsIds = new ArrayList<Long>();

		// Get the userId, account name, and balance from the Form POST
		String userIdString = request.getParameter("userId");
		String accountNameString = request.getParameter("accountname");
		String accountBalanceString = request.getParameter("accountbalance");

		buffer.append("Version 10\n");

		try
		{
			DataAccess da = new DataAccess();
			da.connect();

			long userId = Long.parseLong(userIdString);
			double accountBalance = Double.parseDouble(accountBalanceString);

			accountsIds = da.getAllAccountIds();
			long accId = SystemIdGenerator.getInstance().getNextId();

			for(Long a : accountsIds)
			{
				if(a.equals(accId))
				{
					accId = SystemIdGenerator.getInstance().getNextId();
				}
			}

			User accountUser = da.getUser(userId);

			Account addAccount = new Account(accId, accountUser,
					accountNameString, accountBalance);

			da.saveAccount(addAccount);
			da.disconnect();
			
			// redirect to the account page
			// this assumes success
			response.sendRedirect("http://localhost:8080/atm_webapp2/user-accounts.jsp");


		}
		catch(NumberFormatException ex)
		{
			// redirect to an error page
			// Invalid account id or pin
			response.sendRedirect("http://localhost:8080/atm_webapp2/invalid_id_pin.html");
			String msg = "Error - NumberFormatException: "
					+ getExceptionTrace(ex);
			response.getWriter().println(msg);
			trace(msg);
		}
		catch(AtmInvalidUserException ex)
		{
			// redirect to invalid user error page
			response.sendRedirect("http://localhost:8080/atm_webapp2/invalid_user.html");
			String msg = "Error - AtmInvalidUserException: "
					+ getExceptionTrace(ex);
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
