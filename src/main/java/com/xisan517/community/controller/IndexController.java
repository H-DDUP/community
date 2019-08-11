package com.xisan517.community.controller;

import com.xisan517.community.dto.PageinationDTO;
import com.xisan517.community.dto.QuestionDTO;
import com.xisan517.community.mapper.QuestionMapper;
import com.xisan517.community.mapper.UserMapper;
import com.xisan517.community.model.Question;
import com.xisan517.community.model.User;
import com.xisan517.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {


    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name="page",defaultValue = "1")Integer page,
                        @RequestParam(name="size",defaultValue = "5")Integer size) {
        PageinationDTO pageination = questionService.list(page,size);

        model.addAttribute("pageination",pageination);
        return "index";
    }
}
