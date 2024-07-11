package l1j.server.server.storage;

import l1j.server.server.model.Instance.L1PcInstance;

public interface CharacterStorage {
  void createCharacter(L1PcInstance paramL1PcInstance) throws Exception;
  
  void deleteCharacter(String paramString1, String paramString2) throws Exception;
  
  void storeCharacter(L1PcInstance paramL1PcInstance) throws Exception;
  
  void updateAccountName(L1PcInstance paramL1PcInstance) throws Exception;
  
  L1PcInstance loadCharacter(String paramString) throws Exception;
}


