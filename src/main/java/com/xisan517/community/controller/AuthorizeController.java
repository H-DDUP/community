package com.xisan517.community.controller;

import com.xisan517.community.dto.AccessTokenDTO;
import com.xisan517.community.dto.GithubUser;
import com.xisan517.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code")String code,
                           @RequestParam(name="state")String state){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(cliectId);
        accessTokenDTO.setClient_secret(clientSerect);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redircetUri);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser user = githubProvider.getUser(accessToken);
        System.out.println(user);
        return "index";
    }



}
