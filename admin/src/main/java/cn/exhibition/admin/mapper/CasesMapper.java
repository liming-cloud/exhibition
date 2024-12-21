package cn.exhibition.admin.mapper;

import cn.exhibition.admin.database.BaseMapperPlus;
import cn.exhibition.admin.domain.entity.CasesPo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CasesMapper extends BaseMapperPlus<CasesPo,CasesPo> {
}