package me.zhengjie.modules.system.service.async;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.system.domain.Article;
import me.zhengjie.modules.system.domain.ArticleReadingLog;
import me.zhengjie.modules.system.repository.ArticleReadingLogRepository;
import me.zhengjie.modules.system.repository.ArticleRepository;
import me.zhengjie.utils.ValidationUtil;
import org.springframework.stereotype.Service;

import javax.persistence.OptimisticLockException;
import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncArticleService {
    private final ArticleRepository repository;
    private final ArticleReadingLogRepository articleReadingLogRepository;

    @Transactional
    public void read(Long id, String openId, boolean type,String ip) {
        int num = 0;
        ArticleReadingLog articleReadingLog = new ArticleReadingLog();
        articleReadingLog.setArticle(id);
        articleReadingLog.setOpenId(openId);
        articleReadingLog.setType(type);
        articleReadingLog.setIp(ip);
        if (!type){
            articleReadingLogRepository.save(articleReadingLog);
        }else {
            long count = articleReadingLogRepository.countByArticleAndOpenIdAndType(id,openId,type);
            if (count == 0){
                articleReadingLogRepository.save(articleReadingLog);
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
    }
}
