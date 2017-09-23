package edu.uic.ids517.model;

public class DbInformationBean {

	private String userName;
	private String password;
	private String dbmsHost;
	private String dbmsSchema;
	private String dbms;
	private String dbmsPort;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDbmsHost() {
		return dbmsHost;
	}

	public void setDbmsHost(String dbmsHost) {
		this.dbmsHost = dbmsHost;
	}

	public String getDbmsSchema() {
		return dbmsSchema;
	}

	public void setDbmsSchema(String dbmsSchema) {
		this.dbmsSchema = dbmsSchema;
	}

	public String getDbms() {
		return dbms;
	}

	public void setDbms(String dbms) {
		this.dbms = dbms;
	}

	public String getDbmsPort() {
		return dbmsPort;
	}

	public void setDbmsPort(String dbmsPort) {
		this.dbmsPort = dbmsPort;
	}

}
