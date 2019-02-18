package com.home.expenses.controllers;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class CalculateStatsWeekly implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		log.info("JOB TRIGGERED!");		
	}

	
}
