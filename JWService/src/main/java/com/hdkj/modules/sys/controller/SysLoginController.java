/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.hdkj.modules.sys.controller;


import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.hdkj.common.utils.AesUtil;
import com.hdkj.common.utils.R;
import com.hdkj.modules.sys.service.SysUserService;
import com.hdkj.modules.sys.shiro.ShiroUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录相关
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月10日 下午1:15:31
 */
@Controller
public class SysLoginController {

    @Autowired
    private Producer producer;
    @Autowired
    private SysUserService sysUserService;

    //创建全局map用来保存用户登录错误次数信息
    private Map<String, Integer> maps = new HashMap<>();
    private String passwordSum = "5";

    @RequestMapping("captcha.jpg")
    public void captcha(HttpServletResponse response) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //生成文字验证码
        String text = producer.createText();
        //生成图片验证码
        BufferedImage image = producer.createImage(text);
        //保存到shiro session
        ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
    }

    /**
     * 登录
     */
    @ResponseBody
    @RequestMapping(value = "/sys/login", method = RequestMethod.POST)
    public R login(String user) {
        try {
            //解密user
            String aesUser = AesUtil.aesDecrypt(user.replaceAll(" ","+"));
            //获取第一个“=”的位置
            int index1 = aesUser.indexOf("=");
            //获取第二个“=”的位置
            int index2 = aesUser.indexOf("=", index1 + 1);
            //获取第三个“=”的位置
            int index3 = aesUser.indexOf("=", index2 + 1);
            //获取第一个“&”的位置
            int index4 = aesUser.indexOf("&");
            //获取第二个“&”的位置
            int index5 = aesUser.indexOf("&", index4 + 1);
            //截取用户名
            String username = aesUser.substring(index1+1, index4);
            //截取密码
            String password = aesUser.substring(index2+1, index5);
            //截取验证码
            String captcha = aesUser.substring(index3+1);
            String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
            if (!captcha.equalsIgnoreCase(kaptcha)) {
                return R.error("验证码不正确");
            }
            if (maps == null || maps.size() < 1) {
                maps.put(username, Integer.parseInt(passwordSum));
            }
            try {
                Subject subject = ShiroUtils.getSubject();
                UsernamePasswordToken token = new UsernamePasswordToken(username, password);
                subject.login(token);
            } catch (UnknownAccountException e) {
                return R.error(e.getMessage());
            } catch (IncorrectCredentialsException e) {
                //错误一次减一次机会
                maps.put(username, maps.get(username) - 1);
                //判断密码错误次数是否超过三次
                if (maps.get(username) <= 0) {
                    //修改用户状态
                    sysUserService.updateStatus(username);
                    return R.error("您的账号已被冻结！");
                }
                return R.error("账号或密码不正确，您还有" + maps.get(username) + "次机会");
            } catch (LockedAccountException e) {
                return R.error("账号已被锁定,请联系管理员");
            } catch (AuthenticationException e) {
                return R.error("账户验证失败");
            }
        } catch (Exception e) {
            return R.error("未知异常，请联系管理员");
        }
        return R.ok();
    }

    /**
     * 退出
     */
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout() {
        ShiroUtils.logout();
        return "redirect:login.html";
    }

}
