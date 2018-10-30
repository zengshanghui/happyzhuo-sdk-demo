package com.happyzhuo.sdk.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.happyzhuo.sdk.api.ADAgent;
import com.happyzhuo.sdk.api.AdListener;
import com.happyzhuo.sdk.api.BillingManager;
import com.happyzhuo.sdk.api.BillingUpdatesListener;
import com.happyzhuo.sdk.api.RewardedVideoAdListener;
import com.happyzhuo.sdk.api.SkuId;

public class MainActivity extends AppCompatActivity {

    private BillingManager billingManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化广告
        ADAgent.getInstance().onActivityCreate(this);
        //初始化内购
        billingManager = new BillingManager(this, new BillingUpdatesListener() {
            /**
             * 购买商品成功后的回调
             * @param skuId  商品编号
             */
            @Override
            public void onPurchasesSuccess(String skuId) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"购买商品成功！",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onPurchasesFail(int resultCode) {

            }
        });

        initButtonAction();
    }

    protected void onDestroy(){
        super.onDestroy();
        ADAgent.getInstance().onActivityDestory(this);
        if(billingManager!=null){
            billingManager.destroy();
        }
    }

    private void initButtonAction(){
        findViewById(R.id.bt_inst).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ADAgent.getInstance().showInterstitial(MainActivity.this, new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                    }

                    @Override
                    public void onAdOpened() {
                        super.onAdOpened();
                    }

                    @Override
                    public void onAdNotReady() {
                        super.onAdNotReady();
                        Toast.makeText(MainActivity.this,"插屏广告还没准备好！",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        findViewById(R.id.bt_reward).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ADAgent.getInstance().rewardedVideoIsReady()){
                    Toast.makeText(MainActivity.this,"奖励视频广告还没准备好，请稍后再试！",Toast.LENGTH_SHORT).show();
                    return;
                }
                ADAgent.getInstance().playVideoAd(new RewardedVideoAdListener() {
                    @Override
                    public void onRewarded() {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this,"开始实施奖励！",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onAdStart() {

                    }

                    @Override
                    public void onRewardedVideoClosed() {

                    }

                    @Override
                    public void onUnableToPlayAd() {

                    }
                });
            }
        });

        findViewById(R.id.bt_purchase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                billingManager.purchase(SkuId.SKU1);//购买商品1
                //billingManager.purchase(BillingManager.SkuId.SKU2);//购买商品2
                //billingManager.purchase(BillingManager.SkuId.SKU3);//购买商品3
            }
        });
    }

}
