package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.dto.SaleReportDTO;
import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;

	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

	public Page<SaleReportDTO> searchReport(String minDate, String maxDate, String name, Pageable pageable) {

		LocalDate dataInicial = (minDate != null && !minDate.isEmpty()) ? LocalDate.parse(minDate)
				: LocalDate.now().minusYears(1L);
		LocalDate dataFinal = (maxDate != null && !maxDate.isEmpty()) ? LocalDate.parse(maxDate)
				: LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
		if (dataInicial.isAfter(dataFinal)) {
			throw new IllegalArgumentException("Data inicial não pode ser posterior à data final.");
		}
		Page<SaleReportDTO> result = repository.findByNameAndDataBetween(name, dataInicial, dataFinal, pageable)
				.map(x -> new SaleReportDTO(x));
		return result;

	}

	public Page<SaleSummaryDTO> searchSummary(String minDate, String maxDate, Pageable pageable) {

		LocalDate dataInicial = (minDate != null && !minDate.isEmpty()) ? LocalDate.parse(minDate)
				: LocalDate.now().minusYears(1L);
		LocalDate dataFinal = (maxDate != null && !maxDate.isEmpty()) ? LocalDate.parse(maxDate)
				: LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());

		if (dataInicial.isAfter(dataFinal)) {
			throw new IllegalArgumentException("Data inicial não pode ser posterior à data final.");
		}
		Page<Object[]> result = repository.searchSalesBySeller(dataInicial, dataFinal, pageable);
		List<SaleSummaryDTO> summaries = new ArrayList<>();
		for (Object[] row : result.getContent()) {
			String sellerName = (String) row[0];
			Double totalSalesAmount = (Double) row[1];
			summaries.add(new SaleSummaryDTO(sellerName, totalSalesAmount));
		}
		return new PageImpl<>(summaries, pageable, result.getTotalElements());

	}

}
