package br.com.apppedido;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScannerActivity extends AppCompatActivity {
Button btn_scan;


    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

        IntentIntegrator integrador =  new IntentIntegrator(this);
        integrador.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrador.setPrompt("Camera Scan");
        integrador.setOrientationLocked(true);
        integrador.setBeepEnabled(true);
        integrador.setCameraId(0); // 0 = CAmera Traseira
        integrador.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult result =  IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result != null){
                if(result.getContents()!=null){

                    Intent intent = getIntent();
                    if(intent.hasExtra("scan")){

                        intent.putExtra("scan", result.getContents());
                        setResult(RESULT_OK, intent);

                    }

                    if(intent.hasExtra("scanCadCodBar")){

                        intent.putExtra("scanCadCodBar", result.getContents());
                        setResult(RESULT_OK, intent);

                    }


                    finish();

                }else{
                    alert("Scan Cancelado");
                }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

   private  void alert(String msg){
       Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
   }
}

