package gx.io;

import gx.etc.*;

import java.io.*;
import java.net.*;
import java.nio.channels.*;

import static gx.etc.Util.*;
import static java.net.StandardSocketOptions.*;

public abstract class UDP {

	static final int SEQ_PORT = 54321;
	static final int RCV_PORT = 54322;
	static final String GROUP = "225.0.0.100";
	static final String[] NIS = {"lo0", "lo"};

	public static boolean block = true;

	protected final InetSocketAddress addr;
	protected final InetAddress grp;
	protected final NetworkInterface ni;
	protected final DatagramChannel channel;
	private final String str;

	protected UDP(int port) {
		try {
			ni = findNi();
			grp = InetAddress.getByName(GROUP);
			addr = new InetSocketAddress(grp, port);
			channel = DatagramChannel.open(StandardProtocolFamily.INET);
			channel.setOption(SO_REUSEADDR, true);
			channel.setOption(IP_MULTICAST_IF, ni);
			channel.setOption(IP_MULTICAST_TTL, 0);
			channel.configureBlocking(block);

			str = String.format("%s: %s en:%s mtu:%s ttl:%s block:%s rxbuf:%s txbuf:%s",
				getClass().getName().replaceFirst(".*\\.", ""),
				addr,
				ni.getDisplayName(),
				ni.getMTU(),
				channel.getOption(IP_MULTICAST_TTL),
				channel.isBlocking(),
				channel.getOption(SO_RCVBUF),
				channel.getOption(SO_SNDBUF)
		 	 );
		}
		catch (Exception e) {
			throw die(e);
		}
	}

	public String toString() {
		return str;
	}

	private NetworkInterface findNi() throws SocketException {
		for (String ni : NIS) {
			NetworkInterface ret = NetworkInterface.getByName(ni);
			if (ret != null) {
				return ret;
			}
		}
		return null;
	}

	public static class TX extends UDP implements Xport.TX {

		public TX(int port) {
			super(port);
		}

		TX(boolean reverse) {
			super(reverse ? RCV_PORT : SEQ_PORT);
//			try {
//				channel.connect(addr);
//			} catch (IOException e) {
//				Util.die(e);
//			}
		}

		@Override
		public void send(Buf buf) {
			try {
				channel.send(buf.buf(), addr);
			} catch (IOException e) {
				die(e);
			}
		}
	}

	public static class RX extends UDP implements Xport.RX {

		public RX(int port) {
			super(port);
			try {
				channel.join(grp, ni);
				channel.bind(addr);
			} catch (IOException e) {
				die(e);
			}
		}

		public RX(boolean reverse) {
			this(reverse ? SEQ_PORT : RCV_PORT);
		}

		@Override
		public void receive(Buf buf) {
			try {
				while(channel.receive(buf.buf()) == null) ;
			} catch (IOException e) {
				Util.die(e);
			}
		}
	}
}
