         package l1j.server.server.monitor;public interface Logger { void addChat(LoggerChatType paramLoggerChatType, L1PcInstance paramL1PcInstance, String paramString); void addWhisper(L1PcInstance paramL1PcInstance1, L1PcInstance paramL1PcInstance2, String paramString); void addCommand(String paramString); void addConnection(String paramString);
           void addError(String paramString);
           void addWarehouse(WarehouseType paramWarehouseType, boolean paramBoolean, L1PcInstance paramL1PcInstance, L1ItemInstance paramL1ItemInstance, int paramInt);
           void addTrade(boolean paramBoolean, L1PcInstance paramL1PcInstance1, L1PcInstance paramL1PcInstance2, L1ItemInstance paramL1ItemInstance, int paramInt);
           void addEnchant(L1PcInstance paramL1PcInstance, L1ItemInstance paramL1ItemInstance, boolean paramBoolean);
           void addAll(String paramString);
           void addItemAction(ItemActionType paramItemActionType, L1PcInstance paramL1PcInstance, L1ItemInstance paramL1ItemInstance, int paramInt);
           void addLevel(L1PcInstance paramL1PcInstance, int paramInt);
           void flush() throws IOException;
           public enum LoggerChatType { Normal, Global, Clan, Alliance, Guardian, Party, Group, Shouting; }




           public enum ItemActionType
           {
             Pickup, Drop, Delete, del, DeathDrop;
           }



           public enum WarehouseType
           {
             Private, Clan, Package, Elf;
           } }


