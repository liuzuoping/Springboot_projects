package cn.itxiaoliu;

import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.realm.text.TextConfigurationRealm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroConfig {
    @Bean
    public SessionsSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm());
        return securityManager;
    }
    @Bean
    TextConfigurationRealm realm(){
        TextConfigurationRealm realm=new TextConfigurationRealm();
        realm.setUserDefinitions("xiaoliu=123,user \n admin=123,admin");
        realm.setRoleDefinitions("admin=read,write \n user=read");
        return realm;
    }
    @Bean
    ShiroFilterChainDefinition shiroFilterChainDefinition(){
        DefaultShiroFilterChainDefinition definition=new DefaultShiroFilterChainDefinition();
        definition.addPathDefinition("/doLogin","anon" );
        definition.addPathDefinition("/**","authc" );
        return definition;
    }
}
