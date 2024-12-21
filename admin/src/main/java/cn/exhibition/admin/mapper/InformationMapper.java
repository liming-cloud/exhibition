package cn.exhibition.admin.mapper;

import cn.exhibition.admin.database.BaseMapperPlus;
import cn.exhibition.admin.domain.entity.InformationPo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InformationMapper extends BaseMapperPlus<InformationPo,InformationPo> {
}