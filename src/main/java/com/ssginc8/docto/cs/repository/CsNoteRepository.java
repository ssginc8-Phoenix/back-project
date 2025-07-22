package com.ssginc8.docto.cs.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ssginc8.docto.cs.entity.CsNote;

public interface CsNoteRepository extends JpaRepository<CsNote, Long> {
	Page<CsNote> findByCsRoom_CsRoomIdOrderByCreatedAtDesc(Long csRoomId, Pageable pageable);
}
