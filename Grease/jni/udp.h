#include <netinet/in.h>


struct sock { 
  struct sockaddr_in addr;
  int fd, addrlen;
} ; 

struct sock* udprx(char* group, int port) ;
struct sock *udptx(char* group, int port) ; 
int sendx(struct sock* ss, char* buf, int len) ; 
int receive(struct sock* ss, char* buf, int len) ;
