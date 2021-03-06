package com.liu.spring.springoauth.config.configadapter;

import com.liu.spring.springoauth.config.provider.MyCustomUserDetailsService;
import com.liu.spring.springoauth.config.token.JwtAccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.sql.DataSource;
import java.security.KeyPair;

/**
 * ??????????????????
 */
@Configuration
@EnableAuthorizationServer
public class OAuthConfigurer extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @Autowired
    private MyCustomUserDetailsService customUserDetailsService;

    @Autowired
    private DataSource dataSource;

    @Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }

    //token???????????????
//    @Bean
//    public JdbcTokenStore jdbcTokenStore(){
//        return new JdbcTokenStore(dataSource);
//    }

    //token??????redis
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    RedisTokenStore redisTokenStore(){
        return new RedisTokenStore(redisConnectionFactory);
    }


    /**
     * jdbc??????????????????:oauth_code
     * ??????Authentication code,????????????oauth???????????????,????????????????????????
     * @return
     */
//    @Bean
//    public AuthorizationCodeServices authorizationCodeServices(){
//        return new JdbcAuthorizationCodeServices(dataSource);
//    }

    /**
     * redis??????
     * ??????Authentication code,????????????oauth???????????????,????????????????????????
     * @return
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices(){
        return new RandomValueAuthorizationCodeServices() {
            @Override
            protected void store(String code, OAuth2Authentication authentication) {
                RedisConnection conn = redisConnectionFactory.getConnection();
                try {
                    conn.hSet("auth_code".getBytes("utf-8"), code.getBytes("utf-8"),SerializationUtils.serialize(authentication));
                } catch (Exception e) {
                    System.out.println("??????authentication code ??????" + e.getMessage());
                } finally {
                    conn.close();
                }
            }

            @Override
            protected OAuth2Authentication remove(String code) {
                RedisConnection conn = redisConnectionFactory.getConnection();
                OAuth2Authentication authentication = null;
                try {
                    try {
                        authentication = SerializationUtils.deserialize(conn.hGet("auth_code".getBytes("utf-8"), code.getBytes("utf-8")));
                    } catch (Exception e) {
                        return null;
                    }

                    if (null != authentication) {
                        conn.hDel("auth_code".getBytes("utf-8"), code.getBytes("utf-8"));
                    }
                    return authentication;
                } catch (Exception e) {
                    return null;
                } finally {
                    conn.close();
                }
            }
        };
    }






    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessToken();
        KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource("keystore.jks"), "tc123456".toCharArray()).getKeyPair("tycoonclient");
        converter.setKeyPair(keyPair);
        return converter;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients)
            throws Exception {
//        clients.inMemory()
        //???????????????????????????????????????oauth_client_details ?????????
//                .withClient("ssoclient")
//                .secret("ssosecret")
//                .authorizedGrantTypes("authorization_code", "refresh_token")
//                .accessTokenValiditySeconds(7200)   //??????Access Token???????????????
//                .accessTokenValiditySeconds(2)   //??????Access Token???????????????
//                .scopes("openid")
//                .scopes("all")//??????Api?????????????????????api???????????????????????????scopes????????????all?????????????????????read???write?????????????????????????????????????????????????????????????????????
//                .redirectUris("http://localhost:8005/getHello")//??????http://localhost:8005/getHello?code=XXFzbd&state=rOBMf7
//                .autoApprove(false);//??????autoApprove?????????true?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        //????????????????????????
        clients.withClientDetails(clientDetails());
    }

//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }



//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security)
            throws Exception {
        security
                .tokenKeyAccess("permitAll()")  // ??????/oauth/token_key???????????????????????????
                .checkTokenAccess("isAuthenticated()")  // ??????/oauth/check_token??????????????????????????????
                .allowFormAuthenticationForClients()
//                .passwordEncoder(passwordEncoder());
                .passwordEncoder(securityConfiguration.bCryptPasswordEncoder());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints)
            throws Exception {
        //token??????
//        endpoints.tokenStore(jdbcTokenStore());
        endpoints.tokenStore(redisTokenStore());

        endpoints.accessTokenConverter(jwtAccessTokenConverter());

        // refresh_token??????userDetailsService
        endpoints.reuseRefreshTokens(false).userDetailsService(customUserDetailsService);
        endpoints.tokenServices(defaultTokenServices());

        //??????Authentication code,????????????oauth???????????????,????????????????????????
        endpoints.authorizationCodeServices(authorizationCodeServices());
    }

    /**
     * <p>??????????????????TokenServices????????????????????????@Primary??????????????????</p>
     * @return
     */
    @Primary
    @Bean
    public DefaultTokenServices defaultTokenServices(){
        DefaultTokenServices tokenServices = new DefaultTokenServices();
//        tokenServices.setTokenStore(jdbcTokenStore());
        tokenServices.setTokenStore(redisTokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setClientDetailsService(clientDetails());
//        tokenServices.setAccessTokenValiditySeconds(60 * 60 * 12); // token?????????????????????????????????12??????
        tokenServices.setAccessTokenValiditySeconds(60 * 60 * 3); // token????????????????????????
//        tokenServices.setAccessTokenValiditySeconds(60); // token?????????????????????????????????2??????
        tokenServices.setRefreshTokenValiditySeconds(60 * 60 * 24 * 7);//??????30??????????????????7???
        return tokenServices;
    }


    /**
     * ??????, ?????????????????? vue-cli ??????????????????nginx
     */
    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean =  new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }


}
