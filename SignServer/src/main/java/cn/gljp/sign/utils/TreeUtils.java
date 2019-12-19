package cn.gljp.sign.utils;

import cn.gljp.sign.admin.entity.ArticleCategory;
import cn.gljp.sign.admin.entity.DictionaryType;

import java.util.ArrayList;
import java.util.List;

public class TreeUtils<T> {
    /**
     * @Description：list转tree（树形方法）
     **/
    public static List<DictionaryType> listToTree(List<DictionaryType> DictionaryTypeList){
        List<DictionaryType> dictionaryTypeTree = new ArrayList<DictionaryType>();
        for(DictionaryType dictionaryType : DictionaryTypeList){

            //层级字段数据为两位数，则是一级数据
            if(dictionaryType.getPid()==0){
                dictionaryTypeTree.add(dictionaryType);
            }
            //循环出子级数据，添加到一级数据
            for(DictionaryType subDictionaryType : DictionaryTypeList){
                if(subDictionaryType.getPid()==dictionaryType.getId()){
                    if (dictionaryType.getChildren() == null) {
                        dictionaryType.setChildren(new ArrayList<DictionaryType>());
                    }
                    dictionaryType.getChildren().add(subDictionaryType);

                }
            }

        }

        return dictionaryTypeTree;
    }
    /**
     * @Description：list转tree（树形方法）
     **/
    public static List<ArticleCategory> listToTreeForArticle(List<ArticleCategory> articleCategoryList){
        List<ArticleCategory> articleCategoryTree = new ArrayList<ArticleCategory>();
        for(ArticleCategory articleCategory : articleCategoryList){

            //层级字段数据为两位数，则是一级数据
            if(articleCategory.getFatherId()==0){
                articleCategoryTree.add(articleCategory);
            }
            //循环出子级数据，添加到一级数据
            for(ArticleCategory subArticleCategory : articleCategoryList){
                if(subArticleCategory.getFatherId()==articleCategory.getId()){
                    if (articleCategory.getChildren() == null) {
                        articleCategory.setChildren(new ArrayList<ArticleCategory>());
                    }
                    articleCategory.getChildren().add(subArticleCategory);

                }
            }

        }
        return articleCategoryTree;
    }

}
