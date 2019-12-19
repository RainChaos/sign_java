package cn.gljp.sign.admin.mapper.sign;

import cn.gljp.sign.admin.entity.Article;
import cn.gljp.sign.admin.entity.ArticleCategory;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface ArticleMapper {

    void insert(Article article);
    void insertSelective(Article article);
    void update(Article article);

    void delete(Article article);

    List<Article> getArticleList(@Param("index") int index,@Param("limit") int limit);

    Article getArticleByTitle(Article article);

    List<ArticleCategory> getArticleCategoryTree();

    List<ArticleCategory> listArticleCategoryByFid(ArticleCategory article);

    void categoryInsert(ArticleCategory article);

    void categoryUpdate(ArticleCategory article);

    void categoryDelete(ArticleCategory article);

    Long listArticleCount();

    Article getOneArticle(Article article);

    void updateSelective(Article article);
}
