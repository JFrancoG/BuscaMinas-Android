package com.example.p08buscaminas;

import java.util.ArrayList;

public class Ganador 
{
	private String apellido;
	private String nombre;

	private static ArrayList<Ganador> lista = null;
	
	public Ganador(String nombre, String apellido) 
	{
		this.apellido = apellido;
		this.nombre = nombre;
	}
	
	public String getNombre() 
	{
		return nombre;
	}
	
	public String getApellido() 
	{
		return apellido;
	}
	
	public static void setLista(ArrayList<Ganador> lis) 
	{
	    lista = lis;
	}
	   
	public static ArrayList<Ganador> getLista() 
	{
	    return lista;
	}
}