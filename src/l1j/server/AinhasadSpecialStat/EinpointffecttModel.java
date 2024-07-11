package l1j.server.AinhasadSpecialStat;

import java.util.ArrayList;

public class EinpointffecttModel {
	public EinpointffecttModel(){
	}
	
	private int Level;
	private ArrayList<Integer> Bless;
	private ArrayList<Integer> Lucky;
	private ArrayList<Integer> Vital;
	private ArrayList<Integer> Invoke;
	private ArrayList<Integer> Restore;
	private ArrayList<Integer> Potion;
	
	public int getLevel() {
		return Level;
	}
	public void setLevel(int level) {
		Level = level;
	}
	public ArrayList<Integer> getBless() {
		return Bless;
	}
	public void setBless(ArrayList<Integer> bless) {
		Bless = bless;
	}
	public ArrayList<Integer> getLucky() {
		return Lucky;
	}
	public void setLucky(ArrayList<Integer> lucky) {
		Lucky = lucky;
	}
	public ArrayList<Integer> getVital() {
		return Vital;
	}
	public void setVital(ArrayList<Integer> vital) {
		Vital = vital;
	}
	public ArrayList<Integer> getInvoke() {
		return Invoke;
	}
	public void setInvoke(ArrayList<Integer> invoke) {
		Invoke = invoke;
	}
	public ArrayList<Integer> getRestore() {
		return Restore;
	}
	public void setRestore(ArrayList<Integer> restore) {
		Restore = restore;
	}
	public ArrayList<Integer> getPotion() {
		return Potion;
	}
	public void setPotion(ArrayList<Integer> potion) {
		Potion = potion;
	}
	
}
