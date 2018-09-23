package com.game.alv.mygame;


import android.widget.Toast;

class MonsterAction implements Runnable {

    private GameActivity gameActivity;

    private boolean shouldStop = false;

    MonsterAction(GameActivity gameActivityIn){
        gameActivity = gameActivityIn;
    }

    @Override
    public void run() {


        //判断建立线程时玩家行动数是否为0,如果为0则退出
        if(gameActivity.motivationTime>0)return;

        //提示玩家轮到怪物行动
        gameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //刷新界面
                Functions.showPlayerAndMonster(gameActivity.place, gameActivity.m, gameActivity.p);
                Functions.showInformation(gameActivity.p, gameActivity.motivationTime, gameActivity.txtMap, gameActivity.txtPlayer,
                        gameActivity.txtGoods, gameActivity.txtSystem, gameActivity.txtTips);
            }
        });

        //怪物行动,中间时间间隔
        for (Monster aM : gameActivity.m) {
            if (aM.getPlace() >= 0) {
                //休眠三百毫秒,
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                aM.Action(gameActivity.p, gameActivity.m);
                //刷新界面
                gameActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //刷新界面
                        Functions.showPlayerAndMonster(gameActivity.place, gameActivity.m, gameActivity.p);
                        Functions.showInformation(gameActivity.p, gameActivity.motivationTime, gameActivity.txtMap, gameActivity.txtPlayer,
                                gameActivity.txtGoods, gameActivity.txtSystem, gameActivity.txtTips);
                    }
                });
                //此次休眠两百毫秒
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                aM.setHitting(false);
                //刷新界面
                gameActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //刷新界面
                        Functions.showPlayerAndMonster(gameActivity.place, gameActivity.m, gameActivity.p);
                        Functions.showInformation(gameActivity.p, gameActivity.motivationTime, gameActivity.txtMap, gameActivity.txtPlayer,
                                gameActivity.txtGoods, gameActivity.txtSystem, gameActivity.txtTips);
                    }
                });

                //判断玩家是否死亡
                if(gameActivity.p.getHp()<=0){
                    //玩家死亡,发送玩家死亡的通知,并结束进程
                    gameActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //弹出消息
                            Toast.makeText(gameActivity.getApplicationContext(), "You Lose.", Toast.LENGTH_LONG).show();
                            //结束游戏
                            gameActivity.finish();
                            //告诉线程,退出,不执行下面的代码了
                            shouldStop = true;
                        }
                    });
                }
                //退出线程
                if(shouldStop) return;
            }
        }

        //重设行动次数
        gameActivity.motivationTime = Functions.resetMotivationTime();

        //更新状态栏
        gameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //刷新界面
                Functions.showPlayerAndMonster(gameActivity.place, gameActivity.m, gameActivity.p);
                Functions.showInformation(gameActivity.p, gameActivity.motivationTime, gameActivity.txtMap, gameActivity.txtPlayer,
                        gameActivity.txtGoods, gameActivity.txtSystem, gameActivity.txtTips);
            }
        });

        //判断玩家是否死亡
        if(gameActivity.p.getHp()<=0){
            //玩家死亡,发送玩家死亡的通知,并结束进程
            gameActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //弹出消息
                    Toast.makeText(gameActivity.getApplicationContext(), "You Lose.", Toast.LENGTH_LONG).show();
                    //结束游戏
                    gameActivity.finish();
                    //告诉线程需要结束
                    shouldStop = true;
                }
            });
        }
        //结束线程
        if(shouldStop)return;

        //如果玩家行动被跳过,则再执行一次线程
        if (gameActivity.motivationTime == 0 && gameActivity.p.getHp()>0){
            new Thread(new MonsterAction(gameActivity)).start();
        }
    }

}