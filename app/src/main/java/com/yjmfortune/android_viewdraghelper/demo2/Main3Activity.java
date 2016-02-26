package com.yjmfortune.android_viewdraghelper.demo2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.yjmfortune.android_viewdraghelper.R;

import java.lang.reflect.Field;

public class Main3Activity extends AppCompatActivity {

    int[] imageIds = new int[12];
    private String imagePaths[] = {"assets://wall01.jpg",
            "assets://wall02.jpg", "assets://wall03.jpg",
            "assets://wall04.jpg", "assets://wall05.jpg",
            "assets://wall06.jpg", "assets://wall07.jpg",
            "assets://wall08.jpg", "assets://wall09.jpg",
            "assets://wall10.jpg", "assets://wall11.jpg",
            "assets://wall12.jpg", "assets://wall01.jpg",
            "assets://wall02.jpg", "assets://wall03.jpg",
            "assets://wall04.jpg", "assets://wall05.jpg",
            "assets://wall06.jpg", "assets://wall07.jpg",
            "assets://wall08.jpg", "assets://wall09.jpg",
            "assets://wall10.jpg", "assets://wall11.jpg", "assets://wall12.jpg"}; // 24个图片资源名称

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        for (int i = 0; i < imageIds.length; i++) {
            try {
                //  根据随机产生的1至9的整数从R.drawable类中获得相应资源ID（静态变量）的Field对象
                Field field;

                if(i<9){
                    field = R.drawable.class.getDeclaredField("wall0" + (i + 1));
                }else{
                    field = R.drawable.class.getDeclaredField("wall" + (i + 1));
                }
                //  获得资源ID的值，也就是静态变量的值
                int resourceId = Integer.parseInt(field.get(null).toString());
                imageIds[i] = resourceId;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        CardSlidePanel my_CardSlidePanel = (CardSlidePanel) findViewById(R.id.my_CardSlidePanel);
        initImageLoader();
        my_CardSlidePanel.setImageID(imageIds,imagePaths);




    }


    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this)
                .memoryCacheExtraOptions(480, 800)
                        // default = device screen dimensions
                .threadPoolSize(3)
                        // default
                .threadPriority(Thread.NORM_PRIORITY - 1)
                        // default
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                        // default
                .denyCacheImageMultipleSizesInMemory()
                        ////当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                        //这个类就是这个开源框架默认的内存缓存类，缓存的是bitmap的强引用，
                .memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(13) // default
                .discCacheSize(50 * 1024 * 1024) // 缓冲大小
                .discCacheFileCount(100) // 缓冲文件数目
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(this)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs().build();

        // 2.单例ImageLoader类的初始化
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }

}
