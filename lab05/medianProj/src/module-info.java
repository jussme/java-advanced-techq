import com.implementation.median.Average;

import ex.api.ClusterAnalysisService;

module implementation {
	requires api;
	exports com.implementation.median;
	provides ClusterAnalysisService with Average;
}