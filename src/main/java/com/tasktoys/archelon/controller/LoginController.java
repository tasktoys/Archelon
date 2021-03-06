/*
 * Copyright(C) 2014 tAsktoys. All rights reserved.
 */
package com.tasktoys.archelon.controller;

import com.tasktoys.archelon.data.entity.User;
import com.tasktoys.archelon.service.UserService;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller of /login in header.jspf
 *
 * @author mikan
 * @since 0.1
 */
@Controller
@RequestMapping(value = "/login")
@SessionAttributes(types = {UserSession.class})
public class LoginController {

    @Autowired
    private UserService userService;

    private static Logger log = Logger.getLogger(LoginController.class.getName());

    private static final String VIEW_SUCCESS = "success";
    private static final String VIEW_REGISTER = "register";
    private static final String SUBMIT_NORMAL = "normalLogin";
    private static final String SUBMIT_TWITTER = "twitterLogin";
    private static final String SUBMIT_REGISTER = "register";
    private static final String SUBMIT_LOGOUT = "logout";
    private static final String PARAM_USER_NAME = "username";
    private static final String PARAM_USER_PASSWORD = "userpassword";
    private static final String ATTR_ID = "userId";
    private static final String ATTR_PASSWORD = "userPassword";

    @RequestMapping(method = RequestMethod.POST, params = SUBMIT_NORMAL)
    public String handleLoginRequest(@RequestParam Map<String, String> params,
            Model model, UserSession userSession, SessionStatus sessionStatus,
            RedirectAttributes attributes) {
        String userName = params.get(PARAM_USER_NAME);
        String userPassword = params.get(PARAM_USER_PASSWORD);
        User user = userService.findUserByName(userName);
        if (user == null) {
            log.log(Level.WARNING, "login failed: {0}", userName);
            sessionStatus.setComplete();
            attributes.addFlashAttribute("message", "error.auth.failed");
            return ErrorController.REDIRECT;
        }
        if (!user.getName().equals(userName)) {
            log.log(Level.WARNING, "login failed: {0}", userName);
            sessionStatus.setComplete();
            attributes.addFlashAttribute("message", "error.auth.failed");
            return ErrorController.REDIRECT;
        }
        if (!user.isValidPasswordWithPlaneString(userPassword)) {
            log.log(Level.WARNING, "login failed: {0}", userName);
            sessionStatus.setComplete();
            attributes.addFlashAttribute("message", "error.auth.failed");
            return ErrorController.REDIRECT;
        }
        model.addAttribute(ATTR_ID, userName);
        userSession.setUser(user);
        return VIEW_SUCCESS;
    }

    @RequestMapping(method = RequestMethod.POST, params = SUBMIT_TWITTER)
    public String handleTwitterLoginRequest(@RequestParam Map<String, String> params, 
            Model model, UserSession userSession, SessionStatus sessionStatus,
            RedirectAttributes redirect) {
        redirect.addFlashAttribute(ErrorController.ATTR_MESSAGE, "error.sorry");
        return ErrorController.REDIRECT;
    }

    @RequestMapping(method = RequestMethod.POST, params = SUBMIT_REGISTER)
    public String handleRegisterRequest(@RequestParam Map<String, String> params, 
            Model model, SessionStatus sessionStatus) {
        model.addAttribute(ATTR_ID, params.get(PARAM_USER_NAME));
        model.addAttribute(ATTR_PASSWORD, params.get(PARAM_USER_PASSWORD));
        sessionStatus.setComplete();
        return VIEW_REGISTER;
    }

    @RequestMapping(method = RequestMethod.POST, params = SUBMIT_LOGOUT)
    public String handleLogoutRequest(UserSession userSession, 
            SessionStatus sessionStatus) {
        userSession.clear();
        sessionStatus.setComplete();
        return IndexController.VIEW;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String handleEmptyRequest() {
        return IndexController.REDIRECT;
    }

    @ModelAttribute(value = "userSession") // (1)
    public UserSession setUpUserSession() {
        log.log(Level.INFO, "user logged in");
        return new UserSession();
    }
}
