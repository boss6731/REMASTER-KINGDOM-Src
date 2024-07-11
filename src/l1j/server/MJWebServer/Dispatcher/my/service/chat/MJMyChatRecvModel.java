package l1j.server.MJWebServer.Dispatcher.my.service.chat;

import l1j.server.Config;
import l1j.server.MJTemplate.MJEncoding;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client.ChatType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CHAT_MESSAGE_NOTI_PACKET;
import l1j.server.MJWebServer.Dispatcher.my.service.chat.MJMyChatModel.MJMyChatSystemModel;
import l1j.server.MJWebServer.ws.MJWebSockRequest;
import l1j.server.MJWebServer.ws.MJWebSockServerProvider;
import l1j.server.MJWebServer.ws.protocol.MJWebSockRecvModel;
import l1j.server.MJWebServer.ws.protocol.MJWebSockSendModel;
import l1j.server.server.GMCommands;
import l1j.server.server.datatables.SpamTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1ExcludingList;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.monitor.Logger;
import l1j.server.server.monitor.LoggerInstance;

class MJMyChatRecvModel extends MJWebSockRecvModel{
	static void system(MJWebSockRequest request, String msg){
		MJMyChatSystemModel model = new MJMyChatSystemModel();
		model.nick = "�ý���";
		model.msg = msg;
		model.gm = true;
		MJWebSockSendModel<MJMyChatModel> smodel = new MJWebSockSendModel<MJMyChatModel>("onchat", model);
		request.write(smodel);
	}
	
	private String chatType;
	private String message;
	private String to;
	private String command;
	@Override
	public void callback(MJWebSockRequest request) {
		MJMyChatUserInfo uInfo = request.attr(MJMyChatExchangeHandler.chatUserKey).get();
		if(uInfo == null || uInfo.character() == null){
			system(request, "����� ������ ã�� �� �����ϴ�.");
			return;
		}
		
		
		L1PcInstance pc = L1World.getInstance().getPlayer(uInfo.character().nick());
		if (pc == null) {
			if(uInfo.character().gm()) {
				onCallbackGm0(request, uInfo);			
			}else {
				system(request, "�޽��� ������ ���忡 �������� ĳ���͸� �����մϴ�.");
			}
			return;
		}
		onCallback0(request, pc);
	}
	
	private void onCallback0(MJWebSockRequest request, L1PcInstance pc) {
		switch(chatType){
		case "world":
			onWorldChat(request, pc);
			break;
		case "whisper":
			onWhisperChat(request, pc);
			break;
		case "pledge":
			onPledgeChat(request, pc);
			break;
		case "command":
			onCommandChat(request, pc);
			break;
		default:
			MJWebSockServerProvider.provider().print(request, String.format("invalid MJMyChatRecvModel chat type : %s. connection close.", chatType));
			request.close();
			break;
		}
	}
	
	private void onCallbackGm0(MJWebSockRequest request, MJMyChatUserInfo uInfo) {
		switch(chatType){
		case "world":
			onWorldChatOnlyGM(request, uInfo);
			break;
		case "whisper":
			system(request, "�ش� ����� ���忡 �������� ĳ���͸� �����մϴ�.");
			break;
		case "pledge":
			system(request, "�ش� ����� ���忡 �������� ĳ���͸� �����մϴ�.");
			break;
		case "command":
			onCommandGmChat(request, uInfo);
			break;
		default:
			MJWebSockServerProvider.provider().print(request, String.format("invalid MJMyChatRecvModel chat type : %s. connection close.", chatType));
			request.close();
			break;
		}
	}
	
	private void onWorldChat(MJWebSockRequest request, L1PcInstance pc){
		if(!L1World.getInstance().isWorldChatElabled()){
			system(request, "���� �Ŵ����� ���� ���� ä���� ������ �����Դϴ�.");
			return;			
		}
		if(pc.getLevel() < Config.ServerAdSetting.GLOBALCHATLEVEL){
			system(request, String.format("���� ä���� %d���� ���� �����մϴ�.", Config.ServerAdSetting.GLOBALCHATLEVEL));
			return;
		}
		
		if (pc.hasSkillEffect(L1SkillId.STATUS_CHAT_PROHIBITED)) {
			system(request, "ä�ñ��� ���¿����� ä�� ���񽺸� �̿��� �� �����ϴ�.");			
			return;
		}
		
		SC_CHAT_MESSAGE_NOTI_PACKET noti = SC_CHAT_MESSAGE_NOTI_PACKET.newInstance();
		noti.set_time_t64(0L);
		noti.set_type(ChatType.CHAT_WORLD);
		noti.set_message(message.getBytes());
		noti.set_name(pc.isGm() ? "******".getBytes(MJEncoding.MS949) : pc.getName().getBytes(MJEncoding.MS949));
		int step = pc.getRankLevel();
		if(step != 0){
			noti.set_ranker_rating(step);
		}
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_CHAT_MESSAGE_NOTI_PACKET);
		noti.dispose();
		
		for (L1PcInstance listner : L1World.getInstance().getAllPlayers()) {
			L1ExcludingList spamList = SpamTable.getInstance().getExcludeTable(listner.getId());
			if (!spamList.contains(0, pc.getName())) {
				if (listner.isShowWorldChat()) {
					listner.sendPackets(stream, false);
				}
			}
		}
		stream.dispose();
		MJMyChatService.service().worldWriter().write(pc, message, MJString.EmptyString);
		LoggerInstance.getInstance().addChat(Logger.LoggerChatType.Global, pc, message);
		if (!pc.isGm()) {
			pc.checkChatInterval();
		}
	}
	
	private void onWorldChatOnlyGM(MJWebSockRequest request, MJMyChatUserInfo uInfo){
		if(!L1World.getInstance().isWorldChatElabled()){
			system(request, "���� �Ŵ����� ���� ���� ä���� ������ �����Դϴ�.");
			return;			
		}
		
		SC_CHAT_MESSAGE_NOTI_PACKET noti = SC_CHAT_MESSAGE_NOTI_PACKET.newInstance();
		noti.set_time_t64(0L);
		noti.set_type(ChatType.CHAT_WORLD);
		noti.set_message(message.getBytes());
		noti.set_name("******".getBytes(MJEncoding.MS949));
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_CHAT_MESSAGE_NOTI_PACKET);
		noti.dispose();
		for (L1PcInstance listner : L1World.getInstance().getAllPlayers()) {
			listner.sendPackets(stream, false);
		}
		stream.dispose();
		MJMyChatService.service().worldGmWriter().write(null, message, MJString.EmptyString);
	}
	
	private void onPledgeChat(MJWebSockRequest request, L1PcInstance pc){
		int clanId = pc.getClanid();
		if(clanId == 0){
			system(request, "���Ϳ� ���ԵǾ� ���� �ʽ��ϴ�.");
			return;
		}
		L1Clan clan = L1World.getInstance().getClan(clanId);
		if(clan == null){
			system(request, "���� ������ ã�� �� �����ϴ�.");
			return;
		}
		if (pc.hasSkillEffect(L1SkillId.STATUS_CHAT_PROHIBITED)) {
			system(request, "ä�ñ��� ���¿����� ä�� ���񽺸� �̿��� �� �����ϴ�.");			
			return;
		}
		SC_CHAT_MESSAGE_NOTI_PACKET noti = SC_CHAT_MESSAGE_NOTI_PACKET.newInstance();
		noti.set_time_t64(0L);
		noti.set_type(ChatType.CHAT_PLEDGE);
		noti.set_message(message.getBytes());
		noti.set_name(pc.getName().getBytes(MJEncoding.MS949));
		int step = pc.getRankLevel();
		if(step != 0){
			noti.set_ranker_rating(step);
		}
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_CHAT_MESSAGE_NOTI_PACKET);
		noti.dispose();
		
		for (L1PcInstance listner : clan.getOnlineClanMember()) {
			L1ExcludingList spamList = SpamTable.getInstance().getExcludeTable(listner.getId());
			if (!spamList.contains(0, pc.getName())) {
				listner.sendPackets(stream, false);
			}
		}
		stream.dispose();		
		MJMyChatService.service().pledgeWriter().write(pc, message, MJString.EmptyString);
		LoggerInstance.getInstance().addChat(Logger.LoggerChatType.Clan, pc, message);
		if (!pc.isGm()) {
			pc.checkChatInterval();
		}
	}
	
	private void onWhisperChat(MJWebSockRequest request, L1PcInstance pc){
		if(message.length() > 50){
			system(request, "�Ӹ��� ���� �� �ִ� ���ڼ��� �ʰ��Ͽ����ϴ�.");
			return;
		}
		
		if(MJString.isNullOrEmpty(to) || to.length() > 50){
			system(request, "���� ������ ã�� �� �����ϴ�.");
			return;
		}
		
		L1PcInstance toPlayer = L1World.getInstance().getPlayer(to);
		if(toPlayer == null){
			system(request, String.format("%s���� ���ӿ� ���������� �ʽ��ϴ�.", to));
			return;			
		}
		if (pc.hasSkillEffect(L1SkillId.STATUS_CHAT_PROHIBITED)) {
			system(request, "ä�ñ��� ���¿����� ä�� ���񽺸� �̿��� �� �����ϴ�.");			
			return;
		}
		if (toPlayer.hasSkillEffect(L1SkillId.STATUS_CHAT_PROHIBITED)) {
			system(request, "������ ä�ñ��� �����Դϴ�.");			
			return;
		}
		SC_CHAT_MESSAGE_NOTI_PACKET notiTo = SC_CHAT_MESSAGE_NOTI_PACKET.newInstance();
		SC_CHAT_MESSAGE_NOTI_PACKET notiFrom = SC_CHAT_MESSAGE_NOTI_PACKET.newInstance();
		notiTo.set_time_t64(0L);
		notiFrom.set_time_t64(0L);
		notiTo.set_type(ChatType.CHAT_WHISPER);
		notiFrom.set_type(ChatType.CHAT_WHISPER);
		byte[] buff = message.getBytes();
		notiTo.set_message(buff);
		notiFrom.set_message(buff);
		byte[] names = pc.getName().getBytes(MJEncoding.MS949);
		notiTo.set_name(names);
		notiFrom.set_name(names);
		int step = pc.getRankLevel();
		if(step != 0){
			notiTo.set_ranker_rating(step);
			notiFrom.set_ranker_rating(step);
		}
		notiTo.set_target_user_name(to);
		notiFrom.set_target_user_name(pc.getName());
		pc.sendPackets(notiTo, MJEProtoMessages.SC_CHAT_MESSAGE_NOTI_PACKET);
		toPlayer.sendPackets(notiFrom, MJEProtoMessages.SC_CHAT_MESSAGE_NOTI_PACKET);
		
		MJMyChatService.service().whisperWriter().write(pc, message, to);
		LoggerInstance.getInstance().addWhisper(pc, toPlayer, message);
		if (!pc.isGm()) {
			pc.checkChatInterval();
		}
	}
	
	private void onCommandChat(final MJWebSockRequest request, L1PcInstance pc){
		if(pc.isGm()){
			GMCommands.getInstance().handleCommands(MJMyChatCommandInstance.instance(), command);
			MJMyChatCommandInstance.instance().onCommandFlush();
		}
	}
	
	private void onCommandGmChat(final MJWebSockRequest request, MJMyChatUserInfo uInfo){
		GMCommands.getInstance().handleCommands(MJMyChatCommandInstance.instance(), command);
		MJMyChatCommandInstance.instance().onCommandFlush();
	}
}
