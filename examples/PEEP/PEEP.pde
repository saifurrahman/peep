import PEEP.*;

PEEP peep;

void setup() {
  size(600,400);
  
  peep = new PEEP(this);
   
}

void draw() {
  background(0);
  
  ellipse(peep.raw().x,peep.raw().y, 10, 10);  
}