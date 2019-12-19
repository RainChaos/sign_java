package cn.gljp.sign.admin.service;

import cn.gljp.sign.admin.entity.Article;
import cn.gljp.sign.admin.entity.ArticleCategory;
import cn.gljp.sign.admin.mapper.sign.ArticleMapper;
import cn.gljp.sign.utils.PageResult;
import cn.gljp.sign.utils.Result;
import cn.gljp.sign.utils.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    public void insert(Article article) {
        articleMapper.insert(article);
    }

    public void insertSelective(Article article) {
        articleMapper.insertSelective(article);
    }

    public void update(Article article) {
        articleMapper.updateSelective(article);
    }

    public void delete(Article article) {
        articleMapper.delete(article);
    }

    public Result getArticleList(int index, int limit) {
        Long total = articleMapper.listArticleCount();
        List<Article> list = articleMapper.getArticleList(index, limit);
        PageResult<Article> pageList = new PageResult<Article>(total, list);
        return new Result(StatusCode.OK, true, "", pageList);
    }

    public Article getArticleByTitle(Article article) {
        return articleMapper.getArticleByTitle(article);
    }

    public List<ArticleCategory> getArticleCategoryTree() {
        return articleMapper.getArticleCategoryTree();
    }

    public List<ArticleCategory> listArticleCategoryByFid(ArticleCategory article) {
        return articleMapper.listArticleCategoryByFid(article);
    }

    public void categoryInsert(ArticleCategory article) {
        articleMapper.categoryInsert(article);
    }

    public void categoryUpdate(ArticleCategory article) {
        articleMapper.categoryUpdate(article);
    }

    public void categoryDelete(ArticleCategory article) {
        articleMapper.categoryDelete(article);
    }

    public Article getOneArticle(Article article) {
        return articleMapper.getOneArticle(article);
    }


}
