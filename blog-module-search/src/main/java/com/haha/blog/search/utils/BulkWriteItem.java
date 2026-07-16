package com.haha.blog.search.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class BulkWriteItem {

    /** 文档ID */
    private String id;
    /** 完整文档数据 */
    private Map<String, Object> source;
}
