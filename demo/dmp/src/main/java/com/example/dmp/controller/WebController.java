package com.example.dmp.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.example.dmp.model.User;
import com.example.dmp.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @version "\$Id$" kenan
 */
@Controller
@RequestMapping
public class WebController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/home")
    @Transactional(readOnly = true)
    public ModelAndView home(@CookieValue(value = "dmpId", required = false) String dmpId, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView("home");
        if (dmpId == null) {
            dmpId = UUID.randomUUID().toString();
        }
        response.addCookie(new Cookie("dmpId", dmpId));
        modelAndView.addObject("id", dmpId);
        final List<User> all = userRepository.findAll();
        modelAndView.addObject("users", all);
        return modelAndView;
    }

}
