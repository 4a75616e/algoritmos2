/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kakuro;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author Juan
 */
public class BT{
    static int forks_hilos=0;     //lleva la cuenta de cuantos se han hecho para asi no hacer mas 
    static boolean forks=false;
    static Grafico grafico=new Grafico();
    static int ix,iy;
    public static boolean solve(int i,int j,Tablero t){
        //System.out.println("gniblos "+i+" "+j);
        boolean a=false;
        //t=(Tablero)ThreadBT.deepClone(t);
        boolean condicion1,condicion2,condicion3,condicion4;
        casillaMatriz siguiente,abajo;
        if(j==iy && i==ix){
            return true;
        }
        if(j==14){
            j=0;
            i++;
        }
        if(i==14){
            System.out.println("<<Solucion>>");
            t.toString();
            long fin=System.nanoTime();
            System.out.println(grafico.getStartTime()/1000000);
            System.out.println(fin/1000000);
            System.out.println((fin-grafico.getStartTime())/1000000);
            
            /*
            synchronized(grafico){
                grafico.imprimir();
            }
            */
            
            return true;
        }
        casillaMatriz cm=t.getCasillaMatriz(i, j);
        
        if(cm.isNegra()){      //no se le tiene que poner un valor
            //System.out.println("Negra: "+i+" "+j);
            /*
            if(forks_hilos>0){
                if(forks){
                    //mientras no se tengan forks
                    solve(i,j+1,t);
                }
                else{
                    ThreadBT tbt=new ThreadBT(i,j+1,t);
                    forks_hilos--;
                    tbt.start();
                    //solve(i,j+1,t);
                }
            }
            else{
                solve(i,j+1,t);
            }
            */
            //t=(Tablero)ThreadBT.deepClone(t);
            //solve(i,j+1,t);
            //Tablero w=(Tablero)ThreadBT.deepClone(t);
            a=a||solve(i,j+1,t);
            
        }
        else if(cm.isTriangulo()){
            //System.out.println("Triangulo: "+i+" "+j);
            //solve(i,j+1,t);
            //Tablero w=(Tablero)ThreadBT.deepClone(t);
            a=a||solve(i,j+1,t);
        }
        else{
            abajo=t.getAbajo(i, j);
            siguiente=t.getSiguiente(i, j);
            for(int k=1;k<10;k++){
                //System.out.println("Numero: "+i+" "+j+" "+k);
                
                condicion1=!t.getSuma(i, j).isInAbajo(k);
                condicion2=!t.getSuma(i, j).isInDerecha(k);
                if(siguiente.isNumeral()){
                    condicion3=(t.getSuma(i, j).getObjetivoDerecha()>t.getSuma(i, j).getActualDerecha()+k);
                }
                else{
                    condicion3=((t.getSuma(i, j).getObjetivoDerecha()==t.getSuma(i, j).getActualDerecha()+k));
                }
                if(abajo.isNumeral()){
                    condicion4=(t.getSuma(i, j).getObjetivoAbajo()>t.getSuma(i, j).getActualAbajo()+k);
                }
                else{
                    condicion4=((t.getSuma(i, j).getObjetivoAbajo()==t.getSuma(i, j).getActualAbajo()+k));
                }
                //System.out.println("ID: "+t.getSuma(i, j).getDerecha()+" Numero: "+i+" "+j+" "+k+" Sumas: d/a "+t.getSuma(i, j).getObjetivoDerecha()+" "+t.getSuma(i, j).getObjetivoAbajo()+" "+t.getSuma(i, j).getActualDerecha()+" "+t.getSuma(i, j).getActualAbajo()+/*t.getSuma(i, j)+" "+info.getSumaHorizontal(i)+" "+k+*/"  Condiciones: "+condicion1+" "+condicion2+" "+condicion3+" "+condicion4);
                //si se cumplen las condiciones de poda
                if(condicion1&&condicion2&&condicion3&&condicion4){
                    synchronized(grafico){
                        grafico.addCasilla(i, j);
                    }
                    t.getSuma(i, j).sumarActualAbajo(k);
                    t.getSuma(i, j).sumarActualDerecha(k);
                    t.setCasillaMatriz(i, j, k);
                    //System.out.println("TABLERO: "+i+" "+j);
                    //t.toString();
                    //System.out.println(forks_hilos+" "+forks);
                   
                    a=a||solve(i,j+1,t);
                    
                    t.setCasillaMatriz(i, j, 0);
                    t.getSuma(i, j).restarActualAbajo(k);
                    t.getSuma(i, j).restarActualDerecha(k);
                }
                    
            }
        } 
        return a;
    }
    
    public static void setFH(int num){
        forks_hilos=num;
    }
    public static void setForks(boolean t){
        forks=t;
    }
    public static void setMaxXY(int x,int y){
        ix=x-1;
        iy=y-1;
    }
}

class ThreadBT extends Thread{
    private int i;
    private int j;
    private Tablero t;
    public ThreadBT(int i,int j,Tablero t){
        this.i=i;
        this.j=j;
        
        this.t=(Tablero)deepClone(t);
    }
    @Override
    public void run(){
        //System.out.println("Solving "+i+" "+j);
        BT.solve(i, j, t);
    }
    
    public static Object deepClone(Object object) {
        
        try {
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          ObjectOutputStream oos = new ObjectOutputStream(baos);
          oos.writeObject(object);
          ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
          ObjectInputStream ois = new ObjectInputStream(bais);
          return ois.readObject();
        }
        catch (Exception e) {
          e.printStackTrace();
          return object;
        }
        
        /*
        XStream x = new XStream(new StaxDriver());
        Object myClone = x.fromXML(x.toXML(object));
        return myClone;
        */
      }
    
    
}


