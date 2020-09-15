package com.moon.security.config;

import com.moon.security.entity.User;
import com.moon.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private UserService<User> userService;

    @Autowired
    private AuthenticationSuccessHandlerImpl authenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandlerImpl authenticationFailureHandler;

    @Autowired
    private LogoutSuccessHandlerImpl logoutSuccessHandler;

    @Autowired
    private AccessDeniedHandlerImpl accessDeniedHandler;

    @Autowired
    private AuthenticationEntryPointImpl authenticationEntryPoint;


    /**
     * 拦截
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                //未登录时，进行json格式的提示
                .authenticationEntryPoint(authenticationEntryPoint)
                /**
                 * 默认指定一个页面或者一类或者一个接口不需要登录
                 */
                .and().authorizeRequests().antMatchers("/all/**").permitAll()
                /**
                 * 接口权限 这个参数可以加到数据库 形成细粒度的权限操作,这个必须在authenticated之前
                 */
                .antMatchers("/level1/**").hasAnyRole("USER")
                .antMatchers("/level2/**").hasAnyRole("MANAGER")
                .antMatchers("/level3/**").hasAnyRole("ADMIN")
                /**
                 * 必须登录
                 */
                .anyRequest().authenticated()

                /**
                 * 替换默认登录过滤器
                 */
                .and().addFilterAt(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .formLogin() //使用自带的登录
                .permitAll()
                /**
                 * 登录失败
                 */
                .failureHandler(authenticationFailureHandler)
                /**
                 * 登录成功
                 */
                .successHandler(authenticationSuccessHandler)
                /**
                 * 未登录
                 */
                .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                /**
                 * 退出登录处理
                 */
                .and().logout().logoutSuccessHandler(logoutSuccessHandler).permitAll();
        http.cors().disable();
        //csrf 是否开启
        http.csrf().disable();

    }

    /**
     * 静态资源
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().mvcMatchers("/css/**", "/fonts/**", "/img/**", "/js/**");

    }

    /**
     * 配置认证 规则
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /**
         * auth.jdbcAuthentication().dataSource().withDefaultSchema().withUser() 可以指定数据查询
         * auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder()).withUser("moon")
         * .password(new BCryptPasswordEncoder().encode("123456")).roles("vip")普通设置权限
         * 加密推荐官方的加密方式
         */
        auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());

    }

    @Bean
    CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(authenticationFailureHandler);
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }
}
