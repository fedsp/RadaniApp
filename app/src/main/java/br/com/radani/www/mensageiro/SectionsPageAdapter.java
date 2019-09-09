package br.com.radani.www.mensageiro;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class SectionsPageAdapter extends FragmentStatePagerAdapter {

    Bundle bundleLocal = new Bundle();
    public SectionsPageAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch(position)
        {
            case 0:
                leituraConfiguracao leituraConfiguracao = new leituraConfiguracao();
                leituraConfiguracao.setArguments(bundleLocal);
                return leituraConfiguracao;
            case 1:
                leituraDisplay leituraDisplay = new leituraDisplay();
                leituraDisplay.setArguments(bundleLocal);
                return leituraDisplay;
            case 2:
                leituraFalhas leituraFalhas = new leituraFalhas();
                leituraFalhas.setArguments(bundleLocal);
                return leituraFalhas;
            case 3:
                leituraParametrizacao leituraParametrizacao = new leituraParametrizacao();
                leituraParametrizacao.setArguments(bundleLocal);
                return leituraParametrizacao;
            case 4:
                leituraSequenciaInicial leituraSequenciaInicial = new leituraSequenciaInicial();
                leituraSequenciaInicial.setArguments(bundleLocal);
                return leituraSequenciaInicial;
            case 5:
                leituraSequenciaCiclica leituraSequenciaCiclica = new leituraSequenciaCiclica();
                leituraSequenciaCiclica.setArguments(bundleLocal);
                return leituraSequenciaCiclica;
            case 6:
                leituraSequenciaFinal leituraSequenciaFinal = new leituraSequenciaFinal();
                leituraSequenciaFinal.setArguments(bundleLocal);
                return leituraSequenciaFinal;
            case 7:
                leituraSequenciaRotina1 leituraSequenciaRotina1 = new leituraSequenciaRotina1();
                leituraSequenciaRotina1.setArguments(bundleLocal);
                return leituraSequenciaRotina1;
            case 8:
                leituraSequenciaRotina2 leituraSequenciaRotina2 = new leituraSequenciaRotina2();
                leituraSequenciaRotina2.setArguments(bundleLocal);
                return leituraSequenciaRotina2;
            default:
                return fragment;
        }
    }

    @Override
    public int getCount() {
        return 9;
    }


    public void recebeBundle(Bundle bundletodosDados){
        bundleLocal = bundletodosDados;
    }
}