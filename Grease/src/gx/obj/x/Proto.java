package gx.obj.x ;

import gx.io.* ;

import gx.etc.* ;
import gx.obj.* ;

public final class Proto extends Obj {

  static final byte TYPE = 1 ;
  static final int   LEN = 60 ;

  public interface Recv {
    void receive(Proto proto) ;
  }

  private   byte b;         // 4:21
  private  short s;         // 5:22
  private    int i;         // 6:24
  private   long l;         // 7:28
  private  float f;         // 8:36
  private double d;         // 9:40
  private   long t;         // 10:48
  private    int n;         // 11:56

  protected byte type() {
    return TYPE;
  }

  public int len() {
    return LEN;
  }

  public void to(Buf buf) {
    super.to(buf) ;
    buf.putByte(b()) ;
    buf.putShort(s()) ;
    buf.putInt(i()) ;
    buf.putLong(l()) ;
    buf.putFloat(f()) ;
    buf.putDouble(d()) ;
    buf.putLong(t()) ;
    buf.putInt(n()) ;
  }

  public void b(byte b) {
    this.b = b ;
    set(4) ;
  }

  public byte b() {
    if (!isSet(4)) {
      b(buf.getByte(21)) ;
    }
    return b ;
  }

  public void s(short s) {
    this.s = s ;
    set(5) ;
  }

  public short s() {
    if (!isSet(5)) {
      s(buf.getShort(22)) ;
    }
    return s ;
  }

  public void i(int i) {
    this.i = i ;
    set(6) ;
  }

  public int i() {
    if (!isSet(6)) {
      i(buf.getInt(24)) ;
    }
    return i ;
  }

  public void l(long l) {
    this.l = l ;
    set(7) ;
  }

  public long l() {
    if (!isSet(7)) {
      l(buf.getLong(28)) ;
    }
    return l ;
  }

  public void f(float f) {
    this.f = f ;
    set(8) ;
  }

  public float f() {
    if (!isSet(8)) {
      f(buf.getFloat(36)) ;
    }
    return f ;
  }

  public void d(double d) {
    this.d = d ;
    set(9) ;
  }

  public double d() {
    if (!isSet(9)) {
      d(buf.getDouble(40)) ;
    }
    return d ;
  }

  public void t(long t) {
    this.t = t ;
    set(10) ;
  }

  public long t() {
    if (!isSet(10)) {
      t(buf.getLong(48)) ;
    }
    return t ;
  }

  public void n(int n) {
    this.n = n ;
    set(11) ;
  }

  public int n() {
    if (!isSet(11)) {
      n(buf.getInt(56)) ;
    }
    return n ;
  }

  public String toString() {
    return "Proto["
      + "b:" + b() + " "
      + "s:" + s() + " "
      + "i:" + i() + " "
      + "l:" + l() + " "
      + "f:" + f() + " "
      + "d:" + d() + " "
      + "t:" + Util.prettyTime(t()) + " "
      + "n:" + Util.str(n())
      + "]";
  }
}