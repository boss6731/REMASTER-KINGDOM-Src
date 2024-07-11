package l1j.server.MJTemplate.MJSqlHelper.Clause;

class MJClauseHelper {
	static int calculateOffset(int page, int countPerPage){
		if(page <= 1){
			return 0;
		}
		return (page - 1) * countPerPage;
	}
}
