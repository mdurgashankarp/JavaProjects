package edu.uic.ids517.model;

import java.awt.Color;
import java.io.File;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.math3.stat.StatUtils;
import org.jfree.chart.ChartFactory;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.DefaultPieDataset;

import org.jfree.data.category.DefaultCategoryDataset;

@ManagedBean
@SessionScoped
public class ActionBeanAnalysis {

	private boolean renderChart;
	private FacesContext facesContext;
	private String chartType;
	private DbAccessActionBean dbAccessActionBean;
	private String tableName;
	private DbAccessBean dbAccessBean;
	private ResultSet resultSet;
	private String xyChartPath;
	private boolean xySeriesChart;
	private String columnNameSelected;
	private ResultSetMetaData resultSetMetaData;
	private DefaultPieDataset pieDataset = new DefaultPieDataset();
	private DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
	private String pieChartPath;
	private String barChartPath;
	private String predictorValue;
	private String responseValue;
	private boolean renderPieChart;
	private MessagesBean messagesBean;
	private String path;
	private StatisticsActionBean statisticsActionBean;
	private DbInformationBean dbInformationBean;
	private List<String> columnNames;
	private String tableinListColumn;
	private boolean queryRendered;
	private boolean columnListRendered;
	private List<String> tableViewList;
	private boolean tableListRendered;
	private boolean renderBarChart;
	private boolean renderScatterPlot;
	private boolean renderScatterPlotLine;
	private String scatterChartPath;
	private String scatterChartPathLine;
	private boolean renderRegressionColumns;

	public boolean isRenderScatterPlotLine() {
		return renderScatterPlotLine;
	}

	public void setRenderScatterPlotLine(boolean renderScatterPlotLine) {
		this.renderScatterPlotLine = renderScatterPlotLine;
	}

	@PostConstruct
	public void init() {
		facesContext = FacesContext.getCurrentInstance();
		Map<String, Object> m = facesContext.getExternalContext().getSessionMap();
		dbAccessActionBean = (DbAccessActionBean) m.get("dBAccessActionBean");
		if (dbAccessActionBean == null)
			dbAccessActionBean = new DbAccessActionBean();
		dbAccessBean = (DbAccessBean) m.get("dbAccessBean");
		if (dbAccessBean == null)
			dbAccessBean = new DbAccessBean();
		dbInformationBean = (DbInformationBean) m.get("dbInformationBean");
		if (dbInformationBean == null)
			dbInformationBean = new DbInformationBean();
		statisticsActionBean = (StatisticsActionBean) m.get("statisticsActionBean");
		if (statisticsActionBean == null)
			statisticsActionBean = new StatisticsActionBean();
		messagesBean = (MessagesBean) m.get("messagesBean");
		if (messagesBean == null)
			messagesBean = new MessagesBean();

		path = facesContext.getExternalContext().getRealPath("/ChartImages");
	}

	/* Lists all the tables */
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

	public List<String> getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
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

	public boolean isColumnListRendered() {
		return columnListRendered;
	}

	public boolean isRenderRegressionColumn() {
		return renderRegressionColumns;
	}

	public void setRenderRegressionColumn(boolean renderRegressionColumn) {
		this.renderRegressionColumns = renderRegressionColumns;
	}

	public void setColumnListRendered(boolean columnListRendered) {
		this.columnListRendered = columnListRendered;
	}

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

	/* lists all the columns */
	public String listColumns() {

		try {
			messagesBean.resetAll();
			if (null != tableName && !"".equals(tableName)) {
				columnNames = dbAccessBean.columnList(tableName);
				tableinListColumn = tableName;
				queryRendered = false;
				String sqlQuery = "";
				if (null != columnNames) {
					if (!(chartType.equalsIgnoreCase("3") || chartType.equalsIgnoreCase("4"))) {
						columnListRendered = true;
						renderRegressionColumns = false;
					} else {
						renderRegressionColumns = true;
						columnListRendered = false;
					}
				} else {
					messagesBean.setErrorMessage("Please select Table Name from the list");
					messagesBean.setRenderErrorMessage(true);
					return "FAIL";

				}
			}
		} catch (Exception e) {
			columnListRendered = false;
			renderRegressionColumns = false;
			messagesBean.setErrorMessage("");
			messagesBean.setErrorMessage("Exception occurred: " + e.getMessage());
			messagesBean.setRenderErrorMessage(true);
			e.printStackTrace();
			return "FAIL";
		}
		return "SUCCESS";

	}

	public boolean isRenderRegressionColumns() {
		return renderRegressionColumns;
	}

	public void setRenderRegressionColumns(boolean renderRegressionColumns) {
		this.renderRegressionColumns = renderRegressionColumns;
	}

	/* Processes Graphics abd Visualizations */
	public String processGraphicQuery() {
		try {
			messagesBean.resetAll();
			String header;
			if (!chartType.equalsIgnoreCase("3")) {
				header = columnNameSelected;
			} else {
				header = predictorValue + "," + responseValue;
			}

			String query = "select " + header + " from " + tableName;
			dbAccessBean.execute(query);
			if (dbAccessBean.getResultSet() != null) {
				resultSet = dbAccessBean.getResultSet();
				double dataValues[] = new double[dbAccessBean.getNumberOfRows()];
				for (int i = 0; i < dbAccessBean.getNumberOfRows(); i++) {
					resultSet.next();
					dataValues[i] = resultSet.getDouble(1);
				}
				double median = StatUtils.percentile(dataValues, 50.0);
				double q1 = StatUtils.percentile(dataValues, 25.0);
				double q3 = StatUtils.percentile(dataValues, 75.0);

				Double value = 0.0;
				int countMedian = 0;
				int countQ1 = 0;
				int countQ3 = 0;
				int greaterThanQ3 = 0;

				resultSetMetaData = resultSet.getMetaData();
				for (double temp : dataValues) {
					if (temp <= q1) {
						countQ1++;
					} else if (temp > q1 && temp <= median)
						countMedian++;
					else if (temp > median && temp <= q3)
						countQ3++;
					else if (temp > q3)
						greaterThanQ3++;
				}
				switch (chartType) {

				case "1":
					pieDataset.setValue("Quartile Q1", countQ1);
					pieDataset.setValue("Lies between Quartile Q1 and Median", countMedian);
					pieDataset.setValue("Lies between Median and Quartile Q3", countQ3);
					pieDataset.setValue("Greater than Q3", greaterThanQ3);

					JFreeChart pieChart = ChartFactory.createPieChart(columnNameSelected, pieDataset, true, true,
							false);

					File outPie = new File(path + "/" + dbInformationBean.getUserName() + "_piechart.png");
					ChartUtilities.saveChartAsPNG(outPie, pieChart, 600, 450);
					pieChartPath = "/ChartImages/" + dbInformationBean.getUserName() + "_piechart.png";
					renderPieChart = true;
					renderChart = false;
					dbAccessBean.transactionLog("CREATE_TRANSACTION_GRAPHICALANALYSIS");
					break;
				case "2":
					categoryDataset.setValue(countQ1, "Quartile Q1", "Category 1");
					categoryDataset.setValue(countMedian, "Lies between Quartile Q1 and Median", "Category 2");
					categoryDataset.setValue(countQ3, "Lies between Median and Quartile Q3", "Category 3");
					categoryDataset.setValue(greaterThanQ3, "Greater than Q3", "Category 4");

					JFreeChart barChart = ChartFactory.createBarChart(columnNameSelected, "CategoryValue", "Value",
							categoryDataset);

					File outBar = new File(path + "/" + dbInformationBean.getUserName() + "_barchart.png");
					ChartUtilities.saveChartAsPNG(outBar, barChart, 600, 450);
					barChartPath = "/ChartImages/" + dbInformationBean.getUserName() + "_barchart.png";
					renderBarChart = true;
					renderChart = false;
					dbAccessBean.transactionLog("CREATE_TRANSACTION_GRAPHICALANALYSIS");
					break;
				case "3":
					if (responseValue == null || predictorValue == null) {
						return "FAIL";
					}
					if (responseValue.equals("0") || predictorValue.equals("0")) {
						return "FAIL";
					}
					statisticsActionBean.setPredictorValue(predictorValue);
					statisticsActionBean.setResponseValue(responseValue);
					statisticsActionBean.setTableViewList(tableViewList);
					statisticsActionBean.setTableNameSelected(tableName);
					if (statisticsActionBean.generateRegressionReport().equalsIgnoreCase("SUCCESS")) {

						JFreeChart chart = ChartFactory.createScatterPlot("Scatter Plot", predictorValue, responseValue,
								statisticsActionBean.getXySeriesVariable(), PlotOrientation.VERTICAL, true, true,
								false);
						File xy = new File(path + "/" + dbInformationBean.getUserName() + "_scatterplot.png");
						ChartUtilities.saveChartAsPNG(xy, chart, 600, 450);
						scatterChartPath = "/ChartImages/" + dbInformationBean.getUserName() + "_scatterplot.png";
						renderScatterPlot = true;
						renderChart = false;
						dbAccessBean.transactionLog("CREATE_TRANSACTION_GRAPHICALANALYSIS");
						return "SUCCESS";
					} else {
						return "fail";
					}
				case "4":
					if (responseValue == null || predictorValue == null) {
						return "FAIL";
					}
					if (responseValue.equals("0") || predictorValue.equals("0")) {
						return "FAIL";
					}
					statisticsActionBean.setPredictorValue(predictorValue);
					statisticsActionBean.setResponseValue(responseValue);
					statisticsActionBean.setTableViewList(tableViewList);
					statisticsActionBean.setTableNameSelected(tableName);
					if (statisticsActionBean.generateRegressionReport().equalsIgnoreCase("SUCCESS")) {

						JFreeChart chart = ChartFactory.createScatterPlot("Scatter Plot with Line", predictorValue,
								responseValue, statisticsActionBean.getXySeriesVariable(), PlotOrientation.VERTICAL,
								true, true, false);

						File xy = new File(path + "/" + dbInformationBean.getUserName() + "_scatterplot.png");
						XYPlot xyplot = chart.getXYPlot();

						xyplot.setDataset(1, statisticsActionBean.getXyDataset());

						XYLineAndShapeRenderer xylineandshaperenderer = new XYLineAndShapeRenderer(true, false);

						xylineandshaperenderer.setSeriesPaint(0, Color.BLACK);

						xyplot.setRenderer(1, xylineandshaperenderer);

						File xyReg = new File(path + "/" + dbInformationBean.getUserName() + "_scatterplotwRline.png");

						ChartUtilities.saveChartAsPNG(xyReg, chart, 600, 450);

						scatterChartPathLine = "/ChartImages/" + dbInformationBean.getUserName()
								+ "_scatterplotwRline.png";

						ChartUtilities.saveChartAsPNG(xy, chart, 600, 450);
						// scatterChartPathLine = "/ChartImages/" +
						// dbInformationBean.getUserName() + "_scatterplot.png";
						renderScatterPlotLine = true;
						renderChart = false;
						dbAccessBean.transactionLog("CREATE_TRANSACTION_GRAPHICALANALYSIS");
						return "SUCCESS";
					} else {
						return "fail";
					}
				default:
					break;

				}

			} else {
				return "FAIL";
			}
			dbAccessBean.transactionLog("CREATE_TRANSACTION_GRAPHICALANALYSIS");
			return "SUCCESS";

		} catch (Exception e) {
			e.printStackTrace();
			messagesBean.setErrorMessage("There is an error in creating graphs");
			messagesBean.setRenderErrorMessage(true);
			return "FAIL";
		}
	}

	public DbAccessBean getDbAccessBean() {
		return dbAccessBean;
	}

	public void setDbAccessBean(DbAccessBean dbAccessBean) {
		this.dbAccessBean = dbAccessBean;
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

	public String getBarChartPath() {
		return barChartPath;
	}

	public boolean isRenderScatterPlot() {
		return renderScatterPlot;
	}

	public void setRenderScatterPlot(boolean renderScatterPlot) {
		this.renderScatterPlot = renderScatterPlot;
	}

	public String getScatterChartPath() {
		return scatterChartPath;
	}

	public String getScatterChartPathLine() {
		return scatterChartPathLine;
	}

	public void setScatterChartPathLine(String scatterChartPathLine) {
		this.scatterChartPathLine = scatterChartPathLine;
	}

	public void setScatterChartPath(String scatterChartPath) {
		this.scatterChartPath = scatterChartPath;
	}

	public void setBarChartPath(String barChartPath) {
		this.barChartPath = barChartPath;
	}

	public boolean isRenderBarChart() {
		return renderBarChart;
	}

	public void setRenderBarChart(boolean renderBarChart) {
		this.renderBarChart = renderBarChart;
	}

	public void setPieChartPath(String pieChartPath) {
		this.pieChartPath = pieChartPath;
	}

	public boolean isRenderPieChart() {
		return renderPieChart;
	}

	public boolean isRenderChart() {
		return renderChart;
	}

	public void setRenderChart(boolean renderChart) {
		this.renderChart = renderChart;
	}

	public String getChartType() {
		return chartType;
	}

	public void setChartType(String chartType) {
		this.chartType = chartType;
	}

	public String getColumnNameSelected() {
		return columnNameSelected;
	}

	public void setColumnNameSelected(String columnNameSelected) {
		this.columnNameSelected = columnNameSelected;
	}

	public DefaultPieDataset getPieDataset() {
		return pieDataset;
	}

	public void setPieDataset(DefaultPieDataset pieDataset) {
		this.pieDataset = pieDataset;
	}

	public String getPieChartPath() {
		return pieChartPath;
	}

	public void setRenderPieChart(boolean renderPieChart) {
		this.renderPieChart = renderPieChart;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/* reset the variables */
	public String resetMessage() {
		renderPieChart = false;
		renderBarChart = false;
		renderScatterPlot = false;
		return "HOME";
	}
}
