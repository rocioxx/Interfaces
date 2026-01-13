package com.example.calculadora;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etDisplay;
    private double primerNumero = 0;
    private String operacionActual = "";
    private boolean nuevaOperacion = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establece el layout XML que diseñamos
        setContentView(R.layout.activity_main);

        // Inicializa la pantalla
        etDisplay = findViewById(R.id.et_display);
    }

    // Listener para botones numéricos y el punto
    public void insertNumber(View v) {
        Button b = (Button) v;
        String valor = b.getText().toString();

        if (nuevaOperacion) {
            etDisplay.setText(valor);
            nuevaOperacion = false;
        } else {
            if (etDisplay.getText().toString().equals("0")) {
                etDisplay.setText(valor);
            } else {
                etDisplay.append(valor);
            }
        }
    }

    // Listener para los botones de operación (+, -, ×, /)
    public void operate(View v) {
        Button b = (Button) v;
        primerNumero = Double.parseDouble(etDisplay.getText().toString());
        operacionActual = b.getText().toString();
        nuevaOperacion = true;
    }

    // Listener para el botón de borrar "C"
    public void clearScreen(View v) {
        etDisplay.setText("0");
        primerNumero = 0;
        operacionActual = "";
        nuevaOperacion = true;
    }

    // Listener para el botón de retroceso (borrar último carácter)
    public void deleteLast(View v) {
        String texto = etDisplay.getText().toString();
        if (texto.length() > 1) {
            etDisplay.setText(texto.substring(0, texto.length() - 1));
        } else {
            etDisplay.setText("0");
            nuevaOperacion = true;
        }
    }

    // Listener para el botón "="
    public void calculateResult(View v) {
        double segundoNumero = Double.parseDouble(etDisplay.getText().toString());
        double resultado = 0;

        switch (operacionActual) {
            case "+": resultado = primerNumero + segundoNumero; break;
            case "-": resultado = primerNumero - segundoNumero; break;
            case "×": resultado = primerNumero * segundoNumero; break;
            case "/":
                if (segundoNumero != 0) resultado = primerNumero / segundoNumero;
                break;
        }

        // Mostramos el resultado (eliminando el .0 si es entero)
        if (resultado % 1 == 0) {
            etDisplay.setText(String.valueOf((int) resultado));
        } else {
            etDisplay.setText(String.valueOf(resultado));
        }
        nuevaOperacion = true;
    }
}