package br.com.radani.www.mensageiro;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class escritaSectionsPageAdapter extends FragmentStatePagerAdapter {

    Bundle bundleLocal = new Bundle();
    public escritaSectionsPageAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch(position)
        {
            case 0:
                escritaConfiguracao escritaConfiguracao = new escritaConfiguracao();
                escritaConfiguracao.setArguments(bundleLocal);
                return escritaConfiguracao;
            case 1:
                escritaDisplay escritaDisplay = new escritaDisplay();
                escritaDisplay.setArguments(bundleLocal);
                return escritaDisplay;
            case 2:
                escritaFalhas escritaFalhas = new escritaFalhas();
                escritaFalhas.setArguments(bundleLocal);
                return escritaFalhas;
            case 3:
                escritaParametrizacao escritaParametrizacao = new escritaParametrizacao();
                escritaParametrizacao.setArguments(bundleLocal);
                return escritaParametrizacao;
            case 4:
                escritaSequenciaInicial escritaSequenciaInicial = new escritaSequenciaInicial();
                escritaSequenciaInicial.setArguments(bundleLocal);
                return escritaSequenciaInicial;
            case 5:
                escritaSequenciaCiclica escritaSequenciaCiclica = new escritaSequenciaCiclica();
                escritaSequenciaCiclica.setArguments(bundleLocal);
                return escritaSequenciaCiclica;
            case 6:
                escritaSequenciaFinal escritaSequenciaFinal = new escritaSequenciaFinal();
                escritaSequenciaFinal.setArguments(bundleLocal);
                return escritaSequenciaFinal;
            case 7:
                escritaSequenciaRotina1 escritaSequenciaRotina1 = new escritaSequenciaRotina1();
                escritaSequenciaRotina1.setArguments(bundleLocal);
                return escritaSequenciaRotina1;
            case 8:
                escritaSequenciaRotina2 escritaSequenciaRotina2 = new escritaSequenciaRotina2();
                escritaSequenciaRotina2.setArguments(bundleLocal);
                return escritaSequenciaRotina2;
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