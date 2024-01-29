package com.devsuperior.dsmeta.repositories;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.devsuperior.dsmeta.entities.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {
	@Query(value = "SELECT s FROM Sale s JOIN FETCH s.seller WHERE (:name IS NULL OR UPPER(s.seller.name) "
			+ "LIKE UPPER(CONCAT('%', :name ,'%'))) AND s.date BETWEEN :dataInicial AND :dataFinal", countQuery = "SELECT COUNT(s) FROM Sale s JOIN s.seller WHERE (:name IS NULL OR UPPER(s.seller.name) LIKE UPPER(CONCAT('%', :name ,'%'))) AND s.date BETWEEN :dataInicial AND :dataFinal")
	Page<Sale> findByNameAndDataBetween(String name, LocalDate dataInicial, LocalDate dataFinal, Pageable pageable);

	@Query(value = "SELECT s.seller.name, SUM(s.amount) " + "FROM Sale s " + "JOIN s.seller "
			+ "WHERE s.date BETWEEN :dataInicial AND :dataFinal "
			+ "GROUP BY s.seller.name", countQuery = "SELECT COUNT(DISTINCT s.seller) FROM Sale s JOIN s.seller")
	Page<Object[]> searchSalesBySeller(LocalDate dataInicial, LocalDate dataFinal, Pageable pageable);

}
