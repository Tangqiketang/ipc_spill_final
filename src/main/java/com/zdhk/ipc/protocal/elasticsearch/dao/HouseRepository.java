package com.zdhk.ipc.protocal.elasticsearch.dao;

import com.zdhk.ipc.protocal.elasticsearch.entity.HouseIndexTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseRepository extends ElasticsearchRepository<HouseIndexTemplate, Long> {
}
