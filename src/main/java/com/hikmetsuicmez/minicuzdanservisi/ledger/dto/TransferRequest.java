package com.hikmetsuicmez.minicuzdanservisi.ledger.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record TransferRequest(
		
		@NotNull(message = "Gönderen hesap boş olamaz")
		Long senderAccountId,
		@NotNull(message = "Alıcı hesap boş olamaz")
		Long recipientAccountId,
		@NotNull(message = "Tutar alanı boş bırakılamaz.")
		@Positive(message = "Tutar 0'dan büyük bir değer olmalıdır.")
		@Digits(
			integer = 15, // Noktadan önceki maksimum tam sayı basamak sayısı
			fraction = 2, // Noktadan sonraki maksimum küsurat/ondalık (scale) sayısı
			message = "Tutar formatı geçersiz. En fazla 15 basamak tam sayı ve 2 basamak ondalık girebilirsiniz."	
		)
		BigDecimal amount,
		
		@Size(max = 255)
		String description
) {}
