package com.example.p08buscaminas;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends Activity implements OnTouchListener
{
	private final int NIVEL1 = 4;
	private final int NIVEL2 = 8;
	private final int NIVEL3 = 16;
	private final int BUM = 80;
	private final int HUECO = 40;
	
	private int TAMF = 8;
	private int TAMC = 8;
	private int nBombas;
	private Tablero fondo;
	private Casilla[][] casillas;
	private boolean activo = true;
	private int idExplosion, idColofon, idBeep;
	private SoundPool sp;
	private Paint pntpeque, pntgrande, pntlinea, pntbomba;
	private Button btnComenzar,btnSalir, btnNivel,btnHonor;
	private ArrayList<Ganador> lisGanadores = new ArrayList<Ganador>();

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		
		nBombas = NIVEL2;
		
		btnComenzar = (Button) findViewById(R.id.btnComenzar);
		btnSalir = (Button) findViewById(R.id.btnSalir);
		btnNivel = (Button) findViewById(R.id.btnNivel);
		btnHonor = (Button) findViewById(R.id.btnHonor);
		
		porDerecha(btnComenzar);
		aparecer(btnSalir);
		traslacion(btnNivel);
		porAbajo(btnHonor);
    	
		LinearLayout layout = (LinearLayout) findViewById(R.id.tablero);
		fondo = new Tablero(this);
		fondo.setOnTouchListener(this);				  
		layout.addView(fondo);
		casillas = new Casilla[TAMF][TAMC];
		for (int f = 0; f < TAMF; f++) 
			for (int c = 0; c < TAMC; c++) 
				casillas[f][c] = new Casilla();
		
    	pntpeque = new Paint();
    	pntgrande = new Paint();
    	pntlinea = new Paint();
    	pntbomba = new Paint();
		
		this.ponerBombas();
		this.contarBombasAlrededor();
		
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		sp = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
		AssetManager assetManager = getAssets(); 
		try 
		{
			AssetFileDescriptor explosion = assetManager.openFd("explosion.mp3"); 
			AssetFileDescriptor colofon = assetManager.openFd("colofon.mp3");
			AssetFileDescriptor beep = assetManager.openFd("toc.mp3");
			idExplosion = sp.load(explosion, 1); 
			idColofon = sp.load(colofon, 1);
			idBeep = sp.load(beep, 1);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void aparecer(Button btn)
	{
		Animation a = AnimationUtils.loadAnimation(this,R.anim.aparicion);
		btn.startAnimation(a);
	}
	
	public void porIzquierda(Button btn)
	{
		Animation a = AnimationUtils.loadAnimation(this,R.anim.izquierda);
		btn.startAnimation(a);
	}
	
	public void porDerecha(Button btn)
	{
		Animation a = AnimationUtils.loadAnimation(this,R.anim.derecha);
		btn.startAnimation(a);
	}
	
	public void porAbajo(Button btn)
	{
		Animation a = AnimationUtils.loadAnimation(this,R.anim.abajo);
		btn.startAnimation(a);
	}
	
	public void traslacion(Button btn)
	{
		Animation a = AnimationUtils.loadAnimation(this,R.anim.traslacion);
		btn.startAnimation(a);
	}	
	
	public void pulsaJugar(View v) 
	{
		Animation anim = AnimationUtils.loadAnimation(this,R.anim.rotacion);
		btnComenzar.startAnimation(anim);
		
		casillas = new Casilla[TAMF][TAMC];
		for (int f = 0; f < TAMF; f++) 
			for (int c = 0; c < TAMC; c++) 
				casillas[f][c] = new Casilla();
		
		this.ponerBombas();
		this.contarBombasAlrededor();
		activo = true;

		fondo.invalidate();
	}

	public void pulsaSalir(View v) 
	{
		Animation anim = AnimationUtils.loadAnimation(this,R.anim.rotazoom);
		btnSalir.startAnimation(anim);
		finish();
	}

	public void pulsaNivel(View v) 
	{
		Animation anim = AnimationUtils.loadAnimation(this,R.anim.rotazoom);
		btnNivel.startAnimation(anim);
		Intent intNivel = new Intent(MainActivity.this, NivelDif.class);
		startActivityForResult(intNivel,11);
	}
	
	public void pulsaHonor(View v) 
	{
		Animation anim = AnimationUtils.loadAnimation(this,R.anim.rotazoom);
		btnHonor.startAnimation(anim);
		System.out.println("---1---");
		Intent intHonor = new Intent(MainActivity.this, Honor.class);
		startActivity(intHonor);
	}
	
	public int calcularBombas(int nivel)
	{
		int bombas = 0;
		switch (nivel)
		{
			case 1: bombas = NIVEL1;
					break;
			case 2: bombas = NIVEL2;
					break;
			case 3: bombas = NIVEL3;
					break;	
		}
		return bombas;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 11)
			if(resultCode == RESULT_OK)
			{
				nBombas = calcularBombas(data.getExtras().getInt("NIVEL"));
			}
		if(requestCode == 21)
			if(resultCode == RESULT_OK)
			{
				String nombre = data.getExtras().getCharSequence("NOMBRE").toString();
				String apellido = data.getExtras().getCharSequence("APELLIDO").toString();
				Ganador g = new Ganador(nombre, apellido);
				lisGanadores.add(g);
				Ganador.setLista(lisGanadores);
			}
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) 
	{
		if (activo)
			for (int f = 0; f < TAMF; f++) 
				for (int c = 0; c < TAMC; c++) 
					if (casillas[f][c].dentro((int) event.getX(),(int) event.getY())) 
					{
						casillas[f][c].destapado = true;
						if (casillas[f][c].contenido == BUM) 
						{
							sp.play(idExplosion, 1, 1, 0, 0, 1);
							Toast.makeText(this, "Booooooooom",Toast.LENGTH_LONG).show();
							activo = false;
						} 
						else 
							if (casillas[f][c].contenido == 0)
								recorrer(f, c);
							else
								sp.play(idBeep, 1, 1, 0, 0, 1);
						fondo.invalidate();
					}
			
		if (gano() && activo) 
		{
			sp.play(idColofon, 1, 1, 0, 0, 1);
			Toast.makeText(this, "Ganaste", Toast.LENGTH_LONG).show();
			activo = false;
			Intent intIncluir = new Intent(MainActivity.this, Incluir.class);
			startActivityForResult(intIncluir, 21);
		}

		return true;
	}

	private void ponerBombas() 
	{
		int cantidad = nBombas;
		do 
		{
			int fila = (int) (Math.random() * TAMF);
			int columna = (int) (Math.random() * TAMC);
			if (casillas[fila][columna].contenido == 0) 
			{
				casillas[fila][columna].contenido = BUM;
				cantidad--;
			}
		} while (cantidad != 0);
	}

	private boolean gano() 
	{
		int cant = 0;
		for (int f = 0; f < TAMF; f++)
			for (int c = 0; c < TAMC; c++)
				if (casillas[f][c].destapado)
					cant++;
		if (cant == 64-nBombas)
			return true;
		else
			return false;
	}

	private void contarBombasAlrededor() 
	{
		for (int f = 0; f < TAMF; f++) 
			for (int c = 0; c < TAMC; c++) 
				if (casillas[f][c].contenido == 0) 
				{
					int cant = contarCoordenada(f, c);
					casillas[f][c].contenido = cant;
				}
	}

	int contarCoordenada(int fila, int columna) 
	{
		int total = 0;
		if (fila - 1 >= 0 && columna - 1 >= 0) 
			if (casillas[fila - 1][columna - 1].contenido == BUM)
				total++;
		
		if (fila - 1 >= 0) 
			if (casillas[fila - 1][columna].contenido == BUM)
				total++;
		
		if (fila - 1 >= 0 && columna + 1 < TAMF) 
			if (casillas[fila - 1][columna + 1].contenido == BUM)
				total++;

		if (columna + 1 < TAMC) 
			if (casillas[fila][columna + 1].contenido == BUM)
				total++;
		
		if (fila + 1 < TAMF && columna + 1 < TAMC) 
			if (casillas[fila + 1][columna + 1].contenido == BUM)
				total++;

		if (fila + 1 < TAMF) 
			if (casillas[fila + 1][columna].contenido == BUM)
				total++;
		
		if (fila + 1 < TAMF && columna - 1 >= 0) 
			if (casillas[fila + 1][columna - 1].contenido == BUM)
				total++;
		
		if (columna - 1 >= 0) 
			if (casillas[fila][columna - 1].contenido == BUM)
				total++;
		
		return total;
	}

	private void recorrer(int fil, int col) 
	{
		if (fil >= 0 && fil < TAMF && col >= 0 && col < TAMC) 
			if (casillas[fil][col].contenido == 0) 
			{
				casillas[fil][col].destapado = true;
				casillas[fil][col].contenido = HUECO;
				recorrer(fil, col + 1);
				recorrer(fil, col - 1);
				recorrer(fil + 1, col);
				recorrer(fil - 1, col);
				recorrer(fil - 1, col - 1);
				recorrer(fil - 1, col + 1);
				recorrer(fil + 1, col + 1);
				recorrer(fil + 1, col - 1);
			} 
			else 
				if (casillas[fil][col].contenido >= 1 && casillas[fil][col].contenido <= 8) 
					casillas[fil][col].destapado = true;	
	}
	
	class Tablero extends View 
	{
		public Tablero(Context context) 
		{
			super(context);
		}

		protected void onDraw(Canvas canvas)
		{
			
			canvas.drawRGB(0, 0, 0);
			int ancho = 0;
			if (canvas.getWidth() < canvas.getHeight())
				ancho = fondo.getWidth();
			else
				ancho = fondo.getHeight();
			int anchocuadro = ancho / TAMF;
			int TAMFUENTE = anchocuadro/2;
			int RADIO = anchocuadro/4;
			
			pntpeque.setTextSize(TAMFUENTE);
			pntgrande.setTextSize(TAMFUENTE);
			pntgrande.setTypeface(Typeface.DEFAULT_BOLD);
			pntgrande.setARGB(255, 0, 0, 255);

			pntlinea.setARGB(255, 255, 255, 255);
			int filaActual = 0;
			for (int f = 0; f < TAMF; f++) 
			{
				for (int c = 0; c < TAMC; c++) 
				{
					casillas[f][c].fijarxy(c * anchocuadro, filaActual, anchocuadro);
					
					if (casillas[f][c].destapado == false)
						pntpeque.setARGB(153, 204, 204, 204);
					else
						pntpeque.setARGB(255, 153, 153, 153);
					
					canvas.drawRect(c * anchocuadro, filaActual, c * anchocuadro + anchocuadro - 2, filaActual + anchocuadro - 2, pntpeque);
					canvas.drawLine(c * anchocuadro, filaActual, c * anchocuadro + anchocuadro, filaActual, pntlinea);
					canvas.drawLine(c * anchocuadro + anchocuadro - 1, filaActual, c * anchocuadro + anchocuadro - 1, filaActual + anchocuadro, pntlinea);

					if (casillas[f][c].contenido >= 1 && casillas[f][c].contenido <= 8 && casillas[f][c].destapado)
						canvas.drawText(String.valueOf(casillas[f][c].contenido), c * anchocuadro + (anchocuadro / 2) - TAMFUENTE/4,filaActual + (anchocuadro / 2)+TAMFUENTE/4, pntgrande);

					if (casillas[f][c].contenido == BUM && casillas[f][c].destapado) 
					{
						pntbomba.setARGB(255, 255, 0, 0);
						canvas.drawCircle(c * anchocuadro + (anchocuadro / 2),filaActual + (anchocuadro / 2), RADIO, pntbomba);
					}
				}
				filaActual = filaActual + anchocuadro;
			}
		}
		
	}

}
