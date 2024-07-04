package me.zhengjie.utils;

import me.zhengjie.modules.system.domain.LikeParam;

import java.util.concurrent.LinkedBlockingQueue;

public class BoundedBuffer {
    public static LinkedBlockingQueue<LikeParam> buffer = new LinkedBlockingQueue <>();

    public static void produce(LikeParam like) throws InterruptedException {
        buffer.put(like);
    }

    public static LikeParam consume() throws InterruptedException {
        return buffer.take();
    }
}
