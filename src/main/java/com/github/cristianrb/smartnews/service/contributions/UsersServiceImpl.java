package com.github.cristianrb.smartnews.service.contributions;

import com.github.cristianrb.smartnews.entity.UserDAO;
import com.github.cristianrb.smartnews.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDAO saveUser(UserDAO user) {
        return usersRepository.save(user);
    }

    @Override
    public Optional<UserDAO> getUser(String id) {
        return usersRepository.findById(id);
    }

    @Override
    public List<UserDAO> getAllUsers() {
        return usersRepository.findAll();
    }
}
