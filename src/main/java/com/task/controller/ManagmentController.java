package com.task.controller;

import com.task.entity.User;
import com.task.repo.UserRepo;
import com.task.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.netty.http.server.HttpServerRequest;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ManagmentController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping({"", "/"})
    public String home(HttpServerRequest httpServerRequest, Model model) {
        if (getLoggedInUser() != null)
            model.addAttribute("user", getLoggedInUser());
        else
            model.addAttribute("user", null);
        return "index";

    }

    @RequestMapping("/login")
    public String login(HttpServerRequest httpServerRequest) {
        return "login";
    }

    @RequestMapping("/forgot")
    public String forgot(HttpServerRequest httpServerRequest) {
        return "forgot";
    }

    @RequestMapping("/createUser")
    public String createUser(HttpServerRequest httpServerRequest) {
        return "register";
    }

    @RequestMapping("/profile")
    public String userProfile(HttpServerRequest httpServerRequest, Model model) {
        if (getLoggedInUser() != null)
            model.addAttribute("user", getLoggedInUser());
        else
            model.addAttribute("user", null);
        return "profile";
    }

    @RequestMapping(value = {"/profile/update"}, method =
            RequestMethod.POST,
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<?> updateUser(HttpServletRequest request, @ModelAttribute User existingUser) {
        User checkEmail = userService.findByEmail(existingUser.getEmail());
        if(checkEmail != null && checkEmail.getId() != existingUser.getId())
            return new ResponseEntity<>("Email already exists", HttpStatus.NOT_ACCEPTABLE);
        try{
            User newUser = userService.findById(existingUser.getId());
            newUser.setUsername(existingUser.getUsername());
            newUser.setFirstName(existingUser.getFirstName());
            newUser.setLastName(existingUser.getLastName());
            newUser.setEmail(existingUser.getEmail());
            newUser.setNumber(existingUser.getNumber());
            newUser.setAge(existingUser.getAge());
            newUser.setGender(existingUser.getGender());
            newUser.setAddress(existingUser.getAddress());

            // Server side validation
            if(!ObjectUtils.isEmpty(getNullFields(newUser)))
                return new ResponseEntity<>(getNullFields(newUser)+" fields are required", HttpStatus.NOT_ACCEPTABLE);
            userService.save(newUser);
            authanticateUser(newUser, request);
            return new ResponseEntity<>("success", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = {"/register/checkEmail"}, method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<?> checkEmail(HttpServletRequest request, @ModelAttribute("email") String email) {
        if (ObjectUtils.isEmpty(email))
            return new ResponseEntity<>("Email is empty", HttpStatus.NOT_ACCEPTABLE);
        else {
            if (ObjectUtils.isEmpty(userService.findByEmail(email)))
                return new ResponseEntity<>("", HttpStatus.OK);
            else
                return new ResponseEntity<>("Email allready exists", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @RequestMapping(value = {"/register/verifyEmail"}, method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<?> verifyEmail(HttpServletRequest request, @ModelAttribute("email") String email) {
        if (ObjectUtils.isEmpty(email))
            return new ResponseEntity<>("Email is empty", HttpStatus.NOT_ACCEPTABLE);
        else {
            User user = userService.findByEmail(email);
            if (!ObjectUtils.isEmpty(user))
                return new ResponseEntity<>(user.getId(), HttpStatus.OK);
            else
                return new ResponseEntity<>("Email not exists", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @RequestMapping(value = {"/forgot/changePassword"}, method = RequestMethod.PUT,
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<?> changePassword(HttpServletRequest request,@ModelAttribute("id") Integer id, @ModelAttribute("password") String password, @ModelAttribute("passwordTwo") String password2) {
        if (ObjectUtils.isEmpty(password) || ObjectUtils.isEmpty(password2))
            return new ResponseEntity<>("Password is empty", HttpStatus.NOT_ACCEPTABLE);
        else {
            if(password.equals(password2)) {
                userService.changePassword(id, password);
                return new ResponseEntity<>("success", HttpStatus.OK);
            }
            else
                return new ResponseEntity<>("Both password are not matched", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @RequestMapping(value = {"/register"}, method =
            RequestMethod.POST,
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<?> register(HttpServletRequest request, @ModelAttribute User register) {

        User user = userService.findByUsername(register.getUsername());
        if (user == null) {
            register.setPassword(encodePassword(register.getPassword()));
            register.setEnabled(true);
            register.setRole("ROLE_USER");

            // Server side validation
            if(!ObjectUtils.isEmpty(getNullFields(register)))
                return new ResponseEntity<>(getNullFields(register)+" fields are required", HttpStatus.NOT_ACCEPTABLE);
            userService.save(register);
            authanticateUser(register, request);
            return new ResponseEntity<>("sucess", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("username allready have", HttpStatus.BAD_REQUEST);
        }
    }


    private static String encodePassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    public String getNullFields(User user) {
        StringBuilder builder = new StringBuilder();

        if (ObjectUtils.isEmpty(user.getUsername()))
            builder.append("username, ");
        if (ObjectUtils.isEmpty(user.getFirstName()))
            builder.append("firstName, ");
        if (ObjectUtils.isEmpty(user.getLastName()))
            builder.append("lastName, ");
        if (ObjectUtils.isEmpty(user.getEmail()))
            builder.append("email, ");
        if (ObjectUtils.isEmpty(user.getNumber()))
            builder.append("number, ");
        if (ObjectUtils.isEmpty(user.getGender()))
            builder.append("gender, ");
        if (ObjectUtils.isEmpty(user.getAge()))
            builder.append("age, ");
        if (ObjectUtils.isEmpty(user.getAddress()))
            builder.append("address, ");

        String result = builder.toString();
        if(!ObjectUtils.isEmpty(result)) {
            if (result.endsWith(", ")) {
                result = result.substring(0, result.length() - 2);
            }
            return result;
        }else
            return null;
    }

}
