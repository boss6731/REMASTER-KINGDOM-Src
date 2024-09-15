package l1j.server.MJRankSystem.Loader;

import l1j.server.MJRankSystem.Business.MJRankBusiness;
import l1j.server.MJTemplate.MJPropertyReader;
import l1j.server.MJTemplate.Command.MJCommand;
import l1j.server.MJTemplate.Command.MJCommandArgs;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SystemMessage;

public class MJRankLoadManager implements MJCommand{
	private static MJRankLoadManager _instance;
	public static MJRankLoadManager getInstance(){
		if(_instance == null)
			_instance = new MJRankLoadManager();
		return _instance;
	}
	
	public static int MRK_SYS_UPDATE_CLOCK_CLASS;
	public static int MRK_SYS_UPDATE_CLOCK;
	public static boolean MRK_SYS_ISON;
	public static int MRK_SYS_MINLEVEL;
	public static int MRK_SYS_TOTAL_RANGE;
	public static int MRK_SYS_CLASS_RANGE;
	public static int MRK_SYS_RANK_POTION;
	public static int MRK_TOPPROTECTION_ID;
	public static boolean MRK_TOPCLASSPROTECTION_USE;
	public static String MRK_TOPCLASSPROTECTION_ID;
	public static String MRK_2NDCLASSPROTECTION_ID;
	public static String MRK_3RDCLASSPROTECTION_ID;
	public static boolean MRK_TOP_GIVE_ITEM_USE; 
	public static String MRK_TOP_GIVE_ITEM_LIMIT;
	public static String MRK_TOP_GIVE_ITEM_ID;
	public static String MRK_TOP_GIVE_ITEM_ENCHANT;
	public static boolean MRK_1ST_GIVE_AZIT_USE;
	public static int MRK_1ST_GIVE_AZIT_KEY_ITEM_ID;
	
	private static int MRK_LOADING_COUNT;
	
	private MJRankLoadManager(){
	}
	
	public void load(){
		loadConfig();
		MJRankUserLoader.getInstance();
		if(MRK_SYS_ISON)
			MJRankBusiness.getInstance().run();
	}
	
	public void loadConfig(){
		MJPropertyReader reader = null;
		try{
			reader 							= new MJPropertyReader("./config/mj_rank.properties");
			MRK_SYS_UPDATE_CLOCK_CLASS		= reader.readInt("UpdateClock_Class", "25200");
			MRK_SYS_UPDATE_CLOCK			= reader.readInt("UpdateClock", "25200");
			if(MRK_LOADING_COUNT++ == 0)
				MRK_SYS_ISON				= reader.readBoolean("isStartupRankSystem", "true");
			MRK_SYS_MINLEVEL				= reader.readInt("InRankMinLevel", "60");
			MRK_SYS_TOTAL_RANGE				= reader.readInt("TotalRankRange", "200");
			MRK_SYS_CLASS_RANGE				= reader.readInt("ClassRankRange", "200");
			MRK_SYS_RANK_POTION				= reader.readInt("rankingPotionLevel", "30");
			MRK_TOPPROTECTION_ID			= reader.readInt("TopProtectionItemId", "5558");
			MRK_TOPCLASSPROTECTION_USE		= reader.readBoolean("TopClassProtectionItemUse", "false");
			MRK_TOPCLASSPROTECTION_ID		= reader.readString("TopClassProtectionItemId", "4100624,4100625,4100626,4100627,4100628,4100629,4100630,4100631,4100632");
			MRK_2NDCLASSPROTECTION_ID		= reader.readString("2ndClassProtectionItemId", "30001756,30001757,30001758,30001759,30001760,30001761,30001762,30001763,30001764,30001765");
			MRK_3RDCLASSPROTECTION_ID		= reader.readString("3rdClassProtectionItemId", "30001766,30001767,30001768,30001769,30001770,30001771,30001772,30001773,30001774,30001775");
			
			MRK_TOP_GIVE_ITEM_USE			= reader.readBoolean("RankGiveItem", "false");
			MRK_TOP_GIVE_ITEM_LIMIT			= reader.readString("RankGiveLimit","1,2,3");
			MRK_TOP_GIVE_ITEM_ID			= reader.readString("RankGiveItemId","1,2,3");
			MRK_TOP_GIVE_ITEM_ENCHANT		= reader.readString("RankGiveItemEnchant","1,2,3");
			MRK_1ST_GIVE_AZIT_USE			= reader.readBoolean("Rank1stAzit", "false");
			MRK_1ST_GIVE_AZIT_KEY_ITEM_ID	= reader.readInt("Rank1stAzitItemId", "30001881");
					
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(reader != null)
				reader.dispose();
		}
	}

	@Override
	public void execute(MJCommandArgs args) {
		try{
			switch(args.nextInt()){
			case 1:
				toggleCommand(args);
				break;
			case 2:
				reloadCommand(args);
				break;
			case 3:
				settingCommand(args);
				break;
			default:
				throw new Exception();
			}
			
		}catch(Exception e){
			args.notify(".��ŷ�ý���");
			args.notify("[1.���][2.���ε�][3.����]");
		}finally{
			args.dispose();
		}
	}
	
	private void toggleCommand(MJCommandArgs args){
		try{
			switch(args.nextInt()){
			case 1:
				if(MRK_SYS_ISON)
					args.notify("�̹� Ȱ��ȭ ���Դϴ�.");
				else{
					MRK_SYS_ISON = true;
					MJRankUserLoader.reload();
					MJRankBusiness.getInstance().run();
					Thread.sleep(3000L);
					for(L1PcInstance pc : L1World.getInstance().getAllPlayers()){
						if(pc == null || pc.getAI() != null)
							continue;
						
						MJRankUserLoader.getInstance().onUser(pc);
					}
					args.notify("�ý����� Ȱ��ȭ�մϴ�.");
				}
				break;
				
			case 2:
				if(!MRK_SYS_ISON)
					args.notify("�̹� ��Ȱ��ȭ ���Դϴ�.");
				else{
					MRK_SYS_ISON = false;
					MJRankBusiness.getInstance().dispose();
					MJRankUserLoader.getInstance().offBuff();
					args.notify("�ý����� ��Ȱ��ȭ �մϴ�.");
				}
				break;
				
			case 3:
				args.notify(String.format("Ȱ��ȭ ���� : %s", MRK_SYS_ISON));
				break;
			default:
				throw new Exception();
			}
		}catch(Exception e){
			args.notify(".��ŷ�ý��� 1 [1.��][2.��][3.����]");
		}
	}
	
	private void reloadCommand(MJCommandArgs args){
		try{
			switch(args.nextInt()){
			case 1:
				loadConfig();
				args.notify("��ŷ ������ ���ε�Ǿ����ϴ�.");
				break;
				
			case 2:
				if(!MRK_SYS_ISON){
					MRK_SYS_ISON = true;
					MJRankBusiness.getInstance().run();
				}else{
					MRK_SYS_ISON = false;
					MJRankBusiness.getInstance().dispose();
					MRK_SYS_ISON = true;
					MJRankUserLoader.getInstance().offBuff();
					Thread.sleep(1000L);
					MJRankUserLoader.reload();
					MJRankBusiness.getInstance().run();
					Thread.sleep(3000L);
					for(L1PcInstance pc : L1World.getInstance().getAllPlayers()){
						if(pc == null || pc.getAI() != null)
							continue;
						
						MJRankUserLoader.getInstance().onUser(pc);
					}
				}
				args.notify("�ý����� �簻�ŵ�(GM�� 1�ʷ� ���� �� ����(����)");

				L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("\\f3��ŷ�� �������� �Ϸ�!���� �������� ���� �˴ϴ�"));
				L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\f3��ŷ�ý����� �������� �Ǿ� ���� �ð� �������� ���� �˴ϴ�."));
				break;
			default:
				throw new Exception();
			}
			
		}catch(Exception e){
			args.notify(".��ŷ�ý��� 2 [1.����][2.�ý����簡��]");
		}
	}
	
	private void settingCommand(MJCommandArgs args){
		try{
			switch(args.nextInt()){
			case 1:{
				int old = MJRankLoadManager.MRK_SYS_UPDATE_CLOCK;
				MJRankLoadManager.MRK_SYS_UPDATE_CLOCK = args.nextInt();
				args.notify(String.format("�ý��� Ŭ���� %d�ʿ��� %d�ʷ� ����Ǿ����ϴ�.", old, MJRankLoadManager.MRK_SYS_UPDATE_CLOCK));
				break;
			}
			case 2:{
				int old = MJRankLoadManager.MRK_SYS_MINLEVEL;
				MJRankLoadManager.MRK_SYS_MINLEVEL = args.nextInt();
				args.notify(String.format("�ּ� ������ %d�ʿ��� %d�ʷ� ����Ǿ����ϴ�.", old, MJRankLoadManager.MRK_SYS_MINLEVEL));
				break;
			}
			case 3:{
				int old = MJRankLoadManager.MRK_SYS_RANK_POTION;
				MJRankLoadManager.MRK_SYS_RANK_POTION = args.nextInt();
				args.notify(String.format("��ŷ���� ���� ������ %d���� %d�� ����Ǿ����ϴ�.", old, MJRankLoadManager.MRK_SYS_RANK_POTION));
				break;
			}
			case 4:{
				int old = MJRankLoadManager.MRK_SYS_TOTAL_RANGE;
				MJRankLoadManager.MRK_SYS_TOTAL_RANGE = args.nextInt();
				args.notify(String.format("��ü��ŷ ���� ������ %d���� %d�� ����Ǿ����ϴ�.", old, MJRankLoadManager.MRK_SYS_TOTAL_RANGE));
				break;
			}
			case 5:{
				int old = MJRankLoadManager.MRK_SYS_CLASS_RANGE;
				MJRankLoadManager.MRK_SYS_CLASS_RANGE = args.nextInt();
				args.notify(String.format("Ŭ������ŷ ���� ������ %d���� %d�� ����Ǿ����ϴ�.", old, MJRankLoadManager.MRK_SYS_CLASS_RANGE));
				break;
			}
			default:
				throw new Exception();
			}
			
		}catch(Exception e){
			args.notify(".��ŷ�ý��� 3 [�ɼ�] [��]");
			args.notify("�ɼ� : [1.Ŭ��][2.�ּҷ���][3.��ŷ����][4.��ü����][5.Ŭ��������]");
		}
	}
}
