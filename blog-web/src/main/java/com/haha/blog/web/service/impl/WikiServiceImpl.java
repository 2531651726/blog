package com.haha.blog.web.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haha.blog.admin.domain.vo.wiki.WikiCatalogListVO;
import com.haha.blog.common.domain.dos.WikiCatalogDO;
import com.haha.blog.common.domain.dos.WikiDO;
import com.haha.blog.common.enums.PublishStatus;
import com.haha.blog.common.enums.WikiCatalogLevel;
import com.haha.blog.common.mapper.WikiCatalogMapper;
import com.haha.blog.common.mapper.WikiMapper;
import com.haha.blog.common.utils.BeanUtils;
import com.haha.blog.web.domain.query.wiki.WikiArticlePreNextQuery;
import com.haha.blog.web.domain.query.wiki.WikiCatalogQuery;
import com.haha.blog.web.domain.vo.article.PreOrNextArticleVO;
import com.haha.blog.web.domain.vo.wiki.WikiArticlePreNextVO;
import com.haha.blog.web.domain.vo.wiki.WikiCatalogVO;
import com.haha.blog.web.domain.vo.wiki.WikiVO;
import com.haha.blog.web.service.IWikiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WikiServiceImpl extends ServiceImpl<WikiMapper, WikiDO> implements IWikiService {

    private final WikiMapper wikiMapper;
    private final WikiCatalogMapper wikiCatalogMapper;

    @Override
    public List<WikiVO> queryWikiPage() {
        List<WikiDO> wikiDOS = lambdaQuery()
                .eq(WikiDO::getIsPublish, PublishStatus.PUBLISHED)
                .orderByDesc(WikiDO::getWeight)
                .orderByDesc(WikiDO::getCreateTime)
                .list();
        List<WikiVO> voList = null;
        if(!CollectionUtil.isEmpty(wikiDOS)){
            voList = wikiDOS.stream()
                    .map(wikiDO ->{
                        WikiVO wikiVO = BeanUtils.copyBean(wikiDO, WikiVO.class);
                        wikiVO.setIsTop(wikiDO.getWeight() > 0);
                        WikiCatalogDO wikiCatalogDO = wikiCatalogMapper.selectFirstArticleId(wikiDO.getId());
                        if(!Objects.isNull(wikiCatalogDO)){
                            wikiVO.setFirstArticleId(wikiCatalogDO.getArticleId());
                        }
                        return wikiVO;
                    })
                    .collect(Collectors.toList());
        }
        return voList;
    }

    @Override
    public List<WikiCatalogVO> queryWikiCatalog(WikiCatalogQuery query) {
        Long wikiId = query.getId();
        // 获取该知识库下的所有目录
        List<WikiCatalogDO> wikiCatalogDOs = wikiCatalogMapper.selectListByWikiId(wikiId);
        List<WikiCatalogVO> vos = null;
        if(!CollectionUtil.isEmpty(wikiCatalogDOs)){
            // 一级目录
            List<WikiCatalogDO> firstLevelDOs = wikiCatalogDOs.stream()
                    .filter(catalogDO -> Objects.equals(catalogDO.getLevel(), WikiCatalogLevel.FIRST_LEVEL))
                    .sorted(Comparator.comparing(WikiCatalogDO::getSort))
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(firstLevelDOs)) {
                return vos;
            }
            // 一级目录 DO 转 VO
            vos = firstLevelDOs.stream()
                    .map(DO -> BeanUtils.copyBean(DO, WikiCatalogVO.class))
                    .collect(Collectors.toList());
            // 封装二级目录
            vos.forEach(firstLevelVO -> {
                Long parentId = firstLevelVO.getId();
                // 提取二级目录
                List<WikiCatalogDO> secondLevelDOs = wikiCatalogDOs.stream()
                        .filter(catalogDO -> Objects.equals(catalogDO.getParentId(), parentId)
                                && Objects.equals(catalogDO.getLevel(), WikiCatalogLevel.SECOND_LEVEL))
                        .sorted(Comparator.comparing(WikiCatalogDO::getSort))
                        .collect(Collectors.toList());
                // 二级目录 DO 转 VO
                if (!CollectionUtils.isEmpty(secondLevelDOs)) {
                    List<WikiCatalogVO> secondLevelVOs = secondLevelDOs.stream()
                            .map(DO -> BeanUtils.copyBean(DO, WikiCatalogVO.class))
                            .collect(Collectors.toList());
                    firstLevelVO.setChildren(secondLevelVOs);
                }
            });
        }
        return vos;
    }

    @Override
    public WikiArticlePreNextVO queryWikiArticlePreAndNext(WikiArticlePreNextQuery query) {
        Long wikiId = query.getId();
        Long articleId = query.getArticleId();
        WikiArticlePreNextVO vo = new WikiArticlePreNextVO();
        WikiCatalogDO wikiCatalogDO = wikiCatalogMapper.selectByWikiIdAndArticleId(wikiId, articleId);
        // 上一篇文章
        WikiCatalogDO wikiCatalogPre = wikiCatalogMapper.selectPreArticle(wikiId, wikiCatalogDO.getId());
        if(!Objects.isNull(wikiCatalogPre)){
            PreOrNextArticleVO preArticleVO = new PreOrNextArticleVO()
                    .setArticleId(wikiCatalogPre.getArticleId())
                    .setArticleTitle(wikiCatalogPre.getTitle());
            vo.setPreArticle(preArticleVO);
        }
        // 下一篇文章
        WikiCatalogDO wikiCatalogNext = wikiCatalogMapper.selectNextArticle(wikiId, wikiCatalogDO.getId());
        if(!Objects.isNull(wikiCatalogNext)){
            PreOrNextArticleVO nextArticleVO = new PreOrNextArticleVO()
                    .setArticleId(wikiCatalogNext.getArticleId())
                    .setArticleTitle(wikiCatalogNext.getTitle());
            vo.setNextArticle(nextArticleVO);
        }
        return vo;
    }
}
