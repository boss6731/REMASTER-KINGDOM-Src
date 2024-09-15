package l1j.server.MJNetSafeSystem.Distribution;

import l1j.server.MJNetSafeSystem.MJNetSafeLoadManager;
import l1j.server.server.GameClient;
import l1j.server.server.Opcodes;
import l1j.server.server.clientpackets.ClientBasePacket;

import java.util.HashMap;

public enum MJClientStatus {
	CLNT_STS_HANDSHAKE(1, new HandShakeDistributor()),            // 初次連接時 鍵 -> 版本 -> Ping數據包
	CLNT_STS_CONNECTED(2, new ConnectedDistributor()),            // Ping數據包認證後 帳號 -> C_Read_News為止
	CLNT_STS_AUTHLOGIN(3, new SelectCharacterDistributor()),      // 角色選擇介面
	CLNT_STS_ENTERWORLD(4, new WorldDistributor()),               // 進入世界
	CLNT_STS_CHANGENAME(5, new WorldDistributor());               // 角色名變更
	
	private static HashMap<Integer, MJClientStatus> _statuses = new HashMap<Integer, MJClientStatus>();
	static{
		MJClientStatus[] arr = MJClientStatus.values();
		for(MJClientStatus sts : arr)
			_statuses.put(sts.toInt(), sts);
	}
	
	private int 		_status;
	private Distributor _dist;
	MJClientStatus(int i, Distributor dist){
		_status = i;
		_dist	= dist;
	}
	
	public int toInt() {
		return _status;
	}

	public static long TestMillis = 0L;
	public void process(GameClient clnt, byte[] data){
		int op 	= data[0] & 0xff;
		//System.out.println(MJHexHelper.toString(data, data.length));
		int ret = MJNetSafeLoadManager.isFilter(clnt, op);
		if(ret != MJNetSafeLoadManager.RESULT_NORMAL)
			return;
/*
		if(op == Opcodes.C_ATTACK) {
			System.out.println("C_ATTACK");
		}
		if(op == Opcodes.C_ATTACK_CONTINUE) {
			System.out.println("C_ATTACK_CONTINUE");
		}*/
		
		if(op == Opcodes.C_USE_ITEM){
			TestMillis = System.currentTimeMillis();
		}
		ClientBasePacket cbp = null;
		try{
			cbp = _dist.handle(clnt, data, op);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(cbp != null){
			cbp.clear();
			cbp = null;
		}
	}
	
	public static MJClientStatus fromInt(int i){
		return _statuses.get(i);
	}
}
