package gx.inf;

import gx.io.*;

public class Seq {

	public static void main(String...args) throws Exception {

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				System.out.println("-:seq");
				Runtime.getRuntime().halt(0);
			}
		});

		Xport.Default.reverse = true;
		Xport.RX rx = Xport.Default.RX();
		Xport.TX tx = Xport.Default.TX();

		System.out.println(rx);
		System.out.println(tx);
		System.out.println("+:seq");

		System.gc();

		long seqNo = 0 ;
		Buf buf = new Buf();

		while (true) {
			buf.clear();
			rx.receive(buf);
			buf.flip();
			buf.buf().putLong(1, ++seqNo);
			buf.buf().putLong(9, System.nanoTime());
			tx.send(buf);
		}
	}
}
