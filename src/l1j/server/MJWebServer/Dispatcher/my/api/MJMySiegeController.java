package l1j.server.MJWebServer.Dispatcher.my.api;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import l1j.server.MJTemplate.MJString;
import l1j.server.MJWarSystem.MJCastleWar;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.MJWebServer.Dispatcher.my.MJMyJsonModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.api.MJMySiegeModel.SiegeInfo;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.model.L1Clan;

class MJMySiegeController extends MJMyApiController{
	private static final int[] viewCastleIds = new int[]{
		4, 1, 2	
	};
	
	MJMySiegeController(MJHttpRequest request) {
		super(request);
	}

	@Override
	protected boolean isNeedLogin() {
		return true;
	}

	@Override
	protected MJMyModel responseModel() {
		MJMySiegeModel model = new MJMySiegeModel();
		model.code = MJMyApiModel.SUCCESS;
		
		for(int castleId : viewCastleIds){
			SiegeInfo sInfo = new SiegeInfo();
			sInfo.castleId = castleId;
			MJCastleWar war = MJCastleWarBusiness.getInstance().get(castleId);
			if(war != null){
				sInfo.korName = war.getCastleName();
				sInfo.tax = war.getPublicMoney();
				sInfo.powerbookUrl = MJString.concat("https://lineage.plaync.com/powerbook/wiki/", war.getCastleName());
				L1Clan clan = war.getDefenseClan();
				if(clan != null && !clan.getClanName().equalsIgnoreCase("붉은 기사단")){
					sInfo.pledge = clan.getClanName();
					sInfo.pledgeMaster = clan.getLeaderName();
					sInfo.occupyDate = convertSiegeDate(clan.getCastleDate());
					sInfo.siegePoint = clan.getWarPoint();
				}else{
					sInfo.pledge = "붉은 기사단";
					sInfo.pledgeMaster = "데포로주";
					sInfo.occupyDate = "2017.01.01.";
				}
			}
			model.siegeCastleList.add(sInfo);
		}		
		return new MJMyJsonModel(request(), model, null);
	}
	
	private static String convertSiegeDate(Timestamp ts){
		LocalDateTime datetime = ts.toLocalDateTime();
		return String.format("%04d.%02d.%02d.", datetime.getYear(), datetime.getMonthValue(), datetime.getDayOfMonth());
	}
}
