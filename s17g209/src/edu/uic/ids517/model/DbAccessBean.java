package edu.uic.ids517.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.sql.*;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;

public class DbAccessBean {

	private Connection connection;
	private DatabaseMetaData databaseMetaData;
	private Statement statement;
	private ResultSetMetaData resultSetMetaData;
	private List<String> columnNamesSelected;

	public List<String> getColumnNamesSelected() {
		return columnNamesSelected;
	}

	public void setColumnNamesSelected(List<String> columnNamesSelected) {
		this.columnNamesSelected = columnNamesSelected;
	}

	private FacesContext context;
	private String jdbcDriver;
	private String url;
	private DbInformationBean dbInformationBean;
	private static final String MY_SQL = "MySQL";
	private static final String DB2 = "DB2";
	private static final String ORACLE = "Oracle";
	private String message;
	private static final String ACCESS_DENIED = "28000";
	private static final String INVALID_DB_SCHEMA = "42000";
	private static final String TIMEOUT = "08S01";
	private ResultSet resultSet;
	private static final String[] TABLE_TYPES = { "TABLE", "VIEW" };
	private int numOfCols = 0;
	private int numOfRows = 0;
	private Result result;
	private int changedRows;
	private Timestamp loginTime;
	HttpSession session;
	String sessionId;
	HttpServletRequest request;
	String ipAddress;

	public int getChangedRows() {
		return changedRows;
	}

	public void setChangedRows(int changedRows) {
		this.changedRows = changedRows;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	private String errorMessage;

	public DbAccessBean() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void init() {
		context = FacesContext.getCurrentInstance();
		Map<String, Object> m = context.getExternalContext().getSessionMap();
		dbInformationBean = (DbInformationBean) m.get("dbInformationBean");
		session = (HttpSession) context.getExternalContext().getSession(false);
		sessionId = session.getId();
		request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		ipAddress = request.getHeader("X-FORWARDED-FOR");

	}

	/* Executes Query List */
	public List<String> executequeryList(String query) {

		List<String> queryList = new ArrayList<String>();
		try {
			resultSet = statement.executeQuery(query);
			if (resultSet != null) {
				resultSetMetaData = resultSet.getMetaData();
				int numOfCols = resultSetMetaData.getColumnCount();
				resultSet.last();
				numOfRows = resultSet.getRow();
				resultSet.beforeFirst();
				int j = 0;
				while (resultSet.next()) {
					// String[] output = new String[numOfCols];
					String out = new String();
					for (int i = 0; i < numOfCols; i++) {

						out += resultSet.getString(i + 1) + " ";

					}
					queryList.add(out);
					j++;
				}

			}
		} catch (SQLException e) {
			System.err.println("SQLState: " + ((SQLException) e).getSQLState());
			System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
			System.err.println("Message: " + e.getMessage());
			close();
		}
		return queryList;
	}

	/* Login Method */
	public String connectToDb() {
		String dbms = dbInformationBean.getDbms();
		message = "";
		switch (dbms) {
		case MY_SQL:
			jdbcDriver = "com.mysql.jdbc.Driver";
			url = "jdbc:mysql://" + dbInformationBean.getDbmsHost() + ":" + dbInformationBean.getDbmsPort() + "/"
					+ dbInformationBean.getDbmsSchema() + "?&useSSL=false";
			break;
		case DB2:
			jdbcDriver = "com.ibm.db2.jcc.DB2Driver";
			url = "jdbc:db2://" + dbInformationBean.getDbmsHost() + ":" + dbInformationBean.getDbmsPort() + "/"
					+ dbInformationBean.getDbmsSchema() + "?&useSSL=false";
			break;
		case ORACLE:
			jdbcDriver = "oracle.jdbc.driver.OracleDriver";
			url = "jdbc:oracle:thin:@" + dbInformationBean.getDbmsHost() + ":" + dbInformationBean.getDbmsPort() + "/"
					+ dbInformationBean.getDbmsSchema() + "?&useSSL=false";
			break;
		}
		try {
			Class.forName(jdbcDriver);
			connection = DriverManager.getConnection(url, dbInformationBean.getUserName(),
					dbInformationBean.getPassword());
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			databaseMetaData = connection.getMetaData();
			loginTime = new java.sql.Timestamp(System.currentTimeMillis());
			retriveAccessLog("CREATE_LOG_TABLE");
			transactionLog("CREATE_TRANSACTION_TABLE");
			return "SUCCESS";

		} catch (ClassNotFoundException ce) {
			// TODO: handle exception
			message = "Database: " + dbInformationBean.getDbms() + " not supported.";
			return "FAIL";
		} catch (SQLException se) {
			if (se.getSQLState().equals(ACCESS_DENIED)) {
				message = "Credentials are invalid! Please use the right credentials to login";
			} else if (se.getSQLState().equals(INVALID_DB_SCHEMA)) {
				message = "Database Schema is invalid ! Please use the right schema to login";
			} else if (se.getSQLState().equals(TIMEOUT)) {
				message = " Please check host and port!";
			} else {
				message = "SQL Exception occurred!\n" + "Error Code: " + se.getErrorCode() + "\n" + "SQL State: "
						+ se.getSQLState() + "\n" + "Message :" + se.getMessage() + "\n\n";
			}
			return "FAIL";
		} catch (Exception e) {
			message = "Exception occurred: " + e.getMessage();
			return "FAIL";
		}
	}

	public Timestamp getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Timestamp loginTime) {
		this.loginTime = loginTime;
	}

	/* Retrieve Access Logs */
	public boolean retriveAccessLog(String type) {
		try {
			Timestamp t = new java.sql.Timestamp(System.currentTimeMillis());
			if (type.equalsIgnoreCase("CREATE_LOG_TABLE")) {
				String sqlQuery = "Create table if not exists s17g209_Accesslog (LogID INT(6)"
						+ " NOT NULL AUTO_INCREMENT , Username char(50) not null, "
						+ "dbms char(50) ,DateTime char(50), " + "Activity char(50),"
						+ "IPAddress char(50), SessionID char(50), PRIMARY KEY (LogID)) "
						+ "ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;";

				if (ipAddress == null)
					ipAddress = request.getRemoteAddr();

				execute(sqlQuery);
				String insertQuery = "Insert into s17g209_Accesslog (Username,dbms,"
						+ "DateTime,Activity,IPAddress,SessionID)" + " values ( '" + dbInformationBean.getUserName()
						+ "','" + dbInformationBean.getDbms() + "','" + t.toString() + "','" + "Login" + "','"
						+ ipAddress + "','" + sessionId + "')";
				execute(insertQuery);

				return true;
			} else if (type.equalsIgnoreCase("INSERTLOG")) {
				if (ipAddress == null)
					ipAddress = request.getRemoteAddr();
				String insertQuery = "Insert into s17g209_Accesslog (Username,dbms,"
						+ "DateTime,Activity,IPAddress,SessionID)" + " values ( '" + dbInformationBean.getUserName()
						+ "','" + dbInformationBean.getDbms() + "','" + t.toString() + "','" + "Login" + "','"
						+ ipAddress + "','" + sessionId + "')";

				execute(insertQuery);
				return true;
			} else if (type.equalsIgnoreCase("LOGOUT")) {
				if (ipAddress == null)
					ipAddress = request.getRemoteAddr();
				String insertQuery = "Insert into s17g209_Accesslog (Username,dbms,"
						+ "DateTime,Activity,IPAddress,SessionID)" + " values ( '" + dbInformationBean.getUserName()
						+ "','" + dbInformationBean.getDbms() + "','" + t.toString() + "','" + "Logout" + "','"
						+ ipAddress + "','" + sessionId + "')";

				execute(insertQuery);
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/* Create Transaction Logs */
	public boolean transactionLog(String param) {
		try {
			if (param.equalsIgnoreCase("CREATE_TRANSACTION_TABLE")) {
				String sqlQuery = "Create table if not exists s17g209_Transactionlog (LogID INT(6)"
						+ " NOT NULL AUTO_INCREMENT , Username char(50) not null, " + "dbms char(50) , "
						+ "Transaction char(50)," + "SessionID char(50), PRIMARY KEY (LogID)) "
						+ "ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;";

				execute(sqlQuery);
				return true;
			} else if (param.equalsIgnoreCase("CREATE_TRANSACTION_IMPORT")) {
				String insertQuery = "Insert into s17g209_Transactionlog (Username,dbms," + "Transaction,SessionID)"
						+ " values ( '" + dbInformationBean.getUserName() + "','" + dbInformationBean.getDbms() + "','"
						+ "FILE_IMPORT" + "','" + sessionId + "')";
				execute(insertQuery);
				return true;
			} else if (param.equalsIgnoreCase("CREATE_TRANSACTION_EXPORT")) {
				String insertQuery = "Insert into s17g209_Transactionlog (Username,dbms," + "Transaction,SessionID)"
						+ " values ( '" + dbInformationBean.getUserName() + "','" + dbInformationBean.getDbms() + "','"
						+ "FILE_EXPORT" + "','" + sessionId + "')";
				execute(insertQuery);
				return true;
			} else if (param.equalsIgnoreCase("CREATE_TRANSACTION_GRAPHICALANALYSIS")) {
				String insertQuery = "Insert into s17g209_Transactionlog (Username,dbms," + "Transaction,SessionID)"
						+ " values ( '" + dbInformationBean.getUserName() + "','" + dbInformationBean.getDbms() + "','"
						+ "GRAPHICAL_ANALYSIS" + "','" + sessionId + "')";
				execute(insertQuery);
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/* list all tables */
	public List<String> tableList() {
		List<String> tableList = null;
		try {
			if (databaseMetaData != null) {
				resultSet = databaseMetaData.getTables(null, dbInformationBean.getUserName(), null, TABLE_TYPES);
				resultSet.last();
				// determine the number of rows
				int numberOfRows = resultSet.getRow();
				tableList = new ArrayList<String>(numberOfRows);
				resultSet.beforeFirst();
				String tableName = "";
				if (resultSet != null) {
					while (resultSet.next()) {
						tableName = resultSet.getString("TABLE_NAME");
						if (!dbInformationBean.getDbms().equalsIgnoreCase("oracle") || tableName.length() < 4)
							tableList.add(tableName);
						else if (!tableName.substring(0, 4).equalsIgnoreCase("BIN$"))
							tableList.add(tableName);
					}
				}
			}
		} catch (SQLException e) {
			message = "SQL Exception occurred!\n" + "Error Code: " + e.getErrorCode() + "\n" + "SQL State: "
					+ e.getSQLState() + "\n" + "Message :" + e.getMessage() + "\n\n";
		}
		return tableList;
	}

	/* list all columns */
	public List<String> columnList(String tableName) {
		List<String> columnList = new ArrayList<String>();
		try {
			if (databaseMetaData != null) {
				resultSet = databaseMetaData.getColumns(null, dbInformationBean.getDbmsSchema(), tableName, null);

				String columnName = "";
				if (resultSet != null) {
					while (resultSet.next()) {
						columnName = resultSet.getString("COLUMN_NAME");
						columnList.add(columnName);
					}
				}
			}
		} catch (SQLException e) {
			message = "SQL Exception occurred!\n" + "Error Code: " + e.getErrorCode() + "\n" + "SQL State: "
					+ e.getSQLState() + "\n" + "Message :" + e.getMessage() + "\n\n";
		}
		return columnList;
	}

	/* close all the connections */
	public void close() {
		try {
			if (statement != null) {
				statement.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			message = "SQL Exception occurred!\n" + "Error Code: " + e.getErrorCode() + "\n" + "SQL State: "
					+ e.getSQLState() + "\n" + "Message :" + e.getMessage() + "\n\n";
		}
	}

	/* Execute the Query */
	public String execute(String query) {
		try {
			// to execute a query
			if (connection != null && statement != null) {
				if (query.toLowerCase().startsWith("select")) {
					resultSet = statement.executeQuery(query);
					if (resultSet != null) {
						resultSetMetaData = resultSet.getMetaData();
						numOfCols = resultSetMetaData.getColumnCount();
						resultSet.last();
						numOfRows = resultSet.getRow();
						resultSet.beforeFirst();
						columnNamesSelected = new ArrayList<String>(numOfCols);
						for (int i = 0; i < numOfCols; i++) {
							columnNamesSelected.add(resultSetMetaData.getColumnName(i + 1));
						}
					}
				} else {
					// UPDATE,INSERT,DELETE
					statement.executeUpdate(query);
				}
			}
			return "SUCCESS";
		} catch (SQLException e) {
			message = "SQL Exception occurred!\n" + "Error Code: " + e.getErrorCode() + "\n" + "SQL State: "
					+ e.getSQLState() + "\n" + "Message :" + e.getMessage() + "\n\n";
			return "FAIL";
		}
	}

	/* Process the query */
	public String processQuery(String query) {
		String status = "FAIL";
		try {
			if (this.connection == null) {
				connectToDb();
			}
		} catch (NullPointerException e) {
			this.errorMessage = e.getMessage();
		}
		String queryType = query.split(" ")[0];
		switch (queryType.toLowerCase()) {
		case "select":
			try {
				this.resultSet = this.statement.executeQuery(query);
				if (this.resultSet != null) {
					getColumnNames();
					status = "SUCCESS";
				}
			} catch (SQLException e) {
				this.errorMessage = e.getMessage();
				e.printStackTrace();
			}
			break;
		case "insert":
			try {
				changedRows = this.statement.executeUpdate(query);
				status = "SUCCESS";
			} catch (SQLException e) {
				this.errorMessage = e.getMessage();
				e.printStackTrace();
			}
			break;
		case "update":
			try {
				changedRows = this.statement.executeUpdate(query);
				status = "SUCCESS";
			} catch (SQLException e) {
				this.errorMessage = e.getMessage();
				e.printStackTrace();
			}
			break;
		case "delete":
			try {
				changedRows = this.statement.executeUpdate(query);
				status = "SUCCESS";
			} catch (SQLException e) {
				this.errorMessage = e.getMessage();
				e.printStackTrace();
			}
			break;
		case "create":
			try {
				this.statement.executeUpdate(query);
				status = "SUCCESS";
			} catch (SQLException e) {
				this.errorMessage = e.getMessage();
				e.printStackTrace();
			}
			break;
		case "drop":
			try {
				this.statement.executeUpdate(query);
				status = "SUCCESS";
			} catch (SQLException e) {
				this.errorMessage = e.getMessage();
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
		return status;
	}

	/* Select method of database */
	public ResultSet processSelect(String query) {
		try {
			resultSet = statement.executeQuery(query);
			return resultSet;
		} catch (SQLException se) {
			message = "Error Code: " + se.getErrorCode() + "\n" + "SQL State: " + se.getSQLState() + "\n" + "Message :"
					+ se.getMessage() + "\n\n" + "SQLException while processing query.";
			return resultSet = null;
		} catch (Exception e) {
			message = "Exception occurred: " + e.getMessage();
			return resultSet = null;
		}
	}

	public ResultSet getColumnNames(String sqlQuery) {
		try {
			ResultSet resultSet = statement.executeQuery(sqlQuery);
			return resultSet;
		} catch (SQLException se) {
			message = "Error Code: " + se.getErrorCode() + "\n" + "SQL State: " + se.getSQLState() + "\n" + "Message :"
					+ se.getMessage() + "\n\n" + "SQLException while getting table columns.";
			return resultSet = null;
		} catch (Exception e) {
			message = "Exception occurred: " + e.getMessage();
			return resultSet = null;
		}
	}

	/* Get Column Names */
	private void getColumnNames() {
		try {
			if (this.resultSet != null) {
				this.resultSetMetaData = this.resultSet.getMetaData();
				int columnCount = this.resultSetMetaData.getColumnCount();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void generateResult() {
		if (resultSet != null) {
			result = ResultSupport.toResult(resultSet);
		}
	}

	public ResultSet getResultSet() {
		return resultSet;
	}

	public Connection getConnection() {
		return connection;
	}

	public String getMessage() {
		return message;
	}

	public int getNumberOfCols() {
		return numOfCols;
	}

	public int getNumberOfRows() {
		return numOfRows;
	}

	public Result getResult() {
		return result;
	}

}
