package me.zhengjie.modules.system.service.async;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.system.domain.Article;
import me.zhengjie.modules.system.repository.ArticleRepository;
import me.zhengjie.utils.ValidationUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.persistence.OptimisticLockException;
import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncArticleService {
    private final ArticleRepository repository;

    @Transactional
    public void read(Long id) {
        int num = 0;
        do {
            num++;
            Article article = repository.findByIdAndEnabled(id,true).orElseGet(Article::new);
            ValidationUtil.isNull(article.getId(),"Article","id",id);
            try {
                repository.read(article.getId(),article.getVersion());
            }catch (OptimisticLockException ex){
                log.info("更新文章{}失败，进行第[{}]次重试...",article.getTitle());
                if (num >= 3){
                    log.error("{}次重试失败，更新文章[{}]彻底失败！",article.getTitle());
                }
            }
        }while (num >= 3);
    }
}
