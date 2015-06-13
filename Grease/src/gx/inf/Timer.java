package gx.inf;

import gx.etc.*;
import gx.obj.x.*;

import java.util.concurrent.*;

public class Timer extends GX implements TimerReq.Recv {

	private final ScheduledExecutorService ex
		= Executors.newSingleThreadScheduledExecutor();

	private final TimerTic tic = new TimerTic();

	public Timer(Args args) {
	}

	@Override
	public void receive(TimerReq req) {
		final int id = req.id();
		final long when = req.t();

		ex.schedule(new Runnable() {
			@Override
			public void run() {
				synchronized (Timer.this) {
					tic.reset();
					tic.id(id);
					tic.t(when);
					send(tic);
				}
			}
		}, when - now(), TimeUnit.NANOSECONDS);
	}
}
