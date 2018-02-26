package com.marketplace.api.model.dto;

import java.util.Set;

import com.marketplace.api.model.entity.Bid;

import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 
 * @author Narasim
 *
 */
@Data
@EqualsAndHashCode(callSuper =false)
public class ProjectDTO extends ProjectBaseDTO {

	private double bid;

}