package me.zhengjie.utils;

import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.system.domain.LikeParam;
import me.zhengjie.modules.system.service.async.AsyncCommentLikeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class ConsumerCommandLineRunner implements CommandLineRunner {
    private final AsyncCommentLikeService asyncCommentLikeService;

    @Override
    public void run(String... args) {
        Thread consumerThread = new Thread(() -> {
            try {
                log.info("开始消费消息");
                while (true){
                    LikeParam like = BoundedBuffer.consume();
                    log.info("消费:{}", JSON.toJSONString(like));
                    asyncCommentLikeService.like(like.getKey().getId(), like.getKey().getFrom(),like.getKey().getTo(),like.isLike());
                }
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            } catch (Exception ex){
                log.error(ex.getMessage());
            }
        });
        consumerThread.start();
    }
}
