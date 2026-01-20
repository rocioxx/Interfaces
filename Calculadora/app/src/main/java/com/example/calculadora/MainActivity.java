package com.example.calculadora;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etDisplay;
    private TextView tvHistory;
    private double primerNumero = 0;
    private double memoria = 0;
    private String operacionActual = "";
    private boolean nuevaOperacion = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etDisplay = findViewById(R.id.et_display);
        tvHistory = findViewById(R.id.tv_history);
        etDisplay.setShowSoftInputOnFocus(false);
    }

    public void insertNumber(View v) {
        Button b = (Button) v;
        String valor = b.getText().toString();

        if (nuevaOperacion) {
            etDisplay.setText(valor.equals(".") ? "0." : valor);
            nuevaOperacion = false;
        } else {
            String actual = etDisplay.getText().toString();
            if (valor.equals(".") && actual.contains(".")) return;
            etDisplay.append(valor);
        }
    }

    public void operate(View v) {
        String numeroActual = etDisplay.getText().toString();
        if (!numeroActual.isEmpty() && !numeroActual.equals("Error")) {
            primerNumero = Double.parseDouble(numeroActual);

            // Formatear el primer número para que no salga ".0" en el historial
            String primerNumFormateado = (primerNumero % 1 == 0) ?
                    String.valueOf((long) primerNumero) : String.valueOf(primerNumero);

            if (v.getId() == R.id.btn_power) {
                operacionActual = "^";
            } else {
                operacionActual = ((Button) v).getText().toString();
            }

            tvHistory.setText(primerNumFormateado + " " + operacionActual);
            nuevaOperacion = true;
        }
    }

    public void memoryLogic(View v) {
        try {
            String valorTexto = etDisplay.getText().toString();
            if(valorTexto.equals("Error")) return;

            double valorActual = Double.parseDouble(valorTexto);
            int id = v.getId();

            if (id == R.id.btn_m_plus) {
                memoria += valorActual;
                Toast.makeText(this, "Sumado a memoria", Toast.LENGTH_SHORT).show();
                nuevaOperacion = true;
            }
            else if (id == R.id.btn_m_minus) {
                memoria -= valorActual;
                Toast.makeText(this, "Restado de memoria", Toast.LENGTH_SHORT).show();
                nuevaOperacion = true;
            }
            else if (id == R.id.btn_m_recall) {
                formatResult(memoria);
                nuevaOperacion = true;
            }
        } catch (Exception e) {
            // Error de parseo silencioso
        }
    }

    public void calculateRoot(View v) {
        try {
            double valorActual = Double.parseDouble(etDisplay.getText().toString());
            if (valorActual >= 0) {
                double res = Math.sqrt(valorActual);

                // Formatear valorActual para el historial
                String numFormateado = (valorActual % 1 == 0) ?
                        String.valueOf((long) valorActual) : String.valueOf(valorActual);

                tvHistory.setText("√(" + numFormateado + ")");
                formatResult(res);
            } else {
                etDisplay.setText("Error");
            }
            nuevaOperacion = true;
        } catch (Exception e) {
            etDisplay.setText("Error");
        }
    }

    public void calculateResult(View v) {
        if (operacionActual.isEmpty()) return;
        try {
            double segundoNumero = Double.parseDouble(etDisplay.getText().toString());
            double resultado = 0;

            // Formatear el segundo número para el historial (quitar el .0)
            String segundoNumFormateado = (segundoNumero % 1 == 0) ?
                    String.valueOf((long) segundoNumero) : String.valueOf(segundoNumero);

            tvHistory.setText(tvHistory.getText().toString() + " " + segundoNumFormateado + " =");

            switch (operacionActual) {
                case "+": resultado = primerNumero + segundoNumero; break;
                case "-": resultado = primerNumero - segundoNumero; break;
                case "×": resultado = primerNumero * segundoNumero; break;
                case "/":
                    if (segundoNumero != 0) resultado = primerNumero / segundoNumero;
                    else { etDisplay.setText("Error"); return; }
                    break;
                case "^": resultado = Math.pow(primerNumero, segundoNumero); break;
            }
            formatResult(resultado);
            operacionActual = "";
            nuevaOperacion = true;
        } catch (Exception e) {
            etDisplay.setText("Error");
        }
    }

    private void formatResult(double res) {
        // Esta función se encarga de que el resultado en el display no tenga .0 si es entero
        if (res % 1 == 0) {
            etDisplay.setText(String.valueOf((long) res));
        } else {
            etDisplay.setText(String.valueOf(res));
        }
    }

    public void clearScreen(View v) {
        etDisplay.setText("0");
        tvHistory.setText("");
        primerNumero = 0;
        operacionActual = "";
        nuevaOperacion = true;
    }

    public void deleteLast(View v) {
        String texto = etDisplay.getText().toString();
        if (texto.equals("Error") || texto.length() <= 1) {
            etDisplay.setText("0");
            nuevaOperacion = true;
        } else {
            etDisplay.setText(texto.substring(0, texto.length() - 1));
        }
    }
}