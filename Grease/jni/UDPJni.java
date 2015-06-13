import java.nio.* ; 

public class UDPJni {

  static { 
    System.loadLibrary("bridge") ; 
  }

  protected native long udprx(String group, int port) ;
  protected native long udptx(String group, int port) ; 
  protected native int receive(long ptr, ByteBuffer buf, int capacity) ;
  protected native int sendx(long ptr, ByteBuffer buf, int limit) ; 

  protected long ptr; 

  public static class RX extends UDPJni {
    RX(String group, int port) {
      ptr = udprx(group, port); 
    }

    public void read(ByteBuffer buf) {
      int read = receive(ptr, buf, buf.capacity()); 
      buf.limit(read); 
    }
  }

  public static class TX extends UDPJni {
    TX(String group, int port) { 
      ptr = udptx(group, port); 
    }

    public void write(ByteBuffer buf) { 
      sendx(ptr, buf, buf.limit()) ; 
    }
  }

  private static String str(ByteBuffer buf) { 
    byte[] bytes = new byte[buf.limit()]; 
    for (int i=0 ; i<buf.limit() ; ++i) { 
      bytes[i] = buf.get(i); 
      if (bytes[i] < ' ' || '~' < bytes[i]) { 
        return buf.toString();
      }
    }
    return new String(bytes); 
  }

  public static void main(String...argv) throws Exception { 
    int PORT = 12345 ; 
    String GROUP = "225.0.0.37" ; 

    ByteBuffer buf = ByteBuffer.allocateDirect(256); 

    if (argv.length < 1) { 
      RX rx = new UDPJni.RX(GROUP, PORT); 
      while(true) {
        buf.clear();
        rx.read(buf); 
        long now = System.nanoTime(); 
        System.out.printf(">> %s : %s\n", str(buf), buf.limit());
      }
    }

    else { 
      TX tx = new UDPJni.TX(GROUP, PORT); 
      String msg = "xox - i hate you";
      while(true) { 
        buf.clear() ;
        buf.put(msg.getBytes()) ; 
        buf.flip(); 
        tx.write(buf); 
        System.out.printf("<< %s : %s\n", str(buf), buf.limit());
        Thread.sleep(1000);
      }
    }
  }

}
