package com.example.p08buscaminas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

public class NivelDif extends Activity
{
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.niveles);
		NumberPicker npNivel = (NumberPicker)findViewById(R.id.npNivel);
		String[] n = new String[3];
		for(int i=0; i<n.length; i++)
			n[i] = Integer.toString(i+1);
		npNivel.setMinValue(1);
		npNivel.setMaxValue(3);
		npNivel.setWrapSelectorWheel(false);
		npNivel.setDisplayedValues(n);
		npNivel.setValue(2);
	}
		
	public void pulsaAceptar(View v) 
	{
		Intent nivel = new Intent();
		NumberPicker npNivel = (NumberPicker)findViewById(R.id.npNivel);
		if(v instanceof Button)
			nivel.putExtra("NIVEL", npNivel.getValue());
		
		setResult(RESULT_OK,nivel);
		finish();
	}
	
	public void pulsaCancelar(View v)
	{
		finish();
	}

}
