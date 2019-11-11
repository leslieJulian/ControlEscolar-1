package com.example.controlescolar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DetalleMateria extends AppCompatActivity {

    Bundle parametros;
    private String key = "";

    private EditText clave,nombre,creditos,horasClase,horasTeoricas,horasPracticas,semestre,requisito1,requisito2,requisito3,requisito4,requisito5,nombreCorto;
    private Spinner planes,especialidades;
    private CheckBox especialidad,requerimientos;
    private Button modificar, cancelar;

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance ();
    private DatabaseReference mDatabaseReference = mDatabase.getReference ();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_materia);
        parametros = DetalleMateria.this.getIntent().getExtras();

        inicializarElementos();
        cargarDatos();

    }

    public void cargarDatos(){
        if(parametros != null){
            clave.setText(parametros.getString("clave"));
            nombre.setText(parametros.getString("nombre"));
            nombreCorto.setText(parametros.getString("nombreCorto"));
            planes.setSelection(getIndex(planes, parametros.getString("plan")));
            creditos.setText(parametros.getInt("creditos")+"");
            horasClase.setText(parametros.getInt("horasClase")+"");
            horasTeoricas.setText(parametros.getInt("horasTeoricas")+"");
            horasPracticas.setText(parametros.getInt("horasPracticas")+"");
            semestre.setText(parametros.getInt("semestre")+"");
            if(parametros.getBoolean("isEspecialidad")){
                especialidad.setChecked(true);
                especialidadVisible();
                especialidades.setSelection(getIndex(especialidades, parametros.getString("especialidad")));
            }
            if(parametros.getBoolean("isRequerimiento")){
                requerimientos.setChecked(true);
                requisistosVisible();
                requisito1.setText(parametros.getString("requerimiento1"));
                requisito2.setText(parametros.getString("requerimiento2"));
                requisito3.setText(parametros.getString("requerimiento3"));
                requisito4.setText(parametros.getString("requerimiento4"));
                requisito5.setText(parametros.getString("requerimiento5"));
            }
        }
    }

    private int getIndex(Spinner spinner, String valor){
        int index =spinner.getCount();
        for (int i = 0; i < index; i++){
            if(spinner.getItemAtPosition(i).equals(valor)){
                index = i;
                break;
            }
        }
        return index;
    }

    public void inicializarElementos(){

        clave=(EditText)findViewById(R.id.txtClave);
        nombre=(EditText)findViewById(R.id.txtNombre);
        creditos=(EditText)findViewById(R.id.txtCreditos);
        horasClase=(EditText)findViewById(R.id.txtHorasClase);
        horasTeoricas=(EditText)findViewById(R.id.txtHorasT);
        horasPracticas=(EditText)findViewById(R.id.txtHorasP);
        semestre=(EditText)findViewById(R.id.txtSemestre);
        requisito1=(EditText)findViewById(R.id.txtRequisito1);
        requisito2=(EditText)findViewById(R.id.txtRequisito2);
        requisito3=(EditText)findViewById(R.id.txtRequisito3);
        requisito4=(EditText)findViewById(R.id.txtRequisito4);
        requisito5=(EditText)findViewById(R.id.txtRequisito5);
        nombreCorto=(EditText)findViewById(R.id.txtNombreCorto);

        planes=(Spinner)findViewById(R.id.spPlanes);
        especialidades=(Spinner)findViewById(R.id.spEspecialidades);

        especialidad=(CheckBox)findViewById(R.id.cbEspecialidad);
        requerimientos=(CheckBox)findViewById(R.id.cbRequisitos);

        modificar=(Button)findViewById(R.id.btnGuardar);
        cancelar=(Button)findViewById(R.id.btnCancelar);

        cargarPlanes();
        cargarEspecialidades();
    }

    public void cargarPlanes(){
        mDatabaseReference.child("planesdeestudio").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    ArrayList<String> listaPlanes = new ArrayList<String>();
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        String plan = ds.getKey();
                        listaPlanes.add(plan);
                        planes.setAdapter(new ArrayAdapter<String>(DetalleMateria.this, android.R.layout.simple_spinner_dropdown_item, listaPlanes));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void cargarEspecialidades(){
        mDatabaseReference.child("especialidades").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    ArrayList<String> listaEspecialidades = new ArrayList<String>();
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        String especialidad = ds.getKey();
                        listaEspecialidades.add(especialidad);
                        especialidades.setAdapter(new ArrayAdapter<String>(DetalleMateria.this, android.R.layout.simple_spinner_dropdown_item, listaEspecialidades));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void esEspecialidad(View view){
        if (especialidad.isChecked()){
            especialidades.setVisibility(View.VISIBLE);
        }else{
            especialidades.setVisibility(View.GONE);
        }
    }

    public void especialidadVisible(){
        especialidades.setVisibility(View.VISIBLE);
    }

    public void tieneRequisitos(View view){
        if (requerimientos.isChecked()){
            requisito1.setVisibility(View.VISIBLE);
            requisito2.setVisibility(View.VISIBLE);
            requisito3.setVisibility(View.VISIBLE);
            requisito4.setVisibility(View.VISIBLE);
            requisito5.setVisibility(View.VISIBLE);
        }else{
            requisito1.setVisibility(View.GONE);
            requisito2.setVisibility(View.GONE);
            requisito3.setVisibility(View.GONE);
            requisito4.setVisibility(View.GONE);
            requisito5.setVisibility(View.GONE);
        }
    }

    public void requisistosVisible(){
        requisito1.setVisibility(View.VISIBLE);
        requisito2.setVisibility(View.VISIBLE);
        requisito3.setVisibility(View.VISIBLE);
        requisito4.setVisibility(View.VISIBLE);
        requisito5.setVisibility(View.VISIBLE);
    }

    public void cancelar(View view){
        Intent intent = new Intent(DetalleMateria.this, MainActivity.class);
        DetalleMateria.this.startActivity(intent);
        DetalleMateria.this.finish();
    }

    public void modificar(View view) {

        mDatabaseReference.child("materias").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        key = ds.getKey();
                        String claveM = ds.child("clave").getValue().toString();
                        if (claveM.equals(parametros.getString("clave"))) {
                            executarModificar(key);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void executarModificar(String Key){

        int creditosMat = 0, hClase = 0 , hTeoria = 0, hPractica = 0, semestreMat = 0;
        Materia materia = null;
        try {
            String idClave = clave.getText().toString();
            String nombreMateria=nombre.getText().toString();
            String plan = planes.getSelectedItem().toString();
            creditosMat = (Integer.parseInt(creditos.getText().toString()));
            hClase = (Integer.parseInt(horasClase.getText().toString()));
            hTeoria = (Integer.parseInt(horasTeoricas.getText().toString()));
            hPractica = (Integer.parseInt(horasPracticas.getText().toString()));
            semestreMat = (Integer.parseInt(semestre.getText().toString()));
            boolean isEspecialidad = especialidad.isChecked()?true:false;
            String especialidadMateria = especialidad.isChecked()?especialidades.getSelectedItem().toString():null;
            boolean isRequerimientos = requerimientos.isChecked()?true:false;
            String r1=null, r2=null, r3=null, r4=null, r5=null;
            String nombreCortoM = nombreCorto.getText().toString();
            if(isRequerimientos){
                r1 = !requisito1.getText().toString().isEmpty()?requisito1.getText().toString():null;
                r2 = !requisito2.getText().toString().isEmpty()?requisito2.getText().toString():null;
                r3 = !requisito3.getText().toString().isEmpty()?requisito3.getText().toString():null;
                r4 = !requisito4.getText().toString().isEmpty()?requisito4.getText().toString():null;
                r5 = !requisito5.getText().toString().isEmpty()?requisito5.getText().toString():null;
            }
            materia = new Materia(idClave, nombreMateria,nombreCortoM ,plan, especialidadMateria, r1, r2, r3, r4, r5, creditosMat,hClase, hTeoria, hPractica, semestreMat, isEspecialidad, isRequerimientos);
        }catch (Exception e){
        }

        if(camposVacios()){
            if(evaluar(creditosMat, 10,1)){
                if (evaluar(hClase, 5, 1)) {
                    if(evaluar(hTeoria, 5, 0)) {
                        if (evaluar(hPractica, 5, 1)) {
                            if(evaluar(semestreMat, 9, 1)){

                                mDatabaseReference = mDatabase.getReference("materias");
                                mDatabaseReference.child(Key).setValue(materia);

                                Intent intent = new Intent(DetalleMateria.this, MainActivity.class);
                                DetalleMateria.this.startActivity(intent);
                                DetalleMateria.this.finish();
                            }else{
                                Toast.makeText(DetalleMateria.this, "Campo semestre fuera de rango", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(DetalleMateria.this, "Campo horasPracticas fuera de rango", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(DetalleMateria.this, "Campo horas teoricas fuera de rango", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(DetalleMateria.this, "Campo horas clase fuera de rango", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(DetalleMateria.this, "Campo creditos fuera de rango", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(DetalleMateria.this, "Campos vacios", Toast.LENGTH_SHORT).show();
        }
    }


    public Boolean camposVacios(){
        if(clave.getText().toString().isEmpty() || nombre.getText().toString().isEmpty() ||nombreCorto.getText().toString().isEmpty() ||
                creditos.getText().toString().isEmpty()|| horasClase.getText().toString().isEmpty() ||
                horasPracticas.getText().toString().isEmpty() || horasTeoricas.getText().toString().isEmpty()
                || semestre.getText().toString().isEmpty()){
            return false;
        }else{
            return true;
        }
    }

    public Boolean evaluar(int num, int maxlimte, int minlimte){
        if(num >= minlimte && num <=maxlimte){
            return true;
        }else{
            return false;
        }
    }
}
