package cn.itxaioliu.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "cn.itxaioliu.mapper2",sqlSessionFactoryRef = "sqlSessionFactory2",
        sqlSessionTemplateRef = "sqlSessionTemplate2")
public class MybatisConfigTwo {
    @Resource(name = "dsTwo")
    DataSource dsTwo;
    @Bean
    SqlSessionFactory sqlSessionFactory2(){
        SqlSessionFactoryBean bean=new SqlSessionFactoryBean();
        try {
            bean.setDataSource(dsTwo);
            return bean.getObject();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @Bean
    SqlSessionTemplate sqlSessionTemplate2(){
        return new SqlSessionTemplate(sqlSessionFactory2());
    }
}
