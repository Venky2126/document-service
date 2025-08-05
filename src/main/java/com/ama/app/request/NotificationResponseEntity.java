package com.ama.app.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationResponseEntity {

	private Integer card_id;
	private Boolean salutation;
	private Character first_name_english;
	private String last_name_english;
	private int gender;
	private boolean eng_ref_no;
	private char address1;
	private Double address2;
	private String address3;
	private String address4;
	private String city;
	private String country_of_residence;
	private String embates;
	private String birth_place;
	private String dob;
	private String id_type;
	private String id;
	private String cust_type;
	private String postal_code;
	private String country_code;
	private String email;
	private String mobile;
	private String occupation;
	private String version;
	private String id_expiry;
	private String embossed_name;
	private String company_name;
	private String sanction_screening_status;
	private String middle_name_english;
	private String nationality;
	private String card_type;
	private String visa_expiry_date;
	private String company_embossed_name;
	private String cin_no;
	private String company_reg_no;
	private int card_status;
	private String card_status_desc;

}
