package com.game.alv.mygame;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.LoginFilter;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Objects;
import java.util.Random;

public class GameActivity extends AppCompatActivity {


    //人站立的位置
    private ImageView[] place = new ImageView[14];
    //人物
    private Player p;
    //怪物
    private Monster[] m = new Monster[5];
    //上方标题栏信息
    private TextView txtPlayer;
    private TextView txtGoods;
    private TextView txtMap;
    private TextView txtSystem;
    //人物行动次数
    private int motivationTime;
    //用于提示消息的控件
    private TextView txtTips;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //隐藏标题栏
        Objects.requireNonNull(getSupportActionBar()).hide();
        //隐藏系统状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //设置横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //取得位置
        place[0] = findViewById(R.id.img_place_1);
        place[1] = findViewById(R.id.img_place_2);
        place[2] = findViewById(R.id.img_place_3);
        place[3] = findViewById(R.id.img_place_4);
        place[4] = findViewById(R.id.img_place_5);
        place[5] = findViewById(R.id.img_place_6);
        place[6] = findViewById(R.id.img_place_7);
        place[7] = findViewById(R.id.img_place_8);
        place[8] = findViewById(R.id.img_place_9);
        place[9] = findViewById(R.id.img_place_10);
        place[10] = findViewById(R.id.img_place_11);
        place[11] = findViewById(R.id.img_place_12);
        place[12] = findViewById(R.id.img_place_13);
        place[13] = findViewById(R.id.img_place_14);

        //初始化人物
        p = new Player();
        //获取视图上方的信息栏
        txtPlayer = findViewById(R.id.game_txt_player_status);
        txtGoods = findViewById(R.id.game_txt_player_goods);
        txtMap = findViewById(R.id.game_txt_map_message);
        txtSystem = findViewById(R.id.game_txt_system);
        //获取底部消息提示栏
        txtTips = findViewById(R.id.game_txt_tips);
        //获取视图中的按钮
        Button btn_go = findViewById(R.id.game_btn_forward);
        Button btn_back = findViewById(R.id.game_btn_backward);
        Button btn_attack = findViewById(R.id.game_btn_hit);
        Button btn_define = findViewById(R.id.game_btn_define);

        //给按钮添加监听
        btn_go.setOnClickListener(new btnForward());
        btn_back.setOnClickListener(new btnBackward());
        btn_attack.setOnClickListener(new btnAttack());
        btn_define.setOnClickListener(new btnDefine());

        //循环设置图片在下方显示人物
        for(int i = 0; i<14; i++){
            place[i].setScaleType(ImageView.ScaleType.FIT_END);
        }

        //新建地图
        newMap();

        //显示玩家和怪物
        showPlayerAndMonster();
        //重设玩家行动次数
        resetMotivationTime();
        //显示状态栏
        showInformation(p);


        //建立游戏控制主线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(p.getHp()>0){
                    //如果行动次数大于0,则跳过
                    if(motivationTime>0)continue;
                    //提示玩家轮到怪物行动
                    GameActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //刷新界面
                            showPlayerAndMonster();
                            showInformation(p);
                        }
                    });
                    //暂停半秒钟
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //怪物行动,中间时间间隔
                    for (Monster aM : m) {
                        if (aM.getPlace() >= 0) {
                            aM.Action(p, m);
                            //刷新界面
                            GameActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showPlayerAndMonster();
                                    showInformation(p);
                                }
                            });
                            //每一个怪物行动一次就暂停半秒钟
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            aM.setHitting(false);
                            //刷新界面
                            GameActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showPlayerAndMonster();
                                    showInformation(p);
                                }
                            });
                        }
                    }

                    //重设行动次数
                    resetMotivationTime();

                    //更新状态栏
                    GameActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //更新图形界面
                            showPlayerAndMonster();
                            //更新信息栏
                            showInformation(p);
                        }
                    });
                }
                //玩家死亡
                GameActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "You Lose.", Toast.LENGTH_SHORT).show();
                        CharSequence sTips = "You Lose.";
                        txtTips.setText(sTips);
                    }
                });
                GameActivity.this.finish();
            }
        }).start();

    }

    private void newMap(){
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

    private void showPlayerAndMonster() {
        //消除其他图片
        for(int i = 0; i<14; i++){
            //清空人物图像
            place[i].setImageBitmap(null);
            //清空怪物图像
            place[i].setBackground(null);
        }
        //显示现在新的颜色
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
            if(!p.isDefense()){
                //显示人物
                place[p.getPlace()].setImageResource(R.drawable.player_noweapon);
            }else{
                place[p.getPlace()].setImageResource(R.drawable.player_define);
            }
        }else{
            if(!p.isDefense()){
                //显示人物
                place[p.getPlace()].setImageResource(R.drawable.player_noweapon_back);
            }else{
                place[p.getPlace()].setImageResource(R.drawable.player_define_back);
            }
        }


    }


    class btnForward implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //判断行动次数是否大于0,如果没有行动次数则不处理此次动作
            if(motivationTime<=0){
                return;
            }
            //行动次数减一
            motivationTime--;
            //消除原本人物站立位置的图像
            place[p.getPlace()].setImageBitmap(null);
            //人物前进,如果进入新地图建立怪物
            int movReturn = p.moveForward();
            if(movReturn == 1){
                newMap();
            }else if(movReturn == 2){
                //如果走到地图最尽头,则玩家胜利
                Toast.makeText(getApplicationContext(), "You Win!!!", Toast.LENGTH_LONG).show();
                //结束进程
                GameActivity.this.finish();
            }

            //更新画面
            showPlayerAndMonster();
            //显示人物状态
            showInformation(p);

        }
    }

    class btnBackward implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //判断行动次数是否大于0,如果没有行动次数则不处理此次动作
            if(motivationTime<=0){
                return;
            }
            //行动次数减一
            motivationTime--;
            //消除原本人物站立位置图像
            place[p.getPlace()].setImageBitmap(null);
            //人物后退
            p.moveBackward();
            //更新画面
            showPlayerAndMonster();
            //显示人物状态
            showInformation(p);
        }
    }

    class btnAttack implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //判断行动次数是否大于0,如果没有行动次数则不处理此次动作
            if(motivationTime<=0){
                return;
            }
            //行动次数减一
            motivationTime--;
            //在攻击范围内的怪物会收到伤害
            for(Monster aM : m){
                //判断怪物是否在攻击范围内
                if((aM.getPlace() - p.getPlace())*p.getDirection()>=0 && Math.abs(aM.getPlace() - p.getPlace())<=p.getHitDistance()){
                    //在攻击范围内的所有怪物受到伤害
                    aM.wasHit(p.hit());
                }
            }
            //更新状态栏状态
            showInformation(p);
            //更新画面
            showPlayerAndMonster();
        }
    }

    class btnDefine implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //判断行动次数是否大于0,如果没有行动次数则不处理此次动作
            if(motivationTime<=0){
                return;
            }
            //行动次数减一
            motivationTime--;
            //设置人物防御
            p.setDefense(true);
            //更新画面
            showPlayerAndMonster();
            //更新状态栏状态
            showInformation(p);
        }
    }

    //更新系统状态栏信息
    private void showInformation(Player p){

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
    private void resetMotivationTime(){
        Random random = new Random();
        //1/4无行动,1/2一次行动,1/4两次行动
        motivationTime = (random.nextInt(4) + 2);
        if(random.nextInt(100)<5){
            motivationTime = 0;
        }
    }

}
