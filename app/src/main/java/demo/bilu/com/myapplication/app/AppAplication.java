package demo.bilu.com.myapplication.app;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;


public class AppAplication extends Application {

    private static Context mContext;


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        initImageLoader();

    }

    public static Context getContext() {
        return mContext;
    }

    /**
     * 初始化ImageLoader
     */
    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
//				.writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }


}