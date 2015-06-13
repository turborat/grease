all: udp baremetal UDPJni.class 

recv: UDPJni.class
	java -cp . -Djava.library.path=. UDPJni 

send: UDPJni.class
	java -cp . -Djava.library.path=. UDPJni "blah blah blah.."

udp: main.c udp.o
	gcc -o $@ $< udp.o

baremetal: baremetal.c udp.o
	gcc -o $@ $< udp.o

udp.o: udp.c
	gcc -o $@ $< -c 

libbridge.dylib: bridge.c udp.o
	gcc -o $@ $< -dynamiclib udp.o -I /System/Library/Frameworks/JavaVM.framework//Versions/A/Headers

UDPJni.class: UDPJni.java libbridge.dylib
	javac $<
	javah -cp . UDPJni

clean:
	rm -f udp baremetal *.o lib* *class UDPJni*.h
