 package l1j.server.server.utils;

 import java.io.IOException;

 public class ItemPresentOutStream
   extends BinaryOutputStream
 {
   public ItemPresentOutStream() {}

   public ItemPresentOutStream(int capacity) {
     super(capacity);
   }


   public void writeDMG(int SmallDmg, int LargeDmg) {
     writeC(1);
     writeC(SmallDmg);
     writeC(LargeDmg);
   }


   public void writeLargeDMG(int DMG) {
     writeC(2);
     writeC(DMG);
   }


   public void writeDurability(int Durability) {
     writeC(3);
     writeC(Durability);
   }


   public void writeTwoHand() {
     writeC(4);
   }


   public void writeWeaponHIT(int WeaponHIT) {
     writeC(5);
     writeC(WeaponHIT);
   }


   public void writeAddDMG(int AddDMG) {
     writeC(6);
     writeC(AddDMG);
   }


   public void writeShortAddDMG(int AddDMG) {
     writeC(47);
     writeC(AddDMG);
   }


   public void writeClass(int Class) {
     writeC(7);
     writeC(Class);
   }


   public void writeaSTR_Bu(int STR_Bu) {
     writeC(8);
     writeC(STR_Bu);
   }


   public void writeaDEX_Bu(int DEX_Bu) {
     writeC(9);
     writeC(DEX_Bu);
   }


   public void writeaCON_Bu(int ON_Bu) {
     writeC(10);
     writeC(ON_Bu);
   }


   public void writeaWIS_Bu(int WIS_Bu) {
     writeC(11);
     writeC(WIS_Bu);
   }


   public void writeaINT_Bu(int INT_Bu) {
     writeC(12);
     writeC(INT_Bu);
   }


   public void writeaCHA_Bu(int CHA_Bu) {
     writeC(13);
     writeC(CHA_Bu);
   }


   public void writeAddMaxHP(int AddMaxHP) {
     writeC(14);
     writeH(AddMaxHP);
   }


   public void writeAddMR(int AddMR) {
     writeC(15);
     writeH(AddMR);
   }


   public void writeMpDrain() {
     writeC(16);
   }


   public void writeAddSP(int AddSP) {
     writeC(17);
     writeC(AddSP);
   }


   public void writeHaste() {
     writeC(18);
   }


   public void writeaAcUP(int AcUP) {
     writeC(19);
     writeC(AcUP);
   }


   public void writeAddLuck(int AddLuck) {
     writeC(20);
     writeC(AddLuck);
   }


   public void writeFoodVolume(int FoodVolume) {
     writeC(21);
     writeH(FoodVolume);
   }


   public void writeLightRange(int LightRange) {
     writeC(22);
     writeH(LightRange);
   }


   public void writeMaterial(int Material, int Weight) {
     writeC(23);
     writeC(Material);
     writeD(Weight);
   }


   public void writeType(int type) {
     writeC(25);
     writeC(type);
   }


   public void writeLevel(int Level) {
     writeC(26);
     writeD(Level);
   }


   public void writeRegistFire(int Fire) {
     writeC(27);
     writeC(Fire);
   }


   public void writeRegistWater(int Water) {
     writeC(28);
     writeC(Water);
   }


   public void writeRegistWind(int Wind) {
     writeC(29);
     writeC(Wind);
   }


   public void writeRegistEarth(int Earth) {
     writeC(30);
     writeC(Earth);
   }


   public void writeHP(int HP) {
     writeC(31);
     writeC(HP);
   }


   public void writeMaxMP(int MaxMP) {
     writeC(32);
     writeH(MaxMP);
   }


   public void writeRegistSleep(int Sleep) {
     writeC(33);
     writeC(Sleep);
   }


   public void writeHpDrain() {
     writeC(34);
   }


   public void writeLongDMG(int LongDMG) {
     writeC(35);
     writeC(LongDMG);
   }


   public void writeLongHIT(int IongHIT) {
     writeC(24);
     writeC(IongHIT);
   }


   public void writeAddEXP(int AddEXP) {
     writeC(36);
     writeC(AddEXP);
   }


   public void writeAddHPPrecovery(int AddHPrecovery) {
     writeC(37);
     writeC(AddHPrecovery);
   }


   public void writeAddMPPrecovery(int AddMPPrecovery) {
     writeC(38);
     writeC(AddMPPrecovery);
   }


   public void writeAddStunHit(int AddStunHit) {
     writeC(39);
     writeS(String.format("眩暈擊中 +%d", new Object[] { Integer.valueOf(AddStunHit) }));
   }


   public void writeMagicHIT(int MagicHIT) {
     writeC(40);
     writeC(MagicHIT);
   }


   public void writeExp(int Exp) {
     writeC(41);
     writeC(Exp);
   }


   public void writeShortDMG(int DMG) {
     writeC(47);
     writeC(DMG);
   }


   public void writeShortHIT(int HIT) {
     writeC(48);
     writeC(HIT);
   }


   public void writeMagicCritical(int MagicCritical) {
     writeC(50);
     writeH(MagicCritical);
   }


   public void writeAddAc(int Ac) {
     writeC(56);
     writeC(Ac);
   }


   public void writeaSecond(int Second) {
     writeC(58);
     writeC(Second);
   }


   public void writePVPAddDMG(int PVPAddDMG) {
     writeC(59);
     writeC(PVPAddDMG);
   }


   public void writePVPAddDMGdown(int PVPAddDMGdown) {
     writeC(60);
     writeC(PVPAddDMGdown);
   }


   public void writeAutoDelete(int Delete) {
     writeC(61);
     writeD(Delete);
   }


   public void writeDMGdown(int DMGdown) {
     writeC(63);
     writeC(DMGdown);
   }


   public void writeDMGdownprobability(int DMGdownprobability, int dmgdown) {
     writeC(64);
     writeC(DMGdownprobability);
     writeC(dmgdown);
   }


   public void writePotionrecovery(int Potionrecovery, int Percent) {
     writeC(65);
     writeC(Potionrecovery);
     writeC(Percent);
   }


   public void writeAddWeightPer(int WeightPer) {
     writeC(65);
     writeC(WeightPer);
   }


   public void writeSetItem() {
     writeC(69);
   }


   public void writeSetItemOption(int Option) {
     writeC(71);
     writeH(Option);
   }


   public void writeMagic(String Magic) {
     writeC(73);
     writeS(Magic);
   }


   public void writeMagic2(String Magic) {
     writeC(74);
     writeS(Magic);
   }


   public void writeLawful(int Lawful) {
     writeC(75);
     writeC(Lawful);
   }


   public void writeStep(int Step) {
     writeC(77);
     writeC(Step);
   }


   public void writeAttr(int Attr) {
     writeC(78);
     writeC(Attr);
   }


   public void writeUseLevel(int UseLevel) {
     writeC(79);
     writeC(UseLevel);
   }


   public void writeaHPUP(int HPUP) {
     writeC(87);
     writeC(HPUP);
   }


   public void writeaMPUP(int MPUP) {
     writeC(88);
     writeC(MPUP);
   }


   public void writeMagicDodge(int MagicDodge) {
     writeC(89);
     writeD(MagicDodge);
   }


   public void writeAddWeight(int AddWeight) {
     writeC(90);
     writeH(AddWeight);
   }


   public void writePenetrate(int Penetrate) {
     writeC(94);
     writeC(Penetrate);
   }


   public void writeAddDmgPer(int Dmg, int Dmgper) {
     writeC(95);
     writeC(Dmg);
     writeC(Dmgper);
   }


   public void writeHealDefence(int Defence) {
     writeC(96);
     writeC(Defence);
   }


   public void writeReductiondown(int Reductiondown) {
     writeC(97);
     writeC(Reductiondown);
   }


   public void writeLongCritical(int longCritical) {
     writeC(99);
     writeC(longCritical);
   }


   public void writeShortCritical(int ShortCritical) {
     writeC(100);
     writeC(ShortCritical);
   }


   public void writeaFouslayer(int Fouslayer) {
     writeC(101);
     writeC(Fouslayer);
   }


   public void writeaTitan(int Titan) {
     writeC(102);
     writeC(Titan);
   }


   public void writeaPercentDmg(int Dmg) {
     writeC(103);
     writeC(Dmg);
   }


   public void writeWeaponDmg(int SmallDmg, int LargeDmg) {
     writeC(107);
     writeC(SmallDmg);
     writeC(LargeDmg);
   }


   public void writeAttrDmg(int AttrDmg) {
     writeC(109);
     writeC(AttrDmg);
   }


   public void writeLimitLevel(int MinLevel, int MaxLevel) {
     writeC(111);
     writeC(MinLevel);
     writeH(MaxLevel);
   }


   public void writeLimitTime(int Time) {
     writeC(112);
     writeD(Time);
   }


   public void writeaBlesssomo(int Blesssomo) {
     writeC(116);
     writeH(Blesssomo);
   }


   public void writeability_resis(int ability_resis) {
     writeC(117);
     writeC(ability_resis);
   }


   public void writeaspirit_resis(int spirit_resis) {
     writeC(118);
     writeC(spirit_resis);
   }


   public void writeadragonS_resis(int dragonS_resis) {
     writeC(119);
     writeC(dragonS_resis);
   }


   public void writeafear_resis(int fear_resis) {
     writeC(120);
     writeC(fear_resis);
   }


   public void writeaAll_resis(int All_resis) {
     writeC(121);
     writeC(All_resis);
   }


   public void writeability_pierce(int ability_pierce) {
     writeC(122);
     writeC(ability_pierce);
   }


   public void writeaspirit_pierce(int spirit_pierce) {
     writeC(123);
     writeC(spirit_pierce);
   }


   public void writeadragonS_pierce(int dragonS_pierce) {
     writeC(124);
     writeC(dragonS_pierce);
   }


   public void writeafear_pierce(int fear_pierce) {
     writeC(125);
     writeC(fear_pierce);
   }


   public void writeaAll_pierce(int All_pierc) {
     writeC(126);
     writeC(All_pierc);
   }


   public void writeAddStun(int AddStun) {
     writeH(1313);
     writeC(AddStun);
   }

   public void writeString(String message, double d) throws IOException {
     writeC(39);
     writeS(String.format("\\f3%s: \\aA%s 船", new Object[] { message, Double.valueOf(d) }));
   }

   public void writeStringS(String message) throws IOException {
     writeC(39);
     writeS(String.format("%s", new Object[] { message }));
   }

   public void writeOption(String message, int d) throws IOException {
     writeC(39);
     writeS(String.format("\\fI%s: \\aA+%s", new Object[] { message, Integer.valueOf(d) }));
   }

   public void writeOptionMagicdmg(String message, int d) throws IOException {
     writeC(39);
     writeS(String.format("\\fI%s: \\aA魔法傷害 +%s", new Object[] { message, Integer.valueOf(d) }) + "%");
   }

   public void writeOptionA(String message, int d) throws IOException {
     writeC(39);
     writeS(String.format("\\fI%s: \\aA+%s", new Object[] { message, Integer.valueOf(d) }));
   }
 }


