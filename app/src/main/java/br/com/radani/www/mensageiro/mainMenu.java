package br.com.radani.www.mensageiro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class mainMenu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void vaiparaleitura(View view)
    {
        Intent intent = new Intent(mainMenu.this, DeviceListActivity.class);
        startActivity(intent);
    }

}
