package com.meeting.rancho.shark;

/**
 * Created by Administrator on 2016/12/3.
 */

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by Administrator on 2016/10/31.
 */
public class ShowDown {
    private int[][] cards;
    private int[] suits;
    private int[] rank;
    private int[] dominance;
    private int[] rankmap;
    private int[] suitmap;
    private static String StraightFlush =  "Straight-Flush";
    private static String Quads = "Quads";
    private static String Fullhouse = "Full-House";
    private static String Flush = "Flush";
    private static String Straight = "Straight";
    private static String Three = "Three of One Kind";
    private static String TwoPairs = "Two Pairs";
    private static String OnePair = "One Pair";
    private static String HighCard = "High Card";
    private String outs;

    public ShowDown(int[][] player, int[][] cards){
        int[][] allcards = new int[7][2];
        int i = 0;
        for(; i < 2; i ++){
            allcards[i] = player[i];
        }
        for(; i < 7; i ++){
            allcards[i] = cards[i - 2];
        }
        suits = new int[7];//suit ranges from 1 to 4;
        rank = new int[7];// rank ranges from 2 to 14
        this.cards  = allcards;
        i = 0;
        for(int[] card : allcards){
            suits[i] = card[0];
            rank[i] = card[1];
            i ++;
        }

        rankmap = new int[13];
        for(int n : rank){
            rankmap[n - 2] ++; //rankmap[0-12] refers to the rank's appearance from 2 - 14
        }

        suitmap = new int[4];
        for(int n : suits){
            suitmap[n - 1] ++; //suitmap[0 - 3] refers
        }

        dominance = new int[2];// dominance refers to "kind + rank "
    }

    public int[] getDominance(){
        if(isStraightFlush()){
            outs = StraightFlush;
            return dominance;
        }
        if(isQuads()){
            outs = Quads;
            return dominance;
        }else if(isFullHouse()){
            outs = Fullhouse;
            return dominance;
        }else if(isFlush()){
            outs = Flush;
            return dominance;
        }else if(isStraight()){
            outs = Straight;
            return dominance;
        }else if(isThree()){
            outs = Three;
            return dominance;
        }else if(isTwoPair()){
            outs = TwoPairs;
            return dominance;
        }else if(isOnePair()){
            outs = OnePair;
            return dominance;
        }
        isHighCard();
        outs = HighCard;

        return dominance;
    }
    public boolean isStraightFlush(){
        int suit = -1;
        for(int i = 0; i < suitmap.length; i ++)
            if(suitmap[i] >= 5)
                suit = i;
        HashSet<Integer> set = new HashSet<>();
        for(int i = 0; i < rank.length; i ++){
            if(suits[i] - 1 == suit)
                set.add(rank[i]);
        }
        for(int i = 2; i < 11 ; i++){
            boolean res;
            if(res = set.contains(i)){
                for(int j = 1; j < 5; j ++)
                    res = res & set.contains(i + j);
                if(res) {
                    dominance[0] = 8;
                    dominance[1] = i;
                }
            }
        }
        if(set.contains(14) & set.contains(2) & set.contains(3) & set.contains(4) & set.contains(5)){
            dominance[0] = 8;
            dominance[1] = Math.max(1, dominance[1]);
        }
        if(dominance[0] == 8)
            return true;
        if(dominance[0] != 0)
            System.out.print("error\n");
        return false;

    }

    public boolean isQuads(){
        int quads = -1;
        int max = 0;
        for(int i = 0 ; i < 13; i ++){
            if(rankmap[i] > 0 && rankmap[i] < 4){  // find the max card other than the quads
                max = i;
            } else if(rankmap[i] == 4){
                dominance[0] = 7;
                quads = i; //find the quads, i = 0 -12 stands for 2 - 14.
                break;
            }
        }
        if(quads >= 0) {
            dominance[1] = quads * 13 + max;//quads rank = quids * 13 + max ranges from (2 * 13 + 3) to (14 * 13 + 13).
            return true;
        }
        if(dominance[0] != 0)
            System.out.print("error\n");
        return false;
    }

    public boolean isFullHouse(){
        int fullhouse3 = -1;
        int fullhouse2 = -1;
        for(int i  = 0; i < rankmap.length; i ++){
            if(rankmap[i] == 2){
                fullhouse2 = i;
                /* find the pair,
                 * notice that this "if" case may be used twice,
                 * because rankmap's index is increasing ,
                 * therefore former smaller pair could be replaced by the new bigger pair.
                 * Consider 44QQK + KK.
                 */
            }
            else if(fullhouse3 == -1 && rankmap[i] == 3){
                fullhouse3 = i;// find the three of a kind
            }else if(fullhouse3 > 0 && rankmap[i] == 3){
                fullhouse2 = fullhouse3;// this is when 331 happens, for e.g. 4777T + TT
                fullhouse3 = i;
            }
        }
        if(fullhouse2 >=0 && fullhouse3 >= 0){
            dominance[0] = 6;
            dominance[1] = fullhouse3 * 13 + fullhouse2;// Fullhouse rank ranges from (0 * 13 + 1) to (12 * 13 + 11).
            return true;
        }
        if(dominance[0] != 0)
            System.out.print("error\n");
        return false;
    }

    public boolean isFlush(){
        int suit = -1;
        int[] newrank = new int[5];
        int index = 0; // index of initializing new rank
        for(int i = 0; i < 4; i ++){
            if(suitmap[i] > 4){
                dominance[0] = 5;
                suit = i;
                break;
            }
        }
        if(suit == -1) {
            if(dominance[0] != 0)
                System.out.print("error\n");
            return false;
        }

        for(int i = 0; i < 7; i ++){
            if(suits[i] == suit) {
                newrank[index++] = rank[i];
            }
        }
        Arrays.sort(newrank);
        for(int i = newrank.length - 1; i > newrank.length - 6; i --){
            dominance[1] = dominance[1] * 13 + newrank[i];
        }
        return true;
    }

    public boolean isStraight(){
        HashSet<Integer> set = new HashSet<Integer>();
        for(int i : rank ){
            set.add(i);// set contains 2 to 14
        }
        for(int i = 2; i < 11 ; i++){
            boolean res;
            if(res = set.contains(i)){
                for(int j = 1; j < 5; j ++)
                    res = res & set.contains(i + j);
                if(res) {
                    dominance[0] = 4;
                    dominance[1] = i;
                }
            }
        }
        if(set.contains(14) & set.contains(2) & set.contains(3) & set.contains(4) & set.contains(5)){
            dominance[0] = 4;
            dominance[1] = Math.max(1, dominance[1]);
        }
        if(dominance[0] == 4)
            return true;
        if(dominance[0] != 0)
            System.out.print("error\n");
        return false;
    }

    public boolean isThree(){
        int three =  -1;
        int[] ones = new int[7];
        int index = 0;
        for(int i = 0; i < rankmap.length; i ++){
            if(rankmap[i] == 3){
                /*
                 * We do not consider if you are holding a fullhouse a something else containing
                 * three, because we believe such priority problem is solved when this method is called.
                 */
                three = i;
            } else if(rankmap[i] == 1){
                ones[index ++] = i;
            }
        }
        if(three == -1) {
            if(dominance[0] != 0)
                System.out.print("error\n");
            return false;
        }
        Arrays.sort(ones);
        dominance[0] = 3;
        dominance[1] = three * 169 + ones[6] * 13 + ones[5];
        return true;

    }

    public boolean isTwoPair(){
        int max = -1;
        int spair = -1, bpair = -1;
        for(int i = 0; i < rankmap.length; i ++){
            if(rankmap[i] == 2){
                spair = bpair;
                bpair = i;
            }else if(rankmap[i] == 1){
                max = i;
            }
        }
        if(spair == -1) {
            if(dominance[0] != 0)
                System.out.print("error\n");
            return false;
        }
        dominance[0] = 2;
        dominance[1] = bpair * 169 + spair * 13 + max;
        return true;
    }

    public boolean isOnePair(){
        int pair = -1;
        int[] ones = new int[7];
        int index = 0;
        for(int i = 0; i < rankmap.length; i ++){
            if(rankmap[i] == 2){
                pair = i;
            }else  if(rankmap[i] == 1){
                ones[index ++] = i;
            }
        }
        if(pair == -1) {
            if(dominance[0] != 0)
                System.out.print("error\n");
            return false;
        }
        Arrays.sort(ones);
        dominance[0] = 1;
        dominance[1] = pair;
        for(int i = 0; i < 3; i ++){
            dominance[1] = dominance[1] * 13 + ones[ones.length - i - 1];
        }
        return true;
    }

    public boolean isHighCard(){
        int[] ones = new int[7];
        int index = 0;
        for(int i = 0; i < rankmap.length; i ++){
            if(rankmap[i] == 1){
                ones[index ++] = i;
            }
        }
        dominance[0] = 0;
        Arrays.sort(ones);
        for(int i = ones.length - 1; i >= 0; i --){
            dominance[1] = dominance[1] * 13 + ones[i];
        }
        return true;
    }
    public String getOuts(){
        if(outs != null)
            return this.outs;
        else
            return "Initializing Error";
    }

}

