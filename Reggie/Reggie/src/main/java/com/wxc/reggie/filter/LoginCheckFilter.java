package com.wxc.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.wxc.reggie.common.BaseContext;
import com.wxc.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否已经完成登入
 *
 * @WebServlet : 声明一个自定义的 Servlet
 * @WebFilter : 声明一个Servlet 过滤器
 * @WebListener : 声明一个类为 Servlet 监听器
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 1. 获取本次请求的URI
        String requestURL = request.getRequestURI();

        log.info("拦截到的请求: {}", requestURL);

        // 定义可以放行的资源
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg", // 移动端发送短信
                "/user/login"    // 移动端登入
        };

        // 2. 判断本次请求, 是否需要登录, 才可以访问
        boolean check = check(urls, requestURL);

        // 3. 如果不需要，则直接放行
        if (check) {
            log.info("不需要处理的请求: {}", requestURL);
            filterChain.doFilter(request, response);
            return;
        }

        // 4-1. 判断登录状态，如果已登录，则直接放行(项目后台用户)
        if (request.getSession().getAttribute("employee") != null) {

            log.info("用户已登入，用户ID为: {}", request.getSession().getAttribute("employee"));
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request, response);
            return;
        }

        // 4-2. 判断登录状态，如果已登录，则直接放行（移动端用户登入）
        if (request.getSession().getAttribute("user") != null) {

            log.info("用户已登入，用户ID为: {}", request.getSession().getAttribute("user"));
            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request, response);
            return;
        }

        log.info("用户未登入...");

        // 5. 如果未登录, 则返回未登录结果，通过输出流的方式向客户端响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     *
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            // URL匹配
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
