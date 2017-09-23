package edu.uic.ids517.model;

public class StatisticsBean implements Cloneable {
	private String dataset;
	private String variable;
	private int numberObs;
	private double minValue;
	private double q1;
	private double median;
	private double q3;
	private double maxValue;
	private double mean;
	private double variance;
	private double standardDeviation;
	private double iqr;
	private double range;

	private static final String columnNames[] = { "dataset", "variable", "numberObs", "minValue", "q1", "median", "q3",
			"maxValue", "mean", "variance", "standardDeviation", "iqr", "range" };

	public String getDataset() {
		return dataset;
	}

	public void setDataset(String dataset) {
		this.dataset = dataset;
	}

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	public int getNumberObs() {
		return numberObs;
	}

	public void setNumberObs(int numberObs) {
		this.numberObs = numberObs;
	}

	public double getMinValue() {
		return minValue;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	public double getQ1() {
		return q1;
	}

	public void setQ1(double q1) {
		this.q1 = q1;
	}

	public double getMedian() {
		return median;
	}

	public void setMedian(double median) {
		this.median = median;
	}

	public double getQ3() {
		return q3;
	}

	public void setQ3(double q3) {
		this.q3 = q3;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public double getMean() {
		return mean;
	}

	public void setMean(double mean) {
		this.mean = mean;
	}

	public double getVariance() {
		return variance;
	}

	public void setVariance(double variance) {
		this.variance = variance;
	}

	public double getStandardDeviation() {
		return standardDeviation;
	}

	public void setStandardDeviation(double standardDeviation) {
		this.standardDeviation = standardDeviation;
	}

	public double getIqr() {
		return iqr;
	}

	public void setIqr(double iqr) {
		this.iqr = iqr;
	}

	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}

	public static String[] getColumnnames() {
		return columnNames;
	}

	public StatisticsBean clone() throws CloneNotSupportedException {
		StatisticsBean cloned = (StatisticsBean) super.clone();
		return cloned;
	}
}
