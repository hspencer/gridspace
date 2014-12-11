class Punto{
  float xpos, ypos, ox, oy;
  int gx, gy; // numero en la grilla
  float g = 1;
  float rot = random(0, TWO_PI);
  boolean libre = true;
  boolean cruce = false;
  boolean pop = false;
  float inc = random(0.001, 0.12);
  float largo = map(inc, 0.001, 0.12, 20, 3);

  Punto(){
    ox = xpos = random(width);
    oy = ypos = random(height);
  }

  Punto(float x, float y){
    ox = xpos = x;
    oy = ypos = y;
  }

  void dibuja(){
    switch(tp){
    case 0:
      fill(punto);
      noStroke();
      ellipse(xpos, ypos, 1.5, 1.5); 
      break;

    case 1:
      stroke(punto);
      strokeWeight(0.5);
      noFill();
      ellipse(xpos, ypos, cw*inc*100, cw*inc*100);
      break;

    case 2:
      pushMatrix();
      translate(xpos, ypos);
      rotate(rot);
      stroke(punto);
      rot += inc;
      strokeWeight(0.5);
      line(-largo, 0, largo, 0);
      line(0, -largo, 0, largo);
      popMatrix();
      break;

    case 3:
      break;
    }
  }
}









