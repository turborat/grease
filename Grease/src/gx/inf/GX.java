package gx.inf;

import gx.io.*;
import gx.obj.*;
import gx.obj.x.*;

import java.io.*;

import static gx.io.FileIO.*;

/**
 * ctor -> recover -> start -> realtime -> stop -> cleanup
 */
public class GX {

	private Xport.RX rx	= Xport.Default.RX();
	private Xport.TX tx;
	private final Buf txBuf = new Buf();
	private final Buf rxBuf = new Buf();
	private boolean realtime = true;
	private long now;
	private long lastSeqNo = 0;

	private final Dispatcher dispatcher = new Dispatcher() {
		@Override
		protected void process(Obj obj) {
			long seqNo = obj.seqNo();
			if (seqNo != lastSeqNo+1 && lastSeqNo > 0) {
				System.err.printf("!:seq-err@%s (%,d)\n", seqNo, seqNo - lastSeqNo);
			}
			lastSeqNo = seqNo;

			now = obj.ts();
		}
	};

	public GX() {
		subscribe(this);
	}

	public void subscribe(Object obj) {
		this.dispatcher.subscribe(obj);
	}

	protected void start() {
	}

	protected String cleanup() {
		return null;
	}

	public void send(Obj obj) {
		if (!realtime) {
			return;
		}

		if (tx == null) {
			tx = Xport.Default.TX();
		}

		txBuf.clear();
		obj.to(txBuf);
		txBuf.flip();
		tx.send(txBuf);
	}

	void stop() {
		realtime = false;
	}

	public long now() {
		// todo: warn if now==0
		return now;
	}

	void recover() {
		if (!new File(Hist.FNAME).exists()) {
			System.out.println("nothing to recover");
			return;
		}

		realtime = false;

		try (FileIO.Reader reader = new FileIO.Reader(Hist.FNAME, false)) {
			long start = System.nanoTime();
			long n=0;
			while (reader.read(rxBuf) != EOF) {
				n++;
				dispatcher.dispatch(rxBuf);
				rxBuf.clear();
			}
			long elapsed = System.nanoTime() - start;
			float s = elapsed / 1_000_000_000f;
			System.out.printf("recovered %,d events in %,fs (%,d/s)\n", n, s, (int) (lastSeqNo/s));
		}

		realtime = true;
	}

	protected void realtime() {
		while(realtime) {
			rxBuf.clear();
			rx.receive(rxBuf);
			rxBuf.flip();
			dispatcher.dispatch(rxBuf);
		}
	}

}

