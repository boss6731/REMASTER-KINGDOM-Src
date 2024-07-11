package l1j.server.server.model.npc.action;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.npc.L1NpcHtml;

public interface L1NpcAction {
  boolean acceptsRequest(String paramString, L1PcInstance paramL1PcInstance, L1Object paramL1Object);
  
  L1NpcHtml execute(String paramString, L1PcInstance paramL1PcInstance, L1Object paramL1Object, byte[] paramArrayOfbyte);
  
  L1NpcHtml executeWithAmount(String paramString, L1PcInstance paramL1PcInstance, L1Object paramL1Object, int paramInt);
}


