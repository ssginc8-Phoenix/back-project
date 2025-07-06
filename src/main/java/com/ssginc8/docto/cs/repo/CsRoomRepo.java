package com.ssginc8.docto.cs.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssginc8.docto.cs.entity.CsRoom;

public interface CsRoomRepo extends JpaRepository<CsRoom, Long> {
	List<CsRoom> findAllByCustomerId(Long customerId);

}
