package br.com.radani.www.mensageiro;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;

import static br.com.radani.www.mensageiro.gerenciadorBanco.CONFIG_1_TABLE_NAME;
import static br.com.radani.www.mensageiro.gerenciadorBanco.VALORES_ATUAIS_TABLE_NAME;
import static br.com.radani.www.mensageiro.utils.literalObterCodigo;
import static br.com.radani.www.mensageiro.utils.literalObterTipoDado;

public class escritaEdicao extends Activity {
    gerenciadorBanco conectorBanco;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conectorBanco = new gerenciadorBanco(this);
        String frase = getIntent().getExtras().getString("linha");
        setContentView(R.layout.escrita_edicao);
        TextView valorAnteriorTextBox = findViewById(R.id.valor_anterior_edicao);
        TextView titulo = findViewById(R.id.titulo_edicao);
        TextView labelTextBox = findViewById(R.id.label_edicao);
        Spinner dropdown = findViewById(R.id.dropdown_valores);
        Button botaoMenos = findViewById(R.id.menos);
        TextView valorNumericoAtualTextBox = findViewById(R.id.valor_numerico_atual);
        Button botaoMais = findViewById(R.id.mais);
        String codigo = literalObterCodigo(frase);
        String tipoDado = literalObterTipoDado(codigo);
        String valorAnterior;
        if (tipoDado.substring(0,1)!= "s"){
            valorAnterior = "Valor anterior "+frase.substring(5);
        }
        else{
            valorAnterior = "Valor anterior "+frase.substring(6);
        }
        valorAnteriorTextBox.setText(valorAnterior);
        titulo.setText(codigo);


        Cursor dados_atuais_linha = conectorBanco.consultaBanco("SELECT * FROM "+VALORES_ATUAIS_TABLE_NAME+" WHERE codigo = "+"'"+codigo+"'");

        Cursor dados_mestre_linha ;
        switch (tipoDado){

            case "C":

                dados_mestre_linha = conectorBanco.consultaBanco("SELECT * FROM "+ CONFIG_1_TABLE_NAME +" WHERE codigo = "+"'"+codigo+"'");

                if (dados_mestre_linha.moveToFirst()) {
                    String label = (dados_mestre_linha.getString(dados_mestre_linha.getColumnIndex("label")));
                    labelTextBox.setText(label);
                    String multiplicador = (dados_mestre_linha.getString(dados_mestre_linha.getColumnIndex("multiplicador")));
                    if (multiplicador != null) {
                        botaoMenos.setOnClickListener(v -> decrement(v, multiplicador));
                        botaoMais.setOnClickListener(v -> increment(v, multiplicador));

                        dropdown.setVisibility(View.GONE);
                        if (dados_atuais_linha.moveToFirst()) {
                            String valorNumerico = (dados_atuais_linha.getString(dados_atuais_linha.getColumnIndex("valor")));
                            valorNumericoAtualTextBox.setText(String.valueOf(valorNumerico));
                        }

                    }
                    else{
                        ArrayList textos = utils.ObterValoresDropdown(dados_mestre_linha);
                        ArrayAdapter dropdownAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, textos);
                        dropdown.setAdapter(dropdownAdapter);
                        botaoMais.setVisibility(View.GONE);
                        botaoMenos.setVisibility(View.GONE);
                        valorNumericoAtualTextBox.setVisibility(View.GONE);
                        }

                }
                dados_mestre_linha.close();
                break;
            case "P":
                break;

        }


    }

    public void decrement(View v, String multiplicador)
    {
        Double multiplicadorDouble = Double.valueOf(multiplicador);
        TextView valorNumericoAtualTextBox = findViewById(R.id.valor_numerico_atual);
        String valorNumericoTexto =  String.valueOf(valorNumericoAtualTextBox.getText());
        Double valorNumerico = Double.valueOf(valorNumericoTexto);
        BigDecimal conta;
        BigDecimal valorNumericoBigDecimal = BigDecimal.valueOf(valorNumerico);
        BigDecimal multiplicadorBigDecimal = BigDecimal.valueOf(multiplicadorDouble);
        conta = valorNumericoBigDecimal.subtract(multiplicadorBigDecimal);
        valorNumericoTexto = conta.toString();
        valorNumericoTexto = utils.trata_valor_multiplicador(valorNumericoTexto,Double.valueOf(multiplicador));
        valorNumericoAtualTextBox.setText(valorNumericoTexto);
    }


    public void increment(View v, String multiplicador)
    {
        Double multiplicadorDouble = Double.valueOf(multiplicador);
        TextView valorNumericoAtualTextBox = findViewById(R.id.valor_numerico_atual);
        String valorNumericoTexto =  String.valueOf(valorNumericoAtualTextBox.getText());
        Double valorNumerico = Double.valueOf(valorNumericoTexto);
        BigDecimal conta;
        BigDecimal valorNumericoBigDecimal = BigDecimal.valueOf(valorNumerico);
        BigDecimal multiplicadorBigDecimal = BigDecimal.valueOf(multiplicadorDouble);
        conta = valorNumericoBigDecimal.add(multiplicadorBigDecimal);
        valorNumericoTexto = conta.toString();
        valorNumericoTexto = utils.trata_valor_multiplicador(valorNumericoTexto,Double.valueOf(multiplicador));
        valorNumericoAtualTextBox.setText(valorNumericoTexto);
    }
}
