package gx.io;

public interface Xport {

	public interface RX {
		void receive(Buf buf);
	}

	public interface TX {
		void send(Buf buf);
	}

	public static class Default {
		private static RX rx ;
		private static TX tx ;

		public static boolean reverse;

		public static RX RX() {
			if (rx == null) {
				rx = new UDP.RX(reverse);
			}
			return rx;
		}

		public static TX TX() {
			if (tx == null) {
				tx = new UDP.TX(reverse);
			}
			return tx;
		}
	}

}
