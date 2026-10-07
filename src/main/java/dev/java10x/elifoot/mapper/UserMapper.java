package dev.java10x.elifoot.mapper;

import dev.java10x.elifoot.controller.request.CreateUserRequest;
import dev.java10x.elifoot.controller.response.UserResponse;
import dev.java10x.elifoot.entity.Scope;
import dev.java10x.elifoot.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "scopes", source = "scopes", qualifiedByName = "mapScopeIdsToScopeEntities")
    User toEntity(CreateUserRequest request);

    @Mapping(target = "scopes", source = "scopes", qualifiedByName = "mapScopeEntitiesToStringScopes")
    UserResponse toResponse(User user);

    @Named("mapScopeEntitiesToStringScopes")
    default List<String> mapScopeEntitiesToStringScopes(List<Scope> scopes) {
        if(scopes == null) return List.of();

        return scopes.stream()
                .map(Scope::getName)
                .toList();
    }

    @Named("mapScopeIdsToScopeEntities")
    default List<Scope> mapScopeIdsToScopeEntities(List<Long> scopesIds) {
        if(scopesIds == null) return List.of();

        return scopesIds.stream()
                .map(id -> Scope.builder().id(id).build())
                .toList();
    }
}
