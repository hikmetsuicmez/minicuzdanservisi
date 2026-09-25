package com.hikmetsuicmez.minicuzdanservisi.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAccountRequest(
		
		@NotBlank(message = "Hesap sahibi adı boş veya sadece boşluklardan oluşamaz")
		@Size(min = 2, max = 100, message = "Hesap sahibi adı 2 ile 100 karakter arasında olmalıdır")
		String ownerName		
) {}
