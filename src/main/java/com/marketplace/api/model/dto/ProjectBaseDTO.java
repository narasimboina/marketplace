package com.marketplace.api.model.dto;


import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
/**
 * 
 * @author Narasim
 *
 */
@Data
public class ProjectBaseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String projectName;
	private String description;
	private Double budjet;
	private Long id;	
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z", timezone = "America/Los_Angeles")
	private Calendar closingDate;

}