package gx.obj.x ;

import gx.io.* ;

import gx.etc.* ;
import gx.obj.* ;

public final class Ping extends Obj {

  static final byte TYPE = 2 ;
  static final int   LEN = 25 ;

  public interface Recv {
    void receive(Ping ping) ;
  }

  private    int n;         // 4:21

  protected byte type() {
    return TYPE;
  }

  public int len() {
    return LEN;
  }

  public void to(Buf buf) {
    super.to(buf) ;
    buf.putInt(n()) ;
  }

  public void n(int n) {
    this.n = n ;
    set(4) ;
  }

  public int n() {
    if (!isSet(4)) {
      n(buf.getInt(21)) ;
    }
    return n ;
  }

  public String toString() {
    return "Ping["
      + "n:" + n()
      + "]";
  }
}