package l1j.server.MJNetSafeSystem.Distribution;

import l1j.server.Config;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.Opcodes;
import l1j.server.server.clientpackets.*;
import l1j.server.server.serverpackets.S_CharPass;

import static l1j.server.server.Opcodes.C_QUIT;
import static l1j.server.server.Opcodes.C_SAVEIO;

public class SelectCharacterDistributor  extends Distributor{

	@Override
	public ClientBasePacket handle(GameClient clnt, byte[] data, int op) throws Exception {
		switch(op){
			case Opcodes.C_READ_NEWS:
				return null;

			case Opcodes.C_EXTENDED_PROTOBUF:
				if (MJEProtoMessages.existsProto(clnt, data))
					return null;

				return new C_Craft(data, clnt);

			case Opcodes.C_CREATE_CUSTOM_CHARACTER:
				if (data.length > 0x20)
					break;

				return new C_CreateChar(data, clnt);

			case Opcodes.C_DELETE_CHARACTER:
				if (clnt.getAccount().getCPW() != null) {
					clnt.getAccount().setwaitpacket(data);
					clnt.sendPacket(new S_CharPass(S_CharPass._PASSWORD_INPUT_SCREEN));
					return null;
				}
				return new C_DeleteChar(data, clnt);

			case Opcodes.C_CHANNEL:
				return new C_Report(data, clnt);

			case Opcodes.C_LOGOUT:
				return new C_ReturnToLogin(data, clnt);

			case Opcodes.C_ENTER_WORLD:
				long diff = System.currentTimeMillis() - clnt.latestRestartMillis();
				if(diff < Config.Login.worldDelayMillis) {
					GeneralThreadPool.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							try {
								handle(clnt, data, op);
							} catch (Exception e) {
								e.printStackTrace();
							}
							return null;
						}
					}, Config.Login.worldDelayMillis - diff);
					return null;
				}
				// 當進行二次密碼除錯時，找到補丁點不便
				//            if (MJFxEntry.IS_DEBUG_MODE)
				//                clnt.setLoginRecord(true);
				// 當進行二次密碼除錯時，找到補丁點不便
				if(clnt.getAccount().getCPW() != null && !clnt.isLoginRecord()){
					clnt.getAccount().setwaitpacket(data);
					clnt.sendPacket(new S_CharPass(S_CharPass._PASSWORD_INPUT_SCREEN), false);
					return null;
				}
				C_LoginToServerWrap wrap = new C_LoginToServerWrap(data, clnt);
				return wrap;
			case Opcodes.C_VOICE_CHAT:
				return new C_ReturnStaus(data, clnt);

		/*case Opcodes.C_READ_NEWS:
			ConnectedDistributor.sendNotice(clnt, clnt.getAccountName());
			return null;*/

			case C_QUIT:
				return new C_Quit(data, clnt);

			case Opcodes.C_RESTART:
				return new C_NewCharSelect(data, clnt);

			case C_SAVEIO:
				return new C_CharcterConfig(data, clnt);

			// TODO 確認按下重生按鈕時同時進來的數據包原因 -> 不輸出訊息
			case Opcodes.C_MOVE:
			case Opcodes.C_USE_ITEM:
			case Opcodes.C_USE_SPELL:
			case Opcodes.C_BUY_SELL:
			case Opcodes.C_ONOFF:
//		case Opcodes.C_SAVEIO:
				return null;
			case Opcodes.C_RETURN_SUMMON:
				return new C_Teleport(data, clnt);

			default:
				break;
		}
		toInvalidOp(clnt, op, data.length, "SelectCharacter", false);
		return null;
	}

}
