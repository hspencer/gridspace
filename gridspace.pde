
import processing.pdf.*;

PFont font;
String verb = "";

ArrayList l;
PImage pal;
int ancho = 1440; // screen.width;
int alto  = 900;  // screen.height;
float margen = 100;
float gridSpacer = 10;
float cw = gridSpacer/3;
float tension = 0;
int gridX = round((float)(ancho - 2*margen)/gridSpacer);
int gridY = round((float)(alto - 2*margen)/gridSpacer);
Punto[][] p = new Punto[gridX][gridY];
boolean vuelveaGrilla = false;
boolean Repele = true;
int tp = 3;
int tg = 1;
boolean dibujar = true;
boolean pdf = false;
int pag = 0;


color fondo = color(250);
color grilla = color(50, 50);

void setup(){
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

void keyPressed(){

  if(key =='o'){
    vuelveaGrilla = !vuelveaGrilla;
    verb = "Vuelve a la grilla original = "+vuelveaGrilla;
  }
  if(key =='m'){
    Repele = !Repele;
    verb = "repele = "+Repele;
  }
  if(key =='g'){
    tg ++;
    tg %= 3;
    String v = "";
    if(tg == 0) v = "no";
    if(tg == 1) v = "cuadrada";
    if(tg == 2) v = "curva";
    verb = "dibuja grilla = "+v;
  }
  if(key =='p'){
    tp ++;
    tp %= 4;
    verb = "puntos "+tp;
  }
  if(key =='1'){
    for(int y = 0; y < gridY; y++){
      for(int x = 0; x < gridX; x++){
        p[x][y].xpos *= 0.9;
        p[x][y].ypos *= 0.9;
      }
    }
    verb = "achicar";
  }
  if(key =='2'){
    for(int y = 0; y < gridY; y++){
      for(int x = 0; x < gridX; x++){
        p[x][y].xpos *= 1.2;
        p[x][y].ypos *= 1.2;
      }
    }
    verb = "agrandar";
  }

  if(key =='S'){
    verb = "exportando...";
    String filename = "pdf/"+year()+"_"+month()+"_"+day()+"_"+hour()+"_"+minute()+"_"+second()+".pdf";
    beginRecord(PDF, filename);
    todo();
    endRecord();
    pag++;
    verb = "================== pag "+pag+" ==================";
  }

  if(key =='s'){
    saveFrame("img/########.png");
  }
  if(key =='z'){
    power += 0.1;
    verb = "power = "+power;  
  }
  if(key =='x'){
    power -= 0.1;
    verb = "power = "+power;  

  }
  if(key ==' '){
    Linea lin = new Linea();
    l.add(lin);
  }
  if(key =='l'){
    dibujar = !dibujar;
    if(dibujar) verb = "dibujando las líneas";
    if(!dibujar) verb = "las líneas no se dibujan";
  }
  if(key =='L'){
    l.clear(); 
  }
  if(key =='a'){
    adelgazar();
    verb = "adelgazar";
  }
  if(key =='e'){
    engrosar();
    verb = "engrosar";
  }
  if(key =='c'){
    simplificar();
    verb = "simplificar";
  }
  if(key =='v'){
    reColorear();
    verb = "recolorear";
  }
  if(key =='h'){
    reEngrosar();
    verb = "regenerar colores y grosores";
  }
  if(key =='b'){
    aclara();
    verb = "aclarar";
  }
  if(key =='k'){
    oscurece();
    verb = "opacar";
  }
  if(key ==','){
    tension -= 0.1;
    verb = "tensón de la curva = "+tension;
  }
  if(key =='.'){
    tension += 0.1;
    verb = "tensón de la curva = "+tension;
  }
  if(key =='q'){
    verb = "listo";
    exit();
  }
  if(key == CODED){
    if(keyCode == RIGHT){
      for(int y = 0; y < gridY; y++){
        for(int x = 0; x < gridX; x++){
          p[x][y].xpos += mouseX;
        }
      }
    }
    if(keyCode == LEFT){
      for(int y = 0; y < gridY; y++){
        for(int x = 0; x < gridX; x++){
          p[x][y].xpos -= mouseX;
        }
      }
    }
    if(keyCode == UP){
      for(int y = 0; y < gridY; y++){
        for(int x = 0; x < gridX; x++){
          p[x][y].ypos -= mouseX;
        }
      }
    }
    if(keyCode == DOWN){
      for(int y = 0; y < gridY; y++){
        for(int x = 0; x < gridX; x++){
          p[x][y].ypos += mouseX;
        }
      }
    }
  }
}





