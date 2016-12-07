package com.meeting.rancho.shark;


import android.os.AsyncTask;
import android.util.Log;
import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {
    private  Deck deck;
    public final static int OPPO1_REQUEST_CODE = 0;
    public final static int OPPO2_REQUEST_CODE = 1;
    public final static int FLOP1_REQUEST_CODE = 2;
    public final static int FLOP2_REQUEST_CODE = 3;
    public final static int FLOP3_REQUEST_CODE = 4;
    public final static int TURN_REQUEST_CODE = 5;
    public final static int RIVER_REQUEST_CODE = 6;
    public final static int MY1_REQUEST_CODE = 7;
    public final static int MY2_REQUEST_CODE = 8;
    private ImageView oppo1;
    private ImageView oppo2;
    private ImageView flop1;
    private ImageView flop2;
    private ImageView flop3;
    private ImageView turn;
    private ImageView river;
    private ImageView my1;
    private ImageView my2;
    private Button calculate;
    private TextView res;
    private TextView yn;
    private EditText potText;
    private EditText callText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int[] stat = new int[3];
        deck = new Deck(stat);
        setContentView(R.layout.activity_main);

        oppo1 = (ImageView) this.findViewById(R.id.oppo1);
        oppo2 = (ImageView) this.findViewById(R.id.oppo2);
        flop1 = (ImageView) this.findViewById(R.id.flop1);
        flop2 = (ImageView) this.findViewById(R.id.flop2);
        flop3 = (ImageView) this.findViewById(R.id.flop3);
        turn = (ImageView) this.findViewById(R.id.turn);
        river = (ImageView) this.findViewById(R.id.river);
        my1 = (ImageView) this.findViewById(R.id.my1);
        my2 = (ImageView) this.findViewById(R.id.my2);

        calculate = (Button) this.findViewById(R.id.calculate);
        res = (TextView) this.findViewById(R.id.res);
        yn = (TextView) this.findViewById(R.id.yn);

        potText = (EditText) this.findViewById(R.id.pot);
        callText = (EditText) this.findViewById(R.id.call) ;





        oppo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                deck.setFocus(OPPO1_REQUEST_CODE);
                data.putSerializable("deck", deck);
                Intent intent = new Intent(MainActivity.this, SelectActivity.class);
                intent.putExtras(data);
                startActivityForResult(intent, OPPO1_REQUEST_CODE);
            }
        });

        oppo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                deck.setFocus(OPPO2_REQUEST_CODE);
                data.putSerializable("deck", deck);
                Intent intent = new Intent(MainActivity.this, SelectActivity.class);
                intent.putExtras(data);
                startActivityForResult(intent, OPPO2_REQUEST_CODE);
            }
        });

        flop1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                deck.setFocus(FLOP1_REQUEST_CODE);
                data.putSerializable("deck", deck);
                Intent intent = new Intent(MainActivity.this, SelectActivity.class);
                intent.putExtras(data);
                startActivityForResult(intent, FLOP1_REQUEST_CODE);
            }
        });

        flop2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                deck.setFocus(FLOP2_REQUEST_CODE);
                data.putSerializable("deck", deck);
                Intent intent = new Intent(MainActivity.this, SelectActivity.class);
                intent.putExtras(data);
                startActivityForResult(intent, FLOP2_REQUEST_CODE);
            }
        });

        flop3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                deck.setFocus(FLOP3_REQUEST_CODE);
                data.putSerializable("deck", deck);
                Intent intent = new Intent(MainActivity.this, SelectActivity.class);
                intent.putExtras(data);
                startActivityForResult(intent, FLOP3_REQUEST_CODE);
            }
        });

        turn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                deck.setFocus(TURN_REQUEST_CODE);
                data.putSerializable("deck", deck);
                Intent intent = new Intent(MainActivity.this, SelectActivity.class);
                intent.putExtras(data);
                startActivityForResult(intent, TURN_REQUEST_CODE);
            }
        });

        river.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                deck.setFocus(RIVER_REQUEST_CODE);
                data.putSerializable("deck", deck);
                Intent intent = new Intent(MainActivity.this, SelectActivity.class);
                intent.putExtras(data);
                startActivityForResult(intent, RIVER_REQUEST_CODE);
            }
        });

        my1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                deck.setFocus(MY1_REQUEST_CODE);
                data.putSerializable("deck", deck);
                Intent intent = new Intent(MainActivity.this, SelectActivity.class);
                intent.putExtras(data);
                startActivityForResult(intent, MY1_REQUEST_CODE);
            }
        });

        my2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                deck.setFocus(MY2_REQUEST_CODE);
                data.putSerializable("deck", deck);
                Intent intent = new Intent(MainActivity.this, SelectActivity.class);
                intent.putExtras(data);
                startActivityForResult(intent, MY2_REQUEST_CODE);
            }
        });

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deck.getNum() != 52) {
                    CalculateTask calTask = new CalculateTask();
                    calTask.execute("");
                    calculate.setEnabled(false);
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        if(resultCode == SelectActivity.RESULT_POSITIVE_CODE){
            deck = (Deck) intent.getSerializableExtra("deck");
            Log.i("Set Focus View", "Focus :" + Integer.toString(deck.getFocus()));
            Log.i("Set Focus View", "Card :" + Integer.toString(deck.getFocusCard()[0]) + Integer.toString(deck.getFocusCard()[1]));
            setFocusView(deck);
        }
    }

    public void setFocusView(Deck deck) {
        int focus = deck.getFocus();
        int[] card = deck.getFocusCard();
        int start = R.id.oppo1;
        int shift;
        if(focus < 2)
            shift = 0;
        else if(focus < 5)
            shift = 1;
        else if(focus < 7)
            shift = 2;
        else
            shift = 3;
        Log.i("Find View Id", "View Id " + Integer.toHexString(start + focus + shift));
        ImageView v = (ImageView) findViewById(start + focus + shift);
        int resStart = R.drawable.card_0_0;
        int index;
        if (card[1] != 0)
            index = card[0] * 14 + card[1] - 1;
        else
            index = card[0] * 14;
        v.setImageResource(index + resStart);
    }

    private class CalculateTask extends AsyncTask<String, Integer, int[]>{
        @Override
        protected void onPreExecute(){
            yn.setText("Started");
            res.setText("Started");
        }
        @Override
        protected int[] doInBackground(String... params){
            if(deck.getNum() != 52){
                int threadN = 5;
                Thread threads[] = new Thread[threadN];
                int complx = ((int) Math.log10(deck.getComplexity()) + 1);
                int basicLoop = 10000;
                deck.setLoop(basicLoop * complx);
                int[] stat = deck.getStat();
                for(int i = 0; i < threadN; i ++){
                    Deck cpyDeck = new Deck(deck);
                    cpyDeck.initStat(stat);
                    threads[i] = new Thread(cpyDeck);
                    threads[i].start();
                }
                while(Deck.count == 0);
                while(Deck.count != 0){
                    try {
                        Thread.sleep(200);
                        publishProgress((int)(100 * (stat[0] * 1.0 / (stat[0] + stat[1] + stat[2]))));
                        Log.i("Shark Completed",Integer.toOctalString(Deck.completed));
                        Log.i("Shark Stat[0]", Integer.toOctalString(stat[0]));
                        Log.i("Shark ratio", Integer.toOctalString(stat[0] / (Deck.completed / 100)));
                    }catch (Exception e){
                        Log.e("Progress Exception", e.getMessage());
                    }
                }
                int[] result = new int[4];
                for(int i = 0; i < 3; i ++)
                    result[i] = stat[i];
                result[3] = result[0] + result[1] + result[2];
                return result;
            }else {
                Toast.makeText(getApplicationContext(), "请至少输入一张实际牌", Toast.LENGTH_LONG).show();
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... progress){
            res.setText(progress[0] + "%...");
            yn.setText("Calculating");
        }
        @Override
        protected void onPostExecute(int[] result){
            res.setText((int)(100*(result[0] * 1.0 /result[3])) + "%");
            Log.i("Shark Final result", result[0] + " wins and "+ result[1]+" splits and " + result[2] +" loses out of " + result[3]);
            int[] newStat = new int[3];
            deck.initStat(newStat);
            String potS = potText.getText().toString();
            String callS = callText.getText().toString();
            calculate.setEnabled(true);
            Deck.count = 0;
            Deck.completed = 0;
            yn.setText("Y or N");
            if(potS.equals("") || callS.equals(""))
                return;
            if(potS.length() > 9 || callS.length() > 9){
                Toast.makeText(getApplicationContext(),"底池或者跟注数字太大",Toast.LENGTH_LONG).show();
                return;
            }
            int pot = Integer.parseInt(potS);
            int call = Integer.parseInt(callS);
            if(pot * result[0] - call * result[2] > 0)
                yn.setText("跟吧！");
            else
                yn.setText("弃！");

        }
    }
}
