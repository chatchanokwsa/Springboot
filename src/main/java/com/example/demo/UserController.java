package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.jws.soap.SOAPBinding;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/users-post")
    public UserResponse createNewUser(@RequestBody NewUserRequest request) {
        // Validate input
        //Create new user into database =>move to other service
        User user = new User();
        user.setName(request.getName());
        user.setAge(request.getAge());
        user  = userRepository.save(user);
        return new UserResponse(user.getId(), user.getName() + user.getAge());
    }

    @PostMapping("/users1")
    public String createNewUserWithFormData(NewUserRequest request) {
        return request.getName() + request.getAge();
    }

    @GetMapping("/users")
    public PagingResponse getAllUser(@RequestParam(defaultValue = "1") int page,
                                     @RequestParam(name = "item_per_page", defaultValue = "10") int itemPerPage) {

        PagingResponse pagingResponse = new PagingResponse(page, itemPerPage);

        List<UserResponse> userResponseList = new ArrayList<>();
        Page<User> users = userRepository.findAll(PageRequest.of(page-1, itemPerPage));

//        Iterable<User> users = userRepository.findAll();
        for (User user: users.getContent()) {
            userResponseList.add(new UserResponse(user.getId(),user.getName(), user.getAge()));
        }


        pagingResponse.setUsersResponse(userResponseList);
        return pagingResponse;
    }

    @GetMapping("/users/{id}")
    public UserResponse getUserById( @PathVariable int id) {
        Optional<User> user = userRepository.findById(id);
        return new UserResponse(user.get().getId(), user.get().getName());
    }

}
