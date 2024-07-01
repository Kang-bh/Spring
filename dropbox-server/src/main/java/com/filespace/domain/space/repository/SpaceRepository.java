package com.filespace.domain.space.repository;

import com.filespace.domain.space.enumuration.SpaceType;
import com.filespace.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.filespace.domain.space.domain.Space;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface SpaceRepository extends JpaRepository<Space, Long> {

    // write JPQL query
    @Query("SELECT space FROM Space space WHERE space.spaceType = :spaceType AND space.ownerId = :user")
    Space findOwnSpace(@Param("user") User user, @Param("spaceType") SpaceType spaceType);
}
