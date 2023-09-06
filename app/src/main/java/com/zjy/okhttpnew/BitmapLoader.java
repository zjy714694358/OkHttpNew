package com.zjy.okhttpnew;

/**
 * Date:2023/9/5 10:15
 * author:jingyu zheng
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.collection.LruCache;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;


/**
 * @author yapple
 * @date 2019/12/21
 * # Description bitmap 缓存、加载类
 */
public class BitmapLoader {

    private static BitmapLoader mBitmapLoader;

    private LruCache<String, Bitmap> mCache;
    private DiskLruCache mDiskLruCache;

    /**
     * 将DISK_FILE_PATH字符串中的<application package>替换成自己的包名
     */
    private static final String DISK_FILE_PATH = "/data/data/Android/<application package>/cache/bitmapCache";
    private static final long DISK_MAX_SIZE = 100 * 1024 * 1024;

    /**
     * 内存缓存的大小
     * 上面说了内存资源很珍贵，这里我们规定好内存资源的大小以kb为单位
     */
    private int mCacheSize;

    private BitmapLoader() {
        long maxSize = Runtime.getRuntime().maxMemory();
        mCacheSize = (int) (maxSize / 8);
        mCache = new LruCache<>(mCacheSize);
        try {
            File file = new File(DISK_FILE_PATH);
            if (!file.exists()) {
                boolean mkdirs = file.mkdirs();
                if (!mkdirs) {
                    throw new IOException("yapple.e " + DISK_FILE_PATH + " cant be create");
                }
            }
            mDiskLruCache = DiskLruCache.open(file, 1, 1, DISK_MAX_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BitmapLoader getInstance() {
        if (mBitmapLoader == null) {
            synchronized (BitmapLoader.class) {
                if (mBitmapLoader == null) {
                    mBitmapLoader = new BitmapLoader();
                }
            }
        }
        return mBitmapLoader;
    }

    public int getmCacheSize() {
        return mCacheSize;
    }

    /**
     * 修改内存缓存的大小
     */
    public void setmCacheSize(int mCacheSize) {
        this.mCacheSize = mCacheSize;
        mCache.resize(mCacheSize);
    }

    /**
     * 将bitmap保存到缓存中, 由于我这里并没有写网络相关的环节，所以直接将bitmap作为参数进行保存，
     * 实际通过上流的方式来保存会更加方便，也比较接近项目需求。
     * @param key 通过key value形式保存bitmap，key可以是URL等
     */
    public void putBitmapToCache(String key, Bitmap bitmap) {
        if (key != null && bitmap != null) {
            mCache.put(key, bitmap);
            try {
                int bytes = bitmap.getByteCount();
                ByteBuffer buffer = ByteBuffer.allocate(bytes);
                bitmap.copyPixelsToBuffer(buffer);
                DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                OutputStream outputStream = editor.newOutputStream(0);
                outputStream.write(buffer.array());
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从本地获取图片
     * 当内存中存在时，直接取内存中的bitmap，当内存中不存在时，则会从磁盘中获取。
     * 如果都不存在，则返回null；请从网络中加载
     */
    public Bitmap getBitmapFromLocal(String key) {
        Bitmap bitmap = mCache.get(key);
        if (bitmap == null) {
            bitmap = getBitmapFromDisk(key);
        }
        return bitmap;
    }

    private Bitmap getBitmapFromDisk(String key) {
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
            InputStream inputStream = snapshot.getInputStream(1);
            return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
