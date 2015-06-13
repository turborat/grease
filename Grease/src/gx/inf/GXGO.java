package gx.inf;

import gx.etc.*;

import static java.lang.ClassLoader.*;

public class GXGO {

	public static void main(String[] cli) throws Throwable {

		Args args = new Args(cli);

		Class<?> clz = getSystemClassLoader().loadClass(args.prog());

		final GX gx = (GX) clz.getConstructor(Args.class).newInstance(args);

		boolean recover = args.has("-r", "recover");

		args.validate();

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				gx.stop();
				System.out.println("-:" + gx.getClass().getSimpleName().toLowerCase() + xx(gx.cleanup()));
				Runtime.getRuntime().halt(0);
			}
		});

		System.out.println("+:" + gx.getClass().getSimpleName().toLowerCase());

		if (recover) {
			gx.recover();
		}

		gx.start();

		System.gc();

		gx.realtime();

	}

	private static String xx(String str) {
		return str == null ? "" : (" : " + str);
	}
}
