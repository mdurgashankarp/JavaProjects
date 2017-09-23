package edu.uic.ids517.model;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class ActionBeanComputeReturns {
	private FacesContext context;
	private MessagesBean messagesBean;
	private List<String> tableViewList;
	private String tableNameSelected;
	private List<String> sourceData;
	private List<String> destinationData;
	private DbAccessBean dbAccessBean;
	private ResultSet resultSet;
	private boolean tableListRendered;
	private List<String> columnNamesTable;
	private String source;
	private String destination;

	@PostConstruct
	public void init() {
		context = FacesContext.getCurrentInstance();
		Map<String, Object> m = context.getExternalContext().getSessionMap();
		messagesBean = (MessagesBean) m.get("messagesBean");
		if (messagesBean == null)
			messagesBean = new MessagesBean();
		dbAccessBean = (DbAccessBean) m.get("dbAccessBean");
		if (dbAccessBean == null)
			dbAccessBean = new DbAccessBean();
	}

	/* lists all tables */
	public String listTables() {
		try {
			messagesBean.resetAll();
			tableViewList = dbAccessBean.tableList();
			if (null != tableViewList) {
				tableListRendered = true;
			}
			return "SUCCESS";
		} catch (Exception e) {
			tableListRendered = false;
			messagesBean.setErrorMessage("");
			messagesBean.setErrorMessage("Exception occurred: " + e.getMessage());
			messagesBean.setRenderErrorMessage(true);
			return "FAIL";
		}
	}

	/* displays all columns */
	public String displayColumns() {
		messagesBean.resetAll();
		try {
		if (tableViewList.isEmpty()) {
			messagesBean.setErrorMessage("No tables found in the schema.");
			messagesBean.setRenderErrorMessage(true);
			return "FAIL";
		}
		if (tableNameSelected == null) {
			messagesBean.setErrorMessage("Please select a table.");
			messagesBean.setRenderErrorMessage(true);
			return "FAIL";
		}
	
			String sqlQuery = "select * from " + tableNameSelected;
			resultSet = dbAccessBean.processSelect(sqlQuery);
			if (resultSet != null) {
				columnNamesTable = new ArrayList<String>();
				sourceData = new ArrayList<String>();
				destinationData = new ArrayList<String>();

				ResultSetMetaData resultSetmd = (ResultSetMetaData) resultSet.getMetaData();
				int columnCount = resultSetmd.getColumnCount();
				for (int i = 1; i <= columnCount; i++) {
					String name = resultSetmd.getColumnName(i);
					sourceData.add(name);
					destinationData.add(name);
				}

			} else {
				messagesBean.setErrorMessage(dbAccessBean.getMessage());
				messagesBean.setRenderErrorMessage(true);

			}
			return "SUCCESS";
		} catch (Exception e) {
			messagesBean.setErrorMessage(dbAccessBean.getMessage());
			messagesBean.setRenderErrorMessage(true);
			return "FAIL";
		}
	}

	/* computes return between source and destination */
	public String computeReturns() {
		try {
			String sqlQuery = "select TRANS_DATE, " + source + " from " + tableNameSelected;
			List<String> listQuery = dbAccessBean.executequeryList(sqlQuery);
			if (!listQuery.isEmpty()) {
				String[] lists = listQuery.get(0).split(" ");
				double prev = Double.parseDouble(lists[1]);
				String updateQuery0 = "Update " + tableNameSelected + " set " + destination + " = " + 0 + " WHERE "
						+ "TRANS_DATE = '" + lists[0] + "'";
				dbAccessBean.execute(updateQuery0);
				for (int i = 1; i < listQuery.size(); i++) {
					String[] str = listQuery.get(i).split(" ");
					double compute = Double.parseDouble(str[1]);
					double new1 = (Math.log(compute) - Math.log(prev));
					String updateQuery = "Update " + tableNameSelected + " set " + destination + " = " + new1
							+ " WHERE " + "TRANS_DATE = '" + str[0] + "'";
					dbAccessBean.execute(updateQuery);
					prev = compute;
					messagesBean.setRenderErrorMessage(true);
					messagesBean.setErrorMessage("Computed Returns Successfully");

				}
			}
			return "SUCCESS";
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			messagesBean.setRenderErrorMessage(true);
			messagesBean.setErrorMessage("Select columns to compute");
			return "FAIL";
		} catch (Exception e) {
			e.printStackTrace();
			messagesBean.setRenderErrorMessage(true);
			messagesBean.setErrorMessage("Select columns to compute");
			return "FAIL";
		}
	}

	public FacesContext getContext() {
		return context;
	}

	public void setContext(FacesContext context) {
		this.context = context;
	}

	public MessagesBean getMessageBean() {
		return messagesBean;
	}

	public void setMessageBean(MessagesBean messageBean) {
		this.messagesBean = messageBean;
	}

	public List<String> getTableViewList() {
		return tableViewList;
	}

	public void setTableViewList(List<String> tableViewList) {
		this.tableViewList = tableViewList;
	}

	public String getTableNameSelected() {
		return tableNameSelected;
	}

	public void setTableNameSelected(String tableNameSelected) {
		this.tableNameSelected = tableNameSelected;
	}

	public List<String> getSourceData() {
		return sourceData;
	}

	public void setSourceData(List<String> sourceData) {
		this.sourceData = sourceData;
	}

	public List<String> getDestinationData() {
		return destinationData;
	}

	public void setDestinationData(List<String> destinationData) {
		this.destinationData = destinationData;
	}

	public DbAccessBean getDbAccessBean() {
		return dbAccessBean;
	}

	public void setDbAccessBean(DbAccessBean dbAccessBean) {
		this.dbAccessBean = dbAccessBean;
	}

	public ResultSet getResultSet() {
		return resultSet;
	}

	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	public boolean isTableListRendered() {
		return tableListRendered;
	}

	public void setTableListRendered(boolean tableListRendered) {
		this.tableListRendered = tableListRendered;
	}

	public List<String> getColumnNamesTable() {
		return columnNamesTable;
	}

	public void setColumnNamesTable(List<String> columnNamesTable) {
		this.columnNamesTable = columnNamesTable;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	/* reset variable values */
	public String resetMessage() {
		messagesBean.setErrorMessage("");
		messagesBean.setRenderErrorMessage(false);
		return "HOME";

	}
}
