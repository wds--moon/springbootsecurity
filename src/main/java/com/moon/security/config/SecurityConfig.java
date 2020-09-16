package com.moon.security.config;

import com.moon.security.entity.User;
import com.moon.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
                .and().authorizeRequests().antMatchers("/all/*").permitAll()
                .and().authorizeRequests().antMatchers("/oauth/*").permitAll()
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
                 * 替换默认登录页面
                 */
                .and().addFilterAt(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .formLogin() //使用自带的登录
                .permitAll()
                /**
                 * 登录失败处理
                 */
                .failureHandler(authenticationFailureHandler)
                /**
                 * 成功处理
                 */
                .successHandler(authenticationSuccessHandler)
                /**
                 * 未授权处理
                 */
                .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                .and().logout().logoutUrl("/logout").logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true).logoutSuccessHandler(logoutSuccessHandler).permitAll();
        http.cors().disable();
        //开启模拟请求，比如API POST测试工具的测试，不开启时，API POST为报403错误
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

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        AuthenticationManager manager = super.authenticationManagerBean();
        return manager;
    }

    /**
     * 把加密规则替换
     * @return
     */
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
