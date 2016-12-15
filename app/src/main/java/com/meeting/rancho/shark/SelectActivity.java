package com.meeting.rancho.shark;

import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.InputStream;

public class SelectActivity extends Activity {
    public final static int RESULT_POSITIVE_CODE = 100;
    public final static int RESULT_NEGATIVE_CODE = 101;
    private Deck deck;
    private ImageView spade;
    private ImageView heart;
    private ImageView club;
    private ImageView diamond;
    private ImageView questionLeft;
    private ImageView cancel;
    private ImageView confirmation;
    private ImageView[] numbers;

    private ImageView selectedSuit;
    private ImageView selectedRank;
    private int selectedSuitDrawable;
    private int selectedRankDrawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        deck = (Deck) intent.getSerializableExtra("deck");

        setContentView(R.layout.activity_select);


        selectedSuit = questionLeft;
        selectedSuitDrawable = R.drawable.suit_5_0;

        selectedRank = numbers[13];
        selectedRankDrawable = R.drawable.number_15_black_checked;


        spade.setOnClickListener(new SuitOnClickListener(spade));

        heart.setOnClickListener(new SuitOnClickListener(heart));

        club.setOnClickListener(new SuitOnClickListener(club));

        diamond.setOnClickListener(new SuitOnClickListener(diamond));

        questionLeft.setOnClickListener(new SuitOnClickListener(questionLeft));

        for(int i = 0; i < 14; i ++)
            numbers[i].setOnClickListener(new NumberOnClickListener(numbers[i]));


        confirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int[] selectedCard = new int[2];
                int suit = selectedSuit.getId() - spade.getId() + 1;// 1 for spade, 2 for heart, 3 for club, 4 for diamond
                int rank = selectedRank.getId() - numbers[0].getId() + 2;
                rank = (rank == 15)?0:rank;
                suit = (suit == 5)?0:suit;
                selectedCard[0] = suit;
                selectedCard[1] = rank;

                if(deck.hasCard(selectedCard)) {
                    switch (deck.getFocus()) {
                        case 0:deck.dealOppo1(selectedCard);break;
                        case 1:deck.dealOppo2(selectedCard);break;
                        case 2:deck.dealFlop1(selectedCard);break;
                        case 3:deck.dealFlop2(selectedCard);break;
                        case 4:deck.dealFlop3(selectedCard);break;
                        case 5:deck.dealTurn(selectedCard);break;
                        case 6:deck.dealRiver(selectedCard);break;
                        case 7:deck.dealMy1(selectedCard);break;
                        case 8:deck.dealMy2(selectedCard);break;
                        default:break;
                    }
                }
                Intent intent = new Intent();
                Bundle data = new Bundle();
                data.putSerializable("deck", deck);
                intent.putExtras(data);
                setResult(RESULT_POSITIVE_CODE, intent);
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_NEGATIVE_CODE, intent);
                finish();
            }
        });

    }

    @Override
    public void setContentView(int layoutResId){
        super.setContentView(R.layout.activity_select);

        LinearLayout background = (LinearLayout) this.findViewById(R.id.background);

        spade = (ImageView) findViewById(R.id.spade);
        heart = (ImageView) findViewById(R.id.heart);
        club = (ImageView) findViewById(R.id.club);
        diamond = (ImageView) findViewById(R.id.diamond);
        questionLeft = (ImageView) findViewById(R.id.question_left);


        numbers = new ImageView[14];
        int numberStart = R.id.number_02;
        for(int i = 0; i < 14; i ++ )
            numbers[i] = (ImageView) findViewById(numberStart + i);


        cancel = (ImageView) findViewById(R.id.cancel);
        confirmation = (ImageView) findViewById(R.id.confirmation);

        background.setBackgroundDrawable(myGetDrawable(R.drawable.selectbg, this));

        spade.setImageDrawable(myGetDrawable(R.drawable.suit_1_1, this));
        heart.setImageDrawable(myGetDrawable(R.drawable.suit_2_1, this));
        club.setImageDrawable(myGetDrawable(R.drawable.suit_3_1,this));
        diamond.setImageDrawable(myGetDrawable(R.drawable.suit_4_1,this));
        questionLeft.setImageDrawable(myGetDrawable(R.drawable.suit_5_0, this));

        for(int i = 0; i < 13; i ++)
            numbers[i].setImageDrawable(myGetDrawable(R.drawable.number_02_blue + i * 7 ,this));
        numbers[13].setImageDrawable(myGetDrawable(R.drawable.number_15_blue_checked, this));

        cancel.setImageDrawable(myGetDrawable(R.drawable.cancel, this));
        confirmation.setImageDrawable(myGetDrawable(R.drawable.confirmation, this));



        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int cancelWidth = dp2px(this, 40);
        int cancelHeight = dp2px(this, 40);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(cancelWidth, cancelHeight);
        layoutParams.setMargins(0,0,screenWidth * 3 / 5,0);
        cancel.setLayoutParams(layoutParams);

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

    private int dp2px(Context context, float dpValue){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5f);
    }

    private int px2dp(Context context, float pxValue){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }

    protected void setSelectedView(){
        if(selectedSuit == null)
            return;
        if(selectedSuit == spade){
            for(int i = 0; i < 13; i ++){
                if(!deck.getCardSet()[i]){
                    int index = numbers[0].getId() + i;
                    ImageView v = (ImageView) this.findViewById(index);
                    int numberStartDrawble = R.drawable.number_02_black;
                    v.setImageDrawable(myGetDrawable(numberStartDrawble + 7 * i + 4 ,this));
                    //v.setImageResource(numberStartDrawble + 7 * i + 4);//set to color grey
                }
            }
        }else if(selectedSuit == heart){
            for(int i = 13; i < 26; i ++){
                if(!deck.getCardSet()[i]){
                    int index = numbers[0].getId() + i - 13;
                    ImageView v = (ImageView) this.findViewById(index);
                    int numberStartDrawble = R.drawable.number_02_black;
                    v.setImageDrawable(myGetDrawable(numberStartDrawble + 7 * (i - 13) + 4 ,this));
                    //v.setImageResource(numberStartDrawble + 7 * (i - 13) + 4);
                }
            }
        }else if(selectedSuit == club){
            for(int i = 26; i < 39; i ++){
                if(!deck.getCardSet()[i]){
                    int index = numbers[0].getId() + i - 26;
                    ImageView v = (ImageView) this.findViewById(index);
                    int numberStartDrawble = R.drawable.number_02_black;
                    v.setImageDrawable(myGetDrawable(numberStartDrawble + 7 * (i - 26) + 4 ,this));
                   // v.setImageResource(numberStartDrawble + 7 * (i - 26) + 4);
                }
            }
        }else if(selectedSuit == diamond){
            for(int i = 39; i < 52; i ++){
                if(!deck.getCardSet()[i]){
                    int index = numbers[0].getId() + i - 39;
                    ImageView v = (ImageView) this.findViewById(index);
                    int numberStartDrawble = R.drawable.number_02_black;
                    v.setImageDrawable(myGetDrawable(numberStartDrawble + 7 * (i - 39) + 4 ,this));
                    //v.setImageResource(numberStartDrawble + 7 * (i - 39) + 4);
                }
            }
        }
    }

    class NumberOnClickListener implements View.OnClickListener {
        ImageView v;
        private int index;
        private int drawableStart;
        private int selectedSuitInt;

        private NumberOnClickListener(ImageView v) {
            this.v = v;
            index = v.getId() - numbers[0].getId();
            drawableStart = R.drawable.number_02_black;
        }

        @Override
        public void onClick(View view) {
            selectedSuitInt = selectedSuit.getId() - spade.getId();
            Log.i("Suit Selected", "Selected Suit is " + Integer.toString(selectedSuit.getId() - spade.getId()));
            if(index != 13 && selectedSuitInt != 4) {
                if (!deck.getCardSet()[selectedSuitInt * 13 + index]) {
                    Log.i("Selected Log", "Card Set[" + Integer.toString(selectedSuitInt * 13 + index) + "] is " + Boolean.toString(deck.getCardSet()[selectedSuitInt * 13 + index]));
                    return;
                }
            }
            Log.i("tag", "case = " + selectedSuitInt);
            switch (selectedSuit.getId() - spade.getId()) {
                case 0:selectedRank.setImageDrawable(myGetDrawable((selectedRankDrawable - drawableStart) / 7 * 7 + drawableStart ,SelectActivity.this));break;
                case 1:selectedRank.setImageDrawable(myGetDrawable(5 + (selectedRankDrawable - drawableStart) / 7 * 7 + drawableStart, SelectActivity.this));break;
                case 2:selectedRank.setImageDrawable(myGetDrawable((selectedRankDrawable - drawableStart) / 7 * 7 + drawableStart, SelectActivity.this));break;
                case 3:selectedRank.setImageDrawable(myGetDrawable(5 + (selectedRankDrawable - drawableStart) / 7 * 7 + drawableStart, SelectActivity.this));break;
                case 4:selectedRank.setImageDrawable(myGetDrawable(2 + (selectedRankDrawable - drawableStart) / 7 * 7 + drawableStart, SelectActivity.this));break;
                default:break;
            }
            if (selectedSuit == spade || selectedSuit == club)
                selectedRankDrawable = drawableStart + 7 * index + 1;
            else if (selectedSuit == diamond || selectedSuit == heart)
                selectedRankDrawable = drawableStart + 7 * index + 6;
            else
                selectedRankDrawable = drawableStart + 7 * index + 3;
            selectedRank = v;
            v.setImageDrawable(myGetDrawable(selectedRankDrawable, SelectActivity.this));
        }
    }

    class SuitOnClickListener implements View.OnClickListener {
        ImageView v;
        private int index;//0 -3 means spade - diamond, 4 means question
        private int drawableStart;

        private SuitOnClickListener(ImageView v) {
            this.v = v;
            index = v.getId() - spade.getId();
            drawableStart = R.drawable.suit_1_0;
        }

        @Override
        public void onClick(View view) {
            int last = selectedSuit.getId() - spade.getId();
            int now = v.getId() - spade.getId();

            Log.i("last n now: ", "last = " + last + " now =" + now);

            if (Math.abs(last - now) == 1 || Math.abs(last - now) == 3 || (last == 4 && now != 4) || (last != 4 && now == 4)) {//Which needs to re-draw
                Log.i("Redraw", "Starts to redraw");
                int rankDrawableStart = R.drawable.number_02_black;
                for (int i = 0; i < 14; i++) {
                    if (now == 0 || now == 2) {
                        numbers[i].setImageDrawable(myGetDrawable(rankDrawableStart + 7 * i, SelectActivity.this));
                       // Log.i("Suit Tag", "Black Pressed. Loop:  " + i);
                    } else if (now == 1 || now == 3) {
                        numbers[i].setImageDrawable(myGetDrawable(rankDrawableStart + 7 * i + 5, SelectActivity.this));
                        //numbers[i].setImageResource(rankDrawableStart + 7 * i + 5);
                      //  Log.i("Tag", "Red Pressed. Loop:  " + i);
                    } else {
                        numbers[i].setImageDrawable(myGetDrawable(rankDrawableStart + 7 * i + 2, SelectActivity.this));
                      //  Log.i("Tag", "Blue Pressed. Loop:  " + i);
                    }
                }
            }

            if (selectedSuit != v) {
               // Log.i("Suit Tag", "selectedSuitDrawable = " + selectedSuitDrawable);
                selectedSuit.setImageDrawable(myGetDrawable(selectedSuitDrawable + 1, SelectActivity.this));
               // Log.i("Suit Tag", "selectedSuitDrawable + 1 = " + (selectedSuitDrawable + 1));
                selectedSuit = v;
                Log.i("Suit Selected", "Selected Suit is " + Integer.toString(selectedSuit.getId() - spade.getId()));
                selectedSuitDrawable = drawableStart + index * 2;//2 means every suit has 2 kinds drawble
                v.setImageDrawable(myGetDrawable(selectedSuitDrawable, SelectActivity.this));
                //v.setImageResource(selectedSuitDrawable);
            }

            setSelectedView();
        }

    }

}
