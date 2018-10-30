一、在项目中导入Happyzhuo SDK （AndroidSutio）

	1. File -> New Module -> Import .JAR/.AAR
	2. 选择adbilling.aar
	3. 向应用层级（而不是项目层级！）的build.gradle 添加下列编译语句，以使用Happyzhuo SDK
	
		dependencies {
		  implementation project(':adbilling')
		  ...
		  
		  implementation 'com.android.billingclient:billing:1.0'//谷歌内购SDK
		  implementation 'com.google.android.gms:play-services-ads:15.0.1'//谷歌admob SDK
		  implementation 'com.facebook.android:audience-network-sdk:5.0.1'//facebook广告SDK
		  implementation 'com.github.vungle:vungle-android-sdk:5.3.2'//vungle广告SDK
		  
		  //以下为广告SDK依赖的android库
		  implementation 'com.android.support:appcompat-v7:27.1.1'
		  implementation 'com.android.support:support-v4:27.1.1'
		  implementation 'com.android.support:support-media-compat:27.1.1'
		  implementation 'com.android.support:recyclerview-v7:27.1.1'
		  implementation 'com.android.support:customtabs:27.1.1'
		  implementation 'com.android.support:animated-vector-drawable:27.1.1'
		}
		
二、修改AndroidManifest.xml

	1、添加权限
	   <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" android:required="false"/>
	   <uses-permission android:name="android.permission.INTERNET" android:required="false"/>
	   <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" android:required="false"/>
	   <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" android:maxSdkVersion="999" tools:replace="android:maxSdkVersion"/>
	   //谷歌内购需要的权限
	   <uses-permission android:name="com.android.vending.BILLING" android:required="false"/>
	2、添加元数据
	   //Happyzhuo为你的游戏或应用分配的securekey，开发测试阶段可以直接使用下面的key值
	   <meta-data android:name="com.happyzhuo.sdk.securekey" android:value="Ue+sM7mV9Ehvu9jn+jQ1icUMeaugvgwqmgEjIsUCWAFh7U/RvewGOUN4HZEytaA6EUtGRrzdaBLzLG3Gsv0uBA46yB25AuGkoiqRftZhE/oNGr6onfGh8Ny98uGZOoEtUuCEWqr3mfGL0WPtiugI86Ts7Fnt9srOnE3NSXqKtidH2TexCtYtvkibHPtKTd3j8kaasfxgoKth9njXNGvQisiHmURWCP9IoEBsWB9fkcqC3dZxvpc9khj7nNUPIJQOJQxpK0UotDRjmKQnwHh4Fq4etsy3osw73OeP0Ebi+XbKMOMPxacl/xs9n97Bp28GzGhbBDoxZKl0Pctd8Y5NOuTpi9aE7fkGYxctQGBTygTrmfx8BlxduHcOnj/ZBKTIexj8NgMVv5DzblIif3UdqoqubsiDh/XrcfV/prmjcZ0sJDZuSGae6eiv6D0Pejm/zhXKmOsbpqj4+QyfxNYaP65ZjygJ5HTjiVCIf5kzARMVykgPjGvRJl3LgZQgM5GxCV9UezVR8LCxmNqyBaUfx6KF/FenxuekTPW4g7OxFh3kRjfamfXr683vOgnJ4x4WYn5aaGKsPBULvpdp/Ti6wEz3SbIbhJBxVacKC7ZUwPNhwF2HDg60cvn4uylrwbwd"/>
	   //开发测试阶段设置为true，正式发布版本需要修改为false
	   <meta-data android:name="com.happyzhuo.sdk.debug" android:value="true"/>
	   //谷歌广告和内购需要加的配置项,copy即可
	   <meta-data android:name="com.google.android.gms.version" android:value="@integer/google_play_services_version"/>
	   
三、广告接入

	1、	在Activity的onCreate方法中加入如下广告SDK初始化代码
		ADAgent.getInstance().onActivityCreate(this);
		
	2、 在Activity的onDestroy方法中加入如下广告SDK的清除方法
		ADAgent.getInstance().onActivityDestory(this);
		
	3、 广告展示
		Banner：
		<com.happyzhuo.sdk.api.BannerLayout
		android:id="@+id/banner_view"
		android:layout_width="match_parent"
		android:layout_height="wrap_content" android:layout_alignParentBottom="true"/>
		
		插屏广告：
		ADAgent.getInstance().showInterstitial(context,new AdListener() {
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
					}
				});
				
		视频奖励广告：
		ADAgent.getInstance().playVideoAd(new RewardedVideoAdListener() {
					@Override
					public void onRewarded() {
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
				
四、内购接入

	1、	在Activity的onCreate方法中加入内购初始化代码
		billingManager = new BillingManager(this, new BillingUpdatesListener() {
			/**
			 * 购买商品成功后的回调
			 * @param skuId  商品编号
			 */
			@Override
			public void onPurchasesSuccess(String skuId) {
			}

			@Override
			public void onPurchasesFail(int resultCode) {
			}
		});
		
	2、 在Activity的onDestroy方法中加入内购的清除方法
		if(billingManager!=null){
			billingManager.destroy();
		}
		
	3、发起内购
		传入商品编号，发起内购请求，目前可使用SDK内置的16个商品编号，
		每个商品编号对应的价格以及商品描叙需要在正式发布上线前告知我们，以便提前做好配置。
		billingManager.purchase(SkuId.SKU1);//购买1号商品
