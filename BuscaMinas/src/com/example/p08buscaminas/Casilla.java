package com.example.p08buscaminas;

public class Casilla 
{
    public int cx,cy,anchura;
    public int contenido = 0;
    public boolean destapado = false;
    
    public void fijarxy(int cx,int cy, int anchura) 
    {
        this.cx = cx;
        this.cy = cy;
        this.anchura = anchura;
    }
    
    public boolean dentro(int coordx,int coordy) 
    {
        if (coordx >= this.cx && coordx <= this.cx + anchura && coordy >= this.cy && coordy <= this.cy + anchura) 
            return true;
        else
            return false;
    }
}