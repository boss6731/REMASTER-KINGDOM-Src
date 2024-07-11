package l1j.server.MJTemplate.Keyword;

public interface MJKeywordModelFactory {
	public static MJKeywordModelFactory newDefault(){
		return new MJKeywordModelFactory(){
			@Override
			public MJKeywordModel newNormalModel(String tableName) {
				return MJKeywordModel.newDatabaseModel(tableName);
			}

			@Override
			public MJKeywordModel newEmptyModel(String tableName) {
				return MJKeywordModel.newEmptyModel(tableName);
			}			
		};
	}
	
	public MJKeywordModel newNormalModel(String tableName);
	public MJKeywordModel newEmptyModel(String tableName);
}
