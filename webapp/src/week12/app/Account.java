package week12.app;

import week12.core.AtmObject;

/**
 * Represents a user account.
 * 
 * @author scottl
 *
 */
public class Account extends AtmObject
{
	/**
	 * Default constructor
	 */
	public Account()
	{
		this(-1, new User(), "Default Account", 0.0);
	}
	
	/**
	 * Parameterized constructors
	 * @param id account id
	 * @param user User reference
	 * @param name account name
	 * @param balance Account balance
	 */
	public Account(long id, User user, String name, double balance)
	{
		m_accountId = id;
		m_user = user;
		m_accountName = name;
		m_balance = balance;
	}
	
	/**
	 * @return the m_userId
	 */
	public long getAccountId()
	{
		return m_accountId;
	}
	
	public User getUser()
	{
		return m_user;
	}
	
	/**
	 * @return the m_userId
	 */
	public long getUserId()
	{
		return m_user.getUserId();
	}
	
	/**
	 * @param user User reference
	 */
	public void setUser(User user)
	{
		this.m_user = user;
	}
		
	/**
	 * @return the m_firstName
	 */
	public String getName()
	{
		return m_accountName;
	}

	/**
	 * @param name Set the account name
	 */
	public void setName(String name)
	{
		this.m_accountName = name;
	}
	
	

	public double getBalance()
	{
		return m_balance;
	}

	/**
	 * Compares two Account instances.
	 * Returns true if the contents are equal, otherwise false
	 */
	@Override
	public boolean equals(Object obj)
	{
		boolean result = true;
		
		if( obj == null )
		{
			result = false;
		}
		else if( getClass() != obj.getClass())
		{
			result = false;
		}
		else
		{
			// valid User object, check the contents
			final Account otherUser = (Account) obj;
			if( !this.m_accountName.equals(otherUser.m_accountName) ||
				!this.m_user.equals(otherUser.m_user) ||
				!(this.m_balance == otherUser.m_balance) ||
				!(this.m_accountId == otherUser.m_accountId))
			{
				result = false;
			}
		}
				
		return result;
	}
	
	/**
	 * Formatted output of the account information
	 */
	@Override
	public String toString()
	{
		String fmt = String.format("ID: %d, %s %f", this.m_accountId, this.m_accountName, this.m_balance);
		return fmt;
	}
	
	private long m_accountId;
	private User m_user;
	private String m_accountName;
	private double m_balance;
}
