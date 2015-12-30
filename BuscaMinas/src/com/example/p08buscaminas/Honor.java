package com.example.p08buscaminas;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class Honor extends Activity
{
	private ArrayList<String> lista = new ArrayList<String>();
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cuadrodehonor);

		for(int i=0; i<Ganador.getLista().size(); i++)
			lista.add(Ganador.getLista().get(i).getNombre()+" "+Ganador.getLista().get(i).getApellido());

		ListView lvGanadores = (ListView)findViewById(R.id.lvGanadores);  

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, lista);
		lvGanadores.setAdapter(adapter);
	}
}