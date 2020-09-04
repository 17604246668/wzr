package com.tongchuang.huangshan.admin.security;

import com.tongchuang.huangshan.admin.base.LoginUser;
import com.tongchuang.huangshan.admin.manager.AsyncFactory;
import com.tongchuang.huangshan.admin.manager.AsyncManager;
import com.tongchuang.huangshan.common.constatnts.Constants;
import com.tongchuang.huangshan.common.enums.ResultEnum;
import com.tongchuang.huangshan.common.exception.HuangshanException;
import com.tongchuang.huangshan.common.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 登录校验方法
 * 
 * @author fangwenzao
 */
@Slf4j
@Component
public class SysLoginService
{
    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    /**
     * 登录验证
     * 
     * @param username 用户名
     * @param password 密码
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid)
    {
        // 用户验证
        Authentication authentication = null;
        try
        {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        }
        catch (Exception e)
        {
            if (e instanceof BadCredentialsException)
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new HuangshanException(ResultEnum.IS_ERROR.getCode(),MessageUtils.message("user.password.not.match"));
            }
            else
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new HuangshanException(ResultEnum.IS_ERROR.getCode(),e.getMessage());
            }
        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        // 生成token
        return tokenService.createToken(loginUser);
    }
}
