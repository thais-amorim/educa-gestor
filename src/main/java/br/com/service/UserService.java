package br.com.service;

import br.com.dto.UserDto;
import br.com.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;


@ApplicationScoped
public class UserService {

    public List<User> findAll() {
        return User.findAll().list();
    }

    @Transactional
    public User create(UserDto dto) {
        User user = new User(dto.name, dto.documentNumber, dto.email);
        user.persist();

        return user;
    }
}
