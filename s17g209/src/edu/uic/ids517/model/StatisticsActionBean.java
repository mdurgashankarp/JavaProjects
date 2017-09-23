package edu.uic.ids517.model;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.jfree.data.function.LineFunction2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.statistics.Regression;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.mysql.jdbc.ResultSetMetaData;

public class StatisticsActionBean {
	private StatisticsBean descriptiveStatisticsBean;
	private DbInformationBean dbInfoBean;
	private XYDataset xyDataset;
	private boolean renderRegButton;
	public boolean isRenderRegButton() {
		return renderRegButton;
	}

	public boolean isPrButton() {
		return prButton;
	}

	public void setPrButton(boolean prButton) {
		this.prButton = prButton;
	}

	public void setRenderRegButton(boolean renderRegButton) {
		this.renderRegButton = renderRegButton;
	}

	public boolean isDescriptiveButton() {
		return descriptiveButton;
	}

	public void setDescriptiveButton(boolean descriptiveButton) {
		this.descriptiveButton = descriptiveButton;
	}

	public void setDescriptiveStatisticsBean(StatisticsBean descriptiveStatisticsBean) {
		this.descriptiveStatisticsBean = descriptiveStatisticsBean;
	}

	private boolean descriptiveButton;
	private boolean prButton;

	public XYDataset getXyDataset() {
		return xyDataset;
	}

	public void setXyDataset(XYDataset xyDataset) {
		this.xyDataset = xyDataset;
	}

	public DbInformationBean getDbInfobean() {
		return dbInfoBean;
	}

	public void setDbInfobean(DbInformationBean dbInfobean) {
		this.dbInfoBean = dbInfobean;
	}

	public StatisticsBean getDescriptiveStatisticsBean() {
		return descriptiveStatisticsBean;
	}

	public void setStatisticsBean(StatisticsBean statisticsBean) {
		this.descriptiveStatisticsBean = statisticsBean;
	}

	public List<StatisticsBean> getStatisticsBeanList() {
		return statisticsBeanList;
	}

	public void setStatisticsBeanList(List<StatisticsBean> statisticsBeanList) {
		this.statisticsBeanList = statisticsBeanList;
	}

	private List<StatisticsBean> statisticsBeanList;
	private List<String> columnNames;
	private String descrStats[];
	private FileImportBean actionBeanFile;
	private boolean renderStats;
	private boolean renderRegression;

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	private boolean columnRender;
	private boolean renderSchema;
	private boolean buttonDisable;
	private boolean renderMessage;
	private boolean renderReport;
	private boolean renderTabledata;
	private boolean renderTablename;
	private boolean renderRegressionColumn;
	private boolean renderRegressionButton;
	private boolean renderColumnListbutton;
	private boolean renderRegressionResult;
	private List<String> numericData;
	private List<String> categoricalData;
	private List<String> columnSelected;
	private List<String> columnsList;
	private List<String> tableList;
	private List<String> columns;
	private List<String> list;
	private ResultSet resultSet;
	private String predictorValue;
	private String responseValue;
	private XYSeriesCollection xySeriesVariable;
	private XYSeriesCollection xyTimeSeriesCollection;
	private XYSeries predictorSeries;
	private XYSeries responseSeries;
	private XYSeries xySeries;
	private XYSeriesCollection xySeriesVariables;
	private ResultSetMetaData resultSetMetaData;
	private RegressionAnalysisManagedBean regressionAnalysisManagedBean;

	public RegressionAnalysisManagedBean getRegressionAnalysisManagedBean() {
		return regressionAnalysisManagedBean;
	}

	public void setRegressionAnalysisManagedBean(RegressionAnalysisManagedBean regressionAnalysisManagedBean) {
		this.regressionAnalysisManagedBean = regressionAnalysisManagedBean;
	}

	public XYSeriesCollection getXySeriesVariable() {
		return xySeriesVariable;
	}

	public void setXySeriesVariable(XYSeriesCollection xySeriesVariable) {
		this.xySeriesVariable = xySeriesVariable;
	}

	public XYSeriesCollection getXyTimeSeriesCollection() {
		return xyTimeSeriesCollection;
	}

	public void setXyTimeSeriesCollection(XYSeriesCollection xyTimeSeriesCollection) {
		this.xyTimeSeriesCollection = xyTimeSeriesCollection;
	}

	public XYSeriesCollection getXySeriesVariables() {
		return xySeriesVariables;
	}

	public void setXySeriesVariables(XYSeriesCollection xySeriesVariables) {
		this.xySeriesVariables = xySeriesVariables;
	}

	public ResultSetMetaData getResultSetMetaData() {
		return resultSetMetaData;
	}

	public void setResultSetMetaData(ResultSetMetaData resultSetMetaData) {
		this.resultSetMetaData = resultSetMetaData;
	}

	public List<String> getColumnsList() {
		return columnsList;
	}

	public void setColumnsList(List<String> columnsList) {
		this.columnsList = columnsList;
	}

	public String getPredictorValue() {
		return predictorValue;
	}

	public void setPredictorValue(String predictorValue) {
		this.predictorValue = predictorValue;
	}

	public String getResponseValue() {
		return responseValue;
	}

	public void setResponseValue(String responseValue) {
		this.responseValue = responseValue;
	}

	public boolean isColumnRender() {
		return columnRender;
	}

	public void setColumnRender(boolean columnRender) {
		this.columnRender = columnRender;
	}

	public boolean isButtonDisable() {
		return buttonDisable;
	}

	public void setButtonDisable(boolean buttonDisable) {
		this.buttonDisable = buttonDisable;
	}

	public boolean isRenderSchema() {
		return renderSchema;
	}

	public void setRenderSchema(boolean renderSchema) {
		this.renderSchema = renderSchema;
	}

	public boolean isRenderMessage() {
		return renderMessage;
	}

	public void setRenderMessage(boolean renderMessage) {
		this.renderMessage = renderMessage;
	}

	public boolean isRenderReport() {
		return renderReport;
	}

	public void setRenderReport(boolean renderReport) {
		this.renderReport = renderReport;
	}

	public boolean isRenderTabledata() {
		return renderTabledata;
	}

	public void setRenderTabledata(boolean renderTabledata) {
		this.renderTabledata = renderTabledata;
	}

	public boolean isRenderTablename() {
		return renderTablename;
	}

	public void setRenderTablename(boolean renderTablename) {
		this.renderTablename = renderTablename;
	}

	public boolean isRenderRegressionColumn() {
		return renderRegressionColumn;
	}

	public void setRenderRegressionColumn(boolean renderRegressionColumn) {
		this.renderRegressionColumn = renderRegressionColumn;
	}

	public boolean isRenderRegressionButton() {
		return renderRegressionButton;
	}

	public void setRenderRegressionButton(boolean renderRegressionButton) {
		this.renderRegressionButton = renderRegressionButton;
	}

	public boolean isRenderColumnListbutton() {
		return renderColumnListbutton;
	}

	public void setRenderColumnListbutton(boolean renderColumnListbutton) {
		this.renderColumnListbutton = renderColumnListbutton;
	}

	public boolean isRenderRegressionResult() {
		return renderRegressionResult;
	}

	public void setRenderRegressionResult(boolean renderRegressionResult) {
		this.renderRegressionResult = renderRegressionResult;
	}

	private int numberObs;
	private List<String> columnNamesSelected;

	public List<String> getColumnNamesSelected() {
		return columnNamesSelected;
	}

	public void setColumnNamesSelected(List<String> columnNamesSelected) {
		this.columnNamesSelected = columnNamesSelected;
	}

	private FacesContext context;
	private DbAccessBean dbAccessBean;
	// private MessagesBean messageBean;
	private List<String> tableViewList;
	private List<String> myTableList;

	public List<String> getMyTableList() {
		return myTableList;
	}

	public void setMyTableList(List<String> myTableList) {
		this.myTableList = myTableList;
	}

	private boolean tableListRendered;
	private String tableNameSelected;

	public String getTableNameSelected() {
		return tableNameSelected;
	}

	public void setTableNameSelected(String tableNameSelected) {
		this.tableNameSelected = tableNameSelected;
	}

	private String tableinListColumn;
	private boolean queryRendered;
	private String sqlQuery;
	private boolean columnListRendered;
	private List<String> columnNamesTable;

	public List<String> getColumnNamesTable() {
		return columnNamesTable;
	}

	public void setColumnNamesTable(List<String> columnNamesTable) {
		this.columnNamesTable = columnNamesTable;
	}

	public int getNumberObs() {
		return numberObs;
	}

	public void setNumberObs(int numberObs) {
		this.numberObs = numberObs;
	}

	public List<String> getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}

	public String[] getDescrStats() {
		return descrStats;
	}

	public void setDescrStats(String[] descrStats) {
		this.descrStats = descrStats;
	}

	public FileImportBean getActionBeanFile() {
		return actionBeanFile;
	}

	public void setActionBeanFile(FileImportBean actionBeanFile) {
		this.actionBeanFile = actionBeanFile;
	}

	public boolean isRenderStats() {
		return renderStats;
	}

	public void setRenderStats(boolean renderStats) {
		this.renderStats = renderStats;
	}

	public boolean isRenderRegression() {
		return renderRegression;
	}

	public void setRenderRegression(boolean renderRegression) {
		this.renderRegression = renderRegression;
	}

	public List<String> getCategoricalData() {
		return categoricalData;
	}

	public void setCategoricalData(List<String> categoricalData) {
		this.categoricalData = categoricalData;
	}

	public List<String> getNumericData() {
		return numericData;
	}

	public void setNumericData(List<String> numericData) {
		this.numericData = numericData;
	}

	public ResultSet getResultSet() {
		return resultSet;
	}

	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	public StatisticsActionBean() {
		descriptiveStatisticsBean = new StatisticsBean();
		categoricalData = new ArrayList<String>();
		columnsList = new ArrayList<String>();
		numericData = new ArrayList<String>();
		statisticsBeanList = null;
		renderStats = false;
		renderRegression = false;
		columnNames = null;
		descrStats = StatisticsBean.getColumnnames();
		columnNames = new ArrayList<String>(descrStats.length);
		for (int i = 0; i < descrStats.length; i++) {
			columnNames.add(descrStats[i]);
		}
		xySeries = new XYSeries("MatchedData");
		xySeriesVariable = new XYSeriesCollection();
		renderTablename = false;
		xyTimeSeriesCollection = new XYSeriesCollection();
		predictorSeries = new XYSeries("Predictor");
		responseSeries = new XYSeries("Response");
		getTables();
	}

	@PostConstruct
	public void init() {
		context = FacesContext.getCurrentInstance();
		Map<String, Object> m = context.getExternalContext().getSessionMap();
		dbAccessBean = (DbAccessBean) m.get("dbAccessBean");
		if (dbAccessBean == null)
			dbAccessBean = new DbAccessBean();

		regressionAnalysisManagedBean = (RegressionAnalysisManagedBean) m.get("regressionAnalysisManagedBean");
		if (regressionAnalysisManagedBean == null)
			regressionAnalysisManagedBean = new RegressionAnalysisManagedBean();

		dbInfoBean = (DbInformationBean) m.get("dbInformationBean");
		if (dbInfoBean == null)
			dbInfoBean = new DbInformationBean();

		descriptiveStatisticsBean = (StatisticsBean) m.get("descriptiveStatisticsBean");
		if (descriptiveStatisticsBean == null)
			descriptiveStatisticsBean = new StatisticsBean();
		/*
		 * messageBean = (MessagesBean) m.get("messageBean"); if (messageBean ==
		 * null) messageBean = new MessagesBean();
		 */
		listTables();
	}

	/*Get Tables*/
	public String getTables() {
		try {
			reset();
			tableViewList = new ArrayList<String>();
			// String tablenames;
			tableViewList = dbAccessBean.tableList();

			renderTableList();
			return "SUCCESS";
		} catch (Exception e) {
			message = e.getMessage();
			renderMessage = true;
			return "FAIL";
		}
	}

	/*Get Table List*/
	public void renderTableList() {
		reset();
		if (tableViewList.isEmpty()) {
			message = "No tables found in the schema.";
			columnRender = false;
			renderRegressionResult = false;
			columnRender = false;
			renderRegressionColumn = false;
			renderTabledata = false;
			renderMessage = true;
			renderTablename = false;
			columnRender = false;
			renderMessage = true;
		} else
			renderTablename = true;
	}

	/*List All Tables*/
	public String listTables() {
		try {
			// messageBean.resetAll();
			myTableList = new ArrayList<String>();
			tableViewList = dbAccessBean.tableList();
			if (null != tableViewList) {
				tableListRendered = true;
			}
			for (String s : tableViewList) {
				if (s.startsWith("s17g211")) {
					myTableList.add(s);
				}

			}

			return "SUCCESS";
		} catch (Exception e) {
			tableListRendered = false;
			/*
			 * messageBean.setErrorMessage("");
			 * messageBean.setErrorMessage("Exception occurred: " +
			 * e.getMessage()); messageBean.setRenderErrorMessage(true);
			 */
			return "FAIL";
		}
	}

	/*List All Columns*/
	public String listColumns() {
		try {
			// messageBean.resetAll();
			descriptiveButton = false;
			renderRegressionColumn = false;
			renderRegressionButton = true;
			renderRegressionResult = false;
			renderStats = false;
			if (null != tableNameSelected && !"".equals(tableNameSelected)) {
				columnNamesTable = dbAccessBean.columnList(tableNameSelected);
				// tableinListColumn = tableName;

				if (null != columnNamesTable) {
					columnListRendered = true;
				}
			} else {
				/*
				 * messageBean.
				 * setErrorMessage("Please select Table Name from the list");
				 * messageBean.setRenderErrorMessage(true);
				 */
				return "FAIL";

			}
		} catch (Exception e) {
			columnListRendered = false;
			/*
			 * messageBean.setErrorMessage("");
			 * messageBean.setErrorMessage("Exception occurred: " +
			 * e.getMessage()); messageBean.setRenderErrorMessage(true);
			 */
			return "FAIL";
		}
		return "SUCCESS";

	}

	/*Generate Descriptive Statistics*/
	public String generateDescriptiveStatistics() {
		renderStats = false;
		renderRegressionResult = false;
		renderRegressionButton = true;
		columnListRendered=true;
		renderRegressionColumn=false;
		String dataRow[] = null;
		statisticsBeanList = new ArrayList<StatisticsBean>();
		String header = "";
		try{
			if(columnNamesSelected!=null){
				for (int j = 0; j < columnNamesSelected.size(); j++) {
			
					header = columnNamesSelected.get(j);
			
					String query = "select " + header + " from " + tableNameSelected;
					dbAccessBean.execute(query);
					if (dbAccessBean.getResultSet() != null) {
						ResultSet resultSet = dbAccessBean.getResultSet();
						// List<String[]> dataList = actionBeanFile.getDataList();
						// numberObs = dataList.size();
						double dataValues[] = new double[dbAccessBean.getNumberOfRows()];
						for (int i = 0; i < dbAccessBean.getNumberOfRows(); i++) {
							try {
								resultSet.next();
								dataValues[i] = resultSet.getDouble(1);
							} catch (Exception e) {
			
							}
			
						}
						// String data = null;
						double minValue = StatUtils.min(dataValues);
						double maxValue = StatUtils.max(dataValues);
						double mean = StatUtils.mean(dataValues);
						double variance = StatUtils.variance(dataValues, mean);
						double standardDeviation = Math.sqrt(variance);
						double median = StatUtils.percentile(dataValues, 50.0);
						double q1 = StatUtils.percentile(dataValues, 25.0);
						double q3 = StatUtils.percentile(dataValues, 75.0);
						double iqr = q3 - q1;
						double range = maxValue - minValue;
						descriptiveStatisticsBean.setDataset(tableNameSelected);
						descriptiveStatisticsBean.setVariable(header);
						descriptiveStatisticsBean.setNumberObs(dbAccessBean.getNumberOfRows());
						descriptiveStatisticsBean.setMinValue(minValue);
						descriptiveStatisticsBean.setMaxValue(maxValue);
						descriptiveStatisticsBean.setMean(mean);
						descriptiveStatisticsBean.setVariance(variance);
						descriptiveStatisticsBean.setStandardDeviation(standardDeviation);
						descriptiveStatisticsBean.setMedian(median);
						descriptiveStatisticsBean.setQ1(q1);
						descriptiveStatisticsBean.setQ3(q3);
						descriptiveStatisticsBean.setIqr(iqr);
						descriptiveStatisticsBean.setRange(range);
						try {
							statisticsBeanList.add(descriptiveStatisticsBean.clone());
						} catch (CloneNotSupportedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						renderStats = true;
			
					}
				}
		}
			return "SUCCESS";
	}catch(Exception e){
		e.printStackTrace();
		return "FAIL";
		
	}
	}

	public void reset() {
		renderMessage = false;
		renderTabledata = false;
		renderRegressionResult = false;
	}

	/*Method for Regresssion*/
	public String displayColumnsforRegression() {
		columnListRendered=false;
		renderRegressionColumn=true;
		descriptiveButton = true;
		renderRegressionButton = false;
		renderStats=false;
		reset();
		if (tableViewList.isEmpty()) {
			message = "No tables found in the schema.";
			renderMessage = true;
			renderColumnListbutton = true;
			renderReport = true;
			renderRegressionColumn = false;
			return "FAIL";
		}
		if (tableNameSelected == null) {
			message = "Please select a table.";
			renderMessage = true;
			renderColumnListbutton = true;
			renderReport = true;
			return "FAIL";
		}
		String status = getRegressionColumnNames();
		if (status.equalsIgnoreCase("SUCCESS")) {
			columnRender = false;
			renderRegressionButton = false;
			renderRegressionColumn = true;
			renderColumnListbutton = true;
			renderReport = true;

			return "SUCCESS";
		} else {
			renderMessage = true;
			return "FAIL";
		}
	}

	/*Get Regression columns*/
	public String getRegressionColumnNames() {
		reset();
		if (tableViewList.isEmpty()) {
			message = "No tables found in the schema.";
			renderMessage = true;
			return "FAIL";
		}
		if (tableNameSelected.isEmpty()) {
			message = "Please select a table.";
			renderMessage = true;
			return "FAIL";
		}
		if (generateRegressionColumns()) {

			return "SUCCESS";
		} else {
			renderMessage = true;
			return "FAIL";
		}
	}

	/*Generate RegressionColumns*/
	public boolean generateRegressionColumns() {
		try {
			String sqlQuery = "select * from " + dbInfoBean.getDbmsSchema() + "." + tableNameSelected;
			resultSet = dbAccessBean.getColumnNames(sqlQuery);
			if (resultSet != null) {
				columnsList.clear();
				categoricalData.clear();
				numericData.clear();
				ResultSetMetaData resultSetmd = (ResultSetMetaData) resultSet.getMetaData();
				int columnCount = resultSetmd.getColumnCount();
				for (int i = 1; i <= columnCount; i++) {
					String name = resultSetmd.getColumnName(i);
					String datatype = resultSetmd.getColumnTypeName(i);
					if (datatype.equalsIgnoreCase("char") || datatype.equalsIgnoreCase("varchar")) {
						categoricalData.add(name);
					} else
						numericData.add(name);
				}
				columnRender = true;
			} else {
				message = dbAccessBean.getMessage();
				renderMessage = true;
				return false;
			}
			return true;
		} catch (Exception e) {
			message = e.getMessage();
			e.printStackTrace();
			renderMessage = true;
			return false;
		}
	}

	/*Generate Regression Report*/
	public String generateRegressionReport() {
		reset();
		if (tableViewList.isEmpty()) {
			message = "No tables found in the schema.";
			renderMessage = true;
			
			renderColumnListbutton = true;
			renderReport = true;
			renderRegressionColumn = false;
			return "FAIL";
		}
		if (tableNameSelected == null) {
			message = "Please select a table.";
			renderMessage = true;
			return "FAIL";
		}
		if (predictorValue == null || responseValue == null) {
			message = "Please select a predictor and a response variable.";
			renderMessage = true;
			return "FAIL";
		}

		if (responseValue.equals("0") || predictorValue.equals("0")) {
			message = "Please select a predictor and a response variable.";
			renderMessage = true;
			return "FAIL";
		}
		if (calculateRegressionVariables()) {
			return "SUCCESS";
		} else
			return "FAIL";
	}

	public String resetButton() {
		columnRender = false;
		renderRegressionButton = true;
		renderColumnListbutton = false;
		renderRegressionColumn = false;
		renderRegressionResult = false;
		renderReport = false;
		renderMessage = false;
		columnNamesSelected.clear();
		return "SUCCESS";
	}

	/*Calculate Regression Variables*/
	public boolean calculateRegressionVariables() {
		try {
			
			responseSeries.clear();
			predictorSeries.clear();
			xySeries.clear();
			xySeriesVariable.removeAllSeries();
			xyTimeSeriesCollection.removeAllSeries();
			String sqlQuery = "select " + predictorValue + ", " + responseValue + " from " + tableNameSelected;
			resultSet = dbAccessBean.processSelect(sqlQuery);
			if (resultSet != null) {
				resultSetMetaData = (ResultSetMetaData) resultSet.getMetaData();
				String predictorName = resultSetMetaData.getColumnTypeName(1);
				String responseName = resultSetMetaData.getColumnTypeName(2);
				List<Double> predictorList = new ArrayList<Double>();
				List<Double> responseList = new ArrayList<Double>();
				while (resultSet.next()) {
					switch (predictorName.toLowerCase()) {
					case "int":
						predictorList.add((double) resultSet.getInt(1));
						break;
					case "smallint":
						predictorList.add((double) resultSet.getInt(1));
						break;
					case "float":
						predictorList.add((double) resultSet.getFloat(1));
						break;
					case "double":
						predictorList.add((double) resultSet.getDouble(1));
						break;
					case "long":
						predictorList.add((double) resultSet.getLong(1));
						break;
					default:
						predictorList.add((double) resultSet.getDouble(1));
						break;
					}
					switch (responseName.toLowerCase()) {
					case "int":
						responseList.add((double) resultSet.getInt(2));
						break;
					case "smallint":
						responseList.add((double) resultSet.getInt(2));
						break;
					case "float":
						responseList.add((double) resultSet.getFloat(2));
						break;
					case "double":
						responseList.add((double) resultSet.getDouble(2));
						break;
					case "long":
						responseList.add((double) resultSet.getLong(2));
						break;
					default:
						responseList.add((double) resultSet.getDouble(2));
						break;
					}
				}
				double[] predictorArray = new double[predictorList.size()];
				for (int i = 0; i < predictorList.size(); i++) {
					predictorArray[i] = (double) predictorList.get(i);
					predictorSeries.add(i + 1, (double) predictorList.get(i));
				}
				double[] responseArray = new double[responseList.size()];
				for (int i = 0; i < responseList.size(); i++) {
					responseArray[i] = (double) responseList.get(i);
					responseSeries.add(i + 1, (double) responseList.get(i));
				}
				xyTimeSeriesCollection.addSeries(predictorSeries);
				xyTimeSeriesCollection.addSeries(responseSeries);
				SimpleRegression sr = new SimpleRegression();
				if (responseArray.length > predictorArray.length) {
					for (int i = 0; i < predictorArray.length; i++) {
						sr.addData(predictorArray[i], responseArray[i]);
						xySeries.add(predictorArray[i], responseArray[i]);
					}
				} else {
					for (int i = 0; i < responseArray.length; i++) {
						sr.addData(predictorArray[i], responseArray[i]);
						xySeries.add(predictorArray[i], responseArray[i]);
					}
				}
				xySeriesVariable.addSeries(xySeries);
				int totalDF = responseArray.length - 1;
				TDistribution tDistribution = new TDistribution(totalDF);
				double intercept = sr.getIntercept();
				double interceptStandardError = sr.getInterceptStdErr();
				double tStatistic = 0;
				int predictorDF = 1;
				int residualErrorDF = totalDF - predictorDF;
				double rSquare = sr.getRSquare();
				double rSquareAdjusted = rSquare - (1 - rSquare) / (totalDF - predictorDF - 1);

				if (interceptStandardError != 0) {
					tStatistic = (double) intercept / interceptStandardError;
				}
				double interceptPValue = (double) 2 * tDistribution.cumulativeProbability(-Math.abs(tStatistic));
				double slope = sr.getSlope();
				double slopeStandardError = sr.getSlopeStdErr();
				double tStatisticpredict = 0;
				if (slopeStandardError != 0) {
					tStatisticpredict = (double) slope / slopeStandardError;
				}

				double pValuePredictor = (double) 2 * tDistribution.cumulativeProbability(-Math.abs(tStatisticpredict));
				double standardErrorModel = Math.sqrt(StatUtils.variance(responseArray))
						* (Math.sqrt(1 - rSquareAdjusted));
				double regressionSumSquares = sr.getRegressionSumSquares();
				double sumSquaredErrors = sr.getSumSquaredErrors();
				double totalSumSquares = sr.getTotalSumSquares();
				
				double meanSquare = 0;
				if (predictorDF != 0) {
					meanSquare = regressionSumSquares / predictorDF;
				}
				double meanSquareError = 0;
				if (residualErrorDF != 0) {
					meanSquareError = (double) (sumSquaredErrors / residualErrorDF);
				}
				double totalMeanSquares = meanSquare + meanSquareError;
				double fValue = 0;
				if (meanSquareError != 0) {
					fValue = meanSquare / meanSquareError;
				}
				String regressionEquation = responseValue + " = " + intercept + " + (" + slope + ") " + predictorValue;
				FDistribution fDistribution = new FDistribution(predictorDF, residualErrorDF);
				double pValue = (double) (1 - fDistribution.cumulativeProbability(fValue));
				if(predictorValue.equalsIgnoreCase(responseValue)){
				tStatistic = (Double.parseDouble("NaN"));
				pValuePredictor = 0.0;
				interceptPValue = 0.0;
				}
				boolean regressionResultsStatus = regressionAnalysisManagedBean.setRegressionAnalysisVariables(
						regressionEquation, intercept, interceptStandardError, tStatistic, interceptPValue, slope,
						slopeStandardError, tStatisticpredict, pValuePredictor, standardErrorModel, rSquare,
						rSquareAdjusted, predictorDF, residualErrorDF, totalDF, regressionSumSquares, sumSquaredErrors,
						totalSumSquares, totalMeanSquares, meanSquare, meanSquareError, fValue, pValue);
				double regressionParameters[] = Regression.getOLSRegression(xySeriesVariable,0);

				LineFunction2D linefunction2d = new LineFunction2D(regressionParameters[0], regressionParameters[1]);

				xyDataset = DatasetUtilities.sampleFunction2D(linefunction2d,xySeriesVariable.getDomainLowerBound(false),xySeriesVariable.getDomainUpperBound(false), 100, "Fitted Regression Line");
				if (regressionResultsStatus) {
					renderRegressionResult = true;
					return true;
				} else {
					message = regressionAnalysisManagedBean.getMessage();
					renderMessage = true;
					return false;
				}
			} else {
				message = dbAccessBean.getMessage();
				renderMessage = true;
				return false;
			}
		} catch (Exception e) {
			message = e.getMessage();
			e.printStackTrace();
			renderMessage = true;
			return false;
		}
	}

	public FacesContext getContext() {
		return context;
	}

	public void setContext(FacesContext context) {
		this.context = context;
	}

	public DbAccessBean getDbAccessBean() {
		return dbAccessBean;
	}

	public void setDbAccessBean(DbAccessBean dbAccessBean) {
		this.dbAccessBean = dbAccessBean;
	}

	/*
	 * public MessagesBean getMessageBean() { return messageBean; }
	 * 
	 * public void setMessageBean(MessagesBean messageBean) { this.messageBean =
	 * messageBean; }
	 */

	public List<String> getTableViewList() {
		return tableViewList;
	}

	public void setTableViewList(List<String> tableViewList) {
		this.tableViewList = tableViewList;
	}

	public boolean isTableListRendered() {
		return tableListRendered;
	}

	public void setTableListRendered(boolean tableListRendered) {
		this.tableListRendered = tableListRendered;
	}

	public String getTableinListColumn() {
		return tableinListColumn;
	}

	public void setTableinListColumn(String tableinListColumn) {
		this.tableinListColumn = tableinListColumn;
	}

	public boolean isQueryRendered() {
		return queryRendered;
	}

	public void setQueryRendered(boolean queryRendered) {
		this.queryRendered = queryRendered;
	}

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

	public String getSqlQuery() {
		return sqlQuery;
	}

	public void setSqlQuery(String sqlQuery) {
		this.sqlQuery = sqlQuery;
	}

	public boolean getColumnListRendered() {
		return columnListRendered;
	}

	public void setColumnListRendered(boolean columnListRendered) {
		this.columnListRendered = columnListRendered;
	}
	
	public String resetMessage(){
		renderRegressionResult = false;
		renderStats = false;
		return "HOME";
	}
}
