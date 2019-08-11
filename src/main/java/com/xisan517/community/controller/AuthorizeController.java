package com.xisan517.community.controller;

import com.xisan517.community.dto.AccessTokenDTO;
import com.xisan517.community.dto.GithubUser;
import com.xisan517.community.mapper.UserMapper;
import com.xisan517.community.model.User;
import com.xisan517.community.provider.GithubProvider;
import com.xisan517.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String cliectId;
    @Value("${github.client.secret}")
    private String clientSerect;
    @Value("${github.redirect.uri}")
    private String redircetUri;



    @Autowired
    private UserService userService;


    @GetMapping("/callback")
    public String callback(@RequestParam(name="code")String code,
                           @RequestParam(name="state")String state,
                           HttpServletRequest request,
                           HttpServletResponse response){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(cliectId);
        accessTokenDTO.setClient_secret(clientSerect);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redircetUri);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if(githubUser != null && githubUser.getId() != null){
            //登录成功，写cookie和session
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl(githubUser.getAvatar_url());
            userService.createOrUpdate(user);
           response.addCookie(new Cookie("token",token));
            return "redirect:/";

        }else{
            //登录失败，重新登录
            return "redirect:/";
        }


    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }


}
