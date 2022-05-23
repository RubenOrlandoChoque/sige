package com.smartapps.sigev2.ui.shift;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.afollestad.materialdialogs.MaterialDialog;
import com.smartapps.sigev2.R;
import com.smartapps.sigev2.classes.SharedConfig;
import com.smartapps.sigev2.models.shifts.Shift;
import com.smartapps.sigev2.models.shifts.ShiftViewModel;
import com.smartapps.sigev2.ui.MainActivity;

import java.util.List;
import java.util.Set;

import static br.com.zbra.androidlinq.Linq.stream;

public class ShiftActivity extends AppCompatActivity {

    private ShiftViewModel shiftViewModel;
    private String origin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift);
        ((TextView) findViewById(R.id.txt_ab_title)).setText("Seleccionar Turno - "+ "Año: " + SharedConfig.getCurrentSchoolYear());

        origin = getIntent().getStringExtra("origin");
        if (origin == null) {
            origin = "";
        }

        Set<String> ids = SharedConfig.getUserShiftIds();
        if (ids.size() == 0) {
            findViewById(R.id.withoutShit).setVisibility(View.VISIBLE);
            findViewById(R.id.withShit).setVisibility(View.GONE);
        } else if (ids.size() == 1) {
            shiftViewModel = ViewModelProviders.of(this).get(ShiftViewModel.class);
            shiftViewModel.getAll().observe(this, new Observer<List<Shift>>() {
                @Override
                public void onChanged(@Nullable List<Shift> shifts) {
                    Shift found = stream(shifts).where(i -> Integer.toString(i.getId()).equals(stream(ids).first())).firstOrNull();
                    if (found != null) {
                        SharedConfig.setShiftId(found.getId());
                        SharedConfig.setShiftDescription(found.getDescription());
                        Intent i = new Intent(ShiftActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            });
        } else {
            findViewById(R.id.withoutShit).setVisibility(View.GONE);
            findViewById(R.id.withShit).setVisibility(View.VISIBLE);
            shiftViewModel = ViewModelProviders.of(this).get(ShiftViewModel.class);
            shiftViewModel.getAll().observe(this, new Observer<List<Shift>>() {
                @Override
                public void onChanged(@Nullable List<Shift> shifts) {
                    for (Shift shift : shifts) {
                        String found = stream(ids).where(i -> i.equals(Integer.toString(shift.getId()))).firstOrNull();
                        if (found != null) {
                            Button button = new Button(ShiftActivity.this);
                            final float scale = ShiftActivity.this.getResources().getDisplayMetrics().density;
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (270 * scale + 0.5f), (int) (100 * scale + 0.5f));
                            params.setMargins(0, 0, 0, (int) (10 * scale + 0.5f));
                            params.gravity = Gravity.CENTER;
                            button.setLayoutParams(params);
                            button.setBackground(getResources().getDrawable(R.drawable.button_bg_rounded_corner));
                            button.setTextSize(TypedValue.COMPLEX_UNIT_PT, 12);
                            button.setTextColor(getResources().getColor(R.color.white));
                            Typeface font = ResourcesCompat.getFont(ShiftActivity.this, R.font.opensans_semibold);
                            button.setTypeface(font);
                            button.setText(shift.getDescription());
                            button.setOnClickListener(v -> {
                                SharedConfig.setShiftId(shift.getId());
                                SharedConfig.setShiftDescription(shift.getDescription());
                                if (!origin.equals("MainActivity")) {
                                    Intent i = new Intent(ShiftActivity.this, MainActivity.class);
                                    startActivity(i);
                                }
                                finish();
                            });
                            ((LinearLayout) findViewById(R.id.container)).addView(button);
                        }
                    }
                }
            });
        }
    }

    public void onCloseSession(View view) {
        finish();
    }

    @Override
    public void onBackPressed() {
        new MaterialDialog.Builder(ShiftActivity.this)
                .title("Info")
                .content("¿Desea cerrar la aplicación?")
                .positiveText("Si")
                .negativeText("No")
                .onPositive((d, which) -> {
                    d.dismiss();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        super.finishAndRemoveTask();
                    } else {
                        super.finish();
                    }
                })
                .onNegative((d, which) -> d.dismiss())
                .show();
    }
}
