package l1j.server.MJWebServer.Dispatcher.my.service.refund;

import l1j.server.MJTemplate.MJSqlHelper.Clause.MJWhereClause;

public class MJMyRefundCategory {
	static final MJMyRefundCategory[] categories = new MJMyRefundCategory[]{
			new MJMyRefundCategory(999, "전체", true),
			new MJMyRefundCategory(0, "대기중", false),
			new MJMyRefundCategory(1, "완료됨", false),
	};
	
	public static MJMyRefundCategory findCategory(int categoryIndex){
		for(MJMyRefundCategory category : categories){
			if(category.category == categoryIndex){
				return category;
			}
		}
		return categories[0];
	}
	
	private int category;
	private String categoryTitle;
	private boolean allView;
	private MJWhereClause clause;
	private MJMyRefundCategory(int category, String categoryTitle, boolean allView){
		this.category = category;
		this.categoryTitle = categoryTitle;
		this.allView = allView;
		if(category == 999){
			clause = MJWhereClause.emptyClause;
		}else{			
			clause = MJWhereClause.integerClause("is_refund", category);
		}
	}
	
	public int category(){
		return category;
	}
	
	public String categoryTitle(){
		return categoryTitle;
	}
	
	public boolean allView(){
		return allView;
	}
	
	public MJWhereClause whereClause(){
		return clause;
	}
}
