package com.game.alv.mygame;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Objects;

public class GameActivity extends AppCompatActivity {


    //人站立的位置
    public ImageView[] place = new ImageView[14];
    //人物
    public Player p;
    //怪物
    public Monster[] m = new Monster[5];
    //上方标题栏信息
    public TextView txtPlayer;
    public TextView txtGoods;
    public TextView txtMap;
    public TextView txtSystem;
    //人物行动次数
    public int motivationTime;
    //用于提示消息的控件
    public TextView txtTips;

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
        Functions.newMap(m, p);

        //显示玩家和怪物
        Functions.showPlayerAndMonster(place, m, p);
        //重设玩家行动次数
        motivationTime = Functions.resetMotivationTime();
        //更新状态栏状态
        Functions.showInformation(p, motivationTime, txtMap, txtPlayer, txtGoods, txtSystem, txtTips);
        //防止开始是玩家行动数为0,让怪物行动线程先运行一次,如果行动数不为0会直接退出线程而不做任何行动
        //如果行动数为0会使怪物行动一次
        new Thread(new MonsterAction(GameActivity.this)).start();

    }

    //前进按钮事件监听
    class btnForward implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //判断行动次数是否大于0,如果没有行动次数则不处理此次动作
            if(motivationTime<=0){
                return;
            }
            //行动次数减一
            motivationTime--;
            //人物前进,如果进入新地图建立怪物
            int movReturn = p.moveForward();
            if(movReturn == 1){
                Functions.newMap(m, p);
            }else if(movReturn == 2){
                //如果走到地图最尽头,则玩家胜利
                Toast.makeText(getApplicationContext(), "You Win!!!", Toast.LENGTH_LONG).show();
                //结束进程
                GameActivity.this.finish();
            }

            //更新画面
            Functions.showPlayerAndMonster(place, m, p);
            //更新状态栏状态
            Functions.showInformation(p, motivationTime, txtMap, txtPlayer, txtGoods, txtSystem, txtTips);
            //当行动次数耗尽时运行
            if(motivationTime == 0){
                new Thread(new MonsterAction(GameActivity.this)).start();
            }

        }
    }

    //后退按钮事件监听
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
            Functions.showPlayerAndMonster(place, m, p);
            //更新状态栏状态
            Functions.showInformation(p, motivationTime, txtMap, txtPlayer, txtGoods, txtSystem, txtTips);
            //当行动次数耗尽时运行
            if(motivationTime == 0){
                new Thread(new MonsterAction(GameActivity.this)).start();
            }
        }
    }

    //攻击按钮事件监听
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
                    if(aM.getHp()<=0){
                        p.gainMoney(10*(aM.getType()+1));
                    }
                }
            }
            //更新画面
            Functions.showPlayerAndMonster(place, m, p);
            //更新状态栏状态
            Functions.showInformation(p, motivationTime, txtMap, txtPlayer, txtGoods, txtSystem, txtTips);
            //当行动次数耗尽时运行
            if(motivationTime == 0){
                new Thread(new MonsterAction(GameActivity.this)).start();
            }
        }
    }

    //防御按钮事件监听
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
            Functions.showPlayerAndMonster(place, m, p);
            //更新状态栏状态
            Functions.showInformation(p, motivationTime, txtMap, txtPlayer, txtGoods, txtSystem, txtTips);
            //当行动次数耗尽时运行
            if(motivationTime == 0){
                new Thread(new MonsterAction(GameActivity.this)).start();
            }
        }
    }

}
