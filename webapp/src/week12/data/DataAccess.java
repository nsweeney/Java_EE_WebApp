package week12.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;

import week12.app.Account;
import week12.app.User;
import week12.util.AtmInvalidUserException;

import java.sql.SQLException;

/**
 * Provides the interface to the datastore For this implementation that is a
 * MySql database
 * 
 * @author scottl
 * 
 */
public class DataAccess {
	/**
	 * Default Constructor
	 */
	public DataAccess() {

	}

	/**
	 * Saves an account
	 * 
	 * @param account
	 *            The account to save
	 * @throws AtmDataException
	 *             if there is a save error
	 */
	public void saveAccount(Account account) throws AtmDataException {
		Calendar now = Calendar.getInstance();
		Date updateDate = new java.sql.Date(now.getTime().getTime());

		try {
			m_insertAccountByUserStatment.setLong(1, account.getAccountId());
			m_insertAccountByUserStatment.setLong(2, account.getUserId());
			m_insertAccountByUserStatment.setString(3, account.getName());
			m_insertAccountByUserStatment.setDouble(4, account.getBalance());
			m_insertAccountByUserStatment.setDate(5, updateDate);
			m_insertAccountByUserStatment.executeUpdate();
		} catch (SQLException ex) {
			throw new AtmDataException(ex);
		}
	}

	/**
	 * Get the requested Account
	 * 
	 * @param user
	 *            User reference
	 * @return An Account reference
	 * @throws AtmDataException
	 */
	public List<Account> getUserAccounts(User user) throws AtmDataException {
		List<Account> accounts = new ArrayList<Account>();
		try {
			m_selectAccountByUseridStatement.setLong(1, user.getUserId());
			ResultSet resultSet = m_selectAccountByUseridStatement
					.executeQuery();

			while (resultSet.next()) {
				long id = resultSet.getLong("id");
				String name = resultSet.getString("name");
				double balance = resultSet.getDouble("balance");

				accounts.add(new Account(id, user, name, balance));
			}
		} catch (SQLException ex) {
			throw new AtmDataException(ex);
		}

		return accounts;
	}

	/**
	 * Gets all the account Ids in a list
	 * 
	 * @return List of Accounts Ids
	 * @throws AtmDataException
	 */
	public List<Long> getAllAccountIds() throws AtmDataException{
		List<Long> accountIds = new ArrayList<Long>();
		try {			
			ResultSet resultSet = m_selectAllAccountIdsStatement
					.executeQuery();

			while (resultSet.next()) {
				long id = resultSet.getLong("id");

				accountIds.add(id);				
			}
		} catch (SQLException ex) {
			throw new AtmDataException(ex);
		}

		return accountIds;
	}

	/**
	 * Gets all the accounts in a list
	 * 
	 * @return List of Accounts
	 * @throws AtmDataException
	 */
	public List<Account> getAllAccounts() throws AtmDataException {
		// List<Account> accounts = new ArrayList<Account>();
		throw new RuntimeException("Not implemented");
		// return accounts;
	}
	
	/**
	 * Save a user instance
	 * 
	 * @param user
	 *            The User reference to save
	 * @throws AtmDataException
	 */
	public void saveUser(User user) throws AtmDataException {
		Calendar now = Calendar.getInstance(); // Gets the current date and
												// time.
		Date updateDate = new java.sql.Date(now.getTime().getTime());

		try {
			m_saveUserStatement.setLong(1, user.getUserId());
			m_saveUserStatement.setString(2, user.getFirstName());
			m_saveUserStatement.setString(3, user.getLastName());
			m_saveUserStatement.setInt(4, user.getPin());
			m_saveUserStatement.setDate(5, updateDate);
			m_saveUserStatement.executeUpdate();
		} catch (SQLException ex) {
			throw new AtmDataException(ex);
		}
	}

	/**
	 * Gets a user from the datastore.
	 * 
	 * @param userId
	 *            The user id
	 * @return A valid User reference
	 * @throws AtmDataException
	 * @throws AtmInvalidUserException
	 */
	public User getUser(long userId) throws AtmDataException,
			AtmInvalidUserException {
		User user = null;
		ResultSet resultSet = null;

		try {
			m_selectUserStatement.setLong(1, userId);
			resultSet = m_selectUserStatement.executeQuery();
			if (resultSet.next()) {
				long id = resultSet.getLong("id");
				String first = resultSet.getString("first_name");
				String last = resultSet.getString("last_name");
				int pin = resultSet.getInt("pin");
				user = new User(id, pin, first, last);
			} else {
				throw new AtmInvalidUserException("No user for id "
						+ Long.toString(userId));
			}
		} catch (SQLException ex) {
			// log error
			trace("DataAccess - SQLException " + ex.getMessage());
			throw new AtmDataException(ex);
		} catch (Exception ex) {
			// log error
			trace("DataAccess - Exception " + ex.getMessage());
			throw new AtmDataException(ex);
		}

		return user;
	}

	/**
	 * Delete a user. Used by the test harness. This is a destructive delete.
	 * 
	 * @param user
	 *            The user to delete
	 * @throws AtmDataException
	 */
	public void deleteUser(User user) throws AtmDataException {
		try {
			m_deleteUserStatement.setLong(1, user.getUserId());
			int count = m_deleteUserStatement.executeUpdate();
			if (count == 0) {
				trace("User not deleted " + user.toString());
			}
		} catch (SQLException ex) {
			throw new AtmDataException(ex);
		}
	}

	/**
	 * Get the list of all users.
	 * 
	 * @return Full list of users
	 * @throws AtmDataException
	 */
	public List<User> getUsers() throws AtmDataException {
		List<User> userList = new ArrayList<User>();
		ResultSet resultSet = null;

		try {
			resultSet = m_selectUsersStatement.executeQuery();

			while (resultSet.next()) {
				long userId = resultSet.getLong("id");
				String first = resultSet.getString("first_name");
				String last = resultSet.getString("last_name");
				int pin = resultSet.getInt("pin");

				userList.add(new User(userId, pin, first, last));
			}
		} catch (SQLException ex) {
			// log error
			throw new AtmDataException(ex);
		}

		return userList;
	}

	/**
	 * Disconnects the data access connection
	 */
	public void disconnect() {
		try {
			trace("DataAccess - disconnect");
			m_connect.close();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}

	/**
	 * Establishes the connection to the database. Loads the JDBC driver for
	 * MySQL user and password are hard coded. Not a best practice. Initializes
	 * all the prepared statements.
	 * 
	 * @throws AtmDataException
	 */
	public void connect() throws AtmDataException {
		try {
			trace("DataAccess - connect");
			// this will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");

			// setup the connection with the DB.
			m_connect = DriverManager
					.getConnection("jdbc:mysql://localhost/atm?"
							+ "user=root&password=mainroot");
			//
			// pre-compile prepared statements
			//
			m_saveUserStatement = m_connect.prepareStatement(INSERT_USER_SQL);
			m_selectUserStatement = m_connect.prepareStatement(SELECT_USER_SQL);
			m_selectUsersStatement = m_connect
					.prepareStatement(SELECT_USERS_SQL);
			m_deleteUserStatement = m_connect.prepareStatement(DELETE_USER_SQL);

			m_selectAccountByUseridStatement = m_connect
					.prepareStatement(SELECT_ACCOUNT_BY_USERID_SQL);
			m_selectAllAccountIdsStatement = m_connect
					.prepareStatement(SELECT_ALL_ACCOUNT_IDS_SQL);
			m_insertAccountByUserStatment = m_connect.prepareStatement(INSERT_ACCOUNT_SQL);
		} catch (SQLException ex) {
			// log exception
			System.out.println(ex.getMessage());
			throw new AtmDataException(ex);
		} catch (Exception ex) {
			// log exception
			System.out.println(ex.getMessage());
			throw new AtmDataException(ex);
		}
	}

	/**
	 * Provides access to the connection object.
	 * 
	 * @return The Connection reference.
	 */
	public Connection getConnection() {
		return m_connect;
	}

	private void trace(String msg) {
		System.out.println(msg);
	}

	private Connection m_connect = null;
	// User prepared statements
	private PreparedStatement m_saveUserStatement;
	private PreparedStatement m_selectUsersStatement;
	private PreparedStatement m_selectUserStatement;
	private PreparedStatement m_deleteUserStatement;

	// Account prepared statements
	private PreparedStatement m_selectAccountByUseridStatement;
	private PreparedStatement m_selectAllAccountIdsStatement;
	private PreparedStatement m_insertAccountByUserStatment;

	// User queries
	private String INSERT_USER_SQL = "insert into  atm.user values (?, ?, ?, ?, ?)";
	private String SELECT_USERS_SQL = "SELECT id, first_name, last_name, pin from atm.user";
	private String SELECT_USER_SQL = "SELECT id, first_name, last_name, pin from atm.user where id=?";
	private String DELETE_USER_SQL = "DELETE FROM atm.user where id=?";

	// Account queries
	private String SELECT_ACCOUNT_BY_USERID_SQL = "SELECT id, user_id, name, balance from atm.account where user_id=?";
	private String SELECT_ALL_ACCOUNT_IDS_SQL = "SELECT id from atm.account";
	private String INSERT_ACCOUNT_SQL = "INSERT INTO atm.account VALUES (?,?,?,?,?)";
}
