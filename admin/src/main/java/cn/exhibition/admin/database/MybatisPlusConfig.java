package cn.exhibition.admin.database;

import cn.exhibition.admin.config.ContactProgressHandler;
import cn.exhibition.admin.config.ExhibitionTypeHandler;
import cn.exhibition.admin.domain.enums.ContactProgress;
import cn.exhibition.admin.domain.enums.ExhibitionType;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;

/**
 * @author slm
 * @description
 */
@Configuration
public class MybatisPlusConfig {

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * 注册枚举类型处理器
     */
    @PostConstruct
    public void registerTypeHandlers() {
        TypeHandlerRegistry typeHandlerRegistry = sqlSessionFactory.getConfiguration().getTypeHandlerRegistry();
        typeHandlerRegistry.register(ExhibitionType.class, ExhibitionTypeHandler.class);
        typeHandlerRegistry.register(ContactProgress.class, ContactProgressHandler.class);
    }

}
