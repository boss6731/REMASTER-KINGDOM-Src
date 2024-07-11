     package l1j.server.server.utils;

     import java.util.ArrayList;
     import java.util.logging.Logger;
     import l1j.server.Config;
     import l1j.server.FatigueProperty;
     import l1j.server.MJCaptchaSystem.Loader.MJCaptchaLoadManager;
     import l1j.server.MJCaptchaSystem.MJCaptcha;
     import l1j.server.MJCompanion.Basic.HuntingGround.MJCompanionHuntingGround;
     import l1j.server.MJCompanion.Instance.MJCompanionInstance;
     import l1j.server.MJCompanion.MJCompanionSettings;
     import l1j.server.MJPassiveSkill.MJPassiveID;
     import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
     import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
     import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
     import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_REST_EXP_INFO_NOTI;
     import l1j.server.MJTemplate.MJRnd;
     import l1j.server.server.Account;
     import l1j.server.server.GameServerSetting;
     import l1j.server.server.datatables.BonusExpTable;
     import l1j.server.server.datatables.ExpTable;
     import l1j.server.server.datatables.PartyMapInfoTable;
     import l1j.server.server.datatables.PetTable;
     import l1j.server.server.datatables.SpecialMapTable;
     import l1j.server.server.model.Instance.L1NpcInstance;
     import l1j.server.server.model.Instance.L1PcInstance;
     import l1j.server.server.model.Instance.L1PetInstance;
     import l1j.server.server.model.Instance.L1SummonInstance;
     import l1j.server.server.model.L1Character;
     import l1j.server.server.model.L1Clan;
     import l1j.server.server.model.L1Object;
     import l1j.server.server.model.L1World;
     import l1j.server.server.serverpackets.S_PacketBox;
     import l1j.server.server.serverpackets.S_ServerMessage;
     import l1j.server.server.serverpackets.ServerBasePacket;
     import l1j.server.server.templates.L1BonusExp;
     import l1j.server.server.templates.L1Pet;
     import l1j.server.server.templates.L1SpecialMap;
     import l1j.server.server.types.Point;


     public class CalcExp
     {
       private static final long serialVersionUID = 1L;
       private static Logger _log = Logger.getLogger(CalcExp.class.getName());




       public static void calcExp(L1PcInstance l1pcinstance, int targetid, ArrayList<?> acquisitorList, ArrayList<?> hateList, long exp) {
         if (l1pcinstance == null) {
           return;
         }

         int i = 0;
         double party_level = 0.0D;
         double dist = 0.0D;
         long member_exp = 0L;
         int member_lawful = 0;
         L1Object l1object = L1World.getInstance().findObject(targetid);
         L1NpcInstance npc = (L1NpcInstance)l1object;

         if (l1pcinstance.getLevel() < 55) {
           int add_lawful = (int)(npc.getLawful() * Config.ServerRates.RateLawful) * -1;
           l1pcinstance.addLawful(add_lawful);
           double exppenalty = ExpTable.getPenaltyRate(l1pcinstance.getLevel());
           long add_exp = (long)(exp * exppenalty * Config.ServerRates.RateXpClaudia);
           if (l1pcinstance.getLevel() < 5) {
             add_exp *= 5L;
           }
           if (BonusExpTable.getInstance().isExpBonusLv(l1pcinstance.getLevel())) {
             L1BonusExp temp = BonusExpTable.getInstance().getExpBonusLv(l1pcinstance.getLevel());
             if (temp != null) {
               add_exp = (long)(add_exp * temp.getExpBonus());
             }
           }

           l1pcinstance.add_exp(add_exp);


           return;
         }

         int hate = 0;
         long acquire_exp = 0L;
         int acquire_lawful = 0;
         long party_exp = 0L;
         int party_lawful = 0;
         long totalHateExp = 0L;
         int totalHateLawful = 0;
         long partyHateExp = 0L;
         int partyHateLawful = 0;
         long ownHateExp = 0L;

         if (acquisitorList.size() != hateList.size()) {
           return;
         }
         for (i = hateList.size() - 1; i >= 0; i--) {
           L1PcInstance l1PcInstance; L1Character acquisitor = (L1Character)acquisitorList.get(i);
           if (acquisitor instanceof MJCompanionInstance)
             l1PcInstance = ((MJCompanionInstance)acquisitor).get_master();
           hate = ((Integer)hateList.get(i)).intValue();
           if (l1PcInstance != null && !l1PcInstance.isDead()) {
             totalHateExp += hate;
             if (l1PcInstance instanceof L1PcInstance) {
               totalHateLawful += hate;
             }
           } else {
             acquisitorList.remove(i);
             hateList.remove(i);
           }
         }
         if (totalHateExp == 0L) {
           return;
         }

         if (l1object != null && !(npc instanceof L1PetInstance) && !(npc instanceof L1SummonInstance)) {

           if (!L1World.getInstance().isProcessingContributionTotal() && l1pcinstance.getHomeTownId() > 0) {
             int contribution = npc.getLevel() / 10;
             l1pcinstance.addContribution(contribution);
           }
           int lawful = npc.getLawful();

           if (l1pcinstance.isInParty() && getPartyIsinScreen(l1pcinstance) > 1) {
             partyHateExp = 0L;
             partyHateLawful = 0;
             for (i = hateList.size() - 1; i >= 0; i--) {
               L1PcInstance l1PcInstance; L1Character acquisitor = (L1Character)acquisitorList.get(i);
               if (acquisitor instanceof MJCompanionInstance) {
                 l1PcInstance = ((MJCompanionInstance)acquisitor).get_master();
               }
               hate = ((Integer)hateList.get(i)).intValue();
               if (l1PcInstance instanceof L1PcInstance) {
                 L1PcInstance pc = l1PcInstance;
                 if (pc == l1pcinstance) {
                   partyHateExp += hate;
                   partyHateLawful += hate;
                 } else if (l1pcinstance.getParty().isMember(pc)) {
                   partyHateExp += hate;
                   partyHateLawful += hate;
                 } else {
                   if (totalHateExp > 0L) {
                     acquire_exp = exp * hate / totalHateExp;
                   }
                   if (totalHateLawful > 0) {
                     acquire_lawful = lawful * hate / totalHateLawful;
                   }
                   AddExp(pc, acquire_exp, acquire_lawful);
                 }
               } else if (l1PcInstance instanceof L1PetInstance) {
                 L1PetInstance pet = (L1PetInstance)l1PcInstance;
                 L1PcInstance master = pet.getMaster();
                 if (master == l1pcinstance) {
                   partyHateExp += hate;
                 } else if (l1pcinstance.getParty().isMember(master)) {
                   partyHateExp += hate;
                 } else {
                   if (totalHateExp > 0L) {
                     acquire_exp = exp * hate / totalHateExp;
                   }
                   AddExpPet(pet, acquire_exp);
                 }
               } else if (l1PcInstance instanceof L1SummonInstance) {
                 L1SummonInstance summon = (L1SummonInstance)l1PcInstance;
                 L1PcInstance master = (L1PcInstance)summon.getMaster();
                 if (master == l1pcinstance) {
                   partyHateExp += hate;
                 } else if (l1pcinstance.getParty().isMember(master)) {
                   partyHateExp += hate;
                 }
               }
             }


             if (totalHateExp > 0L) {
               party_exp = exp * partyHateExp / totalHateExp;
             }
             if (totalHateLawful > 0) {
               party_lawful = lawful * partyHateLawful / totalHateLawful;
             }


             double pri_bonus = 0.0D;
             L1PcInstance leader = l1pcinstance.getParty().getLeader();
             if (leader.isCrown() && (l1pcinstance.knownsObject((L1Object)leader) || l1pcinstance.equals(leader))) {
               pri_bonus = 0.059D;
             }


             L1PcInstance[] ptMembers = l1pcinstance.getParty().getMembers();
             double pt_bonus = 0.0D;
             for (L1PcInstance each : l1pcinstance.getParty().getMembers()) {
               if (l1pcinstance.knownsObject((L1Object)each) || l1pcinstance.equals(each)) {
                 party_level += (each.getLevel() * each.getLevel());
               }
               if (l1pcinstance.knownsObject((L1Object)each)) {
                 pt_bonus += 0.04D;
               }
             }

             party_exp = (long)(party_exp * (0.5D + pt_bonus + pri_bonus));
             if (Config.ServerAdSetting.IsPartyExp) {
               party_exp *= (long)Config.ServerAdSetting.AddPartyExp;
             }


             if (party_level > 0.0D) {
               dist = (l1pcinstance.getLevel() * l1pcinstance.getLevel()) / party_level;
             }
             member_exp = (long)(party_exp * dist);
             member_lawful = (int)(party_lawful * dist);

             ownHateExp = 0L;
             for (i = hateList.size() - 1; i >= 0; i--) {
               L1PcInstance l1PcInstance; L1Character acquisitor = (L1Character)acquisitorList.get(i);
               if (acquisitor instanceof MJCompanionInstance)
                 l1PcInstance = ((MJCompanionInstance)acquisitor).get_master();
               hate = ((Integer)hateList.get(i)).intValue();
               if (l1PcInstance instanceof L1PcInstance) {
                 L1PcInstance pc = l1PcInstance;
                 if (pc == l1pcinstance) {
                   ownHateExp += hate;
                 }
               } else if (l1PcInstance instanceof L1PetInstance) {
                 L1PetInstance pet = (L1PetInstance)l1PcInstance;
                 L1PcInstance master = pet.getMaster();
                 if (master == l1pcinstance) {
                   ownHateExp += hate;
                 }
               } else if (l1PcInstance instanceof L1SummonInstance) {
                 L1SummonInstance summon = (L1SummonInstance)l1PcInstance;
                 L1PcInstance master = (L1PcInstance)summon.getMaster();
                 if (master == l1pcinstance) {
                   ownHateExp += hate;
                 }
               }
             }

             if (ownHateExp != 0L) {
               for (i = hateList.size() - 1; i >= 0; i--) {
                 L1PcInstance l1PcInstance; L1Character acquisitor = (L1Character)acquisitorList.get(i);
                 if (acquisitor instanceof MJCompanionInstance)
                   l1PcInstance = ((MJCompanionInstance)acquisitor).get_master();
                 hate = ((Integer)hateList.get(i)).intValue();
                 if (l1PcInstance instanceof L1PcInstance) {
                   L1PcInstance pc = l1PcInstance;
                   if (pc == l1pcinstance) {
                     if (ownHateExp > 0L) {
                       acquire_exp = member_exp * hate / ownHateExp;
                     }
                     AddExp(pc, acquire_exp, member_lawful);
                   }
                 } else if (l1PcInstance instanceof L1PetInstance) {
                   L1PetInstance pet = (L1PetInstance)l1PcInstance;
                   L1PcInstance master = pet.getMaster();
                   if (master == l1pcinstance) {
                     if (ownHateExp > 0L) {
                       acquire_exp = member_exp * hate / ownHateExp;
                     }
                     AddExpPet(pet, acquire_exp);
                   }
                 } else if (l1PcInstance instanceof L1SummonInstance) {
                   L1SummonInstance sum = (L1SummonInstance)l1PcInstance;
                   L1PcInstance master = (L1PcInstance)sum.getMaster();
                   if (master == l1pcinstance) {
                     if (ownHateExp > 0L) {
                       acquire_exp = member_exp * hate / ownHateExp;
                     }
                     AddExp(master, acquire_exp, member_lawful);
                   }
                 }
               }
             } else {

               AddExp(l1pcinstance, member_exp, member_lawful);
             }


             for (int cnt = 0; cnt < ptMembers.length; cnt++) {
               if (l1pcinstance.knownsObject((L1Object)ptMembers[cnt])) {
                 if (party_level > 0.0D) {
                   dist = (ptMembers[cnt].getLevel() * ptMembers[cnt].getLevel()) / party_level;
                 }
                 member_exp = (long)(party_exp * dist);
                 member_lawful = (int)(party_lawful * dist);

                 ownHateExp = 0L;
                 for (i = hateList.size() - 1; i >= 0; i--) {
                   L1PcInstance l1PcInstance; L1Character acquisitor = (L1Character)acquisitorList.get(i);
                   if (acquisitor instanceof MJCompanionInstance)
                     l1PcInstance = ((MJCompanionInstance)acquisitor).get_master();
                   hate = ((Integer)hateList.get(i)).intValue();
                   if (l1PcInstance instanceof L1PcInstance) {
                     L1PcInstance pc = l1PcInstance;
                     if (pc == ptMembers[cnt]) {
                       ownHateExp += hate;
                     }
                   } else if (l1PcInstance instanceof L1PetInstance) {
                     L1PetInstance pet = (L1PetInstance)l1PcInstance;
                     L1PcInstance master = pet.getMaster();
                     if (master == ptMembers[cnt]) {
                       ownHateExp += hate;
                     }
                   } else if (l1PcInstance instanceof L1SummonInstance) {
                     L1SummonInstance summon = (L1SummonInstance)l1PcInstance;
                     L1PcInstance master = (L1PcInstance)summon.getMaster();
                     if (master == ptMembers[cnt]) {
                       ownHateExp += hate;
                     }
                   }
                 }

                 if (ownHateExp != 0L) {
                   for (i = hateList.size() - 1; i >= 0; i--) {
                     L1PcInstance l1PcInstance; L1Character acquisitor = (L1Character)acquisitorList.get(i);
                     if (acquisitor instanceof MJCompanionInstance)
                       l1PcInstance = ((MJCompanionInstance)acquisitor).get_master();
                     hate = ((Integer)hateList.get(i)).intValue();
                     if (l1PcInstance instanceof L1PcInstance) {
                       L1PcInstance pc = l1PcInstance;
                       if (pc == ptMembers[cnt]) {
                         if (ownHateExp > 0L) {
                           acquire_exp = member_exp * hate / ownHateExp;
                         }
                         AddExp(pc, acquire_exp, member_lawful);
                       }
                     } else if (l1PcInstance instanceof L1PetInstance) {
                       L1PetInstance pet = (L1PetInstance)l1PcInstance;
                       L1PcInstance master = pet.getMaster();
                       if (master == ptMembers[cnt]) {
                         if (ownHateExp > 0L) {
                           acquire_exp = member_exp * hate / ownHateExp;
                         }
                         AddExpPet(pet, acquire_exp);
                       }
                     } else if (l1PcInstance instanceof L1SummonInstance) {
                       L1SummonInstance sum = (L1SummonInstance)l1PcInstance;
                       L1PcInstance pc = (L1PcInstance)sum.getMaster();
                       AddExp(pc, acquire_exp, acquire_lawful);
                     }
                   }
                 } else {

                   AddExp(ptMembers[cnt], member_exp, member_lawful);
                 }
               }
             }
           } else {

             for (i = hateList.size() - 1; i >= 0; i--) {
               L1PcInstance l1PcInstance; L1Character acquisitor = (L1Character)acquisitorList.get(i);
               if (acquisitor instanceof MJCompanionInstance)
                 l1PcInstance = ((MJCompanionInstance)acquisitor).get_master();
               hate = ((Integer)hateList.get(i)).intValue();
               acquire_exp = exp * hate / totalHateExp;
               if (l1PcInstance instanceof L1PcInstance &&
                 totalHateLawful > 0) {
                 acquire_lawful = lawful * hate / totalHateLawful;
               }


               if (l1PcInstance instanceof L1PcInstance) {
                 L1PcInstance pc = l1PcInstance;
                 AddExp(pc, acquire_exp, acquire_lawful);
               } else if (l1PcInstance instanceof L1PetInstance) {
                 L1PetInstance pet = (L1PetInstance)l1PcInstance;
                 AddExpPet(pet, acquire_exp);
               } else if (l1PcInstance instanceof L1SummonInstance) {
                 L1SummonInstance sum = (L1SummonInstance)l1PcInstance;
                 L1PcInstance pc = (L1PcInstance)sum.getMaster();
                 AddExp(pc, acquire_exp, acquire_lawful);
               }
             }
           }
         }
       }

       private static void AddExp(L1PcInstance pc, long exp, int lawful) {
         if (Config.Login.StandbyServer) {
           return;
         }
         if (pc.isGhost()) {
           return;
         }

         int add_lawful = (int)(lawful * Config.ServerRates.RateLawful) * -1;
         pc.addLawful(add_lawful);
         MJCompanionHuntingGround hground = MJCompanionHuntingGround.get_hunting_ground(pc.getMapId());
         if (hground != null) {
           double magnification_exp = exp * hground.get_magnification_by_exp();
           exp = (long)(exp - magnification_exp);
           MJCompanionInstance companion = pc.get_companion();
           if (companion != null) {
             companion.update_exp((long)magnification_exp);
           }
           if (exp <= 0L) {
             return;
           }
         }


         if (pc.getAI() != null) {
           return;
         }

         double exppenalty = ExpTable.getPenaltyRate(pc.getLevel());

         double expposion = 1.0D;
         double EXP_TAM = 1.0D;
         double levelupBonus = 1.0D;
         double clanBonus = 1.0D;
         double comboBonus = 1.0D;
         double Arka = 1.0D;
         double special_map = 1.0D;
         double party_map_rate = 1.0D;
         double exp_posion = 1.0D;

         if (SpecialMapTable.getInstance().isSpecialMap(pc.getMapId())) {
           L1SpecialMap SM = SpecialMapTable.getInstance().getSpecialMap(pc.getMapId());
           if (SM != null) {
             special_map += SM.getExpRate();
           }
         }

         if (pc.isInParty()) {
           Double pmr = Double.valueOf(PartyMapInfoTable.getInstance().getPartyMapExpRate(pc.getMapId()));
           if (pmr.doubleValue() != 0.0D) {
             int member_count = 0;
             for (L1PcInstance member : pc.getParty().getList()) {
               if (pc.getId() == member.getId()) {
                 continue;
               }
               if (pc.getLocation().isInScreen((Point)member.getLocation())) {
                 member_count++;
               }
             }

             if (member_count != 0) {
               party_map_rate += pmr.doubleValue();
             }
           }
         }

         if (pc.hasSkillEffect(23069))
         {
           expposion += pc.isPcBuff() ? 0.4D : 0.2D;
         }
         if (pc.hasSkillEffect(13069))
         {
           expposion += pc.isPcBuff() ? 0.9D : 0.7D;
         }
         if (pc.hasSkillEffect(7795)) {
           EXP_TAM += 0.05D;
         }

         if (pc.hasSkillEffect(8382) && pc.getAccount().getBlessOfAin() >= 10000) {
           int lvl = pc.getLevel();
           if (lvl <= 79) {
             exp_posion += 1.3D;
           } else if (lvl <= 81) {
             exp_posion += 1.2D;
           } else if (lvl <= 83) {
             exp_posion += 1.1D;
           } else if (lvl <= 85) {
             exp_posion++;
           } else if (lvl <= 89) {
             exp_posion += 0.9D;
           }
         }
         L1Clan clan = L1World.getInstance().getClan(pc.getClanid());

         if (pc.getInventory().checkEquipped(900033) || pc.getInventory().checkEquipped(9961) || pc
           .getInventory().checkEquipped(900116)) {
           Arka += 0.2D;
         }

         if (pc.hasSkillEffect(80007)) {
           levelupBonus += 1.23D;
         }
         try {
           if (pc.getAccount().getBlessOfAin() > 0 &&
             pc.hasSkillEffect(85010) &&
             pc.getComboCount() <= 11) {
             comboBonus += pc.getComboCount() * 0.1D;
           }



           double einhasadBonus = 1.0D;
           if (pc.getAccount().getBlessOfAin() > 0) {
             int ein_level = pc.getAccount().get_ein_level();
             if (pc.getClan() != null)
               clan.addBlessCount((int)exp);
             einhasadBonus += SC_REST_EXP_INFO_NOTI.expRation(ein_level);
             if (pc.hasSkillEffect(80009) || pc.hasSkillEffect(80008)) {
               einhasadBonus += SC_REST_EXP_INFO_NOTI.expExtra(pc, ein_level);
             }
           } else {
             einhasadBonus += SC_REST_EXP_INFO_NOTI.expExtra(pc, pc.getAccount().get_ein_level());
           }

           if (pc.getExpAmplifier() != null) {
             einhasadBonus += pc.getExpAmplifier().getMagnifier();
           }
           double emeraldBonus = 1.0D;

           if (pc.hasSkillEffect(22018) && pc.getAccount().getBlessOfAin() > 10000) {
             emeraldBonus += 0.8D;
             if (pc.getClan() != null) {
               clan.addBlessCount((int)exp);
             }
           } else if (pc.hasSkillEffect(80008) && pc.getAccount().getBlessOfAin() > 10000) {
             if (pc.getLevel() >= 49 && pc.getLevel() <= 54) {
               einhasadBonus += 0.53D;
             } else if (pc.getLevel() >= 55 && pc.getLevel() <= 59) {
               einhasadBonus += 0.43D;
             } else if (pc.getLevel() >= 60 && pc.getLevel() <= 64) {
               einhasadBonus += 0.33D;
             } else if (pc.getLevel() >= 65) {
               einhasadBonus += 0.23D;
             }  if (pc.getClan() != null)
               clan.addBlessCount((int)exp);
             if (pc.getAccount().getBlessOfAin() <= 10000) {
               pc.removeSkillEffect(80008);
             }
           } else if (pc.hasSkillEffect(80009) && pc.getAccount().getBlessOfAin() > 10000) {
             einhasadBonus += 1.5D;
             if (pc.getClan() != null)
               clan.addBlessCount((int)exp);
             if (pc.hasSkillEffect(85010) &&
               pc.getComboCount() <= 11) {
               comboBonus += pc.getComboCount() * 0.1D;
             }

             if (pc.getAccount().getBlessOfAin() <= 10000) {
               pc.removeSkillEffect(80009);
             }
           }

           if (pc.getAccount().getBlessOfAin() > 10000 || einhasadBonus != 0.0D) {
             pc.getAccount().addBlessOfAin(-((int)SC_REST_EXP_INFO_NOTI.calcDecreaseCharacterEinhasad(pc, exp)), pc);
             SC_REST_EXP_INFO_NOTI.send(pc);
           }

           double clanOnlineBonus = 1.0D;


           if (pc.getClanid() != 0 && (
             pc.getClan().getOnlineClanMember()).length >= Config.ServerAdSetting.CLANBUFFUSERCOUNT) {
             clanOnlineBonus += 0.1D;
           }



           double BloodBonus = 1.0D;
           if (clan != null && clan.getCastleId() != 0) {
             BloodBonus += Config.ServerRates.BloodBonus;
           }


             if (pc.getLevel() >= Config.ServerAdSetting.NewCha1 && pc.getMapId() >= 1911 && pc.getMapId() <= 1912) {
                 pc.sendPackets((ServerBasePacket)new S_PacketBox(84, "在此狩獵場中，您無法獲得更多經驗值。"));
                 return;
             }
             if (pc.getLevel() >= 75 && pc.getMapId() >= 25 && pc.getMapId() <= 28) {
                 pc.sendPackets((ServerBasePacket)new S_PacketBox(84, "在此狩獵場中，您無法獲得更多經驗值。"));
                 return;
             }
           int rank_level = pc.getRankLevel();
           double rank_rate = 1.0D;
           if (rank_level >= 1 && rank_level <= 5 &&
             pc.getAccount().getBlessOfAin() > 2000000) {
             rank_rate += 0.1D;
           }





           double item_bonus_exp = 1.0D + pc.get_item_exp_bonus() * 0.01D;
           double bonus_exp = 0.0D;
           if (pc.getLevel() <= Config.ServerAdSetting.LineageBuff) {
             bonus_exp += Config.ServerAdSetting.LineageBuffExpRation;
           }
           if (pc.hasSkillEffect(11223)) {
             bonus_exp += 10.0D;
           }
           if (bonus_exp != 0.0D) {
             item_bonus_exp += bonus_exp / 100.0D;
           }

           long previousExp = exp;
           long add_exp = (long)(exp * exppenalty * Config.ServerRates.RateXp * levelupBonus * clanBonus * expposion * EXP_TAM * comboBonus * einhasadBonus * emeraldBonus * clanOnlineBonus * BloodBonus * Arka * special_map * party_map_rate * exp_posion * rank_rate * item_bonus_exp);



           if (add_exp < 0L) {
             System.out.println("[經驗值負值處理確認1] : " + pc.getName() + " / " + add_exp + "(進入 : " + exp + ", " + previousExp + ")");

             return;
           }

           if (pc.getLevel() >= 1 &&
             add_exp + pc.get_exp() > ExpTable.getExpByLevel(pc.getLevel() + 1)) {
             add_exp = ExpTable.getExpByLevel(pc.getLevel() + 1) - pc.get_exp();
           }


           if (pc.getLevel() >= Config.CharSettings.MaxLevel) {
             add_exp = 0L;
             pc.sendPackets(Config.Message.MAX_LEVEL_MESSAGE);
           }

           if (pc.getLevel() >= Config.CharSettings.LimitLevel && !pc.isGm()) {
             add_exp = 0L;
             pc.sendPackets(Config.Message.MAX_LEVEL_MESSAGE);
           }


           if (pc.getLevel() >= GameServerSetting.getInstance().get_maxLevel()) {

             long maxexp = ExpTable.getExpByLevel(GameServerSetting.getInstance().get_maxLevel() + 1);
             if (pc.get_exp() + add_exp >= maxexp)
             {
               add_exp = 0L;
             }
           }

             if (add_exp < 0L) {
                 System.out.println("[經驗值負值處理確認2] : " + pc.getName() + " / " + add_exp + "(進入 : " + exp + ")");
                 return;
             }
           if (!pc.isGm() && pc.getAI() == null && !pc.getSafetyZone() && !pc.get_is_client_auto() && pc
             .getMapId() != 1936 && pc.getMapId() != 107 && pc.getMapId() != 2101 && pc.getMapId() != 2151 && pc
             .getMapId() != 612 && pc.getMapId() != 254 && pc.getMapId() != 1930 &&
             !pc.hasSkillEffect(1005)) {
             MJCaptcha captcha = pc.get_captcha();
             if (captcha == null) {
               captcha = pc.create_captcha();
             }
             if (captcha.is_keep_captcha()) {
               if (captcha.inc_relay_count() >= MJCaptchaLoadManager.CAPTCHA_RELAY_COUNT) {
                 captcha.do_fail(pc);
               }
             } else if (MJCaptchaLoadManager.CAPTCHA_IS_RUNNING &&
               !captcha.is_pass_captcha() && MJRnd.isWinning(1000000, MJCaptchaLoadManager.CAPTCHA_SHOW_PROBABILITY_BYMILLIMON)) {
               captcha.drain_captcha(pc);
             }
           }


           if (pc.get_companion() != null) {
             pc.get_companion().update_exp((int)(add_exp * MJCompanionSettings.EXP_BY_MASTER_EXP));
           }
           Account account = pc.getAccount();
           if (account != null && FatigueProperty.getInstance().use_fatigue() && pc.getAI() == null &&
             account.has_fatigue()) {
             add_exp = (long)(add_exp - add_exp * FatigueProperty.getInstance().get_fatigue_effect_exp());
           }



           if (BonusExpTable.getInstance().isExpBonusLv(pc.getLevel())) {
             L1BonusExp temp = BonusExpTable.getInstance().getExpBonusLv(pc.getLevel());
             if (temp != null) {
               add_exp = (long)(add_exp * temp.getExpBonus());
             }
           }

           if (pc != null && pc.isPassive(MJPassiveID.GROWS.toInt())) {
             int Level = pc.getLevel();
             int point = MJRnd.next(4) + 2;
             int probability = Config.MagicAdSetting_Fencer.GROWS_PASSIVE;

             if (Level < 85 &&
               MJRnd.isWinning(100, probability)) {
               add_exp *= point;
               if (point == 2) {
                 pc.send_effect(18572);
               } else if (point == 3) {
                 pc.send_effect(18574);
               } else if (point >= 4) {
                 pc.send_effect(18576);
               }
             }
           }


           pc.add_exp(add_exp);
         }
         catch (Exception e) {
           if (!(e instanceof NullPointerException))
             e.printStackTrace();
         }
       }

       private static void AddExpPet(L1PetInstance pet, long exp) {
         L1PcInstance pc = pet.getMaster();


         int petItemObjId = pet.getItemObjId();

         int levelBefore = pet.getLevel();
         long totalExp = exp * 50L + pet.get_exp();
         if (totalExp >= ExpTable.getExpByLevel(51)) {
           totalExp = ExpTable.getExpByLevel(51) - 1L;
         }
         pet.set_exp(totalExp);
         pet.setLevel(ExpTable.getLevelByExp(totalExp));

         int expPercentage = ExpTable.getExpPercentage(pet.getLevel(), totalExp);

         int gap = pet.getLevel() - levelBefore;
         for (int i = 1; i <= gap; i++) {
           IntRange hpUpRange = pet.getPetType().getHpUpRange();
           IntRange mpUpRange = pet.getPetType().getMpUpRange();
           pet.addMaxHp(hpUpRange.randomValue());
           pet.addMaxMp(mpUpRange.randomValue());
         }

         pet.setExpPercent(expPercentage);
         SC_WORLD_PUT_OBJECT_NOTI noti = SC_WORLD_PUT_OBJECT_NOTI.newInstance((L1NpcInstance)pet);
         pc.broadcastPacket((MJIProtoMessage)noti, MJEProtoMessages.SC_WORLD_PUT_OBJECT_NOTI, true, true);



         if (gap != 0) {


           L1Pet petTemplate = PetTable.getInstance().getTemplate(petItemObjId);
           if (petTemplate == null) {
             _log.warning("L1Pet == null");
             return;
           }
           petTemplate.set_exp(pet.get_exp());
           petTemplate.set_level(pet.getLevel());
           petTemplate.set_hp(pet.getMaxHp());
           petTemplate.set_mp(pet.getMaxMp());
           PetTable.getInstance().storePet(petTemplate);
           pc.sendPackets((ServerBasePacket)new S_ServerMessage(320, pet.getName()));
         }
       }








       public static void AddExp(L1PcInstance pc, long exp) {
         if (Config.Login.StandbyServer) {
           return;
         }

         if (pc.getLevel() > GameServerSetting.getInstance().get_maxLevel()) {
           return;
         }
         if (pc.isDead()) {
           return;
         }
         long add_exp = exp;

         if (add_exp < 0L) {
           return;
         }

         if (ExpTable.getExpByLevel(GameServerSetting.getInstance().get_maxLevel() + 1) <= pc.get_exp() + add_exp) {
           pc.set_exp(ExpTable.getExpByLevel(GameServerSetting.getInstance().get_maxLevel() + 1) - 1L);
         } else {
           pc.add_exp(add_exp);
         }
         pc.onChangeExp();
       }

       private static int getPartyIsinScreen(L1PcInstance pc) {
         int count = 0;

         for (L1PcInstance member : pc.getParty().getMembers()) {
           if (pc.getLocation().isInScreen((Point)member.getLocation())) {
             count++;
           }
         }

         return count;
       }
     }


