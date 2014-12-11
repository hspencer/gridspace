import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.pdf.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class gridspace extends PApplet {

/*
 *
 *    GridSpace - by @hspencer
 *
 */
 


PFont font;
String verb = "";

ArrayList l;
PImage pal;
int ancho, alto;

float margen = 100;
float gridSpacer = 10;
float cw = gridSpacer/3;
float tension = 0;
int gridX, gridY;
Punto[][] p;
boolean vuelveaGrilla = false;
boolean Repele = true;
int tp = 3;
int tg = 1;

boolean dibujar = true;
boolean pdf = false;
boolean fondoClaro = true;

int pag = 0;

// Colores
int fondo = color(250);
int grilla = color(50, 50);
int punto = color(0);

public void setup(){
  ancho = displayWidth;
  alto = displayHeight;
  gridX = round((float)(ancho - 2*margen)/gridSpacer);
  gridY = round((float)(alto - 2*margen)/gridSpacer);
  p = new Punto[gridX][gridY];
  size(ancho, alto);  
  createGrid();
  //smooth();
  noCursor();
  l = new ArrayList();
  pal = loadImage("pal.png");
  strokeCap(SQUARE);
  font = createFont("Lucida Grande", 11);
  textFont(font, 11);
}

public void draw(){
  todo();
  fill(255);
  //text(verb, 30, alto-50);
  mouse();
  smooth();
}






float ep  = 200.0f;             // escala de punto - NOISE
float em = 4000.0f;             // escala de millis - NOISE
float amp = 5.0f;               // amplificaci\u00f3n del ruido - NOISE
float ramp = gridSpacer/10;    // amplificaci\u00f3n del random
float velGrilla = 0.03f;        // velocidad de retorno al origen - NOISE
float power = 2.5f;

public void todo(){
  background(fondo);
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

public void createGrid(){
  for(int y = 0; y < gridY; y++){
    for(int x = 0; x < gridX; x++){
      p[x][y] = new Punto(margen + (x * gridSpacer), margen + (y * gridSpacer));
      p[x][y].gx = x;
      p[x][y].gy = y;
    }
  }
}

public void dibujaPuntos(){
  for(int y = 0; y < gridY; y++){
    for(int x = 0; x < gridX; x++){
      p[x][y].dibuja();
    } 
  } 
}

public void dibujaGrilla(){
  stroke(grilla);
  strokeWeight(1);
  noFill();

  switch(tg){

    case(0):
    break;

    case(1):
    for(int y = 0; y < gridY; y++){
      beginShape();
      for(int x = 0; x < gridX; x++){
        if(x != PApplet.parseInt(gridX /2)) vertex(p[x][y].xpos, p[x][y].ypos);
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
        if(x != PApplet.parseInt(gridX /2)) curveVertex(p[x][y].xpos, p[x][y].ypos);
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

public void dibuja(){
  if(l.size() > 0){
    for(int i = 0; i < l.size(); i++){
      Linea lin = (Linea) l.get(i);
      lin.dibuja();
    }
  }
}

public void distorsionaGrilla(String mode){
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

        float noiseX = (noise(p[x][y].xpos/ep, (p[x][y].ypos/ep) + millis()/em) - 0.5f) * amp;
        float noiseY = (noise((p[x][y].xpos/ep) - millis()/em, p[x][y].ypos/ep) - 0.5f) * amp;
        p[x][y].ypos += noiseY;
        p[x][y].xpos += noiseX;

      } 
    } 
  }
}

public void deformaGrilla(boolean repele){
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
        p[x][y].xpos += difx * 0.33f;
        p[x][y].ypos += dify * 0.33f;
      }
      else {
        r = constrain(r, 0, d);
        p[x][y].xpos += cos(ang) * r;
        p[x][y].ypos += sin(ang) * r;
      }
    }
  } 
}

public void origenGrilla(){
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



public void hace(){
  Linea lin = (Linea) l.get(l.size()-1);
  if(lin.crece == false){
    Linea lin2 = new Linea();
    l.add(lin2);
  }
}

public int randomColor(){
  int paleta = pal.pixels.length;
  int index = (int)random(paleta);
  return pal.pixels[index];
}

public void adelgazar(){
  if(l.size() > 0){
    for(int i = 0; i < l.size(); i++){
      Linea lin = (Linea) l.get(i);
      lin.grosor *= 0.9f;
    }
  }
}

public void aclara(){
  if(l.size() > 0){
    for(int i = 0; i < l.size(); i++){
      Linea lin = (Linea) l.get(i);
      lin.alfa = round((float)lin.alfa * 0.9f);
    }
  }
}

public void oscurece(){
  if(l.size() > 0){
    for(int i = 0; i < l.size(); i++){
      Linea lin = (Linea) l.get(i);
      lin.alfa = round((float)lin.alfa * 1.1f);
      lin.alfa = constrain(lin.alfa, 0, 255);
    }
  }
}

public void engrosar(){
  if(l.size() > 0){
    for(int i = 0; i < l.size(); i++){
      Linea lin = (Linea) l.get(i);
      lin.grosor *= 1.1f;
    }
  }
}

public void simplificar(){
  if(l.size() > 0){
    for(int i = 0; i < l.size(); i++){
      Linea lin = (Linea) l.get(i);
      lin.grosor = 1;
      lin.c = color(50);
      lin.alfa = 225;
      lin.grosor = 0.25f;
    }
  }
}

public void reColorear(){
  if(l.size() > 0){
    for(int i = 0; i < l.size(); i++){
      Linea lin = (Linea) l.get(i);
      lin.c = randomColor();
      lin.alfa = round(random(0,222));
    }
  }
}

public void reEngrosar(){
  if(l.size() > 0){
    for(int i = 0; i < l.size(); i++){
      Linea lin = (Linea) l.get(i);

      if(i % 3 == 0){
        lin.grosor = random(10, 30);
        lin.alfa = (int)random(5, 40);
      }
      else{
        lin.grosor = random(0.5f, 3);
        lin.alfa = (int)random(100, 255);
      }    
    }
  }
}


public void mouse(){
 float diam = map(power, -1, 4, 200, 1);
 noStroke();
 fill(255, 0, 0, diam);
 ellipse(mouseX, mouseY, diam, diam);
}


class Linea{
  ArrayList puntos;
  float grosor;
  int c;
  int ox, oy, nx, ny; // punto de origen en la grilla y el siguiente
  boolean crece = true;
  int tipo;   // 1-arriba, 2-derecha, 3-abajo, 4-izquierda
  int largo, maximo;
  boolean mala = false;
  int alfa;

  Linea(){
    puntos = new ArrayList();
    c = randomColor();
    Punto o = p[round(random(gridX-1))][round(random(gridY-1))];
    if(!o.libre){
      crece = false;
      mala = true;
    }
    if(millis() % 3 == 0){
      grosor = random(10, 20);
      alfa = (int)random(5, 40);
    }
    else{
      grosor = random(0.5f, 2);
      alfa = (int)random(100, 255);
    }
    puntos.add(o);
    ox = nx = o.gx;
    oy = ny = o.gy;
    tipo = round(random(1,4));
    maximo = round(random(20, (gridX + gridY)/1));
    largo = 1;
    verb = "Nueva linea tipo "+tipo+" en ("+ox+", "+oy+") con un largo m\u00e1ximo de "+maximo;
  }

  public void dibuja(){
    if(!mala){
      if(crece){

        switch(tipo){
        case 1: // arriba
          if(ny > 0 && largo < maximo){
            ny--;
            largo++;
          } 
          else{
            crece = false;
          }
          break;
        case 2: // derecha
          if(nx < gridX && largo <= maximo){
            nx++;
            largo++;
          } 
          else{
            crece = false;
          }
          break;
        case 3: // abajo
          if(ny < gridY && largo <= maximo){
            ny++;
            largo++;
          } 
          else{
            crece = false;
          }
          break;
        case 4: // izquierda
          if(nx > 0 && largo <= maximo){
            nx--;
            largo++;
          } 
          else{
            crece = false;
          }
          break;
        }
        nx = constrain(nx, 0, gridX-1);
        ny = constrain(ny, 0, gridY-1);

        if(!p[nx][ny].libre){
          //p[nx][ny].cruce = true;
          crece = false;
        }
        puntos.add(p[nx][ny]);
        p[nx][ny].libre = false;
      }
      curveTightness(tension);
      strokeWeight(grosor);
      if(puntos.size() >= 2){
        noFill();
        stroke(c, alfa);
        strokeWeight(grosor);
        beginShape();
        Punto pt;
        pt = (Punto) puntos.get(0);
        curveVertex(pt.xpos, pt.ypos);
        for(int i = 0; i < puntos.size(); i++){
          pt = (Punto) puntos.get(i);
          curveVertex(pt.xpos, pt.ypos);
        }
        pt = (Punto) puntos.get(puntos.size()-1);
        curveVertex(pt.xpos, pt.ypos);
        endShape();
      }
    }
  }
}














class Punto{
  float xpos, ypos, ox, oy;
  int gx, gy; // numero en la grilla
  float g = 1;
  float rot = random(0, TWO_PI);
  boolean libre = true;
  boolean cruce = false;
  boolean pop = false;
  float inc = random(0.001f, 0.12f);
  float largo = map(inc, 0.001f, 0.12f, 20, 3);

  Punto(){
    ox = xpos = random(width);
    oy = ypos = random(height);
  }

  Punto(float x, float y){
    ox = xpos = x;
    oy = ypos = y;
  }

  public void dibuja(){
    switch(tp){
    case 0:
      fill(punto);
      noStroke();
      ellipse(xpos, ypos, 1.5f, 1.5f); 
      break;

    case 1:
      stroke(punto);
      strokeWeight(0.5f);
      noFill();
      ellipse(xpos, ypos, cw*inc*100, cw*inc*100);
      break;

    case 2:
      pushMatrix();
      translate(xpos, ypos);
      rotate(rot);
      stroke(punto);
      rot += inc;
      strokeWeight(0.5f);
      line(-largo, 0, largo, 0);
      line(0, -largo, 0, largo);
      popMatrix();
      break;

    case 3:
      break;
    }
  }
}









public void keyPressed() {

  if (key =='o') {
    vuelveaGrilla = !vuelveaGrilla;
    verb = "Vuelve a la grilla original = "+vuelveaGrilla;
  }
  if (key =='m') {
    Repele = !Repele;
    verb = "repele = "+Repele;
  }
  if (key =='g') {
    tg ++;
    tg %= 3;
    String v = "";
    if (tg == 0) v = "no";
    if (tg == 1) v = "cuadrada";
    if (tg == 2) v = "curva";
    verb = "dibuja grilla = "+v;
  }
  if (key =='p') {
    tp ++;
    tp %= 4;
    verb = "puntos "+tp;
  }
  if (key =='1') {
    for (int y = 0; y < gridY; y++) {
      for (int x = 0; x < gridX; x++) {
        p[x][y].xpos *= 0.9f;
        p[x][y].ypos *= 0.9f;
      }
    }
    verb = "achicar";
  }
  if (key =='2') {
    for (int y = 0; y < gridY; y++) {
      for (int x = 0; x < gridX; x++) {
        p[x][y].xpos *= 1.2f;
        p[x][y].ypos *= 1.2f;
      }
    }
    verb = "agrandar";
  }

  if (key =='S') {
    verb = "exportando...";
    String filename = "pdf/"+year()+"_"+month()+"_"+day()+"_"+hour()+"_"+minute()+"_"+second()+".pdf";
    beginRecord(PDF, filename);
    todo();
    endRecord();
    pag++;
    verb = "================== pag "+pag+" ==================";
  }

  if (key =='s') {
    saveFrame("img/########.png");
  }

  if (key =='z') {
    power += 0.1f;
    verb = "power = "+power;
  }

  if (key =='x') {
    power -= 0.1f;
    verb = "power = "+power;
  }

  if (key ==' ') {
    Linea lin = new Linea();
    l.add(lin);
  }

  if (key =='l') {
    dibujar = !dibujar;
    if (dibujar) verb = "dibujando las l\u00edneas";
    if (!dibujar) verb = "las l\u00edneas no se dibujan";
  }

  if (key =='L') {
    l.clear();
  }

  if (key =='a') {
    adelgazar();
    verb = "adelgazar";
  }

  if (key =='e') {
    engrosar();
    verb = "engrosar";
  }

  if (key =='c') {
    simplificar();
    verb = "simplificar";
  }

  if (key == 'C') {
    fondoClaro = !fondoClaro;
    if (fondoClaro) {
      fondo = color(250);
      grilla = color(50, 50);
      punto = color(0);
    } else {
      grilla = color(250, 50);
      fondo = color(0);
      punto = color(255);
    }
  }

  if (key =='v') {
    reColorear();
    verb = "recolorear";
  }

  if (key =='h') {
    reEngrosar();
    verb = "regenerar colores y grosores";
  }

  if (key =='b') {
    aclara();
    verb = "aclarar";
  }

  if (key =='k') {
    oscurece();
    verb = "opacar";
  }

  if (key ==',') {
    tension -= 0.1f;
    verb = "tens\u00f3n de la curva = "+tension;
  }

  if (key =='.') {
    tension += 0.1f;
    verb = "tens\u00f3n de la curva = "+tension;
  }

  if (key =='q') {
    verb = "listo";
    exit();
  }

  if (key == CODED) {
    if (keyCode == RIGHT) {
      for (int y = 0; y < gridY; y++) {
        for (int x = 0; x < gridX; x++) {
          p[x][y].xpos += mouseX;
        }
      }
    }
    if (keyCode == LEFT) {
      for (int y = 0; y < gridY; y++) {
        for (int x = 0; x < gridX; x++) {
          p[x][y].xpos -= mouseX;
        }
      }
    }
    if (keyCode == UP) {
      for (int y = 0; y < gridY; y++) {
        for (int x = 0; x < gridX; x++) {
          p[x][y].ypos -= mouseX;
        }
      }
    }
    if (keyCode == DOWN) {
      for (int y = 0; y < gridY; y++) {
        for (int x = 0; x < gridX; x++) {
          p[x][y].ypos += mouseX;
        }
      }
    }
  }
}

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--full-screen", "--bgcolor=#666666", "--hide-stop", "gridspace" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
