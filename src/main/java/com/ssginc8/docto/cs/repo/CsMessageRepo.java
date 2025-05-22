package com.ssginc8.docto.cs.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssginc8.docto.cs.entity.CsMessage;

public interface CsMessageRepo extends JpaRepository<CsMessage, Long> {
}
