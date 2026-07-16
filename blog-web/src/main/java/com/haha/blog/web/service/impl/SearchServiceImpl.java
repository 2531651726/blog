package com.haha.blog.web.service.impl;

import com.haha.blog.common.utils.PageDTO;
import com.haha.blog.search.index.ArticleIndex;
import com.haha.blog.search.utils.ElasticsearchHelper;
import com.haha.blog.web.domain.dto.search.SearchArticlePageListDTO;
import com.haha.blog.web.domain.vo.search.SearchArticlePageListVO;
import com.haha.blog.web.service.ISearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchServiceImpl implements ISearchService {

    private final ElasticsearchHelper elasticsearchHelper;



    @Override
    public PageDTO<SearchArticlePageListVO> searchArticlePageList(SearchArticlePageListDTO dto) {
        long current = dto.getCurrent();
        long size = dto.getSize();
        String word = dto.getWord();
        int from = (int) ((current - 1) * size);

        // 搜索字段：标题、摘要、正文
        String[] SEARCH_FIELDS = {
                ArticleIndex.COLUMN_TITLE,
                ArticleIndex.COLUMN_SUMMARY,
                ArticleIndex.COLUMN_CONTENT
        };
        // 高亮字段（仅标题）
        String[] HIGHLIGHT_FIELDS = {
                ArticleIndex.COLUMN_TITLE
        };
        // 不返回的字段
        String[] EXCLUDED_SOURCE = {
                ArticleIndex.COLUMN_CONTENT
        };

        // 执行搜索
        SearchResponse response = elasticsearchHelper.searchWithHighlight(
                ArticleIndex.NAME, word, SEARCH_FIELDS, HIGHLIGHT_FIELDS, EXCLUDED_SOURCE, from, (int) size);

        // 解析结果
        List<SearchArticlePageListVO> voList = new ArrayList<>();
        for (SearchHit hit : response.getHits()) {
            Map<String, Object> source = hit.getSourceAsMap();
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();

            SearchArticlePageListVO vo = new SearchArticlePageListVO();
            // ID
            Object idObj = source.get(ArticleIndex.COLUMN_ID);
            vo.setId(idObj != null ? Long.valueOf(idObj.toString()) : null);
            // 封面
            Object coverObj = source.get(ArticleIndex.COLUMN_COVER);
            vo.setCover(coverObj != null ? coverObj.toString() : null);
            // 标题（高亮）
            vo.setTitle(ElasticsearchHelper.getHighlightValue(highlightFields, ArticleIndex.COLUMN_TITLE, source));
            // 摘要（原始值，无需高亮）
            Object summaryObj = source.get(ArticleIndex.COLUMN_SUMMARY);
            vo.setSummary(summaryObj != null ? summaryObj.toString() : null);
            // 发布日期
            Object createTime = source.get(ArticleIndex.COLUMN_CREATE_TIME);
            vo.setCreateDate(createTime != null ? createTime.toString() : null);
            voList.add(vo);
        }

        long total = response.getHits().getTotalHits().value;
        long pages = (long) Math.ceil((double) total / size);

        PageDTO<SearchArticlePageListVO> pageDTO = new PageDTO<>();
        pageDTO.setSuccess(true);
        pageDTO.setCurrent(current);
        pageDTO.setSize(size);
        pageDTO.setTotal(total);
        pageDTO.setPages(pages);
        pageDTO.setData(voList);
        return pageDTO;
    }
}
