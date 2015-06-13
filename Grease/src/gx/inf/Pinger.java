package gx.inf;

import gx.etc.*;
import gx.obj.x.*;

import java.util.*;
import java.util.concurrent.*;

import static gx.etc.Util.*;

public class Pinger extends GX implements Ping.Recv, TimerTic.Recv {

	private final Map<Integer,Long> sent = new HashMap();
	private final Stats stats = new Stats();
	private final long wait;
	private final int count ;
	private final Ping ping = new Ping();
	private int n = 0;

	public Pinger(Args args) {
		wait  = (long) (1000 * args.get("-w", "wait time between pings in seconds", 1.0f));
		count = (int) args.get("-c", "number of pings", Float.POSITIVE_INFINITY);
	}

	protected void start() {
		if (count < 1) {
			die("?:" + count);
		}
		new TicRequestor(wait, TimeUnit.MILLISECONDS, this);
	}

	protected String cleanup() {
		System.out.println("\n"+stats.dp(0));
		if (sent.size() > 0) {
			System.out.printf("dropped:%,d (%.2f%%)\n", sent.size(), 100 * sent.size() / (double) n);
		}
		else {
			System.out.println("no packet loss");
		}
		return null;
	}

	@Override
	public void receive(Ping ping) {
		long rcvd = System.nanoTime();

		Long sentAt = sent.remove(ping.n());
		if (sentAt == null) {
			System.out.println("?:" + ping);
			return;
		}

		long elapsed = rcvd - sentAt;
		System.out.printf("%10s Sent %d bytes: n=%s rtt=%,d\n", elapsed(now()), ping.len(), ping.n(), elapsed);
		stats.tally(elapsed);

		if(n >= count) {
			System.exit(0);
		}
	}

	@Override
	public void receive(TimerTic tic) {
		ping.n(++n);
		sent.put(n, System.nanoTime());
		send(ping);
	}

}
