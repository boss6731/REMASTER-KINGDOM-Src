package l1j.server.MJCharacterActionSystem.Walk;

import static l1j.server.server.model.Instance.L1PcInstance.REGENSTATE_MOVE;
import MJFX.UIAdapter.MJPerformAdapter;
import l1j.server.Config;
import l1j.server.ForgottenIsland.FIController;
import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.MJCharacterActionSystem.AbstractActionHandler;
import l1j.server.MJCharacterActionSystem.Executor.CharacterActionExecutor;
import l1j.server.MJINNSystem.MJINNRoom;
import l1j.server.MJINNSystem.Loader.MJINNMapInfoLoader;
import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJTemplate.MJSimpleRgb;
import l1j.server.MJTemplate.Chain.Action.MJWalkFilterChain;
import l1j.server.MJTemplate.Lineage2D.MJPoint;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CONNECT_HIBREEDSERVER_NOTI_PACKET;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_FOURTH_GEAR_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_MESSAGE;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_NOTI;
import l1j.server.MJTemplate.ObServer.MJCopyMapObservable;
import l1j.server.MJTemplate.PacketHelper.MJPacketParser;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.MJWebServer.Dispatcher.my.service.mapview.MJMyMapViewService;
import l1j.server.QueenAntSystem.QueenAntController;
import l1j.server.server.ActionCodes;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.clientpackets.C_LoginToServer;
import l1j.server.server.clientpackets.C_NewCharSelect;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.Dungeon;
import l1j.server.server.model.DungeonRandom;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1MerchantInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.trap.L1WorldTraps;
import l1j.server.server.serverpackets.S_ACTION_UI;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_MoveCharPacket;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.types.Point;
import l1j.server.server.utils.MJCommons;

public class WalkActionHandler extends AbstractActionHandler implements MJPacketParser {
	protected int x;
	protected int y;
	protected int h;
	protected int nextX;
	protected int nextY;

	@Override
	public void parse(L1PcInstance owner, ClientBasePacket pck) {
		this.owner = owner;
		x = pck.readH();
		y = pck.readH();
		h = pck.readC() % 8;
		nextX = x + MJCommons.HEADING_TABLE_X[h];
		nextY = y + MJCommons.HEADING_TABLE_Y[h];
	}

	@Override
	public void doWork() {
		if (owner.hasSkillEffect(L1SkillId.MEDITATION)) {
			if (!owner.isPassive(MJPassiveID.MEDITATION_BEYOND.toInt())) {
				owner.removeSkillEffect(L1SkillId.MEDITATION);
			}
		}

		owner.setCallClanId(0);
		if (!owner.hasSkillEffect(L1SkillId.ABSOLUTE_BARRIER)) {
			owner.setRegenState(REGENSTATE_MOVE);
		}
		register();
	}

	@Override
	public void handle() {
		if (!validation())
			return;

		if (owner.isDead())
			return;

		try {
			owner.setHeading(h);
			owner.getLocation().set(nextX, nextY);
			owner.setLastMoveActionMillis(System.currentTimeMillis());

			// TODO 4速加速權宜之計（可能是Clao錯誤）：這就是問題所在
			owner.broadcastPacket(new S_MoveCharPacket(owner));
			// TODO 4速加速權宜之計（可能是Clao錯誤）：這就是問題所在

			MJMyMapViewService.service().onMoveObject(owner);
			int ztype = owner.getZoneType();
			if (ztype == 0) {
				if (owner.getSafetyZone()) {
					owner.sendPackets(new S_ACTION_UI(S_ACTION_UI.SAFETYZONE, false));
					owner.setSafetyZone(false);
				}
				owner.stopEinhasadTimer();
			} else {
				if (ztype == 1)
					owner.startEinhasadTimer();
				else
					owner.stopEinhasadTimer();
				if (!owner.getSafetyZone()) {
					owner.sendPackets(new S_ACTION_UI(S_ACTION_UI.SAFETYZONE, true));
					owner.setSafetyZone(true);
				}
			}

			if (owner.getMapId() == 4000 && owner.getX() == 32813 && (owner.getY() >= 32766 && owner.getY() <= 32768)) {
				L1DoorInstance door = null;
				for (L1Object object : L1World.getInstance().getObject()) {
					if (object instanceof L1DoorInstance) {
						door = (L1DoorInstance) object;
						if (door.getDoorId() == 2110) {
							if (door.getOpenStatus() == ActionCodes.ACTION_Open) {
								owner.start_teleport(32821, 32767, (short) 4001, owner.getHeading(), 169, true);
							} else {
								int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_GIRAN);
								owner.start_teleport(loc[0], loc[1], (short) loc[2], owner.getHeading(), 169, true);
								owner.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\aG通往盧雲城的橋已關閉。"));
							}
						}
					}
				}
			}

			if (owner.getMapId() == 4001 && owner.getX() == 32939 && (owner.getY() >= 32766 && owner.getY() <= 32767)) {
				owner.start_teleport(32809, 32795, (short) 4002, owner.getHeading(), 169, true);
			}

			if (!QueenAntController.isReady() && owner.getMapId() >= 15871 && owner.getMapId() <= 15899) {
				owner.start_teleport(33444, 32799, 4, owner.getHeading(), 18339, false);
			}
			if (!FIController.isReady() && owner.getMapId() == 70) {
				owner.start_teleport(33444, 32799, 4, owner.getHeading(), 18339, false);
			}

			if (owner.hasSkillEffect(L1SkillId.SHOCK_ATTACK) || owner.hasSkillEffect(L1SkillId.ENSNARE)) {
				owner.send_effect(20460);
			}

			MJCastleWarBusiness.move(owner);

			L1WorldTraps.getInstance().onPlayerMoved(owner);
			owner.getMap().setPassable(nextX, nextY, false);
			// TODO 派對原型
			L1Party party = owner.getParty();
			if (party != null) {
				party.onMoveMember(owner);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// if (owner.isBlackwizard()) {
			// L1SkillId.onMoveEffectHandle(所有者);
			// }
			// TODO 4速加速臨時解決方案（雲端錯誤的可能性）（全部處理完畢後，下面的資料包必須最後處理）
			if (owner.isFourgear()) {
				owner.broadcastPacket(SC_FOURTH_GEAR_NOTI.Fourth_Gear(owner));
			}
		}
	}

	@Override
	public boolean validation() {
		if (h < 0 || h > 7)
			return false;

		if (owner == null) {
			return false;
		}

		// TODO掃描測試（正面應高，背面應低：正常）
		// System.out.println("棚屋檢查：" + (System.currentTimeMillis()
		// -Owner.getLastMoveActionMillis()) + " " + getInterval() + " " +
		// Owner.hasSkillEffect(L1SkillId.FREEZE_AFTER_DELAY));
		// System.out.println("到："+owner.getName());
		if (MJPerformAdapter.CPU_USAGE < Config.Synchronization.CPU_USAGE_USER) {
			if (Config.Synchronization.WALKPOSITIONCHECK) {
				long lastMillis = owner.getLastMoveActionMillis();
				long currentMillis = System.currentTimeMillis();
				long buff = 0;
				// 使用 Lancer-Vanguard 時，如果偵測到掃描，則將數字增加 25。
				// 長間隔 = getInterval() -Config.Synchronization.VANGUARD_PER; //25
				long interval = getInterval();
				// System.out.println(interval);
				if (owner.hasSkillEffect(L1SkillId.TARAS_MOVE_SPEED)) {
					buff = 50;
				}
				if (lastMillis > 0) {
					if ((currentMillis - lastMillis + buff) < interval) {
						owner.addSpeedOverCount();
					} else {
						owner.delSpeedOverCount();
					}
				}

				long motion = owner.getLastMotionMillis();
				if (motion > 0) {
					if (lastMillis > 0) {
						if ((currentMillis - lastMillis) + motion < interval + motion) {
							owner.sendPackets(new S_PacketBox(S_PacketBox.使用者後端, owner));
						}
					}
					owner.setLastMotionMillis(0);
				}

				// System.out.println("현재 카운트 : " + owner.getSpeedOverCount() + " / " +
				// Config.Synchronization.SPEESOVERCHECKCOUNT + " / " +
				// Config.Synchronization.SPEESOVERCHECKCOUNT_AUTO);

				if (owner.getSpeedOverCount() == 1) {
					owner._move_speed_over_loc[0] = owner.getX();
					owner._move_speed_over_loc[1] = owner.getY();
					owner._move_speed_over_loc[2] = owner.getMapId();
				}

				if (owner.get_is_client_auto()) {
					if (owner.getSpeedOverCount() >= Config.Synchronization.SPEESOVERCHECKCOUNT_AUTO) {
						owner.start_teleport(owner._move_speed_over_loc[0], owner._move_speed_over_loc[1],
								owner._move_speed_over_loc[2], h, 18339, false);
						Gameguard(owner);
					}
				} else if (owner.getSpeedOverCount() >= Config.Synchronization.SPEESOVERCHECKCOUNT) {
					owner.start_teleport(owner._move_speed_over_loc[0], owner._move_speed_over_loc[1],
							owner._move_speed_over_loc[2], h, 18339, false);

					Gameguard(owner);
					return false;
				}
			}
		}

		// TODO 拍照時眩暈畫面被推後的現象請檢查這裡的技巧並進行測試。
		if (MJCommons.isLock(owner)) {
			owner.start_teleport(x, y, owner.getMapId(), h, 18339, false, false);
			return false;
		}
		owner.offFishing();
		owner.getMap().setPassable(x, y, true);
		int mid = owner.getMapId();
		try {
			int inn = MJINNMapInfoLoader.isInInnArea(nextX, nextY, mid);
			if (inn != -1) {
				if (MJINNRoom.input(owner, inn))
					return false;
			}

			short mapid = MJCopyMapObservable.getInstance().idenMap((short) mid);
			if (Dungeon.getInstance().dg(nextX, nextY, mapid, owner))
				return false;
			if (DungeonRandom.getInstance().dg(nextX, nextY, mapid, owner))
				return false;

			if (MJWalkFilterChain.getInstance().handle(owner, nextX, nextY))
				return false;

			if (owner.getMapId() == 15891) {
				if (owner.isErzabe_circle()) {
					owner.setErzabe_circle(false);
				}
				for (L1Object obj : L1World.getInstance().getVisibleObjects(owner, 1)) {
					if (obj instanceof L1MerchantInstance) {
						L1MerchantInstance npc = (L1MerchantInstance) obj;
						if (npc.getNpcId() == 8503183) {
							if (npc.getX() == nextX && npc.getY() == nextY && !owner.isDead()) {
								owner.setErzabe_circle(true);
							}
						}
					}
				}
			}

			// TODO 當特定 NPC 在附近時自動移動到該位置（現在在此處使用）
			for (L1Object obj : L1World.getInstance().getVisibleObjects(owner, 1)) {
				if (obj instanceof L1MerchantInstance) {
					L1MerchantInstance npc = (L1MerchantInstance) obj;
					if (npc.getNpcId() == 8503183) {
						if (npc.getX() == nextX && npc.getY() == nextY && !owner.isDead()) {
							owner.setErzabe_circle(true);
						}
					}

					if (npc.getNpcId() == 8503163) {
						for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
							if (pc.getLocation().getTileLineDistance(new Point(npc.getLocation())) < 2
									&& !pc.isDead()) {
								if (pc != null) {
									MJPoint pt = MJPoint.newInstance(32833, 32766, 15, (short) 15871, 50);
									pc.send_effect(12261, true);
									SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send(pc, pt.x, pt.y, pt.mapId,
											SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_DOM_TOWER);
									return true;
								}
							}
						}
					}

					if (npc.getNpcId() == 8503182) {
						for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
							if (pc.getLocation().getTileLineDistance(new Point(npc.getLocation())) < 2
									&& !pc.isDead()) {
								if (pc != null) {
									MJPoint pt = MJPoint.newInstance(32897, 32862, 5, (short) 15891, 50);
									pc.start_teleport(pt.x, pt.y, pt.mapId, pc.getHeading(), 18339, true);
								}
							}
						}
					}

					if (npc.getNpcId() == 8502094) {
						for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(npc, 10)) {// 10個空格內高效搜尋
							try {
								Thread.sleep(1000L);
								int newX = 33632 + 3;
								int newY = 32791 + 3;
								short mapId = 15482;
								if (pc.getLocation().getTileLineDistance(new Point(npc.getLocation())) < 8
										&& !pc.isDead()) {
									if (pc.getLevel() >= 60) {
										boolean check = MJCastleWarBusiness.getInstance().isNowWar(4);
										if (check) {
											// int[] loc = null;
											// loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_GIRAN_CASTLE);
											pc.send_effect(12261, true);
											SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send(pc, newX, newY, mapId,
													SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_INTER_TEST);
											return true;
										} else {
											pc.sendPackets("攻城戰未進行中。");
											return false;
										}
									} else {
										pc.sendPackets("限60級以上玩家進入。");
										return false;
									}
								}
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}

			if (!owner.isGm()) {
				if (!owner.getMap().isUserPassable(x, y, h) && !(owner.getMapId() == 800 || owner.getMapId() == 5490)) {
					owner.start_teleport(x, y, mid, h, 18339, false, false);
					return false;
				}
			}
			// 新增了XXX傳送返回區域（我不能只包含1個，所以我將其註解掉）
			// TeleporterActionListener 偵聽器 =
			// ActionListenerLoader.getInstance().findListener(ListenerFinderTable.getInstance().getListenerInfo(owner.getMapId()));
			// if (監聽器!= null) {
			// if (!listener.is_opened()) {
			// int[] loc = Getback.GetBack_Location(owner, false);
			// owner.start_teleport(loc[0], loc[1], loc[2],owner.getHeading(), 18339, true,
			// false);
			// 返回假；
			// }
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public int getRegistIndex() {
		return CharacterActionExecutor.ACTION_IDX_WALK;
	}

	// 新增 TODO 1000L
	@Override
	public long getInterval() {
		// return 0;//bug
		return owner == null ? 1000L : owner.getCurrentSpriteInterval(EActionCodes.fromInt(owner.getCurrentWeapon()));
	}

	@Override
	public boolean empty() {
		return false;
	}

	private static void Gameguard(final L1PcInstance pc) {
		pc.sendPackets(SC_WORLD_PUT_NOTI.make_stream(pc, pc.getMap().isUnderwater(), false));
		if (Config.LogStatus.WALKPOSITIONCHECK_LOG) {
			pc.sendPackets(SC_NOTIFICATION_MESSAGE.make_stream(Config.Message.SPEESOVERCHECKCOUNT_MESSAGE,
					MJSimpleRgb.green(), 5));
			pc.sendPackets(Config.Message.SPEESOVERCHECKCOUNT_MESSAGE);
			System.out.println(String.format("[使用外掛]確認為真（延遲檢查）：[角色名：%s] [違規次數：%d] [請繼續監視]", pc.getName(),
					Config.Synchronization.SPEESOVERCHECKCOUNT));
		}
		pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PANTHERA, true));
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				GameClient clnt = pc.getNetConnection();
				String name = pc.getName();
				int x = pc.getX();
				int y = pc.getY();
				int mapId = pc.getMapId();
				C_NewCharSelect.restartProcess(pc);// 先租
				try {
					Thread.sleep(700L);// 0.5秒
					pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PANTHERA, true));
					C_LoginToServer.doEnterWorld(name, clnt, false, x, y, mapId);// 再次嘗試連接
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 1500);// 1.5秒
	}
}
