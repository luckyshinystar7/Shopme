package com.shopme.admin.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.dto.ReportItemDTO;
import com.shopme.admin.service.MasterOrderReportService;
import com.shopme.admin.util.ReportType;


@RestController
public class ReportRestController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportRestController.class);
	
	@Autowired 
	private MasterOrderReportService masterOrderReportService;

	@GetMapping("/reports/sales_by_date/{period}")
	public List<ReportItemDTO> getReportDataByDatePeriod(@PathVariable("period") String period) {
		
		LOGGER.info("ReportRestController | getReportDataByDatePeriod is called");
		
		LOGGER.info("ReportRestController | getReportDataByDatePeriod | Report period: " + period);
		
		switch (period) {
			case "last_7_days":
				
				LOGGER.info("ReportRestController | getReportDataByDatePeriod | case last_7_days");
				return masterOrderReportService.getReportDataLast7Days(ReportType.DAY);
	
			case "last_28_days":
				
				LOGGER.info("ReportRestController | getReportDataByDatePeriod | case last_28_days");
				return masterOrderReportService.getReportDataLast28Days(ReportType.DAY);
				
			case "last_6_months":
				LOGGER.info("ReportRestController | getReportDataByDatePeriod | case last_6_months");
				return masterOrderReportService.getReportDataLast6Months(ReportType.MONTH);

			case "last_year":
				LOGGER.info("ReportRestController | getReportDataByDatePeriod | case last_year");
				return masterOrderReportService.getReportDataLastYear(ReportType.MONTH);	
				
			default:
				
				LOGGER.info("ReportRestController | getReportDataByDatePeriod | default last_7_days");
				return masterOrderReportService.getReportDataLast7Days(ReportType.DAY);
		}
	}
	
	@GetMapping("/reports/sales_by_date/{startDate}/{endDate}")
	public List<ReportItemDTO> getReportDataByDatePeriod(@PathVariable("startDate") String startDate,
			@PathVariable("endDate") String endDate) throws ParseException {
		
		LOGGER.info("ReportRestController | getReportDataByDatePeriod is called");
		
		LOGGER.info("ReportRestController | getReportDataByDatePeriod | startDate: " + startDate);
		LOGGER.info("ReportRestController | getReportDataByDatePeriod | endDate: " + endDate);
		
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		Date startTime = dateFormatter.parse(startDate);
		Date endTime = dateFormatter.parse(endDate);
		
		LOGGER.info("ReportRestController | getReportDataByDatePeriod | startDate: " + startDate);
		LOGGER.info("ReportRestController | getReportDataByDatePeriod | endDate: " + endDate);

		return masterOrderReportService.getReportDataByDateRange(startTime, endTime, ReportType.DAY);
	}
}
