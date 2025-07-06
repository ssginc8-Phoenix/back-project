package com.ssginc8.docto.cs.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssginc8.docto.cs.entity.CsRoom;

public interface CsRoomRepo extends JpaRepository<CsRoom, Long> {
}
