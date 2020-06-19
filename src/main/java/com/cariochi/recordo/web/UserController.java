package com.cariochi.recordo.web;

import com.cariochi.recordo.web.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping
    public UserDto create(@RequestBody UserDto userDto) {
        userDto.setId(10);
        return userDto;
    }

    @PutMapping
    public UserDto update(@RequestBody UserDto userDto) {
        userDto.setName("Updated");
        return userDto;
    }

    @PatchMapping("/{id}")
    public UserDto patch(@PathVariable int id, @RequestBody UserDto userDto) {
        userDto.setId(id);
        userDto.setName("Patched");
        return userDto;
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable int id,
                           @RequestParam(value = "name", required = false) Optional<String> name,
                           @RequestHeader(value = "locale", required = false) Optional<String> locale
    ) {
        return UserDto.builder()
                .id(id)
                .name(name.orElse("user_" + id) + " " + locale.orElse(""))
                .build();
    }

    @GetMapping
    public List<UserDto> findAll() {
        return asList(
                UserDto.builder().id(1).name("user_" + 1).build(),
                UserDto.builder().id(2).name("user_" + 2).build()
        );
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        log.info("User {} deleted", id);
    }
}
