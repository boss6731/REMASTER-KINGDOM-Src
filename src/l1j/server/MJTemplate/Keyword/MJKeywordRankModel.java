package l1j.server.MJTemplate.Keyword;

public class MJKeywordRankModel implements Comparable<MJKeywordRankModel>{
	public static MJKeywordRankModel empty(){
		return new MJKeywordRankModel("-", 0, 0);
	}
	
	private String keyword;
	private int rank;
	private int accessCount;
	private int fluctuations;
	private boolean isNew;
	MJKeywordRankModel(String keyword, int rank, int accessCount){
		this.keyword = keyword;
		this.rank = rank;
		this.accessCount = accessCount;
		this.fluctuations = 0;
		this.isNew = false;
	}
	
	void fluctuations(int fluctuations){
		this.fluctuations = fluctuations;
	}
	
	public String keyword(){
		return keyword;
	}
	
	public void isNew(boolean isNew){
		this.isNew = isNew;
	}
	
	public boolean isNew(){
		return isNew;
	}
	
	public int rank(){
		return rank;
	}
	
	public int accessCount(){
		return accessCount;
	}
	
	public int fluctuations(){
		return fluctuations;
	}

	@Override
	public int compareTo(MJKeywordRankModel o) {
		int count = o.accessCount();
		if(count > accessCount){
			return -1;
		}else if(count < accessCount){
			return 1;
		}else{
			return 0;
		}
	}
	
}
