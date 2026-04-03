package com.pocketco.domain.user.repository;

import com.pocketco.domain.user.entity.Judge0Token;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface Judge0TokenRepository extends JpaRepository<Judge0Token, Long> {
    List<Judge0Token> findByHistory_Id(Long historyId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        update Judge0Token t
            set t.statusId = :newStatusId
        where t.token = :token
            and t.statusId in (1, 2)
    """)
    int updateStatusWithToken(@Param("token") String token, @Param("newStatusId") int newStatusId);
}