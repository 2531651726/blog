package com.haha.blog.search.index;

public final class ArticleIndex {
    private ArticleIndex() {
        throw new AssertionError("禁止实例化常量类");
    }
    // 索引名称
    public static final String NAME = "article";

    // 文档字段
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_COVER = "cover";
    public static final String COLUMN_SUMMARY = "summary";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_CREATE_TIME = "createTime";

    // 索引Mapping结构JSON
    public static final String MAPPING = "{\n" +
            "  \"mappings\": {\n" +
            "    \"properties\": {\n" +
            "      \"id\": {\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"title\": {\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "        \"search_analyzer\": \"ik_smart\"\n" +
            "      },\n" +
            "      \"cover\": {\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"summary\": {\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "        \"search_analyzer\": \"ik_smart\"\n" +
            "      },\n" +
            "      \"content\": {\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "        \"search_analyzer\": \"ik_smart\"\n" +
            "      },\n" +
            "      \"createTime\": {\n" +
            "        \"type\": \"date\",\n" +
            "        \"format\": \"yyyy-MM-dd HH:mm:ss||yyyy-MM-dd'T'HH:mm:ss||epoch_millis\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";


}
