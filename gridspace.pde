/*
 *
 *    GridSpace - by @hspencer
 *    v1.0.0
 *
 */
 
import processing.pdf.*;

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
color fondo = color(250);
color grilla = color(50, 50);
color punto = color(0);

void setup(){
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

void draw(){
  todo();
  fill(255);
  //text(verb, 30, alto-50);
  mouse();
  smooth();
}





