package com.bjh.filter;

import com.alibaba.fastjson.JSON;
import com.bjh.entity.Result;
import com.bjh.utils.WarehouseConstants;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class LoginCheckFilter implements Filter {

    private StringRedisTemplate redisTemplate;

    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        //创建list放白名单url接口
        List<String> urlList = new ArrayList<>();
        urlList.add("/captcha/captchaImage");
        urlList.add("/login");
        urlList.add("/product/img-upload");

        //白名单请求直接放行
        String url = request.getServletPath();

        if (urlList.contains(url)||url.contains("/img/upload")){
            filterChain.doFilter(request,response);
            return;
        }
        //是否携带token
        //1:有token
        String token = request.getHeader(WarehouseConstants.HEADER_TOKEN_NAME);
        if (StringUtils.hasText(token)&&redisTemplate.hasKey(token)){
            filterChain.doFilter(request,response);
            return;
        }
        //2：没有token
        /*Map<String,Object> data = new HashMap<>();
        data.put("code",401);
        data.put("message",)*/
        Result result = Result.err(Result.CODE_ERR_UNLOGINED,"您尚未登录！");
        String jsonStr = JSON.toJSONString(result);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.write(jsonStr);
        out.flush();
        out.close();


    }
}
