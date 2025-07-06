package com.ssginc8.docto.cs.repo;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ssginc8.docto.cs.dto.CsRoomResponse;
import com.ssginc8.docto.cs.entity.CsRoom;

public interface CsRoomRepo extends JpaRepository<CsRoom, Long> {



	List<CsRoom> findAllByCustomerId(Long customerId);
}
