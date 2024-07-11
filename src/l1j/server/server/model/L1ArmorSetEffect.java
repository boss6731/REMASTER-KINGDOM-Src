package l1j.server.server.model;

import l1j.server.server.model.Instance.L1PcInstance;

interface L1ArmorSetEffect {
  void giveEffect(L1PcInstance paramL1PcInstance);
  
  void cancelEffect(L1PcInstance paramL1PcInstance);
}


