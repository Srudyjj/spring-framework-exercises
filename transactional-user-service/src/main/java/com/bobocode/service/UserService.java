package com.bobocode.service;

import com.bobocode.dao.UserDao;
import com.bobocode.model.jpa.Role;
import com.bobocode.model.jpa.RoleType;
import com.bobocode.model.jpa.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class proovides {@link User} related service logic.
 * <p>
 * todo: 1. Configure {@link UserService} bean as spring service
 * todo: 2. Inject {@link UserDao} using constructor-based injection
 * todo: 3. Enable transaction management on class level
 * todo: 4. Configure {@link UserService#getAll()} as read-only method
 * todo: 4. Configure {@link UserService#getAllAdmins()} as read-only method
 */

@Service
@Transactional
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void save(User user) {
        userDao.save(user);
    }

    @Transactional(readOnly = true)
    public List<User> getAll() {
        return userDao.findAll();
    }

    @Transactional(readOnly = true)
    public List<User> getAllAdmins() {
        return userDao.findAll().stream()
                .filter(user -> user.getRoles()
                        .stream()
                        .anyMatch(role -> role.getRoleType() == RoleType.ADMIN))
                .collect(Collectors.toList());
    }

    public void addRole(Long userId, RoleType roleType) {
        User user = userDao.findById(userId);
        Role role = Role.valueOf(roleType);
        user.addRole(role);
    }
}
