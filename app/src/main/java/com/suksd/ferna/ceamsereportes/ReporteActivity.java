package com.suksd.ferna.ceamsereportes;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ReporteActivity extends AppCompatActivity {

    //private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    // Access a Cloud Firestore instance from your Activity
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    Button btnEnviar;
    EditText descripcion;
    EditText nombre;
    EditText lugar;
    EditText fecha;

    Button btnFoto;
    String fotonombre;
    ImageView imagenfoto;
    boolean incluyeimagen = false;
    AlertDialog.Builder dialogo1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte);

        btnEnviar = findViewById(R.id.btnEnviar);
        descripcion = findViewById(R.id.txtDescripcion);
        nombre = findViewById(R.id.txtNombre);
        lugar = findViewById(R.id.txtLugar);
        fecha = findViewById(R.id.txtFecha);
        btnFoto = findViewById(R.id.btnFoto);
        imagenfoto = findViewById(R.id.imgFoto);
        fotonombre = getExternalFilesDir(null)+"/"+"Image";
        incluyeimagen=false;

        dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Confirmación");
        dialogo1.setMessage("¿Desea enviar el informe al área de Higiene y Seguridad?");
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                aceptar();
            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                cancelar();
            }
        });

        Date date = new Date();
        DateFormat hourdateFormat = DateFormat.getDateTimeInstance();
        fecha.setText(hourdateFormat.format(date));

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(descripcion.getText().toString().trim().length()<1) {
                    Toast.makeText(getApplicationContext(), "La descripción es obligatoria", Toast.LENGTH_LONG).show();
                }
                else if(lugar.getText().toString().trim().length()<1){
                    Toast.makeText(getApplicationContext(), "El lugar es obligatorio", Toast.LENGTH_LONG).show();
                }
                else{
                    dialogo1.show();
                }


            }
        });

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                Intent intento1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File foto = new File(getExternalFilesDir(null),"Image");
                intento1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(foto));
                startActivityForResult(intento1, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Bitmap bitmap1 = BitmapFactory.decodeFile(fotonombre);
                imagenfoto.setVisibility(View.VISIBLE);
                imagenfoto.setImageBitmap(bitmap1);
                incluyeimagen = true;
            }
        }
    }

    public void aceptar() {
        //String informe = "Nombre/Legajo: "+nombre.getText().toString()+"<br/><br/>"+"Lugar/Sector: "+lugar.getText().toString()+"<br/><br/>"+fecha.getText().toString()+"<br/><br/>"+"Descripción:"+"<br/>"+descripcion.getText().toString();
        //Mail.sendEmail("msuksdorf@ceamse.gov.ar","Informe higiene y seguridad",informe,incluyeimagen,fotonombre);
        //Mail.sendEmail("lic.suksdorf@gmail.com","Informe higiene y seguridad",informe,incluyeimagen,fotonombre);

        //reporte Reporte = new reporte(nombre.getText().toString(),lugar.getText().toString(),fecha.getText().toString(),descripcion.getText().toString());
        //rootRef.child("reportes").push().setValue(Reporte);

        Map<String, Object> data = new HashMap<>();
        data.put("detalles",descripcion.getText().toString());
        data.put("fecha",fecha.getText().toString());
        data.put("usuario:",nombre.getText().toString());
        data.put("sector",lugar.getText().toString());

        Toast.makeText(getApplicationContext(), "Enviando informe...", Toast.LENGTH_LONG).show();

        db.collection("reportes").add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        finish();
                        Toast.makeText(getApplicationContext(), "Informe enviado", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Falla al enviar", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void cancelar() {

    }
}
