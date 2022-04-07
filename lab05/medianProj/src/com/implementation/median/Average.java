package com.implementation.median;

import ex.api.ClusterAnalysisService;
import ex.api.ClusteringException;
import ex.api.DataSet;

public class Average implements ClusterAnalysisService{
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
		return "Average";
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

		String[][] result = new String[1][sums.length];
		for(int it = 0; it < sums.length; ++ it) {
			result[0][it] = countingCol[it]? String.format("%.2f", sums[it]/data.length) : "none";
		}
		returnDataSet = new DataSet();
		returnDataSet.setData(result);
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
