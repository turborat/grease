package gx.inf;

import gx.etc.*;
import gx.io.*;
import gx.obj.*;
import gx.obj.x.*;

import java.util.*;
import java.util.regex.*;

import static java.util.regex.Pattern.compile;

public class Cat {

	public static void main(String...cl) {
		Args args = new Args("cat", cl);
		String pat = args.get("-f", "filter", null);
		Pattern filter = pat != null ? compile(".*" + pat + ".*") : null;
		boolean replay = args.has("-r", "replay");
		args.validate();

		Dispatcher dispatcher = new Dispatcher();
		List<Buf> toReplay = new ArrayList<>();

		try (FileIO.Reader reader = new FileIO.Reader(Hist.FNAME, false)) {
			Buf buf = new Buf();
			while (reader.read(buf) != FileIO.EOF) {
				String info = dispatcher.dispatch(buf).info();

				if (filter == null || filter.matcher(info).matches()) {
					System.out.println(info);
					if (replay) {
						toReplay.add(buf);
					}
				}

				buf = new Buf();
			}
		}

		for (Buf buf : toReplay) {
			Obj obj = dispatcher.dispatch(buf); // argh!
			buf.flip();
			Xport.Default.TX().send(obj.buf());
		}
	}
}
