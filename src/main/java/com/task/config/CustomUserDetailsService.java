package com.task.config;

import com.task.entity.User;
import com.task.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try{
            User user = userRepo.findByUsername(username);
            System.out.println(user);
            if(user==null)
                throw new UsernameNotFoundException("No User");
            else
                return new CustomUserDetails(user);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
