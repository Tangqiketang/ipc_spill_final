package com.zdhk.ipc.protocal.elasticsearch.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
/*import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;*/

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2021-03-02 9:35
 */
// indexName索引名称，type类型
@Document(indexName = "houseindex", type = "house")
@Getter
@Setter
public class HouseIndexTemplate {

    @Id
    private Long houseId;

    // 使用分词器
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String title;

    @Field(type = FieldType.Keyword)
    private String name;

    @Field(type = FieldType.Integer)
    private int price;
}
