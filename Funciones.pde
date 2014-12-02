
float ep  = 200.0;             // escala de punto - NOISE
float em = 4000.0;             // escala de millis - NOISE
float amp = 5.0;               // amplificación del ruido - NOISE
float ramp = gridSpacer/10;    // amplificación del random
float velGrilla = 0.03;        // velocidad de retorno al origen - NOISE
float power = 2.5;

public void todo(){
  background(0);
  if(mousePressed){
    deformaGrilla(Repele);
  }
  if(keyPressed && key =='n') distorsionaGrilla("NOISE");
  if(keyPressed && key =='r') distorsionaGrilla("RANDOM");
  if(vuelveaGrilla) origenGrilla();

  dibujaGrilla();
  dibujaPuntos();
  
  if(dibujar) dibuja();
  //if(l.size() > 0 && l.size() < 40) hace();
}

void createGrid(){
  for(int y = 0; y < gridY; y++){
    for(int x = 0; x < gridX; x++){
      p[x][y] = new Punto(margen + (x * gridSpacer), margen + (y * gridSpacer));
      p[x][y].gx = x;
      p[x][y].gy = y;
    }
  }
}

void dibujaPuntos(){
  for(int y = 0; y < gridY; y++){
    for(int x = 0; x < gridX; x++){
      p[x][y].dibuja();
    } 
  } 
}

void dibujaGrilla(){
  stroke(255, 100);
  strokeWeight(1);
  noFill();

  switch(tg){

    case(0):
    break;

    case(1):
    for(int y = 0; y < gridY; y++){
      beginShape();
      for(int x = 0; x < gridX; x++){
        if(x != int(gridX /2)) vertex(p[x][y].xpos, p[x][y].ypos);
      } 
      endShape();
    } 
    for(int x = 0; x < gridX; x++){
      beginShape();
      for(int y = 0; y < gridY; y++){
        vertex(p[x][y].xpos, p[x][y].ypos);
      } 
      endShape();
    }
    break;
    case(2):
    curveTightness(tension);
    for(int y = 0; y < gridY; y++){
      beginShape();
      for(int x = 0; x < gridX; x++){
        if(x != int(gridX /2)) curveVertex(p[x][y].xpos, p[x][y].ypos);
      } 
      endShape();
    } 
    for(int x = 0; x < gridX; x++){
      beginShape();
      for(int y = 0; y < gridY; y++){
        curveVertex(p[x][y].xpos, p[x][y].ypos);
      } 
      endShape();
    } 
    break;
  }
}

void dibuja(){
  if(l.size() > 0){
    for(int i = 0; i < l.size(); i++){
      Linea lin = (Linea) l.get(i);
      lin.dibuja();
    }
  }
}

void distorsionaGrilla(String mode){
  if(mode == "RANDOM"){
    for(int y = 0; y < gridY; y++){
      for(int x = 0; x < gridX; x++){
        p[x][y].xpos += random (-ramp, ramp);
        p[x][y].ypos += random (-ramp, ramp);
      } 
    } 
  }
  if(mode == "NOISE"){
    for(int y = 0; y < gridY; y++){
      for(int x = 0; x < gridX; x++){

        float noiseX = (noise(p[x][y].xpos/ep, (p[x][y].ypos/ep) + millis()/em) - 0.5) * amp;
        float noiseY = (noise((p[x][y].xpos/ep) - millis()/em, p[x][y].ypos/ep) - 0.5) * amp;
        p[x][y].ypos += noiseY;
        p[x][y].xpos += noiseX;

      } 
    } 
  }
}

void deformaGrilla(boolean repele){
  float ang, d, X, Y, r;
  float fuerza = 10000;
  strokeWeight(10);
  stroke(255,0,0,100);
  point(mouseX, mouseY);
  for(int y = 0; y < gridY; y++){
    for(int x = 0; x < gridX; x++){
      X = p[x][y].xpos;
      Y = p[x][y].ypos;
      pushMatrix();
      translate(X, Y);
      ang = atan2(mouseX - X, mouseY - Y) *-1 + HALF_PI;
      popMatrix();
      if(repele) ang += PI;
      d = dist(p[x][y].xpos, p[x][y].ypos, mouseX, mouseY);
      //d = constrain(d, 0, 1000);
      r = fuerza / (pow(d, power));

      if((r > d) && !repele){
        float difx = mouseX - p[x][y].xpos;
        float dify = mouseY - p[x][y].ypos;
        p[x][y].xpos += difx * 0.33;
        p[x][y].ypos += dify * 0.33;
      }
      else {
        r = constrain(r, 0, d);
        p[x][y].xpos += cos(ang) * r;
        p[x][y].ypos += sin(ang) * r;
      }
    }
  } 
}

void origenGrilla(){
  float difx, dify;
  for(int y = 0; y < gridY; y++){
    for(int x = 0; x < gridX; x++){
      difx = p[x][y].ox - p[x][y].xpos;
      dify = p[x][y].oy - p[x][y].ypos;
      p[x][y].xpos += difx * velGrilla;
      p[x][y].ypos += dify * velGrilla;
    } 
  } 
}



void hace(){
  Linea lin = (Linea) l.get(l.size()-1);
  if(lin.crece == false){
    Linea lin2 = new Linea();
    l.add(lin2);
  }
}

color randomColor(){
  int paleta = pal.pixels.length;
  int index = (int)random(paleta);
  return pal.pixels[index];
}

void adelgazar(){
  if(l.size() > 0){
    for(int i = 0; i < l.size(); i++){
      Linea lin = (Linea) l.get(i);
      lin.grosor *= 0.9;
    }
  }
}

void aclara(){
  if(l.size() > 0){
    for(int i = 0; i < l.size(); i++){
      Linea lin = (Linea) l.get(i);
      lin.alfa = round((float)lin.alfa * 0.9);
    }
  }
}

void oscurece(){
  if(l.size() > 0){
    for(int i = 0; i < l.size(); i++){
      Linea lin = (Linea) l.get(i);
      lin.alfa = round((float)lin.alfa * 1.1);
      lin.alfa = constrain(lin.alfa, 0, 255);
    }
  }
}

void engrosar(){
  if(l.size() > 0){
    for(int i = 0; i < l.size(); i++){
      Linea lin = (Linea) l.get(i);
      lin.grosor *= 1.1;
    }
  }
}

void simplificar(){
  if(l.size() > 0){
    for(int i = 0; i < l.size(); i++){
      Linea lin = (Linea) l.get(i);
      lin.grosor = 1;
      lin.c = color(255);
      lin.alfa = 225;
      lin.grosor = 0.25;
    }
  }
}

void reColorear(){
  if(l.size() > 0){
    for(int i = 0; i < l.size(); i++){
      Linea lin = (Linea) l.get(i);
      lin.c = randomColor();
      lin.alfa = round(random(0,222));
    }
  }
}

void reEngrosar(){
  if(l.size() > 0){
    for(int i = 0; i < l.size(); i++){
      Linea lin = (Linea) l.get(i);

      if(i % 3 == 0){
        lin.grosor = random(10, 30);
        lin.alfa = (int)random(5, 40);
      }
      else{
        lin.grosor = random(0.5, 3);
        lin.alfa = (int)random(100, 255);
      }    
    }
  }
}


void mouse(){
 float diam = map(power, -1, 4, 200, 1);
 noStroke();
 fill(255, 0, 0, diam);
 ellipse(mouseX, mouseY, diam, diam);
}


