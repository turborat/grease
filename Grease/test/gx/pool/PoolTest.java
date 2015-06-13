package gx.pool;

import org.junit.*;

public class PoolTest {

	private static Pool<TestPooled> pool;

	public static class TestPooled extends Refs {
		private TestPooled() {
			super(pool);
		}
	}

	@Before
	public void setup() {
		pool = new Pool<>(TestPooled.class);
	}

	@Test
	public void test1() {
		Refs tp = pool.acquire();
		Assert.assertEquals(1, tp.refs());
		Assert.assertEquals(0, pool.size());

		tp.release();
		Assert.assertEquals(0, tp.refs());
		Assert.assertEquals(1, pool.size());
	}

	@Test
	public void retain() {
		Refs tp = pool.acquire();
		Assert.assertEquals(1, tp.refs());
		Assert.assertEquals(0, pool.size());

		tp.retain();
		Assert.assertEquals(2, tp.refs());
		Assert.assertEquals(0, pool.size());

		tp.release();
		Assert.assertEquals(1, tp.refs());
		Assert.assertEquals(0, pool.size());

		tp.release();
		Assert.assertEquals(0, tp.refs());
		Assert.assertEquals(1, pool.size());
	}

	@Test
	public void poolSize() {
		Pooled tp1 = pool.acquire();
		Pooled tp2 = pool.acquire();
		Assert.assertEquals(0, pool.size());
		Assert.assertNotSame(tp1, tp2);

		tp1.release();
		Assert.assertEquals(1, pool.size());

		tp2.release();
		Assert.assertEquals(2, pool.size());
	}

	@Test
	public void recycling() {
		Pooled tp = pool.acquire();
		tp.release();
		Assert.assertSame(tp, pool.acquire());
	}

}
