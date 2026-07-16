package com.haha.blog.search.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ElasticsearchHelper {

    private final RestHighLevelClient restHighLevelClient;
    private final ObjectMapper objectMapper;

    /**
     * 判断索引是否存在
     */
    public boolean indexExists(String indexName) {
        if (!StringUtils.hasText(indexName)) {
            throw new IllegalArgumentException("索引名称不能为空");
        }
        try {
            GetIndexRequest request = new GetIndexRequest(indexName);
            return restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("判断索引[{}]是否存在时发生异常", indexName, e);
            throw new RuntimeException("ES服务异常，无法校验索引状态", e);
        }
    }

    /**
     * 创建索引
     */
    public void createIndex(String indexName, String mappingJson){
        // 前置参数校验
        if (!StringUtils.hasText(indexName)) {
            log.error("创建索引失败：索引名称不能为空");
            throw new IllegalArgumentException("创建索引失败：索引名称indexName不能为空");
        }
        if (!StringUtils.hasText(mappingJson)) {
            log.error("创建索引[{}]失败：mapping结构不能为空", indexName);
            throw new IllegalArgumentException("创建索引["+indexName+"]失败：mappingJson不能为空");
        }
        // 判断索引是否已存在,存在直接返回
        if(indexExists(indexName)){
            log.info("索引[{}]已存在，无需重复创建", indexName);
            return;
        }
        try {
            // 索引不存在，执行创建
            CreateIndexRequest createRequest = new CreateIndexRequest(indexName);
            createRequest.source(mappingJson, XContentType.JSON);
            restHighLevelClient.indices().create(createRequest, RequestOptions.DEFAULT);
            log.info("索引[{}]创建成功", indexName);
        } catch (IOException e) {
            log.error("索引[{}]创建操作异常", indexName, e);
            throw new RuntimeException("索引创建失败: " + indexName, e);
        }
    }


    // ==================== 文档操作 ====================

    /**
     * 新增文档（指定文档ID）
     *
     * @param indexName    索引名称
     * @param documentId   文档ID
     * @param documentJson 文档JSON数据
     * @return 文档ID
     */
    public String addDocument(String indexName, String documentId, String documentJson) {
        if (!StringUtils.hasText(indexName)) {
            throw new IllegalArgumentException("索引名称不能为空");
        }
        if (!StringUtils.hasText(documentId)) {
            throw new IllegalArgumentException("文档ID不能为空");
        }
        if (!StringUtils.hasText(documentJson)) {
            throw new IllegalArgumentException("文档数据不能为空");
        }
        try {
            IndexRequest request = new IndexRequest(indexName)
                    .id(documentId)
                    .source(documentJson, XContentType.JSON);
            IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
            log.info("文档新增成功，索引[{}]，文档ID[{}]，结果[{}]", indexName, documentId, response.getResult());
            return response.getId();
        } catch (IOException e) {
            log.error("新增文档失败，索引[{}]，文档ID[{}]", indexName, documentId, e);
            throw new RuntimeException("新增文档失败: " + indexName, e);
        }
    }

    /**
     * 新增文档（自动生成文档ID）
     *
     * @param indexName    索引名称
     * @param documentJson 文档JSON数据
     * @return 文档ID
     */
    public String addDocument(String indexName, String documentJson) {
        if (!StringUtils.hasText(indexName)) {
            throw new IllegalArgumentException("索引名称不能为空");
        }
        if (!StringUtils.hasText(documentJson)) {
            throw new IllegalArgumentException("文档数据不能为空");
        }
        try {
            IndexRequest request = new IndexRequest(indexName)
                    .source(documentJson, XContentType.JSON);
            IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
            log.info("文档新增成功，索引[{}]，文档ID[{}]，结果[{}]", indexName, response.getId(), response.getResult());
            return response.getId();
        } catch (IOException e) {
            log.error("新增文档失败，索引[{}]", indexName, e);
            throw new RuntimeException("新增文档失败: " + indexName, e);
        }
    }

    /**
     * 新增文档（使用Map数据，指定文档ID）
     *
     * @param indexName  索引名称
     * @param documentId 文档ID
     * @param document   文档数据Map
     * @return 文档ID
     */
    public String addDocument(String indexName, String documentId, Map<String, Object> document) {
        try {
            String json = objectMapper.writeValueAsString(document);
            return addDocument(indexName, documentId, json);
        } catch (JsonProcessingException e) {
            log.error("文档JSON序列化失败", e);
            throw new RuntimeException("文档JSON序列化失败", e);
        }
    }

    /**
     * 根据ID删除文档
     *
     * @param indexName  索引名称
     * @param documentId 文档ID
     * @return 是否删除成功
     */
    public boolean deleteDocument(String indexName, String documentId) {
        if (!StringUtils.hasText(indexName)) {
            throw new IllegalArgumentException("索引名称不能为空");
        }
        if (!StringUtils.hasText(documentId)) {
            throw new IllegalArgumentException("文档ID不能为空");
        }
        try {
            DeleteRequest request = new DeleteRequest(indexName, documentId);
            DeleteResponse response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
            log.info("文档删除成功，索引[{}]，文档ID[{}]，结果[{}]", indexName, documentId, response.getResult());
            return true;
        } catch (IOException e) {
            log.error("删除文档失败，索引[{}]，文档ID[{}]", indexName, documentId, e);
            throw new RuntimeException("删除文档失败: " + indexName, e);
        }
    }

    /**
     * 全量修改文档（覆盖式更新，若文档不存在则新增）
     * <p>本质是使用相同文档ID执行 Index 操作进行覆盖写入</p>
     *
     * @param indexName    索引名称
     * @param documentId   文档ID
     * @param documentJson 完整的文档JSON数据
     * @return 文档ID
     */
    public String fullUpdateDocument(String indexName, String documentId, String documentJson) {
        return addDocument(indexName, documentId, documentJson);
    }

    /**
     * 全量修改文档（使用Map数据）
     *
     * @param indexName  索引名称
     * @param documentId 文档ID
     * @param document   完整的文档数据Map
     * @return 文档ID
     */
    public String fullUpdateDocument(String indexName, String documentId, Map<String, Object> document) {
        try {
            String json = objectMapper.writeValueAsString(document);
            return fullUpdateDocument(indexName, documentId, json);
        } catch (JsonProcessingException e) {
            log.error("文档JSON序列化失败", e);
            throw new RuntimeException("文档JSON序列化失败", e);
        }
    }

    /**
     * 局部更新文档（仅更新传入的字段，不影响其他字段）
     *
     * @param indexName  索引名称
     * @param documentId 文档ID
     * @param updates    需要更新的字段Map
     * @return 是否更新成功
     */
    public boolean partialUpdateDocument(String indexName, String documentId, Map<String, Object> updates) {
        if (!StringUtils.hasText(indexName)) {
            throw new IllegalArgumentException("索引名称不能为空");
        }
        if (!StringUtils.hasText(documentId)) {
            throw new IllegalArgumentException("文档ID不能为空");
        }
        if (updates == null || updates.isEmpty()) {
            throw new IllegalArgumentException("更新数据不能为空");
        }
        try {
            UpdateRequest request = new UpdateRequest(indexName, documentId)
                    .doc(updates);
            UpdateResponse response = restHighLevelClient.update(request, RequestOptions.DEFAULT);
            log.info("文档局部更新成功，索引[{}]，文档ID[{}]，结果[{}]", indexName, documentId, response.getResult());
            return true;
        } catch (IOException e) {
            log.error("局部更新文档失败，索引[{}]，文档ID[{}]", indexName, documentId, e);
            throw new RuntimeException("局部更新文档失败: " + indexName, e);
        }
    }

    /**
     * 根据ID查询文档
     *
     * @param indexName  索引名称
     * @param documentId 文档ID
     * @return 文档数据Map（文档不存在时返回null）
     */
    public Map<String, Object> getDocument(String indexName, String documentId) {
        if (!StringUtils.hasText(indexName)) {
            throw new IllegalArgumentException("索引名称不能为空");
        }
        if (!StringUtils.hasText(documentId)) {
            throw new IllegalArgumentException("文档ID不能为空");
        }
        try {
            GetRequest request = new GetRequest(indexName, documentId);
            GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
            if (!response.isExists()) {
                log.warn("文档不存在，索引[{}]，文档ID[{}]", indexName, documentId);
                return null;
            }
            return response.getSourceAsMap();
        } catch (IOException e) {
            log.error("查询文档失败，索引[{}]，文档ID[{}]", indexName, documentId, e);
            throw new RuntimeException("查询文档失败: " + indexName, e);
        }
    }

    /**
     * 批量写入文档（全量覆盖，适用于新索引初始化或整块数据重刷）
     *
     * @param indexName 索引名称
     * @param items     批量写入条目列表
     * @return 是否全部成功
     */
    public boolean bulkWriteDocuments(String indexName, List<BulkWriteItem> items) {
        if (!StringUtils.hasText(indexName)) {
            throw new IllegalArgumentException("索引名称不能为空");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("批量写入条目列表不能为空");
        }
        try {
            BulkRequest bulkRequest = new BulkRequest();
            for (BulkWriteItem item : items) {
                IndexRequest indexRequest = new IndexRequest(indexName)
                        .id(item.getId())
                        .source(item.getSource());
                bulkRequest.add(indexRequest);
            }
            BulkResponse response = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            if (response.hasFailures()) {
                String failureMsg = response.buildFailureMessage();
                log.error("批量写入文档存在失败项，索引[{}]，失败信息[{}]", indexName, failureMsg);
                return false;
            }
            log.info("批量写入文档成功，索引[{}]，写入数量[{}]", indexName, items.size());
            return true;
        } catch (IOException e) {
            log.error("批量写入文档失败，索引[{}]", indexName, e);
            throw new RuntimeException("批量写入文档失败: " + indexName, e);
        }
    }


    // ==================== 搜索操作 ====================

    /**
     * 关键词多字段搜索（分页 + 高亮）
     * <p>对指定字段执行关键词查询，并对指定字段进行高亮标记。结果中不包含 excludedSourceFields 字段</p>
     *
     * @param indexName            索引名称
     * @param keyword              搜索关键词
     * @param searchFields         搜索字段
     * @param highlightFields      高亮字段
     * @param excludedSourceFields 不返回的源字段
     * @param from                 分页起始偏移量
     * @param size                 每页数量
     * @return {@link SearchResponse} 搜索响应（包含总数和高亮结果）
     */
    public SearchResponse searchWithHighlight(String indexName, String keyword,
                                             String[] searchFields,
                                             String[] highlightFields,
                                             String[] excludedSourceFields,
                                             int from, int size) {
        if (!StringUtils.hasText(indexName)) {
            throw new IllegalArgumentException("索引名称不能为空");
        }
        if (!StringUtils.hasText(keyword)) {
            throw new IllegalArgumentException("搜索关键词不能为空");
        }
        if (searchFields == null || searchFields.length == 0) {
            throw new IllegalArgumentException("搜索字段不能为空");
        }
        try {
            // 多字段匹配查询
            MultiMatchQueryBuilder queryBuilder = new MultiMatchQueryBuilder(keyword, searchFields)
                    .operator(Operator.OR)
                    .type(MultiMatchQueryBuilder.Type.BEST_FIELDS);
            // 高亮配置
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            for (String field : highlightFields) {
                highlightBuilder.field(new HighlightBuilder.Field(field)
                        .preTags("<span style=\"color: #f73131\">")
                        .postTags("</span>")
                        .fragmentSize(200)
                        .numOfFragments(1));
            }
            // 组装查询
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                    .query(queryBuilder)
                    .highlighter(highlightBuilder)
                    .from(from)
                    .size(size);
            // 过滤返回字段（排除指定字段）
            if (excludedSourceFields != null && excludedSourceFields.length > 0) {
                sourceBuilder.fetchSource(null, excludedSourceFields);
            }
            SearchRequest searchRequest = new SearchRequest(indexName).source(sourceBuilder);
            log.info("执行搜索，索引[{}]，关键词[{}]，搜索字段{}，分页[{}-{}]",
                    indexName, keyword, Arrays.toString(searchFields), from, size);
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            log.info("搜索完成，索引[{}]，关键词[{}]，命中总数[{}]",
                    indexName, keyword, response.getHits().getTotalHits().value);
            return response;
        } catch (IOException e) {
            log.error("搜索失败，索引[{}]，关键词[{}]", indexName, keyword, e);
            throw new RuntimeException("搜索失败: " + indexName, e);
        }
    }

    /**
     * 获取高亮后的字段值，若无高亮片段则返回原始值
     *
     * @param highlightFields ES 返回的高亮字段 Map
     * @param fieldName       字段名
     * @param source          文档源数据
     * @return 高亮后的值或原始值
     */
    public static String getHighlightValue(Map<String, HighlightField> highlightFields,
                                           String fieldName,
                                           Map<String, Object> source) {
        HighlightField highlightField = highlightFields.get(fieldName);
        if (highlightField != null && highlightField.getFragments().length > 0) {
            return highlightField.getFragments()[0].string();
        }
        Object original = source.get(fieldName);
        return original != null ? original.toString() : "";
    }
}
