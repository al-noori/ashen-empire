package de.uniks.stp24.service;

import de.uniks.stp24.dto.CreateUserDto;
import de.uniks.stp24.dto.UserDto;
import de.uniks.stp24.rest.UserApiService;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.AsyncSubject;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserService {

    @Inject
    UserApiService userApiService;

    @Inject
    UserService() {

    }

    public Observable<UserDto> signup(String username, String password) {
        return userApiService.createUser(new CreateUserDto(username, password));
    }

    public Observable<Map<String, String>> decodeUsers(Set<String> toFindSet) {
        AsyncSubject<Map<String, String>> subject = AsyncSubject.create();
        StringBuilder toFindString = new StringBuilder();
        List<String> toFind = toFindSet.stream().toList();
        for (int i = 0, toFindSize = toFind.size(); i < toFindSize - 1; i++) {
            toFindString.append(toFind.get(i)).append(",");
        }
        if (!toFind.isEmpty()) {
            toFindString.append(toFind.getLast());
        }
        Disposable disposable = userApiService.findAll(toFindString.toString()).subscribe(
                foundUsers -> {
                    Map<String, String> userMap = foundUsers.stream().collect(
                            HashMap::new,
                            (map, user) -> map.put(user._id(), user.name()),
                            Map::putAll
                    );
                    subject.onNext(userMap);
                    subject.onComplete();
                },
                error -> {
                    subject.onError(error);
                    subject.onComplete();
                }
        );
        return subject.doOnDispose(disposable::dispose);
    }

    public Observable<String> getUserNameById(String owner) {
        return userApiService.findOne(owner).map(UserDto::name);
    }
}
