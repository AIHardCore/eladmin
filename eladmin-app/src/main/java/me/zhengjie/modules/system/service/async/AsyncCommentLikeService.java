package me.zhengjie.modules.system.service.async;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.system.domain.Article;
import me.zhengjie.modules.system.domain.Comment;
import me.zhengjie.modules.system.domain.CommentLike;
import me.zhengjie.modules.system.repository.CommentLikeRepository;
import me.zhengjie.modules.system.repository.CommentRepository;
import me.zhengjie.modules.system.service.CommentLikeService;
import me.zhengjie.modules.system.service.dto.CommentLikeDto;
import me.zhengjie.utils.ValidationUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.persistence.OptimisticLockException;
import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncCommentLikeService {
    private final CommentLikeRepository repository;
    private final CommentRepository commentRepository;

    @Transactional
    public void like(Long id, String from, String to,boolean isLike) {
        if (isLike) {
            repository.like(id, from, to, from, from);
        }else {
            repository.unlike(id, from, to);
        }
        int num = 0;
        do {
            num++;
            Comment comment = commentRepository.findById(id).orElseGet(Comment::new);
            ValidationUtil.isNull(comment.getId(),"Comment","id",id);
            try {
                if (isLike) {
                    commentRepository.like(comment.getId(), comment.getVersion());
                }else {
                    commentRepository.unlike(comment.getId(),comment.getVersion());
                }
            }catch (OptimisticLockException ex){
                log.info("更新留言{}失败，进行第[{}]次重试...",comment.getId());
                if (num >= 3){
                    log.error("{}次重试失败，更新留言[{}]彻底失败！",comment.getId());
                }
            }
        }while (num >= 3);
    }
}
