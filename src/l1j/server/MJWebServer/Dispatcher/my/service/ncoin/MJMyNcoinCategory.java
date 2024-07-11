package l1j.server.MJWebServer.Dispatcher.my.service.ncoin;

import l1j.server.MJTemplate.MJString;

public class MJMyNcoinCategory {
	static final MJMyNcoinCategory[] categories = new MJMyNcoinCategory[]{
			new MJMyNcoinCategory(999, "전체", true),
			new MJMyNcoinCategory(0, "대기중", false),			
			new MJMyNcoinCategory(1, "완료됨", false),
			new MJMyNcoinCategory(2, "취소됨", false),
	};
	
	public static MJMyNcoinCategory findCategory(int categoryIndex){
		for(MJMyNcoinCategory category : categories){
			if(category.category == categoryIndex){
				return category;
			}
		}
		return null;
	}
	
	private int category;
	private String categoryTitle;
	private boolean allView;
	private String whereClause;
	private MJMyNcoinCategory(int category, String categoryTitle, boolean allView){
		this.category = category;
		this.categoryTitle = categoryTitle;
		this.allView = allView;
		if(category == 999){
			this.whereClause = MJString.EmptyString;
		}else{			
			this.whereClause = String.format("is_deposit=%d", category);
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
	
	public String whereClause(){
		return whereClause;
	}
}
