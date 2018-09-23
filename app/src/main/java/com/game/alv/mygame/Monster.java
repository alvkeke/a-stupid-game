package com.game.alv.mygame;

import java.util.Random;

public class Monster extends Constants{

    //定义怪物属性变量
    private int hp;
    private int type;
    private int place;
    private int hitDistance = 1;
    private boolean isHitting = false;

    Monster(){
        //初始化怪物种类
        Random random = new Random();
        type = random.nextInt(3);
        //根据怪物种类设置怪物的血量
        switch (type){
            case MONSTER_TYPE_NORMAL:
                hp = 60;
                hitDistance = 1;
                break;
            case MONSTER_TYPE_FIRE:
                hp = 30;
                hitDistance = 2;
                break;
            case MONSTER_TYPE_POISON:
                hp = 40;
                hitDistance = 1;
                break;
        }
        //随机初始化怪物出生地点,不确定是否出现
        place = random.nextInt(11) - 1;
        //将不出现的剔除,其他的怪物向右移动两格
        if(place>0) place+=2;
    }



    public void Action(Player player, Monster[] m){
        //判断是否到达攻击范围
        if(Math.abs(player.getPlace() - place) > hitDistance){
            //未到达攻击范围则靠近玩家
            if(player.getPlace() > place){
                //如果怪物前面有其他的怪物,则不行动
                for (Monster aM : m) {
                    if (aM.getPlace() == place + 1)
                        return;
                }
                //如果怪物前有玩家,则不行动
                if(player.getPlace() == place + 1)  return;
                place++;
            }else {
                //如果有怪物阻挡,则不行动
                for (Monster aM : m) {
                    if (aM.getPlace() == place - 1)
                        return;
                }
                //如果有玩家阻挡,则不行动
                if(player.getPlace() == place - 1) return;
                place--;
            }
        }else{
            //否则攻击玩家
            hit(player);
            isHitting = true;
        }
    }

    public boolean isHitting() {
        return isHitting;
    }

    public void setHitting(boolean hitting) {
        isHitting = hitting;
    }

    private void hit(Player p){
        p.wasHit(type);
    }

    public void wasHit(int damage){
        //收到玩家伤害
        hp-=damage;
        //如果血量为0,或者小于0,则消失
        if(hp<=0){
            place = -1;
        }
    }

    public int getPlace() {
        return place;
    }

    public int getHp() {
        return hp;
    }

    public int getType() {
        return type;
    }

}
