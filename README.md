# Mini Cüzdan Servisi 🚀

Mini Cüzdan Servisi, kişisel finans ve cüzdan yönetim süreçlerini yürütmek için geliştirilmiş bir Spring Boot REST API projesidir.

---

## 🛠️ Teknolojiler

* **Java 21+**
* **Spring Boot 4.x**
* **Spring Data JPA & Hibernate**
* **Flyway** (Veritabanı Migrasyon Yönetimi)
* **PostgreSQL**
* **Docker & Docker Compose**

---

## 🚀 Kurulum ve Çalıştırma

### 1. Gereksinimler
* [Docker Desktop](https://www.docker.com/products/docker-desktop/)'ın bilgisayarınızda yüklü ve çalışır durumda olması gerekir.
* [JDK 21+](https://adoptium.net/) veya üzeri.

### 2. Ortam Değişkenleri (.env)
Proje kök dizininde yer alan `.env.example` dosyasını kopyalayarak `.env` adında yeni bir dosya oluşturun ve veritabanı bilgilerinizi tanımlayın:

```bash
cp .env.example .env
```
### 🐳 Docker Yönetim Adımları


* Veritabanını Arka Planda Başlatma
	* docker compose up -d
	
* Çalışan Konteynerleri Kontrol Etme
	* docker compose ps
	
* Logları İzleme
	* docker compose logs -f postgres
	
* Konteynerleri Durdurma
	* docker compose stop
	
### 💻 Uygulamayı Çalıştırma

* **./mvnw spring-boot:run**
