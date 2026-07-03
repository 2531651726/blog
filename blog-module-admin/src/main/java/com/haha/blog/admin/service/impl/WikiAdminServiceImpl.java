package com.haha.blog.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.haha.blog.admin.domain.dto.wiki.*;
import com.haha.blog.admin.domain.query.wiki.WikiCatalogListQuery;
import com.haha.blog.admin.domain.query.wiki.WikiPageListQuery;
import com.haha.blog.admin.domain.vo.wiki.WikiCatalogListVO;
import com.haha.blog.admin.domain.vo.wiki.WikiPageListVO;
import com.haha.blog.admin.service.IWikiAdminService;
import com.haha.blog.common.domain.dos.WikiCatalogDO;
import com.haha.blog.common.domain.dos.WikiDO;
import com.haha.blog.common.enums.ArticleType;
import com.haha.blog.common.enums.PublishStatus;
import com.haha.blog.common.enums.WikiCatalogLevel;
import com.haha.blog.common.exception.BizException;
import com.haha.blog.common.mapper.ArticleMapper;
import com.haha.blog.common.mapper.WikiCatalogMapper;
import com.haha.blog.common.mapper.WikiMapper;
import com.haha.blog.common.utils.BeanUtils;
import com.haha.blog.common.utils.PageDTO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WikiAdminServiceImpl extends ServiceImpl<WikiMapper, WikiDO> implements IWikiAdminService {

    private final WikiMapper wikiMapper;
    private final WikiCatalogMapper wikiCatalogMapper;
    private final ArticleMapper articleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertWiki(InsertWikiDTO dto) {
        String title = dto.getTitle();
        String summary = dto.getSummary();
        String cover = dto.getCover();
        WikiDO wikiDO = new WikiDO()
                .setTitle(title)
                .setSummary(summary)
                .setCover(cover);
        wikiMapper.insert(wikiDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteWiki(DeleteWikiDTO dto) {
        Long wikiId = dto.getId();
        // 删除知识库
        int count = wikiMapper.deleteById(wikiId);
        if (count == 0) {
            throw new BizException("知识库不存在");
        }
        // 通过知识库id查找目录
        List<WikiCatalogDO> wikiCatalogDOS = wikiCatalogMapper.selectListByWikiId(wikiId);
        // 过滤目录中所有文章的 ID
        List<Long> articleIds = wikiCatalogDOS.stream()
                .filter(wikiCatalogDO -> Objects.nonNull(wikiCatalogDO.getArticleId())  // 文章 ID 不为空
                        && Objects.equals(wikiCatalogDO.getLevel(), WikiCatalogLevel.SECOND_LEVEL)) // 二级目录
                .map(WikiCatalogDO::getArticleId) // 提取文章 ID
                .collect(Collectors.toList());
        // 更新文章类型 type 为普通
        if (!CollectionUtils.isEmpty(articleIds)) {
            articleMapper.updateTypeByIds(ArticleType.NORMAL,articleIds);
        }
        // 删除知识库目录
        wikiCatalogMapper.deleteByWikiId(wikiId);
    }

    @Override
    public PageDTO<WikiPageListVO> queryWikiPageList(WikiPageListQuery query) {
        Long current = query.getCurrent();
        Long size = query.getSize();
        // 获取查询条件
        String title = query.getTitle();
        LocalDate startDate = query.getStartDate();
        LocalDate endDate = query.getEndDate();
        LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = endDate != null ? endDate.atTime(LocalTime.MAX) : null;
        // 分页查询
        Page<WikiDO> page = lambdaQuery()
                .like(StringUtils.isNoneBlank(title), WikiDO::getTitle, title)
                .ge(Objects.nonNull(startDateTime), WikiDO::getCreateTime, startDateTime)
                .le(Objects.nonNull(endDateTime), WikiDO::getCreateTime, endDateTime)
                .orderByDesc(WikiDO::getWeight) // 按权重倒序
                .orderByDesc(WikiDO::getCreateTime)
                .page(new Page<>(current, size));
        List<WikiDO> records = page.getRecords();
        // DO 转 VO
        List<WikiPageListVO> vos = null;
        if (!CollectionUtils.isEmpty(records)) {
            vos = records.stream()
                    .map(articleDO -> {
                        WikiPageListVO wikiVo = BeanUtils.copyBean(articleDO, WikiPageListVO.class);
                        wikiVo.setIsTop(articleDO.getWeight() > 0);
                        return wikiVo;
                    })
                    .collect(Collectors.toList());
        }
        return PageDTO.success(page, vos);
    }

    @Override
    public void updateWikiWeight(UpdateWikiWeightDTO dto) {
        Long wikiId = dto.getId();
        Integer weight = dto.getWeight();
        WikiDO wikiDO = wikiMapper.selectById(wikiId);
        if (Objects.isNull(wikiDO)) {
            throw new BizException("该知识库不存在");
        }
        WikiDO wikiUpdateDO = new WikiDO().setId(wikiId).setWeight(weight);
        int row = wikiMapper.updateById(wikiUpdateDO);
        if (row == 0) {
            throw new BizException("知识库权重更新失败");
        }
    }

    @Override
    public void updateWikiPublishStatus(UpdateWikiPublishStatusDTO dto) {
        Long wikiId = dto.getId();
        PublishStatus publishStatus = dto.getIsPublish();
        WikiDO wikiDO = new WikiDO().setId(wikiId).setIsPublish(publishStatus);
        int row = wikiMapper.updateById(wikiDO);
        if (row == 0) {
            throw new BizException("更新知识库发布状态失败");
        }
    }

    @Override
    public void updateWiki(UpdateWikiDTO dto) {
        WikiDO wikiDO = new WikiDO()
                .setId(dto.getId())
                .setTitle(dto.getTitle())
                .setSummary(dto.getSummary())
                .setCover(dto.getCover());
        int row = wikiMapper.updateById(wikiDO);
        if (row == 0) {
            throw new BizException("更新知识库失败");
        }
    }

    @Override
    public List<WikiCatalogListVO> queryWikiCatalogList(WikiCatalogListQuery query) {
        Long wikiId = query.getId();
        // 查询该知识库下所有目录
        List<WikiCatalogDO> wikiCatalogDOList = wikiCatalogMapper.selectListByWikiId(wikiId);
        // DO 转 VO
        List<WikiCatalogListVO> vos = null;
        if (!CollectionUtils.isEmpty(wikiCatalogDOList)) {
            // 一级目录
            List<WikiCatalogDO> firstLevelDOs = wikiCatalogDOList.stream()
                    .filter(catalogDO -> Objects.equals(catalogDO.getLevel(), WikiCatalogLevel.FIRST_LEVEL))
                    .sorted(Comparator.comparing(WikiCatalogDO::getSort))
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(firstLevelDOs)) {
                return vos;
            }
            // 一级目录 DO 转 VO
            vos = firstLevelDOs.stream()
                    .map(DO -> {
                        WikiCatalogListVO vo = BeanUtils.copyBean(DO, WikiCatalogListVO.class);
                        vo.setEditing(Boolean.FALSE);
                        return vo;
                    })
                    .collect(Collectors.toList());
            // 封装二级目录
            vos.forEach(firstLevelVO -> {
                Long parentId = firstLevelVO.getId();
                // 提取二级目录
                List<WikiCatalogDO> secondLevelDOs = wikiCatalogDOList.stream()
                        .filter(catalogDO -> Objects.equals(catalogDO.getParentId(), parentId)
                                && Objects.equals(catalogDO.getLevel(), WikiCatalogLevel.SECOND_LEVEL))
                        .sorted(Comparator.comparing(WikiCatalogDO::getSort))
                        .collect(Collectors.toList());
                // 二级目录 DO 转 VO
                if (!CollectionUtils.isEmpty(secondLevelDOs)) {
                    List<WikiCatalogListVO> secondLevelVOs = secondLevelDOs.stream()
                            .map(DO -> {
                                WikiCatalogListVO vo = BeanUtils.copyBean(DO, WikiCatalogListVO.class);
                                vo.setEditing(Boolean.FALSE);
                                return vo;
                            })
                            .collect(Collectors.toList());
                    firstLevelVO.setChildren(secondLevelVOs);
                }
            });
        }
        return vos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWikiCatalogs(UpdateWikiCatalogDTO dto) {
        Long wikiId = dto.getId();
        List<UpdateWikiCatalogItemDTO> catalogs = dto.getCatalogs();
        // 1. 先将此知识库中的所有文章类型更新为普通
        // 查出此 wiki 下所有的文章 ID
        List<WikiCatalogDO> wikiCatalogDOS = wikiCatalogMapper.selectListByWikiId(wikiId);
        List<Long> articleIds = wikiCatalogDOS.stream()
                .map(WikiCatalogDO::getArticleId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        // 更新文章类型为普通文章
        if (!CollectionUtils.isEmpty(articleIds)) {
            articleMapper.updateTypeByIds(ArticleType.NORMAL, articleIds);
        }
        // 2. 删除该知识库下所有目录
        wikiCatalogMapper.deleteByWikiId(wikiId);
        // 3. 插入新的目录数据
        if (!CollectionUtils.isEmpty(catalogs)) {
            // 重新设置 sort 排序字段的值
            for (int i = 0; i < catalogs.size(); i++) {
                UpdateWikiCatalogItemDTO catalogItemdto = catalogs.get(i);
                List<UpdateWikiCatalogItemDTO> children = catalogItemdto.getChildren();
                catalogItemdto.setSort(i + 1);
                if (!CollectionUtils.isEmpty(children)) {
                    for (int j = 0; j < children.size(); j++) {
                        children.get(j).setSort(j + 1);
                    }
                }
            }
            // vo 转 do
            List<WikiCatalogDO> wikiCatalogList = new ArrayList<>();
            List<Long> updateArticleIds = new ArrayList<>();
            catalogs.forEach(firstCatalog -> {
                // 插入一级目录
                WikiCatalogDO firstCatalogDO = new WikiCatalogDO()
                        .setWikiId(wikiId)
                        .setTitle(firstCatalog.getTitle())
                        .setLevel(firstCatalog.getLevel())
                        .setSort(firstCatalog.getSort());
                wikiCatalogMapper.insert(firstCatalogDO);
                Long catalogId = firstCatalogDO.getId();
                // 插入二级目录
                List<UpdateWikiCatalogItemDTO> children = firstCatalog.getChildren();
                if (!CollectionUtils.isEmpty(children)) {
                    children.forEach(secondCatalog -> {
                        WikiCatalogDO secondCatalogDO = new WikiCatalogDO()
                                .setWikiId(wikiId)
                                .setTitle(secondCatalog.getTitle())
                                .setLevel(secondCatalog.getLevel())
                                .setSort(secondCatalog.getSort())
                                .setParentId(catalogId)
                                .setCreateTime(LocalDateTime.now())
                                .setUpdateTime(LocalDateTime.now())
                                .setIsDeleted(0);
                        if(secondCatalog.getArticleId() != null) {
                            secondCatalogDO.setArticleId(secondCatalog.getArticleId());
                            updateArticleIds.add(secondCatalog.getArticleId());
                        }
                        wikiCatalogList.add(secondCatalogDO);
                    });
                }
            });
            if (!CollectionUtils.isEmpty(wikiCatalogList)) {
                wikiCatalogMapper.insertBatchSomeColumn(wikiCatalogList);
            }
            // 更新文章类型
            if (!CollectionUtils.isEmpty(updateArticleIds)) {
                articleMapper.updateTypeByIds(ArticleType.WIKI, updateArticleIds);
            }

        }
    }
}
