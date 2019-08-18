package br.com.radani.www.mensageiro;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Chirag on 30-Jul-17.
 */

public class SectionsPageAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;

    public PagerAdapter(FragmentManager fm, int NumberOfTabs)
    {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch(position)
        {

            case 0:
                return new leituraConfiguracao();
            case 1:
                return new leituraDisplay();
            case 2:
                return new leituraFalhas();
            case 3:
                return new leituraParametrizacao();
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
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}