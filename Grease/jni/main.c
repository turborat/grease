#include <time.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include "udp.h"

main(int argc, char *argv[]) {
  int port = 12345; 
  char* group = "225.0.0.37"; 
  int n, len=128;
  char buf[len];

  if (argc == 1) { 
    printf("recv..\n") ; 
    struct sock* ss = udprx(group, port); 
    while(1) { 
      n = receive(ss, buf, len); 
      buf[n] = 0 ; 
      printf(">> %s (%d)\n", buf, n);
    }
  }

  struct sock* ss = udptx(group, port) ; 
  while (1) {
    n = sendx(ss, argv[1], strlen(argv[1])) ; 
    printf("<< %s (%d)\n", argv[1], n) ; 
    sleep(1);
  }
}
