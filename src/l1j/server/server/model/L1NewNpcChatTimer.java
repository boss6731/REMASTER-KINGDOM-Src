package l1j.server.server.model;

import java.util.Random;

import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParseeFactory;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParser;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.templates.L1NewNpcChat;

public class L1NewNpcChatTimer implements Runnable {
	private final L1NpcInstance _npc;

	private final L1NewNpcChat _npcChat;
	
	private boolean _chatActive = false;
	private long _repeat;

	private int chatTiming;
	private int chatInterval;
	private boolean isShout;
	private boolean isWorldChat;
	private boolean isNormalChat;
	private String chatMent;
	private int chatMentChance;
	private Random random = new Random(System.nanoTime());

	public L1NewNpcChatTimer(L1NpcInstance npc, L1NewNpcChat npcChat) {
		_npc = npc;
		_npcChat = npcChat;
		_repeat = 0;
	}

	public L1NewNpcChatTimer(L1NpcInstance npc, L1NewNpcChat npcChat, long repeat) {
		_npc = npc;
		_npcChat = npcChat;
		_repeat = repeat;
	}

	public void startChat()
	{
		if (_npc == null || _npcChat == null) {
			return;
		}

		if (_npc.getHiddenStatus() != L1NpcInstance.HIDDEN_STATUS_NONE
				|| _npc._destroyed) {
			return;
		}

		chatTiming = _npcChat.getChatPosition();
		chatInterval = _npcChat.getChatInterval();
		isShout = _npcChat.isShout();
		isWorldChat = _npcChat.isWorldChat();
		isNormalChat = _npcChat.isNormalChat();
		chatMent = _npcChat.getMent();
		chatMentChance = _npcChat.getMentChance();
		
		GeneralThreadPool.getInstance().schedule(this, 300);
	}
	@Override
	public void run() {
		try {
			if (!chatMent.equals("")) {
				if (!_chatActive) {
					_chatActive = true;
					GeneralThreadPool.getInstance().schedule(this, chatInterval);
                    return new L1PcInstance[0];
				} else {
					_chatActive = false;
					String[] ment = (String[])MJArrangeParser.parsing(chatMent, ",", MJArrangeParseeFactory.createStringArrange()).result();
					int rnd = random.nextInt(ment.length);
					if (ment.length > 1)
						chat(_npc, chatTiming, ment[rnd], isNormalChat, isShout, isWorldChat);
					else
						chat(_npc, chatTiming, chatMent, isNormalChat, isShout, isWorldChat);
				}
			}
			if (_repeat > 0) {
				GeneralThreadPool.getInstance().schedule(this, _repeat);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
        return new L1PcInstance[0];
    }

	private void chat(L1NpcInstance npc, int chatTiming, String chatId, 
			boolean isNormal, boolean isShout, boolean isWorldChat) {
		if (chatTiming == L1NpcInstance.CHAT_TIMING_APPEARANCE && (npc.isDead() || npc.STATUS_Escape)) {
			return;
		}
		if (chatTiming == L1NpcInstance.CHAT_TIMING_DEAD && !npc.isDead()) {
			return;
		}
		if (chatTiming == L1NpcInstance.CHAT_TIMING_HIDE && npc.isDead()) {
			return;
		}
		if (chatTiming == L1NpcInstance.CHAT_TIMING_SPAWN && npc.isDead()) {
			return;
		}
		if (chatTiming == L1NpcInstance.CHAT_TIMING_ESCAPE && (npc.isDead() || !npc.STATUS_Escape)) {
			return;
		}
		
		if (chatTiming != L1NpcInstance.CHAT_TIMING_ESCAPE) {
			if (chatMentChance > 0) {
				int rnd = random.nextInt(100) + 1;
				if (rnd > chatMentChance)
					return;
			}
		}
		/* TODO
		 * 0號: 在聊天窗口顯示NPC的文字
		 * 4號: 在聊天窗口隱藏NPC的文字
		 * */
		if (chatTiming == L1NpcInstance.CHAT_TIMING_SPAWN) {
			npc.broadcastPacket(new S_DoActionGFX(npc.getId(), ActionCodes.ACTION_Attack));
		}
		
		if (isNormal) {
			npc.broadcastPacket(new S_NpcChatPacket(npc, chatId, 4));
		}
		
		if (isShout){
			npc.wideBroadcastPacket(new S_NpcChatPacket(npc, chatId, 2));
		}

		if (isWorldChat) {
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (pc != null) {
					pc.sendPackets(new S_NpcChatPacket(npc, chatId, 3));
				}
				break;
			}
		}
	}

}
