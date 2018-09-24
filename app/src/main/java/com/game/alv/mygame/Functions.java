package com.game.alv.mygame;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public final class Functions {

    //帮助模式的刷新地图
    public static void newHelpMap(Monster[] m, Player p){
        //只显示一只怪物
        for(int i = 1; i<m.length; i++){
            m[i] = new Monster(-1, 0);
        }

        //人物所在地图除以三取余数得到怪物种类
        m[0] = new Monster(13, p.getMap()%3);
    }

    //更新地图
    public static void newMap(Monster[] m, Player p){
        //刷新怪物
        for(int i = 0; i<m.length; i++){
            m[i] = new Monster();
            for(int j = 0; j<i; j++){
                if( m[i].getPlace()!=-1 && m[j].getPlace() == m[i].getPlace()){
                    m[i] = new Monster();
                }
            }
        }

        //设置玩家位置
        p.setPlace(0);
    }

    //刷新界面的函数
    public static void showPlayerAndMonster(ImageView[] place, Monster[] m, Player p) {
        //消除其他图片
        for(int i = 0; i<14; i++){
            //清空人物图像
            place[i].setImageBitmap(null);
            //清空怪物图像
            place[i].setBackground(null);
        }
        //显示现在新的图片
        for (Monster aM : m) {
            if (aM.getPlace() >= 0) {
                //将怪物图像设置为背景,使得人物可以和怪物共存
                if(aM.isHitting()){
                    switch (aM.getType()) {
                        case 0:
                            place[aM.getPlace()].setBackgroundResource(R.drawable.monster_normal_hitting);

                            break;
                        case 1:
                            place[aM.getPlace()].setBackgroundResource(R.drawable.monster_fire_hitting);
                            break;
                        case 2:
                            place[aM.getPlace()].setBackgroundResource(R.drawable.monster_poison_hitting);
                            break;
                    }
                }else{
                    switch (aM.getType()) {
                        case 0:
                            place[aM.getPlace()].setBackgroundResource(R.drawable.monster_normal);
                            break;
                        case 1:
                            place[aM.getPlace()].setBackgroundResource(R.drawable.monster_fire);
                            break;
                        case 2:
                            place[aM.getPlace()].setBackgroundResource(R.drawable.monster_poison);
                            break;
                    }
                }
            }
        }

        if(p.getDirection() == 1){
            if(p.isDefense()){
                //显示人物
                place[p.getPlace()].setImageResource(R.drawable.player_define);
            }else{
                place[p.getPlace()].setImageResource(R.drawable.player_noweapon);
            }
        }else{
            if(p.isDefense()){
                //显示人物
                place[p.getPlace()].setImageResource(R.drawable.player_define_back);
            }else{
                place[p.getPlace()].setImageResource(R.drawable.player_noweapon_back);
            }
        }


    }

    //更新系统状态栏信息
    public static void showInformation(Player p, int motivationTime, TextView txtMap, TextView txtPlayer, TextView txtGoods, TextView txtSystem, TextView txtTips){

        //显示地图信息
        CharSequence sMap = "Position:" + p.getMap() + "," + p.getPlace();
        txtMap.setText(sMap);
        //显示人物属性和金钱
        //获取中毒状态
        String sStatus;
        if(p.isPoison()){
            sStatus = "Poison";
        }else{
            sStatus = "None";
        }
        CharSequence sPlayer = "HP:" + p.getHp() + "+" + p.getArmor() + "[" + sStatus + "]";
        txtPlayer.setText(sPlayer);
        //显示人物物品信息
        //获取人物武器信息
        String sWeapon = null;
        switch (p.getWeapon()){
            case 0:
                sWeapon = "None";
                break;
            case 1:
                sWeapon = "Knife";
                break;
            case 2:
                sWeapon = "Sword";
                break;
            case 3:
                sWeapon = "Bow";
                break;
        }
        CharSequence sGoods = "Weapon:" + sWeapon + ", Money:" + p.getMoney() + ", Special:" + p.getSpecial();

        txtGoods.setText(sGoods);
        //显示人物行动次数
        CharSequence sMovTime = "Move:" + motivationTime;
        txtSystem.setText(sMovTime);
        //显示底部提示栏
        if(motivationTime>0){
            CharSequence sTips = "Now is your turn:" + motivationTime;
            txtTips.setText(sTips);
        }else{
            CharSequence sTips = "Now is monsters turn.";
            txtTips.setText(sTips);
        }
    }

    //算出人物行动次数
    public static int resetMotivationTime(){

        Random random = new Random();
        //不同的行动次数有不同的概率
        int motivationTime = (random.nextInt(3) + 1);
        int ir = random.nextInt(100);
        if(ir < 5){
            motivationTime = 0;
        }else if(ir > 95) {
            motivationTime *= 2;
        }else if(ir >90 && ir<95){
            motivationTime +=1;
        }else if(ir >85 && ir<90){
            motivationTime -=1;
        }
        return motivationTime;
    }

}
