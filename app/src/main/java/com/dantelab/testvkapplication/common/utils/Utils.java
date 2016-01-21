package com.dantelab.testvkapplication.common.utils;

import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKApiPhoto;

import java.lang.ref.WeakReference;
import java.util.Objects;

/**
 * Created by ivanbrazhnikov on 20.01.16.
 */
public final class Utils {

    public static <T> WeakReference<T> weak(T object){
        return new WeakReference<T>(object);
    }

    public static VKApiPhoto messageFirstPhoto(VKApiMessage message) {
        if (!message.attachments.isEmpty()) {
            for (Object att : message.attachments) {
                if (att instanceof VKApiPhoto) {
                    return (VKApiPhoto) att;
                }
            }
        }
        return  null;
    }
}
