package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import MJShiftObject.MJEShiftObjectType;
import MJShiftObject.MJShiftObjectHelper;
import MJShiftObject.MJShiftObjectManager;
import MJShiftObject.Template.CommonServerInfo;
import l1j.server.Config;
import l1j.server.IndunSystem.MiniGame.L1Gambling;
import l1j.server.IndunSystem.MiniGame.L1Gambling3;
import l1j.server.MJNetSafeSystem.Distribution.MJClientStatus;
import l1j.server.MJSurveySystem.MJSurveyFactory;
import l1j.server.MJSurveySystem.MJSurveySystemLoader;
import l1j.server.MJTemplate.MJL1Type;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.Chain.Chat.MJNormalChatFilterChain;
import l1j.server.MJTemplate.Chain.Chat.MJWhisperChatFilterChain;
import l1j.server.MJTemplate.Chain.Chat.MJWorldChatFilterChain;
import l1j.server.MJTemplate.MJProto.MainServer_Client_BuilderCommand.SC_MSG_ANNOUNCE;
import l1j.server.MJWebServer.Dispatcher.my.service.chat.MJMyChatService;
import l1j.server.lotto.lotto_system;
import l1j.server.server.GMCommands;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.UserCommands;
import l1j.server.server.Controller.LoginController;
import l1j.server.server.datatables.BuddyTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.SpamTable;
import l1j.server.server.model.L1Buddy;
import l1j.server.server.model.L1ChatParty;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1ExcludingList;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.monitor.Logger.LoggerChatType;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.S_ChangeCharName;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_DisplayEffect;
import l1j.server.server.serverpackets.S_LoginResult;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.MJCommons;

// TODO : �ڵ����� ������ PROTO �ڵ��Դϴ�. made by MJSoft.
public class CS_CHAT_REQ_PACKET implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_CHAT_REQ_PACKET newInstance(){
		return new CS_CHAT_REQ_PACKET();
	}
	private int _transaction_id;
	private ChatType _type;
	private byte[] _message;
	private byte[] _target_user_name;
	private int _target_user_server_no;
	private byte[] _link_message;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CS_CHAT_REQ_PACKET(){
	}
	public int get_transaction_id(){
		return _transaction_id;
	}
	public void set_transaction_id(int val){
		_bit |= 0x1;
		_transaction_id = val;
	}
	public boolean has_transaction_id(){
		return (_bit & 0x1) == 0x1;
	}
	public ChatType get_type(){
		return _type;
	}
	public void set_type(ChatType val){
		_bit |= 0x2;
		_type = val;
	}
	public boolean has_type(){
		return (_bit & 0x2) == 0x2;
	}
	public byte[] get_message(){
		return _message;
	}
	public void set_message(byte[] val){
		_bit |= 0x4;
		_message = val;
	}
	public boolean has_message(){
		return (_bit & 0x4) == 0x4;
	}
	public byte[] get_target_user_name(){
		return _target_user_name;
	}
	public void set_target_user_name(byte[] val){
		_bit |= 0x10;
		_target_user_name = val;
	}
	public boolean has_target_user_name(){
		return (_bit & 0x10) == 0x10;
	}
	public int get_target_user_server_no(){
		return _target_user_server_no;
	}
	public void set_target_user_server_no(int val){
		_bit |= 0x20;
		_target_user_server_no = val;
	}
	public boolean has_target_user_server_no(){
		return (_bit & 0x20) == 0x20;
	}
	public byte[] get_link_message(){
		return _link_message;
	}
	public void set_link_message(byte[] val){
		_bit |= 0x40;
		_link_message = val;
	}
	public boolean has_link_message(){
		return (_bit & 0x40) == 0x40;
	}
	@Override
	public long getInitializeBit(){
		return (long)_bit;
	}
	@Override
	public int getMemorizedSerializeSizedSize(){
		return _memorizedSerializedSize;
	}
	@Override
	public int getSerializedSize(){
		int size = 0;
		if (has_transaction_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _transaction_id);
		}
		if (has_type()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(2, _type.toInt());
		}
		if (has_message()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(3, _message);
		}
		if (has_target_user_name()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(5, _target_user_name);
		}
		if (has_target_user_server_no()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(6, _target_user_server_no);
		}
		if (has_link_message()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(7, _link_message);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_transaction_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_type()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_message()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_transaction_id()){
			output.writeUInt32(1, _transaction_id);
		}
		if (has_type()){
			output.writeEnum(2, _type.toInt());
		}
		if (has_message()){
			output.writeBytes(3, _message);
		}
		if (has_target_user_name()){
			output.writeBytes(5, _target_user_name);
		}
		if (has_target_user_server_no()){
			output.writeUInt32(6, _target_user_server_no);
		}
		if (has_link_message()){
			output.writeBytes(7, _link_message);
		}
	}
	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = 
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.newInstance(getSerializedSize() + l1j.server.MJTemplate.MJProto.WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
		try{
			writeTo(stream);
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		return stream;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x00000008:{
					set_transaction_id(input.readUInt32());
					break;
				}
				case 0x00000010:{
					set_type(ChatType.fromInt(input.readEnum()));
					break;
				}
				case 0x0000001A:{
					set_message(input.readBytes());
					break;
				}
				case 0x0000002A:{
					set_target_user_name(input.readBytes());
					break;
				}
				case 0x00000030:{
					set_target_user_server_no(input.readUInt32());
					break;
				}
				case 0x0000003A:{
					set_link_message(input.readBytes());
					break;
				}
				default:{
					return this;
				}
			}
		}
		return this;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes){
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE, ((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try{
			readFrom(is);

			if (!isInitialized())
				return this;

			l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
			if (pc == null){
				return this;
			}
			
			if (get_message() == null) {
				set_message("".getBytes());
			}
			
			String message = new String(get_message());
			
			if(pc.is_ready_server_shift()){
				do_shift_server(pc, message);
				pc.set_ready_server_shift(false);
				return this;
			}

			if (pc.hasSkillEffect(L1SkillId.SILENCE) || pc.hasSkillEffect(L1SkillId.AREA_OF_SILENCE) || pc.hasSkillEffect(L1SkillId.CONFUSION)
					|| pc.hasSkillEffect(L1SkillId.STATUS_POISON_SILENCE) || pc.hasSkillEffect(L1SkillId.STATUS_CHAT_PROHIBITED)) {
				return this;
			}

			if (pc.getMapId() == 631 && !pc.isGm()) {
				pc.sendPackets(new S_ServerMessage(912)); // ä���� �� �� �����ϴ�.
				return this;
			}

			if (pc.isDeathMatch() && !pc.isGm() && !pc.isGhost()) {
				pc.sendPackets(new S_SystemMessage("������ġ ����߿��� ä���� �����˴ϴ�.")); // ����ä�� �������Դϴ�.
				return this;
			}
			
			if (!pc.isGm() && pc.getMapId() == 5153) {
				if (get_type() != ChatType.CHAT_NORMAL) {
					pc.sendPackets(new S_SystemMessage("�����̾� ��Ʋ�� �����߿��� �Ϲ�ä�ø� �����մϴ�."));
					return this;
				}
			}

			//TODO GM���� ���� ������ ��Ÿ��Ŷ������. ©���� �������
			if (message.startsWith(".") && (pc.getAccessLevel() == Config.ServerAdSetting.GMCODE || pc.isMonitor())) {
				final String cmd = message.substring(1);
				GeneralThreadPool.getInstance().execute(new Runnable() {
					@Override
					public void run() {
						GMCommands.getInstance().handleCommands(pc, cmd);
                        return new L1PcInstance[0];
                    }
				});
				return this;
			}
			
			if (!pc.isGm()) {
				pc.checkChatInterval();
			}
			
			switch(get_type()) {
			case CHAT_NORMAL: // �Ϲ� ä��
				byte[] username = get_target_user_name();
				
				if (Config.Login.UseShiftServer && pc.is_shift_transfer()) {
					if (!MJCommons.isLetterOrDigitString(message, 5, 12)) {
						pc.sendPackets(String.format("%s(��)�� ����� �� ���� �����Դϴ�.", message));
						return this;
					}

					Account account = Account.load(message);
					if (account != null) {
						pc.sendPackets(String.format("%s(��)�� �̹� �����ϴ� �����Դϴ�.", message));
						return this;
					}
					account = pc.getAccount();
					LoginController.getInstance().logout(clnt);
					clnt.setStatus(MJClientStatus.CLNT_STS_ENTERWORLD);
					MJShiftObjectHelper.update_account_name(pc.getAccount(), message);
					MJShiftObjectHelper.update_account_name(pc, message);
					pc.setAccountName(message);
					account.setName(message);
					pc.getNetConnection().setAccount(account);
					try {
						LoginController.getInstance().login(pc.getNetConnection(), pc.getAccount());
					} catch (Exception e) {
						e.printStackTrace();
					}
					pc.sendPackets(String.format("%s(��)�� ������ ����Ǿ����ϴ�.", message));
					pc.start_teleportForGM(33443 + ((MJRnd.isBoolean() ? -1 : 1) * MJRnd.next(4)), 32797 + ((MJRnd.isBoolean() ? -1 : 1) * MJRnd.next(4)), 4, pc.getHeading(), 18339, true, true);
					int locx = 32723 + CommonUtil.random(10);
					int locy = 32851 + CommonUtil.random(10);
					pc.start_teleport(locx, locy, 5166, 5, 18339, false);
					pc.sendPackets(new S_DisplayEffect(S_DisplayEffect.BLACK_DISPLAY));
					pc.sendPackets(new S_Paralysis(7, true));
					pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "ĳ���� ����â���� �̵��մϴ� ��ø� ��ٷ� �ּ���..."));
					GeneralThreadPool.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							pc.getNetConnection().setStatus(MJClientStatus.CLNT_STS_CHANGENAME);
							pc.sendPackets(S_ChangeCharName.getChangedStart());
							int[] loc = null;
							loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_GIRAN);
							pc.start_teleport(loc[0], loc[1], loc[2], pc.getHeading(), 18339, false);
                            return new L1PcInstance[0];
                        }
					}, 2500L);
					return this;
				}
				
				if (pc.isGhost() && !(pc.isGm() || pc.isMonitor())) {
					return this;
				}
				
				//TODO �������� ���� ������ ��Ÿ��Ŷ������. ©���� �������
				if (message.startsWith(".")) {
					final String cmd = message.substring(1);
					GeneralThreadPool.getInstance().execute(new Runnable() {
						@Override
						public void run() {
							UserCommands.getInstance().handleCommands(pc, cmd);
                            return new L1PcInstance[0];
                        }
					});
					return this;
				}
				
				if(MJNormalChatFilterChain.getInstance().handle(pc, message)){
					return this;
				}

				int temporaryItemObjectId = pc.getTemporaryItemObjectId();
				if (temporaryItemObjectId > 0) {
					L1Object obj = L1World.getInstance().findObject(temporaryItemObjectId);
					if (obj.instanceOf(MJL1Type.L1TYPE_ITEMINSTANCE)) {
						L1ItemInstance item = (L1ItemInstance) obj;
						int itemId = item.getItemId();
						if (itemId == 700085 || itemId == 700086) {
							if (MJSurveyFactory.isMegaphoneSpeaking) {
								pc.sendPackets("�̹� Ȯ���� �޽����� ����ǰ� �ֽ��ϴ�. ��� �� �ٽ� �̿����ּ���.");
								pc.clearTemporaryItemObjectId();
								return this;
							}

							pc.sendPackets(MJSurveySystemLoader.getInstance().registerSurvey(
									String.format("�Է��Ͻ� ���ڰ� : \"%s\"�Դϴ�. �̴�� �����Ͻðڽ��ϱ�?", message), temporaryItemObjectId,
									MJSurveyFactory.createMegaphoneSurvey(temporaryItemObjectId, message,
											itemId == 700085 ? 20 : 40),
									10000L));

							pc.clearTemporaryItemObjectId();
							return this;
						}
					}
				}
				
				L1Gambling gam = new L1Gambling();
				if (pc.isGambling()) {
					if (message.startsWith("Ȧ")) {
						gam.Gambling2(pc, message, 1);
						return this;
					} else if (message.startsWith("¦")) {
						gam.Gambling2(pc, message, 2);
						return this;
					} else if (message.startsWith("1")) {
						gam.Gambling2(pc, message, 3);
						return this;
					} else if (message.startsWith("2")) {
						gam.Gambling2(pc, message, 4);
						return this;
					} else if (message.startsWith("3")) {
						gam.Gambling2(pc, message, 5);
						return this;
					} else if (message.startsWith("4")) {
						gam.Gambling2(pc, message, 6);
						return this;
					} else if (message.startsWith("5")) {
						gam.Gambling2(pc, message, 7);
						return this;
					} else if (message.startsWith("6")) {
						gam.Gambling2(pc, message, 8);
						return this;
					}
				}
				if (pc.isGambling3()) {
					L1Gambling3 gam1 = new L1Gambling3();
					if (message.startsWith("��ũ����")) {
						gam1.Gambling3(pc, message, 1);
						return this;
					} else if (message.startsWith("��������")) {
						gam1.Gambling3(pc, message, 2);
						return this;
					} else if (message.startsWith("�����")) {
						gam1.Gambling3(pc, message, 3);
						return this;
					} else if (message.startsWith("������")) {
						gam1.Gambling3(pc, message, 4);
						return this;
					} else if (message.startsWith("�ذ�")) {
						gam1.Gambling3(pc, message, 5);
						return this;
					} else if (message.startsWith("�����ΰ�")) {
						gam1.Gambling3(pc, message, 6);
						return this;
					} else if (message.startsWith("���׺���")) {
						gam1.Gambling3(pc, message, 7);
						return this;
					} else if (message.startsWith("���")) {
						gam1.Gambling3(pc, message, 8);
						return this;
					} else if (message.startsWith("������")) {
						gam1.Gambling3(pc, message, 9);
						return this;
					}
				}
				
				if (pc._ClassChange) {
					if (pc.getInventory().checkItem(849, 1)){	
						if (message.startsWith("����")){		
							pc._ClassChange = false;
							createNewItem(pc, 51093,  1, 1);
//							createNewItem(pc, 844,  12, 1); 
//							createNewItem(pc, 845,  8, 1);
//							createNewItem(pc, 846,  1, 1);
							pc.getInventory().consumeItem(849, 1);
							return this;
						} else if (message.startsWith("���")){	
							pc._ClassChange = false;
							createNewItem(pc, 51094,  1, 1); 
//							createNewItem(pc, 844,  12, 1); 
//							createNewItem(pc, 845,  8, 1);
//							createNewItem(pc, 846,  1, 1);
							pc.getInventory().consumeItem(849, 1);
							return this;
						} else if (message.startsWith("����")){
							pc._ClassChange = false;
							createNewItem(pc, 51100,  1, 1); 
//							createNewItem(pc, 844,  11, 1); 
//							createNewItem(pc, 845,  8, 1);
//							createNewItem(pc, 846,  2, 1);
							pc.getInventory().consumeItem(849, 1);
							return this;
						} else if (message.startsWith("����")){		
							pc._ClassChange = false;
							createNewItem(pc, 51098,  1, 1); 
	//						createNewItem(pc, 844,  12, 1); 
	//						createNewItem(pc, 845,  8, 1);
	//						createNewItem(pc, 846,  1, 1);
							pc.getInventory().consumeItem(849, 1);
							return this;
						} else if (message.startsWith("��ũ����")){	
							pc._ClassChange = false;
							createNewItem(pc, 51097,  1, 1); 
	//						createNewItem(pc, 844,  12, 1); 
	//						createNewItem(pc, 845,  8, 1);
	//						createNewItem(pc, 846,  1, 1);
							pc.getInventory().consumeItem(849, 1);
							return this;
						} else if (message.startsWith("����")){		
							pc._ClassChange = false;
							createNewItem(pc, 51095,  1, 1); 
//							createNewItem(pc, 844,  12, 1); 
//							createNewItem(pc, 845,  8, 1);
//							createNewItem(pc, 846,  1, 1);
							pc.getInventory().consumeItem(849, 1);
							return this;
						} else if (message.startsWith("������")){		
							pc._ClassChange = false;
							createNewItem(pc, 51096,  1, 1); 
//							createNewItem(pc, 844,  12, 1); 
//							createNewItem(pc, 845,  8, 1);
//							createNewItem(pc, 846,  1, 1);
							pc.getInventory().consumeItem(849, 1);
							return this;
						} else if (message.startsWith("ȯ����")){	
							pc._ClassChange = false;
							createNewItem(pc, 51099,  1, 1); 
//							createNewItem(pc, 844,  12, 1); 
//							createNewItem(pc, 845,  8, 1);
//							createNewItem(pc, 846,  1, 1);
							pc.getInventory().consumeItem(849, 1);
							return this;
						} else if (message.startsWith("�˻�")){	
							pc._ClassChange = false;
							createNewItem(pc, 51102,  1, 1); 
//							createNewItem(pc, 844,  12, 1); 
//							createNewItem(pc, 845,  8, 1);
//							createNewItem(pc, 846,  1, 1);
							pc.getInventory().consumeItem(849, 1);
							return this;
						} else if (message.startsWith("â���")){	
							pc._ClassChange = false;
							createNewItem(pc, 51103,  1, 1); 
//							createNewItem(pc, 844,  12, 1); 
//							createNewItem(pc, 845,  8, 1);
//							createNewItem(pc, 846,  1, 1);
							pc.getInventory().consumeItem(849, 1);
							return this;
						} else {
							pc._ClassChange = false;
							pc.sendPackets("Ŭ������ �߸� ���� �ϼ̽��ϴ�.");
							return this;
						}
					}
				}
				if (pc._LottoSelect) {
					/*if (pc.getInventory().checkItem(L1ItemId.ADENA, 100000000)){
						pc.getInventory().consumeItem(L1ItemId.ADENA, 100000000);
					} else {
						pc._LottoSelect = false;
						return this;

					}*/
					if (message.startsWith("�ڵ�")) {
						int[] lotto = new int[2];
						
						lotto[0] = (int)(Math.random()*15 + 1);
						lotto[1] = (int)(Math.random()*15 + 1);
						while(true) {
							if (lotto[1] == lotto[0]) {
								lotto[1] = (int)(Math.random()*15+1);
							} else {
								break;
							}
						}
						
	/*					for (int i = 1; i<3;i++) {
							lotto[i] = (int)(Math.random()*15+1);
							
							for (int j = 0; j < i; j++) {
								while(true) {
									if (lotto[i] == lotto[j]) {
										lotto[i] = (int)(Math.random()*15+1);
									} else {
										break;
									}
								}
							}
						}*/
						Arrays.sort(lotto);
//						pc.sendPackets("�ڵ� ����: "+lotto[0]+", "+lotto[1]+" �Դϴ�");
						lotto_system.getInstance().addcharlotto(pc, lotto[0], lotto[1]);
//						lotto_character_loader.getInstance().updateLotto(pc, round, lotto[0], lotto[1], lotto[2]);
					} else {
						String str = message;
						String srtnospace = str.replaceAll(" ", "");
						String[] array = srtnospace.split(",");
//						System.out.println(array[0]+", "+array[1]);
						boolean over = false;
						
						if (array.length < 2 ) {
							pc.sendPackets("�ζǿ� ���õ� ���ڰ� �ʹ� �۽��ϴ�.");
							return this;
						} else if (array.length > 2) {
							pc.sendPackets("�ζǿ� ���õ� ���ڰ� �ʹ� �����ϴ�.");
							return this;
						}
						
						ArrayList <Integer> intarray = new ArrayList<Integer>();
						for (int i = 0 ; i<array.length; i++) {
							intarray.add(Integer.parseInt(array[i]));
						}

						if (intarray.get(0) > 15 || intarray.get(1) > 15) {
							over = true;
						}
						if (intarray.get(1) == intarray.get(0)) {
							pc.sendPackets("���� ���ڸ� �ι� ���� �����ϴ�.");
							return this;
						}
						if (over) {
							pc.sendPackets("�ζǿ� ���õ� ���ڰ� �ʹ� Ů�ϴ�. 1~15���� ���̿��� ������ �ּ���.");
							return this;
						}
						if (intarray.get(0) > intarray.get(1)) {
//							pc.sendPackets("���� ����: "+intarray.get(1)+", "+intarray.get(0)+" �Դϴ�");
							lotto_system.getInstance().addcharlotto(pc, intarray.get(1), intarray.get(0));
						} else {
//							pc.sendPackets("���� ����: "+intarray.get(0)+", "+intarray.get(1)+" �Դϴ�");
							lotto_system.getInstance().addcharlotto(pc, intarray.get(0), intarray.get(1));
						}
						over = false;
					}
					pc._LottoSelect = false;
					pc.getInventory().consumeItem(30001880);
					return this;
				}

				if(pc.is_combat_field())
					return this;
				
				L1ExcludingList spamList = SpamTable.getInstance().getExcludeTable(pc.getId());
				
				if (!spamList.contains(0, pc.getName())) {
					pc.sendPackets(SC_CHAT_MESSAGE_NOTI_PACKET.chat_send(pc, get_type(), get_message(), get_target_user_name(), get_target_user_server_no(), get_link_message()));
				}
				
				for (L1PcInstance listner : L1World.getInstance().getRecognizePlayer(pc)) {
					if (listner.getMapId() == 621 && !listner.isGm() && !pc.isGm())
						continue;

					if (!listner.isOutsideChat() && !pc.isGm()) {
						L1Buddy buddy = BuddyTable.getInstance().getBuddy(pc.getId(), listner.getName());
						L1Party party = listner.getParty();
						L1ChatParty cparty = listner.getChatParty();

						if (buddy != null || (listner.getClanid() > 0 && listner.getClanid() == pc.getClanid()) 
								|| (party != null && party.isMember(pc)) || (cparty != null && cparty.isMember(pc)))
							continue;
					}

					L1ExcludingList spamList3 = SpamTable.getInstance().getExcludeTable(listner.getId());
					if (!spamList3.contains(0, pc.getName())) {
						listner.sendPackets(SC_CHAT_MESSAGE_NOTI_PACKET.chat_send(pc, get_type(), get_message(), get_target_user_name(), get_target_user_server_no(), get_link_message()));
					}
				}
				
				L1MonsterInstance mob = null;
				for (L1Object obj : pc.getKnownObjects()) {
					if (obj instanceof L1MonsterInstance) {
						mob = (L1MonsterInstance) obj;
						if (mob.getNpcTemplate().is_doppel() && mob.getName().equals(pc.getName())) {
							mob.broadcastPacket(new S_NpcChatPacket(mob, message, 0));
						}
					}
				}
				return this;
			case CHAT_WHISPER: // �ӼӸ�
				String target_name = new String(get_target_user_name());
				if(MJWhisperChatFilterChain.getInstance().handle(pc, target_name, message))
					return this;
				
				if (pc.getLevel() < Config.ServerAdSetting.WHISPERCHATLEVEL) {
					pc.sendPackets(new S_ServerMessage(404, String.valueOf(Config.ServerAdSetting.WHISPERCHATLEVEL)));
					return this;
				}
				L1PcInstance target = L1World.getInstance().getPlayer(target_name);

				if (target == null) {
					pc.sendPackets(new S_ServerMessage(73, target_name));
					return this;
				}
				
				//TODO ���忡 �ش� PC�� ���� ���� ���� ���
//				if (target.getName().equalsIgnoreCase("��Ƽ��") || target.getName().equalsIgnoreCase("�̼��Ǿ�")
//						|| target.getName().equalsIgnoreCase("ī�ÿ����")) {
//					pc.sendPackets("\\f2��Ȱ�� ����� ���� ������ ��� ����� ���� �մϴ�.");
//					return this;
//				}

				if (target.equals(pc)) {
					return this;
				}

				// ���ܵǰ� �ִ� ���
				if (target != null) {
					L1ExcludingList spamList2 = SpamTable.getInstance().getExcludeTable(target.getId());
					if (spamList2.contains(0, pc.getName())) {
						pc.sendPackets(new S_ServerMessage(117, target.getName()));
						return this;
					}
				}
				
				if (!target.isCanWhisper()) {
					pc.sendPackets(new S_ServerMessage(205, target.getName()));
					return this;
				}
				
				pc.sendPackets(SC_CHAT_ACK_PACKET.chat_send(get_type(), get_message(), get_target_user_name(), get_target_user_server_no(), get_link_message()));
				target.sendPackets(SC_CHAT_MESSAGE_NOTI_PACKET.chat_send(pc, get_type(), get_message(), get_target_user_name(), get_target_user_server_no(), get_link_message()));
				LoggerInstance.getInstance().addWhisper(pc, target, message);
				MJMyChatService.service().whisperWriter().write(pc, message, target.getName());
				return this;
			case CHAT_SHOUT: // ��ġ�� ������
				return this;
			case CHAT_WORLD:
			case CHAT_TRADE: // ��â / ��� ä��
				try {
					if(MJWorldChatFilterChain.getInstance().handle(pc, message)){
						return this;
					}
					
					if (pc.getLevel() >= Config.CharSettings.LimitLevel && !pc.isGm()) {
						Account.ban(pc.getAccountName(), S_LoginResult.BANNED_REASON_HACK);
						pc.sendPackets(new S_SystemMessage(pc.getName() + " �� �����з� �Ͽ����ϴ�."));
						pc.sendPackets(new S_Disconnect());
						
						if (pc.getOnlineStatus() == 1) {
							pc.sendPackets(new S_Disconnect());
						}
						System.out.println("�� ���Ǳ׷������� ����ä������ [�з�] : "+ pc.getName());
					}
					
					if (pc.isGm() || pc.getAccessLevel() == 1) {
						if (get_type() == ChatType.CHAT_TRADE) {
							if (pc.Notice) {
								L1World.getInstance().broadcastPacketToAll(SC_MSG_ANNOUNCE.AnnounceMessage(1, "[����] : " + message + ""), true);
							} else {
								L1World.getInstance().broadcastPacketToAll(SC_MSG_ANNOUNCE.AnnounceMessage(1, "[����] : " + message + ""), true);
							}
							return this;
						}
						if (pc.Notice) {
							L1World.getInstance().broadcastPacketToAll(SC_MSG_ANNOUNCE.AnnounceMessage(1, "[����] : " + message + ""), true);
						} else {
							L1World.getInstance().broadcastPacketToAll(SC_CHAT_MESSAGE_NOTI_PACKET.chat_send(pc, get_type(), get_message(), get_target_user_name(), get_target_user_server_no(), get_link_message()), true);
						} 
						MJMyChatService.service().worldWriter().write(pc, message, MJString.EmptyString);
						return this;
					} else if (pc.getLevel() >= Config.ServerAdSetting.GLOBALCHATLEVEL) {
						if (L1World.getInstance().isWorldChatElabled()) {
							if (pc.get_food() >= 12) {
								pc.sendPackets(new S_PacketBox(S_PacketBox.FOOD, pc.get_food()));
								LoggerInstance.getInstance().addChat(LoggerChatType.Global, pc, message);
								MJMyChatService.service().worldWriter().write(pc, message, MJString.EmptyString);
								for (L1PcInstance listner : L1World.getInstance().getAllPlayers()) {
									L1ExcludingList spamList15 = SpamTable.getInstance().getExcludeTable(listner.getId());
									if (!spamList15.contains(0, pc.getName())) {
										listner.sendPackets(SC_CHAT_MESSAGE_NOTI_PACKET.chat_send(pc, get_type(), get_message(), get_target_user_name(), get_target_user_server_no(), get_link_message()));
									}
								}
							} else {
								pc.sendPackets(new S_ServerMessage(462));
								return this;
							}
						} else {
							pc.sendPackets(new S_ServerMessage(510));
							return this;
						}
					} else {
						pc.sendPackets(new S_ServerMessage(195, String.valueOf(Config.ServerAdSetting.GLOBALCHATLEVEL)));
						return this;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return this;
			case CHAT_PLEDGE: // ���� ä��
				if (pc.getClanid() != 0) {
					L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
					if (clan != null) {
						LoggerInstance.getInstance().addChat(LoggerChatType.Clan, pc, message);
						for (L1PcInstance listner : clan.getOnlineClanMember()) {
							L1ExcludingList spamList4 = SpamTable.getInstance().getExcludeTable(listner.getId());
							if (!spamList4.contains(0, pc.getName())) {
								listner.sendPackets(SC_CHAT_MESSAGE_NOTI_PACKET.chat_send(pc, get_type(), get_message(), get_target_user_name(), get_target_user_server_no(), get_link_message()));
							}
						}
						MJMyChatService.service().pledgeWriter().write(pc, message, MJString.EmptyString);
					}
				}
				return this;
			case CHAT_HUNT_PARTY: // ��Ƽ ä��
				if (pc.isInParty()) {
					LoggerInstance.getInstance().addChat(LoggerChatType.Party, pc, message);
					for (L1PcInstance listner : pc.getParty().getMembers()) {
						L1ExcludingList spamList11 = SpamTable.getInstance().getExcludeTable(listner.getId());
						if (!spamList11.contains(0, pc.getName())) {
							listner.sendPackets(SC_CHAT_MESSAGE_NOTI_PACKET.chat_send(pc, get_type(), get_message(), get_target_user_name(), get_target_user_server_no(), get_link_message()));
						}
					}
				}
				return this;
			case CHAT_PLEDGE_PRINCE: // ���� ��ȣ ä��
				if (pc.getClanid() != 0) {
					L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
					int rank = pc.getClanRank();
					if (clan != null) {
						if ((rank == L1Clan.���� || rank == L1Clan.��ȣ)) {
							LoggerInstance.getInstance().addChat(LoggerChatType.Alliance, pc, message);
							for (L1PcInstance listner : clan.getOnlineClanMember()) {
								L1ExcludingList spamList13 = SpamTable.getInstance().getExcludeTable(listner.getId());
								if (!spamList13.contains(0, pc.getName())) {
									listner.sendPackets(SC_CHAT_MESSAGE_NOTI_PACKET.chat_send(pc, get_type(),
											get_message(), get_target_user_name(), get_target_user_server_no(),
											get_link_message()));
								}
							}
						} else {
							pc.sendPackets("����/��ȣ �̻� ä���� �����մϴ�.");
						}
					}
				}
				return this;
			case CHAT_CHAT_PARTY: // ä�� ��Ƽ
				if (pc.isInChatParty()) {
					LoggerInstance.getInstance().addChat(LoggerChatType.Party, pc, message);
					for (L1PcInstance listner : pc.getChatParty().getMembers()) {
						L1ExcludingList spamList14 = SpamTable.getInstance().getExcludeTable(listner.getId());
						if (!spamList14.contains(0, pc.getName())) {
							listner.sendPackets(SC_CHAT_MESSAGE_NOTI_PACKET.chat_send(pc, get_type(), get_message(), get_target_user_name(), get_target_user_server_no(), get_link_message()));
						}
					}
				}
				return this;
			case CHAT_PLEDGE_ALLIANCE: // ���� ä��(�̱���) ~ �ϰ� ���ϴ� ä����.
				if (pc.getClanid() != 0) {
					L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
					int rank = pc.getClanRank();
					if (clan != null && (rank == L1Clan.���� || (rank == L1Clan.��ȣ))) {
						LoggerInstance.getInstance().addChat(LoggerChatType.Alliance, pc, message);
						for (L1PcInstance listner : clan.getOnlineClanMember()) {
							int listnerRank = listner.getClanRank();
							L1ExcludingList spamList13 = SpamTable.getInstance().getExcludeTable(listner.getId());
							if (!spamList13.contains(0, pc.getName()) && (listnerRank == L1Clan.���� || (listnerRank == L1Clan.��ȣ))) {
								listner.sendPackets(SC_CHAT_MESSAGE_NOTI_PACKET.chat_send(pc, get_type(), get_message(), get_target_user_name(), get_target_user_server_no(), get_link_message()));
							}
						}
					} else {
						pc.sendPackets("��ȣ �̻� �����մϴ�.");
					}
				}
				return this;
			case CHAT_PLEDGE_NOTICE: // ���� ���� ä��
				if (pc.getClanid() != 0) {
					L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
					if (clan != null) {
						if (pc.isCrown() && pc.getId() == clan.getLeaderId()) {
							LoggerInstance.getInstance().addChat(LoggerChatType.Guardian, pc, message);
							for (L1PcInstance listner : clan.getOnlineClanMember()) {
								L1ExcludingList spamList17 = SpamTable.getInstance().getExcludeTable(listner.getId());
								if (!spamList17.contains(0, pc.getName())) {
									listner.sendPackets(SC_CHAT_MESSAGE_NOTI_PACKET.chat_send(pc, get_type(),
											get_message(), get_target_user_name(), get_target_user_server_no(),
											get_link_message()));
								}
							}
						} else {
							pc.sendPackets("���ָ� ���� �� �� �ֽ��ϴ�.");
						}
					}
				}
				return this;
			case CHAT_CLASS:
				return this;
			case CHAT_TEAM:
				return this;
			case CHAT_ARENA_TEAM:
				return this;
			case CHAT_ARENA_OBSERVER:
				return this;
			case CHAT_ROOM_ARENA_ALL:
				return this;
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}
	
	private void do_shift_server(L1PcInstance pc, String chatText){
		if(!pc.getInventory().checkItem(MJShiftObjectManager.getInstance().get_character_transfer_itemid())){
			pc.sendPackets("�κ��丮���� ������������ ã�� �� ���� ���� �۾��� ����մϴ�.");
			return;
		}

		try{
			List<CommonServerInfo> servers = MJShiftObjectManager.getInstance().get_commons_servers(true);
			if(servers == null || servers.size() <= 0){
				pc.sendPackets("���� �̵��� �� �ִ� ������ �����ϴ�.");
				return;
			}
			CommonServerInfo select_server_info = null;
			for(CommonServerInfo csInfo : servers){
				if(csInfo.server_description.equals(chatText)){
					select_server_info = csInfo;
					break;
				}
			}
			if(select_server_info == null){
				pc.sendPackets(String.format("%s��(��) ã�� �� �����ϴ�.", chatText));
				return;
			}
			if(!select_server_info.server_is_on){
				pc.sendPackets(String.format("%s ���� �Ұ���(���� OFF)", chatText));
				return;
			}
			if(!select_server_info.server_is_transfer){
				pc.sendPackets(String.format("%s ���� �Ұ���(��� ����)", chatText));
				return;
			}

			if(!pc.getInventory().consumeItem(MJShiftObjectManager.getInstance().get_character_transfer_itemid(), 1))
				return;
			MJShiftObjectManager.getInstance().do_send(pc, MJEShiftObjectType.TRANSFER, select_server_info.server_identity, MJString.EmptyString);
			System.out.println(String.format("%s���� ���� �����(%s)�� ����߽��ϴ�.", pc.getName(), select_server_info.server_description));
			return;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private boolean createNewItem(L1PcInstance pc, int item_id, int count, int bless) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		if (item != null) {
			item.setCount(count);
			item.setIdentified(true);
			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
				item.setBless(bless);
				pc.getInventory().updateItem(item, L1PcInventory.COL_BLESS);
				pc.getInventory().saveItem(item, L1PcInventory.COL_BLESS);
			} else {
				pc.sendPackets(new S_ServerMessage(82)); // ���� �������� �����ϰų� �κ��丮�� ������ �� �� �� �����ϴ�.
				return false;
			}
			pc.sendPackets(new S_ServerMessage(403, item.getLogName()), true); // %0��
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
		return new CS_CHAT_REQ_PACKET();
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
