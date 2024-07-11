package l1j.server.MJTemplate.Keyword;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import l1j.server.server.model.gametime.BaseTime;
import l1j.server.server.model.gametime.TimeListener;

public class MJKeywordView implements TimeListener{
	private MJKeywordModelProvider provider;
	private MJKeywordModel previousModel;
	private MJKeywordModel currentModel;
	private int rankLimit;
	private List<MJKeywordRankModel> models;
	MJKeywordView(MJKeywordModelProvider provider, MJKeywordModel previousModel, MJKeywordModel currentModel, int rankLimit){
		this.provider = provider;
		this.previousModel = previousModel;
		this.currentModel = currentModel;
		this.rankLimit = rankLimit;
	}
	@Override
	public void onMonthChanged(BaseTime time) {
	}
	@Override
	public void onDayChanged(BaseTime time) {
		MJKeywordModel before = previousModel;
		previousModel = currentModel;
		currentModel = provider.newNormalModel(LocalDateTime.now());
		before.dropTable();
	}
	@Override
	public void onHourChanged(BaseTime time) {}
	@Override
	public void onMinuteChanged(BaseTime time) {}
	@Override
	public void onSecondChanged(BaseTime time) {}
	
	void onTick(){
		List<MJKeywordRankModel> newModels = currentModel.selectRanksKeywords(rankLimit);
		if(newModels == null){
			if(models == null){
				onEmptyAllocate();
			}
			return;
		}
		onFluctuations(newModels);
	}
	
	private void onEmptyAllocate(){
		List<MJKeywordRankModel> newModels = new ArrayList<>(rankLimit);
		for(int i=0; i<rankLimit; ++i){
			newModels.add(MJKeywordRankModel.empty());					
		}
		models = newModels;
	}
	
	private void onFluctuations(List<MJKeywordRankModel> newModels){
		for(MJKeywordRankModel model : newModels){
			int numOfKeyword = previousModel.numOfKeywordAny(model.keyword());
			if(numOfKeyword > 0){
				model.fluctuations(model.accessCount() - numOfKeyword);				
				model.isNew(false);
			}else{
				model.fluctuations(model.accessCount());
				model.isNew(true);
			}
		}
		while(newModels.size() < rankLimit){
			newModels.add(MJKeywordRankModel.empty());
		}
		models = newModels;
	}
	
	public List<MJKeywordRankModel> models(){
		return models;
	}
	
	public MJKeywordModel currentModel(){
		return currentModel;
	}
}
