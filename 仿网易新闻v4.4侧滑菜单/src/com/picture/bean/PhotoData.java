package com.picture.bean;


/**
 * Created by harry on 1/17/16.
 */

import com.example.netease.R;

import android.net.Uri;



/**
 * 照片数据
 */
public class PhotoData {

    public static final PhotoData DATA_ADD = new PhotoData(null, R.drawable.ic_publish_button_addpic_small, null);

    public static byte[] EMPTY = new byte[]{};

    /**
     * resource uri for non-local resource
     */
    Uri resourceUri;
    /**
     * resource uri for local resource
     */
    int resourceId = R.drawable.ic_publish_button_addpic_small;

    /**
     * photo binary data
     */
    byte[] data = EMPTY;


    public PhotoData(Uri resourceUri, int resourceId, byte[] data) {
        this.resourceUri = resourceUri;
        this.resourceId = resourceId;
        if (data != null) {
            this.data = data;
        }
    }

    public Uri getResourceUri() {
        return resourceUri;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
