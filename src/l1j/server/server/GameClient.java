package l1j.server.server;

import MJFX.MJFxEntry;
import MJShiftObject.MJEShiftObjectType;
import MJShiftObject.Object.MJShiftObject;
import l1j.server.Config;
import l1j.server.MJNetSafeSystem.Distribution.MJClientStatus;
import l1j.server.MJNetSafeSystem.MJNetSafeLoadManager;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_USER_DATA_EXTEND;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.Login.MJHttpLoginInfo;
import l1j.server.server.Controller.LoginController;
import l1j.server.server.model.Instance.*;
import l1j.server.server.model.L1Trade;
import l1j.server.server.serverpackets.S_CommonNews;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.util.Collection;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


public static class GameClient {
	private static final Timer _observerTimer = new Timer();


	public boolean isAuthPass;
	public int[] charStat;
	private ClientThreadObserver observer;
	private l1j.server.server.Account account;
	private Channel chnnel;
	private l1j.server.server.model.Instance.L1PcInstance activeCharInstance;
	private l1j.server.server.model.Instance.L1PcInstance latestCharacterInstance;
	private boolean close;
	private int chatCount;
	private SendBusiness _business;
	private SendBusinessBlock _businessb;
	private InetAddress _inetAddress;
	private boolean _isLoginRecord;
	private boolean _isUpdate;
	private MJClientStatus _status;
	//	private int _version;
	private long _version;
	private MJHttpLoginInfo m_login_info;
	private String m_latest_accounts;
	public GameClient(Channel channel) {
		m_login_info = null;
		_version = 0;
		isAuthPass = false;
		close = false;
		_isLoginRecord = false;
		_isUpdate = false;
		chnnel = channel;
		charStat = new int[6];
		m_latest_accounts = MJString.EmptyString;
		InetSocketAddress inetAddr = (InetSocketAddress) channel.remoteAddress();
		_inetAddress = inetAddr.getAddress();
		observer = new ClientThreadObserver(Config.Connection.AutomaticKick * 60 * 1000);
		if (Config.Connection.AutomaticKick > 0) {
			observer.start();
		}
		channel.closeFuture().addListener(new ChannelRemoveListener());
	}

	public void latestCharacterInstance(l1j.server.server.model.Instance.L1PcInstance pc) {
		latestCharacterInstance = pc;
	}

	public l1j.server.server.model.Instance.L1PcInstance latestCharacterInstance() {
		return latestCharacterInstance;
	}

	private class ChannelRemoveListener implements ChannelFutureListener{
		@Override
		public <ChannelFuture> void operationComplete(ChannelFuture future) throws Exception {
			if(chnnel == null){
				return;
			}
			chnnel.closeFuture().removeListener(this);
			GameClient.this.close();
			if(MJString.isNullOrEmpty(m_latest_accounts)) {
				LoginController.getInstance().removeClientByAccounts(m_latest_accounts);
			}
		}
	}

	public void set_login_info(MJHttpLoginInfo login_info) {
		m_login_info = login_info;
	}

	public MJHttpLoginInfo get_login_info() {
		return m_login_info;
	}

	public Channel getChannel() {
		return chnnel;
	}

	public void set_version(long version) {
		_version = version;
	}
	public long get_version(){
		return _version;
	}

	public void get_client_close(GameClient gc) {
		new S_CommonNews().UpDate(gc.getAccountName(), "0");
	}

	public void packetwaitgo(byte[] bb) {
		if (bb == null)
			return;
		try {
			_status.process(this, bb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void kick() {
		sendPacket(new S_Disconnect());
		try {
			close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close(long delayMillis) {
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				try {
					close();
				}catch(Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		}, delayMillis);
	}

// TODO: 進行跨伺服器強制延遲測試
//		Thread.sleep(3000);
//      StackTraceElement[] a = new Throwable().getStackTrace();
//      for(int i = a.length - 1; i > 0 ; i--){
//          System.out.print("類別 - " + stackTraceElements[i].getClassName());
//           System.out.print(", 方法 - " + stackTraceElements[i].getMethodName());
//          System.out.print(", 行號 - " + stackTraceElements[i].getLineNumber());
//          System.out.print(", 檔案 - " + stackTraceElements[i].getFileName());
//          System.out.println();
//      }
		if (!close) {
			close = true;
			try {
				if (activeCharInstance != null) {
					quitGame(activeCharInstance);
					synchronized (activeCharInstance) {
						if (!activeCharInstance.isPrivateShop()) {
							activeCharInstance.logout();
						}
						setActiveChar(null);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				LoginController.getInstance().logout(this);
				stopObsever();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (chnnel != null)
					chnnel.close();
			} catch (Exception e) {
			}
		}
	}

	public void setActiveChar(L1PcInstance pc) {
		activeCharInstance = pc;
	}

	public L1PcInstance getActiveChar() {
		return activeCharInstance;
	}

	public void setAccount(Account account) {
		this.account = account;
		if(account != null) {
			m_latest_accounts = account.getName();
		}
	}

	public Account getAccount() {
		return account;
	}

	public String getAccountName() {
		if (account == null) {
			return null;
		}
		String name = account.getName();
		return name;
	}

	public static void quitGame(L1PcInstance pc) {
		pc.remove_companion();
		if (pc.getTradeID() != 0) {
			L1Trade trade = new L1Trade();
			trade.TradeCancel(pc);
		}
		if (pc.isInParty()) {
			pc.getParty().leaveMember(pc);
		}
		if (pc.isInChatParty()) {
			pc.getChatParty().leaveMember(pc);
		}
		Object[] petList = pc.getPetList().values().toArray();
		for (Object petObject : petList) {
			if (petObject instanceof L1PetInstance) {
				L1PetInstance pet = (L1PetInstance) petObject;
				pet.unloadMaster();
			}
			if (petObject instanceof L1SummonInstance) {
				L1SummonInstance summon = (L1SummonInstance) petObject;
				summon.onLeaveMaster();
				/*for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(summon)) {
					visiblePc.sendPackets(new S_SummonPack(summon, visiblePc, false));
				}*/
			}
		}
		L1DollInstance doll = pc.getMagicDoll();
		if (doll != null) {
			doll.deleteDoll();
		}

		Object[] followerList = pc.getFollowerList().values().toArray();
		for (Object followerObject : followerList) {
			L1FollowerInstance follower = (L1FollowerInstance) followerObject;
			follower.setParalyzed(true);
			follower.spawn(follower.getNpcTemplate().get_npcId(), follower.getX(), follower.getY(), follower.getHeading(), follower.getMapId());
			follower.deleteMe();
		}
		pc.stopEtcMonitor();
		pc.setOnlineStatus(0);

		try {
			pc.save();
			pc.saveInventory();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			SC_ATTENDANCE_USER_DATA_EXTEND userData = pc.getAttendanceData();
			if (userData != null) {
				SC_ATTENDANCE_USER_DATA_EXTEND.update(pc.getAccountName(), userData);
				pc.setAttendanceData(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getIp() {
		if (_inetAddress == null)
			return null;

		return _inetAddress.getHostAddress();
	}

	public int getIpBigEndian() {
		if(MJString.isNullOrEmpty(getIp())) {
			return 0;
		}
		int bigendian = 0;
		StringTokenizer tok = new StringTokenizer(getIp());
		for(int i=3; i>=0; --i){
			int bit = i * 8;
			bigendian |= (Integer.parseInt(tok.nextToken(".")) << bit) &  (0xff << bit);
		}
		return bigendian;
	}

	public String getHostname() {
		if (_inetAddress == null)
			return null;

		return _inetAddress.getHostAddress();
	}

	public InetAddress getAddress() {
		return _inetAddress;
	}

	public boolean isConnected() {
		return chnnel != null && chnnel.isActive();
	}

	public void stopObsever() {
		observer.cancel();
	}

	public boolean isClosed() {
		if (!chnnel.isActive())
			return true;
		else {
			return false;
		}
	}

	class ClientThreadObserver extends TimerTask {
		private int _checkct = 1;

		private final int _disconnectTimeMillis;

		public ClientThreadObserver(int disconnectTimeMillis) {
			_disconnectTimeMillis = disconnectTimeMillis;
		}

		public void start() {
			_observerTimer.scheduleAtFixedRate(this, _disconnectTimeMillis, _disconnectTimeMillis);
		}

		@Override
		public void run() {
			try {
				long ns_client_version = Long.parseLong(Integer.toUnsignedString(MJNetSafeLoadManager.NS_CLIENT_VERSION));
				if (_version != ns_client_version) {
					sendPacket(new S_ServerMessage(3362));
					sendPacket(new S_SystemMessage("伺服器版本已更新。請重新啟動連接器，並務必重新下載本服補丁。"));
//					System.out.println(_version+"+"+ns_client_version);
				}

				if (MJFxEntry.IS_DEBUG_MODE)
					return null;

				if (!chnnel.isActive()) {
					cancel();
					return null;
				}
				if (_checkct > 0) {
					_checkct = 0;
					return null;
				}
				if (GameClient.this.getStatus().toInt() != MJClientStatus.CLNT_STS_ENTERWORLD.toInt()) {
					close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				cancel();
			}
			return null;
		}

		public void packetReceived() {
			++_checkct;
		}
	}

	public void handle(byte[] data) {
		observer.packetReceived();
		_status.process(this, data);
	}

	public void sendPacketNonClear(ServerBasePacket bp){
		if (Config.Connection.SendBusiness) {
			if (_businessb == null) {
				_businessb = new SendBusinessBlock();
				GeneralThreadPool.getInstance().execute(_businessb);
			}
			_businessb.in(bp.getBytes());
		} else {
			if (_business == null) {
				_business = new SendBusiness();
				GeneralThreadPool.getInstance().execute(_business);
			}
			_business.in(bp.getBytes());
		}
	}

	public void sendPacket(ServerBasePacket bp, boolean isClear) {
		if (Config.Connection.SendBusiness) {
			if (_businessb == null) {
				_businessb = new SendBusinessBlock();
				GeneralThreadPool.getInstance().execute(_businessb);
			}
			_businessb.in(bp.getBytes());
		} else {
			if (_business == null) {
				_business = new SendBusiness();
				GeneralThreadPool.getInstance().execute(_business);
			}
			_business.in(bp.getBytes());
		}
		if (isClear)
			bp.clear();
	}

	public void sendPacket(ServerBasePacket bp) {
		sendPacket(bp, true);
	}

	public void sendPacket(MJIProtoMessage message, int messageId) {
		sendPacket(message, messageId, true);
	}

	public void sendPacket(MJIProtoMessage message, int messageId, boolean isClear) {
		if (message.isInitialized()) {
			sendPacket(message.writeTo(MJEProtoMessages.fromInt(messageId)), isClear);
			if (isClear)
				message.dispose();
		} else {
			MJEProtoMessages.printNotInitialized(getActiveChar() == null ? getIp() : getActiveChar().getName(),
					messageId, message.getInitializeBit());
		}
	}

	public void sendPacket(ProtoOutputStream stream) {
		sendPacket(stream, true);
	}

	public void sendPacket(ProtoOutputStream stream, boolean isClear){
		if (Config.Connection.SendBusiness) {
			if (_businessb == null) {
				_businessb = new SendBusinessBlock();
				GeneralThreadPool.getInstance().execute(_businessb);
			}
			if(!stream.isCreated())
				stream.createProtoBytes();
			_businessb.in(stream.getProtoBytes());
		} else {
			if (_business == null) {
				_business = new SendBusiness();
				GeneralThreadPool.getInstance().execute(_business);
			}
			if(!stream.isCreated())
				stream.createProtoBytes();
			_business.in(stream.getProtoBytes());
		}

		if(isClear) stream.dispose();
	}

	public void directSendPacket(java.util.Collection<ProtoOutputStream> col){
		for(ProtoOutputStream stream : col){
			if(!stream.isCreated())
				stream.createProtoBytes();
			if (Config.Connection.SendBusiness) {
				_businessb.in(stream.getProtoBytes());
			} else {
				_business.in(stream.getProtoBytes());
			}
		}
	}

	public void directSendPacket(Collection<ProtoOutputStream> col, long delay_millis) throws InterruptedException {
		for (ProtoOutputStream stream : col) {
			if (!stream.isCreated())
				stream.createProtoBytes();
			if (Config.Connection.SendBusiness) {
				_businessb.in(stream.getProtoBytes());
			} else {
				_business.in(stream.getProtoBytes());
			}
			if (delay_millis > 0L)
				Thread.sleep(delay_millis);
		}
	}

/**
 * 原文
 * TODO 模型方式：100個一起聚集後一次性發送封包。在接收線程中接收封包（可能已設置為4個線程核心）。
 * 在4個線程中處理所有用戶的接收（接收）操作。
 * 在接收的同時，直接在接收的封包中進行邏輯處理，然後立即發送。
 * 18.09.20 邏輯修改
 * 當前以 1, 2, 3, 6 隨機的方式獲取隊列值。
 * 當前邏輯下，隊列值不可能全部滿（正常）。
 **/
	class SendBusiness implements Runnable {
		private ArrayBlockingQueue<byte[]> _workQ;

		SendBusiness() {
			_workQ = new ArrayBlockingQueue<byte[]>(256);
		}

		public void in(byte[] data) {
			if (data != null) {
				// TODO: 2月14日之後，若不添加以下代碼，可能會出現無法進入世界的現象。推測以下代碼可能是解決客戶端錯誤的原因。
				if ((data[0] & 0xff) == 0xb4 && (data[1] & 0xff) == 0x09 && (data[2] & 0xff) == 0x02) {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					//System.out.println("reveal");
					//new Throwable().printStackTrace();
				}
//	            if(data.length <= 0) {
//	            	System.out.println(data.length);
//	                return;
//	             }
				/*if((data[0] & 0xff) == Opcodes.S_ATTACK) {
					new Throwable().printStackTrace();
				}*/
				//System.out.println(String.format("%02X %02X %02X", data[0], data[1], data[2]));

				//gm.sendPackets(new S_ServerMessage(403, "$5240 (1000)")); // %0處理
				//System.out.println(MJHexHelper.toString(data, data.length));
				// TODO: 錯誤輸出測試
				/*try {
					throw new Exception(data[0] + "" + data[1]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				 */
				/*
				if(GameClient.this.activeCharInstance != null && GameClient.this.activeCharInstance.attackWait()) {
					System.out.println(MJHexHelper.toString(data, data.length));
				}*/


				//if ((data[0] & 0xff) == Opcodes.S_VOICE_CHAT) {
				//new Throwable().printStackTrace();
				//System.out.println(MJHexHelper.toString(data, data.length));
				//}
				//new Throwable().printStackTrace();
				//System.out.println(MJHexHelper.toString(data, data.length));
				_workQ.offer(data);
			}
		}

		@Override
		public void run() {
			int write_count = 0;
			while (chnnel.isActive()) {
				boolean isFlush = false;
				try {
					byte[] data = _workQ.poll(3000L, TimeUnit.MILLISECONDS);
					if (data != null && chnnel.isWritable()) {
						if (_workQ.size() <= 0) {
							chnnel.writeAndFlush(data);
							isFlush = true;
						} else {
							/**
							 * TODO: 適當處理計數器(write_count)！
							 * 基本值：100
							 **/
							if (++write_count >= 100) {
								chnnel.writeAndFlush(data);
								write_count = 0;
							} else {
								chnnel.write(data);
							}
						}
					}
				} catch (InterruptedException e) {
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (chnnel != null && chnnel.isActive() && !isFlush)
					chnnel.flush();
				write_count = 0;
			}
			_workQ.clear();
			return null;
		}
	}

	class SendBusinessBlock implements Runnable {
		private final BlockingQueue<byte[]> _workQ;

		public SendBusinessBlock() {
			_workQ = new LinkedBlockingQueue<byte[]>();
		}

		public SendBusinessBlock(int i) {
			_workQ = new LinkedBlockingQueue<byte[]>(i);
		}

		public void in(byte[] data) {
			if(data != null){

//				System.out.printf("[Server] opcode:%d, type:%d, size:%d\r\n%s\r\n", data[0] & 0xff,
//						data[1] & 0xff, data.length, DataToPacket(data, data.length));

				// XXX: 20-12-12 如果不加註釋會出錯
//				if((data[0] & 0xff) == 0xb4 &&
//						(data[1] & 0xff) == 0x09 &&
//						(data[2] & 0xff) == 0x02
//						) {
//					try {
//						Thread.sleep(50);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}

				// TODO: 錯誤輸出測試
				/*try {
					throw new Exception(data[0] + "" + data[1]);
				} catch (Exception e) {
					e.printStackTrace();
				}*/

				/*if ((data[0] & 0xff) == Opcodes.S_ATTACK_MANY) {
					System.out.println(MJHexHelper.toString(data, data.length));
				}*/
				//System.out.println(MJHexHelper.toString(data, data.length));
				_workQ.offer(data);
			}
		}

		public void run() {
			boolean isFlush = false;
			while (chnnel.isActive())
				try {
					byte[] data = _workQ.poll(3000L, TimeUnit.MILLISECONDS);
					if ((data != null) && chnnel.isActive())
						if(_workQ.size() <= 0) {
							chnnel.writeAndFlush(data);
							isFlush = true;
						} else {
							chnnel.write(data);
						}
					if (chnnel != null && chnnel.isActive() && !isFlush)
						chnnel.flush();
				} catch (Exception e) {
					e.printStackTrace();
				}
			_workQ.clear();
			return null;
		}
	}

	private String HexToDex(int data, int digits) {
		String number = Integer.toHexString(data);
		for (int i = number.length(); i < digits; i++)
			number = "0" + number;
		return number;
	}

	public String DataToPacket(byte[] data, int len) {
		StringBuffer result = new StringBuffer();
		int counter = 0;
		for (int i = 0; i < len; i++) {
			if (counter % 16 == 0) {
				result.append(HexToDex(i, 4) + ": ");
			}
			result.append(HexToDex(data[i] & 0xFF, 2) + " ");
			counter++;
			if (counter == 16) {
				result.append("   ");
				int charpoint = i - 15;
				for (int a = 0; a < 16; a++) {
					int t1 = data[(charpoint++)];
					if ((t1 > 31) && (t1 < 128))
						result.append((char) t1);
					else {
						result.append('.');
					}
				}
				result.append("\n");
				counter = 0;
			}
		}
		int rest = data.length % 16;
		if (rest > 0) {
			for (int i = 0; i < 17 - rest; i++) {
				result.append("   ");
			}
			int charpoint = data.length - rest;
			for (int a = 0; a < rest; a++) {
				int t1 = data[(charpoint++)];
				if ((t1 > 31) && (t1 < 128))
					result.append((char) t1);
				else {
					result.append('.');
				}
			}
			result.append("\n");
		}
		return result.toString();
	}


//	class SendBusiness implements Runnable {
//		private ArrayBlockingQueue<byte[]> _workQ;
//
//		SendBusiness() {
//			_workQ = new ArrayBlockingQueue<byte[]>(256);
//		}
//
//		public void in(byte[] data) {
//			if (data != null) {
				/*try {
					throw new Exception(String.format("%02X", data[0] & 0xff));
				} catch (Exception e) {
					e.printStackTrace();
				}
				int op = data[0] & 0xff;*/
// 		if() {
//				System.out.println(String.format("%02X", op));
//		 return;
// 		}
//TODO: 錯誤輸出測試
				/*try {
					throw new Exception(data[0] + "" + data[1]);
				} catch (Exception e) {
					e.printStackTrace();
				}*/

				/*if ((data[0] & 0xff) == Opcodes.S_EVENT && (data[1] & 0xff) == 0x14) {
					System.out.println(MJHexHelper.toString(data, data.length));
				}
				// TODO: 測試正確的尺寸值
				if(_workQ.size() >= 200){
		               try{
		                  throw new Exception(_workQ.size() + "");
		               }catch(Exception e){
		                  e.printStackTrace();
		               }
		            }*/
//				_workQ.offer(data);
//			}
//		}

//		@Override
//	      public void run() {
//	         int write_count = 0;
//	         while (chnnel.isActive()) {
//	            boolean isFlush = false;
//	            try {
//	               byte[] data = _workQ.poll(3000L, TimeUnit.MILLISECONDS);
//	               if (data != null && chnnel.isWritable()) {
//	                  if (_workQ.size() <= 0) {
//	                     chnnel.writeAndFlush(data);
//	                     isFlush = true;
//	                  } else {
//	                     if(++write_count >= 100){ // 적당히 카운팅
//	                        chnnel.writeAndFlush(data);
//	                        write_count = 0;
//	                     }else{
//	                        chnnel.write(data);                        
//	                     }
//	                  }
//	               }
//	            } catch (InterruptedException e) {
//	            } catch (Exception e) {
//	               e.printStackTrace();
//	            }
//	            if (chnnel != null && chnnel.isActive() && !isFlush)
//	               chnnel.flush();
//	            write_count = 0;
//	         }
//	         _workQ.clear();
//	      }
//	}

	public int getChatCount() {
		return chatCount;
	}

	public void setChatCount(int i) {
		chatCount = i;
	}

	public void setLoginRecord(boolean b) {
		_isLoginRecord = b;
	}

	public boolean isLoginRecord() {
		return _isLoginRecord;
	}

	/** MJCSWSystem **/
	public boolean isUpdate() {
		return _isUpdate;
	}

	public void setUpdate(boolean b) {
		_isUpdate = b;
	}

	public MJClientStatus getStatus() {
		return _status;
	}

	public boolean is_non_handshake() {
		return _status != null && _status.toInt() != MJClientStatus.CLNT_STS_HANDSHAKE.toInt();
	}

	public void setStatus(MJClientStatus sts) {
		if (sts.toInt() == MJClientStatus.CLNT_STS_AUTHLOGIN.toInt()) {
			if (_status.toInt() == MJClientStatus.CLNT_STS_ENTERWORLD.toInt()) {
				try {
					throw new Exception();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		_status = sts;
	}

	public void setStatus2(MJClientStatus sts) {
		_status = sts;
	}

	private MJEShiftObjectType m_shift_type = MJEShiftObjectType.NONE;
	private String m_server_identity = MJString.EmptyString;
	private String m_server_description = MJString.EmptyString;

	public MJEShiftObjectType get_shift_type() {
		return m_shift_type;
	}

	public void set_shift_type(MJEShiftObjectType shift_type) {
		m_shift_type = shift_type;
	}

	public boolean is_shift_client() {
		return !m_shift_type.equals(MJEShiftObjectType.NONE);
	}

	public boolean is_shift_transfer() {
		return m_shift_type.equals(MJEShiftObjectType.TRANSFER);
	}

	public boolean is_shift_battle() {
		return m_shift_type.equals(MJEShiftObjectType.BATTLE);
	}

	public String get_server_identity() {
		return m_server_identity;
	}

	public void set_server_identity(String server_identity) {
		m_server_identity = server_identity;
	}

	public void set_server_description(String server_description) {
		m_server_description = server_description;
	}

	public String get_server_description() {
		return m_server_description;
	}

	private int m_second_password_failure_count = 0;

	public int get_second_password_failure_count() {
		return m_second_password_failure_count;
	}

	public int inc_second_password_failure_count() {
		return ++m_second_password_failure_count;
	}

	public void reset_second_password_failure_count() {
		m_second_password_failure_count = 0;
	}

	private MJShiftObject m_shift_object;

	public MJShiftObject get_shift_object() {
		return m_shift_object;
	}

	public void set_shift_object(MJShiftObject sobject) {
		m_shift_object = sobject;
	}

	private long latestRestartMillis = 0L;
	public long latestRestartMillis() {
		return latestRestartMillis;
	}
	public void latestRestartMillis(long latestRestartMillis) {
		this.latestRestartMillis = latestRestartMillis;
	}

	private String _auth_token = MJString.EmptyString;

	public String get_Auth_Token() {
		return _auth_token;
	}

	public void set_Auth_Token(String account) {
		_auth_token = account;
	}
}

public void main() {
}