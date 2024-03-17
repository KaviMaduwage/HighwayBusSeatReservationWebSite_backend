package com.project.seatReservation.service;

import com.project.seatReservation.dao.UserDao;
import com.project.seatReservation.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;

    }


    @Override
    public List<User> findUsersByEmail(String email) {
        List<User> userList = userDao.findUsersByEmail(email);
        return userList;
    }

    @Override
    public void saveUser(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.save(user);
    }

    @Override
    public String confirmEmail(String confirmationToken) {
        User user = userDao.findByConfirmationToken(confirmationToken);

        if(user != null)
        {
            //User user = userRepository.findByUserEmailIgnoreCase(token.getUserEntity().getUserEmail());
            user.setEmailVerified(true);
            user.setActive(true);
            userDao.save(user);
            return "Email verified successfully!";
        }
        return "Error: Couldn't verify email";
    }

    @Override
    public boolean isLoginSuccess(User user) {
        List<User> savedUser = userDao.findUsersByEmail(user.getEmail());
        if(!savedUser.isEmpty()){
            Boolean isPwdRight = passwordEncoder.matches(user.getPassword(), savedUser.get(0).getPassword());
            if(isPwdRight){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

}
