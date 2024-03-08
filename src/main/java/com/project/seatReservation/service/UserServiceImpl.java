package com.project.seatReservation.service;

import com.project.seatReservation.dao.UserDao;
import com.project.seatReservation.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    UserDao userDao;


    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;

    }


    @Override
    public List<User> findUsersByEmail(String email) {
        List<User> userList = userDao.findUsersByEmail(email);
        return userList;
    }

}
