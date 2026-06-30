package com.haha.blog.web.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.haha.blog.common.domain.dos.ArticleDO;
import com.haha.blog.common.mapper.ArticleMapper;
import com.haha.blog.common.utils.BeanUtils;
import com.haha.blog.common.utils.PageDTO;
import com.haha.blog.web.domain.query.archive.ArchiveArticlePageQuery;
import com.haha.blog.web.domain.vo.archive.ArchiveArticlePageVO;
import com.haha.blog.web.domain.vo.archive.ArchiveArticleVO;
import com.haha.blog.web.service.IArchiveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArchiveServiceImpl implements IArchiveService {

    private final ArticleMapper articleMapper;

    @Override
    public PageDTO<ArchiveArticlePageVO> queryArchiveArticlePage(ArchiveArticlePageQuery query) {
        Long current = query.getCurrent();
        Long size = query.getSize();
        // 分页查询文章数据
        Page<ArticleDO> page = articleMapper.selectPage(new Page<>(current, size), null);
        List<ArticleDO> records = page.getRecords();
        if(CollectionUtil.isEmpty(records)){
            return PageDTO.success(page, Collections.emptyList());
        }
        // DO转VO
        List<ArchiveArticleVO> vos = records.stream()
                .map(vo -> {
                    ArchiveArticleVO archiveArticleVO = BeanUtils.copyBean(vo, ArchiveArticleVO.class);
                    archiveArticleVO.setCreateDate(vo.getCreateTime().toLocalDate());
                    archiveArticleVO.setCreateMonth(YearMonth.from(vo.getCreateTime()));
                    return archiveArticleVO;
                })
                .collect(Collectors.toList());
        Map<YearMonth, List<ArchiveArticleVO>> map = vos.stream().collect(Collectors.groupingBy(ArchiveArticleVO::getCreateMonth));
        // 使用 TreeMap 按月份倒序排列
        Map<YearMonth, List<ArchiveArticleVO>> sortedMap = new TreeMap<>(Collections.reverseOrder());
        sortedMap.putAll(map);
        // 遍历排序后的 Map，封装结果 VO
        List<ArchiveArticlePageVO> list = new ArrayList<>();
        sortedMap.forEach((k, v) ->
                list.add(new ArchiveArticlePageVO().setMonth(k).setArticles(v))
        );
        return PageDTO.success(page,list);
    }
}
