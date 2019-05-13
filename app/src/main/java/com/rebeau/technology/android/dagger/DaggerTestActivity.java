package com.rebeau.technology.android.dagger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.rebeau.technology.R;
import com.rebeau.technology.android.dagger.activitys.method_inject.FourActivity;
import com.rebeau.technology.android.dagger.activitys.one_mvp.OneActivity;
import com.rebeau.technology.android.dagger.activitys.three_absdagger.ThreeActivity;
import com.rebeau.technology.android.dagger.activitys.two_mvp_dagger.TwoActivity;
import com.rebeau.technology.android.dagger.fragments.TasksActivity;


public class DaggerTestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dagger_test);

        findViewById(R.id.show_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(DaggerTestActivity.this, OneActivity.class));

            }
        });
        findViewById(R.id.show_dialog1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DaggerTestActivity.this, TwoActivity.class));
            }
        });
        findViewById(R.id.show_dialog2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DaggerTestActivity.this, ThreeActivity.class));

            }
        });
        findViewById(R.id.show_dialog3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DaggerTestActivity.this, FourActivity.class));

            }
        });
        findViewById(R.id.show_dialog4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DaggerTestActivity.this, TasksActivity.class));

            }
        });
    }
}
