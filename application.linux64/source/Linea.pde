class Linea{
  ArrayList puntos;
  float grosor;
  color c;
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
      grosor = random(0.5, 2);
      alfa = (int)random(100, 255);
    }
    puntos.add(o);
    ox = nx = o.gx;
    oy = ny = o.gy;
    tipo = round(random(1,4));
    maximo = round(random(20, (gridX + gridY)/1));
    largo = 1;
    verb = "Nueva linea tipo "+tipo+" en ("+ox+", "+oy+") con un largo máximo de "+maximo;
  }

  void dibuja(){
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














