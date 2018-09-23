package com.game.alv.mygame;



public class Player{

    //定义玩家属性变量
    private int hp;
    private int money;
    private int weapon;
    private int armor;
    private int special;
    private int place;
    private int map;
    private boolean defense;
    private int direction;
    private int hitDistance;
    private int poisonHurtTime;


    //初始化人物
    Player(){
        hp = 150;
        money = 10;
        weapon = 0;
        armor = 50;
        special = 0;
        place = 0;
        map = 0;
        direction = Constants.PLAYER_DIRECTION_FORWARD;
        hitDistance = 1;
        poisonHurtTime = 0;
    }

    public void setHelpMode(){
        hp = 100000;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    //人物向前走动
    public int moveForward(){
        //解除防御
        setDefense(false);
        //设置方向为向前
        direction = Constants.PLAYER_DIRECTION_FORWARD;
        //如果处于中毒状态,则每次行动扣血
        if(poisonHurtTime-->0){
            hp-=Constants.POISON_HURT;
        }
        //判断是否到地图尽头
        if(place < 13){
            place++;
        }else if(place ==13 && map< Constants.MAP_MAX_NUMBER){
            //如果在地图右边缘
            place = 0;
            map++;
            return 1;
        }else if(place == 13 && map == Constants.MAP_MAX_NUMBER){
            //胜利返回12
            return 2;
        }
        return 0;
    }

    //向后走
    public void moveBackward(){
        //解除防御
        setDefense(false);
        //设置方向为向后
        direction = Constants.PLAYER_DIRECTION_BACKWARD;
        //如果处于中毒状态,则每次行动扣血
        if(poisonHurtTime-->0){
            hp-=Constants.POISON_HURT;
        }
        //判断人物位置
        if(place > 0){
            //如果人不在左边缘,则后退
            place--;
        }else if(place == 0 && map>0){
            //如果人物在左边缘,则地图页面减1,人物到达右边缘
            place = 13;
            map--;
            //return  1;
        }

        //return 0;
    }

    //攻击敌人
    public int hit(){
        //解除防御
        setDefense(false);
        switch (weapon){
            case Constants.WEAPON_TYPE_NONE:
                return Constants.WEAPON_HIT_NONE;
            case Constants.WEAPON_TYPE_KNIFE:
                return Constants.WEAPON_HIT_KNIFE;
            case Constants.WEAPON_TYPE_SWORD:
                return Constants.WEAPON_HIT_SWORD;
            case Constants.WEAPON_TYPE_BOW:
                return Constants.WEAPON_HIT_BOW;
        }
        //如果处于中毒状态,则每次行动扣血
        if(poisonHurtTime-->0){
            hp-=Constants.POISON_HURT;
        }
        return 10;
    }

    //受到攻击,传入怪物类型
    public void wasHit(int monster){

        //收到伤害量
        int hpDec = 0;
        switch (monster){
            case Constants.MONSTER_TYPE_NORMAL:
                hpDec = Constants.MONSTER_HIT_NORMAL;
                break;
            case Constants.MONSTER_TYPE_FIRE:
                hpDec = Constants.MONSTER_HIT_FIRE;
                break;
            case Constants.MONSTER_TYPE_POISON:
                hpDec = Constants.MONSTER_HIT_POISON;
                //如果不处于中毒状态则中毒
                if(!defense){
                    poisonHurtTime = 5;
                }
                break;
        }

        if(defense){
            //如果处于防御状态,则判断防具状态
            if(armor>50 && armor<=100){
                //消耗防具耐久
                armor-=(hpDec/3);
                //抵挡所有伤害
                hpDec = 0;

            }else if(armor>0 && armor<=50){
                //消耗防具耐久
                armor-=(hpDec/4);
                //判断耐久是否为负数,如果为负数,则将其置零
                if(armor<0) armor = 0;
                //抵挡一般伤害
                hpDec/=2;

            }
        }
        //受到伤害
        hp-=hpDec;

    }

    //防御
    public void setDefense(boolean defense) {
        this.defense = defense;
        //如果处于中毒状态,则每次行动扣血
        if(poisonHurtTime-->0){
            hp-=Constants.POISON_HURT;
        }
    }

    /*
    //购买物品
    public int buy(int goods){
        //TODO:实现购买物品的动作
        return 0;
    }
    */

    //获得防御状态
    public boolean isDefense() {
        return defense;
    }

    //判断是否中毒
    public boolean isPoison() {
        return poisonHurtTime > 0;
    }

    //获取当前位置

    public int getPlace() {
        return place;
    }

    public int getMap(){
        return map;
    }

    public int getHp(){
        return hp;
    }

    public int getMoney() {
        return money;
    }

    public void gainMoney(int gainMoney){
        money+=gainMoney;
    }

    public int getArmor() {
        return armor;
    }

    public int getWeapon() {
        return weapon;
    }

    public int getSpecial() {
        return special;
    }

    public int getDirection() {
        return direction;
    }

    public int getHitDistance() {
        return hitDistance;
    }
}
