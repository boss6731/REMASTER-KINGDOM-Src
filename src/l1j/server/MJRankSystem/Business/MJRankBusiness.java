package l1j.server.MJRankSystem.Business;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ArrayBlockingQueue;

import l1j.server.Config;
import l1j.server.MJRankSystem.Loader.MJRankLoadManager;
import l1j.server.MJRankSystem.Loader.MJRankUserLoader;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_MY_RANKING_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_TOP_RANKER_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_TOP_RANKER_ACK.ResultCode;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_TOP_RANKER_NOTI;
import l1j.server.MJWebServer.Dispatcher.my.service.rank.MJMyRankService;
import l1j.server.MJWebServer.Dispatcher.my.service.rank.MJMyRankServiceModel;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.BaseTime;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.model.gametime.TimeListener;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SystemMessage;

public class MJRankBusiness implements TimeListener{
	private static MJRankBusiness _instance;
	public static MJRankBusiness getInstance(){
		if(_instance == null)
			_instance = new MJRankBusiness();
		return _instance;
	}

	private boolean _is_update;
	private int _accumulate_second;
	private int _accumulate_class_second;
	private long _expendiant;
	private long _version;
	private ProtoOutputStream _nowNotServiceAck;
	private ProtoOutputStream[][] _acks;
	private SC_TOP_RANKER_ACK[][] _rankInfos;
	private ArrayBlockingQueue<Boolean>	_signal;
	private SC_TOP_RANKER_NOTI	_lastTopRanker;
	private boolean	_isrun;
	private MJRankBusiness(){
		_is_update = false;
		_accumulate_second = 0;
		_accumulate_class_second = 0;
		_signal = new ArrayBlockingQueue<Boolean>(1);
		
		SC_TOP_RANKER_ACK ack = SC_TOP_RANKER_ACK.newInstance();
		ack.set_result_code(ResultCode.RC_NOW_NOT_SERVICE);
		_nowNotServiceAck = ack.writeTo(MJEProtoMessages.SC_TOP_RANKER_ACK);
		ack.dispose();
		
		_acks = new ProtoOutputStream[11][2];
		_rankInfos = new SC_TOP_RANKER_ACK[11][2];
		for(int i=10; i>=0; --i){
			_rankInfos[i][0] = SC_TOP_RANKER_ACK.newInstance(i, 2, 1);
			_rankInfos[i][1] = SC_TOP_RANKER_ACK.newInstance(i, 2, 2);
		}
	}
	
	public boolean is_update(){
		return _is_update;
	}
	
	public void run(){
		_isrun = true;
		GeneralThreadPool.getInstance().execute(new MJRankConsumer());
		_accumulate_second = 0;
		_accumulate_class_second = 0;
		_signal.offer(Boolean.TRUE);
		RealTimeClock.getInstance().addListener(this, Calendar.SECOND);		
	}
	
	public void dispose(){
		_isrun = false;
		RealTimeClock.getInstance().removeListener(this, Calendar.SECOND);
	}
	
	@Override
	public void onMonthChanged(BaseTime time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDayChanged(BaseTime time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHourChanged(BaseTime time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMinuteChanged(BaseTime time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSecondChanged(BaseTime time) {
		if(_isrun && ++_accumulate_second >= MJRankLoadManager.MRK_SYS_UPDATE_CLOCK){
			_accumulate_second = 0;
			_signal.offer(Boolean.TRUE);
			L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(Config.Message.RANK_TIME_UPDATOR));
			L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, Config.Message.RANK_TIME_UPDATOR));
		}
		
		if (_isrun && ++_accumulate_class_second >= MJRankLoadManager.MRK_SYS_UPDATE_CLOCK_CLASS) {
			try {
				MJRankLoadManager.MRK_SYS_ISON = false;
				MJRankBusiness.getInstance().dispose();
				MJRankLoadManager.MRK_SYS_ISON = true;
				MJRankUserLoader.getInstance().offBuff();
				Thread.sleep(1000L);
				MJRankUserLoader.reload();
				MJRankBusiness.getInstance().run();
				Thread.sleep(3000L);
				for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
					if (pc == null || pc.getAI() != null)
						continue;
					MJRankUserLoader.getInstance().onUser(pc);
				}
				L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(Config.Message.RANK_RE_TIME_UPDATOR));
				L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, Config.Message.RANK_RE_TIME_UPDATOR));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void on_update(){
		_accumulate_second = 0;
		_accumulate_class_second = 0;
		_signal.offer(Boolean.TRUE);
	}

	class MJRankConsumer implements Runnable{
		@Override
		public void run() {
			try{
				while(_isrun){
					@SuppressWarnings("unused")
					Boolean signal = _signal.take();
					if(!_isrun)
						return;
					
					_is_update = true;
					initializeRankerInfo();
					doUpdateRank();
					_is_update = false;
					makeStream();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}	
	
	private void initializeRankerInfo(){
		_version = System.currentTimeMillis();
		for(int i=10; i>=0; --i){
			_rankInfos[i][0].clearRankers();
			_rankInfos[i][0].set_version(_version);
			_rankInfos[i][1].clearRankers();
			_rankInfos[i][1].set_version(_version);
		}
	}
	
	private void makeStream(){
		if(!MJRankLoadManager.MRK_SYS_ISON)
			return;
		
		ProtoOutputStream[][] acks = new ProtoOutputStream[11][2];
		for(int i=10; i>=0; --i){
			int totalPage = 1;
			if(_rankInfos[i][1].get_rankers() != null && _rankInfos[i][1].get_rankers().size() > 0){
				totalPage = 2;
				_rankInfos[i][1].set_current_page(2);
				_rankInfos[i][1].set_total_page(2);
				acks[i][1] = _rankInfos[i][1].writeTo(MJEProtoMessages.SC_TOP_RANKER_ACK);
			}
			if(_rankInfos[i][0].get_rankers() != null && _rankInfos[i][0].get_rankers().size() > 0){
				_rankInfos[i][0].set_current_page(1);
				_rankInfos[i][0].set_total_page(totalPage);
				acks[i][0] = _rankInfos[i][0].writeTo(MJEProtoMessages.SC_TOP_RANKER_ACK);
			}
		}
		
		ProtoOutputStream[][] tmp = _acks;
		_acks = acks;
		
		for(int i=10; i>=0; --i){
			for(int j=1; j>=0; --j){
				if(tmp[i][j] != null){
					tmp[i][j].dispose();
					tmp[i][j] = null;
				}
			}
		}
	}
	
	private void doUpdateRank(){
		ArrayList<SC_TOP_RANKER_NOTI> rankers = MJRankUserLoader.getInstance().createSortSnapshot();
		if(rankers == null)
			return;
		
		int[] currentPlace = new int[]{1,1,1,1,1,1,1,1,1,1,1};
		SC_TOP_RANKER_NOTI[] prevRankers = new SC_TOP_RANKER_NOTI[11];
		ArrayList<ArrayList<MJMyRankServiceModel>> models = MJMyRankService.service().allocateModels();
		try{
			int size = rankers.size();
			for(int i=0; i<size; ++i){
				SC_TOP_RANKER_NOTI rnk = rankers.get(i);
				int class_id = rnk.get_class();
				int current_class_place = currentPlace[class_id];
				int current_total_place = currentPlace[10];
				if(!rnk.isInRank()){
					rnk.updatePlace();
					rnk.updateAlmost();
				}else{
					rnk.updatePlace(current_class_place, current_total_place);
					rnk.updateRating(current_class_place, current_total_place);
					SC_TOP_RANKER_NOTI prev_class = prevRankers[class_id];
					SC_TOP_RANKER_NOTI prev_total = prevRankers[10];
					rnk.updateAlmost(prev_class, prev_total);
					
					if(current_class_place <= MJRankLoadManager.MRK_SYS_CLASS_RANGE){
						_rankInfos[class_id][current_class_place > 100 ? 1 : 0].add_rankers(rnk.get_class_ranker());
						prevRankers[class_id] = rnk;

						String name = new String(rnk.get_class_ranker().get_name());
//						if(!MJString.isNullOrEmpty(rnk.get_class_ranker().get_name())){
						if(!MJString.isNullOrEmpty(name)){
//							MJMyRankServiceModel model = makeModel(current_class_place, rnk.get_class_ranker().get_name(), class_id);
							MJMyRankServiceModel model = makeModel(current_class_place, name, class_id);
							models.get(class_id).add(model);
						}

						currentPlace[class_id] = ++current_class_place;

					}
					
					if(current_total_place <= MJRankLoadManager.MRK_SYS_TOTAL_RANGE){
						if(current_total_place == 1)
							_lastTopRanker = rnk;
						
						_rankInfos[10][current_total_place > 100 ? 1 : 0].add_rankers(rnk.get_total_ranker());
						prevRankers[10] = rnk;

						if(current_total_place == MJRankLoadManager.MRK_SYS_RANK_POTION - 1)
							_expendiant = rnk.get_exp();
						
						String name = new String(rnk.get_class_ranker().get_name());
						if(!MJString.isNullOrEmpty(name)){
							MJMyRankServiceModel model = makeModel(current_total_place, name, class_id);
//						if(!MJString.isNullOrEmpty(rnk.get_class_ranker().get_name())){
//							MJMyRankServiceModel model = makeModel(current_total_place, rnk.get_total_ranker().get_name(), class_id);
							models.get(10).add(model);
						}
						currentPlace[10] = ++current_total_place;
						
					}
					
				}
				rnk.buff();
			}
			rankers.clear();
			rankers = null;
		}catch(Exception e){
			e.printStackTrace();
		}
		MJMyRankService.service().onNewRank(models);
		
	}
	
	private MJMyRankServiceModel makeModel(int rank, String name, int classId){
		MJMyRankServiceModel model = new MJMyRankServiceModel();
		model.rank = rank;
		model.name = name;
		model.characterClass = classId;
		return model;
	}
	
	public long getExpendiant(){
		return _expendiant;
	}

	public boolean onExpendiant(L1PcInstance pc) {
		try {
// 檢查玩家對象是否為空
			if(pc == null)
				return false;

// 檢查排名系統是否啟用以及_expendiant是否大於0
			if(_expendiant <= 0 || !MJRankLoadManager.MRK_SYS_ISON) {
				pc.sendPackets("排名系統已被管理者暫停。"); //
				return false;
			}

// 檢查玩家是否為GM（管理員）
			if(pc.isGm()) {
				pc.sendPackets("GM無法使用此道具。"); //
				return false;
			}

// 獲取玩家的排名通知信息
			SC_TOP_RANKER_NOTI noti = MJRankUserLoader.getInstance().get(pc.getId());
			if(noti == null) {
// 如果沒有通知信息，創建新的玩家信息
				noti = MJRankUserLoader.getInstance().create_user_information(pc);
			} else {
// 檢查玩家的總排名是否高於設定的排名藥水等級
				if(noti.get_total_ranker().get_rank() <= MJRankLoadManager.MRK_SYS_RANK_POTION) {
					pc.sendPackets("當前排名高於進入排名。"); //
					return false;
				}
			}

// 設置玩家的經驗值
			pc.set_exp((_expendiant + 5));
			pc.sendPackets(String.format("%s的經驗值已調整為排名第%d的經驗值。", pc.getName(), MJRankLoadManager.MRK_SYS_RANK_POTION)); //
			return true;
		} catch(Exception e) {
// 捕獲異常並打印堆棧跟蹤
			e.printStackTrace();
			pc.sendPackets("排名系統已被管理者暫停。"); //
			return false;
		}
	}
	
	public void noti(GameClient clnt, int objid){
		SC_TOP_RANKER_NOTI noti = MJRankUserLoader.getInstance().get(objid);
		if(noti != null)
			clnt.sendPacket(noti.writeTo(MJEProtoMessages.SC_TOP_RANKER_NOTI), true);
	}
	
	public void ack_private_version(L1PcInstance pc, long version){
		if(!MJRankLoadManager.MRK_SYS_ISON){
			SC_MY_RANKING_ACK.send_not_service(pc);
		}else if(_version == version){
			SC_MY_RANKING_ACK.send(pc);
		}else{
			SC_TOP_RANKER_NOTI noti = MJRankUserLoader.getInstance().getRankNoti(pc);
			if(noti != null){
				SC_MY_RANKING_ACK.send(pc, noti, _version);
			}
		}
	}
	
	public void ack(L1PcInstance pc, int classId, long version){
		if(!MJRankLoadManager.MRK_SYS_ISON)
			pc.sendPackets(_nowNotServiceAck, false);
		else if(version == _version){
			SC_TOP_RANKER_ACK ack = SC_TOP_RANKER_ACK.newInstance(version);
			pc.sendPackets(ack, MJEProtoMessages.SC_TOP_RANKER_ACK, true);
		}else{
			int sending = 0;
			for(int i=0; i<2; ++i){
				ProtoOutputStream stream = _acks[classId][i];
				if(stream != null){
					pc.sendPackets(stream, false);
					++sending;
				}
			}
			if(sending == 0)
				pc.sendPackets(_nowNotServiceAck, false);
		}
	}
	
	public SC_TOP_RANKER_NOTI getLastTopRanker(){
		return _lastTopRanker;
	}
}
