package l1j.server.MJWebServer.Dispatcher.Template.Blood.API;

import java.util.Collection;

import com.google.gson.Gson;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Blood.POJO.MJPledgesInfo;
import l1j.server.MJWebServer.Dispatcher.Template.Blood.POJO.MJPledgesInfo.PledgeExtension;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;

public class MJBloodPledgeResponse extends MJHttpResponse{
	
	private static int _joinType;
	
	public MJBloodPledgeResponse(MJHttpRequest request) {
		super(request);
		
		_joinType = Integer.valueOf(request.get_parameters().get("joinType").get(0));
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		MJPledgesInfo pInfo = new MJPledgesInfo();
		Collection<L1Clan> col = L1World.getInstance().getRealClans();
		pInfo.paginationNavi.currentPage = 1;
		pInfo.paginationNavi.paginationSize = 5;
		pInfo.paginationNavi.displayCount = 10;
		pInfo.paginationNavi.totalCount = col.size();
		if(pInfo.paginationNavi.totalCount >= 10){
			pInfo.paginationNavi.maxPageNo = pInfo.paginationNavi.totalCount / 10;
		}else{
			pInfo.paginationNavi.maxPageNo = 1;
		}
		pInfo.paginationNavi.previous = pInfo.paginationNavi.currentPage > 1;
		pInfo.paginationNavi.next = pInfo.paginationNavi.currentPage < pInfo.paginationNavi.maxPageNo;
		pInfo.paginationNavi.previousPage = pInfo.paginationNavi.currentPage - 1;			
		pInfo.paginationNavi.nextPage = pInfo.paginationNavi.currentPage + pInfo.paginationNavi.paginationSize;
		pInfo.paginationNavi.start = 1;
		pInfo.paginationNavi.end = Math.min(pInfo.paginationNavi.maxPageNo, 5);
		for(L1Clan clan : col){
			if(_joinType == -1) {
				PledgeExtension pledge = new PledgeExtension(clan);
				pInfo.pledgeExtensionList.add(pledge);
			} else if(_joinType == clan.getJoinType()) {
				PledgeExtension pledge = new PledgeExtension(clan);
				pInfo.pledgeExtensionList.add(pledge);
			}
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(pInfo);
		HttpResponse response = create_response(HttpResponseStatus.OK, json);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=UTF-8");
		return response;
	}
	
}
