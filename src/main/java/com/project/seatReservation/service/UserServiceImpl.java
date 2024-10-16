package com.project.seatReservation.service;

import com.project.seatReservation.dao.UserDao;
import com.project.seatReservation.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    public User saveUser(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.save(user);
        return user;
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
        if(!savedUser.isEmpty() && savedUser.get(0).isActive()){
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

    @Override
    @Transactional
    public void updateUser(User user) {
        userDao.save(user);
    }

    @Override
    public User findUserByUserId(Integer userId) {
        return userDao.findUserByUserId(userId);
    }

    @Transactional
    @Override
    public String changePassword(User user, String currentPassword, String newPassword) {
        String message = "";
        List<User> savedUser = userDao.findUsersByEmail(user.getEmail());
        Boolean isCurrentPwdRight = passwordEncoder.matches(currentPassword, savedUser.get(0).getPassword());

        if(isCurrentPwdRight){
            user.setPassword(passwordEncoder.encode(newPassword));
            userDao.save(user);
            message = "Password reset is success";

        }else{
            message = "Current password is not match with the existing password";
        }

        return message;
    }


}
