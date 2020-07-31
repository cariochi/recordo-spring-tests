package com.cariochi.recordo.mockmvc;

import com.cariochi.recordo.EnableRecordo;
import com.cariochi.recordo.RecordoExtension;
import com.cariochi.recordo.given.Given;
import com.cariochi.recordo.mockmvc.dto.UserDto;
import com.cariochi.recordo.verify.Expected;
import com.cariochi.recordo.verify.Verify;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.test.web.servlet.MockMvc;

@Slf4j
@WebMvcTest(UserController.class)
@ExtendWith(RecordoExtension.class)
@RequiredArgsConstructor
class MockMvcTest {

    @Autowired
    @EnableRecordo
    private MockMvc mockMvc;

    @Test
    void should_get_user_by_id(
            @GET("/users/{id}?name={name}") @Headers("locale: UA") Request<UserDto> request,
            @Verify("/mockmvc/get_user_response.json") Expected<Response<UserDto>> expected
    ) {
        final Response<UserDto> response = request.execute(1, "Test User");
        expected.assertEquals(response);
    }

    @Test
    void should_get_user_by_id(
            @GET("/users/1?name=Test User") @Headers("locale: UA") Response<UserDto> response,
            @Verify("/mockmvc/get_user_response.json") Expected<Response<UserDto>> expected
    ) {
        expected.assertEquals(response);
    }

    @Test
    void should_get_user_by_id(
            @GET("/users/1?name=Test User") @Headers("locale: UA") UserDto user,
            @Verify("/mockmvc/user.json") Expected<UserDto> expected
    ) {
        expected.assertEquals(user);
    }

    @Test
    void should_get_all_users(
            @GET("/users") Request<Page<UserDto>> request,
            @Verify("/mockmvc/get_all_users_response.json") Expected<Response<Page<UserDto>>> expected
    ) {
        final Response<Page<UserDto>> response = request.execute();
        expected.assertEquals(response);
    }

    @Test
    void should_get_all_users(
            @GET("/users") Response<Page<UserDto>> response,
            @Verify("/mockmvc/get_all_users_response.json") Expected<Response<Page<UserDto>>> expected
    ) {
        expected.assertEquals(response);
    }

    @Test
    void should_get_all_users(
            @GET("/users") Page<UserDto> users,
            @Verify("/mockmvc/users.json") Expected<Page<UserDto>> expected
    ) {
        expected.assertEquals(users);
    }

    @Test
    void should_create_user(
            @Given("/mockmvc/new_user.json") UserDto user,
            @POST("/users") Request<UserDto> request,
            @Verify("/mockmvc/create_user_response.json") Expected<Response<UserDto>> expected
    ) {
        final Response<UserDto> response = request.withBody(user).execute();
        expected.assertEquals(response);
    }

    @Test
    void should_create_user(
            @POST("/users") @Body("/mockmvc/new_user.json") Request<UserDto> request,
            @Verify("/mockmvc/create_user_response.json") Expected<Response<UserDto>> expected
    ) {
        final Response<UserDto> response = request.execute();
        expected.assertEquals(response);
    }

    @Test
    void should_create_user(
            @POST("/users") @Body("/mockmvc/new_user.json") Response<UserDto> response,
            @Verify("/mockmvc/create_user_response.json") Expected<Response<UserDto>> expected
    ) {
        expected.assertEquals(response);
    }

    @Test
    void should_create_user(
            @POST("/users") @Body("/mockmvc/new_user.json") UserDto user,
            @Verify("/mockmvc/user.json") Expected<UserDto> expected
    ) {
        expected.assertEquals(user);
    }

    @Test
    void should_delete_user_by_id(
            @DELETE("/users/{id}") Request<Void> request,
            @Verify("/mockmvc/delete_user_response.json") Expected<Response<Void>> expected
    ) {
        final Response<Void> response = request.execute(1);
        expected.assertEquals(response);
    }

    @Test
    void should_delete_user_by_id(
            @DELETE("/users/1") Response<Void> response,
            @Verify("/mockmvc/delete_user_response.json") Expected<Response<Void>> expected
    ) {
        expected.assertEquals(response);
    }

    @Test
    void should_update_user(
            @PUT("/users") @Body("/mockmvc/user.json") UserDto user,
            @Verify("/mockmvc/updated_user.json") Expected<UserDto> expected
    ) {
        expected.assertEquals(user);
    }

    @Test
    void should_patch_user(
            @PATCH("/users/1") @Body("/mockmvc/user.json") UserDto user,
            @Verify("/mockmvc/updated_user.json") Expected<UserDto> expected
    ) {
        expected.assertEquals(user);
    }
}
