package br.com.apppedido;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import br.com.apppedido.Model.Produto;

public class CadProdutoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ImageView imgProduto;
    EditText nomeProduto;
    EditText preco;
    EditText codBarras;
    private ProgressBar progressBarProduto;
    private Button btnSalvarProduto;

    Bitmap imagem = null;
    String path_image;
    ImageView codBar;

    private static final int SELECT_PICTURE = 200;
    private static final String TAG = "SelectImageActivity";

    private StorageReference storageRef;
    DatabaseReference databaseProduto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cad_produto);

        storageRef = FirebaseStorage.getInstance().getReference();
        databaseProduto = FirebaseDatabase.getInstance().getReference("produtos");

        inicializarComponentes();

        codBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ScannerActivity.class);
                intent.putExtra("scanCadCodBar",true);
                startActivityForResult(intent, 4);
            }
        });

        progressBarProduto.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCadProduto);
        toolbar.setNavigationIcon(R.drawable.ic_cancel); // your drawable
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });


        //Cadastrar Produto
        progressBarProduto.setVisibility(View.GONE);
        btnSalvarProduto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressBarProduto.setVisibility(View.VISIBLE);
                Produto newPro = setProduto();
                databaseProduto.child(newPro.getId()).setValue(newPro).addOnCompleteListener(CadProdutoActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBarProduto.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Cliente salvo com sucesso!", Toast.LENGTH_LONG).show();
                    }
                });
                finish();
            }
        });


    }

    Produto setProduto() {

        Produto prod = new Produto();

        prod.setId(databaseProduto.push().getKey());
        prod.setNome(nomeProduto.getText().toString());
        prod.setPreco(preco.getText().toString());
        prod.setCodBarra(codBarras.getText().toString());

        if(path_image.length()<=0){
            prod.setImgProduto(null);
        }else{
            prod.setImgProduto(path_image);
        }

        return prod;
    }


    private void handlePermission() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //ask for permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    SELECT_PICTURE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case SELECT_PICTURE:
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
                        if (showRationale) {
                            //  Show your own message here
                        } else {
                            showSettingsAlert();
                        }
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /* Choose an image from Gallery */
    void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecione Imagem"), SELECT_PICTURE);
    }

    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                if (resultCode == RESULT_OK) {

                    try {
                        if (requestCode == SELECT_PICTURE) {
                            // Get the url from data

                            final Uri selectedImageUri = data.getData();
                            if (null != selectedImageUri) {

                                imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                                final Bitmap finalImagem = imagem;
                                findViewById(R.id.imgCadPro).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((ImageView) findViewById(R.id.imgCadPro)).setImageBitmap(finalImagem);
                                    }
                                });
                                // Recu perar dados da imagem para o firebase
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                imagem.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                                byte[] dadosImagem = baos.toByteArray();

                                final String[] source = new String[1];


                                final StorageReference[] imageRef = {storageRef
                                        .child("imagens")
                                        .child("produtos")
                                        .child(selectedImageUri.getLastPathSegment() + ".jpeg")};
                                final UploadTask uploadTask = imageRef[0].putBytes(dadosImagem);
                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(CadProdutoActivity.this, "Erro ao fazer upload da imagem", Toast.LENGTH_LONG).show();
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        //here

                                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                        while (!urlTask.isSuccessful());
                                        Uri downloadUrl = urlTask.getResult();

                                        path_image = String.valueOf(downloadUrl);
                                        Toast.makeText(CadProdutoActivity.this, "Imagem:" + path_image, Toast.LENGTH_LONG).show();

                                    }
                                });

                            }
                        }

                        if (requestCode == 4) {

                            String cod =  data.getStringExtra("scanCadCodBar");

                            codBarras.setText(cod);

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }



            }
        }).start();



    }

    /* Get the real path from the URI */
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    private void showSettingsAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SETTINGS",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        openAppSettings(CadProdutoActivity.this);
                    }
                });
        alertDialog.show();
    }

    public static void openAppSettings(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

    public void handleImageClick(View view) {
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
    }


    public void inicializarComponentes() {

        nomeProduto = findViewById(R.id.nomeRazao);
        preco = findViewById(R.id.cadProPreco);
        codBarras = findViewById(R.id.cadProCod);
        btnSalvarProduto = findViewById(R.id.btnSalvarCadPro);
        codBar =  findViewById(R.id.imgCodBarCad);
        nomeProduto.requestFocus();


        imgProduto = (ImageView) findViewById(R.id.imgCadPro);
        progressBarProduto =  findViewById(R.id.progressBarCadPro);
       imgProduto.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               handlePermission();
               openImageChooser();
           }
       });

    }

}
