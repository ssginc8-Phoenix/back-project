package com.ssginc8.docto.cs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssginc8.docto.cs.entity.CsRoom;

public interface CsRoomRepository extends JpaRepository<CsRoom, Long> {
	List<CsRoom> findAllByCustomerId(Long customerId);

}
