package com.meeting.rancho.shark;

import java.io.Serializable;



public class Deck implements Runnable, Serializable {
    private boolean[] cardSet;// Default : true, means available in the Deck.
    private int num;
    private int[][] my;
    private int[][] oppo;
    private int[][] pubCard;
    private int focus;
    private int[][] backUp;
    private boolean[] cardSetBackUp;
    private final static boolean DEBUG = false;
    private volatile int[] stat;
    private int loop;
    public static volatile int count = 0;


    //Deep Clone
    public Deck(Deck d){
        this.cardSet = new boolean[52];
        this.cardSetBackUp = new boolean[52];
        this.my = new int[2][2];
        this.oppo = new int[2][2];
        this.pubCard = new int[5][2];
        this.backUp = new int[10][2];
        this.stat = new int[13];

        this.cardSet = d.cardSet.clone();
        this.num = d.getNum();
        for(int i = 0; i < d.getMy().length; i ++)
            this.my[i] = d.getMy()[i].clone();
        for(int i = 0; i < d.getOppo().length; i ++)
            this.oppo[i] = d.getOppo()[i].clone();
        for(int i = 0; i < d.getPubCard().length; i ++)
            this.pubCard[i] = d.getPubCard()[i].clone();
        this.focus = d.getFocus();
        for(int i = 0; i < d.getBackUp().length; i ++)
            this.backUp[i] = d.getBackUp()[i].clone();
        this.cardSetBackUp = d.getCardSetBackUp().clone();
        this.stat = d.getStat().clone();
        this.loop =  d.getLoop();
    }

    public Deck(){
        this.cardSet = new boolean[52];
        this.cardSetBackUp = new boolean[52];
        this.num = 52;
        for(int i = 0; i < num; i ++)
            cardSet[i] = true;
        my = new int[2][2];
        oppo = new int[2][2];
        pubCard = new int[5][2];
        backUp = new int[10][2];
        this.stat = new int[13];
        loop = 1;
    }

    public Deck(int[] stat){
        this.cardSet = new boolean[52];
        this.cardSetBackUp = new boolean[52];
        this.num = 52;
        for(int i = 0; i < num; i ++)
            cardSet[i] = true;
        my = new int[2][2];
        oppo = new int[2][2];
        pubCard = new int[5][2];
        backUp = new int[10][2];
        if(stat.length != 13)
            this.stat = new int[13];
        else
            this.stat = stat;
        loop = 1;
    }


    public int getNum(){
        return this.num;
    }

    public int[][] getBackUp(){
        return this.backUp;
    }

    public boolean[] getCardSetBackUp(){
        return this.cardSetBackUp;
    }

    public void setFocus(int focus){
        this.focus = focus;
    }

    public boolean[] getCardSet(){
        return cardSet;
    }

    public int[][] getMy(){
        return my;
    }

    public int[][] getOppo(){
        return oppo;
    }

    public int[][] getPubCard(){
        return pubCard;
    }

    public int[] getFocusCard(){
        if(focus < 2)
            return oppo[focus];
        else if(focus < 7)
            return pubCard[focus - 2];
        else
            return my[focus - 7];
    }

    public int  getFocus(){
        return focus;
    }

    public void setLoop(int loop){
        this.loop = loop;
    }

    public int getLoop(){
        return loop;
    }

    public void dealMy1(int[] card){
        putBack(my[0]);
        my[0] = deal(card);
    }

    public void dealMy2(int[] card){
        putBack(my[1]);
        my[1] = deal(card);
    }

    public void dealOppo1(int[] card){
        putBack(oppo[0]);
        oppo[0] = deal(card);
    }

    public void dealOppo2(int[] card){
        putBack(oppo[1]);
        oppo[1] = deal(card);
    }

    public void dealFlop1(int[] card){
        putBack(pubCard[0]);
        pubCard[0] = deal(card);
    }

    public void dealFlop2(int[] card){
        putBack(pubCard[1]);
        pubCard[1] = deal(card);
    }

    public void dealFlop3(int[] card){
        putBack(pubCard[2]);
        pubCard[2] = deal(card);
    }

    public void dealTurn(int[] card){
        putBack(pubCard[3]);
        pubCard[3] = deal(card);
    }

    public void dealRiver(int[] card){
        putBack(pubCard[4]);
        pubCard[4] = deal(card);
    }

    public boolean hasCard(int[] card){
        int suit = card[0];
        int rank = card[1];
        if(suit != 0 && rank != 0 )
            return cardSet[suit * 13 + rank - 15];
        else
            return true;
    }

    //Only deal real card, cards like [0,12], [4,0] only return, but cannot really be dealt.
    protected int[] deal(int[] card) {
        int suit = card[0];
        int rank = card[1];
        if(suit != 0 && rank != 0 ) {
            if (cardSet[suit * 13 + rank - 15]) {
                cardSet[suit * 13 + rank - 15] = false;
                num--;
            }
        }
        return card;
    }

    protected int[] deal(int index){
        if(cardSet[index]){
            cardSet[index] = false;
            num --;
            int[] res = {index / 13 + 1, index % 13 + 2};
            return res;
        }else
            return null;
    }

    protected void putBack(int card[]){
        int suit = card[0];
        int rank = card[1];
        if(suit != 0 && rank != 0){
            cardSet[suit * 13 + rank -15] = true;
            num ++;
        }
    }

    public int getComplexity(){
        backUp();
        int complexity = 1;
        int[][] temp = new int[9][2];
        for(int i = 0; i < 9; i ++){
            temp[i][0] = backUp[i][0];
            temp[i][1] = backUp[i][1];
            complexity *= randomDeal(temp[i]);
        }
        rollBack();
        return complexity;
    }

    public void run(){
        count ++;
        for(int m = 0; m < loop; m ++){
            backUp();
            int[][] temp = new int[9][2];
            for(int i = 0; i < 9; i ++){
                temp[i][0] = backUp[i][0];
                temp[i][1] = backUp[i][1];
                randomDeal(temp[i]);
            }
            my[0] = temp[0];
            my[1] = temp[1];
            oppo[0] = temp[2];
            oppo[1] = temp[3];
            for(int i = 0; i < 5; i ++)
                pubCard[i] = temp[i + 4];
            if(DEBUG){
                for(int i = 0; i < 5; i ++)
                    System.out.print("(" + pubCard[i][0] + ", " + pubCard[i][1] +")");
            }


            ShowDown mySD = new ShowDown(my, pubCard);
            ShowDown oppoSD = new ShowDown(oppo, pubCard);



            int[] myDmnc = mySD.getDominance();
            if(DEBUG){
                System.out.print("\nI have (" + my[0][0]+", " + my[0][1]+ ") (" + my[1][0] + ", " + my[1][1]  + "). I got " + mySD.getOuts());
            }



            int[] oppoDmnc = oppoSD.getDominance();
            if(DEBUG){
                System.out.print(".\nHe has (" + oppo[0][0]+", " + oppo[0][1]+ ") (" + oppo[1][0] + ", " + oppo[1][1]  + "). He got " + oppoSD.getOuts());
            }


            int res = 1;
            if (myDmnc[0] > oppoDmnc[0])
                res = 0;
            else if (myDmnc[0] < oppoDmnc[0])
                res = 2;
            else if (myDmnc[0] == oppoDmnc[0]) {
                res = (myDmnc[1] > oppoDmnc[1]) ? 0 : ((myDmnc[1] == oppoDmnc[1]) ? 1 : 2);
            }
            rollBack();
            if(DEBUG){
                System.out.println(".\n" + (  (res == 0)?"I win.":((res == 2)?"I lose.":"Split.")));
            }
            setStat(res);
            setStat(myDmnc);
            //0:win, 1: split, 2: lose.
        }
        count--;
    }

    //Cards containing zero will be randomly dealt here.
    public int randomDeal(int card[]){
        int complexity = 1;
        while(card[0] == 0 && card[1] ==0){
            int index = (int) Math.floor(Math.random() * 52);
            if(!cardSet[index])
                continue;
            int[] res = deal(index);
            card[0] = res[0];
            card[1] = res[1];
            complexity *= num;
        }
        while(card[0] == 0){
            int suit = (int) Math.ceil(Math.random() * 4);
            int rank = card[1];
            if(!cardSet[suit*13 + rank - 15])
                continue;
            int[] res = deal(suit*13 + rank - 15);
            card[0] = res[0];
            card[1] = res[1];
            complexity *= 4;
        }
        while(card[1] == 0){
            int rank = (int) (Math.ceil(Math.random() * 13) + 1);
            int suit = card[0];
            if(!cardSet[suit*13 + rank - 15])
                continue;
            int[] res = deal(suit*13 + rank - 15);
            card[0] = res[0];
            card[1] = res[1];
            complexity *= 13;
        }
        return complexity;
    }

    public void backUp(){
        for(int i = 0; i < cardSet.length; i ++)
            cardSetBackUp[i] = cardSet[i];
        for(int j = 0; j < 2; j++){
            backUp[0][j] = my[0][j];
            backUp[1][j] = my[1][j];
            backUp[2][j] = oppo[0][j];
            backUp[3][j] = oppo[1][j];
        }
        for(int i = 0; i < 5; i ++){
            backUp[i + 4][0] = pubCard[i][0];
            backUp[i + 4][1] = pubCard[i][1];
        }
        backUp[9][0] = num;
        backUp[9][1] = focus;
    }

    public void rollBack(){
        for(int i = 0; i < cardSet.length; i ++)
            cardSet[i] = cardSetBackUp[i];
        for(int j  = 0; j < 2; j ++){
            my[0][j] = backUp[0][j];
            my[1][j] = backUp[1][j];
            oppo[0][j] = backUp[2][j];
            oppo[1][j] = backUp[3][j];
        }
        for(int i = 0; i < 5; i ++){
            pubCard[i][0] = backUp[i + 4][0];
            pubCard[i][1] = backUp[i + 4][1];
        }
        num = backUp[9][0];
        focus = backUp[9][1];
    }

    public void initStat(int[] stat){
        this.stat = stat;
    }



    public synchronized void setStat(int res){
        stat[res] ++;
        stat[3]++;
        //Log.i("setStat",Thread.currentThread().getId()+" stat changed. " + res);
    }

    synchronized void setStat(int[] dominance){
        stat[dominance[0]+4]++;
    }

        public synchronized int[] getStat(){
        return stat;
    }

}
