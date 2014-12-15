package week12.util;

import week12.app.User;
import week12.data.AtmDataException;
import week12.data.DataAccess;

/**
 * Provides the security APIs for the application
 */
public class AtmSecurity
{
	/**
	 * Manages ATM security capabilities
	 */
	public AtmSecurity()
	{
		
	}
	
	/**
	 * Login a user
	 * User provides their ATM card which contains their user info
	 * (first name, last name, account number).
	 * User enters pin.
	 * User must be a valid user for that account AND pin must be valid.
	 * 
	 * @param userId User Id to login with 
	 * @param pin Unique PIN tied to user
	 * @return true if valid user
	 */
	public User login(long userId, int pin)
		throws AtmInvalidUserException
	{	
		trace("login");
		
		// create a LoginRequest
		LoginRequest login = new LoginRequest(userId, pin);		
		trace("LoginRequest");
		
		User user = authenticate(login);
		
		return user;
	}
	
	/**
	 * Performs the authentication
	 * @param request LoginRequest reference
	 * @return A user if authentication is successful
	 * @throws AtmInvalidUserException
	 */
	private User authenticate(LoginRequest request)
		throws AtmInvalidUserException
	{
		trace("authenticate");
		
		User user= null;
		// local authentication
		DataAccess da = new DataAccess();

		try
		{
			da.connect();
			user = da.getUser(request.getUserId());
			
			if(user == null || user.getPin() != request.getPin())
			{
				throw new AtmInvalidUserException("Pin mismatch");
			}
		}
		catch(AtmDataException ex)
		{
			trace("DataAccess AtmDataException: " + ex.getMessage());
			throw new AtmInvalidUserException(ex);
		}
		finally
		{
			da.disconnect();
		}
		
		return user;
	}
	
	private void trace(String msg)
	{
		System.out.println(msg);
	}
}
