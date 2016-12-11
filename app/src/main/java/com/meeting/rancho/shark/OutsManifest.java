package com.meeting.rancho.shark;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.*;
import android.content.Intent;


public class OutsManifest extends Activity {

    private GridView grid;
    String[] outs = {"高牌","一对","两对","三条","顺子","同花","葫芦","四条","同花顺"};
    private Deck deck;
    private ImageView confirmation;
    private ImageView cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outs_manifest);
        Intent intent = this.getIntent();
        deck = (Deck) intent.getSerializableExtra("deck");
        int[] stat = deck.getStat();
        String[] probability = new String[10];
        String[] string = new String[20];
        string[0] = "我的成牌";
        string[1] =  "概率";
        for(int i = 0; i < 9; i ++) {
            probability[i] = Integer.toString((int) (100 * (stat[4 + i] * 1.0 / stat[3]))) + "%";
            string[2 * i + 2] = outs[i];
            string[2 * i + 3] = probability[i];
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.string, string);
        grid = (GridView) this.findViewById(R.id.grid);
        grid.setAdapter(adapter);
        confirmation = (ImageView) this.findViewById(R.id.confirmation_mani);
        cancel = (ImageView) this.findViewById(R.id.cancel_mani);
        confirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
