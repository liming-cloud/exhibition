package cn.exhibition.admin.mapper;

import cn.exhibition.admin.database.BaseMapperPlus;
import cn.exhibition.admin.domain.entity.ExhibitionDetailPo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExhibitionDetailMapper extends BaseMapperPlus<ExhibitionDetailPo,ExhibitionDetailPo> {
}