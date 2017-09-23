package edu.uic.ids517.model;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class IpAccessLog {
	private DbAccessBean dbAccess;
	private String message;
	private ResultSet resultSet;
	private List<String> tableList;
	private String currentUserName;
	private FacesContext context;
	private String password;
	private String userSchema;
	private Date loginTime;
	// private Date logoutTime;
	private String getIpAddress;
	private String getSessionId;
	private String loginTimeString;
	private String logoutTimeString;
	private boolean renderLogs;
	private boolean renderMessage;
	private String dbms;
	private int maxRowId;
	private DbAccessBean dbAccessBean;
	private DbInformationBean dbInformationBean;

	private static final String USERNAME = "f16gxxx";
	private static final String PASSWORD = "f16gxxxR02S";

	public IpAccessLog() {
		tableList = new ArrayList<String>();
		renderLogs = false;
		renderMessage = false;
	}

	public void init() {
		context = FacesContext.getCurrentInstance();
		Map<String, Object> m = context.getExternalContext().getSessionMap();
		dbAccessBean = (DbAccessBean) m.get("dbAccessBean");
		if (dbAccessBean == null)
			dbAccessBean = new DbAccessBean();
		dbInformationBean = (DbInformationBean) m.get("dbInformationBean");
		if (dbInformationBean == null)
			dbInformationBean = new DbInformationBean();
	}

	public boolean connectToLogschema() {
		dbInformationBean.setUserName(USERNAME);
		dbInformationBean.setPassword(PASSWORD);
		dbInformationBean.setDbmsSchema(USERNAME);
		if (dbAccessBean.connectToDb().equalsIgnoreCase("SUCCESS"))
			return true;
		else
			return false;
	}

	/*Create Access Logs*/
	public boolean createAccessLogs() {
		try {
			currentUserName = dbInformationBean.getUserName();
			password = dbInformationBean.getPassword();
			userSchema = dbInformationBean.getDbmsSchema();
			String sqlQuery = "Create table if not exists s17g209.log (LogID INT(6)"
					+ " NOT NULL AUTO_INCREMENT , Username char(50) not null, "
					+ "dbms char(50) ,LoginTime timestamp null, "
					+ "IPAddress char(50), SessionID char(50), PRIMARY KEY (LogID)) "
					+ "ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;";
			FacesContext fCtx = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) fCtx.getExternalContext().getSession(false);
			String sessionId = session.getId();
			// boolean connect = connectToLogschema();
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			String ipAddress = request.getHeader("X-FORWARDED-FOR");
			if (ipAddress == null)
				ipAddress = request.getRemoteAddr();
			dbAccessBean.execute(sqlQuery);
			String insertQuery = "Insert into s17g209.Log (Username,dbms," + "LoginTime,IPAddress,SessionID)"
					+ "values ( '" + dbInformationBean.getUserName() + "','" + dbInformationBean.getDbms() + "',"
					+ new java.sql.Timestamp(new Date().getTime()) + ",'" + ipAddress + "','" + sessionId + "'";
			dbAccessBean.execute(insertQuery);
			return true;
		} catch (Exception e) {
			message = e.getMessage();
			// reConnect();
			e.printStackTrace();
			return false;
		}
	}
}
