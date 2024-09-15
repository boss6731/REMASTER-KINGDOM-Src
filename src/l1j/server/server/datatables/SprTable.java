/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.server.server.datatables;

import static l1j.server.server.ActionCodes.*;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

public class SprTable {

	private static Logger _log = Logger.getLogger(SprTable.class.getName());

	private static class Frame {
		private int framecount = 1200;
		private int framerate = 1200;
	}
	
	private static class Spr {
		private final HashMap<Integer, Integer> frameCount		= new HashMap<Integer, Integer>();// original action frame
		private final HashMap<Integer, Integer> actionSpeed		= new HashMap<Integer, Integer>();// action speed
		private final HashMap<Integer, Integer> moveSpeed	 	= new HashMap<Integer, Integer>();
		private final HashMap<Integer, Integer> attackSpeed 	= new HashMap<Integer, Integer>();
		private final HashMap<Integer, Integer> damageSpeed		= new HashMap<Integer, Integer>();
		private int nodirSpellSpeed								= 1200;
		private int dirSpellSpeed								= 1200;

	}

	private static final HashMap<Integer, Spr> _dataMap = new HashMap<Integer, Spr>();
	private static final HashMap<Integer, PolyFrameSprite> _frameMap = new HashMap<Integer, SprTable.PolyFrameSprite>();// frameRate Info
	private static final double BASIC_MILLIS = 1000.0D;
	private static final SprTable _instance = new SprTable();
	private SprTable() {
		loadSprAction();
		loadPolyFrameRate();
	}

	public static SprTable getInstance() {
		return _instance;
	}

	
	// total action speed Exception
	public double getActionSpeed(int sprite, int action, int boundaryLevel){
		try{
			Spr spr = _dataMap.get(sprite);
			if(spr == null)return getActionSpeed(sprite, action);
			int frameCount = spr.frameCount.get(action);// original framCount
			if(frameCount <= 0)return getActionSpeed(sprite, action);
			PolyFrameSprite polySprite = _frameMap.get(sprite);// sprite check
			if(polySprite == null)return getActionSpeed(sprite, action);
			PolyFrameAction polyAction = polySprite.actionList.get(action);// action check
			if(polyAction == null)return getActionSpeed(sprite, action);
			PolyFrameLevelRate levelRate = polyAction.levelRateList.get(boundaryLevel);// level check
			if(levelRate == null)return getActionSpeed(sprite, action);
			return calcActionSpeed(frameCount, levelRate.rate);// calc
		}catch(Exception e){
			return 0D;
		}
	}
	
	// total action speed
	public int getActionSpeed(int sprite, int actid){
		try {
			boolean secondWeapone = false;
			if(actid == ACTION_DoubleAxeAttack){// 雙持武器
				actid = ACTION_AxeAttack;
				secondWeapone = true;
			}
			if(_dataMap.containsKey(sprite) && _dataMap.get(sprite).actionSpeed.containsKey(actid)){
				if(secondWeapone)return (int) (_dataMap.get(sprite).actionSpeed.get(actid) * 0.828);// 處理雙持武器的攻擊速度
				return _dataMap.get(sprite).actionSpeed.get(actid);
			}
		} catch (Exception e) {
			return 0;
		}
		return 0;
	}
	/**
	 * 返回指定 spr 的攻擊速度。如果未設定 spr 為指定的 weapon_type 的數據，則返回 1. attack 的數據。
	 *
	 * @param sprite -
	 *            要檢查的 spr 的 ID
	 * @param actid -
	 *            表示武器種類的值。與 L1Item.getType1() 的轉換值 +1 一致
	 * @return 指定 spr 的攻擊速度 (毫秒)
	 */
	public int getAttackSpeed(int sprite, int actid) {
		try {
			boolean secondWeapone = false;
			if (actid == ACTION_DoubleAxeAttack) { // 雙持
				actid = ACTION_AxeAttack;
				secondWeapone = true;
			}
			if (_dataMap.containsKey(sprite)) {
				if (_dataMap.get(sprite).attackSpeed.containsKey(actid)) {
					if (secondWeapone) {
						return (int) (_dataMap.get(sprite).attackSpeed.get(actid) * 0.828); // 處理雙持武器的攻擊速度
					}
					return _dataMap.get(sprite).attackSpeed.get(actid);
				} else if (actid == ACTION_Attack) {
					return 0;
				} else {
					if (_dataMap.get(sprite).attackSpeed.get(ACTION_Attack) != null)
						return _dataMap.get(sprite).attackSpeed.get(ACTION_Attack);
					else
						return 0;
				}
			}
		} catch (Exception e) {
			return 0;
		}
		return 0;
	}
	
	public int getMoveSpeed(int sprite, int actid) {
		try {
			if(_dataMap.containsKey(sprite)){
				if(_dataMap.get(sprite).moveSpeed.containsKey(actid))
					return _dataMap.get(sprite).moveSpeed.get(actid);
				else if(_dataMap.get(sprite).moveSpeed.containsKey(ACTION_Walk))
					return _dataMap.get(sprite).moveSpeed.get(ACTION_Walk);
				else
					return 0;
			}
		} catch (Exception e) {
			return 0;
		}
		return 0;
	}

	public int getDirSpellSpeed(int sprite) {
		if(_dataMap.containsKey(sprite))return _dataMap.get(sprite).dirSpellSpeed;
		return 0;
	}

	public int getNodirSpellSpeed(int sprite) {
		if(_dataMap.containsKey(sprite))return _dataMap.get(sprite).nodirSpellSpeed;
		return 0;
	}
	
	public int getDamageSpeed(int sprite, int actid) {
		try {
			if(_dataMap.containsKey(sprite) && _dataMap.get(sprite).damageSpeed.containsKey(actid)){
				return _dataMap.get(sprite).damageSpeed.get(actid);
			}
		} catch (Exception e) {
			return 0;
		}
		return 0;
	}
	
	private static class PolyFrameSprite {
		public int sprite;
		public HashMap<Integer, PolyFrameAction> actionList;
	}
	
	private static class PolyFrameAction {
		public int action;
		public HashMap<Integer, PolyFrameLevelRate> levelRateList;
	}
	
	private static class PolyFrameLevelRate {
		public int level;
		public Double rate;
	}

	/**
	 * 從幀數和幀率計算並返回動作的總時間（毫秒）。
	 */
	/*private int calcActionSpeed(int frameCount, int frameRate) {
	return (int) (frameCount * 40 * (24D / frameRate));
	}*/
	
	private int calcActionSpeed(double frameCount, double frameRate) {
		return (int)(fromRPS(frameCount, calcRps(frameRate)));
	}
	
	private double fromRPS(double frameCount, double rps){
		return frameCount * rps;
	}
	
	private double calcRps(double basic_millis, double rate){
		return basic_millis / rate;
	}
	
	private double calcRps(double frameRate){
		return calcRps(BASIC_MILLIS, frameRate);
	}




	/**
	 * 加載 spr_action 資料表。
	 */
	public void loadSprAction() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		Spr spr = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM spr_action");
			rs = pstm.executeQuery();
			while (rs.next()) {
				int key = rs.getInt("spr_id");
				if (!_dataMap.containsKey(key)) {
					spr = new Spr();
					_dataMap.put(key, spr);
				} else {
					spr = _dataMap.get(key);
				}

				int actid = rs.getInt("act_id");
				int frameCount = rs.getInt("framecount");
				int frameRate = rs.getInt("framerate");
				int speed = calcActionSpeed(frameCount, frameRate);

				switch (actid) {
				case ACTION_Walk:
				case ACTION_SwordWalk:
				case ACTION_AxeWalk:
				case ACTION_BowWalk:
				case ACTION_SpearWalk:
				case ACTION_StaffWalk:
				case ACTION_DaggerWalk:
				case ACTION_TwoHandSwordWalk:
				case ACTION_EdoryuWalk:
				case ACTION_ClawWalk:
				case ACTION_ThrowingKnifeWalk:
				case ACTION_ChainSwordWalk:
				case ACTION_DoubleAxeWalk:
					spr.moveSpeed.put(actid, speed);
					break;
				case ACTION_SkillAttack:
					spr.dirSpellSpeed = speed;
					break;
				case ACTION_SkillBuff:
				
					spr.nodirSpellSpeed = speed;
					break;
				case ACTION_Attack:
				case ACTION_SwordAttack:
				case ACTION_AxeAttack:
				case ACTION_BowAttack:
				case ACTION_SpearAttack:
				case ACTION_AltAttack:
				case ACTION_SpellDirectionExtra:
				case ACTION_StaffAttack:
				case ACTION_DaggerAttack:
				case ACTION_TwoHandSwordAttack:
				case ACTION_EdoryuAttack:
				case ACTION_ClawAttack:
				case ACTION_ThrowingKnifeAttack:
				case ACTION_ChainSwordAttack:
				case ACTION_DoubleAxeAttack:
/*					Frame f = new Frame();
					f.framecount = frameCount;
					f.framerate = frameRate;
					spr.attackSpeed.put(actid, f);*/
					spr.attackSpeed.put(actid, speed);
				case ACTION_Damage:
				case ACTION_SwordDamage:
				case ACTION_AxeDamage:
				case ACTION_SpearDamage:
				case ACTION_StaffDamage:
				case ACTION_DaggerDamage:
				case ACTION_TwoHandSwordDamage:
				case ACTION_EdoryuDamage:
				case ACTION_ClawDamage:
				case ACTION_ChainSwordDamage:
				case ACTION_DamageSpear:
					spr.damageSpeed.put(actid, speed);
					break;
				default:
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		_log.config("加載了 " + _dataMap.size() + " 條 SPR 資料");
	}
	
	// document PolyFrameRate load
	private static final String POLY_FRAME_RATE_PATH = "./data/xml/PolyFrameRate/PolyFrameRate.xml";
	private void loadPolyFrameRate(){
		try{
			Document doc = DocumentBuilderFactory
					.newInstance()
					.newDocumentBuilder()
					.parse(new File(POLY_FRAME_RATE_PATH));
			
			loadLevelInformation(doc);
			loadActionRates(doc);
			loadExceptionActionRates(doc);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void settingFrameRate(Integer action, Integer sprite, Double[] rates) {
		PolyFrameSprite spr = _frameMap.get(sprite); // 獲取 sprite
		if (spr == null) {
			spr = new PolyFrameSprite();
			spr.sprite = sprite;
			spr.actionList = new HashMap<Integer, SprTable.PolyFrameAction>();
			_frameMap.put(spr.sprite, spr);
		}

		PolyFrameAction ac = spr.actionList.get(action); // 獲取 action
		if (ac == null) {
			ac = new PolyFrameAction();
			ac.action = action;
			ac.levelRateList = new HashMap<Integer, SprTable.PolyFrameLevelRate>();
			spr.actionList.put(ac.action, ac);
		}

		for (int l = 0; l < _levelToIdx.length; l++) {
			Integer lvl = _levelToIdx[l];
			PolyFrameLevelRate lvlRate = ac.levelRateList.get(lvl); // 獲取對應等級的 rate
			if (lvlRate == null) {
				lvlRate = new PolyFrameLevelRate();
				lvlRate.level = lvl;
				lvlRate.rate = rates[l];
				ac.levelRateList.put(lvlRate.level, lvlRate);
			}
		}
	}

	// 設定邊界等級區間
	private Integer[] _levelToIdx;
	private void loadLevelInformation(Document doc){
		Element element = (Element) doc.getElementsByTagName("Level").item(0);
		_levelToIdx = parsingInteger(element.getAttribute("range"), StringUtil.CommaString);
	}

	// 設定 sprite 動作速度
	private void loadActionRates(Document doc){
		Integer[] targets = parsingNodeList(doc.getElementsByTagName("Target")); // sprite 列表
		NodeList nodes = ((Element)((NodeList)doc.getElementsByTagName("PolyFrameRate")).item(0)).getChildNodes();

		for(int i = nodes.getLength() - 1; i >= 0; --i) {
			Node node = nodes.item(i);
			if(Node.ELEMENT_NODE != node.getNodeType())
				continue;

			Element element = (Element)node;
			Double[] rates = parsingDouble(element.getAttribute("rate"), StringUtil.CommaString);
			Integer[] actions = parsingInteger(element.getAttribute("action"), StringUtil.CommaString);

			for(Integer action : actions) {
				for(Integer sprite : targets) {
					settingFrameRate(action, sprite, rates);
				}
			}
		}
	}

	// 設定例外速度（移動速度：矮人、巴風特等）
	private void loadExceptionActionRates(Document doc){
		Integer[] exceptionSprite = parsingInteger(((Element) ((NodeList) doc.getElementsByTagName("Sprite")).item(0)).getAttribute("target"), StringUtil.CommaString);
		NodeList nodes = ((Element)((NodeList)doc.getElementsByTagName("PolyFrameException")).item(0)).getChildNodes();
		for(int i=nodes.getLength() - 1; i>=0; --i){
			Node node = nodes.item(i);
			if(Node.ELEMENT_NODE != node.getNodeType())
				continue;
			if(!node.getNodeName().equalsIgnoreCase("Frame"))
				continue;
			
			Element 	element = (Element)node;
			Double[] 	rates	= parsingDouble(element.getAttribute("rate"), StringUtil.CommaString);
			Integer[] 	actions = parsingInteger(element.getAttribute("action"), StringUtil.CommaString);
			
			for(Integer action : actions){
				for(Integer sprite : exceptionSprite){
					settingFrameRate(action, sprite, rates);
				}
			}
			break;
		}
	}
	
	private Integer[] parsingNodeList(NodeList nodes){
		Integer[] result = new Integer[nodes.getLength()];
		for(int i=0; i<result.length; i++){
			result[i] = Integer.parseInt(((Element)nodes.item(i)).getTextContent().trim());
		}
		return result;
	}
	
	private Integer[] parsingInteger(String str, String splitStr){
		if(StringUtil.isNullOrEmpty(str))
			return null;
		String[] array = str.split(splitStr);
		Integer[] result = new Integer[array.length];
		for(int i=0; i<result.length; i++){
			result[i] = Integer.parseInt(array[i].trim());
		}
		return result;
	}
	
	private Double[] parsingDouble(String str, String splitStr){
		if(StringUtil.isNullOrEmpty(str))
			return null;
		String[] array = str.split(splitStr);
		Double[] result = new Double[array.length];
		for(int i=0; i<result.length; i++){
			result[i] = Double.parseDouble(array[i].trim());
		}
		return result;
	}

	// 獲取對應等級的邊界等級
	public int getBoundaryLevel(int level) {
		int result = 0;
		for (int i = _levelToIdx.length - 1; i >= 0; --i) {
			if (level >= _levelToIdx[i]) {
				return _levelToIdx[i];
			}
		}
		return result;
	}

	/**
	 * 從幀數和幀率計算並返回動作的總時間（毫秒）。
	 */
	private int calcActionSpeed(int frameCount, int frameRate) {
		return (int) (frameCount * 40 * (24D / frameRate));
	}

/**
 * 返回指定 spr 的攻擊速度。如果未設定 spr 為指定的 weapon_type 的數據，則返回 1. attack 的數據。
 *
 * @param sprid -
 *            要檢查的 spr 的 ID
 * @param actid -
 *            表示武器種類的值。與 L1Item.getType1() 的轉換值 +1 一致
 * @return 指定 spr 的攻擊速度 (毫秒)
 */

	
	/* public int getAttackSpeed(int sprid, int actid, int lv, int classid)
	  {
	    if (PolyTable.getInstance().getSpeed(sprid))
	    {
	      int fc = 22;
	      if (actid == 12)
	        fc = 25;
	      else if (actid == 19)
	        fc = 28;
	      else if (actid == 21)
	        fc = 26;
	      else if (actid == 25)
	        fc = 23;
	      else if (actid == 47)
	        fc = 21;
	      else if (actid == 51)
	        fc = 24;
	      else if (actid == 63)
	        fc = 26;
	      else if (actid == 89) {
	        fc = 22;
	      }

	      if ((sprid == 15865) || (sprid == 15545) || (sprid == 15548) || (sprid == 15550)) {
	        fc += 14;
	      }

	      if (lv >= 10) {
	        fc--;
	      }
	      if (lv >= 20) {
	        fc--;
	      }
	      if (lv >= 30) {
	        fc--;
	      }
	      if (lv >= 40) {
	        fc--;
	      }
	      if (lv >= 45) {
	        fc--;
	      }
	      if (lv >= 50) {
	        fc--;
	      }
	      if (lv >= 52) {
	        fc--;
	      }
	      if (lv >= 55) {
	        fc--;
	      }
	      if (lv >= 75) {
	        fc--;
	      }
	      if (lv >= 80) {
	        fc--;
	      }

	      return calcActionSpeed(fc, 24);
	    }

	    if (_dataMap.containsKey(Integer.valueOf(sprid))) {
	      if (((Spr)_dataMap.get(Integer.valueOf(sprid))).attackSpeed.containsKey(Integer.valueOf(actid))) {
	        Frame f = (Frame)((Spr)_dataMap.get(Integer.valueOf(sprid))).attackSpeed.get(Integer.valueOf(actid));
	        return calcActionSpeed(f.framecount, f.framerate);
	      }if (((Spr)_dataMap.get(Integer.valueOf(sprid))).attackSpeed.containsKey(Integer.valueOf(1))) {
	        Frame f = (Frame)((Spr)_dataMap.get(Integer.valueOf(sprid))).attackSpeed.get(Integer.valueOf(1));
	        return calcActionSpeed(f.framecount, f.framerate);
	      }
	      return 640;
	    }

	    return 640;
	  }*/


}


