package com.vladhuk.dept.api.service.impl;

import com.vladhuk.dept.api.model.User;
import com.vladhuk.dept.api.service.BlacklistService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BlacklistServiceImpl implements BlacklistService {
    @Override
    public List<User> getFullBlacklist() {
        return null;
    }

    @Override
    public List<User> getBlacklistPage(Integer pageNumber, Integer pageSize) {
        return null;
    }

    @Override
    public List<User> addUserToBlacklist(User user) {
        return null;
    }

    @Override
    public void deleteUserFromBlacklist(Long userId) {

    }
}
