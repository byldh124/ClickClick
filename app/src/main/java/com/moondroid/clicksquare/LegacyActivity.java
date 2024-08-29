package com.moondroid.clicksquare;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.moondroid.clicksquare.R;

import java.util.ArrayList;
import java.util.Collections;

public class LegacyActivity extends AppCompatActivity {

    ImageView btnStart;

    TextView textView_stage, textView_num, timer;

    ImageView[][] stageBtns = new ImageView[7][];

    Button reset;

    LinearLayout stage00;
    LinearLayout[] stages = new LinearLayout[7];

    ArrayList<Integer> list = new ArrayList<>();

    int stage = 0;
    int num = 1;
    int btnSum;
    int[] plz = new int[]{0, 1, 5, 14, 30, 55, 91};
    long myBaseTime;
    long myPauseTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legacy);

        btnStart = findViewById(R.id.btnStart);

        reset = findViewById(R.id.reset);

        stage00 = findViewById(R.id.stage00);
        textView_stage = findViewById(R.id.textView_Stage);
        textView_num = findViewById(R.id.textView_num);
        for (int i = 0; i < stages.length; i++) {
            stages[i] = findViewById(R.id.stage01 + i);
        }


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stage00.setVisibility(View.GONE);
                stages[0].setVisibility(View.VISIBLE);
                stageBtns[stage] = new ImageView[(stage + 1) * (stage + 1)];
                timer = findViewById(R.id.timer);

                stageBtns[stage][0] = findViewById(R.id.btn0101);
                stageBtns[stage][0].setImageResource(R.drawable.number_01);
                stageBtns[stage][0].setTag(R.drawable.number_01);
                textView_stage.setVisibility(View.VISIBLE);
                textView_stage.setText("stage" + (stage + 1));
                textView_num.setVisibility(View.VISIBLE);
                timer.setVisibility(View.VISIBLE);
                textView_num.setText(num + "");

                myBaseTime = SystemClock.elapsedRealtime();
                System.out.println(myBaseTime);
                myTimer.sendEmptyMessage(0);
                reset.setVisibility(View.VISIBLE);
            }
        });
    }

    public void clickNum(View v) {

        ImageView iv = (ImageView) v;

        String s = iv.getTag().toString();

        int check = Integer.parseInt(s) - (R.drawable.number_01) + 1;

        if (check == num && check != (stage + 1) * (stage + 1)) {
            iv.setVisibility(View.INVISIBLE);
            num++;
            textView_num.setText(num + "");
        }


        //마지막 버튼 눌렀을때

        if (stage < 5 && check == (stage + 1) * (stage + 1) && check == num) {
            try {
                stages[stage].setVisibility(View.GONE); // stage01
                stage++;
                btnSum = (stage + 1) * (stage + 1);
                stages[stage].setVisibility(View.VISIBLE); // stage02

                stageBtns[stage] = new ImageView[btnSum];

                list.clear();

                for (int i = 0; i < btnSum; i++) {
                    list.add(i);
                }
                Collections.shuffle(list);

                for (int i = 0; i < btnSum; i++) {
                    stageBtns[stage][i] = findViewById(R.id.btn0101 + plz[stage] + i);
                    stageBtns[stage][i].setImageResource(R.drawable.number_01 + list.get(i));
                    stageBtns[stage][i].setTag(R.drawable.number_01 + list.get(i));
                }
                num = 1;
                textView_stage.setText("stage" + (stage + 1));
                textView_num.setText(num + "");

            } catch (Exception e) {

            }

        }

        if (stage >= 5 && check == (stage + 1) * (stage + 1) && check == num) {

            for (int i = 0; i < stages.length; i++) {
                stages[i].setVisibility(View.GONE);
            }

            stages[6].setVisibility(View.VISIBLE);
            textView_stage.setVisibility(View.GONE);
            textView_num.setVisibility(View.GONE);

            myTimer.removeMessages(0);
            myPauseTime = SystemClock.elapsedRealtime();
            timer.setText(timer.getText().toString());
            reset.setVisibility(View.INVISIBLE);

        }


    }

    public void clickFinish(View v) {
        for (int i = 0; i < stages.length; i++) {
            stages[i].setVisibility(View.GONE);
        }

        stage00.setVisibility(View.VISIBLE);
        textView_stage.setVisibility(View.GONE);

        for (int i = 0; i < stageBtns.length - 1; i++) {
            for (int j = 0; j < stageBtns[i].length; j++) {
                stageBtns[i][j].setVisibility(View.VISIBLE);
            }
        }

        stage = 0;
        num = 1;


        myTimer.removeMessages(0);
        timer.setText("00:00:00");
        timer.setVisibility(View.INVISIBLE);
    }

    Handler myTimer = new Handler() {
        public void handleMessage(android.os.Message msg) {
            timer.setText(getTimeOut());

            myTimer.sendEmptyMessage(0);
        }
    };


    public String getTimeOut() {
        long now = SystemClock.elapsedRealtime();
        long outTime = now - myBaseTime;
        String easy_outTime = String.format("%02d:%02d:%02d", outTime / 1000 / 60, (outTime / 1000) % 60, (outTime % 1000) / 10);
        return easy_outTime;
    }

    public void clickBtn(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("처음으로 돌아갈까요?");
        myTimer.removeMessages(0);
        myPauseTime = SystemClock.elapsedRealtime();
        timer.setText(timer.getText().toString());
        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < stages.length; i++) {
                    stages[i].setVisibility(View.GONE);
                }

                stage00.setVisibility(View.VISIBLE);
                textView_stage.setVisibility(View.GONE);
                textView_num.setVisibility(View.INVISIBLE);

                for (int i = 0; i < stage; i++) {
                    for (int j = 0; j < stageBtns[i].length; j++) {
                        stageBtns[i][j].setVisibility(View.VISIBLE);
                    }
                }

                stage = 0;
                num = 1;
                myTimer.removeMessages(0);
                timer.setText("00:00:00");
                timer.setVisibility(View.INVISIBLE);
                reset.setVisibility(View.INVISIBLE);
            }
        });

        builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                textView_num.setText(num + "");

                long now = SystemClock.elapsedRealtime();
                myTimer.sendEmptyMessage(0);
                myBaseTime += (now - myPauseTime);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}