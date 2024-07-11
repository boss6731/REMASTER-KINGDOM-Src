package l1j.server.MJWebServer.Dispatcher.my.service.pledge;

import l1j.server.MJTemplate.MJSqlHelper.Clause.MJOrderClause;

public class MJMyPledgeCategory {
	static final MJMyPledgeCategory[] categories = new MJMyPledgeCategory[]{
			new MJMyPledgeCategory(0, "��ü", MJOrderClause.simpleClause("clan_id", false)),
			new MJMyPledgeCategory(1, "����", MJOrderClause.simpleClause("War_point", false)),
			new MJMyPledgeCategory(2, "����", MJOrderClause.simpleClause("clan_birthday", false)),
			new MJMyPledgeCategory(3, "����Ʈ", MJOrderClause.compositeClause(MJOrderClause.simpleClause("hashouse", false), MJOrderClause.simpleClause("hascastle", false))),			
	};
	
	public static MJMyPledgeCategory findCategory(int categoryIndex){
		for(MJMyPledgeCategory category : categories){
			if(category.category() == categoryIndex){
				return category;
			}
		}
		return null;
	}
	
	private int category;
	private String categoryTitle;
	private MJOrderClause clause;
	private MJMyPledgeCategory(int category, String categoryTitle, MJOrderClause clause){
		this.category = category;
		this.categoryTitle = categoryTitle;
		this.clause = clause;
	}
	
	public int category(){
		return category;
	}
	
	public String categoryTitle(){
		return categoryTitle;
	}
	
	MJOrderClause clause(){
		return clause;
	}
}
