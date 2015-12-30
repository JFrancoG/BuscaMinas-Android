package com.example.p08buscaminas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;

public class Incluir extends Activity
{
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.incluir);
	}

	public void pulsaVolver(View v)
	{
		Intent datos = new Intent();
		String nombre = ((EditText)findViewById(R.id.etNombre)).getText().toString();
		String apellido = ((EditText)findViewById(R.id.etApellido)).getText().toString();
				
		if(v instanceof Button)
		{
			datos.putExtra("NOMBRE",nombre);
			datos.putExtra("APELLIDO",apellido);
		}

		setResult(RESULT_OK,datos);
		finish();
	}
	
}