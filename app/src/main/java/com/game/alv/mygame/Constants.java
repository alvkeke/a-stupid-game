package com.game.alv.mygame;

public final class Constants {

    //游戏常亮
    public static final int MAP_MAX_NUMBER = 10;
    public static final int POISON_HURT = 10;

    //人物常量
    public static final int PLAYER_DIRECTION_FORWARD = 1;
    public static final int PLAYER_DIRECTION_BACKWARD = -1;


    //怪物的常亮
    public static final int MONSTER_TYPE_NORMAL = 0;
    public static final int MONSTER_TYPE_FIRE = 1;
    public static final int MONSTER_TYPE_POISON = 2;

    public static final int MONSTER_HP_NORMAL = 60;
    public static final int MONSTER_HP_FIRE = 30;
    public static final int MONSTER_HP_POISON = 40;

    public static final int MONSTER_HIT_NORMAL = 15;
    public static final int MONSTER_HIT_FIRE = 20;
    public static final int MONSTER_HIT_POISON = 5;

    //武器常亮
    public static final int WEAPON_TYPE_NONE = 0;
    public static final int WEAPON_TYPE_KNIFE = 1;
    public static final int WEAPON_TYPE_SWORD = 2;
    public static final int WEAPON_TYPE_BOW = 3;

    public static final int WEAPON_HIT_NONE = 30;
    public static final int WEAPON_HIT_KNIFE = 40;
    public static final int WEAPON_HIT_SWORD = 50;
    public static final int WEAPON_HIT_BOW = 35;

    public static final String[] tipsOfMonsterNormal = {
            "This is the normal monster",
            "Its attack distance is 1 block",
            "It can give you 15 HP damage",
            "And its life is 60."
    };

    public static final String[] tipsOfMonsterFire = {
            "This is the fire monster",
            "Its attack distance is 2 block",
            "It can give you 20 HP damage",
            "And its life is 30."
    };

    public static final String[] tipsOfMonsterPoison = {
            "This is the poison monster",
            "Its attack distance is 1 block",
            "It can give you 5 HP damage",
            "And its life is 40.",
            "You will be poisoning if you was hit by it when you are not defensive"
    };

    public static final String[] tipsOfMonsterNormal_cn = {
            "这是普通怪物",
            "它的攻击范围是1格",
            "它能在你没有防御的情况下对你造成15点伤害",
            "并且它的生命值有60"
    };

    public static final String[] tipsOfMonsterFire_cn = {
            "这是远程(火焰)怪物",
            "它的攻击范围是2格",
            "它能造成20点伤害",
            "它的生命值为30"
    };

    public static final String[] tipsOfMonsterPoison_cn = {
            "这是毒液怪物",
            "它的攻击范围是1格",
            "它能对你造成5点伤害",
            "它的生命值为40",
            "当你在没有防御的状态下受到它的攻击,会获得中毒状态"
    };

    public static final String[] tipsOfPlayer_cn = {
            "这是关于玩界面的介绍",
            "首先是下方的按钮和提示区域",
            "按钮从左到右依次是:",
            "攻击、防御、向左走、向右走",
            "中间的提示区域是显示行动方和玩家能够行动的次数",
            "上方的状态条显示的是人物的生命值、护甲、健康状态、",
            "武器、金钱、特殊物品",
            "处于地图的位置已经行动次数",
            "本游戏是由玩家和怪物交替行动，",
            "玩家能够行动的次数是随机产生的，由0-6",
            "每次行走和攻击、防御都会消耗一次行动数",
            "当行动数消耗完，就会轮到怪物行动，然后在产生新的行动数",
            "反复交替，直至玩家到达终点或死亡",
            "目前的血量是帮助模式时给予的血量，",
            "普通模式下是150血量+50护甲"
    };
}