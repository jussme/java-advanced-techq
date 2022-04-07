import com.implementation.standarddev.StandardDev;

import ex.api.ClusterAnalysisService;

module sdProj {
	requires api;
	exports com.implementation.standarddev;
	provides ClusterAnalysisService with StandardDev;
}