package de.uniks.stp24;

import de.uniks.stp24.dto.*;
import de.uniks.stp24.rest.AuthApiService;
import de.uniks.stp24.rest.UserApiService;
import de.uniks.stp24.service.EditUserService;

import de.uniks.stp24.service.TokenStorage;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EditUserServiceTest {


    // Update user details successfully updates the user information
    @Test
    public void test_update_user_details_success() {
        // Arrange
        UserApiService userApiService = new UserApiService() {
            @Override
            public Observable<UserDto> createUser(CreateUserDto dto) {
                return null;
            }

            @Override
            public Observable<List<UserDto>> findAll() {
                return null;
            }

            @Override
            public Observable<List<UserDto>> findAll(String ids) {
                return null;
            }

            @Override
            public Observable<UserDto> findOne(String id) {
                return null;
            }

            @Override
            public Observable<UserDto> update(String id, UpdateUserDto dto) {
                return Observable.just(new UserDto(LocalDateTime.now(), LocalDateTime.now(), "newId", dto.name(), null));
            }

            @Override
            public Observable<UserDto> delete(String id) {
                return null;
            }
            // other methods can be left unimplemented for this test
        };
        TokenStorage tokenStorage = new TokenStorage();
        tokenStorage.setUserId("oldId");
        EditUserService editUserService = new EditUserService();
        editUserService.userApiService = userApiService;
        editUserService.tokenStorage = tokenStorage;

        // Act
        UserDto updatedUser = editUserService.update("newName", "newPassword").blockingFirst();

        // Assert
        assertEquals("newId", updatedUser._id());
        assertEquals("newName", updatedUser.name());
        assertEquals("newId", tokenStorage.getUserId());
    }

    // Update user with invalid user ID returns an error
    @Test
    public void test_update_user_invalid_id_error() {
        // Arrange
        UserApiService userApiService = new UserApiService() {
            @Override
            public Observable<UserDto> createUser(CreateUserDto dto) {
                return null;
            }

            @Override
            public Observable<List<UserDto>> findAll() {
                return null;
            }

            @Override
            public Observable<List<UserDto>> findAll(String ids) {
                return null;
            }

            @Override
            public Observable<UserDto> findOne(String id) {
                return null;
            }

            @Override
            public Observable<UserDto> update(String id, UpdateUserDto dto) {
                return Observable.error(new RuntimeException("User not found"));
            }

            @Override
            public Observable<UserDto> delete(String id) {
                return null;
            }
            // other methods can be left unimplemented for this test
        };
        TokenStorage tokenStorage = new TokenStorage();
        tokenStorage.setUserId("invalidId");
        EditUserService editUserService = new EditUserService();
        editUserService.userApiService = userApiService;
        editUserService.tokenStorage = tokenStorage;

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            editUserService.update("newName", "newPassword").blockingFirst();
        });
    }

    // Update user ensures token storage user ID is updated correctly
    @Test
    public void update_user_correctly_updates_token_storage_user_id() {
        UserApiService userApiService = new UserApiService() {
            @Override
            public Observable<UserDto> createUser(CreateUserDto dto) {
                return null;
            }

            @Override
            public Observable<List<UserDto>> findAll() {
                return null;
            }

            @Override
            public Observable<List<UserDto>> findAll(String ids) {
                return null;
            }

            @Override
            public Observable<UserDto> findOne(String id) {
                return null;
            }

            @Override
            public Observable<UserDto> update(String id, UpdateUserDto dto) {
                return Observable.just(new UserDto(LocalDateTime.now(), LocalDateTime.now(), "newId", dto.name(), null));
            }

            @Override
            public Observable<UserDto> delete(String id) {
                return null;
            }
        };
        TokenStorage tokenStorage = new TokenStorage();
        tokenStorage.setUserId("oldId");
        EditUserService editUserService = new EditUserService();
        editUserService.userApiService = userApiService;
        editUserService.tokenStorage = tokenStorage;

        UserDto updatedUser = editUserService.update("newName", "newPassword").blockingFirst();

        assertEquals("newId", updatedUser._id());
        assertEquals("newName", updatedUser.name());
        assertEquals("newId", tokenStorage.getUserId());
    }

    // Check password when token storage is empty returns an error
    @Test
    public void test_check_password_empty_token_returns_error() {
        // Arrange
        AuthApiService authApiService = new AuthApiService() {
            @Override
            public Observable<LoginResult> login(LoginDto dto) {
                return Observable.error(new RuntimeException("Token storage is empty"));
            }

            @Override
            public Observable<LoginResult> refresh(RefreshDto dto) {
                return null;
            }

            @Override
            public Observable<Response<Void>> logout() {
                return null;
            }
        };
        TokenStorage tokenStorage = new TokenStorage();
        EditUserService editUserService = new EditUserService();
        editUserService.authApiService = authApiService;
        editUserService.tokenStorage = tokenStorage;

        // Act & Assert
        assertThrows(RuntimeException.class, () -> editUserService.checkPassword("password").blockingFirst());
    }

    // Delete user when token storage is empty returns an error
    @Test
    public void test_delete_user_empty_token_returns_error() {
        // Arrange
        UserApiService userApiService = new UserApiService() {
            @Override
            public Observable<UserDto> createUser(CreateUserDto dto) {
                return null;
            }

            @Override
            public Observable<List<UserDto>> findAll() {
                return null;
            }

            @Override
            public Observable<List<UserDto>> findAll(String ids) {
                return null;
            }

            @Override
            public Observable<UserDto> findOne(String id) {
                return null;
            }

            @Override
            public Observable<UserDto> update(String id, UpdateUserDto dto) {
                return null;
            }

            @Override
            public Observable<UserDto> delete(String id) {
                return Observable.error(new RuntimeException("Token storage is empty"));
            }
            // other methods can be left unimplemented for this test
        };
        TokenStorage tokenStorage = new TokenStorage();
        EditUserService editUserService = new EditUserService();
        editUserService.userApiService = userApiService;
        editUserService.tokenStorage = tokenStorage;

        // Act & Assert
        assertThrows(RuntimeException.class, () -> editUserService.delete().blockingFirst());
    }

    // Update user with null username or password returns an error
    @Test
    public void update_user_with_null_username_or_password_returns_error() {
        // Arrange
        UserApiService userApiService = new UserApiService() {
            @Override
            public Observable<UserDto> createUser(CreateUserDto dto) {
                return null;
            }

            @Override
            public Observable<List<UserDto>> findAll() {
                return null;
            }

            @Override
            public Observable<List<UserDto>> findAll(String ids) {
                return null;
            }

            @Override
            public Observable<UserDto> findOne(String id) {
                return null;
            }

            @Override
            public Observable<UserDto> update(String id, UpdateUserDto dto) {
                return Observable.error(new RuntimeException("Username or password cannot be null"));
            }

            @Override
            public Observable<UserDto> delete(String id) {
                return null;
            }
            // other methods can be left unimplemented for this test
        };
        TokenStorage tokenStorage = new TokenStorage();
        tokenStorage.setUserId("oldId");
        EditUserService editUserService = new EditUserService();
        editUserService.userApiService = userApiService;
        editUserService.tokenStorage = tokenStorage;

        // Act & Assert
        assertThrows(RuntimeException.class, () -> editUserService.update(null, null).blockingFirst());
    }

    // Delete user with invalid user ID returns an error
    @Test
    public void delete_user_with_invalid_id_returns_error() {
        // Arrange
        UserApiService userApiService = new UserApiService() {
            @Override
            public Observable<UserDto> createUser(CreateUserDto dto) {
                return null;
            }

            @Override
            public Observable<List<UserDto>> findAll() {
                return null;
            }

            @Override
            public Observable<List<UserDto>> findAll(String ids) {
                return null;
            }

            @Override
            public Observable<UserDto> findOne(String id) {
                return null;
            }

            @Override
            public Observable<UserDto> update(String id, UpdateUserDto dto) {
                return null;
            }

            @Override
            public Observable<UserDto> delete(String id) {
                return Observable.error(new RuntimeException("Invalid user ID"));
            }
        };
        TokenStorage tokenStorage = new TokenStorage();
        tokenStorage.setUserId("invalidId");
        EditUserService editUserService = new EditUserService();
        editUserService.userApiService = userApiService;
        editUserService.tokenStorage = tokenStorage;

        // Act
        Throwable error = assertThrows(Exception.class, () -> editUserService.delete().blockingFirst());

        // Assert
        assertEquals("Invalid user ID", error.getMessage());
    }

    // Update user with invalid user ID returns an error
    @Test
    public void test_update_user_with_invalid_id_returns_error() {
        // Arrange
        UserApiService userApiService = new UserApiService() {
            @Override
            public Observable<UserDto> createUser(CreateUserDto dto) {
                return null;
            }

            @Override
            public Observable<List<UserDto>> findAll() {
                return null;
            }

            @Override
            public Observable<List<UserDto>> findAll(String ids) {
                return null;
            }

            @Override
            public Observable<UserDto> findOne(String id) {
                return null;
            }

            @Override
            public Observable<UserDto> update(String id, UpdateUserDto dto) {
                return Observable.error(new RuntimeException("Invalid user ID"));
            }

            @Override
            public Observable<UserDto> delete(String id) {
                return null;
            }
            // other methods can be left unimplemented for this test
        };
        TokenStorage tokenStorage = new TokenStorage();
        tokenStorage.setUserId("oldId");
        EditUserService editUserService = new EditUserService();
        editUserService.userApiService = userApiService;
        editUserService.tokenStorage = tokenStorage;

        // Act & Assert
        assertThrows(Exception.class, () -> editUserService.update("newName", "newPassword").blockingFirst());
    }

    // Delete user successfully removes the user
    @Test
    public void delete_user_successfully_removes_user() {
        // Arrange
        UserApiService userApiService = new UserApiService() {
            @Override
            public Observable<UserDto> createUser(CreateUserDto dto) {
                return null;
            }

            @Override
            public Observable<List<UserDto>> findAll() {
                return null;
            }

            @Override
            public Observable<List<UserDto>> findAll(String ids) {
                return null;
            }

            @Override
            public Observable<UserDto> findOne(String id) {
                return null;
            }

            @Override
            public Observable<UserDto> update(String id, UpdateUserDto dto) {
                return null;
            }

            @Override
            public Observable<UserDto> delete(String id) {
                return Observable.just(new UserDto(LocalDateTime.now(), LocalDateTime.now(), id, "Deleted User", null));
            }
            // other methods can be left unimplemented for this test
        };
        TokenStorage tokenStorage = new TokenStorage();
        tokenStorage.setUserId("userToDeleteId");
        EditUserService editUserService = new EditUserService();
        editUserService.userApiService = userApiService;
        editUserService.tokenStorage = tokenStorage;

        // Act
        UserDto deletedUser = editUserService.delete().blockingFirst();

        // Assert
        assertEquals("userToDeleteId", deletedUser._id());
        assertEquals("Deleted User", deletedUser.name());
        assertEquals(tokenStorage.getUserId(), "userToDeleteId");
    }

    // Update user details successfully updates the user information
    @Test
    public void test_update_user_details_success_Two() {
        // Arrange
        UserApiService userApiService = new UserApiService() {
            @Override
            public Observable<UserDto> createUser(CreateUserDto dto) {
                return null;
            }

            @Override
            public Observable<List<UserDto>> findAll() {
                return null;
            }

            @Override
            public Observable<List<UserDto>> findAll(String ids) {
                return null;
            }

            @Override
            public Observable<UserDto> findOne(String id) {
                return null;
            }

            @Override
            public Observable<UserDto> update(String id, UpdateUserDto dto) {
                return Observable.just(new UserDto(LocalDateTime.now(), LocalDateTime.now(), "newId", dto.name(), null));
            }

            @Override
            public Observable<UserDto> delete(String id) {
                return null;
            }
            // other methods can be left unimplemented for this test
        };
        TokenStorage tokenStorage = new TokenStorage();
        tokenStorage.setUserId("oldId");
        EditUserService editUserService = new EditUserService();
        editUserService.userApiService = userApiService;
        editUserService.tokenStorage = tokenStorage;

        // Act
        UserDto updatedUser = editUserService.update("newName", "newPassword").blockingFirst();

        // Assert
        assertEquals("newId", updatedUser._id());
        assertEquals("newName", updatedUser.name());
        assertEquals("newId", tokenStorage.getUserId());
    }
}