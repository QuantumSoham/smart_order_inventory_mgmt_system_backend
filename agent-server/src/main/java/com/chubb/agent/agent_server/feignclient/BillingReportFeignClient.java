package com.chubb.agent.agent_server.feignclient;

import java.time.LocalDate;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.chubb.agent.agent_server.dto.response.CategorySalesDTO;
import com.chubb.agent.agent_server.dto.response.RevenueReportDTO;
import com.chubb.agent.agent_server.dto.response.TopProductDTO;
import com.chubb.agent.agent_server.dto.response.WarehouseSalesDTO;

@FeignClient(
	   
	    name= "billing-service" 
	)
	public interface BillingReportFeignClient {

    @GetMapping("/billing/reports/revenue")
    List<RevenueReportDTO> revenueByDate(
            @RequestParam("from") LocalDate from,
            @RequestParam("to") LocalDate to
    );

    @GetMapping("/billing/reports/sales/category")
    List<CategorySalesDTO> salesByCategory();

    @GetMapping("/billing/reports/sales/warehouse")
    List<WarehouseSalesDTO> salesByWarehouse();

    @GetMapping("/billing/reports/top-products")
    List<TopProductDTO> topProducts();

	}
