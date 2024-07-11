package l1j.server.GameSystem.Colosseum;

import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class L1ColosseumSpawn implements Comparable<L1ColosseumSpawn> {
	private int _id;
	private int _ubId;
	private int _pattern;
	private int _group;
	private int _npcTemplateId;
	private int _amount;
	private int _spawnDelay;
	private int _sealCount;
	private String _name;
	private String _message;
	public int getId() {
		return _id;
	}

	public void setId(int id) {
		_id = id;
	}

	public int getUbId() {
		return _ubId;
	}

	public void setUbId(int ubId) {
		_ubId = ubId;
	}

	public int getPattern() {
		return _pattern;
	}

	public void setPattern(int pattern) {
		_pattern = pattern;
	}

	public int getGroup() {
		return _group;
	}

	public void setGroup(int group) {
		_group = group;
	}

	public int getNpcTemplateId() {
		return _npcTemplateId;
	}

	public void setNpcTemplateId(int npcTemplateId) {
		_npcTemplateId = npcTemplateId;
	}

	public int getAmount() {
		return _amount;
	}

	public void setAmount(int amount) {
		_amount = amount;
	}

	public int getSpawnDelay() {
		return _spawnDelay;
	}

	public void setSpawnDelay(int spawnDelay) {
		_spawnDelay = spawnDelay;
	}

	public int getSealCount() {
		return _sealCount;
	}

	public void setSealCount(int i) {
		_sealCount = i;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}
	
	public String get_message() {
		return _message;
	}

	public void set_message(String _message) {
		this._message = _message;
	}

	public void spawnOne() {
		L1Colosseum ub = ColosseumTable.getInstance().getUb(_ubId);
		L1Location loc = ub.getLocation().randomLocation((ub.getLocX2() - ub.getLocX1()) / 2, false);
		L1MonsterInstance mob = new L1MonsterInstance(NpcTable.getInstance().getTemplate(getNpcTemplateId()));

		mob.setId(IdFactory.getInstance().nextId());
		mob.setHeading(5);
		if(getGroup() % 2 == 1 && getGroup() != 11) {
			mob.setX(loc.getX());
			mob.setHomeX(loc.getX());
			mob.setY(loc.getY());
			mob.setHomeY(loc.getY());
		} else {
			mob.setX(33537);
			mob.setHomeX(33537);
			mob.setY(32703);
			mob.setHomeY(32703);
		}
		mob.setMap((short) loc.getMapId());
		mob.setUbSealCount(getSealCount());
		mob.setUbId(getUbId());

		L1World.getInstance().storeObject(mob);
		L1World.getInstance().addVisibleObject(mob);

		ProtoOutputStream stream = SC_WORLD_PUT_OBJECT_NOTI.make_stream(mob);
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(mob)) {
			pc.addKnownObject(mob);
			mob.addKnownObject(pc);
			pc.sendPackets(stream, false);
		}
		mob.onNpcAI();
		mob.getLight().turnOnOffLight();

	}

	public void spawnAll() {
		for (int i = 0; i < getAmount(); i++) {
			spawnOne();
		}
	}

	public int compareTo(L1ColosseumSpawn rhs) {
		if (getId() < rhs.getId()) {
			return -1;
		}
		if (getId() > rhs.getId()) {
			return 1;
		}
		return 0;
	}

}
