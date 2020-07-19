package com.apex.jwtserver.controller;

import com.apex.jwtserver.authorization.annotation.Authorization;
import com.apex.jwtserver.authorization.annotation.CurrentUserId;
import com.apex.jwtserver.authorization.model.TokenModel;
import com.apex.jwtserver.config.ResultStatus;
import com.apex.jwtserver.entity.User;
import com.apex.jwtserver.manager.TokenManager;
import com.apex.jwtserver.model.ResultModel;
import com.apex.jwtserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created Date 2020/07/18.
 */
@RestController
@RequestMapping("/user/v1")
public class UserTokenController {
    @Autowired
    private UserService userService;

    @Autowired
    private TokenManager tokenManager;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ResultModel> login(String userName, String passWord) {
        System.out.println(userName);
        System.out.println(passWord);
        if (null == userName || null == passWord) {
            return new ResponseEntity<ResultModel>(ResultModel.error(ResultStatus.DATA_NOT_NULL), HttpStatus.BAD_REQUEST);
        }

        User user = this.userService.selectByUserName(userName);

        if (null == user || // 未注册
                !user.getPassWord().equals(passWord)) { //密码错误
            // 提示用户名或者密码错误
            return new ResponseEntity<>(ResultModel.error(ResultStatus.USERNAME_OR_PASSWORD_ERROR), HttpStatus.NOT_FOUND);
        }
        // 生成一个token，保存用户登录状态
        TokenModel model = this.tokenManager.createToken(user.getUserId());
        return new ResponseEntity<>(ResultModel.ok(model), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @Authorization
    public ResponseEntity<ResultModel> logout(@CurrentUserId Integer userId) {
        System.out.println("userId: " + userId);
        this.tokenManager.deleteToken(userId);
        return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
    }
}
