package cn.exhibition.admin.mapper;

import cn.exhibition.admin.database.BaseMapperPlus;
import cn.exhibition.admin.domain.entity.ExhibitionRegistrationPo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExhibitionRegistrationMapper
        extends BaseMapperPlus<ExhibitionRegistrationPo, ExhibitionRegistrationPo> {
}