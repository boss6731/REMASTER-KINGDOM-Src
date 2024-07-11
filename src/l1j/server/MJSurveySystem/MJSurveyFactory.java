package l1j.server.MJSurveySystem;

import l1j.server.MJInstanceSystem.MJLFC.Creator.MJLFCCreator;
import l1j.server.MJTemplate.MJSimpleRgb;
import l1j.server.MJTemplate.Lineage2D.MJPoint;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_IconMessage;
import l1j.server.server.serverpackets.S_NotificationMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1BoardPost;

public class MJSurveyFactory {
	private static final S_SystemMessage _isOverEvent = new S_SystemMessage("活動已經結束。"); //

	public static MJInterfaceSurvey createLFCSurvey() {
		return new MJInterfaceSurvey() {
			@override
			public void survey(L1PcInstance pc, int num, boolean isYes) {
// 根據帖子ID查找LFC帖子
				L1BoardPost bp = L1BoardPost.findByIdLfc(num - 1000);
				if(bp == null) {
					pc.sendPackets("無效的回應。"); //
					return;
				}
// 獲取帖子內容並解析
				String content = bp.getContent();
				String[] content_array = content.split(" ");
				String target_name = content_array[1];
				int type_id = Integer.parseInt(content_array[0]);
// 根據玩家名稱查找玩家對象
				L1PcInstance owner = L1World.getInstance().findpc(target_name);
				if(owner == null) {
					pc.sendPackets(String.format("找不到 %s。", target_name)); //
					MJLFCCreator.unregistLfc(pc); // 取消LFC註冊
					return;
				}

// 如果回應是否定的
				if(!isYes) {
					if(owner != null)
						owner.sendPackets(String.format("%s 取消了決鬥。", pc.getName())); //
					MJLFCCreator.unregistLfc(pc); // 取消LFC註冊
					return;
				}

// 如果回應是肯定的，創建LFC
				MJLFCCreator.create(pc, owner, type_id);
				MJLFCCreator.unregistLfc(pc); // 取消LFC註冊
			}
		};
	}

	public static MJInterfaceSurvey createObjectCallSurvey() {
		return new MJInterfaceSurvey() {
			@override
			public void survey(L1PcInstance pc, int num, boolean isYes) {
				// 如果玩家選擇否，直接返回
				if(!isYes)
					return;

				// 根據ID查找物件
				L1Object obj = L1World.getInstance().findObject(num);
				if(obj == null) {
					pc.sendPackets(_isOverEvent, false); // "活動已經結束。"
					return;
				}

				// 檢查物件是否為角色且是否死亡
				if(obj instanceof L1Character) {
					L1Character c = (L1Character)obj;
					if(c.isDead()) {
						pc.sendPackets(_isOverEvent, false); // "活動已經結束。"
						return;
					}
				}

				// 創建新的傳送點位置
				MJPoint pt = MJPoint.newInstance(obj.getX(), obj.getY(), 3, obj.getMapId());
				// 開始傳送玩家
				pc.start_teleport(pt.x, pt.y, pt.mapId, pc.getHeading(), 18339, true, false);
			}
		};
	}

	public static boolean isMegaphoneSpeaking = false;

	public static MJInterfaceSurvey createMegaphoneSurvey(int megaphoneObjectId, String message, int duration_second) {
		return new MJInterfaceSurvey() {
			@override
			public void survey(L1PcInstance pc, int num, boolean isYes) {
				// 如果玩家選擇否，直接返回
				if(!isYes)
					return;

				// 在玩家的背包中查找指定ID的物品
				L1ItemInstance item = pc.getInventory().findItemObjId(num);
				if(item == null) {
					pc.sendPackets("找不到擴音器。"); //
					return;
				}

				// 檢查是否已有擴音器消息在播放
				if(isMegaphoneSpeaking) {
					pc.sendPackets("擴音器消息正在播放中。請稍後再試。"); //
					return;
				}

				// 設置擴音器消息為正在播放狀態
				isMegaphoneSpeaking = true;

				// 構建消息包
				final ServerBasePacket[] pcks = new ServerBasePacket[] {
						S_IconMessage.getMessage(String.format("[%s]的擴音通知。", pc.getName()), new MJSimpleRgb(255,255,255), 17, duration_second), //
						S_NotificationMessage.get(S_NotificationMessage.DISPLAY_POSITION_BOTTOM, message, MJSimpleRgb.red(), duration_second) // 底部顯示的紅色消息
				};

				// 向所有在線玩家發送消息
				L1World.getInstance().getAllPlayerStream()
						.filter((L1PcInstance t) -> t != null && t.getAI() == null)
						.forEach((L1PcInstance t) -> {
							t.sendPackets(pcks, false);
						});

				// 安排在消息播放結束後重置擴音器狀態
				GeneralThreadPool.getInstance().schedule(new Runnable() {
					@override
					public void run() {
						isMegaphoneSpeaking = false;
					}
				}, duration_second * 1000L);

				// 從玩家背包中移除擴音器物品
				pc.getInventory().removeItem(num, 1);

				// 清理消息包
				for(ServerBasePacket pck : pcks)
					pck.clear();
			}
		};
	}
}
