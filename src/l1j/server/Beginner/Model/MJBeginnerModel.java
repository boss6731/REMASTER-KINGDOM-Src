package l1j.server.Beginner.Model;

import l1j.server.Beginner.View.MJBeginnerView;

public interface MJBeginnerModel<V extends MJBeginnerView> {
	public void on(V view);
}
