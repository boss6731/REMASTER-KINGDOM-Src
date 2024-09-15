package l1j.server.server.model;

import java.util.ArrayList;
import java.util.Random;

import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.RepeatTask;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_SkillSound;

/**
 * 幻術師立方體類別
 */
@SuppressWarnings("unchecked")
public class L1Cube {

	/** 立方體列表 */
	private ArrayList<L1NpcInstance> CUBE[] = new ArrayList[4];

	/** 單一類別 */
	private static L1Cube instance;
	private static Random _random = new Random(System.nanoTime());

	/** 實例初始化 */
	{
		for (int i = 0; i < CUBE.length; i++)
			CUBE[i] = new ArrayList<L1NpcInstance>();
	}

	/**
	 * 返回立方體類別
	 *
	 * @return 單一類別物件
	 */
	public static L1Cube getInstance() {
		if (instance == null)
			instance = new L1Cube();
		return instance;
	}

	/**
	 * 返回立方體列表
	 *
	 * @param index
	 *            列表索引
	 */
	private L1NpcInstance[] toArray(int index) {
		return CUBE[index].toArray(new L1NpcInstance[CUBE[index].size()]);
	}

	/**
	 * 註冊立方體列表
	 *
	 * @param index
	 *            列表索引
	 * @param npc
	[05:35]
	 *            要註冊的NPC物件
	 */
	public void add(int index, L1NpcInstance npc) {
		if (!CUBE[index].contains(npc)) {
			CUBE[index].add(npc);
		}
	}
	/*
	* @param npc
	*            要刪除的NPC物件
	*/
	private void remove(int index, L1NpcInstance npc) {
		if (CUBE[index].contains(npc)) {
			CUBE[index].remove(npc);
		}
	}

	/** 私有 */
	private L1Cube() {
		GeneralThreadPool.getInstance().execute(new CUBE1());
		GeneralThreadPool.getInstance().execute(new CUBE2());
		GeneralThreadPool.getInstance().execute(new CUBE3());
		GeneralThreadPool.getInstance().execute(new CUBE4());
	}

	/** 一階段 */
	class CUBE1 extends RepeatTask {
		public CUBE1() {
			super(1000);
		}

		@Override
		public void execute() {
			try {
				for (L1NpcInstance npc : toArray(0)) {
					// 如果持續時間結束
					if (npc == null || npc.Cube()) {
						try{
							npc.setCubePc(null);
							remove(0, npc);
						} catch (Exception e) {
						}
						continue;
					} else {
							// 搜索3個單元內的玩家
							// 立方體擁有者的盟友
							// 其他血盟暫時為敵
						for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(npc, 3)) {
							if (pc == null)
								continue;
								// 如果立方體內的人是施法者或盟友
							if (npc.CubePc().getId() == pc.getId() || npc.CubePc().getClanid() > 0
									&& npc.CubePc().getClanid() == pc.getClanid()) {
								if (!pc.hasSkillEffect(L1SkillId.STATUS_IGNITION)) {
									pc.getResistance().addFire(30);
									pc.setSkillEffect(L1SkillId.STATUS_IGNITION, 8 * 1000);
									pc.sendPackets(new S_OwnCharAttrDef(pc));
								} else {
									pc.setSkillEffect(L1SkillId.STATUS_IGNITION, 8 * 1000);
									pc.sendPackets(new S_OwnCharAttrDef(pc));
								}
								pc.sendPackets(new S_SkillSound(pc.getId(), 6708));
							} else if (npc.CubePc().getClanid() > 0 && npc.CubePc().getClanid() != pc.getClanid()) {
								boolean isNowWar = false;
								int castleId = L1CastleLocation.getCastleIdByArea(pc);
								if (castleId != 0) {
									isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleId);
								}

								if (isNowWar && !pc.hasSkillEffect(L1SkillId.ICE_LANCE)
										&& !pc.hasSkillEffect(L1SkillId.MOB_COCA)
										&& !pc.hasSkillEffect(L1SkillId.MOB_BASILL)
										&& !pc.hasSkillEffect(L1SkillId.EARTH_BIND)
										&& pc.getZoneType() == 0) {
									if (_random.nextInt(100) < 20) {
										pc.receiveDamage(npc.CubePc(), 15);
										pc.sendPackets(new S_SkillSound(pc.getId(), 6709));
									}
								}
							}
						}
						npc.setCubeTime(4);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class CUBE1 extends RepeatTask {
		public CUBE1() {
			super(1000);
		}

		@Override
		public void execute() {
			try {
				for (L1NpcInstance npc : toArray(0)) {
					// 如果持續時間結束
					if (npc == null || npc.Cube()) {
						try{
							npc.setCubePc(null);
							remove(0, npc);
						} catch (Exception e) {
						}
						continue;
					} else {
						// 搜索3個單元內的玩家
						// 立方體擁有者的盟友
						// 其他血盟暫時為敵
						for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(npc, 3)) {
							if (pc == null)
								continue;

							// 如果立方體內的人是施法者或盟友
						}

						/** 二階段 */
						class CUBE2 extends RepeatTask {
							public CUBE2() {
								super(1000);
							}

							@Override
							public void execute() {
								try {
									for (L1NpcInstance npc : toArray(1)) {
										// 如果持續時間結束
										if (npc == null || npc.Cube()) {
											try {
												npc.setCubePc(null);
												remove(1, npc);
											} catch (Exception e) {
											}
											continue;
										} else {
											// 搜索3個單元內的玩家
											// 立方體擁有者的盟友
											// 其他血盟暫時為敵
											for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(npc, 3)) {
												if (pc == null) continue;
												if(npc.CubePc() == null) continue;
												// 如果立方體內的人是施法者或盟友
												if (npc.CubePc().getId() == pc.getId() || npc.CubePc().getClanid() > 0
														&& npc.CubePc().getClanid() == pc.getClanid()) {
													if (!pc.hasSkillEffect(L1SkillId.STATUS_QUAKE)) {
														pc.getResistance().addEarth(30);
														pc.setSkillEffect(L1SkillId.STATUS_QUAKE, 8 * 1000);
														pc.sendPackets(new S_OwnCharAttrDef(pc));
													} else {
														pc.setSkillEffect(L1SkillId.STATUS_QUAKE, 8 * 1000);
														pc.sendPackets(new S_OwnCharAttrDef(pc));
													}
													pc.sendPackets(new S_SkillSound(pc.getId(), 6714));
												} else if (npc.CubePc().getClanid() > 0 && npc.CubePc().getClanid() != pc.getClanid()) {
													boolean isNowWar = false;
													int castleId = L1CastleLocation.getCastleIdByArea(pc);
													if (castleId != 0) {
														isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleId);
													}

													if (isNowWar && pc.getZoneType() == 0) {
														if (_random.nextInt(100) < 20) {
															pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, true));
															pc.setSkillEffect(L1SkillId.STATUS_FREEZE, 1 * 1000);
															pc.sendPackets(new S_SkillSound(pc.getId(), 6715));
														}
													}
												}
											}
											npc.setCubeTime(4);
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}


	/** 三階段 */

	class CUBE3 extends RepeatTask {

		public CUBE3() {

			super(1000);

		}



		@Override

		public void execute() {

			try {

				for (L1NpcInstance npc : toArray(2)) {

					// 如果持續時間結束

					if (npc == null || npc.Cube()) {

						try {

							npc.setCubePc(null);

							remove(2, npc);

							npc.deleteMe();

						} catch (Exception e) {

						}

						continue;

					} else {

						// 搜索 3 單元格內的玩家

						// 擁有立方體的人的盟友

						// 其他血職位敵人

						for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(npc, 3)) {

							if (pc == null)

								continue;



							if (npc == null || npc.CubePc() == null)

								break;



							// 如果立方體內的人是施法者或同一盟友
							if (npc.CubePc().getId() == pc.getId() || npc.CubePc().getClanid() > 0
									&& npc.CubePc().getClanid() == pc.getClanid()) {
								if (!pc.hasSkillEffect(L1SkillId.STATUS_SHOCK)) {
									pc.getResistance().addWind(30);
									pc.setSkillEffect(L1SkillId.STATUS_SHOCK, 8 * 1000);
									pc.sendPackets(new S_OwnCharAttrDef(pc));
								} else {
									pc.setSkillEffect(L1SkillId.STATUS_SHOCK, 8 * 1000);
									pc.sendPackets(new S_OwnCharAttrDef(pc));
								}
								pc.sendPackets(new S_SkillSound(pc.getId(), 6720));
							} else if (npc.CubePc().getClanid() > 0 && npc.CubePc().getClanid() != pc.getClanid()) {
								boolean isNowWar = false;
								int castleId = L1CastleLocation.getCastleIdByArea(pc);
								if (castleId != 0) {
									isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleId);
								}

								if (isNowWar && pc.getZoneType() == 0) {
									if (_random.nextInt(100) < 20) {
										pc.setSkillEffect(L1SkillId.STATUS_DESHOCK, 8 * 1000);
										pc.sendPackets(new S_SkillSound(pc.getId(), 6721));
									}
								}
							}
						}
						npc.setCubeTime(4);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/** 四階段 */
	class CUBE4 extends RepeatTask {
		CUBE4() {
			super(1000);
		}

		@Override
		public void execute() {
			try {
				for (L1NpcInstance npc : toArray(3)) {
					// 如果持續時間結束
					if (npc == null || npc.Cube()) {
						try {
							npc.setCubePc(null);
							remove(3, npc);
						} catch (Exception e) {
						}
						continue;
					} else {
	// 搜索 3 單元格內的玩家
	// 擁有立方體的人的盟友
	// 其他血職位敵人
	if (npc.getZoneType() == 1) {
		continue;
	}

						for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(npc, 3)) {
							if (pc != null) {
								if (pc.getCurrentHp() > 25 && !pc.hasSkillEffect(L1SkillId.ICE_LANCE)
										&& !pc.hasSkillEffect(L1SkillId.MOB_COCA)
										&& !pc.hasSkillEffect(L1SkillId.MOB_BASILL)
										&& !pc.hasSkillEffect(L1SkillId.EARTH_BIND)
										) {
									pc.receiveDamage(npc.CubePc(), 5);
									pc.setCurrentMp(pc.getCurrentMp() + 1);
									pc.sendPackets(new S_SkillSound(pc.getId(), 6727));
								}
							}
						}
						npc.setCubeTime(5);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}