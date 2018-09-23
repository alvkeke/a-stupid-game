package com.game.alv.mygame;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    //名称输入栏
    private EditText edtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //设置为横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //隐藏系统状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //获取背景图片,并设置颜色,以后需要替换成图片
        //TODO:将图片背景颜色替换成图片
        ImageView imageView = findViewById(R.id.img_background);
        imageView.setBackgroundColor(Color.RED);

        //获取名称输入栏
        edtName = findViewById(R.id.edt_name);
        //TODO:删掉测试用的语句
        CharSequence sNameTest = "hello";
        edtName.setText(sNameTest);

        //获取视图上的按钮
        Button btn_start = findViewById(R.id.btn_start);
        Button btn_end = findViewById(R.id.btn_end);
        Button btn_help = findViewById(R.id.btn_help);
        //添加按钮监听事件
        btn_start.setOnClickListener(new startBtnOnClick());
        btn_end.setOnClickListener(new endBtnOnClick());
        btn_help.setOnClickListener(new helpBtnOnClick());

    }

    //开始游戏按钮监听类
    class startBtnOnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //判断名称是否为空
            if(edtName.getText().toString().equals("")){
                //名称为空则弹出警告,不做任何处理
                Toast.makeText(getApplicationContext(), "Name can not be empty", Toast.LENGTH_SHORT).show();
            }else{
                //名称不为空,则获取名称,并将其输入游戏界面
                String playerName = edtName.getText().toString();
                //新建intnet,将要切换到游戏界面,设置传入参数
                Intent intentGame = new Intent(MainActivity.this, GameActivity.class);
                //传入参数确定是实战模式
                intentGame.putExtra("isHelp", false);
                intentGame.putExtra("PlayerName", playerName);
                //显示游戏界面
                startActivity(intentGame);
            }
        }

    }

    class helpBtnOnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            //判断名称是否为空
            if(edtName.getText().toString().equals("")){
                //名称为空则弹出警告,不做任何处理
                Toast.makeText(getApplicationContext(), "Name can not be empty", Toast.LENGTH_SHORT).show();
            }else{
                //名称不为空,则获取名称,并将其输入游戏界面
                String playerName = edtName.getText().toString();
                //新建intnet,将要切换到游戏界面,设置传入参数
                Intent intentGame = new Intent(MainActivity.this, GameActivity.class);
                //传入参数说明是帮助模式
                intentGame.putExtra("isHelp", true);
                intentGame.putExtra("PlayerName", playerName);
                //显示游戏界面
                startActivity(intentGame);
            }
        }
    }

    //结束游戏按钮监听类
    class endBtnOnClick implements View.OnClickListener{
        @Override
        public void onClick(View v){
            finish();
        }
    }
}
