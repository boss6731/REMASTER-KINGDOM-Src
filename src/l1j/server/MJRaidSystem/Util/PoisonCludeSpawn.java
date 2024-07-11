package l1j.server.MJRaidSystem.Util;

import l1j.server.server.IdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1NpcDeleteTimer;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.templates.L1Npc;

public class PoisonCludeSpawn implements Runnable{
	private static String _exceptionMessage 			= "[" + PoisonCludeSpawn.class.getName() + "] data not found posionId : %d";
	private static final int		POISON_MATRIX_SIZE 	= 5;
	private static final int[][] 	POISON_MATRIX		= new int[][]{
		{0,0,1,0,0},
		{0,1,1,1,0},
		{1,1,1,1,1},
		{0,1,1,1,0},
		{0,0,1,0,0},
	};
	
	private L1Character 	_cha;
	private int				_pid;
	private int				_duration;
	private int				_x;
	private int				_y;
	public PoisonCludeSpawn(L1Character cha, int x, int y, int poisonId, int duration){
		_cha 		= cha;
		_pid		= poisonId;
		_duration	= duration;
		_x			= x;
		_y			= y;
	}
	
	@Override
	public void run(){
		try{
			L1Npc poison = NpcTable.getInstance().getTemplate(_pid);
			if(poison == null)
				throw new NullPointerException(String.format(_exceptionMessage, _pid));
			
			int					mid		= _cha.getMapId();
			int					cx		= 0;
			int					cy		= 0;
			L1MJRaidPoisonInstance npc 	= null;
			
			cx 	= _x - (POISON_MATRIX_SIZE / 2);
			cy	= _y - (POISON_MATRIX_SIZE / 2);
			for(int row = 0; row < POISON_MATRIX_SIZE; row++){
				for(int col = 0; col < POISON_MATRIX_SIZE; col++){
					if(POISON_MATRIX[row][col] == 0)
						continue;
					
					npc = new L1MJRaidPoisonInstance(poison);
					npc.setId(IdFactory.getInstance().nextId());
					npc.setCurrentSprite(poison.get_gfxid());
					npc.setX(cx + row);
					npc.setY(cy + col);
					npc.setHomeX(cx + row);
					npc.setHomeY(cy + col);
					npc.setHeading(0);
					npc.setMap((short)mid);
					L1World.getInstance().storeObject(npc);
					L1World.getInstance().addVisibleObject(npc);
					L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc, _duration);
					timer.begin();
					if(_cha instanceof L1MonsterInstance)
						npc.setOwner((L1MonsterInstance)_cha);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
