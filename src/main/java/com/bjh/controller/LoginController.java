package com.bjh.controller;

import com.bjh.entity.*;
import com.bjh.service.AuthService;
import com.bjh.service.UserService;
import com.bjh.utils.DigestUtil;
import com.bjh.utils.TokenUtils;
import com.bjh.utils.WarehouseConstants;
import com.google.code.kaptcha.Producer;
import com.jhlabs.math.RidgedFBM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class LoginController {
    @Autowired
    private Producer producer;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private UserService userService;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthService authService;
    /*
     * 获取验证码的接口
     * */
    @RequestMapping("/captcha/captchaImage")
    public void captcheImage(HttpServletResponse httpServletResponse) {

        ServletOutputStream out = null;

        try {

            //生成验证码图片的文件
            String text = producer.createText();
            //使用验证码文本生成图片
            BufferedImage image = producer.createImage(text);
            //将验证码文本作为key保存到redis
            redisTemplate.opsForValue().set(text, "", 60 * 30, TimeUnit.SECONDS);

            /*
             * 将验证码图片相应给前端
             * */

            httpServletResponse.setContentType("/image/jepg");
            out = httpServletResponse.getOutputStream();
            //将验证码图片响应给前端
            ImageIO.write(image, "jpg", out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /*
     * @RequestBody LoginUser loginUser) 表示接受前端传来的json
     *
     * */

    @RequestMapping("/login")
    public Result login(@RequestBody LoginUser loginUser) {


        //拿到客户输入的验证码
        String code = loginUser.getVerificationCode();
        if (!redisTemplate.hasKey(code)){
            return Result.err(Result.CODE_ERR_BUSINESS,"验证码输入错误！");

        }
        User user = userService.queryUserByCode(loginUser.getUserCode());
        //判断账号是否存在
        if (user != null) {
            //账号存在
            //并且判断是否被审核
            if (user.getUserState().equals(WarehouseConstants.USER_STATE_PASS)) {//已审核

                //拿到用户输入的密码
                String userPwd = loginUser.getUserPwd();
                //进行MD5加密
                userPwd = DigestUtil.hmacSign(userPwd);
                if (userPwd.equals(user.getUserPwd())) {
                    //密码合法,生成token,jwt
                    CurrentUser currentUser
                            = new CurrentUser(user.getUserId(), user.getUserCode(), user.getUserName());
                    String token = tokenUtils.loginSign(currentUser, userPwd);

                    return Result.ok("登录成功",token);

                } else {
                    //密码错误
                    return Result.err(Result.CODE_ERR_BUSINESS, "密码错误！");
                }
            } else {
                //未审核
                return Result.err(Result.CODE_ERR_BUSINESS, "用户未审核！");
            }
        } else {
            //不存在
            return Result.err(Result.CODE_ERR_BUSINESS, "账号不存在！");
        }
    }

    /*
    * 获取当前登录的用户账号
    *
    * */
    @RequestMapping("curr-user")
    public Result currentUser(@RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token){
        //解析token
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        return Result.ok(currentUser);

    }

    /*
    * 加载菜单树
    * */
    @RequestMapping("/user/auth-list")
    public Result laodAuthTree(@RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token){

        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        int userId = currentUser.getUserId();

        //执行业务
        List<Auth> authTreeList = authService.queryAuthTreeByUid(userId);

        return Result.ok(authTreeList);

    }

    //退出登录
    @RequestMapping("/logout")
    public Result logout(@RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token){
        //从redis中删除token的键
        redisTemplate.delete(token);
        return Result.ok("退出系统！");
    }
}
