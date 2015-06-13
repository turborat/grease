package gx.io;

public class BareMetal {

	public static void main(String[] args) throws Exception {
		Buf buf = new Buf(0x10);
		UDP.RX rx = new UDP.RX(55555);
		UDP.TX tx = new UDP.TX(55555);
		int n=0;
		long min=-1;

		while(true) {
			buf.clear();
			buf.putLong((byte) 1);
			buf.flip();

			long t1 = System.nanoTime();

			tx.send(buf);
			rx.receive(buf);

			long elapsed = System.nanoTime() - t1;

			if (min == -1 || elapsed < min) {
				min = elapsed ;
			}

			System.out.printf("%,d (%,d)\n", elapsed, min);

			if (++n>20000) {
				Thread.sleep(300);
			}

		}
	}
}
