#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <time.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include "udp.h"

struct sock* udprx(char* group, int port) { 
  struct sock* ss = (struct sock*) malloc(sizeof(struct sock)) ; 
  ss->addrlen = sizeof(ss->addr) ; 
  struct ip_mreq mreq;
  u_int yes=1;            

  if ((ss->fd=socket(AF_INET,SOCK_DGRAM,0)) < 0) {
    perror("socket");
    exit(1);
  }

  /* allow multiple sockets to use the same PORT number */
  if (setsockopt(ss->fd, SOL_SOCKET, SO_REUSEPORT, &yes, sizeof(yes)) < 0) {
    perror("Reusing PORT failed");
    exit(1);
  }

  memset(&ss->addr, 0, ss->addrlen);
  ss->addr.sin_family=AF_INET;
  ss->addr.sin_addr.s_addr=htonl(INADDR_ANY); /* N.B.: differs from sender */
  ss->addr.sin_port=htons(port);

  if (bind(ss->fd, (struct sockaddr*) &ss->addr, ss->addrlen) < 0) {
    perror("bind");
    exit(1);
  }

  mreq.imr_multiaddr.s_addr=inet_addr(group);
  mreq.imr_interface.s_addr=htonl(INADDR_ANY);
  if (setsockopt(ss->fd, IPPROTO_IP, IP_ADD_MEMBERSHIP, &mreq, sizeof(mreq)) < 0) {
    perror("setsockopt");
    exit(1);
  }

  return ss; 
}

struct sock* udptx(char* group, int port) { 
  struct sock* ss = (struct sock*) malloc(sizeof(struct sock)) ; 
  ss->addrlen = sizeof(ss->addr) ; 

  if ((ss->fd=socket(AF_INET,SOCK_DGRAM,0)) < 0) {
    perror("socket");
    exit(1);
  }

  memset(&ss->addr, 0, ss->addrlen);
  ss->addr.sin_family=AF_INET;
  ss->addr.sin_addr.s_addr=inet_addr(group);
  ss->addr.sin_port=htons(port);

  return ss;
}

int sendx(struct sock* ss, char* buf, int len) { 
  int n = sendto(ss->fd, buf, len, 0, (struct sockaddr*) &ss->addr, ss->addrlen);
  if (n < 0) {
    perror("sendto");
    return -1 ;
  }
  return n ; 
}

int receive(struct sock* ss, char* buf, int len) { 
  int n = recvfrom(ss->fd, buf, len, 0, (struct sockaddr*) &ss->addr, &ss->addrlen) ; 
  if (n < 0) { 
    perror("recvfrom");
    return -1 ; 
  }
  return n ;
}

