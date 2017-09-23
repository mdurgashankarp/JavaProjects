package edu.uic.ids517.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DbAccessActionBean {

	private String tableName;
	private String sqlQuery;
	private int noOfCols = 0;
	private int noOfRows = 0;
	private Result result;
	private String tableinListColumn;
	private FacesContext context;
	private List<String> columnNames;
	private List<String> tableViewList;
	private List<String> columnNamesSelected;
	private boolean tableListRendered;
	private boolean columnListRendered = false;
	private boolean queryRendered = false;
	private DbAccessBean dbAccessBean;
	private DbInformationBean dbInformationBean;
	private MessagesBean messagesBean;
	private List<String> createDropTableNames;
	private String status;
	private ResultSet resultSet;

	public List<String> getCreateDropTableNames() {
		return createDropTableNames;
	}

	public void setCreateDropTableNames(List<String> createDropTableNames) {
		this.createDropTableNames = createDropTableNames;
	}

	@PostConstruct
	public void init() {
		context = FacesContext.getCurrentInstance();
		Map<String, Object> m = context.getExternalContext().getSessionMap();
		dbAccessBean = (DbAccessBean) m.get("dbAccessBean");
		if (dbAccessBean == null)
			dbAccessBean = new DbAccessBean();
		dbInformationBean = (DbInformationBean) m.get("dbInformationBean");
		if (dbInformationBean == null)
			dbInformationBean = new DbInformationBean();
		messagesBean = (MessagesBean) m.get("messagesBean");
		if (messagesBean == null)
			messagesBean = new MessagesBean();
		listTables();
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getSqlQuery() {
		return sqlQuery;
	}

	public void setSqlQuery(String sqlQuery) {
		this.sqlQuery = sqlQuery;
	}

	public int getNoOfCols() {
		return noOfCols;
	}

	public int getNoOfRows() {
		return noOfRows;
	}

	public boolean isColumnListRendered() {
		return columnListRendered;
	}

	public boolean isQueryRendered() {
		return queryRendered;
	}

	public boolean isTableListRendered() {
		return tableListRendered;
	}

	public List<String> getColumnNamesSelected() {
		return columnNamesSelected;
	}

	public void setColumnNamesSelected(List<String> columnNamesSelected) {
		this.columnNamesSelected = columnNamesSelected;
	}

	public Result getResult() {
		return result;
	}

	public List<String> getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}

	public List<String> getTableViewList() {
		return tableViewList;
	}

	public boolean createAccesslog() {
		boolean accesslog = dbAccessBean.retriveAccessLog("CREATE_LOG_TABLE");
		if (accesslog)
			return true;
		else
			return false;
	}

	/* creates transaction log */
	public boolean createTransactionlog() {
		boolean transactionLog = dbAccessBean.transactionLog("CREATE_TRANSACTION_TABLE");
		if (transactionLog)
			return true;
		else
			return false;
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

	/* lists all columns */
	public String listColumns() {
		try {
			messagesBean.resetAll();
			if (null != tableName && !"".equals(tableName)) {
				columnNames = dbAccessBean.columnList(tableName);
				tableinListColumn = tableName;
				queryRendered = false;
				sqlQuery = "";
				if (null != columnNames) {
					columnListRendered = true;
				}
			} else {
				messagesBean.setErrorMessage("Please select table name and column from the list");
				messagesBean.setRenderErrorMessage(true);
				return "FAIL";

			}
		} catch (Exception e) {
			columnListRendered = false;
			messagesBean.setErrorMessage("");
			messagesBean.setErrorMessage("Exception occurred: " + e.getMessage());
			messagesBean.setRenderErrorMessage(true);
			return "FAIL";
		}
		return "SUCCESS";

	}

	/* export table into csv */
	public String exportCSV() {

		try {
			messagesBean.resetAll();
			if (tableName.isEmpty()) {
				return "FAIL";
			}
			if (tableViewList.isEmpty()) {
				return "FAIL";
			} else {
				FacesContext fc = FacesContext.getCurrentInstance();
				ExternalContext ec = fc.getExternalContext();
				FileOutputStream fos = null;
				String path = fc.getExternalContext().getRealPath("/temp");
				File dir = new File(path);
				if (!dir.exists())
					new File(path).mkdirs();
				ec.setResponseCharacterEncoding("UTF-8");
				Date date = new Date();

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS");
				String fileNameBase = dbInformationBean.getUserName() + "_" + tableName + "_" + dateFormat.format(date)
						+ ".csv";
				String fileName = path + "/" + dbInformationBean.getUserName() + "_" + fileNameBase;
				File f = new File(fileName);
				resultSet = null;
				String sqlQuery = "select * from " + dbInformationBean.getDbmsSchema() + "." + tableName;
				resultSet = dbAccessBean.processSelect(sqlQuery);
				if (resultSet != null) {
					result = ResultSupport.toResult(resultSet);
					Object[][] sData = result.getRowsByIndex();
					String columnNames[] = result.getColumnNames();
					StringBuffer sb = new StringBuffer();
					try {
						fos = new FileOutputStream(fileName);
						for (int i = 0; i < columnNames.length; i++) {
							sb.append(columnNames[i].toString() + ",");
						}
						sb.append("\n");
						fos.write(sb.toString().getBytes());
						for (int i = 0; i < sData.length; i++) {
							sb = new StringBuffer();
							for (int j = 0; j < sData[0].length; j++) {
								if (sData[i][j] == null) {
									String value2 = "0";
									value2 = value2.replaceAll("[^-A-Za-z0-9.]", " . ");
									if (value2.isEmpty()) {
										value2 = "0";
									}
									sb.append(value2 + ",");
								} else {
									String value = sData[i][j].toString();
									System.out.println(value+" Value ");
									if (value.contains(",")) {
										int index = value.indexOf(",");
										String newValue = value.substring(0, index - 1);
										value = newValue + value.substring(index + 1, value.length());
									}
									value = value.replaceAll("[^-A-Za-z0-9,.]", " ");
									if (value.isEmpty()) {
										value = "0";
									}
									sb.append(value + ",");
								}
							}
							sb.append("\n");
							fos.write(sb.toString().getBytes());
						}
						fos.flush();
						fos.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException io) {
						io.printStackTrace();
					}
					String mimeType = ec.getMimeType(fileName);
					FileInputStream in = null;
					byte b;
					ec.responseReset();
					ec.setResponseContentType(mimeType);
					ec.setResponseContentLength((int) f.length());
					ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileNameBase + "\"");
					try {
						in = new FileInputStream(f);
						OutputStream output = ec.getResponseOutputStream();
						while (true) {
							b = (byte) in.read();
							if (b < 0)
								break;
							output.write(b);
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							in.close();
						} catch (IOException e) {
							// message=e.getMessage();
							// renderMessage=true;
						}
					}
					fc.responseComplete();
				} else {

				}
			}
			dbAccessBean.transactionLog("CREATE_TRANSACTION_EXPORT");
			return "SUCCESS";
		} catch (Exception e) {
			return "FAIL";
		}
	}

	/* export table into xml */
	public String exportXML() {
		try {

			if (tableViewList.isEmpty()) {

				return "FAIL";
			}
			if (tableName.isEmpty()) {

				return "FAIL";
			} else {
				try {
					FacesContext fc = FacesContext.getCurrentInstance();
					ExternalContext ec = fc.getExternalContext();
					String path = fc.getExternalContext().getRealPath("/temp");
					ec.setResponseCharacterEncoding("UTF-8");
					Date date = new Date();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS");
					String fileNameBase = dbInformationBean.getUserName() + "_" + tableName + "_"
							+ dateFormat.format(date) + ".xml";
					String fileName = path + "/" + dbInformationBean.getUserName() + "_" + fileNameBase;
					String sqlQuery = "select * from " + dbInformationBean.getDbmsSchema() + "." + tableName;
					resultSet = dbAccessBean.processSelect(sqlQuery);
					if (resultSet != null) {
						DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
						DocumentBuilder builder = factory.newDocumentBuilder();
						Document doc = builder.newDocument();
						Element results = doc.createElement(tableName);
						doc.appendChild(results);
						ResultSetMetaData rsmd = resultSet.getMetaData();
						int colCount = rsmd.getColumnCount();
						while (resultSet.next()) {
							Element row = doc.createElement("Row");
							results.appendChild(row);
							int column = 0;
							String stringColumn = null;
							int floatColumn = 0;
							int smallInt = 0;
							double doubleColumn = 0;
							for (int i = 1; i <= colCount; i++) {
								String columnName = rsmd.getColumnName(i);
								String dataType = rsmd.getColumnTypeName(i);
								Element node = doc.createElement(columnName);
								switch (dataType.toLowerCase()) {
								case ("int"):
									column = resultSet.getInt(i);
									node.appendChild(doc.createTextNode(String.valueOf(column)));
									row.appendChild(node);
									break;
								case ("char"):
									stringColumn = resultSet.getString(i);
									node.appendChild(doc.createTextNode(String.valueOf(stringColumn)));
									row.appendChild(node);
									break;
								case ("smallint"):
									smallInt = resultSet.getInt(i);
									node.appendChild(doc.createTextNode(String.valueOf(smallInt)));
									row.appendChild(node);
									break;
								case ("double"):
									doubleColumn = resultSet.getInt(i);
									node.appendChild(doc.createTextNode(String.valueOf(doubleColumn)));
									row.appendChild(node);
									break;
								case ("float"):
									floatColumn = resultSet.getInt(i);
									node.appendChild(doc.createTextNode(String.valueOf(floatColumn)));
									row.appendChild(node);
									break;
								}
							}
						}
						TransformerFactory transformerFactory = TransformerFactory.newInstance();
						Transformer transformer = transformerFactory.newTransformer();
						transformer.setOutputProperty(OutputKeys.INDENT, "yes");
						DOMSource source = new DOMSource(doc);
						StreamResult file = new StreamResult(new File(fileName));
						transformer.transform(source, file);
						HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();
						response.setHeader("Content-Disposition", "attachment;filename=\"" + fileNameBase + "\"");
						response.setContentLength((int) fileName.length());
						FileInputStream input = null;
						try {
							int i = 0;
							input = new FileInputStream(fileName);
							byte[] buffer = new byte[1024];
							while ((i = input.read(buffer)) != -1) {
								response.getOutputStream().write(buffer, 0, i);
								response.getOutputStream().flush();
							}
							fc.responseComplete();
							fc.renderResponse();
							dbAccessBean.transactionLog("CREATE_TRANSACTION_EXPORT");
							return "SUCCESS";
						} catch (IOException e) {
							return "FAIL";
						} finally {
							try {
								if (input != null) {
									input.close();
								}
							} catch (IOException e) {
								return "FAIL";
							}
						}
					} else {

						return "FAIL";
					}
				} catch (ParserConfigurationException pe) {
					return "FAIL";
				} catch (SQLException se) {

					return "FAIL";
				} catch (Exception e) {
					return "FAIL";
				}
			}
		} catch (Exception e) {

			return "FAIL";
		}
	}

	/* select all columns */
	public String selectAllColumn() {
		messagesBean.resetAll();
		listColumns();
		if (null != tableinListColumn && !"".equals(tableinListColumn)) {
			sqlQuery = "select * from " + tableinListColumn + " ;";
			dbAccessBean.execute(sqlQuery);
			noOfCols = dbAccessBean.getNumberOfCols();
			noOfRows = dbAccessBean.getNumberOfRows();
			dbAccessBean.generateResult();
			result = dbAccessBean.getResult();
			columnNamesSelected = dbAccessBean.columnList(tableinListColumn);
			queryRendered = true;
			return "SUCCESS";
		} else {
			messagesBean.setErrorMessage("Please select table name from list");
			messagesBean.setRenderErrorMessage(true);
			return "FAIL";
		}
	}

	/* select particular columns */
	public String selectParticularColumn() {
		messagesBean.resetAll();
		listColumns();
		String listString = "";
		String listStringNew = "";
		try {
			if (columnNamesSelected.isEmpty()) {
				messagesBean.setErrorMessage("Please select column from list");
				messagesBean.setRenderErrorMessage(true);
				return "FAIL";
			} else {

				for (String s : columnNamesSelected) {
					listString += s + ",";
				}
				listStringNew = listString.substring(0, listString.length() - 1);

				if (null != tableinListColumn && !"".equals(tableinListColumn)) {
					sqlQuery = "select " + listStringNew + " from " + tableinListColumn + " ;";
					dbAccessBean.execute(sqlQuery);
					noOfCols = dbAccessBean.getNumberOfCols();
					noOfRows = dbAccessBean.getNumberOfRows();
					dbAccessBean.generateResult();
					result = dbAccessBean.getResult();
					queryRendered = true;
					return "SUCCESS";
				} else {
					messagesBean.setErrorMessage("Please select Table Name from list");
					messagesBean.setRenderErrorMessage(true);
					return "FAIL";
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			messagesBean.setErrorMessage("Please select Table Name from list");
			messagesBean.setRenderErrorMessage(true);
			return "FAIL";
		}
	}

	/* drop all tables */
	public String dropAllTables() {
		try {
			dbAccessBean.getConnection().setAutoCommit(false);
			if (null != tableViewList && !tableViewList.isEmpty()) {
				dbAccessBean.execute("set foreign_key_checks=0");
				for (String temp : tableViewList) {
					status = dbAccessBean.execute("drop table " + temp);
					if (status.equals("FAIL")) {
						dbAccessBean.getConnection().rollback();
						dbAccessBean.getConnection().setAutoCommit(true);
						messagesBean.setRenderErrorMessage(true);
						messagesBean.setErrorMessage(messagesBean.getErrorMessage() + "     Transactions Rolled Back");
						return "FAIL";

					}

				}
				dbAccessBean.getConnection().commit();
				dbAccessBean.getConnection().setAutoCommit(true);
				messagesBean.setSuccessMessage(
						tableViewList.toString().replace("[", "").replace("]", "") + " Tables are Dropped");
				messagesBean.setRenderSuccessMessage(true);
			} else {
				messagesBean.setErrorMessage("Please select Table to Drop");
				messagesBean.setRenderErrorMessage(true);
				status = "FAIL";
			}

		} catch (Exception e) {
			messagesBean.setErrorMessage(e.getMessage());
			messagesBean.setRenderErrorMessage(true);
			status = "FAIL";
		}
		return status;
	}

	/* drop particular tables */
	public String dropTables() {

		String status = "SUCCESS";
		try {
			dbAccessBean.getConnection().setAutoCommit(false);
			if (null != tableName && !tableName.isEmpty()) {
				dbAccessBean.execute("set foreign_key_checks=0");
				status = dbAccessBean.execute("drop table " + tableName);
				if (status.equals("FAIL")) {
					dbAccessBean.getConnection().rollback();
					dbAccessBean.getConnection().setAutoCommit(true);
					return "FAIL";
				}
				listTables();
				dbAccessBean.getConnection().commit();
				dbAccessBean.getConnection().setAutoCommit(true);

			} else {
				messagesBean.setErrorMessage("Please select Table to Drop");
				messagesBean.setRenderErrorMessage(true);
				status = "FAIL";
			}
		} catch (Exception e) {
			messagesBean.setErrorMessage(e.getMessage());
			messagesBean.setRenderErrorMessage(true);
			status = "FAIL";
		}
		return status;
	}

	/* process SQL Query */
	public String processSQLQuery() {
		messagesBean.resetAll();
		try {
			if (dbAccessBean.execute(sqlQuery).equals("SUCCESS")) {
				if (sqlQuery.toLowerCase().startsWith("select")) {
					noOfCols = dbAccessBean.getNumberOfCols();
					noOfRows = dbAccessBean.getNumberOfRows();
					dbAccessBean.generateResult();
					result = dbAccessBean.getResult();
					columnNamesSelected = dbAccessBean.getColumnNamesSelected();
					queryRendered = true;
					listTables();
				} else {
					messagesBean.setRenderSuccessMessage(true);
					messagesBean.setSuccessMessage("SQL Query Successfully excecuted");
					queryRendered = false;
				}
			} else {
				queryRendered = false;
				return "FAIL";
			}
			return "SUCCESS";
		} catch (Exception e) {
			messagesBean.setErrorMessage("Exception occurred: " + e.getMessage());
			messagesBean.setErrorMessage("There is some error in the query");
			messagesBean.setRenderErrorMessage(true);
			return "FAIL";
		}
	}

	/* logout */
	public String logout() {
		dbAccessBean.retriveAccessLog("LOGOUT");
		dbAccessBean.close();
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "LOGOUT";
	}

	/* reset all variables */
	public String resetMessage() {
		queryRendered = false;
		return "HOME";
	}

}
