package gx.etc;

import gx.inf.*;
import gx.obj.x.*;

import java.util.concurrent.*;

import static gx.etc.Util.id;

public class TicRequestor implements TimerTic.Recv {

	private final long interval;
	private final TimerTic.Recv recp;
	private final GX gx;
	private final TimerReq ticReq = new TimerReq();
	private final int id;
	private int n = 0;
	private long start;

	public TicRequestor(long interval, TimeUnit unit, GX gx) {
		this.interval = unit.toNanos(interval);
		this.recp = (TimerTic.Recv) gx;
		this.gx = gx;

		gx.subscribe(this);

		id = id(gx.getClass().getSimpleName().substring(0,4).toLowerCase());
		ticReq.id(id);

		request();
	}

	@Override
	public void receive(TimerTic timertic) {
		// todo: filter properly
		if (timertic.id() != id) {
			return;
		}

		if (start == 0) {
			start = gx.now();
		}

		recp.receive(timertic);

		request();
	}

	private void request() {
		ticReq.t(start + n++ * interval);
		gx.send(ticReq);
	}

}
