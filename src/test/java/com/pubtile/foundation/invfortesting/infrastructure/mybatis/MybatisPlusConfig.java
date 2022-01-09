//package com.pubtile.foundation.invfortesting.infrastructure.mybatis;
//
//import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
//import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
//import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//@Deprecated
////@EnableTransactionManagement
//@Configuration
//public class MybatisPlusConfig {
//    /**
//     * 分页插件配置
//     * @return
//     */
//    @Bean
//    //pubtile change
//    public PaginationInnerInterceptor paginationInterceptor() {
//        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
//        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
//        // paginationInterceptor.setOverflow(false);
//        // 设置最大单页限制数量，默认 500 条，-1 不受限制
//        //默认无限制
//        //paginationInterceptor.setMaxLimit(-1L);
//        // 开启 count 的 join 优化,只针对部分 left join
//        ///paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
//        paginationInterceptor.setOptimizeJoin(true);
//        return paginationInterceptor;
//    }
//
//    /**
//     * 乐观锁配置
//     * @return
//     */
//    @Bean
//    public OptimisticLockerInnerInterceptor optimisticLockerInterceptor() {
//        return new OptimisticLockerInnerInterceptor();
//    }
//
//    @Bean
//    public MybatisPlusInterceptor mybatisPlusInterceptor() {
//        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
//        mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
//        return mybatisPlusInterceptor;
//    }
//}
