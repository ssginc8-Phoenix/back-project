package com.ssginc8.docto.cs.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ssginc8.docto.cs.entity.CsNote;

public interface CsNoteRepo extends JpaRepository<CsNote, Long> {
	Page<CsNote> findByCsRoom_CsRoomIdOrderByCreatedAtDesc(Long csRoomId, Pageable pageable);
}
