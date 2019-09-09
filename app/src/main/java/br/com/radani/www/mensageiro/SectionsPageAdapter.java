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
                return new leituraConfiguracao();
            case 1:
                return new leituraDisplay();
            case 2:
                return new leituraFalhas();
            case 3:
                leituraParametrizacao leituraParametrizacao = new leituraParametrizacao();
                leituraParametrizacao.setArguments(bundleLocal);
                return leituraParametrizacao;
            case 4:
                return new leituraSequenciaInicial();
            case 5:
                return new leituraSequenciaCiclica();
            case 6:
                return new leituraSequenciaFinal();
            case 7:
                return new leituraSequenciaRotina1();
            case 8:
                return new leituraSequenciaRotina2();
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