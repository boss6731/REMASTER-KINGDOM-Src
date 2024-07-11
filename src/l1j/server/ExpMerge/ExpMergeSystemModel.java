package l1j.server.ExpMerge;

class ExpMergeSystemModel {
	int mergeLimitCount;
	double mergeExpRation;
	int mergeTargetMinLevel;
	int mergeTargetMaxLevel;
	ExpMergeSystemModel() {
		mergeLimitCount = 1;
		mergeExpRation = 0.80;
		mergeTargetMinLevel = 80;
		mergeTargetMaxLevel = 99;
	}
}
