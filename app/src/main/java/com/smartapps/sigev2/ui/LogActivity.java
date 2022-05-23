package com.smartapps.sigev2.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.smartapps.sigev2.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LogActivity extends AppCompatActivity {

    String r = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        Intent i=this.getIntent();
        final String path = i.getStringExtra("path");
        try {
            File file = new File(path);
            FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            fileReader.close();
            r = result.toString();
            ((TextView)findViewById(R.id.text)).setText(r);
        } catch (IOException e) {
            e.printStackTrace();
            r = "Error al abrir el archivo de log";
            ((TextView)findViewById(R.id.text)).setText(r);
        }
    }

    public void back(View view) {
        finish();
    }

    public void copy(View view) {
        copyErrorToClipboard(r);
        Toast.makeText(this, "Copiado", Toast.LENGTH_SHORT).show();
    }

    private void copyErrorToClipboard(String e) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Error\n", e);
        clipboard.setPrimaryClip(clip);
    }
}
