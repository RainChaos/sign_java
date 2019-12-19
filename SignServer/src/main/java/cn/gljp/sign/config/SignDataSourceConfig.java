package cn.gljp.sign.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import javax.sql.DataSource;

@Configuration  //配置类
@MapperScan(basePackages = {"cn.gljp.sign.admin.mapper.sign"}, sqlSessionTemplateRef = "signSqlSessionTemplate")
public class SignDataSourceConfig {

    @Bean(name = "signDataSource")  // 方法返回的DataSource 绑定到signDataSource对象
    @ConfigurationProperties(prefix = "spring.datasource.sign")  //属性绑定  yml中的 spring.datasource.sign
    // 和配置文件中相关的配置进行绑定；
    public DataSource chargeDataSource() {
//        return DataSourceBuilder.create().build();
        return new AtomikosDataSourceBean();
    }
    /*1.首先SqlSessionFactoryBuilder去读取mybatis的配置文件，然后build一个DefaultSqlSessionFactory,即得到SqlSessionFactory*/
    /*2.获取到SqlSessionFactory之后，就可以利用SqlSessionFactory方法的openSession来获取SqlSession对象了。*/
    /*3.得到SqlSession对象之后就可以利用SqlSession内部的方法进行CRUD操作了,这里是  signSqlSessionTemplate。*/

    @Bean(name = "signSqlSessionFactory")  /*使用 @Bean 给容器中添加组件*/
    public SqlSessionFactory chargeSqlSessionFactory(@Qualifier("signDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setCallSettersOnNulls(true);
        bean.setConfiguration(configuration);
        return bean.getObject();
    }

//    @Bean(name = "signTransactionManager")
//    public DataSourceTransactionManager sentinelTransactionManager(@Qualifier("signDataSource") DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }

    @Bean(name = "signSqlSessionTemplate")   //@Qualifier 表明了哪个实现类才是我们所需要的
    public SqlSessionTemplate sentinelSqlSessionTemplate(@Qualifier("signSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}