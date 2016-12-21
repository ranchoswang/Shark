package com.meeting.rancho.shark;


import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.content.DialogInterface;
import android.util.Log;
import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.io.InputStream;

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
    private TextView details;
    private ImageView delete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int[] stat = new int[13];
        deck = new Deck(stat);

        setContentView(R.layout.activity_main);





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
                if(randomRankIllegal()){
                    Toast.makeText(getApplicationContext(), "请输入合法的卡牌", Toast.LENGTH_LONG).show();
                    return;
                }
                if(pubRankIllegal()){
                    Toast.makeText(getApplicationContext(), "公共牌不支持随机牌", Toast.LENGTH_LONG).show();
                    return;
                }
                if(deck.getNum() == 52) {
                    Toast.makeText(getApplicationContext(), "请至少输入一张实际牌", Toast.LENGTH_LONG).show();
                    return;
                }else{
                    CalculateTask calTask = new CalculateTask();
                    calTask.execute("");
                    calculate.setEnabled(false);
                    delete.setEnabled(false);
                }

            }
        });

        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putSerializable("deck",deck);
                Intent intent = new Intent(MainActivity.this, OutsManifest.class);
                intent.putExtras(data);
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this).
                        setTitle("删除提示").
                        setMessage("是否确认要删除已选牌？").
                        setPositiveButton("确定",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                int[] stat = new int[13];
                                deck = new Deck(stat);
                                int startId = R.id.oppo1;
                                int startDrawable = R.drawable.card_back_01;
                                int shift;
                                for(int i = 0; i < 9; i ++){
                                    if(i < 2)
                                        shift = 0;
                                    else if(i < 5)
                                        shift = 1;
                                    else if(i < 7)
                                        shift = 2;
                                    else
                                        shift = 3;
                                    ImageView view = (ImageView) findViewById(startId + i + shift);
                                    view.setImageDrawable(myGetDrawable((startDrawable + i),MainActivity.this));
                                }
                                details.setEnabled(false);
                                details.setVisibility(View.INVISIBLE);
                                res.setText(R.string.select_please);
                            }
                        }).setNegativeButton("取消",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                            }
                }).show();
            }
        });
    }

    public BitmapDrawable myGetDrawable(int resId, Context context){
        try {
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inPreferredConfig = Bitmap.Config.RGB_565;
            opt.inPurgeable = true;
            opt.inInputShareable = true;
            InputStream is = context.getResources().openRawResource(resId);
            Bitmap bitmap = BitmapFactory.decodeStream(is, null, opt);
            return new BitmapDrawable(context.getResources(), bitmap);
        }catch (Exception e){
            Log.e("Close Exception", e.getMessage());
        }
        return null;
    }

    @Override
    public void setContentView(int layoutResId){
        super.setContentView(layoutResId);
        LinearLayout left = (LinearLayout) this.findViewById(R.id.left);
        LinearLayout right = (LinearLayout) this.findViewById(R.id.right) ;

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

        details = (TextView) this.findViewById(R.id.details);
        delete = (ImageView) this.findViewById(R.id.delete);



        left.setBackgroundDrawable(myGetDrawable(R.drawable.table_surface,this));
        right.setBackgroundDrawable(myGetDrawable(R.drawable.aside_bg,this));
        oppo1.setImageDrawable(myGetDrawable(R.drawable.card_back_01, this));
        oppo2.setImageDrawable(myGetDrawable(R.drawable.card_back_02,this));
        flop1.setImageDrawable(myGetDrawable(R.drawable.card_back_03,this));
        flop2.setImageDrawable(myGetDrawable(R.drawable.card_back_04,this));
        flop3.setImageDrawable(myGetDrawable(R.drawable.card_back_05,this));
        turn.setImageDrawable(myGetDrawable(R.drawable.card_back_06,this));
        river.setImageDrawable(myGetDrawable(R.drawable.card_back_07,this));
        my1.setImageDrawable(myGetDrawable(R.drawable.card_back_08,this));
        my2.setImageDrawable(myGetDrawable(R.drawable.card_back_09,this));
        delete.setImageDrawable(myGetDrawable(R.drawable.delete,this));
        calculate.setBackgroundDrawable(myGetDrawable(R.drawable.cal_button, this));

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

    protected boolean randomRankIllegal(){
        int[] suitmap = new int[15];
        for(int i = 0; i < 2; i ++)
            suitmap[deck.getMy()[i][1]] ++;
        for(int i = 0; i < 5; i ++)
            suitmap[deck.getPubCard()[i][1]] ++ ;
        for(int i = 0; i < 2; i ++)
            suitmap[deck.getOppo()[i][1]] ++ ;
        for(int i = 1; i < 15; i ++){
            if(suitmap[i] > 4 )
                return true;
        }
        return false;
    }

    protected boolean pubRankIllegal(){
        int[][] pubCard = deck.getPubCard();
        for(int i = 0; i < pubCard.length; i ++){
            int suit = pubCard[i][0];
            int rank = pubCard[i][1];
            if((suit == 0 && rank != 0) || (rank == 0 && suit != 0))
                return true;
        }
        return false;
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

        if(index == 0)
            v.setImageDrawable(myGetDrawable(R.drawable.card_back_01 + focus, this));
        else
            v.setImageDrawable(myGetDrawable((index + resStart),this));
    }

    private class CalculateTask extends AsyncTask<String, Integer, int[]>{
        @Override
        protected void onPreExecute(){
            yn.setText(R.string.started);
            res.setText(R.string.started);
            details.setEnabled(false);
            details.setVisibility(View.INVISIBLE);
            int[] newStat = new int[13];
            deck.initStat(newStat);
        }
        @Override
        protected int[] doInBackground(String... params) {
                int threadN = 5;
                Thread threads[] = new Thread[threadN];
                int complx = ((int) Math.log10(deck.getComplexity()) + 1);
                Log.i("Complx", "Compexity : " + complx);
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
                        publishProgress((int)(100 * (deck.getStat()[0] * 1.0 / deck.getStat()[3])));
//                        Log.i("Shark Completed",Integer.toOctalString(deck.getStat()[3]));
//                        Log.i("Shark Stat[0]", Integer.toOctalString(stat[0]));
//                        Log.i("Shark ratio", Integer.toOctalString(stat[0] / (deck.getStat()[3])/ 100));
                    }catch (Exception e){
                        Log.e("Progress Exception", e.getMessage());
                    }
                }
                return stat;
        }
        @Override
        protected void onProgressUpdate(Integer... progress){
            String text = progress[0] + "%...";
            res.setText(text);
            yn.setText("计算中...");
        }
        @Override
        protected void onPostExecute(int[] result){
            String text = (int)Math.round(100*(result[0] * 1.0 /result[3])) + "%";
            res.setText(text);
            //Log.i("Shark Final result", result[0] + " wins and "+ result[1]+" splits and " + result[2] +" loses out of " + result[3]);
            String potS = potText.getText().toString();
            String callS = callText.getText().toString();
            calculate.setEnabled(true);
            delete.setEnabled(true);
            Deck.count = 0;
            yn.setText(R.string.yorn);
            details.setVisibility(View.VISIBLE);
            details.setEnabled(true);
            if(potS.equals("") || callS.equals(""))
                return;
            if(potS.length() > 9 || callS.length() > 9){
                Toast.makeText(getApplicationContext(),"底池或者跟注数字太大",Toast.LENGTH_LONG).show();
                return;
            }
            double pot = Integer.parseInt(potS);
            double call = Integer.parseInt(callS);
            Log.i("result info", pot+ " " + result[0]+ " " + call +" " + result[2] + " " + (pot * result[0] * 1.0 - call * result[2] * 1.0));
            if(pot * result[0] - call * result[2] > 0)
                yn.setText("跟吧！");
            else
                yn.setText("弃！");

        }
    }
}
