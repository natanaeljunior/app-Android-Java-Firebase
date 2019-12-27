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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import br.com.apppedido.Model.Cliente;
import br.com.apppedido.Model.Endereco;

public class CadClienteActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ImageView imgPerfil;
    RadioGroup radioGroup;
    String t_cli;
    EditText nomeRazao;
    EditText apelidoNomeFantasia;
    EditText et_tel1;
    EditText et_tel2;
    EditText email;
    EditText cep;
    Spinner uf;

    EditText cidadeMuni;
    EditText endereco;

    private ProgressBar progressBar;
    private Button btnSalvar;


    Bitmap imagem = null;
    String path_image;

    private static final int SELECT_PICTURE = 200;
    private static final String TAG = "SelectImageActivity";

    private StorageReference storageRef;
    DatabaseReference databaseCliente;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cad_cliente);

        storageRef = FirebaseStorage.getInstance().getReference();
        databaseCliente = FirebaseDatabase.getInstance().getReference("clientes");

        inicializarComponentes();

        final Intent intent = getIntent();

        if(intent.hasExtra("editCliente")){
            Cliente cliEdit =  (Cliente) intent.getSerializableExtra("editCliente");
          setDadosEdit(cliEdit);

        }


        progressBar.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPed);
        toolbar.setNavigationIcon(R.drawable.ic_cancel); // your drawable
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });


        //Cadastrar Cliente
        progressBar.setVisibility(View.GONE);
        btnSalvar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Cliente newCli;
                progressBar.setVisibility(View.VISIBLE);
                if(intent.hasExtra("editCliente")){
                    Cliente cliEdit =  (Cliente) intent.getSerializableExtra("editCliente");
                     newCli = setClienteEdit(cliEdit);

                }else {
                     newCli = setCliente();
                }

                databaseCliente.child(newCli.getId()).setValue(newCli).addOnCompleteListener(CadClienteActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Cliente salvo com sucesso!", Toast.LENGTH_LONG).show();
                    }
                });
                finish();
            }
        });


    }

    private Cliente setClienteEdit(Cliente cliOld) {

        Cliente cli = new Cliente();

        cli.setId(cliOld.getId());
        cli.setTipoCliente(t_cli);
        cli.setNomeRazao(nomeRazao.getText().toString());
        cli.setApelidoNomeFantasia(apelidoNomeFantasia.getText().toString());
        cli.setTelefone1(et_tel1.getText().toString());
        cli.setTelefone2(et_tel2.getText().toString());
        cli.setEmail(email.getText().toString());

        path_image =  cliOld.getCaminhoFoto();
        if(path_image.length()<=0){
            cli.setCaminhoFoto("");
        }else{
            cli.setCaminhoFoto(path_image);
        }

        Endereco end = new Endereco();
        end.setCep(cep.getText().toString());
        end.setUF(uf.getSelectedItem().toString());
        end.setCidadeMunicipio(cidadeMuni.getText().toString());
        end.setEndereco(endereco.getText().toString());

        cli.setEndereco(end);

        return cli;
    }

    private void setDadosEdit(Cliente cliEdit) {

        Picasso.get().load(cliEdit.getCaminhoFoto()).into(imgPerfil);

        if(cliEdit.getTipoCliente().equals("PJ")){
            RadioButton radioPJ = findViewById(R.id.radioPJ);
            radioPJ.setSelected(true);
        }else if(cliEdit.getTipoCliente().equals("PF")){
            RadioButton radioPF = findViewById(R.id.radioPF);
            radioPF.setSelected(true);
        }

        nomeRazao.setText(cliEdit.getNomeRazao());
        apelidoNomeFantasia.setText(cliEdit.getApelidoNomeFantasia());
        et_tel1.setText(cliEdit.getTelefone1());
        et_tel2.setText(cliEdit.getTelefone2());
        email.setText(cliEdit.getEmail());
        cep.setText(cliEdit.getEndereco().getCep());
        uf.setSelected(false);



        cidadeMuni.setText(cliEdit.getEndereco().getCidadeMunicipio());
        endereco.setText(cliEdit.getEndereco().getEndereco());
    }


    Cliente setCliente() {


        Cliente cli = new Cliente();

        cli.setId(databaseCliente.push().getKey());
        cli.setTipoCliente(t_cli);
        cli.setNomeRazao(nomeRazao.getText().toString());
        cli.setApelidoNomeFantasia(apelidoNomeFantasia.getText().toString());
        cli.setTelefone1(et_tel1.getText().toString());
        cli.setTelefone2(et_tel2.getText().toString());
        cli.setEmail(email.getText().toString());

        if(path_image.length()<=0){
            cli.setCaminhoFoto("");
        }else{
            cli.setCaminhoFoto(path_image);
        }

        Endereco end = new Endereco();
        end.setCep(cep.getText().toString());
        end.setUF(uf.getSelectedItem().toString());
        end.setCidadeMunicipio(cidadeMuni.getText().toString());
        end.setEndereco(endereco.getText().toString());

        cli.setEndereco(end);

        return cli;
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
                                findViewById(R.id.profileImageView).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((ImageView) findViewById(R.id.profileImageView)).setImageBitmap(finalImagem);
                                    }
                                });
                                // Recu perar dados da imagem para o firebase
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                imagem.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                                byte[] dadosImagem = baos.toByteArray();

                                final String[] source = new String[1];


                                final StorageReference[] imageRef = {storageRef
                                        .child("imagens")
                                        .child("perfil_cliente")
                                        .child(selectedImageUri.getLastPathSegment() + ".jpeg")};
                                final UploadTask uploadTask = imageRef[0].putBytes(dadosImagem);
                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(CadClienteActivity.this, "Erro ao fazer upload da imagem", Toast.LENGTH_LONG).show();
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        //here
                                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                        while (!urlTask.isSuccessful());
                                        Uri downloadUrl = urlTask.getResult();

                                        path_image = String.valueOf(downloadUrl);
                                        Toast.makeText(CadClienteActivity.this, "Imagem:" + path_image, Toast.LENGTH_LONG).show();

                                    }
                                });

                            }
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
                        openAppSettings(CadClienteActivity.this);
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

        imgPerfil = findViewById(R.id.profileImageView);
        radioGroup = findViewById(R.id.radioGroup);
        nomeRazao = findViewById(R.id.nomeRazao);
        apelidoNomeFantasia = findViewById(R.id.apelidoNomFantasia);
        et_tel1 = findViewById(R.id.et_tel1);
        et_tel2 = findViewById(R.id.et_tel2);
        email = findViewById(R.id.email);
        cep = findViewById(R.id.cep);
        uf = findViewById(R.id.spinner_cli_uf);


        cidadeMuni = findViewById(R.id.cidadeMuni);
        endereco = findViewById(R.id.endereco);
        progressBar = findViewById(R.id.progressBar);
        btnSalvar = findViewById(R.id.btnSalvar);

        nomeRazao.requestFocus();

        // Mascara Edit Text
        SimpleMaskFormatter smf = new SimpleMaskFormatter("(NN)NNNNN-NNNN");
        MaskTextWatcher mtw = new MaskTextWatcher(et_tel1, smf);
        et_tel1.addTextChangedListener(mtw);

        MaskTextWatcher mtw2 = new MaskTextWatcher(et_tel2, smf);
        et_tel2.addTextChangedListener(mtw2);

        imgPerfil = (ImageView) findViewById(R.id.profileImageView);
        imgPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePermission();
                openImageChooser();
            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radioPF)
                   t_cli = "PF";
                else if(checkedId == R.id.radioPJ)
                    t_cli = "PJ";

            }
        });
    }
}
