package com.implementation.standarddev;

import ex.api.ClusterAnalysisService;
import ex.api.ClusteringException;
import ex.api.DataSet;

public class StandardDev implements ClusterAnalysisService{
	private boolean processing;
	private boolean dataSubmitted;
	private boolean error;
	private DataSet dataSet;
	private DataSet returnDataSet;
	
	@Override
	public void setOptions(String[] options) throws ClusteringException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		return "Standard deviation";
	}

	@Override
	public void submit(DataSet ds) throws ClusteringException {
		synchronized(this) {
			processing = true;
			dataSubmitted = true;
		}
		
		String[][] data = ds.getData();
		
		double[] sums = new double[data[0].length];
		boolean[] countingCol = new boolean[data[0].length];
		
		for(int col = 0; col < data[0].length; ++col) {
			try {
				for(int row = 0; row < data.length; ++row) {
					sums[col] += Double.valueOf(data[row][col]);
				}
				countingCol[col] = true;
			} catch (NumberFormatException e) {
				countingCol[col] = false;
			}
		}
		
		double[] avrgs = new double[data[0].length];
		for(int it = 0; it < sums.length; ++ it) {
			avrgs[it] = countingCol[it]? sums[it]/data.length : 0;
		}
		
		//dev
		
		double[] numeratorSums = new double[data[0].length];
		for(int col = 0; col < data[0].length; ++col) {
			if(!countingCol[col])
				continue;
			for(int row = 0; row < data.length; ++row) {
				numeratorSums[col] += Math.pow(Double.valueOf(data[row][col]) - avrgs[col], 2);
			}
		}
		
		String[][] devs = new String[1][data[0].length];
		int rowCount = data.length;
		for(int col = 0; col < data[0].length; ++col) {
			devs[0][col] = countingCol[col]? String.format("%.2f", Math.sqrt(numeratorSums[col]/rowCount)) : "none";
		}

		returnDataSet = new DataSet();
		returnDataSet.setData(devs);
		synchronized(this) {
			processing = false;
			error = false;
		}
	}

	@Override
	public DataSet retrieve(boolean clear) throws ClusteringException {
		synchronized(this) {
			if(error)
				throw new ClusteringException("Processing error");
			if(processing || !dataSubmitted)
				return null;
			var localDataRef = returnDataSet;
			if(clear) {
				returnDataSet = null;
			}
			return localDataRef;
		}
	}

}
