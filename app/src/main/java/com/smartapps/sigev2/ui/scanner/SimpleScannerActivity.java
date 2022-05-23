package com.smartapps.sigev2.ui.scanner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.smartapps.sigev2.R;
import com.smartapps.sigev2.classes.ErrorScanner;
import com.smartapps.sigev2.models.people.Person;
import com.google.zxing.Result;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class SimpleScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_simple_scanner);
//        setupToolbar();

        ViewGroup contentFrame = findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
//        mScannerView.setFlash(true);
        mScannerView.setAutoFocus(true);
        contentFrame.addView(mScannerView);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        //Config.cadena_documento = "Contents = " + rawResult.getText() +", Format = " + rawResult.getBarcodeFormat().toString();
        String r = "Contents = " + rawResult.getText() + ", Format = " + rawResult.getBarcodeFormat().toString();

        // formato 1
        // [0] tramite nro.
        // [1] apellido
        // [2] nombre
        // [3] sexo (M / F)
        // [4] nro dni
        // [5] ejemplar
        // [6] fecha nacimiento (DD/MM/AAAA)
        // [7] fecha emision (DD/MM/AAAA)
        // [8] cuil ¿¿¿??? -- no aparece en todos los casos


        //formato 2 (ejemplo de un dni real)
//        0 = "" ????
//        1 = "44016178    " // dni
//        2 = "A" // ejemplar
//        3 = "2" ??? -- en otro ejemplo encontraod no aparece
//        4 = "RODRIGUEZ" // apellidos
//        5 = "ELEAZAR SEBASTIAN" // nombres
//        6 = "ARGENTINA" // nacionalidad
//        7 = "11/02/2002" // fecha de nacimiento
//        8 = "M" // sexo
//        9 = "10/02/2011" // fecha expedicion
//        10 = "00037928281" // ????
//        11 = "7004 "// oficina de identificacion
//        12 = "11/02/2019"// fecha de vencimiento
//        13 = "727" // ??
//        14 = "0" // ??
//        15 = "ILR:2.01 C:110128.02 (No Cap.)" ??
//        16 = "UNIDAD #10 || S/N: 0040>2008>>0012" // ??

        // formato 3 (ejemplo de informacion encontrado en una pagina web)   https://www.reddit.com/r/argentina/comments/tbpy4/pregunta_sobre_el_dni_versi%C3%B3n_tarjeta/
//        0 DNI
//        1 ? (ni idea, todos los que escanee muestran una letra A pero no se me ocurre que puede ser)
//        2 Apellido
//        3 Nombre
//        4 Nacionalidad
//        5 Fecha de Nacimiento
//        6 Sexo
//        7 Fecha de Expedicion
//        8 Nº de Tramite
//        9 Of. Ident. (dato que aparece en el reverso, junto al nº de tramite. ni idea que a que se refiere, pero no creo que sea dificil de averiguar) ("Identificador de oficina", maybe?)
//        10 Fecha de Vencimiento
//        11 ? (numero, 1-3 digitos)
//        12 ? (numero, 1 digito)
//        13 ? (ILR:x.xx C:xxxxxx.xx)
//        14 ? (UNIDAD #xx || S/N: xxxx>xxxx>>xxxx)

        // SE CAMBIO EL FORMATO 3 PARA SOPORTAR DNI VIEJOS
        // SE PRESUPONE QUE SI LA CANTIDAD DE DATOS ES MAYOR A 12, ES UN DNI VIWEJO Y SE TRATA COMO EL FORMATO 2
        try {
            Log.e("result", r);
            String[] datos = rawResult.getText().split("@");

            if (datos.length == 8 || datos.length == 9) { // posiblement sea el formato 1
                Person person = new Person();
                person.setLastName(datos[1].trim());
                person.setFirstName(datos[2].trim());
                person.setPersonGenderId(datos[3].trim().toUpperCase().equals("M") ? 1 : 2);
                person.setDocumentNumber(datos[4].trim());
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    person.setBirthdate(simpleDateFormat.parse(datos[6].trim()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                succes(person, "Formato 1: " + rawResult.getText() + " - Tipo: " + rawResult.getBarcodeFormat().toString());
            } else if (datos.length == 17) { // posiblemente formato 2
                Person person = new Person();
                person.setLastName(datos[4].trim());
                person.setFirstName(datos[5].trim());
                person.setPersonGenderId(datos[8].trim().toUpperCase().equals("M") ? 1 : 2);
                person.setDocumentNumber(datos[1].trim());
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    person.setBirthdate(simpleDateFormat.parse(datos[7].trim()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                succes(person, "Formato 2: " + rawResult.getText() + " - Tipo: " + rawResult.getBarcodeFormat().toString());
            } else if (datos.length > 12) { // posiblemente formato 3
                Person person = new Person();
                person.setLastName(datos[4].trim());
                person.setFirstName(datos[5].trim());
                person.setPersonGenderId(datos[8].trim().toUpperCase().equals("M") ? 1 : 2);
                person.setDocumentNumber(datos[1].trim());
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    person.setBirthdate(simpleDateFormat.parse(datos[7].trim()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                succes(person, "Formato 2: " + rawResult.getText() + " - Tipo: " + rawResult.getBarcodeFormat().toString());
            } else if(datos.length >= 7){ // intentar con el fromato 1 nuevamente
                Person person = new Person();
                person.setLastName(datos[1].trim());
                person.setFirstName(datos[2].trim());
                person.setPersonGenderId(datos[3].trim().toUpperCase().equals("M") ? 1 : 2);
                person.setDocumentNumber(datos[4].trim());
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    person.setBirthdate(simpleDateFormat.parse(datos[6].trim()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                succes(person, "Formato 1: " + rawResult.getText() + " - Tipo: " + rawResult.getBarcodeFormat().toString());
            }else{ // no coincide con ningun formato
                error("Error: " + rawResult.getText()+ " - Tipo: " + rawResult.getBarcodeFormat().toString());
            }
        } catch (Exception e) {
            error("Error: " + rawResult.getText()+ " - Tipo: " + rawResult.getBarcodeFormat().toString());
        }
    }

    private void error(String rawResult) {
        Intent intent = new Intent();
        intent.putExtra("result", new ErrorScanner());
        intent.putExtra("rawResult", rawResult);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void succes(Person person, String rawResult) {
        // remove F or M  from DNI
        if(person.getDocumentNumber() != null){
            String dni = person.getDocumentNumber();
            dni = dni.toUpperCase().replace("F", "0").replace("M", "0");
            person.setDocumentNumber(dni);
        }

        Intent intent = new Intent();
        intent.putExtra("result", person);
        intent.putExtra("rawResult", rawResult);
        setResult(RESULT_OK, intent);
        finish();
    }
}