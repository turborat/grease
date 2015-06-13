#include <sys/socket.h>
#include <arpa/inet.h>
#include <stdio.h>
#include <strings.h>
#include <mach/mach.h>
#include <mach/mach_time.h>
#include <inttypes.h>
#include "udp.h"

#define PORT 12345
#define GROUP "225.0.0.37"

main(int argc, char *argv[]) {

  struct sock* tx = udptx(GROUP, PORT) ; 
  struct sock* rx = udprx(GROUP, PORT); 
  uint64_t min=-1;
  int n;

  while (1) {
    uint64_t now = mach_absolute_time(); 
    sendx(tx, (char*) &now, 8) ; 

    //printf("<< %" PRId64 "\n", now) ; 

    uint64_t then;
    receive(rx, (char*) &then, 8); 

    uint64_t elapsed = mach_absolute_time() - then; 
    if (min == -1 || elapsed < min) {
      min = elapsed;
    }

    //printf(">> %" PRId64 "\n", then) ; 
    printf("%" PRId64 "\t(%" PRId64 ")\n", elapsed, min) ; 

    sleep(1); 
  }
}
