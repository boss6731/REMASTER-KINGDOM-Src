package l1j.server.MJNetSafeSystem.Distribution;

import static l1j.server.server.Opcodes.C_QUIT;
import static l1j.server.server.Opcodes.C_SAVEIO;

import MJFX.MJFxEntry;
import l1j.server.Config;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.Opcodes;
import l1j.server.server.clientpackets.C_CharcterConfig;
import l1j.server.server.clientpackets.C_Craft;
import l1j.server.server.clientpackets.C_CreateChar;
import l1j.server.server.clientpackets.C_DeleteChar;
import l1j.server.server.clientpackets.C_LoginToServerWrap;
import l1j.server.server.clientpackets.C_NewCharSelect;
import l1j.server.server.clientpackets.C_Quit;
import l1j.server.server.clientpackets.C_Report;
import l1j.server.server.clientpackets.C_ReturnStaus;
import l1j.server.server.clientpackets.C_ReturnToLogin;
import l1j.server.server.clientpackets.C_Teleport;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.serverpackets.S_CharPass;

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
					clnt.sendPacket(new S_CharPass(S_CharPass._密碼輸入框));
					return null;
				}
				return new C_DeleteChar(data, clnt);

			case Opcodes.C_CHANNEL:
				return new C_Report(data, clnt);

			case Opcodes.C_LOGOUT:
				return new C_ReturnToLogin(data, clnt);

			case Opcodes.C_ENTER_WORLD:
				long diff = System.currentTimeMillis() - clnt.latestRestartMillis();
				if (diff < Config.Login.worldDelayMillis) {
					GeneralThreadPool.getInstance().schedule(new Runnable() {
						@override
						public void run() {
							try {
								handle(clnt, data, op);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}, Config.Login.worldDelayMillis - diff);
					return null;
				}

				// 當不使用二次密碼調試模式時，用於查找補丁點的註解
				// if (MJFxEntry.IS_DEBUG_MODE)
				// clnt.setLoginRecord(true);
				// 當不使用二次密碼調試模式時，用於查找補丁點的註解

				if (clnt.getAccount().getCPW() != null && !clnt.isLoginRecord()) {
					clnt.getAccount().setwaitpacket(data);
					clnt.sendPacket(new S_CharPass(S_CharPass._密碼輸入框), false);
					return null;
				}
				C_LoginToServerWrap wrap = new C_LoginToServerWrap(data, clnt);
				return wrap;

			case Opcodes.C_VOICE_CHAT:
				return new C_ReturnStatus(data, clnt);
		}

		/*case Opcodes.C_READ_NEWS:
ConnectedDistributor.sendNotice(clnt, clnt.getAccountName());
return null;*/

		case Opcodes.C_QUIT:
		return new C_Quit(data, clnt);

		case Opcodes.C_RESTART:
		return new C_NewCharSelect(data, clnt);

		case Opcodes.C_SAVEIO:
		return new C_CharacterConfig(data, clnt);

// TODO 確認當按下列表時，同時接收的數據包的原因-> 防止顯示消息
		case Opcodes.C_MOVE:

		case Opcodes.C_USE_ITEM:
		case Opcodes.C_USE_SPELL:
		case Opcodes.C_BUY_SELL:
		case Opcodes.C_ONOFF:
//        case Opcodes.C_SAVEIO:
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
