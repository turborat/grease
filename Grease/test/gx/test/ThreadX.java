package gx.test;

public abstract class ThreadX {

	abstract protected void go() throws Exception;

	private static int n;
	private final Thread t;

	public ThreadX() {
		this(false);
	}

	public ThreadX(final boolean loop) {
		t = new Thread("threadx-" + ++n) {
			public void run() {
				do {
					try {
						go();
					} catch (InterruptedException e) {
						System.out.println(getName() + ":interrupted:" + e.getMessage());
						return;
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
				}
				while (loop);
			}
		};
		t.start();
	}

	public void kill() {
		t.interrupt();
	}

	public boolean killed() {
		return t.isAlive();
	}
}
