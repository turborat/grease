package gx.io;

import gx.test.*;
import org.junit.*;

import java.io.*;
import java.util.concurrent.atomic.*;

import static org.junit.Assert.*;

public class FileIOTest {

	private File file = new File("f1");
	private FileIO.Writer writer;
	private FileIO.Reader reader;
	private Buf buf;

	@Before
	public void setup() {
		buf = new Buf();
		writer = null;
		reader = null;
		cleanup();
	}

	@After
	public void cleanup() {
		if (writer != null) {
			writer.close();
		}
		if (reader != null) {
			reader.close();
		}
		file.delete();
		assertFalse(file.exists());
	}

	@Test
	public void test1() throws IOException {
		writer = new FileIO.Writer("f1");
		buf.putLong(12345);
		buf.flip();
		assertEquals(8, writer.write(buf));
		writer.close();

		reader = new FileIO.Reader("f1", false);
		assertEquals(8, reader.read(buf));
		assertEquals(12345, buf.getLong(0));
		reader.close();

		assertEquals(2 + 8, new File("f1").length());
	}

	@Test
	public void testAppend() throws IOException {
		{
			writer = new FileIO.Writer("f1");
			buf.putLong(1);
			buf.flip();
			writer.write(buf);
			writer.close();
		}

		{
			writer = new FileIO.Writer("f1");
			buf.clear();
			buf.putLong(2);
			buf.flip();
			writer.write(buf);
			writer.close();
		}

		assertEquals(2 + 8 + 2 + 8, file.length());

		reader = new FileIO.Reader("f1", false);
		Buf buf = new Buf();
		assertEquals(8, reader.read(buf));
		assertEquals(1, buf.getLong(0));
		assertEquals(8, reader.read(buf));
		assertEquals(2, buf.getLong(0));
		reader.close();
	}

	@Test
	public void testReadNoFileBlocks() throws Exception {
		final AtomicBoolean read = new AtomicBoolean(false);
		ThreadX t = new ThreadX() {
			protected void go() throws IOException {
				assertFalse(file.exists());
				reader = new FileIO.Reader("f1", true);
				reader.read(buf);
				read.set(true);
			}
		};

		Thread.sleep(200);
		t.kill();
		assertFalse(read.get());
		assertTrue(file.exists());
	}

	@Test
	public void testWrite() throws IOException {
		writer = new FileIO.Writer("f1");
		writer.flush();

		assertEquals(0, file.length());

		buf.flip();
		assertEquals(0, writer.write(buf));
		writer.flush();
		assertEquals(2, file.length());

		buf.clear();
		buf.putInt(1);
		buf.putInt(1);
		buf.putInt(1);
		buf.flip();

		assertEquals(12, writer.write(buf));
		writer.flush();
		assertEquals(2 + 2 + 8 + 4, file.length());
	}

	@Test(timeout = 2000)
	public void testReadBlocks() throws Exception {
		long start = System.currentTimeMillis();
		new ThreadX(false) {
			@Override
			protected void go() throws Exception {
				Thread.sleep(200);
				writer = new FileIO.Writer("f1");
				Buf buf = new Buf();
				buf.putInt(666);
				buf.flip();
				writer.write(buf);
			}
		};

		reader = new FileIO.Reader("f1", true);
		Buf buf = new Buf();
		assertEquals(4, reader.read(buf));
		assertTrue(System.currentTimeMillis() - start >= 200);
		assertEquals(666, buf.getInt(0));
	}

	@Test(timeout = 1000)
	public void testEof() throws IOException {
		buf.putInt(1);
		buf.flip();
		writer = new FileIO.Writer("f1");
		writer.write(buf);
		reader = new FileIO.Reader("f1", false);
		reader.read(buf);
		assertEquals(1, buf.getInt(0));
		assertEquals(FileIO.EOF, reader.read(buf));
	}

	@Test(timeout = 3000)
	public void testEofFollow() throws IOException, InterruptedException {
		writer = new FileIO.Writer("f1");
		buf.putInt(1);
		buf.flip();
		writer.write(buf);

		final AtomicInteger i = new AtomicInteger(0);

		ThreadX t = new ThreadX() {
			@Override
			protected void go() throws Exception {
				reader = new FileIO.Reader("f1", true);
				i.addAndGet(reader.read(buf));
				i.addAndGet(reader.read(buf));
				i.addAndGet(reader.read(buf));
			}
		};

		Thread.sleep(200);
		assertEquals(4, i.get());

		buf.clear();
		buf.putInt(1);
		buf.flip();
		writer.write(buf);

		Thread.sleep(200);
		assertEquals(8, i.get());

		buf.clear();
		buf.putInt(1);
		buf.flip();
		writer.write(buf);

		Thread.sleep(200);
		assertEquals(12, i.get());
	}

}
