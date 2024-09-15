package l1j.server.server.server.monitor;

import MJFX.UIAdapter.MJUIAdapter;
import l1j.server.Config;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.StringUtil;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileLogger implements l1j.server.server.monitor.Logger {
	private static String date = "";

	private ArrayList<String> _chatlog;
	private ArrayList<String> _commandlog;
	private ArrayList<String> _connectionlog;
	private ArrayList<String> _enchantlog;
	private ArrayList<String> _tradelog;
	private ArrayList<String> _warehouselog;
	private ArrayList<String> _itemactionlog;
	private ArrayList<String> _levellog;
	private ArrayList<String> _error;
	private ArrayList<String> _cmd;
	private ArrayList<String> _shop;
	private ArrayList<String> _dollMake;
	private ArrayList<String> _smeltingMake;
	private ArrayList<String> _blessOfAinPointCard;
	private ArrayList<String> _dollPotential;
	private ArrayList<String> _craft_log;

	public FileLogger() {
		_chatlog 			= new ArrayList<String>(1024);
		_commandlog 		= new ArrayList<String>(512);
		_connectionlog 		= new ArrayList<String>(1024);
		_enchantlog 		= new ArrayList<String>(1024);
		_tradelog 			= new ArrayList<String>(512);
		_warehouselog 		= new ArrayList<String>(512);
		_itemactionlog 		= new ArrayList<String>(512);
		_levellog 			= new ArrayList<String>(512);
		_error				= new ArrayList<String>(1024);
		_cmd 				= new ArrayList<String>(1024);
		_shop 				= new ArrayList<String>(1024);
		_dollMake 			= new ArrayList<String>(1024);
		_blessOfAinPointCard = new ArrayList<String>(1024);
		_dollPotential 		= new ArrayList<String>(1024);
		_craft_log			= new ArrayList<String>(1024);
	}

	public void addBlessOfAinPointCard(String s) {
		String msg = String.format("[%s] " + s + "\r\n", getLocalTime());
		synchronized (_blessOfAinPointCard) {
			_blessOfAinPointCard.add(msg);
		}
	}

	public void addDollPotencial(String s) {
		String msg = String.format("[%s] " + s + "\r\n", getLocalTime());
		synchronized (_dollPotential) {
			_dollPotential.add(msg);
		}
	}

	public void addCmd(String s) {
		synchronized (_cmd) {
			_cmd.add(s);
		}
	}

	public void addDollMake(String s) {
		synchronized (_dollMake) {
			_dollMake.add(s);
		}
	}
	public void addSmeltingMake(String s) {
		synchronized (_smeltingMake) {
			_smeltingMake.add(s);
		}
	}
	

	public void addcraftMake(String s) {
		synchronized (_craft_log) {
			_craft_log.add(s);
		}
	}

	public void addDollMake(L1PcInstance pc, L1ItemInstance item, boolean success) {
		String msg = String.format("[%s] [帳號]:%s [角色名]:%s [是否成功]:%s [物品名稱]:%s\r\n", getLocalTime(), pc.getAccountName(), pc.getName(), (success ? "成功" : "失敗"), getFormatItemName(item, 1));
				addDollMake(msg);
	}

	public void addSmeltingMake(L1PcInstance pc, L1ItemInstance item, boolean success) {
		String msg = String.format("[%s] [帳號]:%s [角色名]:%s [是否成功]:%s [物品名稱]:%s\r\n", getLocalTime(), pc.getAccountName(), pc.getName(), (success ? "成功" : "失敗"), getFormatItemName(item, 1));
				System.out.println(msg);
		addSmeltingMake(msg);
	}

	public void addShop(String itemName, int count, long price, String npc, String pc) {
		addShop(String.format("[%s] [NPC購買]:%s [角色名]:%s [物品]:%s [數量]:%d [價格]:%d\r\n", getLocalTime(), npc, pc, itemName, count, price));
	}

	public void addShopSell(String itemName, int count, long price, String npc, String pc) {
		addShop(String.format("[%s] [NPC銷售]:%s [角色名]:%s [物品]:%s [數量]:%d [價格]:%d\r\n", getLocalTime(), npc, pc, itemName, count, price));
	}

	public void addCraftMake(L1PcInstance pc, int desc, String string, int count) {
		String msg = String.format("[%s] [帳號]:%s [角色名]:%s [是否成功]:%s [描述]:%s [數量(僅記錄一個)]: %d\r\n", getLocalTime(), pc.getAccountName(), pc.getName(), string, desc, count);
				addCraftMake(msg);
	}

	private void addCraftMake(String msg) {
	}

	public void addShop(String s) {
		synchronized (_shop) {
			_shop.add(s);
		}
		if (Config.Synchronization.Operation_Manager)
			MJUIAdapter.on_shop_append(s);
	}

	public void addChat(LoggerChatType type, L1PcInstance pc, String msg) {
		String log = "";

		switch (type) {
		case Clan:
			log = String.format("%s\t血盟(%s)\t[%s]\t%s\r\n", getLocalTime(), pc.getClanname(), pc.getName(), msg);
			if (Config.Synchronization.Operation_Manager)
				MJUIAdapter.on_chat_pledge_append(log);
			break;

		case Global:
			log = String.format("%s\t全部\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), msg);
			if (Config.Synchronization.Operation_Manager)
				MJUIAdapter.on_chat_world_append(log);
			break;

		case Normal:
			log = String.format("%s\t一般\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), msg);
			if (Config.Synchronization.Operation_Manager)
				MJUIAdapter.on_chat_normal_append(log);
			break;

		case Alliance:
			log = String.format("%s\t同盟\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), msg);
			if (Config.Synchronization.Operation_Manager)
				MJUIAdapter.on_chat_pledge_append(log);
			break;

		case Guardian:
			log = String.format("%s\t守護\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), msg);
			if (Config.Synchronization.Operation_Manager)
				MJUIAdapter.on_chat_pledge_append(log);
			break;

		case Party:
			log = String.format("%s\t隊伍\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), msg);
			if (Config.Synchronization.Operation_Manager)
				MJUIAdapter.on_chat_party_append(log);
			break;

		case Group:
			log = String.format("%s\t群組\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), msg);
			if (Config.Synchronization.Operation_Manager)
				MJUIAdapter.on_chat_party_append(log);
			break;

		case Shouting:
			log = String.format("%s\t呼叫\t[%s]\t%s\r\n", getLocalTime(), pc.getName(), msg);
			if (Config.Synchronization.Operation_Manager)
				MJUIAdapter.on_chat_normal_append(log);
			break;
		}
		synchronized (_warehouselog) {
			_chatlog.add(log);
		}
	}

	public void addWhisper(L1PcInstance pcfrom, L1PcInstance pcto, String msg) {
		// 시간 귓말 케릭->케릭\t내용
		String log = String.format("%s\t悄悄話\t[%s] -> [%s]\t%s\r\n", getLocalTime(), pcfrom.getName(), pcto.getName(), msg);
		if (Config.Synchronization.Operation_Manager)
			MJUIAdapter.on_chat_whisper_append(log);
		synchronized (_chatlog) {
			_chatlog.add(log);
		}
	}

	public void addCommand(String msg) {
		msg = String.format("%s\t%s\r\n", getLocalTime(), msg);
		if (Config.Synchronization.Operation_Manager)
			MJUIAdapter.on_gm_command(msg);
		synchronized (_commandlog) {
			_commandlog.add(msg);
		}
	}

	public void addConnection(String msg) {
		msg = String.format("%s\t%s\r\n", getLocalTime(), msg);
		synchronized (_connectionlog) {
			_connectionlog.add(msg);
		}
	}

	public void addError(String msg) {
		msg = String.format("%s\t%s\r\n", getLocalTime(), msg);
		synchronized (_error) {
			_error.add(msg);
		}
	}

	public void addEnchant(L1PcInstance pc, L1ItemInstance item, boolean success) {
		// 時間 帳號:角色 狀態 物品
		String msg = String.format("%s\t%s:[%s]\t%s\t%s\r\n", getLocalTime(), pc.getAccountName(), pc.getName(),(success ? "成功" : "失敗"), getFormatItemName(item, 1));
		if (Config.Synchronization.Operation_Manager)
			MJUIAdapter.on_enchant_append(msg);
		synchronized (_enchantlog) {
			_enchantlog.add(msg);
		}
	}

	public void addEnchant(L1PcInstance pc, L1ItemInstance enchanter, L1ItemInstance enchantee, boolean is_success) {
		String msg = String.format("%s\t%s:[%s]\t%s\t%s\t%s->%s\r\n", getLocalTime(), pc.getAccountName(), pc.getName(), (is_success ? "成功" : "失敗"), enchanter.getName(), enchantee.getName());
		if (Config.Synchronization.Operation_Manager)
			MJUIAdapter.on_enchant_append(msg);
		synchronized (_enchantlog) {
			_enchantlog.add(msg);
		}
	}

	public void addTrade(boolean success, L1PcInstance pcfrom, L1PcInstance pcto, L1ItemInstance item, int count) {
		// 時間 成功 帳號:角色 [ID]物品名稱(數量) -> 帳號:角色
		// 交易成功時記錄日誌
		if (pcfrom == null || pcto == null || item == null)
			return;

		String msg = String.format("%s\t%s\t%s:%s\t%s\t%s:[%s]\r\n", getLocalTime(), (success ? "OO完成OO" : "XX取消XX"), pcfrom.getAccountName(), "[" + pcfrom.getName() + "]",
				getFormatItemName(item, count), pcto.getAccountName(), pcto.getName());
		if (Config.Synchronization.Operation_Manager)
			MJUIAdapter.on_trade_append(msg);
		synchronized (_tradelog) {
			_tradelog.add(msg);
		}
	}

	public void 개인상점구매(boolean success, L1PcInstance pcfrom, L1PcInstance pcto, L1ItemInstance item, int count) {
		// 時間 成功 帳號:角色 [ID]物品名稱(數量) -> 帳號:角色
		// 交易成功時留下日誌記錄
		String msg = String.format("%s\t%s\t%s:%s\t%s\t%s:[%s]\r\n", getLocalTime(), (success ? "商店購買" : "商店取消"), pcfrom.getAccountName(), "[" + pcfrom.getName() + "]", getFormatItemName(item, count),
				pcto.getAccountName(), pcto.getName());
		if (Config.Synchronization.Operation_Manager)
			MJUIAdapter.on_trade_append(msg);
		synchronized (_tradelog) {
			_tradelog.add(msg);
		}
	}

	public void addWarehouse(WarehouseType type, boolean put, L1PcInstance pc, L1ItemInstance item, int count) {
		String msg = "";

		// 時間 類型 操作 帳號:角色名 [ID]物品(數量)
		switch (type) {
			// 根據不同的情況格式化消息
			case Private:
				// 當類型為 Private 時，格式化消息包含時間、操作、帳號名、角色名和物品信息
				msg = String.format("[%s] [操作]:%s [帳號名]:%s [角色名]:%s [物件/物品名/數量]:%s\r\n", getLocalTime(), (put ? "存放" : "取回"), pc.getAccountName(), pc.getName(), getFormatItemName(item, count));
				break;
			case Clan:
				// 當類型為 Clan 時，格式化消息包含時間、血盟名、操作、帳號名、角色名和物品信息
				msg = String.format("[%s] [血盟名]:%s [操作]:%s [帳號名]:%s [角色名]:%s [物件/物品名/數量]:%s\r\n", getLocalTime(), pc.getClanname(), (put ? "存放" : "取回"), pc.getAccountName(), pc.getName(), getFormatItemName(item, count));
				break;
			case Package:
				// 當類型為 Package 時，格式化消息包含時間、包裹、操作、帳號名、角色名和物品信息
				msg = String.format("%s\t包裹:%s\t%s:[%s]\t%s\r\n", getLocalTime(), (put ? "存放" : "取回"), pc.getAccountName(), pc.getName(), getFormatItemName(item, count));
				break;
			case Elf:
				// 當類型為 Elf 時，格式化消息包含時間、妖精、操作、帳號名、角色名和物品信息
				msg = String.format("%s\t妖精:%s\t%s:[%s]\t%s\r\n", getLocalTime(), (put ? "存放" : "取回"), pc.getAccountName(), pc.getName(), getFormatItemName(item, count));
				break;

		}
		if (Config.Synchronization.Operation_Manager)
			MJUIAdapter.on_warehouse_append(msg);
		synchronized (_warehouselog) {
			_warehouselog.add(msg);
		}
	}

	public void addItemAction(ItemActionType type, L1PcInstance pc, L1ItemInstance item, int count) {
		String msg = "";
		switch (type) {
			// 根據不同的情況格式化消息
			case Pickup:
				// 當類型為 Pickup 時，格式化消息包含時間、操作、帳號名、角色名和物品信息
				msg = String.format("[%s] [操作]:獲得 [帳號名稱]:%s [角色名稱]:%s [物件/物品名/數量]:%s\r\n", getLocalTime(), pc.getAccountName(), pc.getName(), getFormatItemName(item, count));
				break;
//        case AutoLoot:
//            msg = String.format("%s\t自動拾取\t%s:%s\t%s\r\n", getLocalTime(), pc.getAccountName(), "[" + pc.getName() + "]", getFormatItemName(item, count));
//            break;
			case DeathDrop:
				// 當類型為 DeathDrop 時，格式化消息包含時間、操作、帳號名、角色名和物品信息
				msg = String.format("[%s] [操作]:死亡掉落 [帳號名稱]:%s [角色名稱]:%s [物件/物品名/數量]:%s\r\n", getLocalTime(), pc.getAccountName(), pc.getName(), getFormatItemName(item, count));
				break;
			case Drop:
				// 當類型為 Drop 時，格式化消息包含時間、操作、帳號名、角色名和物品信息
				msg = String.format("[%s] [操作]:掉落 [帳號名稱]:%s [角色名稱]:%s [物件/物品名/數量]:%s\r\n", getLocalTime(), pc.getAccountName(), pc.getName(), getFormatItemName(item, count));
				break;
			case Delete:
				// 當類型為 Delete 時，格式化消息包含時間、操作、帳號名、角色名和物品信息
				msg = String.format("[%s] [操作]:刪除 [帳號名稱]:%s [角色名稱]:%s [物件/物品名/數量]:%s\r\n", getLocalTime(), pc.getAccountName(), pc.getName(), getFormatItemName(item, count));
				break;
			case del:
				// 當類型為 del 時，格式化消息包含時間、操作、帳號名、角色名和物品信息
				msg = String.format("[%s] [操作]:消失 [帳號名稱]:%s [角色名稱]:%s [物件/物品名/數量]:%s\r\n", getLocalTime(), pc.getAccountName(), pc.getName(), getFormatItemName(item, count));
				break;
		}
		if (Config.Synchronization.Operation_Manager)
			MJUIAdapter.on_item_append(msg);
		synchronized (_itemactionlog) {
			_itemactionlog.add(msg);
		}
	}

	/** 從78級開始升級時記錄 levellog */
	public void addLevel(L1PcInstance pc, int level) {
		String msg = "";

		msg = String.format("%s\t%s:%s\t等級提升%d\r\n", getLocalTime(), pc.getAccountName(), "[" + pc.getName() + "]", level);
		synchronized (_levellog) {
			_levellog.add(msg);
		}
	}

	public void addAll(String msg) {
		msg = String.format("%s\t%s\r\n", getLocalTime(), msg);

		synchronized (_chatlog) {
			_chatlog.add(msg);
		}

		synchronized (_commandlog) {
			_commandlog.add(msg);
		}

		synchronized (_connectionlog) {
			_connectionlog.add(msg);
		}

		synchronized (_error) {
			_error.add(msg);
		}

		synchronized (_enchantlog) {
			_enchantlog.add(msg);
		}

		synchronized (_tradelog) {
			_tradelog.add(msg);
		}

		synchronized (_warehouselog) {
			_warehouselog.add(msg);
		}

		synchronized (_itemactionlog) {
			_itemactionlog.add(msg);
		}

		synchronized (_levellog) {
			_levellog.add(msg);
		}
	}

	public void flush() throws IOException {
		synchronized (_chatlog) {
			if (!_chatlog.isEmpty()) {
				writeLog(_chatlog, "聊天.txt");
				_chatlog.clear();
			}
		}

		synchronized (_commandlog) {
			if (!_commandlog.isEmpty()) {
				writeLog(_commandlog, "命令.txt");
				_commandlog.clear();
			}
		}

		synchronized (_connectionlog) {
			if (!_connectionlog.isEmpty()) {
				writeLog(_connectionlog, "登入.txt");
				_connectionlog.clear();
			}
		}

		synchronized (_error) {
			if (!_error.isEmpty()) {
				writeLog(_error, "錯誤日誌.txt");
				_error.clear();
			}
		}

		synchronized (_enchantlog) {
			if (!_enchantlog.isEmpty()) {
				writeLog(_enchantlog, "強化.txt");
				_enchantlog.clear();
			}
		}

		synchronized (_tradelog) {
			if (!_tradelog.isEmpty()) {
				writeLog(_tradelog, "交易,市場.txt");
				_tradelog.clear();
			}
		}

		synchronized (_warehouselog) {
			if (!_warehouselog.isEmpty()) {
				writeLog(_warehouselog, "倉庫.txt");
				_warehouselog.clear();
			}
		}

		synchronized (_itemactionlog) {
			if (!_itemactionlog.isEmpty()) {
				writeLog(_itemactionlog, "物品日誌.txt");
				_itemactionlog.clear();
			}
		}

		synchronized (_levellog) {
			if (!_levellog.isEmpty()) {
				writeLog(_levellog, "升級.txt");
				_levellog.clear();
			}
		}

		synchronized (_cmd) {
			if (!_cmd.isEmpty()) {
				writeLog(_cmd, "CMD.txt");
				_cmd.clear();
			}
		}

		synchronized (_shop) {
			if (!_shop.isEmpty()) {
				writeLog(_shop, "NPC商店.txt");
				_shop.clear();
			}
		}

		synchronized (_dollMake) {
			if (!_dollMake.isEmpty()) {
				writeLog(_dollMake, "娃娃合成.txt");
				_dollMake.clear();
			}
		}

		synchronized (_blessOfAinPointCard) {
			if (!_blessOfAinPointCard.isEmpty()) {
				writeLog(_blessOfAinPointCard, "亞伊那莎德點數.txt");
				_blessOfAinPointCard.clear();
			}
		}

		synchronized (_dollPotential) {
			if (!_dollPotential.isEmpty()) {
				writeLog(_dollPotential, "娃娃潛力.txt");
				_dollPotential.clear();
			}
		}

		synchronized (_dollPotential) {
			if (!_dollPotential.isEmpty()) {
				writeLog(_dollPotential, "娃娃潛力.txt");
				_dollPotential.clear();
			}
		}

		synchronized (_craft_log) {
			if (!_craft_log.isEmpty()) {
				writeLog(_craft_log, "製作.txt");
				_craft_log.clear();
			}
		}

		if (Config.Synchronization.Operation_Manager)
			MJUIAdapter.on_flush();
	}

	// ** 按日期生成文件夾並保存日志 **//
	private static String getDate() {
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd hh-ss", Locale.TAIWAN);
		return s.format(Calendar.getInstance().getTime());
	}

	public void writeLog(List<String> log, String filename) throws IOException {
		//** 按日期生成文件夾並保存日誌 **//
		File f = null;
		String sTemp = StringUtil.EmptyString;
		sTemp = getDate();
		StringTokenizer s = new StringTokenizer(sTemp, StringUtil.EmptyOneString);
		date = s.nextToken();
		f = new File("LogDB/"+date);
		if(!f.exists()){ 
			f.mkdir();
		}
		//** 按日期生成文件夾並保存日誌  **//
		BufferedWriter w = new BufferedWriter(new FileWriter("LogDB/"+ date + StringUtil.SlushString + filename, true));
		PrintWriter pw = new PrintWriter(w, true);
		for (int i = 0, n = log.size(); i < n; i++) {
			pw.print(log.get(i));
		}
		pw.close();
		pw = null;
		w.close();
		w = null;
		sTemp = null;
		date = null;






		// ** 按日期生成文件夾並保存日誌 **//
/*		File f = null;
		String sTemp = "";
		sTemp = getDate();
		StringTokenizer s = new StringTokenizer(sTemp, " ");
		StringBuilder sb = new StringBuilder(256);
		date = s.nextToken();
		sb.append("logDB/").append(date);
		f = new File(sb.toString());
		// f = new File("LogDB/"+date);
		if (!f.exists())
			f.mkdir();
*/		// ** 按日期生成文件夾並保存日誌 **//
/*		sb.append("/").append(filename);
		BufferedWriter w = new BufferedWriter(new FileWriter(sb.toString(), true));
		// BufferedWriter w = new BufferedWriter(new FileWriter("LogDB/"+ date + "/" + filename, true));
		PrintWriter pw = new PrintWriter(w, true);
		for (int i = 0, n = log.size(); i < n; i++) {
			pw.print(log.get(i));
		}
		pw.close();
		pw = null;
		w.close();
		w = null;
		sTemp = null;
		date = null;*/
	}

	public String getLocalTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		GregorianCalendar localtime = new GregorianCalendar();

		return formatter.format(localtime.getTime());
	}

	public String getFormatItemName(L1ItemInstance item, int count) {
		StringBuilder sb = new StringBuilder(256);
		sb.append("[").append(item.getId()).append("]");
		if (item.getEnchantLevel() > 0)
			sb.append("+").append(item.getEnchantLevel());
		else if (item.getEnchantLevel() < 0)
			sb.append(item.getEnchantLevel());
		sb.append(item.getName());
		if (item.isStackable())
			sb.append("(").append(count).append(")");
		return sb.toString();
	}
}
