package l1j.server.server.utils;

import java.io.IOException;

public class ItemPresentOutStream extends l1j.server.server.utils.BinaryOutputStream {
	public ItemPresentOutStream() {
		super();
	}

	public ItemPresentOutStream(int capacity) {
		super(capacity);
	}

	// TODO 小怪物傷害
	public void writeDMG(int SmallDmg, int LargeDmg) {
		writeC(1);
		writeC(SmallDmg);
		writeC(LargeDmg);
	}

	// TODO 大怪物傷害
	public void writeLargeDMG(int DMG) {
		writeC(2);
		writeC(DMG);
	}

	// TODO 耐久度
	public void writeDurability(int Durability) {
		writeC(3);
		writeC(Durability);
	}

	// TODO 雙手武器
	public void writeTwoHand() {
		writeC(4);
	}

	// TODO 武器命中
	public void writeWeaponHIT(int WeaponHIT) {
		writeC(5);
		writeC(WeaponHIT);
	}

	// TODO 額外傷害
	public void writeAddDMG(int AddDMG) {
		writeC(6);
		writeC(AddDMG);
	}

	// TODO 追加傷害
	public void writeShortAddDMG(int AddDMG) {
		writeC(47);
		writeC(AddDMG);
	}

	// TODO 可用的職業
	public void writeClass(int Class) {
		writeC(7);
		writeC(Class);
	}

	// TODO STR
	public void writeaSTR_Bu(int STR_Bu) {
		writeC(8);
		writeC(STR_Bu);
	}

	// TODO DEX
	public void writeaDEX_Bu(int DEX_Bu) {
		writeC(9);
		writeC(DEX_Bu);
	}

	// TODO CON
	public void writeaCON_Bu(int ON_Bu) {
		writeC(10);
		writeC(ON_Bu);
	}

	// TODO WIS
	public void writeaWIS_Bu(int WIS_Bu) {
		writeC(11);
		writeC(WIS_Bu);
	}

	// TODO INT
	public void writeaINT_Bu(int INT_Bu) {
		writeC(12);
		writeC(INT_Bu);
	}

	// TODO CHA
	public void writeaCHA_Bu(int CHA_Bu) {
		writeC(13);
		writeC(CHA_Bu);
	}

	// TODO 最大 HP
	public void writeAddMaxHP(int AddMaxHP) {
		writeC(14);
		writeH(AddMaxHP);
	}

	// TODO MR
	public void writeAddMR(int AddMR) {
		writeC(15);
		writeH(AddMR);
	}

	// TODO MP 吸收
	public void writeMpDrain() {
		writeC(16);
	}

	// TODO SP
	public void writeAddSP(int AddSP) {
		writeC(17);
		writeC(AddSP);
	}

	// TODO 加速效果
	public void writeHaste() {
		writeC(18);
	}

	// TODO AC (不同標記不太可能出現)
	public void writeaAcUP(int AcUP) {
		writeC(19);
		writeC(AcUP);
	}

	// TODO 幸運
	public void writeAddLuck(int AddLuck) {
		writeC(20);
		writeC(AddLuck);
	}

	// TODO 營養
	public void writeFoodVolume(int FoodVolume) {
		writeC(21);
		writeH(FoodVolume);
	}

	// TODO 亮度
	public void writeLightRange(int LightRange) {
		writeC(22);
		writeH(LightRange);
	}

	// TODO 材質
	public void writeMaterial(int Material, int Weight) {
		writeC(23);
		writeC(Material);
		writeD(Weight);
	}

	// TODO 種類 ??
	public void writeType(int type) {
		writeC(25);
		writeC(type);
	}

	// TODO 等級
	public void writeLevel(int Level) {
		writeC(26);
		writeD(Level);
	}

	// TODO 火屬性抗性
	public void writeRegistFire(int Fire) {
		writeC(27);
		writeC(Fire);
	}

	// TODO 水屬性抗性
	public void writeRegistWater(int Water) {
		writeC(28);
		writeC(Water);
	}

	// TODO 風屬性抗性
	public void writeRegistWind(int Wind) {
		writeC(29);
		writeC(Wind);
	}

	// TODO 火屬性抗性
	public void writeRegistEarth(int Earth) {
		writeC(30);
		writeC(Earth);
	}

	// TODO HP (不使用)
	public void writeHP(int HP) {
		writeC(31);
		writeC(HP);
	}

	// TODO 最大 MP
	public void writeMaxMP(int MaxMP) {
		writeC(32);
		writeH(MaxMP);
	}

	// TODO 睡眠抗性 (不使用)
	public void writeRegistSleep(int Sleep) {
		writeC(33);
		writeC(Sleep);
	}

	// TODO HP 吸收
	public void writeHpDrain() {
		writeC(34);
	}

	// TODO 遠距離傷害
	public void writeLongDMG(int LongDMG) {
		writeC(35);
		writeC(LongDMG);
	}

	// TODO 遠距離命中
	public void writeLongHIT(int IongHIT) {
		writeC(24);
		writeC(IongHIT);
	}

	// TODO 經驗值獎勵
	public void writeAddEXP(int AddEXP) {
		writeC(36);
		writeC(AddEXP);
	}

	// TODO HP 恢復
	public void writeAddHPPrecovery(int AddHPrecovery) {
		writeC(37);
		writeC(AddHPrecovery);
	}

	// TODO MP 恢復
	public void writeAddMPPrecovery(int AddMPPrecovery) {
		writeC(38);
		writeC(AddMPPrecovery);
	}

	// TODO 眩暈命中
	public void writeAddStunHit(int AddStunHit) {
		writeC(39);
		writeS(String.format("眩暈命中 +%d", AddStunHit));
	}

	// TODO 魔法命中
	public void writeMagicHIT(int MagicHIT) {
		writeC(40);
		writeC(MagicHIT);
	}

	// TODO 經驗值 (不使用) 1 = 0.01
	public void writeExp(int Exp) {
		writeC(41);
		writeC(Exp);
	}

	// TODO 近距離傷害
	public void writeShortDMG(int DMG) {
		writeC(47);
		writeC(DMG);
	}

	// TODO 近距離命中
	public void writeShortHIT(int HIT) {
		writeC(48);
		writeC(HIT);
	}

	// TODO 魔法致命一擊
	public void writeMagicCritical(int MagicCritical) {
		writeC(50);
		writeH(MagicCritical);
	}

	// TODO 額外防禦力
	public void writeAddAc(int Ac) {
		writeC(56);
		writeC(Ac);
	}

	// TODO 秒 (時間)
	public void writeaSecond(int Second) {
		writeC(58);
		writeC(Second);
	}

	// TODO PVP額外傷害
	public void writePVPAddDMG(int PVPAddDMG) {
		writeC(59);
		writeC(PVPAddDMG);
	}

	// TODO PVP傷害減少
	public void writePVPAddDMGdown(int PVPAddDMGdown) {
		writeC(60);
		writeC(PVPAddDMGdown);
	}

	// TODO 自動刪除
	public void writeAutoDelete(int Delete) {
		writeC(61);
		writeD(Delete);
	}

	// TODO 傷害減少
	public void writeDMGdown(int DMGdown) {
		writeC(63);
		writeC(DMGdown);
	}

	// TODO 傷害減少機率
	public void writeDMGdownprobability(int DMGdownprobability, int dmgdown) {
		writeC(64);
		writeC(DMGdownprobability);
		writeC(dmgdown);
	}

	// TODO 藥水恢復量
	public void writePotionrecovery(int Potionrecovery, int Percent) {
		writeC(65);
		writeC(Potionrecovery);
		writeC(Percent);
	}

	// TODO 攜帶重量增加（機率）
	public void writeAddWeightPer(int WeightPer) {
		writeC(65);
		writeC(WeightPer);
	}

	// TODO 套裝物品
	public void writeSetItem() {
		writeC(69);
	}

	// TODO 套裝物品選項
	public void writeSetItemOption(int Option) {
		writeC(71);
		writeH(Option);
	}

	// TODO 魔法發動
	public void writeMagic(String Magic) {
		writeC(73);
		writeS(Magic);
	}

	// TODO 魔法發動2
	public void writeMagic2(String Magic) {
		writeC(74);
		writeS(Magic);
	}

	// TODO 性向
	public void writeLawful(int Lawful) {
		writeC(75);
		writeC(Lawful);
	}

	// TODO 階段
	public void writeStep(int Step) {
		writeC(77);
		writeC(Step);
	}

	// TODO 屬性
	public void writeAttr(int Attr) {
		writeC(78);
		writeC(Attr);
	}

	// TODO 使用等級
	public void writeUseLevel(int UseLevel) {
		writeC(79);
		writeC(UseLevel);
	}

	// TODO HP絕對恢復（32秒）
	public void writeaHPUP(int HPUP) {
		writeC(87);
		writeC(HPUP);
	}

	// TODO MP絕對恢復（64秒）
	public void writeaMPUP(int MPUP) {
		writeC(88);
		writeC(MPUP);
	}

	// TODO 機率魔法躲避
	public void writeMagicDodge(int MagicDodge) {
		writeC(89);
		writeD(MagicDodge);
	}

	// TODO 攜帶重量增加（+數字）
	public void writeAddWeight(int AddWeight) {
		writeC(90);
		writeH(AddWeight);
	}

	// TODO 貫穿效果
	public void writePenetrate(int Penetrate) {
		writeC(94);
		writeC(Penetrate);
	}

	// TODO 額外傷害機率
	public void writeAddDmgPer(int Dmg, int Dmgper) {
		writeC(95);
		writeC(Dmg);
		writeC(Dmgper);
	}

	// TODO 防止恢復惡化（恐懼）
	public void writeHealDefence(int Defence) {
		writeC(96);
		writeC(Defence);
	}

	// TODO 無視傷害減免
	public void writeReductiondown(int Reductiondown) {
		writeC(97);
		writeC(Reductiondown);
	}

	// TODO 遠程致命一擊
	public void writeLongCritical(int longCritical) {
		writeC(99);
		writeC(longCritical);
	}

	// TODO 近距離致命一擊
	public void writeShortCritical(int ShortCritical) {
		writeC(100);
		writeC(ShortCritical);
	}

	// TODO 對敵人處斬傷害
	public void writeaFouslayer(int Fouslayer) {
		writeC(101);
		writeC(Fouslayer);
	}

	// TODO 泰坦系列發動區間 3%
	public void writeaTitan(int Titan) {
		writeC(102);
		writeC(Titan);
	}

	// TODO 概率性近距離傷害
	public void writeaPercentDmg(int Dmg) {
		writeC(103);
		writeC(Dmg);
	}

	// TODO 傷害顯示
	public void writeWeaponDmg(int SmallDmg, int LargeDmg) {
		writeC(107);
		writeC(SmallDmg);
		writeC(LargeDmg);
	}

	// TODO 武器屬性傷害
	public void writeAttrDmg(int AttrDmg) {
		writeC(109);
		writeC(AttrDmg);
	}

	// TODO 等級限制
	public void writeLimitLevel(int MinLevel, int MaxLevel) {
		writeC(111);
		writeC(MinLevel);
		writeH(MaxLevel);
	}

	// TODO 限制時間
	public void writeLimitTime(int Time) {
		writeC(112);
		writeD(Time);
	}

	// TODO 祝福消耗效率 (因為其他表示方式可能不會出現)
	public void writeaBlesssomo(int Blesssomo) {
		writeC(116);
		writeH(Blesssomo);
	}

	// TODO 技術耐性
	public void writeability_resis(int ability_resis) {
		writeC(117);
		writeC(ability_resis);
	}

	// TODO 精靈耐性
	public void writeaspirit_resis(int spirit_resis) {
		writeC(118);
		writeC(spirit_resis);
	}

	// TODO 龍語耐性
	public void writeadragonS_resis(int dragonS_resis) {
		writeC(119);
		writeC(dragonS_resis);
	}

	// TODO 恐懼耐性
	public void writeafear_resis(int fear_resis) {
		writeC(120);
		writeC(fear_resis);
	}

	// TODO 所有耐性
	public void writeaAll_resis(int All_resis) {
		writeC(121);
		writeC(All_resis);
	}

	// TODO 技術命中
	public void writeability_pierce(int ability_pierce) {
		writeC(122);
		writeC(ability_pierce);
	}

	// TODO 精靈命中
	public void writeaspirit_pierce(int spirit_pierce) {
		writeC(123);
		writeC(spirit_pierce);
	}

	// TODO 龍語命中
	public void writeadragonS_pierce(int dragonS_pierce) {
		writeC(124);
		writeC(dragonS_pierce);
	}

	// TODO 恐懼命中
	public void writeafear_pierce(int fear_pierce) {
		writeC(125);
		writeC(fear_pierce);
	}

	// TODO 所有命中
	public void writeaAll_pierce(int All_pierc) {
		writeC(126);
		writeC(All_pierc);
	}

	// TODO 眩暈耐性
	public void writeAddStun(int AddStun) {
		writeH(0x521);
		writeC(AddStun);
	}

	public void writeString(String message, double d) throws IOException {
		writeC(39);
		writeS(String.format("\\f3%s: \\aA%s 배", message, d));
	}
	
	public void writeStringS(String message) throws IOException {
		writeC(39);
		writeS(String.format("%s", message));
	}
	
	public void writeOption(String message, int d) throws IOException {
		writeC(39);
		writeS(String.format("\\fI%s: \\aA+%s", message, d));
	}
	
	public void writeOptionMagicdmg(String message, int d) throws IOException {
		writeC(39);
		writeS(String.format("\fI%s: \\aA魔法傷害 +%s", message, d) + "%");
	}
	
	public void writeOptionA(String message, int d) throws IOException {
		writeC(39);
		writeS(String.format("\\fI%s: \\aA+%s", message, d));
	}

}
