#include <stdio.h> 

struct s { 
  int i;
  int j; 
}; 

int main() {

  printf("%ld\n", sizeof(struct s)) ; 

  return 0;

}
